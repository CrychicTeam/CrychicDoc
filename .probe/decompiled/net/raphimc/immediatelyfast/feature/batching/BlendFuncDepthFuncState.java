package net.raphimc.immediatelyfast.feature.batching;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Stack;

public record BlendFuncDepthFuncState(boolean BLEND, boolean DEPTH_TEST, int GL_BLEND_SRC_RGB, int GL_BLEND_SRC_ALPHA, int GL_BLEND_DST_RGB, int GL_BLEND_DST_ALPHA, int GL_DEPTH_FUNC) {

    private static final Stack<BlendFuncDepthFuncState> STACK = new Stack();

    public static BlendFuncDepthFuncState current() {
        return new BlendFuncDepthFuncState(GlStateManager.BLEND.mode.enabled, GlStateManager.DEPTH.mode.enabled, GlStateManager.BLEND.srcRgb, GlStateManager.BLEND.srcAlpha, GlStateManager.BLEND.dstRgb, GlStateManager.BLEND.dstAlpha, GlStateManager.DEPTH.func);
    }

    public void saveAndApply() {
        STACK.push(current());
        this.apply();
    }

    public void revert() {
        ((BlendFuncDepthFuncState) STACK.pop()).apply();
    }

    public void apply() {
        if (this.BLEND) {
            RenderSystem.enableBlend();
        } else {
            RenderSystem.disableBlend();
        }
        RenderSystem.blendFuncSeparate(this.GL_BLEND_SRC_RGB, this.GL_BLEND_DST_RGB, this.GL_BLEND_SRC_ALPHA, this.GL_BLEND_DST_ALPHA);
        if (this.DEPTH_TEST) {
            RenderSystem.enableDepthTest();
        } else {
            RenderSystem.disableDepthTest();
        }
        RenderSystem.depthFunc(this.GL_DEPTH_FUNC);
    }
}