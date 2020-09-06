package solution.infrastructure.functions.primitives;

import gpLibrary.primitives.Node;
import gpLibrary.primitives.functions.GeneticFunction;

public class DivisionFunction extends GeneticFunction<Double> {

    public DivisionFunction() {
        super("D");
    }

    @Override
    public Double Operation() throws Exception {
        Double baseValue = _children.get(0).getValue();

        for (int i = 1; i < _children.size(); i++) {
            Double val = _children.get(i).getValue();

            if(val == 0d)
                baseValue = 0d;
            else
                baseValue /= val;
        }
        return baseValue;
    }

    @Override
    public Node<Double> getCopy() {
        var newFunc = new DivisionFunction();
        newFunc = (DivisionFunction) replicate(newFunc);

        return newFunc;
    }

}
