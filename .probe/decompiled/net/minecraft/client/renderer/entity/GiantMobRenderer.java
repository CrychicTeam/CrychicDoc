package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.GiantZombieModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Giant;

public class GiantMobRenderer extends MobRenderer<Giant, HumanoidModel<Giant>> {

    private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation("textures/entity/zombie/zombie.png");

    private final float scale;

    public GiantMobRenderer(EntityRendererProvider.Context entityRendererProviderContext0, float float1) {
        super(entityRendererProviderContext0, new GiantZombieModel(entityRendererProviderContext0.bakeLayer(ModelLayers.GIANT)), 0.5F * float1);
        this.scale = float1;
        this.m_115326_(new ItemInHandLayer<>(this, entityRendererProviderContext0.getItemInHandRenderer()));
        this.m_115326_(new HumanoidArmorLayer<>(this, new GiantZombieModel(entityRendererProviderContext0.bakeLayer(ModelLayers.GIANT_INNER_ARMOR)), new GiantZombieModel(entityRendererProviderContext0.bakeLayer(ModelLayers.GIANT_OUTER_ARMOR)), entityRendererProviderContext0.getModelManager()));
    }

    protected void scale(Giant giant0, PoseStack poseStack1, float float2) {
        poseStack1.scale(this.scale, this.scale, this.scale);
    }

    public ResourceLocation getTextureLocation(Giant giant0) {
        return ZOMBIE_LOCATION;
    }
}