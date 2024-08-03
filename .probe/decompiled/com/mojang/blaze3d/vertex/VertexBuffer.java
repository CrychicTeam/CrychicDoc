package com.mojang.blaze3d.vertex;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import org.joml.Matrix4f;

public class VertexBuffer implements AutoCloseable {

    private final VertexBuffer.Usage usage;

    private int vertexBufferId;

    private int indexBufferId;

    private int arrayObjectId;

    @Nullable
    private VertexFormat format;

    @Nullable
    private RenderSystem.AutoStorageIndexBuffer sequentialIndices;

    private VertexFormat.IndexType indexType;

    private int indexCount;

    private VertexFormat.Mode mode;

    public VertexBuffer(VertexBuffer.Usage vertexBufferUsage0) {
        this.usage = vertexBufferUsage0;
        RenderSystem.assertOnRenderThread();
        this.vertexBufferId = GlStateManager._glGenBuffers();
        this.indexBufferId = GlStateManager._glGenBuffers();
        this.arrayObjectId = GlStateManager._glGenVertexArrays();
    }

    public void upload(BufferBuilder.RenderedBuffer bufferBuilderRenderedBuffer0) {
        if (!this.isInvalid()) {
            RenderSystem.assertOnRenderThread();
            try {
                BufferBuilder.DrawState $$1 = bufferBuilderRenderedBuffer0.drawState();
                this.format = this.uploadVertexBuffer($$1, bufferBuilderRenderedBuffer0.vertexBuffer());
                this.sequentialIndices = this.uploadIndexBuffer($$1, bufferBuilderRenderedBuffer0.indexBuffer());
                this.indexCount = $$1.indexCount();
                this.indexType = $$1.indexType();
                this.mode = $$1.mode();
            } finally {
                bufferBuilderRenderedBuffer0.release();
            }
        }
    }

    private VertexFormat uploadVertexBuffer(BufferBuilder.DrawState bufferBuilderDrawState0, ByteBuffer byteBuffer1) {
        boolean $$2 = false;
        if (!bufferBuilderDrawState0.format().equals(this.format)) {
            if (this.format != null) {
                this.format.clearBufferState();
            }
            GlStateManager._glBindBuffer(34962, this.vertexBufferId);
            bufferBuilderDrawState0.format().setupBufferState();
            $$2 = true;
        }
        if (!bufferBuilderDrawState0.indexOnly()) {
            if (!$$2) {
                GlStateManager._glBindBuffer(34962, this.vertexBufferId);
            }
            RenderSystem.glBufferData(34962, byteBuffer1, this.usage.id);
        }
        return bufferBuilderDrawState0.format();
    }

    @Nullable
    private RenderSystem.AutoStorageIndexBuffer uploadIndexBuffer(BufferBuilder.DrawState bufferBuilderDrawState0, ByteBuffer byteBuffer1) {
        if (!bufferBuilderDrawState0.sequentialIndex()) {
            GlStateManager._glBindBuffer(34963, this.indexBufferId);
            RenderSystem.glBufferData(34963, byteBuffer1, this.usage.id);
            return null;
        } else {
            RenderSystem.AutoStorageIndexBuffer $$2 = RenderSystem.getSequentialBuffer(bufferBuilderDrawState0.mode());
            if ($$2 != this.sequentialIndices || !$$2.hasStorage(bufferBuilderDrawState0.indexCount())) {
                $$2.bind(bufferBuilderDrawState0.indexCount());
            }
            return $$2;
        }
    }

    public void bind() {
        BufferUploader.invalidate();
        GlStateManager._glBindVertexArray(this.arrayObjectId);
    }

    public static void unbind() {
        BufferUploader.invalidate();
        GlStateManager._glBindVertexArray(0);
    }

    public void draw() {
        RenderSystem.drawElements(this.mode.asGLMode, this.indexCount, this.getIndexType().asGLType);
    }

    private VertexFormat.IndexType getIndexType() {
        RenderSystem.AutoStorageIndexBuffer $$0 = this.sequentialIndices;
        return $$0 != null ? $$0.type() : this.indexType;
    }

    public void drawWithShader(Matrix4f matrixF0, Matrix4f matrixF1, ShaderInstance shaderInstance2) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> this._drawWithShader(new Matrix4f(matrixF0), new Matrix4f(matrixF1), shaderInstance2));
        } else {
            this._drawWithShader(matrixF0, matrixF1, shaderInstance2);
        }
    }

    private void _drawWithShader(Matrix4f matrixF0, Matrix4f matrixF1, ShaderInstance shaderInstance2) {
        for (int $$3 = 0; $$3 < 12; $$3++) {
            int $$4 = RenderSystem.getShaderTexture($$3);
            shaderInstance2.setSampler("Sampler" + $$3, $$4);
        }
        if (shaderInstance2.MODEL_VIEW_MATRIX != null) {
            shaderInstance2.MODEL_VIEW_MATRIX.set(matrixF0);
        }
        if (shaderInstance2.PROJECTION_MATRIX != null) {
            shaderInstance2.PROJECTION_MATRIX.set(matrixF1);
        }
        if (shaderInstance2.INVERSE_VIEW_ROTATION_MATRIX != null) {
            shaderInstance2.INVERSE_VIEW_ROTATION_MATRIX.set(RenderSystem.getInverseViewRotationMatrix());
        }
        if (shaderInstance2.COLOR_MODULATOR != null) {
            shaderInstance2.COLOR_MODULATOR.set(RenderSystem.getShaderColor());
        }
        if (shaderInstance2.GLINT_ALPHA != null) {
            shaderInstance2.GLINT_ALPHA.set(RenderSystem.getShaderGlintAlpha());
        }
        if (shaderInstance2.FOG_START != null) {
            shaderInstance2.FOG_START.set(RenderSystem.getShaderFogStart());
        }
        if (shaderInstance2.FOG_END != null) {
            shaderInstance2.FOG_END.set(RenderSystem.getShaderFogEnd());
        }
        if (shaderInstance2.FOG_COLOR != null) {
            shaderInstance2.FOG_COLOR.set(RenderSystem.getShaderFogColor());
        }
        if (shaderInstance2.FOG_SHAPE != null) {
            shaderInstance2.FOG_SHAPE.set(RenderSystem.getShaderFogShape().getIndex());
        }
        if (shaderInstance2.TEXTURE_MATRIX != null) {
            shaderInstance2.TEXTURE_MATRIX.set(RenderSystem.getTextureMatrix());
        }
        if (shaderInstance2.GAME_TIME != null) {
            shaderInstance2.GAME_TIME.set(RenderSystem.getShaderGameTime());
        }
        if (shaderInstance2.SCREEN_SIZE != null) {
            Window $$5 = Minecraft.getInstance().getWindow();
            shaderInstance2.SCREEN_SIZE.set((float) $$5.getWidth(), (float) $$5.getHeight());
        }
        if (shaderInstance2.LINE_WIDTH != null && (this.mode == VertexFormat.Mode.LINES || this.mode == VertexFormat.Mode.LINE_STRIP)) {
            shaderInstance2.LINE_WIDTH.set(RenderSystem.getShaderLineWidth());
        }
        RenderSystem.setupShaderLights(shaderInstance2);
        shaderInstance2.apply();
        this.draw();
        shaderInstance2.clear();
    }

    public void close() {
        if (this.vertexBufferId >= 0) {
            RenderSystem.glDeleteBuffers(this.vertexBufferId);
            this.vertexBufferId = -1;
        }
        if (this.indexBufferId >= 0) {
            RenderSystem.glDeleteBuffers(this.indexBufferId);
            this.indexBufferId = -1;
        }
        if (this.arrayObjectId >= 0) {
            RenderSystem.glDeleteVertexArrays(this.arrayObjectId);
            this.arrayObjectId = -1;
        }
    }

    public VertexFormat getFormat() {
        return this.format;
    }

    public boolean isInvalid() {
        return this.arrayObjectId == -1;
    }

    public static enum Usage {

        STATIC(35044), DYNAMIC(35048);

        final int id;

        private Usage(int p_286680_) {
            this.id = p_286680_;
        }
    }
}