package gpLibrary.primitives.other;

import gpLibrary.concepts.NodeTree;

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
}
