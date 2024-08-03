package snownee.kiwi.shadowed.com.ezylang.evalex.operators.arithmetic;

import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.AbstractOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.PrefixOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@PrefixOperator(leftAssociative = false)
public class PrefixMinusOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(Expression expression, Token operatorToken, EvaluationValue... operands) throws EvaluationException {
        EvaluationValue operand = operands[0];
        if (operand.isNumberValue()) {
            return expression.convertValue(operand.getNumberValue().negate(expression.getConfiguration().getMathContext()));
        } else {
            throw EvaluationException.ofUnsupportedDataTypeInOperation(operatorToken);
        }
    }
}