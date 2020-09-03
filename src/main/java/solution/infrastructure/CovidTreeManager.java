package solution.infrastructure;

import data.models.CovidEntry;
import data.models.CovidPredictionMode;
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

    public CovidTreeManager(Covid19FitnessFunction fitnessFunction, long seed, FunctionalSet<Double> funcSet, List<CovidEntry> covidEntries,CovidPredictionMode predictOn) {
        super(fitnessFunction, seed);
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

        return new PopulationMember<>(tree);
    }

    @Override
    public PopulationMember<Double> createMutant(PopulationMember<Double> toBeMutated) throws Exception {

        int randomPoint = _randomNumberGenerator.nextInt(toBeMutated.tree.getTreeSize());
        int randomFunc = _randomNumberGenerator.nextInt(_functionalSet.size());

        Node<Double> nodeToExchange = toBeMutated.tree.getNode(randomPoint);
        GeneticFunction<Double> nodeReplacing = _functionalSet.get(randomFunc);

        nodeReplacing.Parent = nodeToExchange.Parent;
        for (Node<Double> child : nodeToExchange.getChildren()) {
            nodeReplacing.addChild(child);
        }

        //Find the node being replaced place in parents children
        int indexOfNode = nodeToExchange.Parent.getIndexOfChild(nodeToExchange);
        nodeToExchange.Parent.setChild(indexOfNode,nodeReplacing);

        return toBeMutated;
    }

    @Override
    public List<PopulationMember<Double>> crossOver(PopulationMember<Double> memberOne, PopulationMember<Double> memberTwo) throws Exception {

        List<PopulationMember<Double>> members = new ArrayList<>();


        //Symmetric crossover
        int randomPoint = _randomNumberGenerator.nextInt(memberOne.tree.getTreeSize());
        Node<Double> nodeToExchangeInFirstTree = memberOne.tree.getNode(randomPoint);
        Node<Double> nodeToExchangeInSecondTree = memberTwo.tree.getNode(randomPoint);

        //Create a copy of the first one
        Node<Double> copyOfFirst = nodeToExchangeInFirstTree.getCopy();
        Node<Double> copyOfSecond = nodeToExchangeInSecondTree.getCopy();

        int indexOfFirstNode = nodeToExchangeInFirstTree.Parent.getIndexOfChild(nodeToExchangeInFirstTree);
        int indexOfSecondNode = nodeToExchangeInSecondTree.Parent.getIndexOfChild(nodeToExchangeInSecondTree);

        nodeToExchangeInFirstTree.Parent.setChild(indexOfFirstNode,copyOfSecond);
        nodeToExchangeInSecondTree.Parent.setChild(indexOfSecondNode,copyOfFirst);

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
}
