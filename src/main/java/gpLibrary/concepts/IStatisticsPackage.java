package gpLibrary.concepts;

import gpLibrary.primitives.other.PopulationMember;

import java.util.List;

public interface IStatisticsPackage<T> {

    void print();

    IStatisticsPackage<T> createNew(List<PopulationMember<T>> entry);
}
