package solution.infrastructure.visitors;

import gpLibrary.primitives.INodeVisitor;
import gpLibrary.primitives.Node;

import java.util.ArrayList;
import java.util.List;

public class BreathFirstVisitor<T> implements INodeVisitor<T> {

    private final List<Node<T>> _nodes;

    public BreathFirstVisitor(){
        _nodes = new ArrayList<>();
    }

    @Override
    public void visit(Node<T> node) {
        _nodes.add(node);
    }

    public List<Node<T>> getNodes(){
        return _nodes;
    }
}
