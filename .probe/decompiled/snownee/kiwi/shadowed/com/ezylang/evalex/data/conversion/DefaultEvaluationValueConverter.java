package snownee.kiwi.shadowed.com.ezylang.evalex.data.conversion;

import java.util.Arrays;
import java.util.List;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;

public class DefaultEvaluationValueConverter implements EvaluationValueConverterIfc {

    static List<ConverterIfc> converters = Arrays.asList(new NumberConverter(), new StringConverter(), new BooleanConverter(), new DateTimeConverter(), new DurationConverter(), new ExpressionNodeConverter(), new ArrayConverter(), new StructureConverter());

    @Override
    public EvaluationValue convertObject(Object object, ExpressionConfiguration configuration) {
        if (object == null) {
            return EvaluationValue.nullValue();
        } else if (object instanceof EvaluationValue) {
            return (EvaluationValue) object;
        } else {
            for (ConverterIfc converter : converters) {
                if (converter.canConvert(object)) {
                    return converter.convert(object, configuration);
                }
            }
            throw new IllegalArgumentException("Unsupported data type '" + object.getClass().getName() + "'");
        }
    }
}