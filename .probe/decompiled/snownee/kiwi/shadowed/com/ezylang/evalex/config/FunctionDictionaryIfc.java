package snownee.kiwi.shadowed.com.ezylang.evalex.config;

import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionIfc;

public interface FunctionDictionaryIfc {

    void addFunction(String var1, FunctionIfc var2);

    default boolean hasFunction(String functionName) {
        return this.getFunction(functionName) != null;
    }

    FunctionIfc getFunction(String var1);
}