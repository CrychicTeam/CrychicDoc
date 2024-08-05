package snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic;

import java.math.RoundingMode;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "value")
public class FloorFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        EvaluationValue value = parameterValues[0];
        return expression.convertValue(value.getNumberValue().setScale(0, RoundingMode.FLOOR));
    }
}