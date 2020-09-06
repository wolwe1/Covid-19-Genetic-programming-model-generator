package solution.infrastructure.functions.advanced;

import gpLibrary.primitives.Node;
import gpLibrary.primitives.functions.GeneticFunction;

public class ChangeFunction extends GeneticFunction<Double> {

    public ChangeFunction() {
        super("C");
    }

    @Override
    public Double Operation() throws Exception {
        return getChild(1).getValue() - getChild(0).getValue();
    }

    @Override
    public Node<Double> getCopy() {

        var newFunc = new ChangeFunction();
        newFunc = (ChangeFunction) replicate(newFunc);

        return newFunc;
    }
}
