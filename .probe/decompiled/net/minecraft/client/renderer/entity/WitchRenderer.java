package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.WitchModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.WitchItemLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Witch;

public class WitchRenderer extends MobRenderer<Witch, WitchModel<Witch>> {

    private static final ResourceLocation WITCH_LOCATION = new ResourceLocation("textures/entity/witch.png");

    public WitchRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new WitchModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.WITCH)), 0.5F);
        this.m_115326_(new WitchItemLayer<>(this, entityRendererProviderContext0.getItemInHandRenderer()));
    }

    public void render(Witch witch0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        ((WitchModel) this.f_115290_).setHoldingItem(!witch0.m_21205_().isEmpty());
        super.render(witch0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public ResourceLocation getTextureLocation(Witch witch0) {
        return WITCH_LOCATION;
    }

    protected void scale(Witch witch0, PoseStack poseStack1, float float2) {
        float $$3 = 0.9375F;
        poseStack1.scale(0.9375F, 0.9375F, 0.9375F);
    }
}