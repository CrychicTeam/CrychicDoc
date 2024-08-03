package de.keksuccino.konkrete.json.jsonpath.internal.filter;

import de.keksuccino.konkrete.json.jsonpath.InvalidPathException;
import java.util.Locale;

public enum RelationalOperator {

    GTE(">="),
    LTE("<="),
    EQ("=="),
    TSEQ("==="),
    NE("!="),
    TSNE("!=="),
    LT("<"),
    GT(">"),
    REGEX("=~"),
    NIN("NIN"),
    IN("IN"),
    CONTAINS("CONTAINS"),
    ALL("ALL"),
    SIZE("SIZE"),
    EXISTS("EXISTS"),
    TYPE("TYPE"),
    MATCHES("MATCHES"),
    EMPTY("EMPTY"),
    SUBSETOF("SUBSETOF"),
    ANYOF("ANYOF"),
    NONEOF("NONEOF");

    private final String operatorString;

    private RelationalOperator(String operatorString) {
        this.operatorString = operatorString;
    }

    public static RelationalOperator fromString(String operatorString) {
        String upperCaseOperatorString = operatorString.toUpperCase(Locale.ROOT);
        for (RelationalOperator operator : values()) {
            if (operator.operatorString.equals(upperCaseOperatorString)) {
                return operator;
            }
        }
        throw new InvalidPathException("Filter operator " + operatorString + " is not supported!");
    }

    public String toString() {
        return this.operatorString;
    }
}