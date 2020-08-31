package gpLibrary.concepts;

import gpLibrary.primitives.functions.GeneticFunction;

import java.util.ArrayList;
import java.util.List;

public class FunctionalSet<T>
{
    private final List<GeneticFunction<T>> _functions;

    public FunctionalSet(){
        _functions = new ArrayList<>();
    }

    public List<GeneticFunction<T>> get_functions() {
        return _functions;
    }

    public void addFunction( GeneticFunction<T> newFunc){
        _functions.add(newFunc);
    }
}
