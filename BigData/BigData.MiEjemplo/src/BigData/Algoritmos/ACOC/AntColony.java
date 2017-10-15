package BigData.Algoritmos.ACOC;

import java.util.*;
import BigData.Utilidades.Configuracion;
import BigData.Utilidades.Feromonas;
import BigData.Utilidades.Paciente;

public abstract class AntColony<C, D> {
	
	protected List<Ant<C,D>>	anthill 		= new ArrayList<Ant<C,D>>();
	private Map<C,D> 			matrizFeromonas = new HashMap<C,D>(); // Matriz con las Feromonas
	
	
	/**
     * Constructor de la clase.
     *
     */
	public AntColony()
	{
	}
	
	/**
     * Limpia la soluci�n en cada iteracion.
     *
     */
	public void limpiarSolucion()
	{
		for(Ant<C, D> ant: anthill){
			ant.limpiar();
		}
	}
	
    /**
     * Desarrolla la soluci�n.
     * @throws Exception 
     *
     */
	public abstract Ant<C,D> construirSolucion() throws Exception;
	
	/**
     * C�digo de inicializaci�n para la colina. �nicamente crea las hormigas.
     *
     */
    public abstract void construirColonia();
	
	/**
     * Inicializa la matriz de Feromonas.
     * 	
     */
	public abstract void inicializarFeromonas();
	
	/**
     * Actualizar la matriz de Feromonas.
     *
     */
	public abstract void actualizarFeromonas(Ant<Paciente, Feromonas> hormiga);
	
	/**
     * Evaporar la matriz de Feromonas.
     *
     */
	public abstract void evaporarFeromonas();
	
	// Accesores a propiedades.
	/**
     * Obtiene el n�mero de hormigas de la colina.
     *
     */
	public int getNumeroHormigas() {
        return Integer.parseInt(Configuracion.getValue("numeroHormigas"));
    }

    /**
     * Obtiene la Colonia de Hormigas.
     *
     */
	public List<Ant<C, D>> getAnthill() {
		return anthill;
	}

	/**
     * Establece una Colonoia de Hormigas.
     *
     */
	/*public void setAnthill(List<Ant<C, D>> anthill) {
		this.anthill = anthill;
	}*/

	/**
     * Obtiene la matriz de Feromonas.
     *
     */
	public Map<C, D> getMatrizFeromonas() {
		return matrizFeromonas;
	}

	/**
     * Establece una nueva Matriz de Feromonas.
     *
     */
	/*public void setMatrizFeromonas(Map<C, D> matrizFeromonas) {
		this.matrizFeromonas = matrizFeromonas;
	}*/
	
	/**
     * Obtiene el n�mero de Clusters.
     *
     */
	public int getNumeroClusters() {
		return Integer.parseInt(Configuracion.getValue("numeroClusters"));
	}
}
