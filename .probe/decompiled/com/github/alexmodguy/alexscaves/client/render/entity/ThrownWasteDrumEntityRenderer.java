package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.ThrownWasteDrumEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;

public class ThrownWasteDrumEntityRenderer extends EntityRenderer<ThrownWasteDrumEntity> {

    public ThrownWasteDrumEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.f_114477_ = 0.0F;
    }

    public void render(ThrownWasteDrumEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource source, int lightIn) {
        super.render(entity, entityYaw, partialTicks, poseStack, source, lightIn);
        float ageInTicks = (float) entity.f_19797_ + partialTicks;
        float progress = ((float) entity.getOnGroundFor() + partialTicks) / 20.0F;
        if (!(progress > 1.0F)) {
            float expandScale = 1.0F + (float) Math.sin((double) (progress * progress) * Math.PI) * 0.5F;
            poseStack.pushPose();
            poseStack.scale(1.0F, 1.0F - progress * 0.03F, 1.0F);
            poseStack.pushPose();
            poseStack.scale(expandScale, expandScale - progress * 0.3F, expandScale);
            poseStack.translate(0.0, 0.5, 0.0);
            if (entity.m_20096_()) {
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            } else {
                poseStack.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTicks, entity.f_19859_, entity.m_146908_()) - 90.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(ageInTicks * 25.0F));
            }
            poseStack.translate(-0.5, -0.5, -0.5);
            BlockState state = ACBlockRegistry.WASTE_DRUM.get().defaultBlockState();
            BakedModel bakedmodel = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
            float f = 1.0F - progress * 0.5F;
            float f1 = 1.0F + progress;
            float f2 = 1.0F - progress;
            for (RenderType rt : bakedmodel.getRenderTypes(state, RandomSource.create(42L), ModelData.EMPTY)) {
                NuclearBombRenderer.renderModel(poseStack.last(), source.getBuffer(RenderTypeHelper.getEntityRenderType(rt, false)), state, bakedmodel, f, f1, f2, lightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, rt);
            }
            poseStack.popPose();
            poseStack.popPose();
        }
    }

    public ResourceLocation getTextureLocation(ThrownWasteDrumEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}