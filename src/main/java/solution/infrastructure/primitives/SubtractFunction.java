package solution.infrastructure.primitives;

import gpLibrary.primitives.Node;
import gpLibrary.primitives.functions.GeneticFunction;

public class SubtractFunction extends GeneticFunction<Double> {

    public SubtractFunction() {
        super("S");
    }

    @Override
    public Double Operation() {
        Double baseValue = _children.get(0).getValue();

        for (int i = 1; i < _children.size(); i++) {
            baseValue -= _children.get(i).getValue();
        }
        return baseValue;
    }

    @Override
    public Node<Double> getCopy() {
        var newFunc = new SubtractFunction();
        newFunc = (SubtractFunction)replicate(newFunc);

        return newFunc;
    }
}
