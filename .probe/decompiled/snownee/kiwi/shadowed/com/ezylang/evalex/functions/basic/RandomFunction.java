package snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic;

import java.security.SecureRandom;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

public class RandomFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        SecureRandom secureRandom = new SecureRandom();
        return expression.convertDoubleValue(secureRandom.nextDouble());
    }
}