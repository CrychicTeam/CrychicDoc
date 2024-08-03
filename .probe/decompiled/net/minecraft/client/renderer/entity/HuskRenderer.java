package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;

public class HuskRenderer extends ZombieRenderer {

    private static final ResourceLocation HUSK_LOCATION = new ResourceLocation("textures/entity/zombie/husk.png");

    public HuskRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, ModelLayers.HUSK, ModelLayers.HUSK_INNER_ARMOR, ModelLayers.HUSK_OUTER_ARMOR);
    }

    protected void scale(Zombie zombie0, PoseStack poseStack1, float float2) {
        float $$3 = 1.0625F;
        poseStack1.scale(1.0625F, 1.0625F, 1.0625F);
        super.m_7546_(zombie0, poseStack1, float2);
    }

    @Override
    public ResourceLocation getTextureLocation(Zombie zombie0) {
        return HUSK_LOCATION;
    }
}