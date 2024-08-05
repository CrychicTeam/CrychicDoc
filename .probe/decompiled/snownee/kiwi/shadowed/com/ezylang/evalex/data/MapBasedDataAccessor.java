package snownee.kiwi.shadowed.com.ezylang.evalex.data;

import java.util.Map;
import java.util.TreeMap;

public class MapBasedDataAccessor implements DataAccessorIfc {

    private final Map<String, EvaluationValue> variables = new TreeMap(String.CASE_INSENSITIVE_ORDER);

    @Override
    public EvaluationValue getData(String variable) {
        return (EvaluationValue) this.variables.get(variable);
    }

    @Override
    public void setData(String variable, EvaluationValue value) {
        this.variables.put(variable, value);
    }
}