package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.CodModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Cod;

public class CodRenderer extends MobRenderer<Cod, CodModel<Cod>> {

    private static final ResourceLocation COD_LOCATION = new ResourceLocation("textures/entity/fish/cod.png");

    public CodRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new CodModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.COD)), 0.3F);
    }

    public ResourceLocation getTextureLocation(Cod cod0) {
        return COD_LOCATION;
    }

    protected void setupRotations(Cod cod0, PoseStack poseStack1, float float2, float float3, float float4) {
        super.m_7523_(cod0, poseStack1, float2, float3, float4);
        float $$5 = 4.3F * Mth.sin(0.6F * float2);
        poseStack1.mulPose(Axis.YP.rotationDegrees($$5));
        if (!cod0.m_20069_()) {
            poseStack1.translate(0.1F, 0.1F, -0.1F);
            poseStack1.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }
}