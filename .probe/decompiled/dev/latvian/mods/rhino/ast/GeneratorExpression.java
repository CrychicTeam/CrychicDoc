package dev.latvian.mods.rhino.ast;

import java.util.ArrayList;
import java.util.List;

public class GeneratorExpression extends Scope {

    private final List<GeneratorExpressionLoop> loops = new ArrayList();

    private AstNode result;

    private AstNode filter;

    private int ifPosition = -1;

    private int lp = -1;

    private int rp = -1;

    public GeneratorExpression() {
        this.type = 163;
    }

    public GeneratorExpression(int pos) {
        super(pos);
        this.type = 163;
    }

    public GeneratorExpression(int pos, int len) {
        super(pos, len);
        this.type = 163;
    }

    public AstNode getResult() {
        return this.result;
    }

    public void setResult(AstNode result) {
        this.assertNotNull(result);
        this.result = result;
        result.setParent(this);
    }

    public List<GeneratorExpressionLoop> getLoops() {
        return this.loops;
    }

    public void setLoops(List<GeneratorExpressionLoop> loops) {
        this.assertNotNull(loops);
        this.loops.clear();
        for (GeneratorExpressionLoop acl : loops) {
            this.addLoop(acl);
        }
    }

    public void addLoop(GeneratorExpressionLoop acl) {
        this.assertNotNull(acl);
        this.loops.add(acl);
        acl.setParent(this);
    }

    public AstNode getFilter() {
        return this.filter;
    }

    public void setFilter(AstNode filter) {
        this.filter = filter;
        if (filter != null) {
            filter.setParent(this);
        }
    }

    public int getIfPosition() {
        return this.ifPosition;
    }

    public void setIfPosition(int ifPosition) {
        this.ifPosition = ifPosition;
    }

    public int getFilterLp() {
        return this.lp;
    }

    public void setFilterLp(int lp) {
        this.lp = lp;
    }

    public int getFilterRp() {
        return this.rp;
    }

    public void setFilterRp(int rp) {
        this.rp = rp;
    }
}