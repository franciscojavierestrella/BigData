package BigData.Utilidades;

public class Poblacion {
	
	Individuo[] individuals;

    /*
     * Constructors
     */
    // Create a population
    public Poblacion(int populationSize, boolean initialise) {
        individuals = new Individuo[populationSize];
        // Initialise population
        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < size(); i++) {
                Individuo newIndividual = new Individuo();
                newIndividual.generateIndividual();
                saveIndividual(i, newIndividual);
            }
        }
    }

    /* Getters */
    public Individuo getIndividual(int index) {
        return individuals[index];
    }

    public Individuo getFittest() {
    	Individuo fittest = individuals[0];
        // Loop through individuals to find fittest
        for (int i = 0; i < size(); i++) {
            if (fittest.getFitness() <= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    /* Public methods */
    // Get population size
    public int size() {
        return individuals.length;
    }

    // Save individual
    public void saveIndividual(int index, Individuo indiv) {
        individuals[index] = indiv;
    }
}
