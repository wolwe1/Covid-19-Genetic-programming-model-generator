package gpLibrary.infrastructure;

import gpLibrary.concepts.IStatisticsPackage;
import gpLibrary.primitives.other.PopulationMember;

import java.util.ArrayList;
import java.util.List;

public class Statistics<T> {

    private final List<IStatisticsPackage<T>> _history;
    private final IStatisticsPackage<T> _package;

    public Statistics(IStatisticsPackage<T> packageType) {
        _history = new ArrayList<>();
        _package = packageType;
    }

    public void printLatest(){
        _history.get(_history.size()-1).print();
    }

    public void addEntry(List<PopulationMember<T>> entry){
        _history.add(_package.createNew(entry));
    }

    public List<IStatisticsPackage<T>> getHistory(){
        return _history;
    }
}
