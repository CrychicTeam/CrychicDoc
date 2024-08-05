package snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic;

import java.math.BigDecimal;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameters;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameters({ @FunctionParameter(name = "firstValue"), @FunctionParameter(name = "value", isVarArg = true) })
public class MaxFunction extends AbstractMinMaxFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        BigDecimal min = null;
        for (EvaluationValue parameter : parameterValues) {
            min = this.findMinOrMax(min, parameter, false);
        }
        return expression.convertValue(min);
    }
}