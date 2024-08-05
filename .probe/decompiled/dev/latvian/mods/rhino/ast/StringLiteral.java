package dev.latvian.mods.rhino.ast;

public class StringLiteral extends AstNode {

    private String value;

    private char quoteChar;

    public StringLiteral() {
        this.type = 41;
    }

    public StringLiteral(int pos) {
        super(pos);
        this.type = 41;
    }

    public StringLiteral(int pos, int len) {
        super(pos, len);
        this.type = 41;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.assertNotNull(value);
        this.value = value;
    }

    public String getValue(boolean includeQuotes) {
        return !includeQuotes ? this.value : this.quoteChar + this.value + this.quoteChar;
    }

    public char getQuoteCharacter() {
        return this.quoteChar;
    }

    public void setQuoteCharacter(char c) {
        this.quoteChar = c;
    }

    @Override
    public String toString() {
        return this.value;
    }
}