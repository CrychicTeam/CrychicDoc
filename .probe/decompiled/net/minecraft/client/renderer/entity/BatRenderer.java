package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.BatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ambient.Bat;

public class BatRenderer extends MobRenderer<Bat, BatModel> {

    private static final ResourceLocation BAT_LOCATION = new ResourceLocation("textures/entity/bat.png");

    public BatRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new BatModel(entityRendererProviderContext0.bakeLayer(ModelLayers.BAT)), 0.25F);
    }

    public ResourceLocation getTextureLocation(Bat bat0) {
        return BAT_LOCATION;
    }

    protected void scale(Bat bat0, PoseStack poseStack1, float float2) {
        poseStack1.scale(0.35F, 0.35F, 0.35F);
    }

    protected void setupRotations(Bat bat0, PoseStack poseStack1, float float2, float float3, float float4) {
        if (bat0.isResting()) {
            poseStack1.translate(0.0F, -0.1F, 0.0F);
        } else {
            poseStack1.translate(0.0F, Mth.cos(float2 * 0.3F) * 0.1F, 0.0F);
        }
        super.m_7523_(bat0, poseStack1, float2, float3, float4);
    }
}