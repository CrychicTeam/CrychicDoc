package com.mojang.blaze3d.shaders;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;

public class Uniform extends AbstractUniform implements AutoCloseable {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int UT_INT1 = 0;

    public static final int UT_INT2 = 1;

    public static final int UT_INT3 = 2;

    public static final int UT_INT4 = 3;

    public static final int UT_FLOAT1 = 4;

    public static final int UT_FLOAT2 = 5;

    public static final int UT_FLOAT3 = 6;

    public static final int UT_FLOAT4 = 7;

    public static final int UT_MAT2 = 8;

    public static final int UT_MAT3 = 9;

    public static final int UT_MAT4 = 10;

    private static final boolean TRANSPOSE_MATRICIES = false;

    private int location;

    private final int count;

    private final int type;

    private final IntBuffer intValues;

    private final FloatBuffer floatValues;

    private final String name;

    private boolean dirty;

    private final Shader parent;

    public Uniform(String string0, int int1, int int2, Shader shader3) {
        this.name = string0;
        this.count = int2;
        this.type = int1;
        this.parent = shader3;
        if (int1 <= 3) {
            this.intValues = MemoryUtil.memAllocInt(int2);
            this.floatValues = null;
        } else {
            this.intValues = null;
            this.floatValues = MemoryUtil.memAllocFloat(int2);
        }
        this.location = -1;
        this.markDirty();
    }

    public static int glGetUniformLocation(int int0, CharSequence charSequence1) {
        return GlStateManager._glGetUniformLocation(int0, charSequence1);
    }

    public static void uploadInteger(int int0, int int1) {
        RenderSystem.glUniform1i(int0, int1);
    }

    public static int glGetAttribLocation(int int0, CharSequence charSequence1) {
        return GlStateManager._glGetAttribLocation(int0, charSequence1);
    }

    public static void glBindAttribLocation(int int0, int int1, CharSequence charSequence2) {
        GlStateManager._glBindAttribLocation(int0, int1, charSequence2);
    }

    public void close() {
        if (this.intValues != null) {
            MemoryUtil.memFree(this.intValues);
        }
        if (this.floatValues != null) {
            MemoryUtil.memFree(this.floatValues);
        }
    }

    private void markDirty() {
        this.dirty = true;
        if (this.parent != null) {
            this.parent.markDirty();
        }
    }

    public static int getTypeFromString(String string0) {
        int $$1 = -1;
        if ("int".equals(string0)) {
            $$1 = 0;
        } else if ("float".equals(string0)) {
            $$1 = 4;
        } else if (string0.startsWith("matrix")) {
            if (string0.endsWith("2x2")) {
                $$1 = 8;
            } else if (string0.endsWith("3x3")) {
                $$1 = 9;
            } else if (string0.endsWith("4x4")) {
                $$1 = 10;
            }
        }
        return $$1;
    }

    public void setLocation(int int0) {
        this.location = int0;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public final void set(float float0) {
        this.floatValues.position(0);
        this.floatValues.put(0, float0);
        this.markDirty();
    }

    @Override
    public final void set(float float0, float float1) {
        this.floatValues.position(0);
        this.floatValues.put(0, float0);
        this.floatValues.put(1, float1);
        this.markDirty();
    }

    public final void set(int int0, float float1) {
        this.floatValues.position(0);
        this.floatValues.put(int0, float1);
        this.markDirty();
    }

    @Override
    public final void set(float float0, float float1, float float2) {
        this.floatValues.position(0);
        this.floatValues.put(0, float0);
        this.floatValues.put(1, float1);
        this.floatValues.put(2, float2);
        this.markDirty();
    }

    @Override
    public final void set(Vector3f vectorF0) {
        this.floatValues.position(0);
        vectorF0.get(this.floatValues);
        this.markDirty();
    }

    @Override
    public final void set(float float0, float float1, float float2, float float3) {
        this.floatValues.position(0);
        this.floatValues.put(float0);
        this.floatValues.put(float1);
        this.floatValues.put(float2);
        this.floatValues.put(float3);
        this.floatValues.flip();
        this.markDirty();
    }

    @Override
    public final void set(Vector4f vectorF0) {
        this.floatValues.position(0);
        vectorF0.get(this.floatValues);
        this.markDirty();
    }

    @Override
    public final void setSafe(float float0, float float1, float float2, float float3) {
        this.floatValues.position(0);
        if (this.type >= 4) {
            this.floatValues.put(0, float0);
        }
        if (this.type >= 5) {
            this.floatValues.put(1, float1);
        }
        if (this.type >= 6) {
            this.floatValues.put(2, float2);
        }
        if (this.type >= 7) {
            this.floatValues.put(3, float3);
        }
        this.markDirty();
    }

    @Override
    public final void setSafe(int int0, int int1, int int2, int int3) {
        this.intValues.position(0);
        if (this.type >= 0) {
            this.intValues.put(0, int0);
        }
        if (this.type >= 1) {
            this.intValues.put(1, int1);
        }
        if (this.type >= 2) {
            this.intValues.put(2, int2);
        }
        if (this.type >= 3) {
            this.intValues.put(3, int3);
        }
        this.markDirty();
    }

    @Override
    public final void set(int int0) {
        this.intValues.position(0);
        this.intValues.put(0, int0);
        this.markDirty();
    }

    @Override
    public final void set(int int0, int int1) {
        this.intValues.position(0);
        this.intValues.put(0, int0);
        this.intValues.put(1, int1);
        this.markDirty();
    }

    @Override
    public final void set(int int0, int int1, int int2) {
        this.intValues.position(0);
        this.intValues.put(0, int0);
        this.intValues.put(1, int1);
        this.intValues.put(2, int2);
        this.markDirty();
    }

    @Override
    public final void set(int int0, int int1, int int2, int int3) {
        this.intValues.position(0);
        this.intValues.put(0, int0);
        this.intValues.put(1, int1);
        this.intValues.put(2, int2);
        this.intValues.put(3, int3);
        this.markDirty();
    }

    @Override
    public final void set(float[] float0) {
        if (float0.length < this.count) {
            LOGGER.warn("Uniform.set called with a too-small value array (expected {}, got {}). Ignoring.", this.count, float0.length);
        } else {
            this.floatValues.position(0);
            this.floatValues.put(float0);
            this.floatValues.position(0);
            this.markDirty();
        }
    }

    @Override
    public final void setMat2x2(float float0, float float1, float float2, float float3) {
        this.floatValues.position(0);
        this.floatValues.put(0, float0);
        this.floatValues.put(1, float1);
        this.floatValues.put(2, float2);
        this.floatValues.put(3, float3);
        this.markDirty();
    }

    @Override
    public final void setMat2x3(float float0, float float1, float float2, float float3, float float4, float float5) {
        this.floatValues.position(0);
        this.floatValues.put(0, float0);
        this.floatValues.put(1, float1);
        this.floatValues.put(2, float2);
        this.floatValues.put(3, float3);
        this.floatValues.put(4, float4);
        this.floatValues.put(5, float5);
        this.markDirty();
    }

    @Override
    public final void setMat2x4(float float0, float float1, float float2, float float3, float float4, float float5, float float6, float float7) {
        this.floatValues.position(0);
        this.floatValues.put(0, float0);
        this.floatValues.put(1, float1);
        this.floatValues.put(2, float2);
        this.floatValues.put(3, float3);
        this.floatValues.put(4, float4);
        this.floatValues.put(5, float5);
        this.floatValues.put(6, float6);
        this.floatValues.put(7, float7);
        this.markDirty();
    }

    @Override
    public final void setMat3x2(float float0, float float1, float float2, float float3, float float4, float float5) {
        this.floatValues.position(0);
        this.floatValues.put(0, float0);
        this.floatValues.put(1, float1);
        this.floatValues.put(2, float2);
        this.floatValues.put(3, float3);
        this.floatValues.put(4, float4);
        this.floatValues.put(5, float5);
        this.markDirty();
    }

    @Override
    public final void setMat3x3(float float0, float float1, float float2, float float3, float float4, float float5, float float6, float float7, float float8) {
        this.floatValues.position(0);
        this.floatValues.put(0, float0);
        this.floatValues.put(1, float1);
        this.floatValues.put(2, float2);
        this.floatValues.put(3, float3);
        this.floatValues.put(4, float4);
        this.floatValues.put(5, float5);
        this.floatValues.put(6, float6);
        this.floatValues.put(7, float7);
        this.floatValues.put(8, float8);
        this.markDirty();
    }

    @Override
    public final void setMat3x4(float float0, float float1, float float2, float float3, float float4, float float5, float float6, float float7, float float8, float float9, float float10, float float11) {
        this.floatValues.position(0);
        this.floatValues.put(0, float0);
        this.floatValues.put(1, float1);
        this.floatValues.put(2, float2);
        this.floatValues.put(3, float3);
        this.floatValues.put(4, float4);
        this.floatValues.put(5, float5);
        this.floatValues.put(6, float6);
        this.floatValues.put(7, float7);
        this.floatValues.put(8, float8);
        this.floatValues.put(9, float9);
        this.floatValues.put(10, float10);
        this.floatValues.put(11, float11);
        this.markDirty();
    }

    @Override
    public final void setMat4x2(float float0, float float1, float float2, float float3, float float4, float float5, float float6, float float7) {
        this.floatValues.position(0);
        this.floatValues.put(0, float0);
        this.floatValues.put(1, float1);
        this.floatValues.put(2, float2);
        this.floatValues.put(3, float3);
        this.floatValues.put(4, float4);
        this.floatValues.put(5, float5);
        this.floatValues.put(6, float6);
        this.floatValues.put(7, float7);
        this.markDirty();
    }

    @Override
    public final void setMat4x3(float float0, float float1, float float2, float float3, float float4, float float5, float float6, float float7, float float8, float float9, float float10, float float11) {
        this.floatValues.position(0);
        this.floatValues.put(0, float0);
        this.floatValues.put(1, float1);
        this.floatValues.put(2, float2);
        this.floatValues.put(3, float3);
        this.floatValues.put(4, float4);
        this.floatValues.put(5, float5);
        this.floatValues.put(6, float6);
        this.floatValues.put(7, float7);
        this.floatValues.put(8, float8);
        this.floatValues.put(9, float9);
        this.floatValues.put(10, float10);
        this.floatValues.put(11, float11);
        this.markDirty();
    }

    @Override
    public final void setMat4x4(float float0, float float1, float float2, float float3, float float4, float float5, float float6, float float7, float float8, float float9, float float10, float float11, float float12, float float13, float float14, float float15) {
        this.floatValues.position(0);
        this.floatValues.put(0, float0);
        this.floatValues.put(1, float1);
        this.floatValues.put(2, float2);
        this.floatValues.put(3, float3);
        this.floatValues.put(4, float4);
        this.floatValues.put(5, float5);
        this.floatValues.put(6, float6);
        this.floatValues.put(7, float7);
        this.floatValues.put(8, float8);
        this.floatValues.put(9, float9);
        this.floatValues.put(10, float10);
        this.floatValues.put(11, float11);
        this.floatValues.put(12, float12);
        this.floatValues.put(13, float13);
        this.floatValues.put(14, float14);
        this.floatValues.put(15, float15);
        this.markDirty();
    }

    @Override
    public final void set(Matrix4f matrixF0) {
        this.floatValues.position(0);
        matrixF0.get(this.floatValues);
        this.markDirty();
    }

    @Override
    public final void set(Matrix3f matrixF0) {
        this.floatValues.position(0);
        matrixF0.get(this.floatValues);
        this.markDirty();
    }

    public void upload() {
        if (!this.dirty) {
        }
        this.dirty = false;
        if (this.type <= 3) {
            this.uploadAsInteger();
        } else if (this.type <= 7) {
            this.uploadAsFloat();
        } else {
            if (this.type > 10) {
                LOGGER.warn("Uniform.upload called, but type value ({}) is not a valid type. Ignoring.", this.type);
                return;
            }
            this.uploadAsMatrix();
        }
    }

    private void uploadAsInteger() {
        this.intValues.rewind();
        switch(this.type) {
            case 0:
                RenderSystem.glUniform1(this.location, this.intValues);
                break;
            case 1:
                RenderSystem.glUniform2(this.location, this.intValues);
                break;
            case 2:
                RenderSystem.glUniform3(this.location, this.intValues);
                break;
            case 3:
                RenderSystem.glUniform4(this.location, this.intValues);
                break;
            default:
                LOGGER.warn("Uniform.upload called, but count value ({}) is  not in the range of 1 to 4. Ignoring.", this.count);
        }
    }

    private void uploadAsFloat() {
        this.floatValues.rewind();
        switch(this.type) {
            case 4:
                RenderSystem.glUniform1(this.location, this.floatValues);
                break;
            case 5:
                RenderSystem.glUniform2(this.location, this.floatValues);
                break;
            case 6:
                RenderSystem.glUniform3(this.location, this.floatValues);
                break;
            case 7:
                RenderSystem.glUniform4(this.location, this.floatValues);
                break;
            default:
                LOGGER.warn("Uniform.upload called, but count value ({}) is not in the range of 1 to 4. Ignoring.", this.count);
        }
    }

    private void uploadAsMatrix() {
        this.floatValues.clear();
        switch(this.type) {
            case 8:
                RenderSystem.glUniformMatrix2(this.location, false, this.floatValues);
                break;
            case 9:
                RenderSystem.glUniformMatrix3(this.location, false, this.floatValues);
                break;
            case 10:
                RenderSystem.glUniformMatrix4(this.location, false, this.floatValues);
        }
    }

    public int getLocation() {
        return this.location;
    }

    public int getCount() {
        return this.count;
    }

    public int getType() {
        return this.type;
    }

    public IntBuffer getIntBuffer() {
        return this.intValues;
    }

    public FloatBuffer getFloatBuffer() {
        return this.floatValues;
    }
}