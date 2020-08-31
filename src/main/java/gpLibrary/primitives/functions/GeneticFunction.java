package gpLibrary.primitives.functions;

import gpLibrary.primitives.Node;

public abstract class GeneticFunction<T> extends Node<T> {

    protected GeneticFunction(String name) {
        super(name);
    }

    public abstract T Operation();

    @Override
    public T getValue(){
        return Operation();
    }

    @Override
    public T getBaseValue(){
        return Children.get(0).getBaseValue();
    }

}
