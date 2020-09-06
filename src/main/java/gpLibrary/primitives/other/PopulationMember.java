package gpLibrary.primitives.other;

import gpLibrary.concepts.NodeTree;
import gpLibrary.primitives.Node;
import gpLibrary.primitives.terminals.TerminalNode;
import solution.models.terminals.CovidTerminal;

import java.util.List;

/**
 * A POJO that wraps around a node tree
 * @param <T> The type of the underlying tree
 */
public class PopulationMember<T> {

    public NodeTree<T> tree;
    public double fitness = Double.NEGATIVE_INFINITY;
    public String id;
    public boolean visited;

    public PopulationMember(NodeTree<T> innerTree){
        tree = innerTree;
        id = innerTree.getCombination();
        visited = false;
    }

    public void updateId(){
        id = tree.getCombination();
    }

    public PopulationMember<T> getCopy() throws Exception {

        PopulationMember<T> newMember = new PopulationMember<>(tree.getCopy());
        newMember.id = id;
        newMember.visited = visited;
        newMember.fitness = fitness;

        return newMember;
    }

    public T makePrediction() throws Exception {
        return tree.root.getValue();
    }

    public void loadLeaves(List< ? extends TerminalNode<T>> subList) throws Exception {
        tree.clearLeaves();
        for (Node<T> terminal : subList) {
            tree.addNode(terminal.getCopy());
        }
    }
}
