package com.github.alexthe666.citadel.mixin.client;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.client.event.EventGetOutlineColor;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event.Result;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LevelRenderer.class })
public class LevelRendererMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = { "Lnet/minecraft/client/renderer/LevelRenderer;initOutline()V" }, at = { @At("TAIL") })
    private void citadel_initOutline(CallbackInfo ci) {
        PostEffectRegistry.onInitializeOutline();
    }

    @Inject(method = { "Lnet/minecraft/client/renderer/LevelRenderer;resize(II)V" }, at = { @At("TAIL") })
    private void citadel_resize(int x, int y, CallbackInfo ci) {
        PostEffectRegistry.resize(x, y);
    }

    @Inject(method = { "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderBuffers;bufferSource()Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;", shift = Shift.BEFORE) })
    private void citadel_renderLevel_beforeEntities(PoseStack poseStack, float f, long l, boolean b, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        PostEffectRegistry.clearAndBindWrite(this.minecraft.getMainRenderTarget());
    }

    @Inject(method = { "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OutlineBufferSource;endOutlineBatch()V", shift = Shift.BEFORE) })
    private void citadel_renderLevel_process(PoseStack poseStack, float f, long l, boolean b, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        PostEffectRegistry.processEffects(this.minecraft.getMainRenderTarget(), f);
    }

    @Inject(method = { "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V" }, at = { @At("TAIL") })
    private void citadel_renderLevel_end(PoseStack poseStack, float f, long l, boolean b, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        PostEffectRegistry.blitEffects();
    }

    @Redirect(method = { "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V" }, remap = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getTeamColor()I"))
    private int citadel_getTeamColor(Entity entity) {
        EventGetOutlineColor event = new EventGetOutlineColor(entity, entity.getTeamColor());
        MinecraftForge.EVENT_BUS.post(event);
        int color = entity.getTeamColor();
        if (event.getResult() == Result.ALLOW) {
            color = event.getColor();
        }
        return color;
    }

    @Redirect(method = { "Lnet/minecraft/client/renderer/LevelRenderer;renderSky(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V" }, remap = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getTimeOfDay(F)F"), expect = 2)
    private float citadel_getTimeOfDay(ClientLevel instance, float partialTicks) {
        float lerpBy = Citadel.PROXY.isGamePaused() ? 0.0F : partialTicks;
        float lerpedDayTime = ((float) instance.m_6042_().fixedTime().orElse(instance.m_8044_()) + lerpBy) / 24000.0F;
        double d0 = Mth.frac((double) lerpedDayTime - 0.25);
        double d1 = 0.5 - Math.cos(d0 * Math.PI) / 2.0;
        return (float) (d0 * 2.0 + d1) / 3.0F;
    }
}