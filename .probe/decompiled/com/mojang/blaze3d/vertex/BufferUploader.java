package com.mojang.blaze3d.vertex;

import com.mojang.blaze3d.systems.RenderSystem;
import javax.annotation.Nullable;

public class BufferUploader {

    @Nullable
    private static VertexBuffer lastImmediateBuffer;

    public static void reset() {
        if (lastImmediateBuffer != null) {
            invalidate();
            VertexBuffer.unbind();
        }
    }

    public static void invalidate() {
        lastImmediateBuffer = null;
    }

    public static void drawWithShader(BufferBuilder.RenderedBuffer bufferBuilderRenderedBuffer0) {
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(() -> _drawWithShader(bufferBuilderRenderedBuffer0));
        } else {
            _drawWithShader(bufferBuilderRenderedBuffer0);
        }
    }

    private static void _drawWithShader(BufferBuilder.RenderedBuffer bufferBuilderRenderedBuffer0) {
        VertexBuffer $$1 = upload(bufferBuilderRenderedBuffer0);
        if ($$1 != null) {
            $$1.drawWithShader(RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix(), RenderSystem.getShader());
        }
    }

    public static void draw(BufferBuilder.RenderedBuffer bufferBuilderRenderedBuffer0) {
        VertexBuffer $$1 = upload(bufferBuilderRenderedBuffer0);
        if ($$1 != null) {
            $$1.draw();
        }
    }

    @Nullable
    private static VertexBuffer upload(BufferBuilder.RenderedBuffer bufferBuilderRenderedBuffer0) {
        RenderSystem.assertOnRenderThread();
        if (bufferBuilderRenderedBuffer0.isEmpty()) {
            bufferBuilderRenderedBuffer0.release();
            return null;
        } else {
            VertexBuffer $$1 = bindImmediateBuffer(bufferBuilderRenderedBuffer0.drawState().format());
            $$1.upload(bufferBuilderRenderedBuffer0);
            return $$1;
        }
    }

    private static VertexBuffer bindImmediateBuffer(VertexFormat vertexFormat0) {
        VertexBuffer $$1 = vertexFormat0.getImmediateDrawVertexBuffer();
        bindImmediateBuffer($$1);
        return $$1;
    }

    private static void bindImmediateBuffer(VertexBuffer vertexBuffer0) {
        if (vertexBuffer0 != lastImmediateBuffer) {
            vertexBuffer0.bind();
            lastImmediateBuffer = vertexBuffer0;
        }
    }
}