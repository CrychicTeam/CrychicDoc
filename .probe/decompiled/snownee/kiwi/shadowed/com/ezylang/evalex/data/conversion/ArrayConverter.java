package snownee.kiwi.shadowed.com.ezylang.evalex.data.conversion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;

public class ArrayConverter implements ConverterIfc {

    @Override
    public EvaluationValue convert(Object object, ExpressionConfiguration configuration) {
        List<EvaluationValue> list;
        if (object.getClass().isArray()) {
            list = this.convertArray(object, configuration);
        } else {
            if (!(object instanceof List)) {
                throw this.illegalArgument(object);
            }
            list = convertList((List<?>) object, configuration);
        }
        return EvaluationValue.arrayValue(list);
    }

    @Override
    public boolean canConvert(Object object) {
        return object instanceof List || object.getClass().isArray();
    }

    private static List<EvaluationValue> convertList(List<?> object, ExpressionConfiguration configuration) {
        return (List<EvaluationValue>) object.stream().map(element -> new EvaluationValue(element, configuration)).collect(Collectors.toList());
    }

    private List<EvaluationValue> convertArray(Object array, ExpressionConfiguration configuration) {
        if (array instanceof int[]) {
            return this.convertIntArray((int[]) array, configuration);
        } else if (array instanceof long[]) {
            return this.convertLongArray((long[]) array, configuration);
        } else if (array instanceof double[]) {
            return this.convertDoubleArray((double[]) array, configuration);
        } else if (array instanceof float[]) {
            return this.convertFloatArray((float[]) array, configuration);
        } else if (array instanceof short[]) {
            return this.convertShortArray((short[]) array, configuration);
        } else if (array instanceof char[]) {
            return this.convertCharArray((char[]) array, configuration);
        } else if (array instanceof byte[]) {
            return this.convertByteArray((byte[]) array, configuration);
        } else {
            return array instanceof boolean[] ? this.convertBooleanArray((boolean[]) array, configuration) : this.convertObjectArray((Object[]) array, configuration);
        }
    }

    private List<EvaluationValue> convertIntArray(int[] array, ExpressionConfiguration configuration) {
        List<EvaluationValue> list = new ArrayList();
        for (int i : array) {
            list.add(new EvaluationValue(i, configuration));
        }
        return list;
    }

    private List<EvaluationValue> convertLongArray(long[] array, ExpressionConfiguration configuration) {
        List<EvaluationValue> list = new ArrayList();
        for (long l : array) {
            list.add(new EvaluationValue(l, configuration));
        }
        return list;
    }

    private List<EvaluationValue> convertDoubleArray(double[] array, ExpressionConfiguration configuration) {
        List<EvaluationValue> list = new ArrayList();
        for (double d : array) {
            list.add(new EvaluationValue(Double.valueOf(d), configuration));
        }
        return list;
    }

    private List<EvaluationValue> convertFloatArray(float[] array, ExpressionConfiguration configuration) {
        List<EvaluationValue> list = new ArrayList();
        for (float f : array) {
            list.add(new EvaluationValue(f, configuration));
        }
        return list;
    }

    private List<EvaluationValue> convertShortArray(short[] array, ExpressionConfiguration configuration) {
        List<EvaluationValue> list = new ArrayList();
        for (short s : array) {
            list.add(new EvaluationValue(s, configuration));
        }
        return list;
    }

    private List<EvaluationValue> convertCharArray(char[] array, ExpressionConfiguration configuration) {
        List<EvaluationValue> list = new ArrayList();
        for (char c : array) {
            list.add(new EvaluationValue(c, configuration));
        }
        return list;
    }

    private List<EvaluationValue> convertByteArray(byte[] array, ExpressionConfiguration configuration) {
        List<EvaluationValue> list = new ArrayList();
        for (byte b : array) {
            list.add(new EvaluationValue(b, configuration));
        }
        return list;
    }

    private List<EvaluationValue> convertBooleanArray(boolean[] array, ExpressionConfiguration configuration) {
        List<EvaluationValue> list = new ArrayList();
        for (boolean b : array) {
            list.add(new EvaluationValue(b, configuration));
        }
        return list;
    }

    private List<EvaluationValue> convertObjectArray(Object[] array, ExpressionConfiguration configuration) {
        List<EvaluationValue> list = new ArrayList();
        for (Object o : array) {
            list.add(new EvaluationValue(o, configuration));
        }
        return list;
    }
}