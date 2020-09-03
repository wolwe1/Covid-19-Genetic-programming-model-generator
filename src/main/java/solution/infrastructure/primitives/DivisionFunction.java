package solution.infrastructure.primitives;

import gpLibrary.primitives.Node;
import gpLibrary.primitives.functions.GeneticFunction;

public class DivisionFunction extends GeneticFunction<Double> {

    public DivisionFunction() {
        super("D");
    }

    @Override
    public Double Operation() {
        Double baseValue = _children.get(0).getValue();

        for (int i = 1; i < _children.size(); i++) {
            baseValue /= _children.get(i).getValue();
        }
        return baseValue;
    }

    @Override
    public Node<Double> getCopy() {
        return new DivisionFunction();
    }
}
