package solution.models.terminals;

import data.models.CovidEntry;
import data.models.CovidPredictionMode;
import gpLibrary.primitives.terminals.TerminalNode;

public class CovidTerminal extends TerminalNode<Double> {

    private final CovidEntry _entry;
    public CovidPredictionMode _mode;

    public CovidTerminal(CovidEntry entry) {
        super(entry.observationDate.toString());
        _entry = entry;
    }

    @Override
    public Double getBaseValue() {
        return 0d;
    }

    @Override
    public CovidTerminal getCopy() {
        CovidTerminal newTerminal = new CovidTerminal(_entry.makeCopy());
        newTerminal._mode = _mode;
        newTerminal.value = value;

        return newTerminal;
    }

    public void setMode(CovidPredictionMode mode){
        _mode = mode;

        switch (_mode){

            case Cases:
                value = _entry.confirmedCases;
                break;
            case Deaths:
                value =  _entry.deaths;
                break;
            case Recoveries:
                value =  _entry.recoveries;
                break;
            default:
                value = 0d;
        }
    }
}
