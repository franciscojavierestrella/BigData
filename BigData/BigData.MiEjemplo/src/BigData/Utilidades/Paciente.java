package BigData.Utilidades;

import java.io.Serializable;

import BigData.Algoritmos.ACOC.Exception.ConstruirSolucionException;

public class Paciente implements Serializable {
	
	private static final long serialVersionUID = -7863293541480909490L;
	
	// Propiedades del Paciente, su Genoma.
	static int defaultGeneLength = 0;
    private double[] genes = null;
    
    public Paciente(int defaultGeneLength)
    {
    	Paciente.defaultGeneLength = defaultGeneLength;
    	genes = new double[Paciente.defaultGeneLength];
    	this.generateIndividual();    	
    }
    
    // Establecemos los genes de un Inidividuo.
    public void setGenes(int[] nuevosGenes) throws Exception {
    	if ( genes.length != nuevosGenes.length )
    		throw new Exception();
    		
        for (int i = 0; i < size(); i++) {
            genes[i] = nuevosGenes[i];
        }
    }
    
    // Crear un individo de forma aleatoria
    public void generateIndividual() {
        for (int i = 0; i < size(); i++) {
            int gene = randomWithRange(0,2);
            genes[i] = gene;
        }
    }
    
    /**
     * Calculo de numeros aleatorios sobre un rango.
     *
     */
	private static int randomWithRange(int min, int max)
	{
	   int range = Math.abs(max - min);     
	   return (int) ((Math.random() * range) + (min <= max ? min : max));
	}
    
    /* Getters y setters */
    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = length;
    }
    
    public static int getDefaultGeneLength() {
        return Paciente.defaultGeneLength;
    }

    
    public double getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, double value) {
        genes[index] = value;
    }

    /* Métodos Públicos */
    public int size() {
        return genes.length;
    }

    @Override
    public String toString() {
        String geneString = "Paciente: ";
        for (int i = 0; i < size(); i++) {
            geneString += getGene(i);
            geneString += " ";
        }
        return geneString;
    }
    
	/**
     * Cálcula la distancia Euclidia entre dos pacientes.
     *
     */
	public double distanciaEuclideana( Paciente centroide) {
        double resultado=0;
        if(this.size()!=centroide.size()) {
            throw new ConstruirSolucionException( " Error de tamaños al calcular la distancia Euclidia entre pacientes "+ this.size() + " " + centroide.size());
        }
        else {
            double sumatoriaDiferencias = 0;
            for(int i=0; i<this.size(); i++) {
                double diferencia = this.getGene(i)-centroide.getGene(i);
                sumatoriaDiferencias+= Math.pow(diferencia, 2);
            }
            resultado = Math.sqrt(sumatoriaDiferencias);
        }        
        return resultado;
	}


}
