package dev.latvian.mods.rhino.ast;

public class ForLoop extends Loop {

    private AstNode initializer;

    private AstNode condition;

    private AstNode increment;

    public ForLoop() {
        this.type = 120;
    }

    public ForLoop(int pos) {
        super(pos);
        this.type = 120;
    }

    public ForLoop(int pos, int len) {
        super(pos, len);
        this.type = 120;
    }

    public AstNode getInitializer() {
        return this.initializer;
    }

    public void setInitializer(AstNode initializer) {
        this.assertNotNull(initializer);
        this.initializer = initializer;
        initializer.setParent(this);
    }

    public AstNode getCondition() {
        return this.condition;
    }

    public void setCondition(AstNode condition) {
        this.assertNotNull(condition);
        this.condition = condition;
        condition.setParent(this);
    }

    public AstNode getIncrement() {
        return this.increment;
    }

    public void setIncrement(AstNode increment) {
        this.assertNotNull(increment);
        this.increment = increment;
        increment.setParent(this);
    }
}