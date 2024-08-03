package icyllis.arc3d.engine;

public final class StencilFaceSettings {

    public short mRef;

    public short mTest;

    public short mTestMask;

    public byte mPassOp;

    public byte mFailOp;

    public short mWriteMask;

    public StencilFaceSettings() {
    }

    public StencilFaceSettings(short ref, short test, short testMask, byte passOp, byte failOp, short writeMask) {
        this.mRef = ref;
        this.mTest = test;
        this.mTestMask = testMask;
        this.mPassOp = passOp;
        this.mFailOp = failOp;
        this.mWriteMask = writeMask;
    }
}