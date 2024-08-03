package snownee.kiwi.shadowed.com.ezylang.evalex.config;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.OperatorIfc;

public class MapBasedOperatorDictionary implements OperatorDictionaryIfc {

    final Map<String, OperatorIfc> prefixOperators = new TreeMap(String.CASE_INSENSITIVE_ORDER);

    final Map<String, OperatorIfc> postfixOperators = new TreeMap(String.CASE_INSENSITIVE_ORDER);

    final Map<String, OperatorIfc> infixOperators = new TreeMap(String.CASE_INSENSITIVE_ORDER);

    public static OperatorDictionaryIfc ofOperators(Entry<String, OperatorIfc>... operators) {
        OperatorDictionaryIfc dictionary = new MapBasedOperatorDictionary();
        Arrays.stream(operators).forEach(entry -> dictionary.addOperator((String) entry.getKey(), (OperatorIfc) entry.getValue()));
        return dictionary;
    }

    @Override
    public void addOperator(String operatorString, OperatorIfc operator) {
        if (operator.isPrefix()) {
            this.prefixOperators.put(operatorString, operator);
        } else if (operator.isPostfix()) {
            this.postfixOperators.put(operatorString, operator);
        } else {
            this.infixOperators.put(operatorString, operator);
        }
    }

    @Override
    public OperatorIfc getPrefixOperator(String operatorString) {
        return (OperatorIfc) this.prefixOperators.get(operatorString);
    }

    @Override
    public OperatorIfc getPostfixOperator(String operatorString) {
        return (OperatorIfc) this.postfixOperators.get(operatorString);
    }

    @Override
    public OperatorIfc getInfixOperator(String operatorString) {
        return (OperatorIfc) this.infixOperators.get(operatorString);
    }
}