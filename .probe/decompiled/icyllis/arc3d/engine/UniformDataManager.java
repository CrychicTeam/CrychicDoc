package icyllis.arc3d.engine;

import icyllis.arc3d.core.MathUtil;
import icyllis.arc3d.core.Matrix3;
import icyllis.arc3d.core.Matrix4;
import icyllis.arc3d.core.Matrixc;
import icyllis.arc3d.core.RefCnt;
import org.lwjgl.system.MemoryUtil;

public abstract class UniformDataManager extends RefCnt {

    protected final int[] mUniforms;

    protected final int mUniformSize;

    protected final long mUniformData;

    protected boolean mUniformsDirty;

    public UniformDataManager(int uniformCount, int uniformSize) {
        assert uniformCount >= 1 && uniformSize >= 4;
        this.mUniforms = new int[uniformCount];
        this.mUniformSize = uniformSize;
        this.mUniformData = MemoryUtil.nmemAllocChecked((long) uniformSize);
        this.mUniformsDirty = false;
        assert MathUtil.isAlign4(uniformSize);
        assert MathUtil.isAlign4(this.mUniformData);
    }

    @Override
    protected void deallocate() {
        MemoryUtil.nmemFree(this.mUniformData);
    }

    public void set1i(int u, int v0) {
        int uni = this.mUniforms[u];
        assert uni >> 24 == 27 || uni >> 24 == 31 || uni >> 24 == 1;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        MemoryUtil.memPutInt(buffer, v0);
    }

    public void set1iv(int u, int count, long value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 27 || uni >> 24 == 31 || uni >> 24 == 1;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutInt(buffer, MemoryUtil.memGetInt(value));
            if (++i >= count) {
                return;
            }
            buffer += 16L;
            value += 4L;
        }
    }

    public void set1iv(int u, int offset, int count, int[] value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 27 || uni >> 24 == 31 || uni >> 24 == 1;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutInt(buffer, value[offset + i]);
            if (++i >= count) {
                return;
            }
            buffer += 16L;
        }
    }

    public void set1f(int u, float v0) {
        int uni = this.mUniforms[u];
        assert uni >> 24 == 13;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        MemoryUtil.memPutFloat(buffer, v0);
    }

    public void set1fv(int u, int count, long value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 13;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutInt(buffer, MemoryUtil.memGetInt(value));
            if (++i >= count) {
                return;
            }
            buffer += 16L;
            value += 4L;
        }
    }

    public void set1fv(int u, int offset, int count, float[] value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 13;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutFloat(buffer, value[offset + i]);
            if (++i >= count) {
                return;
            }
            buffer += 16L;
        }
    }

    public void set2i(int u, int v0, int v1) {
        int uni = this.mUniforms[u];
        assert uni >> 24 == 28 || uni >> 24 == 32 || uni >> 24 == 2;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        MemoryUtil.memPutInt(buffer, v0);
        MemoryUtil.memPutInt(buffer + 4L, v1);
    }

    public void set2iv(int u, int count, long value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 28 || uni >> 24 == 32 || uni >> 24 == 2;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutLong(buffer, MemoryUtil.memGetLong(value));
            if (++i >= count) {
                return;
            }
            buffer += 16L;
            value += 8L;
        }
    }

    public void set2iv(int u, int offset, int count, int[] value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 28 || uni >> 24 == 32 || uni >> 24 == 2;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutInt(buffer, value[offset]);
            MemoryUtil.memPutInt(buffer + 4L, value[offset + 1]);
            if (++i >= count) {
                return;
            }
            buffer += 16L;
            offset += 2;
        }
    }

    public void set2f(int u, float v0, float v1) {
        int uni = this.mUniforms[u];
        assert uni >> 24 == 14;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        MemoryUtil.memPutFloat(buffer, v0);
        MemoryUtil.memPutFloat(buffer + 4L, v1);
    }

    public void set2fv(int u, int count, long value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 14;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutLong(buffer, MemoryUtil.memGetLong(value));
            if (++i >= count) {
                return;
            }
            buffer += 16L;
            value += 8L;
        }
    }

    public void set2fv(int u, int offset, int count, float[] value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 14;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutFloat(buffer, value[offset]);
            MemoryUtil.memPutFloat(buffer + 4L, value[offset + 1]);
            if (++i >= count) {
                return;
            }
            buffer += 16L;
            offset += 2;
        }
    }

    public void set3i(int u, int v0, int v1, int v2) {
        int uni = this.mUniforms[u];
        assert uni >> 24 == 29 || uni >> 24 == 33 || uni >> 24 == 3;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        MemoryUtil.memPutInt(buffer, v0);
        MemoryUtil.memPutInt(buffer + 4L, v1);
        MemoryUtil.memPutInt(buffer + 8L, v2);
    }

    public void set3iv(int u, int count, long value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 29 || uni >> 24 == 33 || uni >> 24 == 3;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutLong(buffer, MemoryUtil.memGetLong(value));
            MemoryUtil.memPutInt(buffer + 8L, MemoryUtil.memGetInt(value + 8L));
            if (++i >= count) {
                return;
            }
            buffer += 16L;
            value += 12L;
        }
    }

    public void set3iv(int u, int offset, int count, int[] value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 29 || uni >> 24 == 33 || uni >> 24 == 3;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutInt(buffer, value[offset]);
            MemoryUtil.memPutInt(buffer + 4L, value[offset + 1]);
            MemoryUtil.memPutInt(buffer + 8L, value[offset + 2]);
            if (++i >= count) {
                return;
            }
            buffer += 16L;
            offset += 3;
        }
    }

    public void set3f(int u, float v0, float v1, float v2) {
        int uni = this.mUniforms[u];
        assert uni >> 24 == 15;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        MemoryUtil.memPutFloat(buffer, v0);
        MemoryUtil.memPutFloat(buffer + 4L, v1);
        MemoryUtil.memPutFloat(buffer + 8L, v2);
    }

    public void set3fv(int u, int count, long value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 15;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutLong(buffer, MemoryUtil.memGetLong(value));
            MemoryUtil.memPutInt(buffer + 8L, MemoryUtil.memGetInt(value + 8L));
            if (++i >= count) {
                return;
            }
            buffer += 16L;
            value += 12L;
        }
    }

    public void set3fv(int u, int offset, int count, float[] value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 15;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutFloat(buffer, value[offset]);
            MemoryUtil.memPutFloat(buffer + 4L, value[offset + 1]);
            MemoryUtil.memPutFloat(buffer + 8L, value[offset + 2]);
            if (++i >= count) {
                return;
            }
            buffer += 16L;
            offset += 3;
        }
    }

    public void set4i(int u, int v0, int v1, int v2, int v3) {
        int uni = this.mUniforms[u];
        assert uni >> 24 == 30 || uni >> 24 == 34 || uni >> 24 == 4;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        MemoryUtil.memPutInt(buffer, v0);
        MemoryUtil.memPutInt(buffer + 4L, v1);
        MemoryUtil.memPutInt(buffer + 8L, v2);
        MemoryUtil.memPutInt(buffer + 12L, v3);
    }

    public void set4iv(int u, int count, long value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 30 || uni >> 24 == 34 || uni >> 24 == 4;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        MemoryUtil.memCopy(value, buffer, (long) count * 4L * 4L);
    }

    public void set4iv(int u, int offset, int count, int[] value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 30 || uni >> 24 == 34 || uni >> 24 == 4;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        for (int e = count * 4; i < e; i++) {
            MemoryUtil.memPutInt(buffer, value[offset++]);
            buffer += 4L;
        }
    }

    public void set4f(int u, float v0, float v1, float v2, float v3) {
        int uni = this.mUniforms[u];
        assert uni >> 24 == 16;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        MemoryUtil.memPutFloat(buffer, v0);
        MemoryUtil.memPutFloat(buffer + 4L, v1);
        MemoryUtil.memPutFloat(buffer + 8L, v2);
        MemoryUtil.memPutFloat(buffer + 12L, v3);
    }

    public void set4fv(int u, int count, long value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 16;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        MemoryUtil.memCopy(value, buffer, (long) count * 4L * 4L);
    }

    public void set4fv(int u, int offset, int count, float[] value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 16;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        for (int e = count * 4; i < e; i++) {
            MemoryUtil.memPutFloat(buffer, value[offset++]);
            buffer += 4L;
        }
    }

    public void setMatrix2fv(int u, int count, long value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 17;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutLong(buffer, MemoryUtil.memGetLong(value));
            MemoryUtil.memPutLong(buffer + 16L, MemoryUtil.memGetLong(value + 8L));
            if (++i >= count) {
                return;
            }
            buffer += 32L;
            value += 16L;
        }
    }

    public void setMatrix2fv(int u, int offset, int count, float[] value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 17;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutFloat(buffer, value[offset]);
            MemoryUtil.memPutFloat(buffer + 4L, value[offset + 1]);
            MemoryUtil.memPutFloat(buffer + 16L, value[offset + 2]);
            MemoryUtil.memPutFloat(buffer + 20L, value[offset + 3]);
            if (++i >= count) {
                return;
            }
            buffer += 32L;
            offset += 4;
        }
    }

    public void setMatrix3fv(int u, int count, long value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 18;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutLong(buffer, MemoryUtil.memGetLong(value));
            MemoryUtil.memPutInt(buffer + 8L, MemoryUtil.memGetInt(value + 8L));
            MemoryUtil.memPutLong(buffer + 16L, MemoryUtil.memGetLong(value + 12L));
            MemoryUtil.memPutInt(buffer + 24L, MemoryUtil.memGetInt(value + 20L));
            MemoryUtil.memPutLong(buffer + 32L, MemoryUtil.memGetLong(value + 24L));
            MemoryUtil.memPutInt(buffer + 40L, MemoryUtil.memGetInt(value + 32L));
            if (++i >= count) {
                return;
            }
            buffer += 48L;
            value += 36L;
        }
    }

    public void setMatrix3fv(int u, int offset, int count, float[] value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 18;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        while (true) {
            MemoryUtil.memPutFloat(buffer, value[offset]);
            MemoryUtil.memPutFloat(buffer + 4L, value[offset + 1]);
            MemoryUtil.memPutFloat(buffer + 8L, value[offset + 2]);
            MemoryUtil.memPutFloat(buffer + 16L, value[offset + 3]);
            MemoryUtil.memPutFloat(buffer + 20L, value[offset + 4]);
            MemoryUtil.memPutFloat(buffer + 24L, value[offset + 5]);
            MemoryUtil.memPutFloat(buffer + 32L, value[offset + 6]);
            MemoryUtil.memPutFloat(buffer + 36L, value[offset + 7]);
            MemoryUtil.memPutFloat(buffer + 40L, value[offset + 8]);
            if (++i >= count) {
                return;
            }
            buffer += 48L;
            offset += 9;
        }
    }

    public void setMatrix4fv(int u, int count, long value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 19;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        MemoryUtil.memCopy(value, buffer, (long) count * 4L * 4L * 4L);
    }

    public void setMatrix4fv(int u, int offset, int count, float[] value) {
        assert count > 0;
        int uni = this.mUniforms[u];
        assert uni >> 24 == 19;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        int i = 0;
        for (int e = count * 4 * 4; i < e; i++) {
            MemoryUtil.memPutFloat(buffer, value[offset++]);
            buffer += 4L;
        }
    }

    public void setMatrix3f(int u, Matrixc matrix) {
        int uni = this.mUniforms[u];
        assert uni >> 24 == 18;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        matrix.storeAligned(buffer);
    }

    public void setMatrix3f(int u, Matrix3 matrix) {
        int uni = this.mUniforms[u];
        assert uni >> 24 == 18;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        matrix.storeAligned(buffer);
    }

    public void setMatrix4f(int u, Matrix4 matrix) {
        int uni = this.mUniforms[u];
        assert uni >> 24 == 19;
        long buffer = this.getBufferPtrAndMarkDirty(uni);
        matrix.store(buffer);
    }

    protected long getBufferPtrAndMarkDirty(int uni) {
        this.mUniformsDirty = true;
        return this.mUniformData + (long) (uni & 16777215);
    }
}