package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.GhastModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Ghast;

public class GhastRenderer extends MobRenderer<Ghast, GhastModel<Ghast>> {

    private static final ResourceLocation GHAST_LOCATION = new ResourceLocation("textures/entity/ghast/ghast.png");

    private static final ResourceLocation GHAST_SHOOTING_LOCATION = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");

    public GhastRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new GhastModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.GHAST)), 1.5F);
    }

    public ResourceLocation getTextureLocation(Ghast ghast0) {
        return ghast0.isCharging() ? GHAST_SHOOTING_LOCATION : GHAST_LOCATION;
    }

    protected void scale(Ghast ghast0, PoseStack poseStack1, float float2) {
        float $$3 = 1.0F;
        float $$4 = 4.5F;
        float $$5 = 4.5F;
        poseStack1.scale(4.5F, 4.5F, 4.5F);
    }
}