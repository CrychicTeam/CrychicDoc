package com.mojang.blaze3d.vertex;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.MemoryTracker;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import javax.annotation.Nullable;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.mutable.MutableInt;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;

public class BufferBuilder extends DefaultedVertexConsumer implements BufferVertexConsumer {

    private static final int GROWTH_SIZE = 2097152;

    private static final Logger LOGGER = LogUtils.getLogger();

    private ByteBuffer buffer;

    private int renderedBufferCount;

    private int renderedBufferPointer;

    private int nextElementByte;

    private int vertices;

    @Nullable
    private VertexFormatElement currentElement;

    private int elementIndex;

    private VertexFormat format;

    private VertexFormat.Mode mode;

    private boolean fastFormat;

    private boolean fullFormat;

    private boolean building;

    @Nullable
    private Vector3f[] sortingPoints;

    @Nullable
    private VertexSorting sorting;

    private boolean indexOnly;

    public BufferBuilder(int int0) {
        this.buffer = MemoryTracker.create(int0 * 6);
    }

    private void ensureVertexCapacity() {
        this.ensureCapacity(this.format.getVertexSize());
    }

    private void ensureCapacity(int int0) {
        if (this.nextElementByte + int0 > this.buffer.capacity()) {
            int $$1 = this.buffer.capacity();
            int $$2 = $$1 + roundUp(int0);
            LOGGER.debug("Needed to grow BufferBuilder buffer: Old size {} bytes, new size {} bytes.", $$1, $$2);
            ByteBuffer $$3 = MemoryTracker.resize(this.buffer, $$2);
            $$3.rewind();
            this.buffer = $$3;
        }
    }

    private static int roundUp(int int0) {
        int $$1 = 2097152;
        if (int0 == 0) {
            return $$1;
        } else {
            if (int0 < 0) {
                $$1 *= -1;
            }
            int $$2 = int0 % $$1;
            return $$2 == 0 ? int0 : int0 + $$1 - $$2;
        }
    }

    public void setQuadSorting(VertexSorting vertexSorting0) {
        if (this.mode == VertexFormat.Mode.QUADS) {
            this.sorting = vertexSorting0;
            if (this.sortingPoints == null) {
                this.sortingPoints = this.makeQuadSortingPoints();
            }
        }
    }

    public BufferBuilder.SortState getSortState() {
        return new BufferBuilder.SortState(this.mode, this.vertices, this.sortingPoints, this.sorting);
    }

    public void restoreSortState(BufferBuilder.SortState bufferBuilderSortState0) {
        this.buffer.rewind();
        this.mode = bufferBuilderSortState0.mode;
        this.vertices = bufferBuilderSortState0.vertices;
        this.nextElementByte = this.renderedBufferPointer;
        this.sortingPoints = bufferBuilderSortState0.sortingPoints;
        this.sorting = bufferBuilderSortState0.sorting;
        this.indexOnly = true;
    }

    public void begin(VertexFormat.Mode vertexFormatMode0, VertexFormat vertexFormat1) {
        if (this.building) {
            throw new IllegalStateException("Already building!");
        } else {
            this.building = true;
            this.mode = vertexFormatMode0;
            this.switchFormat(vertexFormat1);
            this.currentElement = (VertexFormatElement) vertexFormat1.getElements().get(0);
            this.elementIndex = 0;
            this.buffer.rewind();
        }
    }

    private void switchFormat(VertexFormat vertexFormat0) {
        if (this.format != vertexFormat0) {
            this.format = vertexFormat0;
            boolean $$1 = vertexFormat0 == DefaultVertexFormat.NEW_ENTITY;
            boolean $$2 = vertexFormat0 == DefaultVertexFormat.BLOCK;
            this.fastFormat = $$1 || $$2;
            this.fullFormat = $$1;
        }
    }

    private IntConsumer intConsumer(int int0, VertexFormat.IndexType vertexFormatIndexType1) {
        MutableInt $$2 = new MutableInt(int0);
        return switch(vertexFormatIndexType1) {
            case SHORT ->
                p_231167_ -> this.buffer.putShort($$2.getAndAdd(2), (short) p_231167_);
            case INT ->
                p_231163_ -> this.buffer.putInt($$2.getAndAdd(4), p_231163_);
        };
    }

    private Vector3f[] makeQuadSortingPoints() {
        FloatBuffer $$0 = this.buffer.asFloatBuffer();
        int $$1 = this.renderedBufferPointer / 4;
        int $$2 = this.format.getIntegerSize();
        int $$3 = $$2 * this.mode.primitiveStride;
        int $$4 = this.vertices / this.mode.primitiveStride;
        Vector3f[] $$5 = new Vector3f[$$4];
        for (int $$6 = 0; $$6 < $$4; $$6++) {
            float $$7 = $$0.get($$1 + $$6 * $$3 + 0);
            float $$8 = $$0.get($$1 + $$6 * $$3 + 1);
            float $$9 = $$0.get($$1 + $$6 * $$3 + 2);
            float $$10 = $$0.get($$1 + $$6 * $$3 + $$2 * 2 + 0);
            float $$11 = $$0.get($$1 + $$6 * $$3 + $$2 * 2 + 1);
            float $$12 = $$0.get($$1 + $$6 * $$3 + $$2 * 2 + 2);
            float $$13 = ($$7 + $$10) / 2.0F;
            float $$14 = ($$8 + $$11) / 2.0F;
            float $$15 = ($$9 + $$12) / 2.0F;
            $$5[$$6] = new Vector3f($$13, $$14, $$15);
        }
        return $$5;
    }

    private void putSortedQuadIndices(VertexFormat.IndexType vertexFormatIndexType0) {
        if (this.sortingPoints != null && this.sorting != null) {
            int[] $$1 = this.sorting.sort(this.sortingPoints);
            IntConsumer $$2 = this.intConsumer(this.nextElementByte, vertexFormatIndexType0);
            for (int $$3 : $$1) {
                $$2.accept($$3 * this.mode.primitiveStride + 0);
                $$2.accept($$3 * this.mode.primitiveStride + 1);
                $$2.accept($$3 * this.mode.primitiveStride + 2);
                $$2.accept($$3 * this.mode.primitiveStride + 2);
                $$2.accept($$3 * this.mode.primitiveStride + 3);
                $$2.accept($$3 * this.mode.primitiveStride + 0);
            }
        } else {
            throw new IllegalStateException("Sorting state uninitialized");
        }
    }

    public boolean isCurrentBatchEmpty() {
        return this.vertices == 0;
    }

    @Nullable
    public BufferBuilder.RenderedBuffer endOrDiscardIfEmpty() {
        this.ensureDrawing();
        if (this.isCurrentBatchEmpty()) {
            this.reset();
            return null;
        } else {
            BufferBuilder.RenderedBuffer $$0 = this.storeRenderedBuffer();
            this.reset();
            return $$0;
        }
    }

    public BufferBuilder.RenderedBuffer end() {
        this.ensureDrawing();
        BufferBuilder.RenderedBuffer $$0 = this.storeRenderedBuffer();
        this.reset();
        return $$0;
    }

    private void ensureDrawing() {
        if (!this.building) {
            throw new IllegalStateException("Not building!");
        }
    }

    private BufferBuilder.RenderedBuffer storeRenderedBuffer() {
        int $$0 = this.mode.indexCount(this.vertices);
        int $$1 = !this.indexOnly ? this.vertices * this.format.getVertexSize() : 0;
        VertexFormat.IndexType $$2 = VertexFormat.IndexType.least($$0);
        boolean $$4;
        int $$5;
        if (this.sortingPoints != null) {
            int $$3 = Mth.roundToward($$0 * $$2.bytes, 4);
            this.ensureCapacity($$3);
            this.putSortedQuadIndices($$2);
            $$4 = false;
            this.nextElementByte += $$3;
            $$5 = $$1 + $$3;
        } else {
            $$4 = true;
            $$5 = $$1;
        }
        int $$8 = this.renderedBufferPointer;
        this.renderedBufferPointer += $$5;
        this.renderedBufferCount++;
        BufferBuilder.DrawState $$9 = new BufferBuilder.DrawState(this.format, this.vertices, $$0, this.mode, $$2, this.indexOnly, $$4);
        return new BufferBuilder.RenderedBuffer($$8, $$9);
    }

    private void reset() {
        this.building = false;
        this.vertices = 0;
        this.currentElement = null;
        this.elementIndex = 0;
        this.sortingPoints = null;
        this.sorting = null;
        this.indexOnly = false;
    }

    @Override
    public void putByte(int int0, byte byte1) {
        this.buffer.put(this.nextElementByte + int0, byte1);
    }

    @Override
    public void putShort(int int0, short short1) {
        this.buffer.putShort(this.nextElementByte + int0, short1);
    }

    @Override
    public void putFloat(int int0, float float1) {
        this.buffer.putFloat(this.nextElementByte + int0, float1);
    }

    @Override
    public void endVertex() {
        if (this.elementIndex != 0) {
            throw new IllegalStateException("Not filled all elements of the vertex");
        } else {
            this.vertices++;
            this.ensureVertexCapacity();
            if (this.mode == VertexFormat.Mode.LINES || this.mode == VertexFormat.Mode.LINE_STRIP) {
                int $$0 = this.format.getVertexSize();
                this.buffer.put(this.nextElementByte, this.buffer, this.nextElementByte - $$0, $$0);
                this.nextElementByte += $$0;
                this.vertices++;
                this.ensureVertexCapacity();
            }
        }
    }

    @Override
    public void nextElement() {
        ImmutableList<VertexFormatElement> $$0 = this.format.getElements();
        this.elementIndex = (this.elementIndex + 1) % $$0.size();
        this.nextElementByte = this.nextElementByte + this.currentElement.getByteSize();
        VertexFormatElement $$1 = (VertexFormatElement) $$0.get(this.elementIndex);
        this.currentElement = $$1;
        if ($$1.getUsage() == VertexFormatElement.Usage.PADDING) {
            this.nextElement();
        }
        if (this.f_85824_ && this.currentElement.getUsage() == VertexFormatElement.Usage.COLOR) {
            BufferVertexConsumer.super.color(this.f_85825_, this.f_85826_, this.f_85827_, this.f_85828_);
        }
    }

    @Override
    public VertexConsumer color(int int0, int int1, int int2, int int3) {
        if (this.f_85824_) {
            throw new IllegalStateException();
        } else {
            return BufferVertexConsumer.super.color(int0, int1, int2, int3);
        }
    }

    @Override
    public void vertex(float float0, float float1, float float2, float float3, float float4, float float5, float float6, float float7, float float8, int int9, int int10, float float11, float float12, float float13) {
        if (this.f_85824_) {
            throw new IllegalStateException();
        } else if (this.fastFormat) {
            this.putFloat(0, float0);
            this.putFloat(4, float1);
            this.putFloat(8, float2);
            this.putByte(12, (byte) ((int) (float3 * 255.0F)));
            this.putByte(13, (byte) ((int) (float4 * 255.0F)));
            this.putByte(14, (byte) ((int) (float5 * 255.0F)));
            this.putByte(15, (byte) ((int) (float6 * 255.0F)));
            this.putFloat(16, float7);
            this.putFloat(20, float8);
            int $$14;
            if (this.fullFormat) {
                this.putShort(24, (short) (int9 & 65535));
                this.putShort(26, (short) (int9 >> 16 & 65535));
                $$14 = 28;
            } else {
                $$14 = 24;
            }
            this.putShort($$14 + 0, (short) (int10 & 65535));
            this.putShort($$14 + 2, (short) (int10 >> 16 & 65535));
            this.putByte($$14 + 4, BufferVertexConsumer.normalIntValue(float11));
            this.putByte($$14 + 5, BufferVertexConsumer.normalIntValue(float12));
            this.putByte($$14 + 6, BufferVertexConsumer.normalIntValue(float13));
            this.nextElementByte += $$14 + 8;
            this.endVertex();
        } else {
            super.m_5954_(float0, float1, float2, float3, float4, float5, float6, float7, float8, int9, int10, float11, float12, float13);
        }
    }

    void releaseRenderedBuffer() {
        if (this.renderedBufferCount > 0 && --this.renderedBufferCount == 0) {
            this.clear();
        }
    }

    public void clear() {
        if (this.renderedBufferCount > 0) {
            LOGGER.warn("Clearing BufferBuilder with unused batches");
        }
        this.discard();
    }

    public void discard() {
        this.renderedBufferCount = 0;
        this.renderedBufferPointer = 0;
        this.nextElementByte = 0;
    }

    @Override
    public VertexFormatElement currentElement() {
        if (this.currentElement == null) {
            throw new IllegalStateException("BufferBuilder not started");
        } else {
            return this.currentElement;
        }
    }

    public boolean building() {
        return this.building;
    }

    ByteBuffer bufferSlice(int int0, int int1) {
        return MemoryUtil.memSlice(this.buffer, int0, int1 - int0);
    }

    public static record DrawState(VertexFormat f_85733_, int f_85734_, int f_166797_, VertexFormat.Mode f_85735_, VertexFormat.IndexType f_166798_, boolean f_166799_, boolean f_166800_) {

        private final VertexFormat format;

        private final int vertexCount;

        private final int indexCount;

        private final VertexFormat.Mode mode;

        private final VertexFormat.IndexType indexType;

        private final boolean indexOnly;

        private final boolean sequentialIndex;

        public DrawState(VertexFormat f_85733_, int f_85734_, int f_166797_, VertexFormat.Mode f_85735_, VertexFormat.IndexType f_166798_, boolean f_166799_, boolean f_166800_) {
            this.format = f_85733_;
            this.vertexCount = f_85734_;
            this.indexCount = f_166797_;
            this.mode = f_85735_;
            this.indexType = f_166798_;
            this.indexOnly = f_166799_;
            this.sequentialIndex = f_166800_;
        }

        public int vertexBufferSize() {
            return this.vertexCount * this.format.getVertexSize();
        }

        public int vertexBufferStart() {
            return 0;
        }

        public int vertexBufferEnd() {
            return this.vertexBufferSize();
        }

        public int indexBufferStart() {
            return this.indexOnly ? 0 : this.vertexBufferEnd();
        }

        public int indexBufferEnd() {
            return this.indexBufferStart() + this.indexBufferSize();
        }

        private int indexBufferSize() {
            return this.sequentialIndex ? 0 : this.indexCount * this.indexType.bytes;
        }

        public int bufferSize() {
            return this.indexBufferEnd();
        }
    }

    public class RenderedBuffer {

        private final int pointer;

        private final BufferBuilder.DrawState drawState;

        private boolean released;

        RenderedBuffer(int int0, BufferBuilder.DrawState bufferBuilderDrawState1) {
            this.pointer = int0;
            this.drawState = bufferBuilderDrawState1;
        }

        public ByteBuffer vertexBuffer() {
            int $$0 = this.pointer + this.drawState.vertexBufferStart();
            int $$1 = this.pointer + this.drawState.vertexBufferEnd();
            return BufferBuilder.this.bufferSlice($$0, $$1);
        }

        public ByteBuffer indexBuffer() {
            int $$0 = this.pointer + this.drawState.indexBufferStart();
            int $$1 = this.pointer + this.drawState.indexBufferEnd();
            return BufferBuilder.this.bufferSlice($$0, $$1);
        }

        public BufferBuilder.DrawState drawState() {
            return this.drawState;
        }

        public boolean isEmpty() {
            return this.drawState.vertexCount == 0;
        }

        public void release() {
            if (this.released) {
                throw new IllegalStateException("Buffer has already been released!");
            } else {
                BufferBuilder.this.releaseRenderedBuffer();
                this.released = true;
            }
        }
    }

    public static class SortState {

        final VertexFormat.Mode mode;

        final int vertices;

        @Nullable
        final Vector3f[] sortingPoints;

        @Nullable
        final VertexSorting sorting;

        SortState(VertexFormat.Mode vertexFormatMode0, int int1, @Nullable Vector3f[] vectorF2, @Nullable VertexSorting vertexSorting3) {
            this.mode = vertexFormatMode0;
            this.vertices = int1;
            this.sortingPoints = vectorF2;
            this.sorting = vertexSorting3;
        }
    }
}