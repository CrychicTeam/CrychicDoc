package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.StriderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Strider;

public class StriderRenderer extends MobRenderer<Strider, StriderModel<Strider>> {

    private static final ResourceLocation STRIDER_LOCATION = new ResourceLocation("textures/entity/strider/strider.png");

    private static final ResourceLocation COLD_LOCATION = new ResourceLocation("textures/entity/strider/strider_cold.png");

    public StriderRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new StriderModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.STRIDER)), 0.5F);
        this.m_115326_(new SaddleLayer<>(this, new StriderModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.STRIDER_SADDLE)), new ResourceLocation("textures/entity/strider/strider_saddle.png")));
    }

    public ResourceLocation getTextureLocation(Strider strider0) {
        return strider0.isSuffocating() ? COLD_LOCATION : STRIDER_LOCATION;
    }

    protected void scale(Strider strider0, PoseStack poseStack1, float float2) {
        if (strider0.m_6162_()) {
            poseStack1.scale(0.5F, 0.5F, 0.5F);
            this.f_114477_ = 0.25F;
        } else {
            this.f_114477_ = 0.5F;
        }
    }

    protected boolean isShaking(Strider strider0) {
        return super.m_5936_(strider0) || strider0.isSuffocating();
    }
}