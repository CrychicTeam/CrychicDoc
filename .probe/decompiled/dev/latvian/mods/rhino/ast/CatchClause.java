package dev.latvian.mods.rhino.ast;

public class CatchClause extends AstNode {

    private Name varName;

    private AstNode catchCondition;

    private Block body;

    private int ifPosition = -1;

    private int lp = -1;

    private int rp = -1;

    public CatchClause() {
        this.type = 125;
    }

    public CatchClause(int pos) {
        super(pos);
        this.type = 125;
    }

    public CatchClause(int pos, int len) {
        super(pos, len);
        this.type = 125;
    }

    public Name getVarName() {
        return this.varName;
    }

    public void setVarName(Name varName) {
        this.assertNotNull(varName);
        this.varName = varName;
        varName.setParent(this);
    }

    public AstNode getCatchCondition() {
        return this.catchCondition;
    }

    public void setCatchCondition(AstNode catchCondition) {
        this.catchCondition = catchCondition;
        if (catchCondition != null) {
            catchCondition.setParent(this);
        }
    }

    public Block getBody() {
        return this.body;
    }

    public void setBody(Block body) {
        this.assertNotNull(body);
        this.body = body;
        body.setParent(this);
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

    public int getIfPosition() {
        return this.ifPosition;
    }

    public void setIfPosition(int ifPosition) {
        this.ifPosition = ifPosition;
    }
}