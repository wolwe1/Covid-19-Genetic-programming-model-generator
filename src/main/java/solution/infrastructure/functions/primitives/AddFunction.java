package solution.infrastructure.functions.primitives;

import gpLibrary.primitives.Node;
import gpLibrary.primitives.functions.GeneticFunction;

public class AddFunction extends GeneticFunction<Double> {

    public AddFunction() {
        super("A");
    }

    @Override
    public Double Operation() throws Exception {
        Double baseValue = _children.get(0).getBaseValue();

        for (Node<Double> child : _children) {

            baseValue += child.getValue();
        }
        return baseValue;
    }

    @Override
    public Node<Double> getCopy() {
        var newFunc = new AddFunction();
        newFunc = (AddFunction) replicate(newFunc);

        return newFunc;
    }

}
