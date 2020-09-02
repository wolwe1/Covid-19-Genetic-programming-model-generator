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

    public int size(){
        return _functions.size();
    }

    public GeneticFunction<T> get(int index) throws Exception {

        if(index < 0 || index >= size())
            throw new Exception("Attempted to access function out of range");

        return (GeneticFunction<T>) _functions.get(index).getCopy();

    }
}
