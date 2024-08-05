package icyllis.arc3d.engine.shading;

import icyllis.arc3d.core.SLDataType;
import icyllis.arc3d.engine.Processor;
import icyllis.arc3d.engine.ShaderVar;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Nullable;

public abstract class UniformHandler {

    public static final String NO_MANGLE_PREFIX = "SV_";

    public static final String PROJECTION_NAME = "SV_Projection";

    public static final boolean Std140Layout = false;

    public static final boolean Std430Layout = true;

    public static final int MAIN_DESC_SET = 0;

    public static final int SAMPLER_DESC_SET = 1;

    public static final int INPUT_DESC_SET = 2;

    public static final int UNIFORM_BINDING = 0;

    public static final String UNIFORM_BLOCK_NAME = "UniformBlock";

    public static final int INPUT_BINDING = 0;

    protected final PipelineBuilder mPipelineBuilder;

    protected UniformHandler(PipelineBuilder pipelineBuilder) {
        this.mPipelineBuilder = pipelineBuilder;
    }

    public final int addUniform(Processor owner, int visibility, byte type, String name) {
        assert name != null && !name.isEmpty();
        assert (visibility & -4) == 0;
        assert SLDataType.checkSLType(type);
        assert !SLDataType.isCombinedSamplerType(type);
        return this.internalAddUniformArray(owner, visibility, type, name, 0);
    }

    public final int addUniformArray(Processor owner, int visibility, byte type, String name, int arrayCount) {
        assert name != null && !name.isEmpty();
        assert (visibility & -4) == 0;
        assert SLDataType.checkSLType(type);
        assert !SLDataType.isCombinedSamplerType(type);
        assert arrayCount >= 1;
        return this.internalAddUniformArray(owner, visibility, type, name, arrayCount);
    }

    public abstract ShaderVar getUniformVariable(int var1);

    public final String getUniformName(int handle) {
        return this.getUniformVariable(handle).getName();
    }

    public abstract int numUniforms();

    public abstract UniformHandler.UniformInfo uniform(int var1);

    @Nullable
    public final ShaderVar getUniformMapping(Processor owner, String rawName) {
        for (int i = this.numUniforms() - 1; i >= 0; i--) {
            UniformHandler.UniformInfo u = this.uniform(i);
            if (u.mOwner == owner && u.mRawName.equals(rawName)) {
                return u.mVariable;
            }
        }
        return null;
    }

    @Nullable
    public final ShaderVar liftUniformToVertexShader(Processor owner, String rawName) {
        for (int i = this.numUniforms() - 1; i >= 0; i--) {
            UniformHandler.UniformInfo u = this.uniform(i);
            if (u.mOwner == owner && u.mRawName.equals(rawName)) {
                u.mVisibility |= 1;
                return u.mVariable;
            }
        }
        return null;
    }

    protected abstract int internalAddUniformArray(Processor var1, int var2, byte var3, String var4, int var5);

    protected abstract int addSampler(int var1, short var2, String var3);

    protected abstract String samplerVariable(int var1);

    protected abstract short samplerSwizzle(int var1);

    protected int addInputSampler(short swizzle, String name) {
        throw new UnsupportedOperationException();
    }

    protected String inputSamplerVariable(int handle) {
        throw new UnsupportedOperationException();
    }

    protected short inputSamplerSwizzle(int handle) {
        throw new UnsupportedOperationException();
    }

    protected abstract void appendUniformDecls(int var1, StringBuilder var2);

    public static int getAlignmentMask(byte type, boolean nonArray, boolean layout) {
        switch(type) {
            case 0:
            case 35:
            case 36:
            case 37:
            case 38:
                throw new IllegalStateException(String.valueOf(type));
            case 1:
            case 13:
            case 27:
            case 31:
                return !layout && !nonArray ? 15 : 3;
            case 2:
            case 14:
            case 28:
            case 32:
                return !layout && !nonArray ? 15 : 7;
            case 3:
            case 4:
            case 15:
            case 16:
            case 18:
            case 19:
            case 29:
            case 30:
            case 33:
            case 34:
                return 15;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
                throw new IllegalArgumentException(String.valueOf(type));
            case 17:
                return layout ? 7 : 15;
        }
    }

    public static int getSize(byte type, boolean layout) {
        switch(type) {
            case 0:
            case 35:
            case 36:
            case 37:
            case 38:
                throw new IllegalStateException(String.valueOf(type));
            case 1:
            case 27:
            case 31:
                return 4;
            case 2:
            case 28:
            case 32:
                return 8;
            case 3:
            case 29:
            case 33:
                return 12;
            case 4:
            case 30:
            case 34:
                return 16;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
                throw new IllegalArgumentException(String.valueOf(type));
            case 13:
                return 4;
            case 14:
                return 8;
            case 15:
                return 12;
            case 16:
                return 16;
            case 17:
                return layout ? 16 : 32;
            case 18:
                return 48;
            case 19:
                return 64;
        }
    }

    public static int getAlignedOffset(int offset, byte type, int arrayCount, boolean layout) {
        assert SLDataType.checkSLType(type);
        assert arrayCount == 0 || arrayCount >= 1;
        int alignmentMask = getAlignmentMask(type, arrayCount == 0, layout);
        return offset + alignmentMask & ~alignmentMask;
    }

    public static int getAlignedStride(byte type, int arrayCount, boolean layout) {
        assert SLDataType.checkSLType(type);
        assert arrayCount == 0 || arrayCount >= 1;
        if (arrayCount == 0) {
            return getSize(type, layout);
        } else {
            int elementSize;
            if (layout) {
                elementSize = getSize(type, true);
            } else {
                elementSize = Math.max(getSize(type, false), 16);
            }
            assert (elementSize & 15) == 0;
            return elementSize * arrayCount;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SamplerHandle {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface UniformHandle {
    }

    public static class UniformInfo {

        public ShaderVar mVariable;

        public int mVisibility;

        public Processor mOwner;

        public String mRawName;

        public int mOffset;
    }
}