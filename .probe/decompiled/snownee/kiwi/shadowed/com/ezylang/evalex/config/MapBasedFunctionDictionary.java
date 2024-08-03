package snownee.kiwi.shadowed.com.ezylang.evalex.config;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionIfc;

public class MapBasedFunctionDictionary implements FunctionDictionaryIfc {

    private final Map<String, FunctionIfc> functions = new TreeMap(String.CASE_INSENSITIVE_ORDER);

    public static FunctionDictionaryIfc ofFunctions(Entry<String, FunctionIfc>... functions) {
        FunctionDictionaryIfc dictionary = new MapBasedFunctionDictionary();
        Arrays.stream(functions).forEach(entry -> dictionary.addFunction((String) entry.getKey(), (FunctionIfc) entry.getValue()));
        return dictionary;
    }

    @Override
    public FunctionIfc getFunction(String functionName) {
        return (FunctionIfc) this.functions.get(functionName);
    }

    @Override
    public void addFunction(String functionName, FunctionIfc function) {
        this.functions.put(functionName, function);
    }
}