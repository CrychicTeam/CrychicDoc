package snownee.kiwi.shadowed.com.ezylang.evalex.data.conversion;

import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;

public interface ConverterIfc {

    EvaluationValue convert(Object var1, ExpressionConfiguration var2);

    boolean canConvert(Object var1);

    default IllegalArgumentException illegalArgument(Object object) {
        return new IllegalArgumentException("Unsupported data type '" + object.getClass().getName() + "'");
    }
}