package solution.infrastructure;

import data.models.CovidEntry;
import gpLibrary.concepts.FunctionalSet;
import gpLibrary.infrastructure.ITreeManager;
import gpLibrary.primitives.other.IFitnessFunction;
import gpLibrary.primitives.other.PopulationMember;
import gpLibrary.primitives.terminals.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class CovidTreeManager extends ITreeManager<Double> {

    private FunctionalSet<Double> _functionalSet;
    private List<TerminalNode<Double>> _terminalNodes;

    public CovidTreeManager(IFitnessFunction fitnessFunction, long seed, FunctionalSet<Double> funcSet, List<CovidEntry> covidEntries) {
        super(fitnessFunction, seed);
        _functionalSet = funcSet;
        _terminalNodes = convertEntriesToNodes(covidEntries);
    }

    @Override
    public PopulationMember<Double> createRandom() {
        return null;
    }

    @Override
    public PopulationMember<Double> createMutant(PopulationMember<Double> toBeMutated) {
        return null;
    }

    @Override
    public List<PopulationMember<Double>> crossOver(PopulationMember<Double> memberOne, PopulationMember<Double> memberTwo) {
        return null;
    }

    private List<TerminalNode<Double>> convertEntriesToNodes(List<CovidEntry> covidEntries) {

        List<TerminalNode<Double>> terminalNodes = new ArrayList<>();

        for (CovidEntry covidEntry : covidEntries) {

        }
    }
}
