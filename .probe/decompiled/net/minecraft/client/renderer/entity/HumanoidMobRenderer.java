package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.Mob;

public abstract class HumanoidMobRenderer<T extends Mob, M extends HumanoidModel<T>> extends MobRenderer<T, M> {

    public HumanoidMobRenderer(EntityRendererProvider.Context entityRendererProviderContext0, M m1, float float2) {
        this(entityRendererProviderContext0, m1, float2, 1.0F, 1.0F, 1.0F);
    }

    public HumanoidMobRenderer(EntityRendererProvider.Context entityRendererProviderContext0, M m1, float float2, float float3, float float4, float float5) {
        super(entityRendererProviderContext0, m1, float2);
        this.m_115326_(new CustomHeadLayer<>(this, entityRendererProviderContext0.getModelSet(), float3, float4, float5, entityRendererProviderContext0.getItemInHandRenderer()));
        this.m_115326_(new ElytraLayer<>(this, entityRendererProviderContext0.getModelSet()));
        this.m_115326_(new ItemInHandLayer<>(this, entityRendererProviderContext0.getItemInHandRenderer()));
    }
}