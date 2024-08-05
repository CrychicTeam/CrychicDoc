package snownee.kiwi.shadowed.com.ezylang.evalex.data.conversion;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;

public class StructureConverter implements ConverterIfc {

    @Override
    public EvaluationValue convert(Object object, ExpressionConfiguration configuration) {
        Map<String, EvaluationValue> structure = new HashMap();
        for (Entry<?, ?> entry : ((Map) object).entrySet()) {
            String name = entry.getKey().toString();
            structure.put(name, new EvaluationValue(entry.getValue(), configuration));
        }
        return EvaluationValue.structureValue(structure);
    }

    @Override
    public boolean canConvert(Object object) {
        return object instanceof Map;
    }
}