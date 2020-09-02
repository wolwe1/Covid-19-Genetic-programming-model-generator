package solution.models.functions;

import gpLibrary.primitives.Node;
import gpLibrary.primitives.functions.GeneticFunction;

public class AddFunction extends GeneticFunction<Double> {

    public AddFunction(String name) {
        super(name);
    }

    @Override
    public Double Operation() {

        Double total = getBaseValue();

        for (Node<Double> child : _children) {
            total += child.getValue();
        }

        return total;
    }
}
