package dev.latvian.mods.rhino.ast;

public class EmptyExpression extends AstNode {

    public EmptyExpression() {
        this.type = 129;
    }

    public EmptyExpression(int pos) {
        super(pos);
        this.type = 129;
    }

    public EmptyExpression(int pos, int len) {
        super(pos, len);
        this.type = 129;
    }
}