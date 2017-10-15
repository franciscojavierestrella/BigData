package BigData.Algoritmos.ACOC;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

import org.apache.spark.broadcast.Broadcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import BigData.Algoritmos.ACOC.Exception.*;
import BigData.Utilidades.Feromonas;
import BigData.Utilidades.Paciente;


public abstract class Ant<C, D> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 863320284567141110L;

	private static Logger LOG = LoggerFactory.getLogger(Ant.class);
	
	private ArrayList<C> 	solucion 					= new ArrayList<C>(); 		// Lista de clusters
	private Map<C,Boolean> 	componentesVisitados 		= new HashMap<C,Boolean>(); //Lista de Componentes indicando si están visitados
	private Map<C,Integer> 	componentesClusterAsignados = new HashMap<C,Integer>(); //Lista de Componentes al cluster asignado
	
	public  Map<C,D> 		matrizFeromonas 			= new HashMap<C,D>(); 		// Matriz con las Feromonas
	public double 			factorFeromonas = 0;
	public double 			factorHeuristico = 0;
	public int 				numeroDeClusters = 0;

	
	/**
     * Constructor de la clase.
     * 	Desde la matriz de Feromonas, sacamos los componentes a clasificar y construimos el universo
     *  	de trabajo de la hormiga.
     *
     */
	public Ant(){
	}
	
	/**
     * Constructor de la clase.
     * 	Desde la matriz de Feromonas, sacamos los componentes a clasificar y construimos el universo
     *  	de trabajo de la hormiga.
     *
     */
	public void construirDatos(Map<C,D> matrizFeromonas){
		this.matrizFeromonas = matrizFeromonas;
		Iterator<Map.Entry<C, D>> it = matrizFeromonas.entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry<C, D> pair = (Entry<C, D>) it.next();
		    componentesVisitados.put(pair.getKey(), false);
		    componentesClusterAsignados.put(pair.getKey(), 0);
		}
		LOG.debug("Datos contruidos para matrizFeromonas");
	}
	
	/**
     * Código para limpiar a la hormiga.
     *
     */
	public void limpiar(){
		solucion.clear();
		componentesVisitados.clear();
		componentesClusterAsignados.clear();
	}
	
	/**
     * Resuelve el problema asignado a la hormiga.
     *
     */	
	public abstract Ant<C, D> resolver();
	
	/**
     * Indica si la solución es Ok.
     * 	Para ello la hormiga a debido de visitar todos los componentes y debe haber rellenado una
     * 	solución completa con tantos cluster como los indicados.
     *
     */
	public Boolean esSolucionOk(){
		Boolean bRet = true;
		Iterator<Map.Entry<C, Boolean>> it = componentesVisitados.entrySet().iterator();		
		while (it.hasNext()) {
		    Map.Entry<C, Boolean> pair = (Entry<C, Boolean>) it.next();
		    if ( pair.getValue()==false){
		    	bRet = false;
		    	break;
		    }	
		}
		LOG.debug("Soy la hormiga: solucion {} número de cluster {} ", solucion.size(), this.getNumeroClusters());
		return solucion.size() == this.getNumeroClusters() && bRet;
	}
	
	/**
     * Selección del siguiente nodo a visitar.
     * 	Aleatorio, sobre el mapa con los elementos, seleccionamos el siguiente que no 
     * 	este visitado.
     *
     */
	public C seleccionaProximoComponente(){
		C componente = null;		
		Random random= new Random();		
		do {
			int valor = random.nextInt(componentesVisitados.size()), indice = 0;
			Iterator<Map.Entry<C, Boolean>> it = componentesVisitados.entrySet().iterator();
			while (it.hasNext()) {
			    Map.Entry<C, Boolean> pair = (Entry<C, Boolean>) it.next();
			    componente = pair.getKey();
			    if (indice++ == valor){
			    	break;
			    }	
			}
		}while(componentesVisitados.get(componente));
		
		LOG.debug("Seleccionado el próximo Componente {}", componente.toString());
		
		return componente;
	}
	
	/**
     * Algoritmo para seleccionar el Cluster al que asignamos el elemento.
     *
     */
	public C seleccionaClusterComponente(C componente){
		D feromona = matrizFeromonas.get(componente);
		C nuevoCentroide = null;
		
		// Si Solucion vacio es nodo cluster para todos los que queremos calcular.
		if ( solucion.size()==0)
		{
			LOG.debug(" No se dispone de centroides ");
			
			solucion = new ArrayList<C>();
			for(int i=1;i<=this.getNumeroClusters();i++){
				solucion.add(componente);
				componentesClusterAsignados.replace(componente, i);
			}
			componentesVisitados.replace(componente, true);
			nuevoCentroide = componente;
		}
		else{
			LOG.debug(" Cluster al que le asigmaos {}", componente.toString());
			
			// Calculamos la probabilidad del nodo sobre el feronoma con el coeficiente.
			double [] probabilidadFeromonas = 
					getProbabilidadComponenteFeromona(componente, feromona);
			
			// Calculamos la distancia del nodo al cluster con su coeficiente
			double [] probabilidadCentroides =
					getDistanciaComponenteCluster(componente, solucion);
			
			// Chequamos tamaños.
			if ( probabilidadFeromonas.length != probabilidadCentroides.length){
				throw new 
						ConstruirSolucionException ( " Error al seleccionar nuevo Cluster, no coincide centroides y feromonas...");
			}
			
			// Multiplicamos ambas y cogemos la máxima ==> Será el nuevo Cluster.
			int numeroCluster = 1;
			double maxProbabilidad = 0;
			for ( int i=0; i< probabilidadFeromonas.length; i++){
				double valor = probabilidadFeromonas[i] * probabilidadCentroides[i];
				if ( valor > maxProbabilidad){
					maxProbabilidad = valor;
					numeroCluster = i+1;
				}
			}
			
			LOG.debug("seleccionaClusterComponente Cluster al que le asigmaos {}", numeroCluster);
			
			// Marcamos el nodo como vistiado y le asignamos el cluster al que ira.
			componentesVisitados.replace(componente, true);
			componentesClusterAsignados.replace(componente, numeroCluster);
			
			// Calculamos el nuevo centro del cluster
			// Obtenemos los valores del Cluster
			Vector<C> cluster = new Vector<C>();
			Iterator<Map.Entry<C, Integer>> it = componentesClusterAsignados.entrySet().iterator();
			while (it.hasNext()) {
			    Map.Entry<C, Integer> pair = (Entry<C, Integer>) it.next();
			    if ( pair.getValue()==numeroCluster){
			    	cluster.add(pair.getKey());
			    }	
			}
			nuevoCentroide = getNexCentroide(cluster);
			LOG.debug("Nuevo centroide {}", nuevoCentroide.toString());
			//Lo asigmaos a la solución.
			solucion.set(numeroCluster-1, nuevoCentroide);
			
			for(C item: solucion){
				LOG.debug("Solucion obtenida (clusters) {}", item.toString());
			}
		}
		return nuevoCentroide;
	}
	
	/**
     * Obtiene la probabilidad de un componente a partir de las Feromonas.
     *
     */
	public abstract double[] getProbabilidadComponenteFeromona(C componente, D feromona);
	
	/**
     * Obtiene la distancia del componente con respecto el centroide del Cluster.
     *
     */
	public abstract double[] getDistanciaComponenteCluster(C componente, ArrayList<C> centroides);
	
	/**
     * Dado un Cluster de componentes cácula su nuevo centroide.
     *
     */
	public abstract C getNexCentroide(Vector<C> cluster);
	
	/**
     * Calcula la calidad de la solucion de una hormiga.
     *
     */
	public abstract double getCalidadSolucion();
	
	/**
     * Obtiene el el número de Cluster de la solucion
     *
     */
	public abstract int getNumeroClusters();

	/**
     * Set matriz de Feromonas
     *
     */
	public abstract void setMatrizFeromonas(Broadcast<Map<Paciente,Feromonas>> matrizFeromonas);
	
	/**
     * Obtiene la solución encontrada por la hormiga
     *
     */
	public ArrayList<C> getSolucion() {
        return solucion;
    }
    
    /**
     * Obtiene el mapa con los componentes asignados a un cluster.
     *
     */
    public Map<C, Integer> getClusterizados() {
        return componentesClusterAsignados;
    }
   
    /**
     * Set factorFeromonas
     *
     */
	public void setFactorFeromonas(Broadcast<Double> factorFeromonas){
		this.factorFeromonas = factorFeromonas.value();
	}

    /**
     * Set FactorHeuristico
     *
     */
	public void setFactorHeuristico(Broadcast<Double> factorHeuristico){
		this.factorHeuristico = factorHeuristico.value();
	}

    /**
     * Set NumeroClusters
     *
     */
	public void setNumeroClusters(Broadcast<Integer> numeroClusters){
		this.numeroDeClusters = numeroClusters.value();
	}

	/**
     * Get Componentes asignamos a los Clusters
     *
     */
	public Map<C, Integer> getComponentesClusterAsignados() {
		return componentesClusterAsignados;
	}
}
