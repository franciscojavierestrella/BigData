package BigData.Utilidades;

import java.io.Serializable;

public class Feromonas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5985194827878889966L;
	// Propiedades del Paciente, su Genoma.
	static int defaultClusters = 0;
	double[] feromona = null;
	
	public Feromonas(int defaultClusters)
    {
		Feromonas.defaultClusters = defaultClusters;
    	this.inicializarFeromonas(defaultClusters);    	
    }

	/**
     * Inicializamos los valores de los Cluster para una Feromona.
     *
     */
	public void inicializarFeromonas(int clusters){
		Feromonas.defaultClusters = clusters;		
		feromona = new double[Feromonas.defaultClusters];
		for ( int i=0; i < feromona.length ; i++ ){
			feromona[i] = randomWithRange(0, 1);
		}	
	}
	
	/**
     * Cálculo de numeros aleatorios sobre un rango.
     *
     */
	private double randomWithRange(double min, double max)
	{
	   double range = Math.abs(max - min);     
	   return (Math.random() * range) + (min <= max ? min : max);
	}
	
	/**
     * Obtenemos las Feromonas Cluster.
     *
     */
	public double[] getFeromonas(){
		return feromona;
	}
	
	/**
     * Obtenemos la feromona.
     *
     */
	public double getFeromona(int index) {
        return feromona[index];
    }
	
	/**
     * Obtenemos la dimensión del Cluster para una Feromona.
     *
     */
	public int getClustersFeromonas(){
		return defaultClusters;
	}
	
	/**
     * Acualizamos las feromonas del cluster con el valor de calidad de solucion para el paciente.
     *
     */
	public void actualizarFeromonas(double valor){
		for ( int i=0; i < feromona.length ; i++ ){
			feromona[i] = feromona[i] + (1 / valor);
		};
	}
	/**
     * Acualizamos las feromonas del cluster con el valor de calidad de solucion para el paciente.
     *
     */
	public void evaporarFeromonas(double p){
		for ( int i=0; i < feromona.length ; i++ ){
			feromona[i] = (1-p) * feromona[i];
		};
	}
	
	public int size() {
        return feromona.length;
    }
	
	@Override
    public String toString() {
        String geneString = "Feromonas: ";
        for (int i = 0; i < size(); i++) {
            geneString += getFeromona(i);
            geneString += " ";
        }
        return geneString;
    }
}
