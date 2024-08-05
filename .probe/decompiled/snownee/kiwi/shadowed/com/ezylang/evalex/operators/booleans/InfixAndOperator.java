package snownee.kiwi.shadowed.com.ezylang.evalex.operators.booleans;

import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.AbstractOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.InfixOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@InfixOperator(precedence = 4, operandsLazy = true)
public class InfixAndOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(Expression expression, Token operatorToken, EvaluationValue... operands) throws EvaluationException {
        return expression.convertValue(expression.evaluateSubtree(operands[0].getExpressionNode()).getBooleanValue() && expression.evaluateSubtree(operands[1].getExpressionNode()).getBooleanValue());
    }
}