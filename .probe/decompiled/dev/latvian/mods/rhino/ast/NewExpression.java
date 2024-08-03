package dev.latvian.mods.rhino.ast;

public class NewExpression extends FunctionCall {

    private ObjectLiteral initializer;

    public NewExpression() {
        this.type = 30;
    }

    public NewExpression(int pos) {
        super(pos);
        this.type = 30;
    }

    public NewExpression(int pos, int len) {
        super(pos, len);
        this.type = 30;
    }

    public ObjectLiteral getInitializer() {
        return this.initializer;
    }

    public void setInitializer(ObjectLiteral initializer) {
        this.initializer = initializer;
        if (initializer != null) {
            initializer.setParent(this);
        }
    }
}