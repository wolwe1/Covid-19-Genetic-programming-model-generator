package solution.infrastructure.stats;

import gpLibrary.concepts.IStatisticsPackage;
import gpLibrary.primitives.other.PopulationMember;

import java.util.Comparator;
import java.util.List;

public class BasicStats implements IStatisticsPackage<Double> {

    private double _medianFitness;
    private double _meanFitness;
    private double _worstFitness;
    private double _bestFitness;

    public BasicStats(){
        _medianFitness = 0;
        _meanFitness = 0;
        _worstFitness = 0;
        _bestFitness = 0;
    }

    @Override
    public void print() {
        System.out.println("Median : " + _medianFitness);
        System.out.println("Mean : " + _meanFitness);
        System.out.println("Worst : " + _worstFitness);
        System.out.println("Best : " + _bestFitness);
    }

    @Override
    public IStatisticsPackage<Double> createNew(List<PopulationMember<Double>> entries) {
        BasicStats stats = new BasicStats();

        entries.sort(Comparator.comparingDouble(a -> a.fitness));
        stats._bestFitness = entries.get(0).fitness;
        stats._worstFitness = entries.get(entries.size() -1).fitness;

        if (entries.size() % 2 == 0)
            stats._medianFitness = ( entries.get(entries.size() / 2).fitness + (double) entries.get(entries.size() / 2 - 1).fitness)/2;
        else
            stats._medianFitness =  entries.get(entries.size() / 2).fitness;

        double total = 0;
        for (PopulationMember<Double> entry : entries) {
            total += entry.fitness;
        }
        stats._meanFitness = total/entries.size();

        return stats;
    }
}
