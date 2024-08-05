package snownee.kiwi.shadowed.com.ezylang.evalex.data.conversion;

import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;

public class BooleanConverter implements ConverterIfc {

    @Override
    public EvaluationValue convert(Object object, ExpressionConfiguration configuration) {
        return EvaluationValue.booleanValue((Boolean) object);
    }

    @Override
    public boolean canConvert(Object object) {
        return object instanceof Boolean;
    }
}