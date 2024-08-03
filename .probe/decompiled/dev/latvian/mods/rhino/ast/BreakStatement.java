package dev.latvian.mods.rhino.ast;

public class BreakStatement extends Jump {

    private Name breakLabel;

    private AstNode target;

    public BreakStatement() {
        this.type = 121;
    }

    public BreakStatement(int pos) {
        this.type = 121;
        this.position = pos;
    }

    public BreakStatement(int pos, int len) {
        this.type = 121;
        this.position = pos;
        this.length = len;
    }

    public Name getBreakLabel() {
        return this.breakLabel;
    }

    public void setBreakLabel(Name label) {
        this.breakLabel = label;
        if (label != null) {
            label.setParent(this);
        }
    }

    public AstNode getBreakTarget() {
        return this.target;
    }

    public void setBreakTarget(Jump target) {
        this.assertNotNull(target);
        this.target = target;
        this.setJumpStatement(target);
    }
}