package solution.infrastructure;

import solution.models.terminals.CovidTerminal;
import gpLibrary.concepts.NodeTree;
import gpLibrary.primitives.other.IFitnessFunction;

import java.util.ArrayList;
import java.util.List;

public class Covid19FitnessFunction implements IFitnessFunction<Double> {

    private List<CovidTerminal> _terminals;
    private int _lookAhead;

    public Covid19FitnessFunction(int lookAhead){
        _lookAhead = lookAhead;
    }
    @Override
    public double getWorstPossibleValue() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double calculateFitness(NodeTree<Double> populationMember) throws Exception {

        int numberOfDataPointsTreeCanContain = populationMember.getNumberOfPossibleLeafNodes();
        int startIndexOfComparisons =  numberOfDataPointsTreeCanContain + _lookAhead;
        List<Double> answerSet = new ArrayList<>();
        List<Double> treeAnswers = new ArrayList<>();

        //Load answer set
        for (int i = startIndexOfComparisons; i < _terminals.size(); i++) {
            answerSet.add(_terminals.get(i).getValue());
        }

        //Perform a sliding window comparison
        int endPointForTest = _terminals.size() - (numberOfDataPointsTreeCanContain + _lookAhead);
        for (int i = 0; i < _terminals.size(); i++) {
            //Load tree with data points
            for (int j = i; j < numberOfDataPointsTreeCanContain; j++) {
                CovidTerminal dataPoint = _terminals.get(j);
                populationMember.addNode(dataPoint);
            }
            treeAnswers.add(populationMember.root.getValue());
            populationMember.clearLeaves();
        }

        double difference = 0;

        for (int i = 0, treeAnswersSize = treeAnswers.size(); i < treeAnswersSize; i++) {
            Double treeAnswer = treeAnswers.get(i);
            Double answer = answerSet.get(i);
            difference += Math.abs(treeAnswer - answer);
        }

        return difference;
    }

    @Override
    public boolean firstFitterThanSecond(double firstFitness, double secondFitness) {
        return firstFitness < secondFitness;
    }

    /**
     * This method sets the range of covid reports to be used when calculating fitness of a tree
     * @param terminalNodes The list of possible data points in the data set
     */
    public void setDomain(List<CovidTerminal> terminalNodes) {
        _terminals = terminalNodes;
    }
}