package gpLibrary.primitives;

import java.util.ArrayList;
import java.util.List;

public abstract class Node<T>
{
    protected final List<Node<T>> _children;

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
        _children = new ArrayList<>();
    }

    protected Node(String name)
    {
        Name = name;
        _children = new ArrayList<>();
    }

    public Node<T> addChild(Node<T> newNode) throws Exception {

        if (IsFull()) throw new Exception("Node cannot have any more children");

        newNode.Parent = this;
        newNode._level = _level + 1;
        _children.add(newNode);

        return newNode;
    }

    public List<Node<T>> getChildren(){ return _children; }

    public Node<T> getChild(int index) throws Exception {
        if (index >= _children.size())
            return null;

        return _children.get(index);
    }

    public boolean IsFull()
    {
        return _children.size() == _maxChildren;
    }

    public abstract T getValue();
    public abstract T getBaseValue();

    public abstract Node<T> getCopy();

    public void setChild(int index,Node<T> newChild) throws Exception {
        if(index < 0 || index >= _maxChildren)
            throw new Exception("Attempted to set a child out of range");

        _children.set(index,newChild);
    }

    public int getIndexOfChild(Node<T> child){

        for (int i = 0, childrenSize = _children.size(); i < childrenSize; i++) {
            Node<T> tNode = _children.get(i);
            if (tNode == child)
                return i;
        }
        return -1;
    }
}
