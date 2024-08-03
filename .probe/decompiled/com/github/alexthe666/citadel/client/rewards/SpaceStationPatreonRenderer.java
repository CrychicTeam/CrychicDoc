package com.github.alexthe666.citadel.client.rewards;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.ClientProxy;
import com.github.alexthe666.citadel.client.shader.CitadelShaderRenderTypes;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.github.alexthe666.citadel.client.texture.CitadelTextureManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class SpaceStationPatreonRenderer extends CitadelPatreonRenderer {

    private static final ResourceLocation CITADEL_TEXTURE = new ResourceLocation("citadel", "textures/patreon/citadel_model.png");

    private static final ResourceLocation CITADEL_LIGHTS_TEXTURE = new ResourceLocation("citadel", "textures/patreon/citadel_model_glow.png");

    private final ResourceLocation resourceLocation;

    private int[] colors;

    public SpaceStationPatreonRenderer(ResourceLocation resourceLocation, int[] colors) {
        this.resourceLocation = resourceLocation;
        this.colors = colors;
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource buffer, int light, float partialTick, LivingEntity entity, float distanceIn, float rotateSpeed, float rotateHeight) {
        float tick = (float) entity.f_19797_ + partialTick;
        float bob = (float) (Math.sin((double) (tick * 0.1F)) * 1.0 * 0.05F - 0.05F);
        float scale = 0.4F;
        float rotation = Mth.wrapDegrees(tick * rotateSpeed % 360.0F);
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation));
        matrixStackIn.translate(0.0F, entity.m_20206_() + bob + (rotateHeight - 1.0F), entity.m_20205_() * distanceIn);
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(75.0F));
        matrixStackIn.scale(scale, scale, scale);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation * 10.0F));
        ClientProxy.CITADEL_MODEL.resetToDefaultPose();
        if (CitadelConstants.debugShaders()) {
            PostEffectRegistry.renderEffectForNextTick(ClientProxy.RAINBOW_AURA_POST_SHADER);
            ClientProxy.CITADEL_MODEL.m_7695_(matrixStackIn, buffer.getBuffer(CitadelShaderRenderTypes.getRainbowAura(CITADEL_TEXTURE)), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            ClientProxy.CITADEL_MODEL.m_7695_(matrixStackIn, buffer.getBuffer(RenderType.entityCutoutNoCull(CitadelTextureManager.getColorMappedTexture(this.resourceLocation, CITADEL_TEXTURE, this.colors))), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            ClientProxy.CITADEL_MODEL.m_7695_(matrixStackIn, buffer.getBuffer(RenderType.eyes(CITADEL_LIGHTS_TEXTURE)), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }
}