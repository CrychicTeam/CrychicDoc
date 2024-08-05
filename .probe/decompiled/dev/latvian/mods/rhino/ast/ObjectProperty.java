package dev.latvian.mods.rhino.ast;

public class ObjectProperty extends InfixExpression {

    public ObjectProperty() {
        this.type = 104;
    }

    public ObjectProperty(int pos) {
        super(pos);
        this.type = 104;
    }

    public ObjectProperty(int pos, int len) {
        super(pos, len);
        this.type = 104;
    }

    public void setNodeType(int nodeType) {
        if (nodeType != 104 && nodeType != 152 && nodeType != 153 && nodeType != 164) {
            throw new IllegalArgumentException("invalid node type: " + nodeType);
        } else {
            this.setType(nodeType);
        }
    }

    public void setIsGetterMethod() {
        this.type = 152;
    }

    public boolean isGetterMethod() {
        return this.type == 152;
    }

    public void setIsSetterMethod() {
        this.type = 153;
    }

    public boolean isSetterMethod() {
        return this.type == 153;
    }

    public void setIsNormalMethod() {
        this.type = 164;
    }

    public boolean isNormalMethod() {
        return this.type == 164;
    }

    public boolean isMethod() {
        return this.isGetterMethod() || this.isSetterMethod() || this.isNormalMethod();
    }
}