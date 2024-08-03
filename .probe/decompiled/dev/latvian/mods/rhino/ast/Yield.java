package dev.latvian.mods.rhino.ast;

public class Yield extends AstNode {

    private AstNode value;

    public Yield() {
        this.type = 73;
    }

    public Yield(int pos) {
        super(pos);
        this.type = 73;
    }

    public Yield(int pos, int len) {
        super(pos, len);
        this.type = 73;
    }

    public Yield(int pos, int len, AstNode value, boolean isStar) {
        super(pos, len);
        this.type = isStar ? 166 : 73;
        this.setValue(value);
    }

    public AstNode getValue() {
        return this.value;
    }

    public void setValue(AstNode expr) {
        this.value = expr;
        if (expr != null) {
            expr.setParent(this);
        }
    }
}