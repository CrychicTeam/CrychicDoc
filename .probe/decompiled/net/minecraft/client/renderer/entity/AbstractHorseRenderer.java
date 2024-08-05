package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HorseModel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public abstract class AbstractHorseRenderer<T extends AbstractHorse, M extends HorseModel<T>> extends MobRenderer<T, M> {

    private final float scale;

    public AbstractHorseRenderer(EntityRendererProvider.Context entityRendererProviderContext0, M m1, float float2) {
        super(entityRendererProviderContext0, m1, 0.75F);
        this.scale = float2;
    }

    protected void scale(T t0, PoseStack poseStack1, float float2) {
        poseStack1.scale(this.scale, this.scale, this.scale);
        super.m_7546_(t0, poseStack1, float2);
    }
}