package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.MagmaCube;

public class MagmaCubeRenderer extends MobRenderer<MagmaCube, LavaSlimeModel<MagmaCube>> {

    private static final ResourceLocation MAGMACUBE_LOCATION = new ResourceLocation("textures/entity/slime/magmacube.png");

    public MagmaCubeRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new LavaSlimeModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.MAGMA_CUBE)), 0.25F);
    }

    protected int getBlockLightLevel(MagmaCube magmaCube0, BlockPos blockPos1) {
        return 15;
    }

    public ResourceLocation getTextureLocation(MagmaCube magmaCube0) {
        return MAGMACUBE_LOCATION;
    }

    public void render(MagmaCube magmaCube0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        this.f_114477_ = 0.25F * (float) magmaCube0.m_33632_();
        super.render(magmaCube0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    protected void scale(MagmaCube magmaCube0, PoseStack poseStack1, float float2) {
        int $$3 = magmaCube0.m_33632_();
        float $$4 = Mth.lerp(float2, magmaCube0.f_33585_, magmaCube0.f_33584_) / ((float) $$3 * 0.5F + 1.0F);
        float $$5 = 1.0F / ($$4 + 1.0F);
        poseStack1.scale($$5 * (float) $$3, 1.0F / $$5 * (float) $$3, $$5 * (float) $$3);
    }
}