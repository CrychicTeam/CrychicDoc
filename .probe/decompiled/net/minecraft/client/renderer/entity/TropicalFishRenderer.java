package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.model.TropicalFishModelA;
import net.minecraft.client.model.TropicalFishModelB;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.TropicalFishPatternLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.TropicalFish;

public class TropicalFishRenderer extends MobRenderer<TropicalFish, ColorableHierarchicalModel<TropicalFish>> {

    private final ColorableHierarchicalModel<TropicalFish> modelA = (ColorableHierarchicalModel<TropicalFish>) this.m_7200_();

    private final ColorableHierarchicalModel<TropicalFish> modelB;

    private static final ResourceLocation MODEL_A_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_a.png");

    private static final ResourceLocation MODEL_B_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_b.png");

    public TropicalFishRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new TropicalFishModelA<>(entityRendererProviderContext0.bakeLayer(ModelLayers.TROPICAL_FISH_SMALL)), 0.15F);
        this.modelB = new TropicalFishModelB<>(entityRendererProviderContext0.bakeLayer(ModelLayers.TROPICAL_FISH_LARGE));
        this.m_115326_(new TropicalFishPatternLayer(this, entityRendererProviderContext0.getModelSet()));
    }

    public ResourceLocation getTextureLocation(TropicalFish tropicalFish0) {
        return switch(tropicalFish0.getVariant().base()) {
            case SMALL ->
                MODEL_A_TEXTURE;
            case LARGE ->
                MODEL_B_TEXTURE;
        };
    }

    public void render(TropicalFish tropicalFish0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        ColorableHierarchicalModel<TropicalFish> $$6 = switch(tropicalFish0.getVariant().base()) {
            case SMALL ->
                this.modelA;
            case LARGE ->
                this.modelB;
        };
        this.f_115290_ = $$6;
        float[] $$7 = tropicalFish0.getBaseColor().getTextureDiffuseColors();
        $$6.setColor($$7[0], $$7[1], $$7[2]);
        super.render(tropicalFish0, float1, float2, poseStack3, multiBufferSource4, int5);
        $$6.setColor(1.0F, 1.0F, 1.0F);
    }

    protected void setupRotations(TropicalFish tropicalFish0, PoseStack poseStack1, float float2, float float3, float float4) {
        super.m_7523_(tropicalFish0, poseStack1, float2, float3, float4);
        float $$5 = 4.3F * Mth.sin(0.6F * float2);
        poseStack1.mulPose(Axis.YP.rotationDegrees($$5));
        if (!tropicalFish0.m_20069_()) {
            poseStack1.translate(0.2F, 0.1F, 0.0F);
            poseStack1.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }
}