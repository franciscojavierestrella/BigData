package BigData.Algoritmos;


import BigData.Algoritmos.ACOC.Ant;
import BigData.Utilidades.*;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class AlgoritmoKMeans {
	
	private static Logger LOG = LoggerFactory.getLogger(Ant.class);

    // Variables Generales, por defecto asignamos valores nulos
	private String appName = "";
	private int numClusters = 0;
	private int numIterations = 0;
	private String path = "";
	private String ficheroSalida = "";
	private String ficheroModelo = "";
	private boolean modeloPredictivo = false;

	public AlgoritmoKMeans( String appName, 
							int numClusters, 
							int numIterations, 
							String path,
							String ficheroModelo,
							boolean modeloPredictivo)
	{
		this.appName = appName;
		this.numClusters = numClusters;
		this.numIterations = numIterations;
		this.path = path;
		this.ficheroModelo = ficheroModelo;
		this.modeloPredictivo = modeloPredictivo;
	}
	
	public AlgoritmoKMeans( String appName, 
							String path, 
							String ficheroSalida,
							String ficheroModelo)
	{
		this.appName = appName;
		this.path = path;
		this.ficheroSalida = ficheroSalida;
		this.ficheroModelo = ficheroModelo;
	}
	
	
	public void EjecutarAlgoritmo()
	{
	    SparkConf conf = new SparkConf().setAppName(appName);
	    JavaSparkContext jsc = new JavaSparkContext(conf);

	    // Cargamos los datos y los parsaeamos
	    JavaRDD<String> data = jsc.textFile(path);

	    @SuppressWarnings("resource")
		JavaRDD<Vector> parsedData = data.map(s -> {
	    	String[] sarray = s.split(";");
	    	double[] values = new double[sarray.length];
	    	for (int i = 0; i < sarray.length; i++) {
	    		if (sarray[i].trim().isEmpty())
	    			values[i] = 0;
	    		else
	    			values[i] = Double.parseDouble(sarray[i].trim());    	 
	    	}
	    	return Vectors.dense(values);
	    });
	    parsedData.cache();

	    // Modelamos los datos en función del número de Cluster e Iteraciones definidas
	    KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, numIterations, 12, "k-means||");
	    
	    //Empezamos a trazas
	    LOG.info("---- Cluster centers: ");			
	    
	    for (Vector center: clusters.clusterCenters()) {
	    	LOG.info("----- Datos localizados -----");
	    	LOG.info(" Cluster: {}", center.toString());
	    	LOG.info("----- Datos localizados -----");
	    }
	    
	    double cost = clusters.computeCost(parsedData.rdd());
	    LOG.info("----- Cost= {} ", cost);

	    // Evaluamos el computo de los erroress
	    double WSSSE = clusters.computeCost(parsedData.rdd());
	    LOG.info("------ ErrorsCuadraticos = {} ", WSSSE);
	    
	    if ( modeloPredictivo){
		    // Salvamos el modelo para futuras predicciones
		    clusters.save(jsc.sc(), ficheroModelo);	    	
	    }
	    jsc.stop();
	    jsc.close();
	}
	
	public void PredecirAlgoritmo()
	{
		Salida salida = new Salida(ficheroSalida);
	    
	    SparkConf conf = new SparkConf().setAppName(appName);
	    JavaSparkContext jsc = new JavaSparkContext(conf);

	    // Cargamos los datos y los parsaeamos
	    JavaRDD<String> data = jsc.textFile(path);
	    
	    //Empezamos a trazas
	    salida.AbrirFichero("---- Predecimos el cluster:");
	    
	    @SuppressWarnings("resource")
		JavaRDD<Vector> parsedData = data.map(s -> {
	    	String[] sarray = s.split(";");
	    	double[] values = new double[sarray.length];
	    	for (int i = 0; i < sarray.length; i++) {
	    		if (sarray[i].trim().isEmpty())
	    			values[i] = 0;
	    		else
	    			values[i] = Double.parseDouble(sarray[i].trim());    	 
	    	}
	    	return Vectors.dense(values);
	    });
	    parsedData.cache();
	    
	    KMeansModel modelo = KMeansModel.load(jsc.sc(), ficheroModelo );
	    
	    JavaRDD<Integer> resultadoData = modelo.predict(parsedData);
	    
	    for (Integer cluster: resultadoData.collect()) {
	    	salida.EscribirFichero("----- Cluster localizados -----");
	    	salida.EscribirFichero(" Los datos se localizan en el cluster " + cluster.toString());
	    	salida.EscribirFichero("-----------------------------");
	    }
	    
	    jsc.stop();
	    jsc.close();
	
	}
}


