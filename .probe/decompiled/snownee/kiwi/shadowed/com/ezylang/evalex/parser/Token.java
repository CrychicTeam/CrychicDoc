package snownee.kiwi.shadowed.com.ezylang.evalex.parser;

import lombok.Generated;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionIfc;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.OperatorIfc;

public final class Token {

    private final int startPosition;

    private final String value;

    private final Token.TokenType type;

    private final FunctionIfc functionDefinition;

    private final OperatorIfc operatorDefinition;

    public Token(int startPosition, String value, Token.TokenType type) {
        this(startPosition, value, type, null, null);
    }

    public Token(int startPosition, String value, Token.TokenType type, FunctionIfc functionDefinition) {
        this(startPosition, value, type, functionDefinition, null);
    }

    public Token(int startPosition, String value, Token.TokenType type, OperatorIfc operatorDefinition) {
        this(startPosition, value, type, null, operatorDefinition);
    }

    @Generated
    public int getStartPosition() {
        return this.startPosition;
    }

    @Generated
    public String getValue() {
        return this.value;
    }

    @Generated
    public Token.TokenType getType() {
        return this.type;
    }

    @Generated
    public FunctionIfc getFunctionDefinition() {
        return this.functionDefinition;
    }

    @Generated
    public OperatorIfc getOperatorDefinition() {
        return this.operatorDefinition;
    }

    @Generated
    public String toString() {
        return "Token(startPosition=" + this.getStartPosition() + ", value=" + this.getValue() + ", type=" + this.getType() + ")";
    }

    @Generated
    public Token(int startPosition, String value, Token.TokenType type, FunctionIfc functionDefinition, OperatorIfc operatorDefinition) {
        this.startPosition = startPosition;
        this.value = value;
        this.type = type;
        this.functionDefinition = functionDefinition;
        this.operatorDefinition = operatorDefinition;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Token)) {
            return false;
        } else {
            Token other = (Token) o;
            if (this.startPosition != other.startPosition) {
                return false;
            } else {
                Object this$value = this.value;
                Object other$value = other.value;
                if (this$value == null ? other$value == null : this$value.equals(other$value)) {
                    Object this$type = this.type;
                    Object other$type = other.type;
                    return this$type == null ? other$type == null : this$type.equals(other$type);
                } else {
                    return false;
                }
            }
        }
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.startPosition;
        Object $value = this.value;
        result = result * 59 + ($value == null ? 43 : $value.hashCode());
        Object $type = this.type;
        return result * 59 + ($type == null ? 43 : $type.hashCode());
    }

    public static enum TokenType {

        BRACE_OPEN,
        BRACE_CLOSE,
        COMMA,
        STRING_LITERAL,
        NUMBER_LITERAL,
        VARIABLE_OR_CONSTANT,
        INFIX_OPERATOR,
        PREFIX_OPERATOR,
        POSTFIX_OPERATOR,
        FUNCTION,
        FUNCTION_PARAM_START,
        ARRAY_OPEN,
        ARRAY_CLOSE,
        ARRAY_INDEX,
        STRUCTURE_SEPARATOR
    }
}