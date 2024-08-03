package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.SalmonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Salmon;

public class SalmonRenderer extends MobRenderer<Salmon, SalmonModel<Salmon>> {

    private static final ResourceLocation SALMON_LOCATION = new ResourceLocation("textures/entity/fish/salmon.png");

    public SalmonRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new SalmonModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.SALMON)), 0.4F);
    }

    public ResourceLocation getTextureLocation(Salmon salmon0) {
        return SALMON_LOCATION;
    }

    protected void setupRotations(Salmon salmon0, PoseStack poseStack1, float float2, float float3, float float4) {
        super.m_7523_(salmon0, poseStack1, float2, float3, float4);
        float $$5 = 1.0F;
        float $$6 = 1.0F;
        if (!salmon0.m_20069_()) {
            $$5 = 1.3F;
            $$6 = 1.7F;
        }
        float $$7 = $$5 * 4.3F * Mth.sin($$6 * 0.6F * float2);
        poseStack1.mulPose(Axis.YP.rotationDegrees($$7));
        poseStack1.translate(0.0F, 0.0F, -0.4F);
        if (!salmon0.m_20069_()) {
            poseStack1.translate(0.2F, 0.1F, 0.0F);
            poseStack1.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }
}