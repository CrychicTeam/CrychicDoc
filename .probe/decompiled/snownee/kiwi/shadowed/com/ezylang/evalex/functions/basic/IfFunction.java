package snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic;

import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameters;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameters({ @FunctionParameter(name = "condition"), @FunctionParameter(name = "resultIfTrue", isLazy = true), @FunctionParameter(name = "resultIfFalse", isLazy = true) })
public class IfFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
        return Boolean.TRUE.equals(parameterValues[0].getBooleanValue()) ? expression.evaluateSubtree(parameterValues[1].getExpressionNode()) : expression.evaluateSubtree(parameterValues[2].getExpressionNode());
    }
}