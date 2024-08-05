package dev.latvian.mods.rhino.ast;

public class GeneratorExpressionLoop extends ForInLoop {

    public GeneratorExpressionLoop() {
    }

    public GeneratorExpressionLoop(int pos) {
        super(pos);
    }

    public GeneratorExpressionLoop(int pos, int len) {
        super(pos, len);
    }

    @Override
    public boolean isForEach() {
        return false;
    }

    @Override
    public void setIsForEach(boolean isForEach) {
        throw new UnsupportedOperationException("this node type does not support for each");
    }
}