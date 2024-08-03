package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.WaveModel;
import com.github.alexmodguy.alexscaves.server.entity.item.WaveEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

public class WaveRenderer extends EntityRenderer<WaveEntity> {

    private static final ResourceLocation TEXTURE_0 = new ResourceLocation("alexscaves", "textures/entity/deep_one/wave_0.png");

    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("alexscaves", "textures/entity/deep_one/wave_1.png");

    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("alexscaves", "textures/entity/deep_one/wave_2.png");

    private static final ResourceLocation TEXTURE_3 = new ResourceLocation("alexscaves", "textures/entity/deep_one/wave_3.png");

    private static final ResourceLocation OVERLAY_TEXTURE_0 = new ResourceLocation("alexscaves", "textures/entity/deep_one/wave_overlay_0.png");

    private static final ResourceLocation OVERLAY_TEXTURE_1 = new ResourceLocation("alexscaves", "textures/entity/deep_one/wave_overlay_1.png");

    private static final ResourceLocation OVERLAY_TEXTURE_2 = new ResourceLocation("alexscaves", "textures/entity/deep_one/wave_overlay_2.png");

    private static final ResourceLocation OVERLAY_TEXTURE_3 = new ResourceLocation("alexscaves", "textures/entity/deep_one/wave_overlay_3.png");

    private static final WaveModel MODEL = new WaveModel();

    public WaveRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(WaveEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (!entityIn.m_20145_()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0, 1.5, 0.0);
            matrixStackIn.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19859_, entityIn.getYRot()) + 180.0F));
            float ageInTicks = (float) entityIn.activeWaveTicks + partialTicks;
            float f = ageInTicks / 10.0F;
            matrixStackIn.translate(0.0, (double) (-0.1F + (1.0F - f) * -1.0F), -0.5);
            matrixStackIn.scale(1.0F, -(0.2F + f * 0.9F), 1.0F);
            MODEL.setupAnim(entityIn, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(this.getWaveTexture(entityIn.activeWaveTicks)));
            int waterColorAt = ((Biome) entityIn.m_9236_().m_204166_(entityIn.m_20183_()).get()).getWaterColor();
            float colorR = (float) (waterColorAt >> 16 & 0xFF) / 255.0F;
            float colorG = (float) (waterColorAt >> 8 & 0xFF) / 255.0F;
            float colorB = (float) (waterColorAt & 0xFF) / 255.0F;
            MODEL.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, colorR, colorG, colorB, 1.0F);
            VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(RenderType.entityTranslucent(this.getOverlayTexture(entityIn.activeWaveTicks)));
            MODEL.m_7695_(matrixStackIn, ivertexbuilder2, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        }
    }

    public ResourceLocation getTextureLocation(WaveEntity entity) {
        return this.getWaveTexture(entity.activeWaveTicks);
    }

    private ResourceLocation getWaveTexture(int tickCount) {
        int j = tickCount % 12 / 3;
        switch(j) {
            case 0:
                return TEXTURE_0;
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
            default:
                return TEXTURE_3;
        }
    }

    private ResourceLocation getOverlayTexture(int tickCount) {
        int j = tickCount % 12 / 3;
        switch(j) {
            case 0:
                return OVERLAY_TEXTURE_0;
            case 1:
                return OVERLAY_TEXTURE_1;
            case 2:
                return OVERLAY_TEXTURE_2;
            default:
                return OVERLAY_TEXTURE_3;
        }
    }
}