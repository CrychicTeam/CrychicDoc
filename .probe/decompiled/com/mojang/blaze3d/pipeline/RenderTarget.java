package com.mojang.blaze3d.pipeline;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import org.joml.Matrix4f;

public abstract class RenderTarget {

    private static final int RED_CHANNEL = 0;

    private static final int GREEN_CHANNEL = 1;

    private static final int BLUE_CHANNEL = 2;

    private static final int ALPHA_CHANNEL = 3;

    public int width;

    public int height;

    public int viewWidth;

    public int viewHeight;

    public final boolean useDepth;

    public int frameBufferId;

    protected int colorTextureId;

    protected int depthBufferId;

    private final float[] clearChannels = Util.make(() -> new float[] { 1.0F, 1.0F, 1.0F, 0.0F });

    public int filterMode;

    public RenderTarget(boolean boolean0) {
        this.useDepth = boolean0;
        this.frameBufferId = -1;
        this.colorTextureId = -1;
        this.depthBufferId = -1;
    }

    public void resize(int int0, int int1, boolean boolean2) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> this._resize(int0, int1, boolean2));
        } else {
            this._resize(int0, int1, boolean2);
        }
    }

    private void _resize(int int0, int int1, boolean boolean2) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._enableDepthTest();
        if (this.frameBufferId >= 0) {
            this.destroyBuffers();
        }
        this.createBuffers(int0, int1, boolean2);
        GlStateManager._glBindFramebuffer(36160, 0);
    }

    public void destroyBuffers() {
        RenderSystem.assertOnRenderThreadOrInit();
        this.unbindRead();
        this.unbindWrite();
        if (this.depthBufferId > -1) {
            TextureUtil.releaseTextureId(this.depthBufferId);
            this.depthBufferId = -1;
        }
        if (this.colorTextureId > -1) {
            TextureUtil.releaseTextureId(this.colorTextureId);
            this.colorTextureId = -1;
        }
        if (this.frameBufferId > -1) {
            GlStateManager._glBindFramebuffer(36160, 0);
            GlStateManager._glDeleteFramebuffers(this.frameBufferId);
            this.frameBufferId = -1;
        }
    }

    public void copyDepthFrom(RenderTarget renderTarget0) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._glBindFramebuffer(36008, renderTarget0.frameBufferId);
        GlStateManager._glBindFramebuffer(36009, this.frameBufferId);
        GlStateManager._glBlitFrameBuffer(0, 0, renderTarget0.width, renderTarget0.height, 0, 0, this.width, this.height, 256, 9728);
        GlStateManager._glBindFramebuffer(36160, 0);
    }

    public void createBuffers(int int0, int int1, boolean boolean2) {
        RenderSystem.assertOnRenderThreadOrInit();
        int $$3 = RenderSystem.maxSupportedTextureSize();
        if (int0 > 0 && int0 <= $$3 && int1 > 0 && int1 <= $$3) {
            this.viewWidth = int0;
            this.viewHeight = int1;
            this.width = int0;
            this.height = int1;
            this.frameBufferId = GlStateManager.glGenFramebuffers();
            this.colorTextureId = TextureUtil.generateTextureId();
            if (this.useDepth) {
                this.depthBufferId = TextureUtil.generateTextureId();
                GlStateManager._bindTexture(this.depthBufferId);
                GlStateManager._texParameter(3553, 10241, 9728);
                GlStateManager._texParameter(3553, 10240, 9728);
                GlStateManager._texParameter(3553, 34892, 0);
                GlStateManager._texParameter(3553, 10242, 33071);
                GlStateManager._texParameter(3553, 10243, 33071);
                GlStateManager._texImage2D(3553, 0, 6402, this.width, this.height, 0, 6402, 5126, null);
            }
            this.setFilterMode(9728);
            GlStateManager._bindTexture(this.colorTextureId);
            GlStateManager._texParameter(3553, 10242, 33071);
            GlStateManager._texParameter(3553, 10243, 33071);
            GlStateManager._texImage2D(3553, 0, 32856, this.width, this.height, 0, 6408, 5121, null);
            GlStateManager._glBindFramebuffer(36160, this.frameBufferId);
            GlStateManager._glFramebufferTexture2D(36160, 36064, 3553, this.colorTextureId, 0);
            if (this.useDepth) {
                GlStateManager._glFramebufferTexture2D(36160, 36096, 3553, this.depthBufferId, 0);
            }
            this.checkStatus();
            this.clear(boolean2);
            this.unbindRead();
        } else {
            throw new IllegalArgumentException("Window " + int0 + "x" + int1 + " size out of bounds (max. size: " + $$3 + ")");
        }
    }

    public void setFilterMode(int int0) {
        RenderSystem.assertOnRenderThreadOrInit();
        this.filterMode = int0;
        GlStateManager._bindTexture(this.colorTextureId);
        GlStateManager._texParameter(3553, 10241, int0);
        GlStateManager._texParameter(3553, 10240, int0);
        GlStateManager._bindTexture(0);
    }

    public void checkStatus() {
        RenderSystem.assertOnRenderThreadOrInit();
        int $$0 = GlStateManager.glCheckFramebufferStatus(36160);
        if ($$0 != 36053) {
            if ($$0 == 36054) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
            } else if ($$0 == 36055) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
            } else if ($$0 == 36059) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
            } else if ($$0 == 36060) {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
            } else if ($$0 == 36061) {
                throw new RuntimeException("GL_FRAMEBUFFER_UNSUPPORTED");
            } else if ($$0 == 1285) {
                throw new RuntimeException("GL_OUT_OF_MEMORY");
            } else {
                throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + $$0);
            }
        }
    }

    public void bindRead() {
        RenderSystem.assertOnRenderThread();
        GlStateManager._bindTexture(this.colorTextureId);
    }

    public void unbindRead() {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._bindTexture(0);
    }

    public void bindWrite(boolean boolean0) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> this._bindWrite(boolean0));
        } else {
            this._bindWrite(boolean0);
        }
    }

    private void _bindWrite(boolean boolean0) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._glBindFramebuffer(36160, this.frameBufferId);
        if (boolean0) {
            GlStateManager._viewport(0, 0, this.viewWidth, this.viewHeight);
        }
    }

    public void unbindWrite() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> GlStateManager._glBindFramebuffer(36160, 0));
        } else {
            GlStateManager._glBindFramebuffer(36160, 0);
        }
    }

    public void setClearColor(float float0, float float1, float float2, float float3) {
        this.clearChannels[0] = float0;
        this.clearChannels[1] = float1;
        this.clearChannels[2] = float2;
        this.clearChannels[3] = float3;
    }

    public void blitToScreen(int int0, int int1) {
        this.blitToScreen(int0, int1, true);
    }

    public void blitToScreen(int int0, int int1, boolean boolean2) {
        RenderSystem.assertOnGameThreadOrInit();
        if (!RenderSystem.isInInitPhase()) {
            RenderSystem.recordRenderCall(() -> this._blitToScreen(int0, int1, boolean2));
        } else {
            this._blitToScreen(int0, int1, boolean2);
        }
    }

    private void _blitToScreen(int int0, int int1, boolean boolean2) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._colorMask(true, true, true, false);
        GlStateManager._disableDepthTest();
        GlStateManager._depthMask(false);
        GlStateManager._viewport(0, 0, int0, int1);
        if (boolean2) {
            GlStateManager._disableBlend();
        }
        Minecraft $$3 = Minecraft.getInstance();
        ShaderInstance $$4 = $$3.gameRenderer.blitShader;
        $$4.setSampler("DiffuseSampler", this.colorTextureId);
        Matrix4f $$5 = new Matrix4f().setOrtho(0.0F, (float) int0, (float) int1, 0.0F, 1000.0F, 3000.0F);
        RenderSystem.setProjectionMatrix($$5, VertexSorting.ORTHOGRAPHIC_Z);
        if ($$4.MODEL_VIEW_MATRIX != null) {
            $$4.MODEL_VIEW_MATRIX.set(new Matrix4f().translation(0.0F, 0.0F, -2000.0F));
        }
        if ($$4.PROJECTION_MATRIX != null) {
            $$4.PROJECTION_MATRIX.set($$5);
        }
        $$4.apply();
        float $$6 = (float) int0;
        float $$7 = (float) int1;
        float $$8 = (float) this.viewWidth / (float) this.width;
        float $$9 = (float) this.viewHeight / (float) this.height;
        Tesselator $$10 = RenderSystem.renderThreadTesselator();
        BufferBuilder $$11 = $$10.getBuilder();
        $$11.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        $$11.m_5483_(0.0, (double) $$7, 0.0).uv(0.0F, 0.0F).color(255, 255, 255, 255).endVertex();
        $$11.m_5483_((double) $$6, (double) $$7, 0.0).uv($$8, 0.0F).color(255, 255, 255, 255).endVertex();
        $$11.m_5483_((double) $$6, 0.0, 0.0).uv($$8, $$9).color(255, 255, 255, 255).endVertex();
        $$11.m_5483_(0.0, 0.0, 0.0).uv(0.0F, $$9).color(255, 255, 255, 255).endVertex();
        BufferUploader.draw($$11.end());
        $$4.clear();
        GlStateManager._depthMask(true);
        GlStateManager._colorMask(true, true, true, true);
    }

    public void clear(boolean boolean0) {
        RenderSystem.assertOnRenderThreadOrInit();
        this.bindWrite(true);
        GlStateManager._clearColor(this.clearChannels[0], this.clearChannels[1], this.clearChannels[2], this.clearChannels[3]);
        int $$1 = 16384;
        if (this.useDepth) {
            GlStateManager._clearDepth(1.0);
            $$1 |= 256;
        }
        GlStateManager._clear($$1, boolean0);
        this.unbindWrite();
    }

    public int getColorTextureId() {
        return this.colorTextureId;
    }

    public int getDepthTextureId() {
        return this.depthBufferId;
    }
}