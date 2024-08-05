package snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "value", nonNegative = true)
public class SqrtFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        BigDecimal x = parameterValues[0].getNumberValue();
        MathContext mathContext = expression.getConfiguration().getMathContext();
        if (x.compareTo(BigDecimal.ZERO) == 0) {
            return expression.convertValue(BigDecimal.ZERO);
        } else {
            BigInteger n = x.movePointRight(mathContext.getPrecision() << 1).toBigInteger();
            int bits = n.bitLength() + 1 >> 1;
            BigInteger ix = n.shiftRight(bits);
            BigInteger test;
            do {
                BigInteger ixPrev = ix;
                ix = ix.add(n.divide(ix)).shiftRight(1);
                Thread.yield();
                test = ix.subtract(ixPrev).abs();
            } while (test.compareTo(BigInteger.ZERO) != 0 && test.compareTo(BigInteger.ONE) != 0);
            return expression.convertValue(new BigDecimal(ix, mathContext.getPrecision()));
        }
    }
}