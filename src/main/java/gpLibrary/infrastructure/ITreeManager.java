package gpLibrary.infrastructure;

import gpLibrary.primitives.other.IFitnessFunction;
import gpLibrary.primitives.other.PopulationMember;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class manages creation of trees, the operations that can be performed on them and used encapsulates the fitness function used on them
 * @param <T>
 */
public abstract class ITreeManager<T> {

    private List<PopulationMember<T>> _population;
    private IFitnessFunction _fitnessFunction;
    private final Random _randomNumberGenerator;

    protected ITreeManager(IFitnessFunction fitnessFunction, long seed){
        _fitnessFunction = fitnessFunction;
        _randomNumberGenerator = new Random(seed);
        _population = new ArrayList<>();
    }

    /**
     * Finds the best performing tree in the managers population according to the set fitness function
     * @Assumption All trees have already had their fitness calculated
     * @return The best performing tree or null if the population is empty
     */
    PopulationMember<T> getBest(){

        if(_population.size() == 0)
            return null;

        double bestFitness = _fitnessFunction.getWorstPossibleValue();
        int bestPerformerIndex = 0;

        for (int i = 0, populationSize = _population.size(); i < populationSize; i++) {

            PopulationMember<T> populationMember = _population.get(i);

            if (_fitnessFunction.firstFitterThanSecond(populationMember.fitness,bestFitness)){
                bestPerformerIndex = i;
                bestFitness = populationMember.fitness;
            }
        }

        return _population.get(bestPerformerIndex);
    }

    /**
     * Creates a random tree using a format specified by the inheritor
     * @Assumption  The tree must have its fitness calculated
     * @return A newly created tree, no guarantee of uniqueness
     */
    public abstract PopulationMember<T> createRandom();

    /**
     * Selects a random member of the population that has not been visited yet
     * @return A randomly chosen not yet visited member
     */
    public PopulationMember<T> getRandom(){

        PopulationMember<T> theChosenOne = _population.get(_randomNumberGenerator.nextInt(_population.size()));

        while(theChosenOne.visited){
            theChosenOne = _population.get(_randomNumberGenerator.nextInt(_population.size()));
        }

        theChosenOne.visited = true;
        return theChosenOne;
    }

    /**
     * Uses the internal fitness function to decide which tree is fitter
     * @Assumption Both trees have had their fitness calculated
     * @param first The tree to be used as a comparison base
     * @param second The tree the first tree will be compared against
     * @return True if the first tree is fitter, otherwise false
     */
    public boolean firstFitterThanSecond(PopulationMember<T> first, PopulationMember<T> second){
        return _fitnessFunction.firstFitterThanSecond(first.fitness,second.fitness);
    }

    /**
     * Creates a mutated copy of the provided tree
     * @param toBeMutated The tree that is to be mutated
     * @return A mutated version of the tree
     */
    public abstract PopulationMember<T> createMutant(PopulationMember<T> toBeMutated);

    /**
     * Creates copies of the provided trees, with chosen features swapped
     * @param memberOne Tree one of the crossover
     * @param memberTwo The counterpart of the crossover
     * @return A list containing two new trees, that are crossed over versions of the ones provided
     */
    public abstract List<PopulationMember<T>> crossOver(PopulationMember<T> memberOne, PopulationMember<T> memberTwo);

    /**
     * Sets the new population and ensures their fitness is calculated
     * @param newPopulation The new population for the class to manage
     */
    public void setNewPopulation(List<PopulationMember<T>> newPopulation){
        _population = newPopulation;

        for (PopulationMember<T> member : _population) {
            if(member.fitness == Double.NEGATIVE_INFINITY)
                member.fitness = _fitnessFunction.calculateFitness(member.tree);
        }
    }

    public void set_fitnessFunction(IFitnessFunction _fitnessFunction) {
        this._fitnessFunction = _fitnessFunction;
    }
}
