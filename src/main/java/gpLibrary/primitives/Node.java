package gpLibrary.primitives;

import java.util.ArrayList;
import java.util.List;

public abstract class Node<T>
{
    protected final List<Node<T>> Children;

    public Node<T> Parent;
    public final String Name;
    public int _maxChildren;
    public int _level = 0;
    public int _drawPos = 0;
    public int _depth = 0;

    protected Node(int maxChildren,String name)
    {
        _maxChildren = maxChildren;
        Name = name;
        Children = new ArrayList<>();
    }

    protected Node(String name)
    {
        Name = name;
        Children = new ArrayList<>();
    }

    public Node<T> addChild(Node<T> newNode) throws Exception {

        if (IsFull()) throw new Exception("Node cannot have any more children");

        newNode.Parent = this;
        newNode._level = _level + 1;
        Children.add(newNode);

        return newNode;
    }

    public List<Node<T>> getChildren(){ return Children; }

    public Node<T> getChild(int index) throws Exception {
        if (index >= Children.size())
            return null;

        return Children.get(index);
    }

    public boolean IsFull()
    {
        return Children.size() == _maxChildren;
    }

    public abstract T getValue();
    public abstract T getBaseValue();
}
