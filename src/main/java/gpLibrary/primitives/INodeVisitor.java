package gpLibrary.primitives;

public interface INodeVisitor<T>
{
    void visit(Node<T> node);
}
