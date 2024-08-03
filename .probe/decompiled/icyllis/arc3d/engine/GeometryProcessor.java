package icyllis.arc3d.engine;

import icyllis.arc3d.core.MathUtil;
import icyllis.arc3d.core.Matrix;
import icyllis.arc3d.core.SLDataType;
import icyllis.arc3d.engine.shading.FPFragmentBuilder;
import icyllis.arc3d.engine.shading.UniformHandler;
import icyllis.arc3d.engine.shading.VaryingHandler;
import icyllis.arc3d.engine.shading.VertexGeomBuilder;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public abstract class GeometryProcessor extends Processor {

    private int mVertexAttributesMask;

    private int mInstanceAttributesMask;

    @Nonnull
    protected static GeometryProcessor.Attribute makeColorAttribute(String name, boolean wideColor) {
        return new GeometryProcessor.Attribute(name, (byte) (wideColor ? 3 : 17), (byte) 16);
    }

    protected GeometryProcessor(int classID) {
        super(classID);
    }

    public abstract byte primitiveType();

    public int numTextureSamplers() {
        return 0;
    }

    public int textureSamplerState(int i) {
        throw new IndexOutOfBoundsException(i);
    }

    public short textureSamplerSwizzle(int i) {
        throw new IndexOutOfBoundsException(i);
    }

    public final boolean hasVertexAttributes() {
        assert this.mVertexAttributesMask == 0 || this.allVertexAttributes() != null;
        return this.mVertexAttributesMask != 0;
    }

    public final int numVertexAttributes() {
        return Integer.bitCount(this.mVertexAttributesMask);
    }

    public final int numVertexLocations() {
        assert this.mVertexAttributesMask == 0 || this.allVertexAttributes() != null;
        return this.mVertexAttributesMask == 0 ? 0 : this.allVertexAttributes().numLocations(this.mVertexAttributesMask);
    }

    @Nonnull
    public final Iterable<GeometryProcessor.Attribute> vertexAttributes() {
        assert this.mVertexAttributesMask == 0 || this.allVertexAttributes() != null;
        if (this.mVertexAttributesMask == 0) {
            return Collections.emptySet();
        } else {
            GeometryProcessor.AttributeSet attrs = this.allVertexAttributes();
            return (Iterable<GeometryProcessor.Attribute>) (this.mVertexAttributesMask == attrs.mAllMask ? attrs : new Iterable<GeometryProcessor.Attribute>() {

                @Nonnull
                public Iterator<GeometryProcessor.Attribute> iterator() {
                    return GeometryProcessor.this.allVertexAttributes().new Iter(GeometryProcessor.this.mVertexAttributesMask);
                }
            });
        }
    }

    public final int vertexStride() {
        assert this.mVertexAttributesMask == 0 || this.allVertexAttributes() != null;
        return this.mVertexAttributesMask == 0 ? 0 : this.allVertexAttributes().stride(this.mVertexAttributesMask);
    }

    public final boolean hasInstanceAttributes() {
        assert this.mInstanceAttributesMask == 0 || this.allInstanceAttributes() != null;
        return this.mInstanceAttributesMask != 0;
    }

    public final int numInstanceAttributes() {
        return Integer.bitCount(this.mInstanceAttributesMask);
    }

    public final int numInstanceLocations() {
        assert this.mInstanceAttributesMask == 0 || this.allInstanceAttributes() != null;
        return this.mInstanceAttributesMask == 0 ? 0 : this.allInstanceAttributes().numLocations(this.mInstanceAttributesMask);
    }

    @Nonnull
    public final Iterable<GeometryProcessor.Attribute> instanceAttributes() {
        assert this.mInstanceAttributesMask == 0 || this.allInstanceAttributes() != null;
        if (this.mInstanceAttributesMask == 0) {
            return Collections.emptySet();
        } else {
            GeometryProcessor.AttributeSet attrs = this.allInstanceAttributes();
            return (Iterable<GeometryProcessor.Attribute>) (this.mInstanceAttributesMask == attrs.mAllMask ? attrs : new Iterable<GeometryProcessor.Attribute>() {

                @Nonnull
                public Iterator<GeometryProcessor.Attribute> iterator() {
                    return GeometryProcessor.this.allInstanceAttributes().new Iter(GeometryProcessor.this.mInstanceAttributesMask);
                }
            });
        }
    }

    public final int instanceStride() {
        assert this.mInstanceAttributesMask == 0 || this.allInstanceAttributes() != null;
        return this.mInstanceAttributesMask == 0 ? 0 : this.allInstanceAttributes().stride(this.mInstanceAttributesMask);
    }

    public abstract void appendToKey(@Nonnull KeyBuilder var1);

    public final void appendAttributesToKey(@Nonnull KeyBuilder b) {
        GeometryProcessor.AttributeSet vertexAttributes = this.allVertexAttributes();
        if (vertexAttributes != null) {
            b.appendComment("vertex attributes");
            vertexAttributes.appendToKey(b, this.mVertexAttributesMask);
        }
        GeometryProcessor.AttributeSet instanceAttributes = this.allInstanceAttributes();
        if (instanceAttributes != null) {
            b.appendComment("instance attributes");
            instanceAttributes.appendToKey(b, this.mInstanceAttributesMask);
        }
    }

    @Nonnull
    public abstract GeometryProcessor.ProgramImpl makeProgramImpl(ShaderCaps var1);

    @Nullable
    protected abstract GeometryProcessor.AttributeSet allVertexAttributes();

    protected final void setVertexAttributes(int mask) {
        GeometryProcessor.AttributeSet attrs = this.allVertexAttributes();
        assert attrs != null;
        this.mVertexAttributesMask = this.mVertexAttributesMask | mask & attrs.mAllMask;
    }

    @Nullable
    protected abstract GeometryProcessor.AttributeSet allInstanceAttributes();

    protected final void setInstanceAttributes(int mask) {
        GeometryProcessor.AttributeSet attrs = this.allInstanceAttributes();
        assert attrs != null;
        this.mInstanceAttributesMask = this.mInstanceAttributesMask | mask & attrs.mAllMask;
    }

    @Immutable
    public static class Attribute {

        static final int IMPLICIT_OFFSET = 1;

        private final String mName;

        private final byte mSrcType;

        private final byte mDstType;

        private final short mOffset;

        public static int alignOffset(int offset) {
            return MathUtil.align4(offset);
        }

        public Attribute(@Nonnull String name, byte srcType, byte dstType) {
            if (name.isEmpty() || name.startsWith("_")) {
                throw new IllegalArgumentException();
            } else if (srcType < 0 || srcType > 25) {
                throw new IllegalArgumentException();
            } else if (SLDataType.locations(dstType) <= 0) {
                throw new IllegalArgumentException();
            } else {
                this.mName = name;
                this.mSrcType = srcType;
                this.mDstType = dstType;
                this.mOffset = 1;
            }
        }

        public Attribute(@Nonnull String name, byte srcType, byte dstType, int offset) {
            if (name.isEmpty() || name.startsWith("_")) {
                throw new IllegalArgumentException();
            } else if (srcType < 0 || srcType > 25) {
                throw new IllegalArgumentException();
            } else if (SLDataType.locations(dstType) <= 0) {
                throw new IllegalArgumentException();
            } else if (offset >= 0 && offset < 32768 && alignOffset(offset) == offset) {
                this.mName = name;
                this.mSrcType = srcType;
                this.mDstType = dstType;
                this.mOffset = (short) offset;
            } else {
                throw new IllegalArgumentException();
            }
        }

        public final String name() {
            return this.mName;
        }

        public final byte srcType() {
            return this.mSrcType;
        }

        public final byte dstType() {
            return this.mDstType;
        }

        public final int offset() {
            assert this.mOffset >= 0;
            return this.mOffset;
        }

        public final int size() {
            return Engine.VertexAttribType.size(this.mSrcType);
        }

        public final int locations() {
            return SLDataType.locations(this.mDstType);
        }

        public final int stride() {
            int size = this.size();
            int count = this.locations();
            assert size > 0 && count > 0;
            return size * count;
        }

        @Nonnull
        public final ShaderVar asShaderVar() {
            return new ShaderVar(this.mName, this.mDstType, (byte) 2);
        }
    }

    @Immutable
    public static class AttributeSet implements Iterable<GeometryProcessor.Attribute> {

        private final GeometryProcessor.Attribute[] mAttributes;

        private final int mStride;

        final int mAllMask;

        private AttributeSet(@Nonnull GeometryProcessor.Attribute[] attributes, int stride) {
            int offset = 0;
            for (GeometryProcessor.Attribute attr : attributes) {
                if (attr.offset() != 1) {
                    if (attr.offset() < offset) {
                        throw new IllegalArgumentException();
                    }
                    offset = attr.offset();
                    assert GeometryProcessor.Attribute.alignOffset(offset) == offset;
                }
            }
            this.mAttributes = attributes;
            this.mStride = stride;
            this.mAllMask = -1 >>> 32 - this.mAttributes.length;
        }

        @Nonnull
        public static GeometryProcessor.AttributeSet makeImplicit(@Nonnull GeometryProcessor.Attribute... attrs) {
            if (attrs.length != 0 && attrs.length <= 32) {
                return new GeometryProcessor.AttributeSet(attrs, 1);
            } else {
                throw new IllegalArgumentException();
            }
        }

        @Nonnull
        public static GeometryProcessor.AttributeSet makeExplicit(int stride, @Nonnull GeometryProcessor.Attribute... attrs) {
            if (attrs.length == 0 || attrs.length > 32) {
                throw new IllegalArgumentException();
            } else if (stride <= 0 || stride > 32768) {
                throw new IllegalArgumentException();
            } else if (GeometryProcessor.Attribute.alignOffset(stride) != stride) {
                throw new IllegalArgumentException();
            } else {
                return new GeometryProcessor.AttributeSet(attrs, stride);
            }
        }

        final int stride(int mask) {
            if (this.mStride != 1) {
                return this.mStride;
            } else {
                int rawCount = this.mAttributes.length;
                int stride = 0;
                int i = 0;
                for (int bit = 1; i < rawCount; bit <<= 1) {
                    GeometryProcessor.Attribute attr = this.mAttributes[i];
                    if ((mask & bit) != 0) {
                        if (attr.offset() != 1) {
                            stride = attr.offset();
                        }
                        stride += GeometryProcessor.Attribute.alignOffset(attr.stride());
                    }
                    i++;
                }
                return stride;
            }
        }

        final int numLocations(int mask) {
            int rawCount = this.mAttributes.length;
            int locations = 0;
            int i = 0;
            for (int bit = 1; i < rawCount; bit <<= 1) {
                GeometryProcessor.Attribute attr = this.mAttributes[i];
                if ((mask & bit) != 0) {
                    locations += attr.locations();
                }
                i++;
            }
            return locations;
        }

        final void appendToKey(@Nonnull KeyBuilder b, int mask) {
            int rawCount = this.mAttributes.length;
            b.addBits(6, rawCount, "attribute count");
            int offset = 0;
            int i = 0;
            for (int bit = 1; i < rawCount; bit <<= 1) {
                GeometryProcessor.Attribute attr = this.mAttributes[i];
                if ((mask & bit) != 0) {
                    b.appendComment(attr.name());
                    b.addBits(8, attr.srcType() & 255, "attrType");
                    b.addBits(8, attr.dstType() & 255, "attrGpuType");
                    if (attr.offset() != 1) {
                        offset = attr.offset();
                    }
                    assert offset >= 0 && offset < 32768;
                    b.addBits(16, offset, "attrOffset");
                    offset += GeometryProcessor.Attribute.alignOffset(attr.stride());
                } else {
                    b.appendComment("unusedAttr");
                    b.addBits(8, 255, "attrType");
                    b.addBits(8, 255, "attrGpuType");
                    b.addBits(16, 65535, "attrOffset");
                }
                i++;
            }
            if (this.mStride == 1) {
                i = offset;
            } else {
                i = this.mStride;
                if (i < offset) {
                    throw new IllegalStateException();
                }
            }
            assert i > 0 && i <= 32768;
            assert GeometryProcessor.Attribute.alignOffset(i) == i;
            b.addBits(16, i, "stride");
        }

        @Nonnull
        public Iterator<GeometryProcessor.Attribute> iterator() {
            return new GeometryProcessor.AttributeSet.Iter(this.mAllMask);
        }

        private class Iter implements Iterator<GeometryProcessor.Attribute> {

            private final int mMask;

            private int mIndex;

            private int mOffset;

            Iter(int mask) {
                this.mMask = mask;
            }

            public boolean hasNext() {
                this.forward();
                return this.mIndex < AttributeSet.this.mAttributes.length;
            }

            @Nonnull
            public GeometryProcessor.Attribute next() {
                this.forward();
                try {
                    GeometryProcessor.Attribute curr = AttributeSet.this.mAttributes[this.mIndex++];
                    GeometryProcessor.Attribute ret;
                    if (curr.offset() == 1) {
                        ret = new GeometryProcessor.Attribute(curr.name(), curr.srcType(), curr.dstType(), this.mOffset);
                    } else {
                        ret = curr;
                        this.mOffset = curr.offset();
                    }
                    this.mOffset = this.mOffset + GeometryProcessor.Attribute.alignOffset(curr.stride());
                    return ret;
                } catch (IndexOutOfBoundsException var3) {
                    throw new NoSuchElementException(var3);
                }
            }

            private void forward() {
                while (this.mIndex < AttributeSet.this.mAttributes.length && (this.mMask & 1 << this.mIndex) == 0) {
                    this.mIndex++;
                }
            }
        }
    }

    public abstract static class ProgramImpl {

        protected static Matrix setTransform(@Nonnull UniformDataManager pdm, int uniform, @Nonnull Matrix matrix, @Nullable Matrix state) {
            if (uniform != -1 && (state == null || !state.equals(matrix))) {
                if (matrix.isScaleTranslate()) {
                    pdm.set4f(uniform, matrix.getScaleX(), matrix.getTranslateX(), matrix.getScaleY(), matrix.getTranslateY());
                } else {
                    pdm.setMatrix3f(uniform, matrix);
                }
                return matrix;
            } else {
                return state;
            }
        }

        protected static void writePassthroughWorldPosition(VertexGeomBuilder vertBuilder, ShaderVar inPos, ShaderVar outPos) {
            assert inPos.getType() == 14 || inPos.getType() == 15;
            vertBuilder.codeAppendf("vec%d _worldPos = %s;\n", new Object[] { SLDataType.vectorDim(inPos.getType()), inPos.getName() });
            outPos.set("_worldPos", inPos.getType());
        }

        protected static void writeWorldPosition(VertexGeomBuilder vertBuilder, ShaderVar inPos, String matrixName, ShaderVar outPos) {
            assert inPos.getType() == 14 || inPos.getType() == 15;
            if (inPos.getType() == 15) {
                vertBuilder.codeAppendf("vec3 _worldPos = %s * %s;\n", new Object[] { matrixName, inPos.getName() });
                outPos.set("_worldPos", (byte) 15);
            } else {
                vertBuilder.codeAppendf("vec3 _worldPos = %s * vec3(%s, 1.0);\n", new Object[] { matrixName, inPos.getName() });
                outPos.set("_worldPos", (byte) 15);
            }
        }

        public final void emitCode(VertexGeomBuilder vertBuilder, FPFragmentBuilder fragBuilder, VaryingHandler varyingHandler, UniformHandler uniformHandler, ShaderCaps shaderCaps, GeometryProcessor geomProc, String outputColor, String outputCoverage, int[] texSamplers) {
            ShaderVar localPos = new ShaderVar();
            ShaderVar worldPos = new ShaderVar();
            this.onEmitCode(vertBuilder, fragBuilder, varyingHandler, uniformHandler, shaderCaps, geomProc, outputColor, outputCoverage, texSamplers, localPos, worldPos);
            assert worldPos.getType() == 14 || worldPos.getType() == 15;
            vertBuilder.emitNormalizedPosition(worldPos);
            if (worldPos.getType() == 14) {
                varyingHandler.setNoPerspective();
            }
        }

        public abstract void setData(UniformDataManager var1, GeometryProcessor var2);

        protected abstract void onEmitCode(VertexGeomBuilder var1, FPFragmentBuilder var2, VaryingHandler var3, UniformHandler var4, ShaderCaps var5, GeometryProcessor var6, String var7, String var8, int[] var9, ShaderVar var10, ShaderVar var11);
    }
}