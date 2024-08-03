package net.liopyu.animationjs.events.subevents.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.liopyu.animationjs.events.PlayerRenderer;
import net.liopyu.animationjs.network.NetworkHandler;
import net.liopyu.animationjs.network.packet.AnimationStateUpdatePacket;
import net.liopyu.animationjs.utils.AnimationJSHelperClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = "animationjs", value = { Dist.CLIENT }, bus = Bus.FORGE)
public class ClientEventHandlers {

    public static final Map<UUID, PlayerRenderer> thisRenderList = new HashMap();

    private static final ResourceLocation VISION_OVERLAY_TEXTURE = new ResourceLocation("minecraft:textures/misc/pumpkinblur.png");

    @SubscribeEvent
    public static void onClientTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof AbstractClientPlayer clientPlayer) {
            ModifierLayer<IAnimation> anim = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer).get(new ResourceLocation("liosplayeranimatorapi", "factory"));
            if (anim == null) {
                return;
            }
            boolean isAnimationActive = anim.isActive();
            AnimationStateUpdatePacket packet = new AnimationStateUpdatePacket(clientPlayer.m_20148_(), isAnimationActive);
            NetworkHandler.sendToServer(packet);
        }
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new ResourceLocation("animationjs", "animation"), 42, ClientEventHandlers::registerPlayerAnimation);
    }

    private static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
        return new ModifierLayer();
    }

    private static void renderVisionOverlay(Player player, PoseStack poseStack, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderTexture(0, VISION_OVERLAY_TEXTURE);
        RenderSystem.setShader(GameRenderer::m_172817_);
        float scale = 0.5F;
        int width = 10;
        int height = 10;
        float yaw = player.f_19859_ + (player.f_19857_ - player.f_19859_) * partialTicks;
        float pitch = player.f_19860_ + (player.f_19858_ - player.f_19860_) * partialTicks;
        double lookX = -Math.sin(Math.toRadians((double) yaw)) * Math.cos(Math.toRadians((double) pitch));
        double lookY = -Math.sin(Math.toRadians((double) pitch));
        double lookZ = Math.cos(Math.toRadians((double) yaw)) * Math.cos(Math.toRadians((double) pitch));
        float effectiveDistance = 1.0F / (float) Math.cos(Math.toRadians((double) pitch));
        poseStack.pushPose();
        poseStack.translate(lookX, lookY, lookZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.scale(scale, scale, scale);
        float x = (float) (-width) / 2.0F;
        float y = (float) (-height) / 2.0F;
        float z = 0.0F;
        innerBlit(poseStack, VISION_OVERLAY_TEXTURE, AnimationJSHelperClass.convertToInteger(x), AnimationJSHelperClass.convertToInteger(x + (float) width), AnimationJSHelperClass.convertToInteger(y), AnimationJSHelperClass.convertToInteger(y + (float) height), AnimationJSHelperClass.convertToInteger(z), 0.0F, 1.0F, 0.0F, 1.0F);
        poseStack.popPose();
        RenderSystem.enableDepthTest();
    }

    static void innerBlit(PoseStack pose, ResourceLocation pAtlasLocation, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV) {
        Matrix4f matrix4f = pose.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.m_252986_(matrix4f, (float) pX1, (float) pY1, (float) pBlitOffset).uv(pMinU, pMinV).endVertex();
        bufferbuilder.m_252986_(matrix4f, (float) pX1, (float) pY2, (float) pBlitOffset).uv(pMinU, pMaxV).endVertex();
        bufferbuilder.m_252986_(matrix4f, (float) pX2, (float) pY2, (float) pBlitOffset).uv(pMaxU, pMaxV).endVertex();
        bufferbuilder.m_252986_(matrix4f, (float) pX2, (float) pY1, (float) pBlitOffset).uv(pMaxU, pMinV).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }
}