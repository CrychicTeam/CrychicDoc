package snownee.kiwi.shadowed.com.ezylang.evalex.functions.string;

import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "value")
public class StringUpperFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        return expression.convertValue(parameterValues[0].getStringValue().toUpperCase());
    }
}