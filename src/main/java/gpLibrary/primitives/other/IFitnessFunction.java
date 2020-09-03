package gpLibrary.primitives.other;

import gpLibrary.concepts.NodeTree;

public interface IFitnessFunction<T> {

    /**
     * Returns the highest or lowest value depending on fitness function implementation
     * @return Double.NegativeInfinity or Double.Infinity
     */
    double getWorstPossibleValue();

    /**
     * Calculates the fitness of the underlying tree returning the value
     * @param populationMember The package containing the tree
     * @return The fitness of the underlying tree
     */
    double calculateFitness(NodeTree<T> populationMember) throws Exception;

    /**
     * Evaluates if the fitness provided in the first parameter is better that that of the second
     * @param firstFitness The base value for the comparison
     * @param secondFitness The value the first parameter is compared to
     * @return True if the first fitness is better than the second else false
     */
    boolean firstFitterThanSecond(double firstFitness, double secondFitness);

}
