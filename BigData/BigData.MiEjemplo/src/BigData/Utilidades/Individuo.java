package BigData.Utilidades;

public class Individuo {
	
	static int defaultGeneLength = 50;
    private int[] genes = new int[defaultGeneLength];
    // Cache
    private int fitness = 0;

    // Crear un individo de forma aleatoria
    public void generateIndividual() {
        for (int i = 0; i < size(); i++) {
            int gene = (int) Math.round(Math.random());
            genes[i] = gene;
        }
    }

    /* Getters and setters */
    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = length;
    }
    
    public int getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, int value) {
        genes[index] = value;
        fitness = 0;
    }

    /* Public methods */
    public int size() {
        return genes.length;
    }

    public int getFitness() {
        if (fitness == 0) {
            fitness = FitnessCalc.getFitness(this);
        }
        return fitness;
    }

    @Override
    public String toString() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            geneString += getGene(i);
        }
        return geneString;
    }

}
