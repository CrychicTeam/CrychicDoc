package dev.latvian.mods.rhino.ast;

public class WhileLoop extends Loop {

    private AstNode condition;

    public WhileLoop() {
        this.type = 118;
    }

    public WhileLoop(int pos) {
        super(pos);
        this.type = 118;
    }

    public WhileLoop(int pos, int len) {
        super(pos, len);
        this.type = 118;
    }

    public AstNode getCondition() {
        return this.condition;
    }

    public void setCondition(AstNode condition) {
        this.assertNotNull(condition);
        this.condition = condition;
        condition.setParent(this);
    }
}