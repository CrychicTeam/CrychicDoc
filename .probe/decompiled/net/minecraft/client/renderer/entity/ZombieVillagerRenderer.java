package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ZombieVillagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.ZombieVillager;

public class ZombieVillagerRenderer extends HumanoidMobRenderer<ZombieVillager, ZombieVillagerModel<ZombieVillager>> {

    private static final ResourceLocation ZOMBIE_VILLAGER_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_villager.png");

    public ZombieVillagerRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new ZombieVillagerModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.ZOMBIE_VILLAGER)), 0.5F);
        this.m_115326_(new HumanoidArmorLayer<>(this, new ZombieVillagerModel(entityRendererProviderContext0.bakeLayer(ModelLayers.ZOMBIE_VILLAGER_INNER_ARMOR)), new ZombieVillagerModel(entityRendererProviderContext0.bakeLayer(ModelLayers.ZOMBIE_VILLAGER_OUTER_ARMOR)), entityRendererProviderContext0.getModelManager()));
        this.m_115326_(new VillagerProfessionLayer<>(this, entityRendererProviderContext0.getResourceManager(), "zombie_villager"));
    }

    public ResourceLocation getTextureLocation(ZombieVillager zombieVillager0) {
        return ZOMBIE_VILLAGER_LOCATION;
    }

    protected boolean isShaking(ZombieVillager zombieVillager0) {
        return super.m_5936_(zombieVillager0) || zombieVillager0.isConverting();
    }
}