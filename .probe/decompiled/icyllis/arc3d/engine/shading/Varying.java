package icyllis.arc3d.engine.shading;

import icyllis.arc3d.core.SLDataType;
import icyllis.arc3d.engine.ShaderVar;

public final class Varying {

    byte mType;

    String mVsOut = null;

    String mFsIn = null;

    public Varying(byte type) {
        assert type != 0 && !SLDataType.isMatrixType(type);
        this.mType = type;
    }

    public void reset(byte type) {
        assert type != 0 && !SLDataType.isMatrixType(type);
        this.mType = type;
        this.mVsOut = null;
        this.mFsIn = null;
    }

    public byte type() {
        return this.mType;
    }

    public boolean isInVertexShader() {
        return true;
    }

    public boolean isInFragmentShader() {
        return true;
    }

    public String vsOut() {
        assert this.isInVertexShader();
        return this.mVsOut;
    }

    public String fsIn() {
        assert this.isInFragmentShader();
        return this.mFsIn;
    }

    public ShaderVar vsOutVar() {
        assert this.isInVertexShader();
        return new ShaderVar(this.mVsOut, this.mType, (byte) 1);
    }

    public ShaderVar fsInVar() {
        assert this.isInFragmentShader();
        return new ShaderVar(this.mFsIn, this.mType, (byte) 2);
    }
}