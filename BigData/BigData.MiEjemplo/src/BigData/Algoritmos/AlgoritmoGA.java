package BigData.Algoritmos;

import BigData.Utilidades.Individuo;
import BigData.Utilidades.Poblacion;

public class AlgoritmoGA {

	/* GA parameters */
    private static final double uniformRate = 0.1;
    private static final double mutationRate = 0.12;
    private static final int tournamentSize = 15;
    private static final boolean elitism = true;
    
  

    /* Public methods */
    
    // Evolve a population
    public static Poblacion evolvePopulation(Poblacion pop) {
    	Poblacion newPopulation = new Poblacion(pop.size(), false);

        // Keep our best individual
        if (elitism) {
            newPopulation.saveIndividual(0, pop.getFittest());
        }
        // Crossover population
        int elitismOffset;
        if (elitism) {
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }
        // Loop over the population size and create new individuals with
        // crossover
        for (int i = elitismOffset; i < pop.size(); i++) {
            Individuo indiv1 = tournamentSelection(pop);
            Individuo indiv2 = tournamentSelection(pop);
            Individuo newIndiv = crossover(indiv1, indiv2);
            newPopulation.saveIndividual(i, newIndiv);
        }

        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutate(newPopulation.getIndividual(i));
        }

        return newPopulation;
    }

    // Crossover individuals
    private static Individuo crossover(Individuo indiv1, Individuo indiv2) {
    	Individuo newSol = new Individuo();
        // Loop through genes
        for (int i = 0; i < indiv1.size(); i++) {
            // Crossover
            if (Math.random() <= uniformRate) {
                newSol.setGene(i, indiv1.getGene(i));
            } else {
                newSol.setGene(i, indiv2.getGene(i));
            }
        }
        return newSol;
    }

    // Mutate an individual
    private static void mutate(Individuo indiv) {
        // Loop through genes
        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() <= mutationRate) {
                // Create random gene
                int gene = (int) Math.round(Math.random());
                indiv.setGene(i, gene);
            }
        }
    }

    // Select individuals for crossover
    private static Individuo tournamentSelection(Poblacion pop) {
        // Create a tournament population
    	Poblacion tournament = new Poblacion(tournamentSize, false);
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }
        // Get the fittest
        Individuo fittest = tournament.getFittest();
        return fittest;
    }
}
