package snownee.kiwi.shadowed.com.ezylang.evalex;

import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

public class EvaluationException extends BaseException {

    public EvaluationException(Token token, String message) {
        super(token.getStartPosition(), token.getStartPosition() + token.getValue().length(), token.getValue(), message);
    }

    public static EvaluationException ofUnsupportedDataTypeInOperation(Token token) {
        return new EvaluationException(token, "Unsupported data types in operation");
    }
}