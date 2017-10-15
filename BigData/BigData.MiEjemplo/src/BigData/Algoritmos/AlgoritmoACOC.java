package BigData.Algoritmos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import BigData.Algoritmos.ACOC.*;
import BigData.Utilidades.Configuracion;
import BigData.Utilidades.Feromonas;
import BigData.Utilidades.Paciente;
import BigData.Utilidades.Spark.FachadaSpark;

public class AlgoritmoACOC {
	
	private static final Logger LOG = LoggerFactory.getLogger(AlgoritmoACOC.class);
	
	private static AntColony<Paciente,Feromonas> 	colonia = null;
	private String 									config = "";
	
	private AntForPacientes  						mejorHormiga; // La usamos para recoger la mejor hormiga de cada iteración y calcular la mejor solución entre iteraciones.
	private ArrayList<Paciente> 					mejorSolucion = null;
    private String 									mejorSolucionString = "";
	
	/**
     * Constructor de la clase.
     *
     */
	public AlgoritmoACOC(String config)
	{
		this.config = config;
	}
	
	/**
     * Código que resuelve el problema.
	 * @throws Exception 
     *
     */
	public void resolverProblema() throws Exception
	{
		AntForPacientes mejorHormigaIteracion = null;
		Configuracion.leerConfiguracio(config);
		
		if ( Boolean.parseBoolean(Configuracion.getValue("ParalelizarSolucion")) == true )
		{
			FachadaSpark.getFachadaSpark(Configuracion.getValue("AppName"));
			FachadaSpark.init();
		}
		
		colonia = new AntColonyForPacientes();
		colonia.inicializarFeromonas();
		colonia.construirColonia();
		
		int iteraciones = 0;
		while (iteraciones < Integer.parseInt(Configuracion.getValue("numeroIteraciones")))
		{
			LOG.info("Iteracion {} de un total de {}", iteraciones, Integer.parseInt(Configuracion.getValue("numeroIteraciones")));
			colonia.limpiarSolucion();			
			mejorHormigaIteracion = (AntForPacientes) colonia.construirSolucion();			
			actualizarMejorSolucion(mejorHormigaIteracion);			
			iteraciones++;
		}
		
		LOG.info("Mostramos la solución calculada");
		for ( Paciente paciente : getMejorSolucion()){
			LOG.info("Solución ==> Centroide calculado: {}", paciente.toString());			
		}
		if ( Boolean.parseBoolean(Configuracion.getValue("ParalelizarSolucion")) == true )
		{
			FachadaSpark.stop();
		}
		
		LOG.info("Calidad de la Solución ==> {}", calidadSolucion(mejorHormigaIteracion.getComponentesClusterAsignados()));
	}
	
	/**
     * La calidad de la solución se cálcula como la distancia de todos los objetos con respecto su
     *   cluster, invertiendo este resultado como sumatorio para todos los cluster normalizados.
     *
     */
	public double calidadSolucion(Map<Paciente,Integer> componentesClusterAsignados){
		int topeClusters = Integer.parseInt(Configuracion.getValue("numeroClusters"));
		double suma = 0, calidadSolucion = 0;
		int numeroCluster = 1;
		for( ; numeroCluster <= topeClusters;numeroCluster++ ){
			Iterator<Map.Entry<Paciente, Integer>> it = componentesClusterAsignados.entrySet().iterator();
			while (it.hasNext()) {
			    Map.Entry<Paciente, Integer> pair = (Entry<Paciente, Integer>) it.next();
			    if ( pair.getValue()==numeroCluster){
			    	suma += pair.getKey().distanciaEuclideana(getMejorSolucion().get(numeroCluster-1));
			    }
			}
		}
		calidadSolucion = 1/suma;
		return calidadSolucion;
	}
	
	/**
     * Actualiza la mejor de la solución.
     *
     */
	public void actualizarMejorSolucion(AntForPacientes mejorHormigaIteracion){
		if (mejorHormiga==null){
			mejorHormiga = mejorHormigaIteracion;
		}
		else{
			double mejorCoste = mejorHormiga.getCalidadSolucion();
			double calidadSolucion =  mejorHormigaIteracion.getCalidadSolucion();			
			if ( mejorCoste > calidadSolucion){
				mejorHormiga = mejorHormigaIteracion;
			}
		}
		mejorSolucion = mejorHormiga.getSolucion();
    }
	
	// Acccesores
	/**
     * Obtiene la mejor Solucion, el cluster con los datos.
     *
     */
	public ArrayList<Paciente> getMejorSolucion() {
        return mejorSolucion;
    }
	
	/**
     * Establece la mejor solución encontrada por las hormigas
     *
     */
	public void setMejorSolucion(ArrayList<Paciente> mejorSolucion) {
        this.mejorSolucion = mejorSolucion;
    }
}
