package dev.latvian.mods.rhino.ast;

public class ArrayComprehensionLoop extends ForInLoop {

    public ArrayComprehensionLoop() {
    }

    public ArrayComprehensionLoop(int pos) {
        super(pos);
    }

    public ArrayComprehensionLoop(int pos, int len) {
        super(pos, len);
    }

    @Override
    public AstNode getBody() {
        return null;
    }

    @Override
    public void setBody(AstNode body) {
        throw new UnsupportedOperationException("this node type has no body");
    }
}