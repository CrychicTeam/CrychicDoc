package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.world.entity.monster.AbstractIllager;

public abstract class IllagerRenderer<T extends AbstractIllager> extends MobRenderer<T, IllagerModel<T>> {

    protected IllagerRenderer(EntityRendererProvider.Context entityRendererProviderContext0, IllagerModel<T> illagerModelT1, float float2) {
        super(entityRendererProviderContext0, illagerModelT1, float2);
        this.m_115326_(new CustomHeadLayer<>(this, entityRendererProviderContext0.getModelSet(), entityRendererProviderContext0.getItemInHandRenderer()));
    }

    protected void scale(T t0, PoseStack poseStack1, float float2) {
        float $$3 = 0.9375F;
        poseStack1.scale(0.9375F, 0.9375F, 0.9375F);
    }
}