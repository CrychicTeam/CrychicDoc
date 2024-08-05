package icyllis.arc3d.engine.ops;

public abstract class DrawOp extends Op {

    public boolean usesStencil() {
        return false;
    }
}