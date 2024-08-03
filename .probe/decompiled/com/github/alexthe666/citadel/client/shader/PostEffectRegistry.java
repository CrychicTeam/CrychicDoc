package com.github.alexthe666.citadel.client.shader;

import com.github.alexthe666.citadel.Citadel;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;

public class PostEffectRegistry {

    private static List<ResourceLocation> registry = new ArrayList();

    private static Map<ResourceLocation, PostEffectRegistry.PostEffect> postEffects = new HashMap();

    public static void clear() {
        for (PostEffectRegistry.PostEffect postEffect : postEffects.values()) {
            postEffect.close();
        }
        postEffects.clear();
    }

    public static void registerEffect(ResourceLocation resourceLocation) {
        registry.add(resourceLocation);
    }

    public static void onInitializeOutline() {
        clear();
        Minecraft minecraft = Minecraft.getInstance();
        for (ResourceLocation resourceLocation : registry) {
            PostChain postChain;
            RenderTarget renderTarget;
            try {
                postChain = new PostChain(minecraft.getTextureManager(), minecraft.getResourceManager(), minecraft.getMainRenderTarget(), resourceLocation);
                postChain.resize(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
                renderTarget = postChain.getTempTarget("final");
            } catch (IOException var6) {
                Citadel.LOGGER.warn("Failed to load shader: {}", resourceLocation, var6);
                postChain = null;
                renderTarget = null;
            } catch (JsonSyntaxException var7) {
                Citadel.LOGGER.warn("Failed to parse shader: {}", resourceLocation, var7);
                postChain = null;
                renderTarget = null;
            }
            postEffects.put(resourceLocation, new PostEffectRegistry.PostEffect(postChain, renderTarget, false));
        }
    }

    public static void resize(int x, int y) {
        for (PostEffectRegistry.PostEffect postEffect : postEffects.values()) {
            postEffect.resize(x, y);
        }
    }

    public static RenderTarget getRenderTargetFor(ResourceLocation resourceLocation) {
        PostEffectRegistry.PostEffect effect = (PostEffectRegistry.PostEffect) postEffects.get(resourceLocation);
        return effect == null ? null : effect.getRenderTarget();
    }

    public static void renderEffectForNextTick(ResourceLocation resourceLocation) {
        PostEffectRegistry.PostEffect effect = (PostEffectRegistry.PostEffect) postEffects.get(resourceLocation);
        if (effect != null) {
            effect.setEnabled(true);
        }
    }

    public static void blitEffects() {
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        for (PostEffectRegistry.PostEffect postEffect : postEffects.values()) {
            if (postEffect.postChain != null && postEffect.isEnabled()) {
                postEffect.getRenderTarget().blitToScreen(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight(), false);
                postEffect.getRenderTarget().clear(Minecraft.ON_OSX);
                Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
                postEffect.setEnabled(false);
            }
        }
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public static void clearAndBindWrite(RenderTarget mainTarget) {
        for (PostEffectRegistry.PostEffect postEffect : postEffects.values()) {
            if (postEffect.isEnabled() && postEffect.postChain != null) {
                postEffect.getRenderTarget().clear(Minecraft.ON_OSX);
                mainTarget.bindWrite(false);
            }
        }
    }

    public static void processEffects(RenderTarget mainTarget, float f) {
        for (PostEffectRegistry.PostEffect postEffect : postEffects.values()) {
            if (postEffect.isEnabled() && postEffect.postChain != null) {
                postEffect.postChain.process(Minecraft.getInstance().getFrameTime());
                mainTarget.bindWrite(false);
            }
        }
    }

    private static class PostEffect {

        private PostChain postChain;

        private RenderTarget renderTarget;

        private boolean enabled;

        public PostEffect(PostChain postChain, RenderTarget renderTarget, boolean enabled) {
            this.postChain = postChain;
            this.renderTarget = renderTarget;
            this.enabled = enabled;
        }

        public PostChain getPostChain() {
            return this.postChain;
        }

        public RenderTarget getRenderTarget() {
            return this.renderTarget;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void close() {
            if (this.postChain != null) {
                this.postChain.close();
            }
        }

        public void resize(int x, int y) {
            if (this.postChain != null) {
                this.postChain.resize(x, y);
            }
        }
    }
}