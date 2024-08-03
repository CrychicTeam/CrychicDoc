package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;

public abstract class AbstractZombieRenderer<T extends Zombie, M extends ZombieModel<T>> extends HumanoidMobRenderer<T, M> {

    private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation("textures/entity/zombie/zombie.png");

    protected AbstractZombieRenderer(EntityRendererProvider.Context entityRendererProviderContext0, M m1, M m2, M m3) {
        super(entityRendererProviderContext0, m1, 0.5F);
        this.m_115326_(new HumanoidArmorLayer<>(this, m2, m3, entityRendererProviderContext0.getModelManager()));
    }

    public ResourceLocation getTextureLocation(Zombie zombie0) {
        return ZOMBIE_LOCATION;
    }

    protected boolean isShaking(T t0) {
        return super.m_5936_(t0) || t0.isUnderWaterConverting();
    }
}