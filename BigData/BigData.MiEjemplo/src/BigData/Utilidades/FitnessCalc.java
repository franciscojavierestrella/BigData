package BigData.Utilidades;

public class FitnessCalc {
	
	static int defaultGeneLength = 50;
	static int[] solution = new int[defaultGeneLength];

    /* Public methods */
    // Set a candidate solution as a byte array
    public static void setSolution(int[] newSolution) {
        solution = newSolution;
    }

    // To make it easier we can use this method to set our candidate solution 
    // with string of 0s and 1s
    public static void setSolution(String newSolution) {
        solution = new int[newSolution.length()];
        // Loop through each character of our string and save it in our byte 
        // array
        for (int i = 0; i < newSolution.length(); i++) {
            String character = newSolution.substring(i, i + 1);
            if (Integer.parseInt(character)>0 && Integer.parseInt(character) < 3) {
                solution[i] = Integer.parseInt(character);
            } else {
                solution[i] = 0;
            }
        }
    }

    // Calculate inidividuals fittness by comparing it to our candidate solution
    static int getFitness(Individuo individual) {
        int fitness = 0;
        // Loop through our individuals genes and compare them to our cadidates
        for (int i = 0; i < individual.size() && i < solution.length; i++) {
            if (individual.getGene(i) == solution[i]) {
                fitness++;
            }
        }
        return fitness;
    }
    
    // Get optimum fitness
    public static int getMaxFitness() {
        int maxFitness = solution.length;
        return maxFitness;
    }
}
