package com.blamejared.searchables.lang;

import java.util.ArrayList;
import java.util.List;

public class SLScanner {

    private final String source;

    private final List<Token> tokens = new ArrayList();

    private int start = 0;

    private int current = 0;

    public SLScanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!this.isAtEnd()) {
            this.start = this.current;
            this.scanToken();
        }
        this.tokens.add(new Token(TokenType.EOL, "", "", this.start, this.current));
        return this.tokens;
    }

    private void scanToken() {
        char c = this.advance();
        switch(c) {
            case ' ':
                this.space();
                break;
            case '"':
                this.string('"');
                break;
            case '\'':
                this.string('\'');
                break;
            case ':':
                this.addToken(TokenType.COLON, ":");
                break;
            case '`':
                this.string('`');
                break;
            default:
                this.identifier();
        }
    }

    private void space() {
        this.addToken(TokenType.SPACE, " ");
    }

    private void string(char quote) {
        while (this.peek() != quote && !this.isAtEnd()) {
            this.advance();
        }
        if (this.isAtEnd()) {
            String value = this.source.substring(this.start + 1, this.current);
            this.addToken(TokenType.STRING, value);
        } else {
            this.advance();
            String value = this.source.substring(this.start + 1, this.current - 1);
            this.addToken(TokenType.STRING, value);
        }
    }

    private void identifier() {
        while (!this.isAtEnd() && this.peek() != ' ' && this.peek() != ':') {
            this.advance();
        }
        String value = this.source.substring(this.start, this.current);
        this.addToken(TokenType.IDENTIFIER, value);
    }

    private char peek() {
        return this.isAtEnd() ? '\u0000' : this.source.charAt(this.current);
    }

    private char advance() {
        return this.source.charAt(this.current++);
    }

    private void addToken(TokenType type, String literal) {
        String text = this.source.substring(this.start, this.current);
        this.tokens.add(new Token(type, text, literal, this.start, this.current));
    }

    private boolean isAtEnd() {
        return this.current >= this.source.length();
    }
}