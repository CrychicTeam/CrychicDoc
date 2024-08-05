package snownee.kiwi.shadowed.com.ezylang.evalex.operators.arithmetic;

import java.time.Duration;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.AbstractOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.InfixOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@InfixOperator(precedence = 20)
public class InfixPlusOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(Expression expression, Token operatorToken, EvaluationValue... operands) {
        EvaluationValue leftOperand = operands[0];
        EvaluationValue rightOperand = operands[1];
        if (leftOperand.isNumberValue() && rightOperand.isNumberValue()) {
            return expression.convertValue(leftOperand.getNumberValue().add(rightOperand.getNumberValue(), expression.getConfiguration().getMathContext()));
        } else if (leftOperand.isDateTimeValue() && rightOperand.isDurationValue()) {
            return expression.convertValue(leftOperand.getDateTimeValue().plus(rightOperand.getDurationValue()));
        } else if (leftOperand.isDurationValue() && rightOperand.isDurationValue()) {
            return expression.convertValue(leftOperand.getDurationValue().plus(rightOperand.getDurationValue()));
        } else {
            return leftOperand.isDateTimeValue() && rightOperand.isNumberValue() ? expression.convertValue(leftOperand.getDateTimeValue().plus(Duration.ofMillis(rightOperand.getNumberValue().longValue()))) : expression.convertValue(leftOperand.getStringValue() + rightOperand.getStringValue());
        }
    }
}