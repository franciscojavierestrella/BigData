package BigData.Algoritmos.ACOC;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.broadcast.Broadcast;

import BigData.Algoritmos.ACOC.Exception.ConfiguracionException;
import BigData.Utilidades.*;
import BigData.Utilidades.Spark.FachadaSpark;

public class AntColonyForPacientes extends AntColony<Paciente,Feromonas> implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 601273590251230175L;
	private static final Logger LOG = LoggerFactory.getLogger(AntColonyForPacientes.class);

	/**
     * Constructor de la clase
     *
     */
	public AntColonyForPacientes() {
		super();		
	}
	
	/**
     * Código de inicialización para la Colonia.
     *
     */
	@Override
    public void construirColonia() {
        for (int j = 0; j < getNumeroHormigas(); j++) {
        	getAnthill().add(new AntForPacientes());
        }
    }

	/**
     * Código de inicialización de la feromona.
     *
     */
	@Override
	public void inicializarFeromonas() {		
		loadPacientesFeromonas();		
	}
	
	/**
     * Desarrolla la solución.
     * @throws Exception 
     *
     */
	@Override
	public Ant<Paciente,Feromonas> construirSolucion() throws Exception
	{
		Ant<Paciente, Feromonas> hormiga = null;
		
		if (anthill.size()==0) {
			throw new ConfiguracionException(" No se dispone de colina de hormigas ");
		}

		if ( Boolean.parseBoolean(Configuracion.getValue("ParalelizarSolucion")) == false )
		{
			LOG.info("Sobre la colonia ponemos a trabajar a cada hormiga. ");
			for(Ant<Paciente, Feromonas> ant: anthill){
				
				ant.numeroDeClusters = Integer.parseInt(Configuracion.getValue("numeroClusters"));
				ant.factorFeromonas = Double.parseDouble(Configuracion.getValue("factorFeromonas"));
				ant.factorHeuristico = Double.parseDouble(Configuracion.getValue("factorHeuristico"));
				ant.matrizFeromonas = getMatrizFeromonas();
				
				Ant<Paciente, Feromonas> nuevahormiga = ant.resolver();
				if ( hormiga == null ){
					hormiga = nuevahormiga;
				}
				else{
					if ( nuevahormiga.getCalidadSolucion() < hormiga.getCalidadSolucion()){
						hormiga = nuevahormiga;
					}
				}	
				LOG.info("Terminada iteración soy la hormiga {} \n", ant.toString());
			}
			LOG.info("Una vez tenemos la mejor hormiga actulizamos las feromonas");
			
			actualizarFeromonas(hormiga);
			evaporarFeromonas();
		}
		else{
			Broadcast<Double> factorFeromonas = (Broadcast<Double>) FachadaSpark.getSc().broadcast(Double.parseDouble(Configuracion.getValue("factorFeromonas")));
			Broadcast<Double> factorHeuristico = (Broadcast<Double>) FachadaSpark.getSc().broadcast(Double.parseDouble(Configuracion.getValue("factorHeuristico")));
			Broadcast<Integer> numeroClusters = (Broadcast<Integer>) FachadaSpark.getSc().broadcast(Integer.parseInt(Configuracion.getValue("numeroClusters")));
			Broadcast<Map<Paciente,Feromonas>> matrizFeromonasBrd = (Broadcast<Map<Paciente,Feromonas>>) FachadaSpark.getSc().broadcast(getMatrizFeromonas());
			
			
			for(Ant<Paciente, Feromonas> item: anthill){
				item.setMatrizFeromonas(matrizFeromonasBrd);
				item.setFactorFeromonas(factorFeromonas);
				item.setFactorHeuristico(factorHeuristico);
				item.setNumeroClusters(numeroClusters);
			}
			
			LOG.info("Paralelizamos sobre los workers cada una de las hormigas. ");
			
			JavaRDD<Ant<Paciente, Feromonas>> dataSet = FachadaSpark.getSc().parallelize(anthill, Integer.parseInt(Configuracion.getValue("Particiones")));
			
			hormiga = dataSet.map(Ant<Paciente, Feromonas>::resolver).repartition(Integer.parseInt(Configuracion.getValue("Particiones"))).reduce(AntForPacientes::combine);
		
			LOG.info("Una vez combinados todos los resultados, procedemos a actualizar y Evaporar las Feromonas. ");
			
			actualizarFeromonas(hormiga);
			evaporarFeromonas();
			
			// Destruimos la matriz para que se vuelva a crear en la siguiente Iteracion
			matrizFeromonasBrd.destroy();
		}
		
		// Devolvemos la mejor hormiga de la iteración, luego sobre ella vamos calculando la mejor solución.
		return hormiga;
	}
		
	/**
     * Actualizar la matriz de Feromonas.
     *
     */
	@Override
	public void actualizarFeromonas(Ant<Paciente, Feromonas> hormiga){
		double calidadSolucion = hormiga.getCalidadSolucion();
		
		LOG.debug(" La calidad de la solución es {}", calidadSolucion);		
		// Actualizamos la feromonas de cada Paciente
		Iterator<Map.Entry<Paciente, Feromonas>> it = getMatrizFeromonas().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry<Paciente, Feromonas> pair = (Entry<Paciente, Feromonas>) it.next();
		    Feromonas feromona = pair.getValue();		
		    LOG.debug(" Feromonas obtenidas {}", feromona.toString());
		    feromona.actualizarFeromonas(calidadSolucion);		    
		    LOG.debug(" Feromonas actualizadas {}", feromona.toString());
		    pair.setValue(feromona);
		}		
	};
	
	/**
     * Evaporar la matriz de Feromonas.
     *
     */
	@Override
	public void evaporarFeromonas(){
		// Evaporamos las feromonas
		Iterator<Map.Entry<Paciente, Feromonas>> it = getMatrizFeromonas().entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry<Paciente, Feromonas> pair = (Entry<Paciente, Feromonas>) it.next();
		    Feromonas feromona = pair.getValue();
		    LOG.debug(" Evaporamos Feromonas {}", feromona.toString());
		    feromona.evaporarFeromonas(Double.parseDouble(Configuracion.getValue("factorEvaporacionFeromonas")));
		    LOG.debug(" Feromona evaporada {}", feromona.toString());
		    pair.setValue(feromona);
		}		
	};
	
	/**
     * Cargamos los pacientes desde el fichero de datos junto con sus feromonas
     *
     */
	private void loadPacientesFeromonas() {		
		String csvFile = Configuracion.getValue("ficherodatos");
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = Configuracion.getValue("separadorficherodatos");
		
		try {
		    br = new BufferedReader(new FileReader(csvFile));
		    while ((line = br.readLine()) != null) {                
		        String[] datos = line.split(cvsSplitBy);
		        // Construimos el Paciente con los datos leidos
		        Paciente paciente = new Paciente(datos.length);
		        for(int i=0; i < datos.length; i++){
		        	double valor = 0;
		        	try{
		        		valor = Double.parseDouble(datos[i]);
		        	}catch (NumberFormatException ex){
		        		valor = 0;
		        	}		        	
		        	paciente.setGene(i, valor);
		        }
				
		        Feromonas feromona = new Feromonas(getNumeroClusters());
				getMatrizFeromonas().put(paciente, feromona);
				//LOG.info("Cargamos los datos del paciente {}) con las feromonas {}", paciente.toString(), feromona.toString());		        
		    }
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (br != null) {
		        try {
		            br.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}		
	}
}
