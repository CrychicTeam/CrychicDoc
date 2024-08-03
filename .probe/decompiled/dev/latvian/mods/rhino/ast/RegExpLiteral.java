package dev.latvian.mods.rhino.ast;

public class RegExpLiteral extends AstNode {

    private String value;

    private String flags;

    public RegExpLiteral() {
        this.type = 48;
    }

    public RegExpLiteral(int pos) {
        super(pos);
        this.type = 48;
    }

    public RegExpLiteral(int pos, int len) {
        super(pos, len);
        this.type = 48;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.assertNotNull(value);
        this.value = value;
    }

    public String getFlags() {
        return this.flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }
}