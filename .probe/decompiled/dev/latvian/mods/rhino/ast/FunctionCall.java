package dev.latvian.mods.rhino.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionCall extends AstNode {

    protected static final List<AstNode> NO_ARGS = Collections.unmodifiableList(new ArrayList());

    protected AstNode target;

    protected List<AstNode> arguments;

    protected int lp = -1;

    protected int rp = -1;

    public FunctionCall() {
        this.type = 38;
    }

    public FunctionCall(int pos) {
        super(pos);
        this.type = 38;
    }

    public FunctionCall(int pos, int len) {
        super(pos, len);
        this.type = 38;
    }

    public AstNode getTarget() {
        return this.target;
    }

    public void setTarget(AstNode target) {
        this.assertNotNull(target);
        this.target = target;
        target.setParent(this);
    }

    public List<AstNode> getArguments() {
        return this.arguments != null ? this.arguments : NO_ARGS;
    }

    public void setArguments(List<AstNode> arguments) {
        if (arguments == null) {
            this.arguments = null;
        } else {
            if (this.arguments != null) {
                this.arguments.clear();
            }
            for (AstNode arg : arguments) {
                this.addArgument(arg);
            }
        }
    }

    public void addArgument(AstNode arg) {
        this.assertNotNull(arg);
        if (this.arguments == null) {
            this.arguments = new ArrayList();
        }
        this.arguments.add(arg);
        arg.setParent(this);
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