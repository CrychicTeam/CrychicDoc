package dev.latvian.mods.rhino.ast;

public class LetNode extends Scope {

    private VariableDeclaration variables;

    private AstNode body;

    private int lp = -1;

    private int rp = -1;

    public LetNode() {
        this.type = 159;
    }

    public LetNode(int pos) {
        super(pos);
        this.type = 159;
    }

    public LetNode(int pos, int len) {
        super(pos, len);
        this.type = 159;
    }

    public VariableDeclaration getVariables() {
        return this.variables;
    }

    public void setVariables(VariableDeclaration variables) {
        this.assertNotNull(variables);
        this.variables = variables;
        variables.setParent(this);
    }

    public AstNode getBody() {
        return this.body;
    }

    public void setBody(AstNode body) {
        this.body = body;
        if (body != null) {
            body.setParent(this);
        }
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