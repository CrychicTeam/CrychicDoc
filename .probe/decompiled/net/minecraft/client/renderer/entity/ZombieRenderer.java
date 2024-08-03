package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.world.entity.monster.Zombie;

public class ZombieRenderer extends AbstractZombieRenderer<Zombie, ZombieModel<Zombie>> {

    public ZombieRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        this(entityRendererProviderContext0, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR);
    }

    public ZombieRenderer(EntityRendererProvider.Context entityRendererProviderContext0, ModelLayerLocation modelLayerLocation1, ModelLayerLocation modelLayerLocation2, ModelLayerLocation modelLayerLocation3) {
        super(entityRendererProviderContext0, new ZombieModel<>(entityRendererProviderContext0.bakeLayer(modelLayerLocation1)), new ZombieModel<>(entityRendererProviderContext0.bakeLayer(modelLayerLocation2)), new ZombieModel<>(entityRendererProviderContext0.bakeLayer(modelLayerLocation3)));
    }
}