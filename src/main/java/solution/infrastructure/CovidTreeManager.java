package solution.infrastructure;

import data.models.CovidEntry;
import data.models.CovidPredictionMode;
import gpLibrary.infrastructure.Statistics;
import solution.infrastructure.stats.MSEStats;
import solution.models.terminals.CovidTerminal;
import gpLibrary.concepts.FunctionalSet;
import gpLibrary.concepts.NodeTree;
import gpLibrary.infrastructure.ITreeManager;
import gpLibrary.primitives.Node;
import gpLibrary.primitives.functions.GeneticFunction;
import gpLibrary.primitives.other.PopulationMember;

import java.util.ArrayList;
import java.util.List;

public class CovidTreeManager extends ITreeManager<Double> {

    private final FunctionalSet<Double> _functionalSet;
    private final List<CovidTerminal> _terminalNodes;
    private int _historyCount;
    private int _maxTreeDepth;
    private int _treeChildCount;
    private int _maxTreeSize;
    private CovidPredictionMode _predictOn;
    private int _lookAhead;

    public CovidTreeManager(Covid19FitnessFunction fitnessFunction, long seed, FunctionalSet<Double> funcSet, Statistics<Double> stats, List<CovidEntry> covidEntries, CovidPredictionMode predictOn) {
        super(fitnessFunction, stats, seed);
        _lookAhead = fitnessFunction.getLookAhead();
        _functionalSet = funcSet;
        _predictOn = predictOn;
        _terminalNodes = convertEntriesToNodes(covidEntries);
        fitnessFunction.setDomain(_terminalNodes);
    }

    @Override
    public PopulationMember<Double> createRandom() throws Exception {

        NodeTree<Double> tree = new NodeTree<>(_maxTreeDepth,_treeChildCount);

        //Tree is full without terminals at size = (maxsize - 1)/ child count
        int nodesBeforeTerminals = (_maxTreeSize - 1)/_treeChildCount;
        while(tree.getTreeSize() < nodesBeforeTerminals){
            int randomFunc = _randomNumberGenerator.nextInt(_functionalSet.size());
            tree.addNode(_functionalSet.get(randomFunc));
        }

        PopulationMember<Double> popMember = new PopulationMember<>(tree);
        popMember.fitness = _fitnessFunction.getWorstPossibleValue();
        return popMember;
    }

    @Override
    public PopulationMember<Double> createMutant(PopulationMember<Double> toBeMutated) throws Exception {

        int randomPoint = _randomNumberGenerator.nextInt(toBeMutated.tree.getTreeSize());
        Node<Double> nodeToExchange = toBeMutated.tree.getNode(randomPoint);

        int randomFunc = _randomNumberGenerator.nextInt(_functionalSet.size());
        GeneticFunction<Double> nodeReplacing = _functionalSet.get(randomFunc);

        //Ensure a different node is actually swapped in
        while(nodeReplacing.name.equals(nodeToExchange.name)){
            randomFunc = _randomNumberGenerator.nextInt(_functionalSet.size());
            nodeReplacing = _functionalSet.get(randomFunc);
        }

        nodeReplacing = (GeneticFunction<Double>) nodeToExchange.replicate(nodeReplacing);
        nodeReplacing.Parent = nodeToExchange.Parent;
        for (Node<Double> child : nodeToExchange.getChildren()) {
            nodeReplacing.addChild(child);
        }

        if(nodeToExchange.Parent != null){
            //Find the node being replaced place in parents children
            int indexOfNode = nodeToExchange.Parent.getIndexOfChild(nodeToExchange);
            nodeToExchange.Parent.setChild(indexOfNode,nodeReplacing);
        }else{
            //Node has no parent and therefore is a full tree by itself
            NodeTree<Double> newTree = new NodeTree<Double>(toBeMutated.tree.maxDepth,toBeMutated.tree.maxBreadth);
            newTree.addNode(nodeReplacing);
            toBeMutated.tree = newTree;
        }
        toBeMutated.updateId();

        toBeMutated.fitness = _fitnessFunction.getWorstPossibleValue();
        return toBeMutated;
    }

    @Override
    public List<PopulationMember<Double>> crossOver(PopulationMember<Double> memberOne, PopulationMember<Double> memberTwo) throws Exception {

        List<PopulationMember<Double>> members = new ArrayList<>();

        //Symmetric crossover
        int randomPoint = _randomNumberGenerator.nextInt(memberOne.tree.getTreeSize()-1) + 1;
        Node<Double> nodeToExchangeInFirstTree = memberOne.tree.getNode(randomPoint);
        Node<Double> nodeToExchangeInSecondTree = memberTwo.tree.getNode(randomPoint);

        //Create a copy of the first one
        Node<Double> copyOfFirst = nodeToExchangeInFirstTree.deepCopy();
        Node<Double> copyOfSecond = nodeToExchangeInSecondTree.deepCopy();

        int indexOfFirstNode = nodeToExchangeInFirstTree.Parent.getIndexOfChild(nodeToExchangeInFirstTree);
        int indexOfSecondNode = nodeToExchangeInSecondTree.Parent.getIndexOfChild(nodeToExchangeInSecondTree);

        nodeToExchangeInFirstTree.Parent.setChild(indexOfFirstNode,copyOfSecond);
        nodeToExchangeInSecondTree.Parent.setChild(indexOfSecondNode,copyOfFirst);

        memberOne.fitness = _fitnessFunction.getWorstPossibleValue();
        memberOne.updateId();
        memberTwo.fitness = _fitnessFunction.getWorstPossibleValue();
        memberTwo.updateId();
        members.add( memberOne);
        members.add(memberTwo);

        return members;
    }

    private List<CovidTerminal> convertEntriesToNodes(List<CovidEntry> covidEntries) {

        List<CovidTerminal> terminalNodes = new ArrayList<>();

        for (CovidEntry covidEntry : covidEntries) {
            CovidTerminal terminal = new CovidTerminal(covidEntry);
            terminal.setMode(_predictOn);
            terminalNodes.add(terminal);
        }

        return terminalNodes;
    }

    public void setProblemValues(int history,int nodeChildCount){

        int treeDepth = 1;
        _historyCount = history;
        _treeChildCount = nodeChildCount;

        do{
            double layerAboveCount = (double) history / nodeChildCount;
            treeDepth ++;
            history = (int)layerAboveCount;
        }while (history > 1);

        _maxTreeDepth = treeDepth;

        int maxTreeSize = 0;
        for (int i = _maxTreeDepth-1; i >= 0; i--) {
            maxTreeSize += Math.pow(_treeChildCount,i);
        }

        _maxTreeSize = maxTreeSize;
    }

    public void predictOn(CovidPredictionMode predictOn){
        _predictOn = predictOn;
    }

    public List<CovidTerminal> getTerminals() {
        return _terminalNodes;
    }

    public void printTreeEstimates(PopulationMember<Double> tree) throws Exception {

        tree.tree.clearLeaves();
        int numberOfDataPointsTreeCanContain = tree.tree.getNumberOfPossibleLeafNodes();
        int startIndexOfComparisons =  numberOfDataPointsTreeCanContain + _lookAhead;
        List<Double> answerSet = new ArrayList<>();
        List<Double> treeAnswers = new ArrayList<>();

        //Load answer set
        for (int i = startIndexOfComparisons; i < _terminalNodes.size(); i++) {
            answerSet.add(_terminalNodes.get(i).getValue());
        }

        //Constant
        var constant = MSEStats.getConstant(_lookAhead, _terminalNodes);
        //Perform a sliding window comparison
        int endPointForTest = _terminalNodes.size() - (numberOfDataPointsTreeCanContain + _lookAhead);
        for (int i = 0; i < endPointForTest; i++) {
            //Load tree with data points
            for (int j = i; j < (i + numberOfDataPointsTreeCanContain) -1; j++) { //Minus 1 to add constant
                CovidTerminal dataPoint = _terminalNodes.get(j);
                tree.tree.addNode(dataPoint);
            }

            tree.tree.addNode(constant);
            treeAnswers.add(tree.tree.root.getValue());
            tree.tree.clearLeaves();
        }

        System.out.println("Prediction comparison for " + _predictOn.toString());
        for (int i = 0, answerSetSize = answerSet.size(); i < answerSetSize; i++) {
            Double value = answerSet.get(i);
            System.out.println("Day "+ (i + startIndexOfComparisons) + " : " + value + " - Model : " + treeAnswers.get(i));
        }
    }
}
