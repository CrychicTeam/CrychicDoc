package snownee.kiwi.shadowed.com.ezylang.evalex.config;

import snownee.kiwi.shadowed.com.ezylang.evalex.operators.OperatorIfc;

public interface OperatorDictionaryIfc {

    void addOperator(String var1, OperatorIfc var2);

    default boolean hasPrefixOperator(String operatorString) {
        return this.getPrefixOperator(operatorString) != null;
    }

    default boolean hasPostfixOperator(String operatorString) {
        return this.getPostfixOperator(operatorString) != null;
    }

    default boolean hasInfixOperator(String operatorString) {
        return this.getInfixOperator(operatorString) != null;
    }

    OperatorIfc getPrefixOperator(String var1);

    OperatorIfc getPostfixOperator(String var1);

    OperatorIfc getInfixOperator(String var1);
}