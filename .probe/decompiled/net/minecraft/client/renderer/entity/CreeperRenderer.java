package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.CreeperPowerLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Creeper;

public class CreeperRenderer extends MobRenderer<Creeper, CreeperModel<Creeper>> {

    private static final ResourceLocation CREEPER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");

    public CreeperRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new CreeperModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.CREEPER)), 0.5F);
        this.m_115326_(new CreeperPowerLayer(this, entityRendererProviderContext0.getModelSet()));
    }

    protected void scale(Creeper creeper0, PoseStack poseStack1, float float2) {
        float $$3 = creeper0.getSwelling(float2);
        float $$4 = 1.0F + Mth.sin($$3 * 100.0F) * $$3 * 0.01F;
        $$3 = Mth.clamp($$3, 0.0F, 1.0F);
        $$3 *= $$3;
        $$3 *= $$3;
        float $$5 = (1.0F + $$3 * 0.4F) * $$4;
        float $$6 = (1.0F + $$3 * 0.1F) / $$4;
        poseStack1.scale($$5, $$6, $$5);
    }

    protected float getWhiteOverlayProgress(Creeper creeper0, float float1) {
        float $$2 = creeper0.getSwelling(float1);
        return (int) ($$2 * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp($$2, 0.5F, 1.0F);
    }

    public ResourceLocation getTextureLocation(Creeper creeper0) {
        return CREEPER_LOCATION;
    }
}