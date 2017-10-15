package BigData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import BigData.Algoritmos.*;
import BigData.Utilidades.Configuracion;
import BigData.Utilidades.FitnessCalc;
import BigData.Utilidades.Poblacion;

public class Ejecutor {
	
	private static final Logger LOG = LoggerFactory.getLogger(AlgoritmoACOC.class);
	
	public static Map getDiferencia(java.util.Date fecha1, java.util.Date fecha2){
	   java.util.Date fechaMayor = null;
	   java.util.Date fechaMenor = null;
	   Map resultadoMap = new HashMap();
	 
	   /* Verificamos cual es la mayor de las dos fechas, para no tener sorpresas al momento
	    * de realizar la resta.
	    */
	   if (fecha1.compareTo(fecha2) > 0){
	    fechaMayor = fecha1;
	    fechaMenor = fecha2;
	   }else{
	    fechaMayor = fecha2;
	    fechaMenor = fecha1;
	   }
	 
	  //los milisegundos
	   long diferenciaMils = fechaMayor.getTime() - fechaMenor.getTime();
	 
	   //obtenemos los segundos
	   long segundos = diferenciaMils / 1000;
	 
	   //obtenemos las horas
	   long horas = segundos / 3600;
	 
	   //restamos las horas para continuar con minutos
	   segundos -= horas*3600;
	 
	   //igual que el paso anterior
	   long minutos = segundos /60;
	   segundos -= minutos*60;
	 
	   //ponemos los resultados en un mapa :-)
	   resultadoMap.put("horas",Long.toString(horas));
	   resultadoMap.put("minutos",Long.toString(minutos));
	   resultadoMap.put("segundos",Long.toString(segundos));
	   return resultadoMap;
	}
	
	public static void main(String[] args) {

		String KMeansCluster = "AppKMeansCluster";
		String KMeansClasificar = "AppKMeansClasificar";
		String Genetic = "AppGA";
		String GeneticACOC = "AppACOC";

		// Variables Generales, por defecto asignamos valores nulos
		String appName = "";
	    int numClusters = 0;
	    int numIterations = 0;
	    String path = "";
	    String ficheroSalida = "";
	    boolean modeloPredictivo = false;
	    String ficheroModelo = "";
	    String ficheroConfig = "";
	    new SimpleDateFormat("HH:mm:ss yyyy/MM/dd");
	    Date dateBegin = new Date(), dateEnd = null;
	    
	    // Primer tipo de Algoritmo ==> KMeans
	    if ( args[0].toString().compareTo(KMeansCluster)==0)
	    {   
	    	//Recogemos los argumentos.
		    if ( args.length == 2){
		    	ficheroConfig = args[1];
		    	Configuracion.getConfiguracion();
		    	Configuracion.leerConfiguracio(args[1]);
		    	
		    	appName = Configuracion.getValue("AppName");
		    	path = Configuracion.getValue("ficherodatos");
		    	numClusters = Integer.parseInt(Configuracion.getValue("numeroClusters"));
		    	numIterations = Integer.parseInt(Configuracion.getValue("numeroIteraciones"));
		    	ficheroModelo = Configuracion.getValue("ficheroModelo");;
		    	modeloPredictivo = Boolean.parseBoolean(Configuracion.getValue("ModeloPredictivo"));
		    }
		    else{
				  System.out.println(" Error en parametros ");
				  System.err.println(" Error en los parametros....");		    	
			}
		    // Creamos el objeto que nos da soporte.
		    AlgoritmoKMeans algoritmo = new AlgoritmoKMeans(appName, numClusters, numIterations, path, ficheroModelo, modeloPredictivo);
		    
		    //Ejecutamos el algotirmo.
		    algoritmo.EjecutarAlgoritmo();
		    
		    dateEnd = new Date();
	    }
	    
	    // Algoritmo KMeans ==> Predicimos unos individos
	    if ( args[0].toString().compareTo(KMeansClasificar)==0)
	    {   
		    //Recogemos los argumentos.
		    if (args.length == 4 )
		    {
		    	appName = args[0];
		    	path = args[1];
		    	ficheroSalida = args[2];
		    	ficheroModelo = args[3];
		    }
		    else{
			  System.out.println(" Error en parametros ");
			  System.err.println(" Error en los parametros....");
		    }	
		    
		    AlgoritmoKMeans algoritmo = new AlgoritmoKMeans(appName, path, ficheroSalida, ficheroModelo);
		    
		    algoritmo.PredecirAlgoritmo();
		    
		    dateEnd = new Date();
	    }
	    
	    // Algoritmo Genetico ==> Predicimos unos individos
	    if ( args[0].toString().compareTo(Genetic)==0)
	    {
	    	FitnessCalc.setSolution("1111000030002000000043000000040000000000000230000");
	    	Poblacion myPop = new Poblacion(50,true);
	    	
	    	int generationCount = 0; 
	    	while(myPop.getFittest().getFitness() < FitnessCalc.getMaxFitness()){ 
	    	  generationCount++; 
	    	  System.out.println("Generation: "+generationCount+" Fittest: "+myPop.getFittest().getFitness()); 
	    	  myPop = AlgoritmoGA.evolvePopulation(myPop); 
	    	} 
	    	System.out.println("Solution found!"); 
	    	System.out.println("Generation: "+generationCount); 
	    	System.out.println("Genes:"); 
	    	System.out.println(myPop.getFittest());
	    	
	    	dateEnd = new Date();
	    }
	    
	    // Algoritmo ACOC ==> Clasificamos un conjunto de Individuos
	    if ( args[0].toString().compareTo(GeneticACOC)==0)
	    {
	    	//Recogemos los argumentos.
		    if (args.length == 2 )
		    {
		    	ficheroConfig = args[1];
		    	
		    	// Creamos el objeto que nos da soporte.
			    AlgoritmoACOC algoritmoACOC = new AlgoritmoACOC(ficheroConfig);
			    try {
					algoritmoACOC.resolverProblema();
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		    else{
				  System.out.println(" Error en parametros ");
				  System.err.println(" Error en los parametros....");
		    }	
		    
		    dateEnd = new Date();
	    }
	    
	    LOG.info(getDiferencia(dateEnd, dateBegin).toString());
	}

}
