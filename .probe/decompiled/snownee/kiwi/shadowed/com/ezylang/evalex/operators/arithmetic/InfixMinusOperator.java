package snownee.kiwi.shadowed.com.ezylang.evalex.operators.arithmetic;

import java.time.Duration;
import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.AbstractOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.InfixOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@InfixOperator(precedence = 20)
public class InfixMinusOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(Expression expression, Token operatorToken, EvaluationValue... operands) throws EvaluationException {
        EvaluationValue leftOperand = operands[0];
        EvaluationValue rightOperand = operands[1];
        if (leftOperand.isNumberValue() && rightOperand.isNumberValue()) {
            return expression.convertValue(leftOperand.getNumberValue().subtract(rightOperand.getNumberValue(), expression.getConfiguration().getMathContext()));
        } else if (leftOperand.isDateTimeValue() && rightOperand.isDateTimeValue()) {
            return expression.convertValue(Duration.ofMillis(leftOperand.getDateTimeValue().toEpochMilli() - rightOperand.getDateTimeValue().toEpochMilli()));
        } else if (leftOperand.isDateTimeValue() && rightOperand.isDurationValue()) {
            return expression.convertValue(leftOperand.getDateTimeValue().minus(rightOperand.getDurationValue()));
        } else if (leftOperand.isDurationValue() && rightOperand.isDurationValue()) {
            return expression.convertValue(leftOperand.getDurationValue().minus(rightOperand.getDurationValue()));
        } else if (leftOperand.isDateTimeValue() && rightOperand.isNumberValue()) {
            return expression.convertValue(leftOperand.getDateTimeValue().minus(Duration.ofMillis(rightOperand.getNumberValue().longValue())));
        } else {
            throw EvaluationException.ofUnsupportedDataTypeInOperation(operatorToken);
        }
    }
}