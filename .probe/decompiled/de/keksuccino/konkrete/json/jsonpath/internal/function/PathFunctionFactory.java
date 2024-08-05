package de.keksuccino.konkrete.json.jsonpath.internal.function;

import de.keksuccino.konkrete.json.jsonpath.InvalidPathException;
import de.keksuccino.konkrete.json.jsonpath.internal.function.json.Append;
import de.keksuccino.konkrete.json.jsonpath.internal.function.json.KeySetFunction;
import de.keksuccino.konkrete.json.jsonpath.internal.function.numeric.Average;
import de.keksuccino.konkrete.json.jsonpath.internal.function.numeric.Max;
import de.keksuccino.konkrete.json.jsonpath.internal.function.numeric.Min;
import de.keksuccino.konkrete.json.jsonpath.internal.function.numeric.StandardDeviation;
import de.keksuccino.konkrete.json.jsonpath.internal.function.numeric.Sum;
import de.keksuccino.konkrete.json.jsonpath.internal.function.text.Concatenate;
import de.keksuccino.konkrete.json.jsonpath.internal.function.text.Length;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PathFunctionFactory {

    public static final Map<String, Class> FUNCTIONS;

    public static PathFunction newFunction(String name) throws InvalidPathException {
        Class functionClazz = (Class) FUNCTIONS.get(name);
        if (functionClazz == null) {
            throw new InvalidPathException("Function with name: " + name + " does not exist.");
        } else {
            try {
                return (PathFunction) functionClazz.newInstance();
            } catch (Exception var3) {
                throw new InvalidPathException("Function of name: " + name + " cannot be created", var3);
            }
        }
    }

    static {
        Map<String, Class> map = new HashMap();
        map.put("avg", Average.class);
        map.put("stddev", StandardDeviation.class);
        map.put("sum", Sum.class);
        map.put("min", Min.class);
        map.put("max", Max.class);
        map.put("concat", Concatenate.class);
        map.put("length", Length.class);
        map.put("size", Length.class);
        map.put("append", Append.class);
        map.put("keys", KeySetFunction.class);
        FUNCTIONS = Collections.unmodifiableMap(map);
    }
}