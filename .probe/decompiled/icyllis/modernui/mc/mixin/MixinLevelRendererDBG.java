package icyllis.modernui.mc.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import icyllis.modernui.ModernUI;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LevelRenderer.class })
public class MixinLevelRendererDBG {

    @Inject(method = { "renderLevel" }, at = { @At(value = "CONSTANT", args = { "stringValue=blockentities" }, ordinal = 0) })
    private void afterEntities(PoseStack poseStack, float partialTicks, long frameTimeNanos, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projection, CallbackInfo ci) {
        if (Screen.hasAltDown() && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 327)) {
            ModernUI.LOGGER.info("Capture from MixinLevelRendererDBG.afterEntities()");
            ModernUI.LOGGER.info("Param PoseStack.last().pose(): {}", poseStack.last().pose());
            ModernUI.LOGGER.info("Param Camera.getPosition(): {}, pitch: {}, yaw: {}, rot: {}, detached: {}", camera.getPosition(), camera.getXRot(), camera.getYRot(), camera.rotation(), camera.isDetached());
            ModernUI.LOGGER.info("Param ProjectionMatrix: {}", projection);
            ModernUI.LOGGER.info("RenderSystem.getModelViewStack().last().pose(): {}", RenderSystem.getModelViewStack().last().pose());
            ModernUI.LOGGER.info("RenderSystem.getModelViewMatrix(): {}", RenderSystem.getModelViewMatrix());
            ModernUI.LOGGER.info("RenderSystem.getInverseViewRotationMatrix: {}", RenderSystem.getInverseViewRotationMatrix());
            ModernUI.LOGGER.info("GameRenderer.getMainCamera().getPosition(): {}, pitch: {}, yaw: {}, rot: {}, detached: {}", Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), camera.getXRot(), camera.getYRot(), camera.rotation(), camera.isDetached());
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                ModernUI.LOGGER.info("LocalPlayer: yaw: {}, yawHead: {}, eyePos: {}", player.m_146908_(), player.m_6080_(), player.m_20299_(partialTicks));
            }
            Entity cameraEntity = Minecraft.getInstance().cameraEntity;
            if (cameraEntity != null) {
                ModernUI.LOGGER.info("CameraEntity position: {}", cameraEntity.position());
            }
        }
    }
}