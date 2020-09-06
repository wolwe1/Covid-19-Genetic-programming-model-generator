package gpLibrary.primitives.functions;

import gpLibrary.primitives.Node;

public abstract class GeneticFunction<T> extends Node<T> {

    protected GeneticFunction(String name) {
        super(name);
    }

    public abstract T Operation() throws Exception;

    @Override
    public T getValue() throws Exception {
        return Operation();
    }

    @Override
    public T getBaseValue(){
        return _children.get(0).getBaseValue();
    }

}
