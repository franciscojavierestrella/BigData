package BigData.Utilidades.Spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import BigData.Utilidades.Configuracion;

public final class FachadaSpark {
    private static final Logger LOG = LoggerFactory.getLogger(FachadaSpark.class);

    private static SparkConf config;
    private static JavaSparkContext sc;
    
    /**
     * Constructor de la clase
     */
    public static void getFachadaSpark(String appName) {
        LOG.info("Arrancado Spark {} ", appName);
        FachadaSpark.config = new SparkConf().setAppName(appName).set("spark.cores.max", Configuracion.getValue("MaxCores"));
    }

    /**
     * Inicializa el contexto de Spark
     */
    public static void init() {
    	FachadaSpark.sc = new JavaSparkContext(config);
    }

    /**
     * Para el Contexto de Spark.
     */
    public static void stop() {
    	FachadaSpark.sc.close();
    }
    
    /**
     * Devuelve el contexto de Spark para poder usuarlo
     */
    public static JavaSparkContext getSc() {
		return FachadaSpark.sc;
	}
    
    /*public Paciente solveFor() {
        LOG.info("Parallelism: {}", sc.defaultParallelism());

        //Work work = Work.forCities(cities, maxDuration);
        //double start = work.shortest().getDistance();

        for (int i = 0; i < iterations; i++) {
            double iterStart = work.shortest().getDistance();
            JavaRDD<Work> dataSet = sc.parallelize(work.fork(sc.defaultParallelism()));

            work = dataSet.map(Work::solve).reduce(Work::combine);

            LOG.info("Iteration {} result: {}", i, formatDistance(work.shortest().getDistance()));
            if (iterStart == work.shortest().getDistance()) {
                LOG.info("No change; terminating.");
                break;
            }
        }

        double result = work.shortest().getDistance();
        String percent = String.format(Locale.ROOT, "%.2f", result / start * 100.0);

        LOG.info("Final result: {} -> {} ({}%)", formatDistance(start), formatDistance(result), percent);

        return work.shortest();
    }*/
}
