package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PolarBearModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.PolarBear;

public class PolarBearRenderer extends MobRenderer<PolarBear, PolarBearModel<PolarBear>> {

    private static final ResourceLocation BEAR_LOCATION = new ResourceLocation("textures/entity/bear/polarbear.png");

    public PolarBearRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new PolarBearModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.POLAR_BEAR)), 0.9F);
    }

    public ResourceLocation getTextureLocation(PolarBear polarBear0) {
        return BEAR_LOCATION;
    }

    protected void scale(PolarBear polarBear0, PoseStack poseStack1, float float2) {
        poseStack1.scale(1.2F, 1.2F, 1.2F);
        super.m_7546_(polarBear0, poseStack1, float2);
    }
}