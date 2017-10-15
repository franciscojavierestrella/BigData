package BigData.Algoritmos.ACOC;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

import org.apache.spark.broadcast.Broadcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import BigData.Algoritmos.ACOC.Exception.*;
import BigData.Utilidades.*;

public class AntForPacientes extends Ant<Paciente,Feromonas> implements Serializable {

	private static final long serialVersionUID = -7068551563547671602L;
	private static Logger LOG = LoggerFactory.getLogger(AntForPacientes.class);
	
	/**
     * Constructor de Hormigas.
     *
     */
	public AntForPacientes(){
		super();
	}

	/**
     * Probabilidad del componente asociado a la Feromona.
     *
     */
	@Override
	public double[] getProbabilidadComponenteFeromona(Paciente componente, Feromonas feromona) {
		
		LOG.debug(" Componente de trabajo {} con las fermonas asociadas {} ", componente.toString(), feromona.toString());
		double[] propiedades = feromona.getFeromonas();
		for ( int i=0; i < propiedades.length ; i++ ){
			propiedades[i] = propiedades[i] * factorFeromonas;
		}
		return propiedades;
	}

	/**
     * Distancia del componente con el centroide.
     * 	Aplicacirá el factor de correción del Heuristico.
     *
     */
	@Override
	public double[] getDistanciaComponenteCluster(Paciente componente, ArrayList<Paciente> centroides) {		
		double[] propiedades = new double[this.getNumeroClusters()];
		int indice = 0;
		for(Paciente centroide: centroides){
			propiedades[indice++] = distanciaEuclideana(componente, centroide) * factorHeuristico;			
		}		
		return propiedades;
	}

	/**
     * Calcula el nuevo centroide.
     *
     */
	@Override
	public Paciente getNexCentroide(Vector<Paciente> cluster) {
		LOG.debug("Soy la hormiga: {} y creo el nuevo centroide: {} ", toString(), cluster.firstElement().size());
		Paciente centroide = new Paciente(cluster.firstElement().size());
		double promedio, suma = 0;		
		for (int i = 0; i < cluster.firstElement().size(); i++){
			for(Paciente paciente: cluster){
				suma+=paciente.getGene(i);
			}
			promedio = suma/cluster.size();
			centroide.setGene(i,promedio);
			suma = 0;
		}
		LOG.info("Soy la hormiga: {} y el nuevo centroide es: {} ", toString(), centroide.toString());
		return centroide;
	}
	
	/**
     * Calcula la calidad de la solucion de una hormiga.
     * 	Para ello, revisa todos los nodos asignados a cada cluster sumando la distancia eculidia 
     * 	del nodo al centroide del cluster.
     * 	Tendrá mejor calidad la que todos sus nodos estén más cerca del Centroide
     *
     */
	@Override
	public double getCalidadSolucion() {
		double suma = 0;
		int numeroCluster = 1;
		for( ; numeroCluster <= this.getNumeroClusters();numeroCluster++ ){
			Iterator<Map.Entry<Paciente, Integer>> it = getClusterizados().entrySet().iterator();
			while (it.hasNext()) {
			    Map.Entry<Paciente, Integer> pair = (Entry<Paciente, Integer>) it.next();
			    if ( pair.getValue()==numeroCluster){
			    	suma += distanciaEuclideana(pair.getKey(), this.getSolucion().get(numeroCluster-1));
			    }
			}
		}
		LOG.debug("Soy la hormiga: {} con calidad de solucion: {} ", toString(), suma);
		return suma;
	}
	
	/**
     * Cálcula la distancia Euclidia entre dos pacientes.
     *
     */
	private double distanciaEuclideana(Paciente paciente, Paciente centroide) {
        double resultado=0;
        if(paciente.size()!=centroide.size()) {
            throw new ConstruirSolucionException( " Error de tamaños al calcular la distancia Euclidia entre pacientes "+ paciente.size() + " " + centroide.size());
        }
        else {
            double sumatoriaDiferencias = 0;
            for(int i=0; i<paciente.size(); i++) {
                double diferencia = paciente.getGene(i)-centroide.getGene(i);
                sumatoriaDiferencias+= Math.pow(diferencia, 2);
            }
            resultado = Math.sqrt(sumatoriaDiferencias);
        }        
        LOG.debug("Soy la hormiga: {} con distancia eculidia: {} con resultado)", toString(), centroide.toString(), resultado);
        return resultado;
	}
	
	/**
     * Resuelve el problema asignado a la hormiga.
     *
     */	
	@Override
	public Ant<Paciente, Feromonas> resolver(){
		construirDatos(matrizFeromonas);
		LOG.debug("Soy la hormiga: {} los datos ya están construidos ", toString());
		while (!esSolucionOk()){
			LOG.debug("Soy la hormiga: {} y recorro todos los componentes ", toString());
			Paciente componente = seleccionaProximoComponente();
			Paciente centroide = seleccionaClusterComponente(componente);
			LOG.debug("Soy la hormiga: {} y he localizado el centroide: {})", toString(), centroide.toString());
		}
		return this;
	}
    
    /**
     * Usado en las operaciones de reduce para la combinación de dos hormigas sacando su mejor resultado
     *
     * @param work1 primer object
     * @param work2 segundo object
     * @return a new Work object que contiene el mejor resultado
     */
	public static Ant<Paciente, Feromonas> combine(Ant<Paciente, Feromonas> work1, Ant<Paciente, Feromonas> work2) {
		LOG.info("Soy una hormiga y me toca combinar las soluciones work1 {} work2 {}", work1.toString(), work2.toString());
    	AntForPacientes mejorHormiga = (AntForPacientes) work1;
    	double calidad1 = work1.getCalidadSolucion(), calidad2 = work2.getCalidadSolucion();
    	if ( work1.getCalidadSolucion() > work2.getCalidadSolucion()){
    		mejorHormiga = (AntForPacientes) work2;
    	}
    	LOG.info("La mejor solución corresponde a la hormiga {} calidad {} con respecto {} calidad {}", work1.toString(), calidad1, work2.toString(), calidad2);
		return mejorHormiga;    
	};
	
	/**
     * Obtiene el el número de Cluster de la solucion
     *
     */
	@Override
	public int getNumeroClusters(){		
		return numeroDeClusters;
	}

	/**
     * Set matriz de Feromonas
     *
     */
	@Override
	public void setMatrizFeromonas(Broadcast<Map<Paciente,Feromonas>> matrizFeromonas){
		this.matrizFeromonas = matrizFeromonas.value();
	}


}
