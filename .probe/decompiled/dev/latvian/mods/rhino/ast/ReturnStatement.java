package dev.latvian.mods.rhino.ast;

public class ReturnStatement extends AstNode {

    private AstNode returnValue;

    public ReturnStatement() {
        this.type = 4;
    }

    public ReturnStatement(int pos) {
        super(pos);
        this.type = 4;
    }

    public ReturnStatement(int pos, int len) {
        super(pos, len);
        this.type = 4;
    }

    public ReturnStatement(int pos, int len, AstNode returnValue) {
        super(pos, len);
        this.type = 4;
        this.setReturnValue(returnValue);
    }

    public AstNode getReturnValue() {
        return this.returnValue;
    }

    public void setReturnValue(AstNode returnValue) {
        this.returnValue = returnValue;
        if (returnValue != null) {
            returnValue.setParent(this);
        }
    }
}