package snownee.kiwi.shadowed.com.ezylang.evalex.operators.arithmetic;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.AbstractOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.InfixOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@InfixOperator(precedence = 40, leftAssociative = false)
public class InfixPowerOfOperator extends AbstractOperator {

    @Override
    public EvaluationValue evaluate(Expression expression, Token operatorToken, EvaluationValue... operands) throws EvaluationException {
        EvaluationValue leftOperand = operands[0];
        EvaluationValue rightOperand = operands[1];
        if (leftOperand.isNumberValue() && rightOperand.isNumberValue()) {
            MathContext mathContext = expression.getConfiguration().getMathContext();
            BigDecimal v1 = leftOperand.getNumberValue();
            BigDecimal v2 = rightOperand.getNumberValue();
            int signOf2 = v2.signum();
            double dn1 = v1.doubleValue();
            v2 = v2.multiply(new BigDecimal(signOf2));
            BigDecimal remainderOf2 = v2.remainder(BigDecimal.ONE);
            BigDecimal n2IntPart = v2.subtract(remainderOf2);
            BigDecimal intPow = v1.pow(n2IntPart.intValueExact(), mathContext);
            BigDecimal doublePow = BigDecimal.valueOf(Math.pow(dn1, remainderOf2.doubleValue()));
            BigDecimal result = intPow.multiply(doublePow, mathContext);
            if (signOf2 == -1) {
                result = BigDecimal.ONE.divide(result, mathContext.getPrecision(), RoundingMode.HALF_UP);
            }
            return expression.convertValue(result);
        } else {
            throw EvaluationException.ofUnsupportedDataTypeInOperation(operatorToken);
        }
    }

    @Override
    public int getPrecedence(ExpressionConfiguration configuration) {
        return configuration.getPowerOfPrecedence();
    }
}