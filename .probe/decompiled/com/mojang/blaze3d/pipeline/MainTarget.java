package com.mojang.blaze3d.pipeline;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import java.util.Objects;

public class MainTarget extends RenderTarget {

    public static final int DEFAULT_WIDTH = 854;

    public static final int DEFAULT_HEIGHT = 480;

    static final MainTarget.Dimension DEFAULT_DIMENSIONS = new MainTarget.Dimension(854, 480);

    public MainTarget(int int0, int int1) {
        super(true);
        RenderSystem.assertOnRenderThreadOrInit();
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> this.createFrameBuffer(int0, int1));
        } else {
            this.createFrameBuffer(int0, int1);
        }
    }

    private void createFrameBuffer(int int0, int int1) {
        RenderSystem.assertOnRenderThreadOrInit();
        MainTarget.Dimension $$2 = this.allocateAttachments(int0, int1);
        this.f_83920_ = GlStateManager.glGenFramebuffers();
        GlStateManager._glBindFramebuffer(36160, this.f_83920_);
        GlStateManager._bindTexture(this.f_83923_);
        GlStateManager._texParameter(3553, 10241, 9728);
        GlStateManager._texParameter(3553, 10240, 9728);
        GlStateManager._texParameter(3553, 10242, 33071);
        GlStateManager._texParameter(3553, 10243, 33071);
        GlStateManager._glFramebufferTexture2D(36160, 36064, 3553, this.f_83923_, 0);
        GlStateManager._bindTexture(this.f_83924_);
        GlStateManager._texParameter(3553, 34892, 0);
        GlStateManager._texParameter(3553, 10241, 9728);
        GlStateManager._texParameter(3553, 10240, 9728);
        GlStateManager._texParameter(3553, 10242, 33071);
        GlStateManager._texParameter(3553, 10243, 33071);
        GlStateManager._glFramebufferTexture2D(36160, 36096, 3553, this.f_83924_, 0);
        GlStateManager._bindTexture(0);
        this.f_83917_ = $$2.width;
        this.f_83918_ = $$2.height;
        this.f_83915_ = $$2.width;
        this.f_83916_ = $$2.height;
        this.m_83949_();
        GlStateManager._glBindFramebuffer(36160, 0);
    }

    private MainTarget.Dimension allocateAttachments(int int0, int int1) {
        RenderSystem.assertOnRenderThreadOrInit();
        this.f_83923_ = TextureUtil.generateTextureId();
        this.f_83924_ = TextureUtil.generateTextureId();
        MainTarget.AttachmentState $$2 = MainTarget.AttachmentState.NONE;
        for (MainTarget.Dimension $$3 : MainTarget.Dimension.listWithFallback(int0, int1)) {
            $$2 = MainTarget.AttachmentState.NONE;
            if (this.allocateColorAttachment($$3)) {
                $$2 = $$2.with(MainTarget.AttachmentState.COLOR);
            }
            if (this.allocateDepthAttachment($$3)) {
                $$2 = $$2.with(MainTarget.AttachmentState.DEPTH);
            }
            if ($$2 == MainTarget.AttachmentState.COLOR_DEPTH) {
                return $$3;
            }
        }
        throw new RuntimeException("Unrecoverable GL_OUT_OF_MEMORY (allocated attachments = " + $$2.name() + ")");
    }

    private boolean allocateColorAttachment(MainTarget.Dimension mainTargetDimension0) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._getError();
        GlStateManager._bindTexture(this.f_83923_);
        GlStateManager._texImage2D(3553, 0, 32856, mainTargetDimension0.width, mainTargetDimension0.height, 0, 6408, 5121, null);
        return GlStateManager._getError() != 1285;
    }

    private boolean allocateDepthAttachment(MainTarget.Dimension mainTargetDimension0) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._getError();
        GlStateManager._bindTexture(this.f_83924_);
        GlStateManager._texImage2D(3553, 0, 6402, mainTargetDimension0.width, mainTargetDimension0.height, 0, 6402, 5126, null);
        return GlStateManager._getError() != 1285;
    }

    static enum AttachmentState {

        NONE, COLOR, DEPTH, COLOR_DEPTH;

        private static final MainTarget.AttachmentState[] VALUES = values();

        MainTarget.AttachmentState with(MainTarget.AttachmentState p_166164_) {
            return VALUES[this.ordinal() | p_166164_.ordinal()];
        }
    }

    static class Dimension {

        public final int width;

        public final int height;

        Dimension(int int0, int int1) {
            this.width = int0;
            this.height = int1;
        }

        static List<MainTarget.Dimension> listWithFallback(int int0, int int1) {
            RenderSystem.assertOnRenderThreadOrInit();
            int $$2 = RenderSystem.maxSupportedTextureSize();
            return int0 > 0 && int0 <= $$2 && int1 > 0 && int1 <= $$2 ? ImmutableList.of(new MainTarget.Dimension(int0, int1), MainTarget.DEFAULT_DIMENSIONS) : ImmutableList.of(MainTarget.DEFAULT_DIMENSIONS);
        }

        public boolean equals(Object object0) {
            if (this == object0) {
                return true;
            } else if (object0 != null && this.getClass() == object0.getClass()) {
                MainTarget.Dimension $$1 = (MainTarget.Dimension) object0;
                return this.width == $$1.width && this.height == $$1.height;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.width, this.height });
        }

        public String toString() {
            return this.width + "x" + this.height;
        }
    }
}