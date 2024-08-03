package dev.latvian.mods.rhino.ast;

public class WithStatement extends AstNode {

    private AstNode expression;

    private AstNode statement;

    private int lp = -1;

    private int rp = -1;

    public WithStatement() {
        this.type = 124;
    }

    public WithStatement(int pos) {
        super(pos);
        this.type = 124;
    }

    public WithStatement(int pos, int len) {
        super(pos, len);
        this.type = 124;
    }

    public AstNode getExpression() {
        return this.expression;
    }

    public void setExpression(AstNode expression) {
        this.assertNotNull(expression);
        this.expression = expression;
        expression.setParent(this);
    }

    public AstNode getStatement() {
        return this.statement;
    }

    public void setStatement(AstNode statement) {
        this.assertNotNull(statement);
        this.statement = statement;
        statement.setParent(this);
    }

    public int getLp() {
        return this.lp;
    }

    public void setLp(int lp) {
        this.lp = lp;
    }

    public int getRp() {
        return this.rp;
    }

    public void setRp(int rp) {
        this.rp = rp;
    }

    public void setParens(int lp, int rp) {
        this.lp = lp;
        this.rp = rp;
    }
}