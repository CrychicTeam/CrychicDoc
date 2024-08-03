package snownee.kiwi.shadowed.com.ezylang.evalex.data.conversion;

import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;

public interface EvaluationValueConverterIfc {

    EvaluationValue convertObject(Object var1, ExpressionConfiguration var2);
}