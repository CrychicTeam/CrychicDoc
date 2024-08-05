package com.blamejared.searchables.lang;

public class Token {

    private final TokenType type;

    private final String lexeme;

    private final String literal;

    private final int start;

    private final int end;

    public Token(TokenType type, String lexeme, String literal, int start, int end) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.start = start;
        this.end = end;
    }

    public String toString() {
        return "Token{type=" + this.type + ", lexeme='" + this.lexeme + "', literal='" + this.literal + "', start=" + this.start + ", end=" + this.end + "}";
    }

    public TokenType type() {
        return this.type;
    }

    public String lexeme() {
        return this.lexeme;
    }

    public int start() {
        return this.start;
    }

    public int end() {
        return this.end;
    }

    public String literal() {
        return this.literal;
    }
}