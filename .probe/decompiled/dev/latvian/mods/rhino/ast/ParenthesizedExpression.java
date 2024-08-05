package dev.latvian.mods.rhino.ast;

public class ParenthesizedExpression extends AstNode {

    private AstNode expression;

    public ParenthesizedExpression() {
        this.type = 88;
    }

    public ParenthesizedExpression(int pos) {
        super(pos);
        this.type = 88;
    }

    public ParenthesizedExpression(int pos, int len) {
        super(pos, len);
        this.type = 88;
    }

    public ParenthesizedExpression(AstNode expr) {
        this(expr != null ? expr.getPosition() : 0, expr != null ? expr.getLength() : 1, expr);
    }

    public ParenthesizedExpression(int pos, int len, AstNode expr) {
        super(pos, len);
        this.type = 88;
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