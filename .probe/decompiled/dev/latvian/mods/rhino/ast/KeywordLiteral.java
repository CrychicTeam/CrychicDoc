package dev.latvian.mods.rhino.ast;

public class KeywordLiteral extends AstNode {

    public KeywordLiteral() {
    }

    public KeywordLiteral(int pos) {
        super(pos);
    }

    public KeywordLiteral(int pos, int len) {
        super(pos, len);
    }

    public KeywordLiteral(int pos, int len, int nodeType) {
        super(pos, len);
        this.setType(nodeType);
    }

    public KeywordLiteral setType(int nodeType) {
        if (nodeType != 43 && nodeType != 42 && nodeType != 45 && nodeType != 44) {
            throw new IllegalArgumentException("Invalid node type: " + nodeType);
        } else {
            this.type = nodeType;
            return this;
        }
    }

    public boolean isBooleanLiteral() {
        return this.type == 45 || this.type == 44;
    }
}