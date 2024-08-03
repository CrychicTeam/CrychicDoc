package snownee.kiwi.shadowed.com.ezylang.evalex.data.conversion;

import java.time.Duration;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;

public class DurationConverter implements ConverterIfc {

    @Override
    public EvaluationValue convert(Object object, ExpressionConfiguration configuration) {
        return EvaluationValue.durationValue((Duration) object);
    }

    @Override
    public boolean canConvert(Object object) {
        return object instanceof Duration;
    }
}