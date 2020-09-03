package solution.infrastructure.primitives;

import gpLibrary.primitives.Node;
import gpLibrary.primitives.functions.GeneticFunction;

public class AddFunction extends GeneticFunction<Double> {

    public AddFunction() {
        super("A");
    }

    @Override
    public Double Operation() {
        Double baseValue = _children.get(0).getBaseValue();

        for (Node<Double> child : _children) {

            baseValue += child.getValue();
        }
        return baseValue;
    }

    @Override
    public Node<Double> getCopy() {
        return new AddFunction();
    }
}
