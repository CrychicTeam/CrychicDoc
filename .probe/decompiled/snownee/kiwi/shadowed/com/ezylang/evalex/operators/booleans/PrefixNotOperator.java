package snownee.kiwi.shadowed.com.ezylang.evalex.operators.booleans;

import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.AbstractOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.PrefixOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@PrefixOperator
public class PrefixNotOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(Expression expression, Token operatorToken, EvaluationValue... operands) {
        return expression.convertValue(!operands[0].getBooleanValue());
    }
}