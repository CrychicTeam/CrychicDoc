package snownee.kiwi.shadowed.com.ezylang.evalex.operators.arithmetic;

import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.AbstractOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.InfixOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@InfixOperator(precedence = 30)
public class InfixMultiplicationOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(Expression expression, Token operatorToken, EvaluationValue... operands) throws EvaluationException {
        EvaluationValue leftOperand = operands[0];
        EvaluationValue rightOperand = operands[1];
        if (leftOperand.isNumberValue() && rightOperand.isNumberValue()) {
            return expression.convertValue(leftOperand.getNumberValue().multiply(rightOperand.getNumberValue(), expression.getConfiguration().getMathContext()));
        } else {
            throw EvaluationException.ofUnsupportedDataTypeInOperation(operatorToken);
        }
    }
}