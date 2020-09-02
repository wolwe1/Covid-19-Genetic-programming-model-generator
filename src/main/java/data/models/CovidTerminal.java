package data.models;

import gpLibrary.primitives.functions.GeneticFunction;
import gpLibrary.primitives.terminals.TerminalNode;

public class CovidTerminal extends TerminalNode<Double> {

    private CovidEntry _entry;
    private CovidPredictionMode _mode;

    public CovidTerminal(CovidEntry entry) {
        super(entry.observationDate.toString());
    }

    @Override
    public Double getBaseValue() {

        switch (_mode){

            case Cases:
                return _entry.confirmedCases;
            case Deaths:
                return _entry.deaths;
            case Recoveries:
                return _entry.recoveries;
            default:
                return null;
        }
    }

    @Override
    public CovidTerminal getCopy() {
        CovidTerminal newTerminal = new CovidTerminal(_entry.makeCopy());
        newTerminal._mode = _mode;

        return newTerminal;
    }

    public void changeMode(CovidPredictionMode mode){
        _mode = mode;
    }
}
