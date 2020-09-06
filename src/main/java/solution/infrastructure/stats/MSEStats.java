package solution.infrastructure.stats;

import data.models.CovidEntry;
import gpLibrary.concepts.IStatisticsPackage;
import gpLibrary.primitives.other.PopulationMember;
import solution.models.terminals.CovidTerminal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MSEStats implements IStatisticsPackage<Double> {

    private double _averageMSE;
    private double _averageMAE;
    private final List<Double> _mseValues;
    private final List<Double> _maeValues;
    private final List<Double> _averageMAEValues;
    private final List<String> _names;
    private final List<CovidTerminal> _covidEntries;
    private final int _lookAhead;

    public MSEStats(List<CovidTerminal> covidEntries,int lookAhead){
        _averageMSE = 0;
        _averageMAE = 0;
        _maeValues = new ArrayList<>();
        _mseValues = new ArrayList<>();
        _averageMAEValues = new ArrayList<>();
        _lookAhead = lookAhead;
        _covidEntries = covidEntries;
        _names = new ArrayList<>();
    }

    @Override
    public void print() {

        for (int i = 0, mseValuesSize = _mseValues.size(); i < mseValuesSize; i++) {

            System.out.println("Entry " + 1);
            print(i);
        }

        System.out.println("\nAverage MSE of group : " + _averageMSE);
        System.out.println("Average MAE of group : " + _averageMAE);
    }

    public void print(int index){
        Double mseValue = _mseValues.get(index);
        Double maeValue = _maeValues.get(index);
        Double averageMAE = _averageMAEValues.get(index);

        System.out.println("\nTree: " + _names.get(index));
        System.out.println("Mean squared error(MSE) : " + mseValue);
        System.out.println("Mean absolute error(MAE) : " + maeValue);
        System.out.println("Average MAE over run : " + averageMAE);
    }

    @Override
    public IStatisticsPackage<Double> createNew(List<PopulationMember<Double>> entries) {

        for (PopulationMember<Double> entry : entries) {
            try {
                calculateValues(entry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        double totalMSE = 0;
        double totalMAE = 0;

        for (int i = 0, mseValuesSize = _mseValues.size(); i < mseValuesSize; i++) {
            totalMSE += _mseValues.get(i);
            totalMAE += _maeValues.get(i);
        }

        _averageMSE = totalMSE / _mseValues.size();
        _averageMAE = totalMAE / _maeValues.size();

        return this;
    }

    private void calculateValues(PopulationMember<Double> tree) throws Exception {

        double mse = 0;
        double mae = 0;

        int numberOfEntriesInTree = tree.tree.getNumberOfPossibleLeafNodes();
        int entriesInTheFutureToGuess = (numberOfEntriesInTree + _lookAhead) - 1;


        CovidTerminal constant = getConstant(_lookAhead, _covidEntries);
        for (int i = 0, covidEntriesSize = _covidEntries.size() - (numberOfEntriesInTree + _lookAhead); i < covidEntriesSize; i++) {

            var sublist = new ArrayList<>(_covidEntries.subList(i,( (numberOfEntriesInTree + i)-1 )));
            sublist.add(constant);
            tree.loadLeaves(sublist);
            double difference = (tree.makePrediction() - _covidEntries.get(i + entriesInTheFutureToGuess).getValue());

            mae += Math.abs(difference);
            mse += Math.pow(difference,2);
        }

        _mseValues.add(mse);
        _maeValues.add(mae);
        _averageMAEValues.add( mae/entriesInTheFutureToGuess);
        _names.add(tree.id);
    }

    public static CovidTerminal getConstant(int lookAhead, List<CovidTerminal> covidEntries) {
        CovidEntry entryConstant = new CovidEntry();
        entryConstant.confirmedCases = lookAhead;
        entryConstant.deaths = lookAhead;
        entryConstant.recoveries = lookAhead;
        entryConstant.observationDate = new Date();

        CovidTerminal constant = new CovidTerminal(entryConstant);
        constant.setMode(covidEntries.get(0)._mode);

        return constant;
    }
}
