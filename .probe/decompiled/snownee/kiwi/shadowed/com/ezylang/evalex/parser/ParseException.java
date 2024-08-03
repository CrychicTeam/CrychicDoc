package snownee.kiwi.shadowed.com.ezylang.evalex.parser;

import lombok.Generated;
import snownee.kiwi.shadowed.com.ezylang.evalex.BaseException;

public class ParseException extends BaseException {

    public ParseException(int startPosition, int endPosition, String tokenString, String message) {
        super(startPosition, endPosition, tokenString, message);
    }

    public ParseException(String expression, String message) {
        super(1, expression.length(), expression, message);
    }

    public ParseException(Token token, String message) {
        super(token.getStartPosition(), token.getStartPosition() + token.getValue().length() - 1, token.getValue(), message);
    }

    @Generated
    @Override
    public String toString() {
        return "ParseException(super=" + super.toString() + ")";
    }
}