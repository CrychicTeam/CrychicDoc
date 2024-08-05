package icyllis.modernui.mc.testforge.trash;

import javax.annotation.Nonnull;
import org.lwjgl.opengl.GL45C;

public final class GLVertexAttrib {

    private final int mBinding;

    private final GLVertexAttrib.CpuType mCpuType;

    private final GLVertexAttrib.GpuType mGpuType;

    private final boolean mNormalized;

    public GLVertexAttrib(int binding, @Nonnull GLVertexAttrib.CpuType cpuType, @Nonnull GLVertexAttrib.GpuType gpuType, boolean normalized) {
        this.mBinding = binding;
        this.mCpuType = cpuType;
        this.mGpuType = gpuType;
        this.mNormalized = normalized;
    }

    public int getBinding() {
        return this.mBinding;
    }

    public int getLocationSize() {
        return this.mGpuType.mLocationSize;
    }

    public int setFormat(int array, int location, int offset) {
        for (int i = 0; i < this.getLocationSize(); i++) {
            GL45C.glEnableVertexArrayAttrib(array, location);
            GL45C.glVertexArrayAttribFormat(array, location, this.mGpuType.mSize, this.mCpuType.mType, this.mNormalized, offset);
            GL45C.glVertexArrayAttribBinding(array, location, this.mBinding);
            location++;
            offset += this.getStep();
        }
        return offset;
    }

    public int getStep() {
        return this.mCpuType.mSize * this.mGpuType.mSize;
    }

    public int getTotalSize() {
        return this.getStep() * this.getLocationSize();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            GLVertexAttrib that = (GLVertexAttrib) o;
            if (this.mBinding != that.mBinding) {
                return false;
            } else if (this.mNormalized != that.mNormalized) {
                return false;
            } else {
                return this.mCpuType != that.mCpuType ? false : this.mGpuType == that.mGpuType;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.mBinding;
        result = 31 * result + this.mCpuType.hashCode();
        result = 31 * result + this.mGpuType.hashCode();
        return 31 * result + (this.mNormalized ? 1 : 0);
    }

    public static enum CpuType {

        FLOAT(4, 5126),
        BYTE(1, 5120),
        UBYTE(1, 5121),
        SHORT(2, 5122),
        USHORT(2, 5123),
        INT(4, 5124),
        UINT(4, 5125),
        HALF(2, 5131);

        private final int mSize;

        private final int mType;

        private CpuType(int size, int type) {
            this.mSize = size;
            this.mType = type;
        }
    }

    public static enum GpuType {

        FLOAT(1, 1), VEC2(2, 1), VEC3(3, 1), VEC4(4, 1), MAT4(4, 4);

        private final int mSize;

        private final int mLocationSize;

        private GpuType(int size, int locationSize) {
            this.mSize = size;
            this.mLocationSize = locationSize;
        }
    }
}