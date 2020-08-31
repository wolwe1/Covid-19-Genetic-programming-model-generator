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
}
