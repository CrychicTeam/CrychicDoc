package dev.latvian.mods.rhino.ast;

public class ContinueStatement extends Jump {

    private Name label;

    private Loop target;

    public ContinueStatement() {
        this.type = 122;
    }

    public ContinueStatement(int pos) {
        this(pos, -1);
    }

    public ContinueStatement(int pos, int len) {
        this.type = 122;
        this.position = pos;
        this.length = len;
    }

    public ContinueStatement(Name label) {
        this.type = 122;
        this.setLabel(label);
    }

    public ContinueStatement(int pos, Name label) {
        this(pos);
        this.setLabel(label);
    }

    public ContinueStatement(int pos, int len, Name label) {
        this(pos, len);
        this.setLabel(label);
    }

    public Loop getTarget() {
        return this.target;
    }

    public void setTarget(Loop target) {
        this.assertNotNull(target);
        this.target = target;
        this.setJumpStatement(target);
    }

    public Name getLabel() {
        return this.label;
    }

    public void setLabel(Name label) {
        this.label = label;
        if (label != null) {
            label.setParent(this);
        }
    }
}