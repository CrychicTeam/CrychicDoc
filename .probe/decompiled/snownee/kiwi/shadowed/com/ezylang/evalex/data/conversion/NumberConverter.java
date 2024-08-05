package snownee.kiwi.shadowed.com.ezylang.evalex.data.conversion;

import java.math.BigDecimal;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;

public class NumberConverter implements ConverterIfc {

    @Override
    public EvaluationValue convert(Object object, ExpressionConfiguration configuration) {
        BigDecimal bigDecimal;
        if (object instanceof BigDecimal) {
            bigDecimal = (BigDecimal) object;
        } else if (object instanceof Double) {
            bigDecimal = new BigDecimal(Double.toString((Double) object), configuration.getMathContext());
        } else if (object instanceof Float) {
            bigDecimal = BigDecimal.valueOf((double) ((Float) object).floatValue());
        } else if (object instanceof Integer) {
            bigDecimal = BigDecimal.valueOf((long) ((Integer) object).intValue());
        } else if (object instanceof Long) {
            bigDecimal = BigDecimal.valueOf((Long) object);
        } else if (object instanceof Short) {
            bigDecimal = BigDecimal.valueOf((long) ((Short) object).shortValue());
        } else {
            if (!(object instanceof Byte)) {
                throw this.illegalArgument(object);
            }
            bigDecimal = BigDecimal.valueOf((long) ((Byte) object).byteValue());
        }
        return EvaluationValue.numberValue(bigDecimal);
    }

    @Override
    public boolean canConvert(Object object) {
        return object instanceof BigDecimal || object instanceof Double || object instanceof Float || object instanceof Integer || object instanceof Long || object instanceof Short || object instanceof Byte;
    }
}