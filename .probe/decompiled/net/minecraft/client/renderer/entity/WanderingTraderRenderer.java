package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.WanderingTrader;

public class WanderingTraderRenderer extends MobRenderer<WanderingTrader, VillagerModel<WanderingTrader>> {

    private static final ResourceLocation VILLAGER_BASE_SKIN = new ResourceLocation("textures/entity/wandering_trader.png");

    public WanderingTraderRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new VillagerModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.WANDERING_TRADER)), 0.5F);
        this.m_115326_(new CustomHeadLayer<>(this, entityRendererProviderContext0.getModelSet(), entityRendererProviderContext0.getItemInHandRenderer()));
        this.m_115326_(new CrossedArmsItemLayer<>(this, entityRendererProviderContext0.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(WanderingTrader wanderingTrader0) {
        return VILLAGER_BASE_SKIN;
    }

    protected void scale(WanderingTrader wanderingTrader0, PoseStack poseStack1, float float2) {
        float $$3 = 0.9375F;
        poseStack1.scale(0.9375F, 0.9375F, 0.9375F);
    }
}