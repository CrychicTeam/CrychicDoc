package snownee.kiwi.shadowed.com.ezylang.evalex.operators.booleans;

import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.AbstractOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.InfixOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@InfixOperator(precedence = 7)
public class InfixNotEqualsOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(Expression expression, Token operatorToken, EvaluationValue... operands) {
        if (operands[0].getDataType() != operands[1].getDataType()) {
            return EvaluationValue.booleanValue(true);
        } else {
            return operands[0].isNullValue() && operands[1].isNullValue() ? EvaluationValue.booleanValue(false) : expression.convertValue(operands[0].compareTo(operands[1]) != 0);
        }
    }
}