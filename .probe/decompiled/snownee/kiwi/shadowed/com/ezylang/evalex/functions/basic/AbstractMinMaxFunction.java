package snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic;

import java.math.BigDecimal;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;

@FunctionParameter(name = "value", isVarArg = true)
public abstract class AbstractMinMaxFunction extends AbstractFunction {

    BigDecimal findMinOrMax(BigDecimal current, EvaluationValue parameter, boolean findMin) {
        if (parameter.isArrayValue()) {
            for (EvaluationValue element : parameter.getArrayValue()) {
                current = this.findMinOrMax(current, element, findMin);
            }
        } else {
            current = this.compareAndAssign(current, parameter.getNumberValue(), findMin);
        }
        return current;
    }

    BigDecimal compareAndAssign(BigDecimal current, BigDecimal newValue, boolean findMin) {
        if (current == null || (findMin ? newValue.compareTo(current) < 0 : newValue.compareTo(current) > 0)) {
            current = newValue;
        }
        return current;
    }
}