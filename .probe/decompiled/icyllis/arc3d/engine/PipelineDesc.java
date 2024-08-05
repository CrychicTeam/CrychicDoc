package icyllis.arc3d.engine;

import javax.annotation.Nonnull;

public final class PipelineDesc extends KeyBuilder {

    private int mShaderKeyLength;

    public PipelineDesc() {
    }

    public PipelineDesc(PipelineDesc other) {
        super(other);
        this.mShaderKeyLength = other.mShaderKeyLength;
    }

    public int getShaderKeyLength() {
        return this.mShaderKeyLength;
    }

    @Nonnull
    public static PipelineDesc build(PipelineDesc desc, PipelineInfo info, Caps caps) {
        desc.clear();
        genKey(desc, info, caps);
        desc.mShaderKeyLength = desc.size();
        return desc;
    }

    public static String describe(PipelineInfo info, Caps caps) {
        KeyBuilder.StringKeyBuilder b = new KeyBuilder.StringKeyBuilder();
        genKey(b, info, caps);
        return b.toString();
    }

    static void genKey(KeyBuilder b, PipelineInfo info, Caps caps) {
        genGPKey(info.geomProc(), b);
        b.addBits(16, info.writeSwizzle(), "writeSwizzle");
        b.flush();
    }

    static void genGPKey(GeometryProcessor geomProc, KeyBuilder b) {
        b.appendComment(geomProc.name());
        b.addInt32(geomProc.classID(), "gpClassID");
        geomProc.appendToKey(b);
        geomProc.appendAttributesToKey(b);
    }
}