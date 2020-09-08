package gpLibrary.infrastructure;

import gpLibrary.primitives.other.PopulationMember;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm<T> {

    private final int _maxPopulationSize;
    private List<PopulationMember<T>> _population;

    public void set_treeManager(ITreeManager<T> _treeManager) {
        this._treeManager = _treeManager;
    }

    private ITreeManager<T> _treeManager;
    private int _tournamentSize;
    private int _numGenerations;
    private double _mutationRate;
    private double _crossoverRate;
    private double _reproductionRate;

    private boolean print;

    public GeneticAlgorithm(int popSize,int tournamentSize, ITreeManager<T> treeManager) {
        _maxPopulationSize = popSize;
        _population = new ArrayList<>();
        _treeManager = treeManager;
        _tournamentSize = tournamentSize;
    }

    public void resetPopulation(){
        _population = new ArrayList<>();
    }
    /**
     * Sets the mutation and crossover rate, the reproduction rate is the remainder
     * @param mutationRate The percentage of the population mutated each generation. Scale 0 <= x <= 1
     * @param crossoverRate The percentage of the population mutated each generation. Scale 0 <= x <= 1
     * @throws Exception If the two rates are greater than 100&
     */
    public void setRates(double mutationRate, double crossoverRate) throws Exception {

        if(mutationRate + crossoverRate > 1) throw new Exception("Rates set above 100%");

        _mutationRate = mutationRate;
        _crossoverRate = crossoverRate;
        _reproductionRate = (double)Math.round( (1 - (_mutationRate + _crossoverRate))*100 )/100;
    }

    public void setNumberOfGenerations(int numberOfGenerations) {
        _numGenerations = numberOfGenerations;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }

    /**
     * Creates the initial population using randomly generated trees
     */
    private void createPopulation() throws Exception {

        for (int i = 0; i < _maxPopulationSize; i++) {
            _population.add( _treeManager.createRandom());
        }
    }

    private List<PopulationMember<T>> selection(int numOfTreesToReproduce) {

        List<PopulationMember<T>> winners = new ArrayList<>();
        for (int i = 0; i < numOfTreesToReproduce; i++) {
            PopulationMember<T> winner = performTournament();
            winners.add(winner);
        }
        return winners;
    }

    private PopulationMember<T> performTournament(){

        PopulationMember<T> theChosenOne = _treeManager.getRandom();

        for(int count = 2; count <= _tournamentSize; ++count) {
            PopulationMember<T> competitor = _treeManager.getRandom();
            if (_treeManager.firstFitterThanSecond(competitor,theChosenOne))
                theChosenOne = competitor;

        }
        return theChosenOne;
    }

    /**
     * Picks random members of the population to crossover
     * @param numOfCrossovers The total amount of members involved in crossover
     * @throws Exception If an uneven number of members where selected for crossover
     * @return A list containing members which have had crossover performed
     */
    private List<PopulationMember<T>> crossover(int numOfCrossovers) throws Exception {

        //Assertion
        if(numOfCrossovers % 2 != 0) throw new Exception("An uneven number of members where selected for crossover");

        List<PopulationMember<T>> crossoverMembers = new ArrayList<>();

        for (int i = 0; i < numOfCrossovers/2; i++) {
            PopulationMember<T> memberOne = _treeManager.getRandom();
            PopulationMember<T> memberTwo = _treeManager.getRandom();

            List<PopulationMember<T>> crossedOverVersions = _treeManager.crossOver(memberOne,memberTwo);
            crossoverMembers.add( crossedOverVersions.get(0));
            crossoverMembers.add( crossedOverVersions.get(1));
        }

        //Assertion
        if(crossoverMembers.size() != numOfCrossovers) throw new Exception("Members after crossover did not match expected output count");

        return crossoverMembers;
    }

    private List<PopulationMember<T>> regenerate(int numberOfNewMembers) throws Exception {

        List<PopulationMember<T>> newMembers = new ArrayList<>();

        for (int i = 0; i < numberOfNewMembers; i++) {
            newMembers.add( _treeManager.createRandom());
        }

        return newMembers;
    }

    private List<PopulationMember<T>> mutate(int numOfTreesToMutate) throws Exception {
        List<PopulationMember<T>> mutants = new ArrayList<>();

        for (int i = 0; i < numOfTreesToMutate; i++) {
            PopulationMember<T> mutant = _treeManager.getRandom();
            mutants.add(_treeManager.createMutant(mutant));
        }

        return mutants;
    }
    /**
     * Runs the genetic algorithm, performing operations on the members of the population until the designated generation
     * @return The best tree
     * @throws Exception If the pool size was not maintained
     */
    public PopulationMember<T> run() throws Exception {

        createPopulation();
        _treeManager.setNewPopulation(_population);
        PopulationMember<T> bestTreeInGeneration = null;
        double overallBestTreeFitness = Double.POSITIVE_INFINITY;
        int overallBestGeneration = 0;
        double generationsSinceLastImprovement = 0;

        for (int i = 0; i < _numGenerations; i++) {
            if (this.print) System.out.println("=========================================\nGeneration " + i);

            PopulationMember<T> bestTree = _treeManager.getBest();
            if(bestTree.fitness < overallBestTreeFitness)
            {
                overallBestTreeFitness = bestTree.fitness;
                overallBestGeneration = i;
                generationsSinceLastImprovement = 0;
                bestTreeInGeneration = bestTree.getCopy();
            }else{
                if(++generationsSinceLastImprovement == 500){
                    System.out.println("No improvement in the last 500 generations, aborting run");
                    break;
                }
            }
            if (this.print) System.out.println("Best Fitness: " + bestTree.fitness);
            if (this.print) System.out.println("Heuristic Combination: " + bestTree.tree.getCombination());
            if (this.print) System.out.println("\nStatistics");
            if (this.print) System.out.println("------------------------------------------");
            if (this.print) _treeManager.printLatestStatistics();
            if (this.print) System.out.println("*****************************************");
            if (this.print) System.out.println("Overall best: Generation " + overallBestGeneration + " - " + overallBestTreeFitness);
            if (this.print) System.out.println("*****************************************");
            if (this.print) System.out.println("=========================================");

            evolvePopulation();
        }

        System.out.println("Overall best: Generation " + overallBestGeneration + " - " + overallBestTreeFitness);
        return bestTreeInGeneration;
    }

    /**
     * Performs the population operations on the current population
     * @throws Exception If the pool size is not maintained or crossover constraint was not met
     */
    private void evolvePopulation() throws Exception {

        int numOfTreesToRandomTreesToFill = 0;
        int numOfTreesToReproduce = (int)(_reproductionRate * _population.size());
        //Ensure reproduction is bound to tournament size
        while(numOfTreesToReproduce % _tournamentSize != 0){
            numOfTreesToRandomTreesToFill++;
            numOfTreesToReproduce--;
        }

        int numOfTreesToMutate = (int)(_mutationRate * _population.size());
        // After reproduction, trees that lost the tournament will not be included in the pool.
        // For a constant size pool they must be replaced.
        numOfTreesToRandomTreesToFill += numOfTreesToReproduce - (numOfTreesToReproduce / _tournamentSize);

        int numOfTreesToCrossover = (int)(_crossoverRate * _population.size());
        //Ensure crossover uses even number
        if(numOfTreesToCrossover % 2 != 0){
            //Reduce crossover size and increase random new tree count
            numOfTreesToCrossover--;
            numOfTreesToRandomTreesToFill++;
        }

        if( ( (numOfTreesToReproduce/_tournamentSize) + numOfTreesToMutate + numOfTreesToCrossover + numOfTreesToRandomTreesToFill) != _maxPopulationSize)
            throw new Exception("Pool size was not maintained");

        var reproducers = selection((numOfTreesToReproduce/_tournamentSize));
        var newMembers = regenerate(numOfTreesToRandomTreesToFill);
        var mutants = mutate(numOfTreesToMutate);
        var crossoverMembers = crossover(numOfTreesToCrossover);

        List<PopulationMember<T>> newPopulation = new ArrayList<>();

        newPopulation.addAll(reproducers);
        newPopulation.addAll(newMembers);
        newPopulation.addAll(mutants);
        newPopulation.addAll(crossoverMembers);

        _population = newPopulation;
        _treeManager.setNewPopulation(newPopulation);
    }

}
