package dev.latvian.mods.rhino.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwitchStatement extends Jump {

    private static final List<SwitchCase> NO_CASES = Collections.unmodifiableList(new ArrayList());

    private AstNode expression;

    private List<SwitchCase> cases;

    private int lp = -1;

    private int rp = -1;

    public SwitchStatement() {
        this.type = 115;
    }

    public SwitchStatement(int pos) {
        this.type = 115;
        this.position = pos;
    }

    public SwitchStatement(int pos, int len) {
        this.type = 115;
        this.position = pos;
        this.length = len;
    }

    public AstNode getExpression() {
        return this.expression;
    }

    public void setExpression(AstNode expression) {
        this.assertNotNull(expression);
        this.expression = expression;
        expression.setParent(this);
    }

    public List<SwitchCase> getCases() {
        return this.cases != null ? this.cases : NO_CASES;
    }

    public void setCases(List<SwitchCase> cases) {
        if (cases == null) {
            this.cases = null;
        } else {
            if (this.cases != null) {
                this.cases.clear();
            }
            for (SwitchCase sc : cases) {
                this.addCase(sc);
            }
        }
    }

    public void addCase(SwitchCase switchCase) {
        this.assertNotNull(switchCase);
        if (this.cases == null) {
            this.cases = new ArrayList();
        }
        this.cases.add(switchCase);
        switchCase.setParent(this);
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