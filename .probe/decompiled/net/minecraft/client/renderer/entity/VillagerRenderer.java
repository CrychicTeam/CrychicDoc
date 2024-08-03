package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;

public class VillagerRenderer extends MobRenderer<Villager, VillagerModel<Villager>> {

    private static final ResourceLocation VILLAGER_BASE_SKIN = new ResourceLocation("textures/entity/villager/villager.png");

    public VillagerRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new VillagerModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.VILLAGER)), 0.5F);
        this.m_115326_(new CustomHeadLayer<>(this, entityRendererProviderContext0.getModelSet(), entityRendererProviderContext0.getItemInHandRenderer()));
        this.m_115326_(new VillagerProfessionLayer<>(this, entityRendererProviderContext0.getResourceManager(), "villager"));
        this.m_115326_(new CrossedArmsItemLayer<>(this, entityRendererProviderContext0.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(Villager villager0) {
        return VILLAGER_BASE_SKIN;
    }

    protected void scale(Villager villager0, PoseStack poseStack1, float float2) {
        float $$3 = 0.9375F;
        if (villager0.m_6162_()) {
            $$3 *= 0.5F;
            this.f_114477_ = 0.25F;
        } else {
            this.f_114477_ = 0.5F;
        }
        poseStack1.scale($$3, $$3, $$3);
    }
}