package dev.latvian.mods.rhino.ast;

public class ErrorNode extends AstNode {

    private String message;

    public ErrorNode() {
        this.type = -1;
    }

    public ErrorNode(int pos) {
        super(pos);
        this.type = -1;
    }

    public ErrorNode(int pos, int len) {
        super(pos, len);
        this.type = -1;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}