package dev.latvian.mods.rhino.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TryStatement extends AstNode {

    private static final List<CatchClause> NO_CATCHES = Collections.unmodifiableList(new ArrayList());

    private AstNode tryBlock;

    private List<CatchClause> catchClauses;

    private AstNode finallyBlock;

    private int finallyPosition = -1;

    public TryStatement() {
        this.type = 82;
    }

    public TryStatement(int pos) {
        super(pos);
        this.type = 82;
    }

    public TryStatement(int pos, int len) {
        super(pos, len);
        this.type = 82;
    }

    public AstNode getTryBlock() {
        return this.tryBlock;
    }

    public void setTryBlock(AstNode tryBlock) {
        this.assertNotNull(tryBlock);
        this.tryBlock = tryBlock;
        tryBlock.setParent(this);
    }

    public List<CatchClause> getCatchClauses() {
        return this.catchClauses != null ? this.catchClauses : NO_CATCHES;
    }

    public void setCatchClauses(List<CatchClause> catchClauses) {
        if (catchClauses == null) {
            this.catchClauses = null;
        } else {
            if (this.catchClauses != null) {
                this.catchClauses.clear();
            }
            for (CatchClause cc : catchClauses) {
                this.addCatchClause(cc);
            }
        }
    }

    public void addCatchClause(CatchClause clause) {
        this.assertNotNull(clause);
        if (this.catchClauses == null) {
            this.catchClauses = new ArrayList();
        }
        this.catchClauses.add(clause);
        clause.setParent(this);
    }

    public AstNode getFinallyBlock() {
        return this.finallyBlock;
    }

    public void setFinallyBlock(AstNode finallyBlock) {
        this.finallyBlock = finallyBlock;
        if (finallyBlock != null) {
            finallyBlock.setParent(this);
        }
    }

    public int getFinallyPosition() {
        return this.finallyPosition;
    }

    public void setFinallyPosition(int finallyPosition) {
        this.finallyPosition = finallyPosition;
    }
}