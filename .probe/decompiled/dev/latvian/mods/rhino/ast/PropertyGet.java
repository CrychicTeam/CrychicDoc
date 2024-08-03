package dev.latvian.mods.rhino.ast;

public class PropertyGet extends InfixExpression {

    public PropertyGet(AstNode target, Name property, int dotPosition) {
        super(33, target, property, dotPosition);
    }

    public AstNode getTarget() {
        return this.getLeft();
    }

    public void setTarget(AstNode target) {
        this.setLeft(target);
    }

    public Name getProperty() {
        return (Name) this.getRight();
    }

    public void setProperty(Name property) {
        this.setRight(property);
    }
}