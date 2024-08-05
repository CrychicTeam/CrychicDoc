package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PufferfishBigModel;
import net.minecraft.client.model.PufferfishMidModel;
import net.minecraft.client.model.PufferfishSmallModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Pufferfish;

public class PufferfishRenderer extends MobRenderer<Pufferfish, EntityModel<Pufferfish>> {

    private static final ResourceLocation PUFFER_LOCATION = new ResourceLocation("textures/entity/fish/pufferfish.png");

    private int puffStateO = 3;

    private final EntityModel<Pufferfish> small;

    private final EntityModel<Pufferfish> mid;

    private final EntityModel<Pufferfish> big = this.m_7200_();

    public PufferfishRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new PufferfishBigModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.PUFFERFISH_BIG)), 0.2F);
        this.mid = new PufferfishMidModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.PUFFERFISH_MEDIUM));
        this.small = new PufferfishSmallModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.PUFFERFISH_SMALL));
    }

    public ResourceLocation getTextureLocation(Pufferfish pufferfish0) {
        return PUFFER_LOCATION;
    }

    public void render(Pufferfish pufferfish0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        int $$6 = pufferfish0.getPuffState();
        if ($$6 != this.puffStateO) {
            if ($$6 == 0) {
                this.f_115290_ = this.small;
            } else if ($$6 == 1) {
                this.f_115290_ = this.mid;
            } else {
                this.f_115290_ = this.big;
            }
        }
        this.puffStateO = $$6;
        this.f_114477_ = 0.1F + 0.1F * (float) $$6;
        super.render(pufferfish0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    protected void setupRotations(Pufferfish pufferfish0, PoseStack poseStack1, float float2, float float3, float float4) {
        poseStack1.translate(0.0F, Mth.cos(float2 * 0.05F) * 0.08F, 0.0F);
        super.m_7523_(pufferfish0, poseStack1, float2, float3, float4);
    }
}