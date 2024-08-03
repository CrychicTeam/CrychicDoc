package snownee.kiwi.shadowed.com.ezylang.evalex.parser;

import java.util.ArrayList;
import java.util.List;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.FunctionDictionaryIfc;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.OperatorDictionaryIfc;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionIfc;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.OperatorIfc;

public class Tokenizer {

    private final String expressionString;

    private final OperatorDictionaryIfc operatorDictionary;

    private final FunctionDictionaryIfc functionDictionary;

    private final ExpressionConfiguration configuration;

    private final List<Token> tokens = new ArrayList();

    private int currentColumnIndex = 0;

    private int currentChar = -2;

    private int braceBalance;

    private int arrayBalance;

    public Tokenizer(String expressionString, ExpressionConfiguration configuration) {
        this.expressionString = expressionString;
        this.configuration = configuration;
        this.operatorDictionary = configuration.getOperatorDictionary();
        this.functionDictionary = configuration.getFunctionDictionary();
    }

    public List<Token> parse() throws ParseException {
        for (Token currentToken = this.getNextToken(); currentToken != null; currentToken = this.getNextToken()) {
            if (this.implicitMultiplicationPossible(currentToken)) {
                if (!this.configuration.isImplicitMultiplicationAllowed()) {
                    throw new ParseException(currentToken, "Missing operator");
                }
                Token multiplication = new Token(currentToken.getStartPosition(), "*", Token.TokenType.INFIX_OPERATOR, this.operatorDictionary.getInfixOperator("*"));
                this.tokens.add(multiplication);
            }
            this.validateToken(currentToken);
            this.tokens.add(currentToken);
        }
        if (this.braceBalance > 0) {
            throw new ParseException(this.expressionString, "Closing brace not found");
        } else if (this.arrayBalance > 0) {
            throw new ParseException(this.expressionString, "Closing array not found");
        } else {
            return this.tokens;
        }
    }

    private boolean implicitMultiplicationPossible(Token currentToken) {
        Token previousToken = this.getPreviousToken();
        return previousToken == null ? false : previousToken.getType() == Token.TokenType.BRACE_CLOSE && currentToken.getType() == Token.TokenType.BRACE_OPEN || previousToken.getType() == Token.TokenType.NUMBER_LITERAL && currentToken.getType() == Token.TokenType.VARIABLE_OR_CONSTANT || previousToken.getType() == Token.TokenType.NUMBER_LITERAL && currentToken.getType() == Token.TokenType.BRACE_OPEN;
    }

    private void validateToken(Token currentToken) throws ParseException {
        if (currentToken.getType() == Token.TokenType.STRUCTURE_SEPARATOR && this.getPreviousToken() == null) {
            throw new ParseException(currentToken, "Misplaced structure operator");
        } else {
            Token previousToken = this.getPreviousToken();
            if (previousToken != null && previousToken.getType() == Token.TokenType.INFIX_OPERATOR && this.invalidTokenAfterInfixOperator(currentToken)) {
                throw new ParseException(currentToken, "Unexpected token after infix operator");
            }
        }
    }

    private boolean invalidTokenAfterInfixOperator(Token token) {
        switch(token.getType()) {
            case INFIX_OPERATOR:
            case BRACE_CLOSE:
            case COMMA:
                return true;
            default:
                return false;
        }
    }

    private Token getNextToken() throws ParseException {
        this.skipBlanks();
        if (this.currentChar == -1) {
            return null;
        } else if (this.isAtStringLiteralStart()) {
            return this.parseStringLiteral();
        } else if (this.currentChar == 40) {
            return this.parseBraceOpen();
        } else if (this.currentChar == 41) {
            return this.parseBraceClose();
        } else if (this.currentChar == 91 && this.configuration.isArraysAllowed()) {
            return this.parseArrayOpen();
        } else if (this.currentChar == 93 && this.configuration.isArraysAllowed()) {
            return this.parseArrayClose();
        } else if (this.currentChar == 46 && !this.isNextCharNumberChar() && this.configuration.isStructuresAllowed()) {
            return this.parseStructureSeparator();
        } else if (this.currentChar == 44) {
            Token token = new Token(this.currentColumnIndex, ",", Token.TokenType.COMMA);
            this.consumeChar();
            return token;
        } else if (this.isAtIdentifierStart()) {
            return this.parseIdentifier();
        } else {
            return this.isAtNumberStart() ? this.parseNumberLiteral() : this.parseOperator();
        }
    }

    private Token parseStructureSeparator() throws ParseException {
        Token token = new Token(this.currentColumnIndex, ".", Token.TokenType.STRUCTURE_SEPARATOR);
        if (this.arrayOpenOrStructureSeparatorNotAllowed()) {
            throw new ParseException(token, "Structure separator not allowed here");
        } else {
            this.consumeChar();
            return token;
        }
    }

    private Token parseArrayClose() throws ParseException {
        Token token = new Token(this.currentColumnIndex, "]", Token.TokenType.ARRAY_CLOSE);
        if (!this.arrayCloseAllowed()) {
            throw new ParseException(token, "Array close not allowed here");
        } else {
            this.consumeChar();
            this.arrayBalance--;
            if (this.arrayBalance < 0) {
                throw new ParseException(token, "Unexpected closing array");
            } else {
                return token;
            }
        }
    }

    private Token parseArrayOpen() throws ParseException {
        Token token = new Token(this.currentColumnIndex, "[", Token.TokenType.ARRAY_OPEN);
        if (this.arrayOpenOrStructureSeparatorNotAllowed()) {
            throw new ParseException(token, "Array open not allowed here");
        } else {
            this.consumeChar();
            this.arrayBalance++;
            return token;
        }
    }

    private Token parseBraceClose() throws ParseException {
        Token token = new Token(this.currentColumnIndex, ")", Token.TokenType.BRACE_CLOSE);
        this.consumeChar();
        this.braceBalance--;
        if (this.braceBalance < 0) {
            throw new ParseException(token, "Unexpected closing brace");
        } else {
            return token;
        }
    }

    private Token parseBraceOpen() {
        Token token = new Token(this.currentColumnIndex, "(", Token.TokenType.BRACE_OPEN);
        this.consumeChar();
        this.braceBalance++;
        return token;
    }

    private Token getPreviousToken() {
        return this.tokens.isEmpty() ? null : (Token) this.tokens.get(this.tokens.size() - 1);
    }

    private Token parseOperator() throws ParseException {
        int tokenStartIndex = this.currentColumnIndex;
        StringBuilder tokenValue = new StringBuilder();
        boolean possibleNextOperatorFound;
        do {
            tokenValue.append((char) this.currentChar);
            String tokenString = tokenValue.toString();
            String possibleNextOperator = tokenString + (char) this.peekNextChar();
            possibleNextOperatorFound = this.prefixOperatorAllowed() && this.operatorDictionary.hasPrefixOperator(possibleNextOperator) || this.postfixOperatorAllowed() && this.operatorDictionary.hasPostfixOperator(possibleNextOperator) || this.infixOperatorAllowed() && this.operatorDictionary.hasInfixOperator(possibleNextOperator);
            this.consumeChar();
        } while (possibleNextOperatorFound);
        String tokenString = tokenValue.toString();
        if (this.prefixOperatorAllowed() && this.operatorDictionary.hasPrefixOperator(tokenString)) {
            OperatorIfc operator = this.operatorDictionary.getPrefixOperator(tokenString);
            return new Token(tokenStartIndex, tokenString, Token.TokenType.PREFIX_OPERATOR, operator);
        } else if (this.postfixOperatorAllowed() && this.operatorDictionary.hasPostfixOperator(tokenString)) {
            OperatorIfc operator = this.operatorDictionary.getPostfixOperator(tokenString);
            return new Token(tokenStartIndex, tokenString, Token.TokenType.POSTFIX_OPERATOR, operator);
        } else if (this.operatorDictionary.hasInfixOperator(tokenString)) {
            OperatorIfc operator = this.operatorDictionary.getInfixOperator(tokenString);
            return new Token(tokenStartIndex, tokenString, Token.TokenType.INFIX_OPERATOR, operator);
        } else if (tokenString.equals(".") && this.configuration.isStructuresAllowed()) {
            return new Token(tokenStartIndex, tokenString, Token.TokenType.STRUCTURE_SEPARATOR);
        } else {
            throw new ParseException(tokenStartIndex, tokenStartIndex + tokenString.length() - 1, tokenString, "Undefined operator '" + tokenString + "'");
        }
    }

    private boolean arrayOpenOrStructureSeparatorNotAllowed() {
        Token previousToken = this.getPreviousToken();
        if (previousToken == null) {
            return true;
        } else {
            switch(previousToken.getType()) {
                case BRACE_CLOSE:
                case VARIABLE_OR_CONSTANT:
                case ARRAY_CLOSE:
                case STRING_LITERAL:
                    return false;
                case COMMA:
                default:
                    return true;
            }
        }
    }

    private boolean arrayCloseAllowed() {
        Token previousToken = this.getPreviousToken();
        if (previousToken == null) {
            return false;
        } else {
            switch(previousToken.getType()) {
                case INFIX_OPERATOR:
                case COMMA:
                case BRACE_OPEN:
                case PREFIX_OPERATOR:
                case FUNCTION:
                case ARRAY_OPEN:
                    return false;
                case BRACE_CLOSE:
                case VARIABLE_OR_CONSTANT:
                case ARRAY_CLOSE:
                case STRING_LITERAL:
                default:
                    return true;
            }
        }
    }

    private boolean prefixOperatorAllowed() {
        Token previousToken = this.getPreviousToken();
        if (previousToken == null) {
            return true;
        } else {
            switch(previousToken.getType()) {
                case INFIX_OPERATOR:
                case COMMA:
                case BRACE_OPEN:
                case PREFIX_OPERATOR:
                    return true;
                case BRACE_CLOSE:
                case VARIABLE_OR_CONSTANT:
                case ARRAY_CLOSE:
                case STRING_LITERAL:
                default:
                    return false;
            }
        }
    }

    private boolean postfixOperatorAllowed() {
        Token previousToken = this.getPreviousToken();
        if (previousToken == null) {
            return false;
        } else {
            switch(previousToken.getType()) {
                case BRACE_CLOSE:
                case VARIABLE_OR_CONSTANT:
                case STRING_LITERAL:
                case NUMBER_LITERAL:
                    return true;
                case COMMA:
                case ARRAY_CLOSE:
                case BRACE_OPEN:
                case PREFIX_OPERATOR:
                case FUNCTION:
                case ARRAY_OPEN:
                default:
                    return false;
            }
        }
    }

    private boolean infixOperatorAllowed() {
        Token previousToken = this.getPreviousToken();
        if (previousToken == null) {
            return false;
        } else {
            switch(previousToken.getType()) {
                case BRACE_CLOSE:
                case VARIABLE_OR_CONSTANT:
                case STRING_LITERAL:
                case NUMBER_LITERAL:
                case POSTFIX_OPERATOR:
                    return true;
                case COMMA:
                case ARRAY_CLOSE:
                case BRACE_OPEN:
                case PREFIX_OPERATOR:
                case FUNCTION:
                case ARRAY_OPEN:
                default:
                    return false;
            }
        }
    }

    private Token parseNumberLiteral() throws ParseException {
        int nextChar = this.peekNextChar();
        return this.currentChar != 48 || nextChar != 120 && nextChar != 88 ? this.parseDecimalNumberLiteral() : this.parseHexNumberLiteral();
    }

    private Token parseDecimalNumberLiteral() throws ParseException {
        int tokenStartIndex = this.currentColumnIndex;
        StringBuilder tokenValue = new StringBuilder();
        int lastChar = -1;
        boolean scientificNotation = false;
        boolean dotEncountered = false;
        while (this.currentChar != -1 && this.isAtNumberChar()) {
            if (this.currentChar == 46 && dotEncountered) {
                tokenValue.append((char) this.currentChar);
                throw new ParseException(new Token(tokenStartIndex, tokenValue.toString(), Token.TokenType.NUMBER_LITERAL), "Number contains more than one decimal point");
            }
            if (this.currentChar == 46) {
                dotEncountered = true;
            }
            if (this.currentChar == 101 || this.currentChar == 69) {
                scientificNotation = true;
            }
            tokenValue.append((char) this.currentChar);
            lastChar = this.currentChar;
            this.consumeChar();
        }
        if (!scientificNotation || lastChar != 101 && lastChar != 69 && lastChar != 43 && lastChar != 45 && lastChar != 46) {
            return new Token(tokenStartIndex, tokenValue.toString(), Token.TokenType.NUMBER_LITERAL);
        } else {
            throw new ParseException(new Token(tokenStartIndex, tokenValue.toString(), Token.TokenType.NUMBER_LITERAL), "Illegal scientific format");
        }
    }

    private Token parseHexNumberLiteral() {
        int tokenStartIndex = this.currentColumnIndex;
        StringBuilder tokenValue = new StringBuilder();
        tokenValue.append((char) this.currentChar);
        this.consumeChar();
        tokenValue.append((char) this.currentChar);
        this.consumeChar();
        while (this.currentChar != -1 && this.isAtHexChar()) {
            tokenValue.append((char) this.currentChar);
            this.consumeChar();
        }
        return new Token(tokenStartIndex, tokenValue.toString(), Token.TokenType.NUMBER_LITERAL);
    }

    private Token parseIdentifier() throws ParseException {
        int tokenStartIndex = this.currentColumnIndex;
        StringBuilder tokenValue = new StringBuilder();
        while (this.currentChar != -1 && this.isAtIdentifierChar()) {
            tokenValue.append((char) this.currentChar);
            this.consumeChar();
        }
        String tokenName = tokenValue.toString();
        if (this.prefixOperatorAllowed() && this.operatorDictionary.hasPrefixOperator(tokenName)) {
            return new Token(tokenStartIndex, tokenName, Token.TokenType.PREFIX_OPERATOR, this.operatorDictionary.getPrefixOperator(tokenName));
        } else if (this.postfixOperatorAllowed() && this.operatorDictionary.hasPostfixOperator(tokenName)) {
            return new Token(tokenStartIndex, tokenName, Token.TokenType.POSTFIX_OPERATOR, this.operatorDictionary.getPostfixOperator(tokenName));
        } else if (this.operatorDictionary.hasInfixOperator(tokenName)) {
            return new Token(tokenStartIndex, tokenName, Token.TokenType.INFIX_OPERATOR, this.operatorDictionary.getInfixOperator(tokenName));
        } else {
            this.skipBlanks();
            if (this.currentChar == 40) {
                if (!this.functionDictionary.hasFunction(tokenName)) {
                    throw new ParseException(tokenStartIndex, this.currentColumnIndex, tokenName, "Undefined function '" + tokenName + "'");
                } else {
                    FunctionIfc function = this.functionDictionary.getFunction(tokenName);
                    return new Token(tokenStartIndex, tokenName, Token.TokenType.FUNCTION, function);
                }
            } else {
                return new Token(tokenStartIndex, tokenName, Token.TokenType.VARIABLE_OR_CONSTANT);
            }
        }
    }

    Token parseStringLiteral() throws ParseException {
        int startChar = this.currentChar;
        int tokenStartIndex = this.currentColumnIndex;
        StringBuilder tokenValue = new StringBuilder();
        this.consumeChar();
        boolean inQuote;
        for (inQuote = true; inQuote && this.currentChar != -1; this.consumeChar()) {
            if (this.currentChar == 92) {
                this.consumeChar();
                tokenValue.append(this.escapeCharacter(this.currentChar));
            } else if (this.currentChar == startChar) {
                inQuote = false;
            } else {
                tokenValue.append((char) this.currentChar);
            }
        }
        if (inQuote) {
            throw new ParseException(tokenStartIndex, this.currentColumnIndex, tokenValue.toString(), "Closing quote not found");
        } else {
            return new Token(tokenStartIndex, tokenValue.toString(), Token.TokenType.STRING_LITERAL);
        }
    }

    private char escapeCharacter(int character) throws ParseException {
        switch(character) {
            case 34:
                return '"';
            case 39:
                return '\'';
            case 92:
                return '\\';
            case 98:
                return '\b';
            case 102:
                return '\f';
            case 110:
                return '\n';
            case 114:
                return '\r';
            case 116:
                return '\t';
            default:
                throw new ParseException(this.currentColumnIndex, 1, "\\" + (char) character, "Unknown escape character");
        }
    }

    private boolean isAtNumberStart() {
        return Character.isDigit(this.currentChar) ? true : this.currentChar == 46 && Character.isDigit(this.peekNextChar());
    }

    private boolean isAtNumberChar() {
        int previousChar = this.peekPreviousChar();
        if ((previousChar == 101 || previousChar == 69) && this.currentChar != 46) {
            return Character.isDigit(this.currentChar) || this.currentChar == 43 || this.currentChar == 45;
        } else {
            return previousChar == 46 && this.currentChar != 46 ? Character.isDigit(this.currentChar) || this.currentChar == 101 || this.currentChar == 69 : Character.isDigit(this.currentChar) || this.currentChar == 46 || this.currentChar == 101 || this.currentChar == 69;
        }
    }

    private boolean isNextCharNumberChar() {
        if (this.peekNextChar() == -1) {
            return false;
        } else {
            this.consumeChar();
            boolean isAtNumber = this.isAtNumberChar();
            this.currentColumnIndex--;
            this.currentChar = this.expressionString.charAt(this.currentColumnIndex - 1);
            return isAtNumber;
        }
    }

    private boolean isAtHexChar() {
        switch(this.currentChar) {
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
                return true;
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            default:
                return false;
        }
    }

    private boolean isAtIdentifierStart() {
        return Character.isLetter(this.currentChar) || this.currentChar == 95;
    }

    private boolean isAtIdentifierChar() {
        return Character.isLetter(this.currentChar) || Character.isDigit(this.currentChar) || this.currentChar == 95;
    }

    private boolean isAtStringLiteralStart() {
        return this.currentChar == 34 || this.currentChar == 39 && this.configuration.isSingleQuoteStringLiteralsAllowed();
    }

    private void skipBlanks() {
        if (this.currentChar == -2) {
            this.consumeChar();
        }
        while (this.currentChar != -1 && Character.isWhitespace(this.currentChar)) {
            this.consumeChar();
        }
    }

    private int peekNextChar() {
        return this.currentColumnIndex == this.expressionString.length() ? -1 : this.expressionString.charAt(this.currentColumnIndex);
    }

    private int peekPreviousChar() {
        return this.currentColumnIndex == 1 ? -1 : this.expressionString.charAt(this.currentColumnIndex - 2);
    }

    private void consumeChar() {
        if (this.currentColumnIndex == this.expressionString.length()) {
            this.currentChar = -1;
        } else {
            this.currentChar = this.expressionString.charAt(this.currentColumnIndex++);
        }
    }
}