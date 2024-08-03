package dev.latvian.mods.rhino.ast;

public class IfStatement extends AstNode {

    private AstNode condition;

    private AstNode thenPart;

    private int elsePosition = -1;

    private AstNode elsePart;

    private AstNode elseKeyWordInlineComment;

    private int lp = -1;

    private int rp = -1;

    public IfStatement() {
        this.type = 113;
    }

    public IfStatement(int pos) {
        super(pos);
        this.type = 113;
    }

    public IfStatement(int pos, int len) {
        super(pos, len);
        this.type = 113;
    }

    public AstNode getCondition() {
        return this.condition;
    }

    public void setCondition(AstNode condition) {
        this.assertNotNull(condition);
        this.condition = condition;
        condition.setParent(this);
    }

    public AstNode getThenPart() {
        return this.thenPart;
    }

    public void setThenPart(AstNode thenPart) {
        this.assertNotNull(thenPart);
        this.thenPart = thenPart;
        thenPart.setParent(this);
    }

    public AstNode getElsePart() {
        return this.elsePart;
    }

    public void setElsePart(AstNode elsePart) {
        this.elsePart = elsePart;
        if (elsePart != null) {
            elsePart.setParent(this);
        }
    }

    public int getElsePosition() {
        return this.elsePosition;
    }

    public void setElsePosition(int elsePosition) {
        this.elsePosition = elsePosition;
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

    public AstNode getElseKeyWordInlineComment() {
        return this.elseKeyWordInlineComment;
    }

    public void setElseKeyWordInlineComment(AstNode elseKeyWordInlineComment) {
        this.elseKeyWordInlineComment = elseKeyWordInlineComment;
    }
}