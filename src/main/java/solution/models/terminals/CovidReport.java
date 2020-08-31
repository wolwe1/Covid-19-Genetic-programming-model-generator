package solution.models.terminals;

import gpLibrary.primitives.Node;

//The terminal representation of covid 19 statistics for a specific country, state etc at a given point in time

public class CovidReport extends Node<Double> {

    public double confirmedCases;
    public double deaths;
    public double recoveries;

    public CovidReport(int maxChildren, String name) {
        super(maxChildren, name);
    }

    @Override
    public Double getValue() {
        return confirmedCases;
    }

    @Override
    public Double getBaseValue() {
        return 0d;
    }
}
