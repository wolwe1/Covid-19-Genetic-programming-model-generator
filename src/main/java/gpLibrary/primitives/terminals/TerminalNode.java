package gpLibrary.primitives.terminals;

import gpLibrary.primitives.Node;

public abstract class TerminalNode<T> extends Node<T>{

    public T Value;

    protected TerminalNode(String name){
        super(name);
    }

    @Override
    public T getValue(){
        return Value;
    }

}