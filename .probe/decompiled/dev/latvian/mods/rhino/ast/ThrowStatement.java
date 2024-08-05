package dev.latvian.mods.rhino.ast;

public class ThrowStatement extends AstNode {

    private AstNode expression;

    public ThrowStatement() {
        this.type = 50;
    }

    public ThrowStatement(int pos) {
        super(pos);
        this.type = 50;
    }

    public ThrowStatement(int pos, int len) {
        super(pos, len);
        this.type = 50;
    }

    public ThrowStatement(AstNode expr) {
        this.type = 50;
        this.setExpression(expr);
    }

    public ThrowStatement(int pos, AstNode expr) {
        super(pos, expr.getLength());
        this.type = 50;
        this.setExpression(expr);
    }

    public ThrowStatement(int pos, int len, AstNode expr) {
        super(pos, len);
        this.type = 50;
        this.setExpression(expr);
    }

    public AstNode getExpression() {
        return this.expression;
    }

    public void setExpression(AstNode expression) {
        this.assertNotNull(expression);
        this.expression = expression;
        expression.setParent(this);
    }
}