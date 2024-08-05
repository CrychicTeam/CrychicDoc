package com.mna.entities.renderers.ritual;

import com.mna.entities.rituals.FlatLandsProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FlatLandsProjectileRenderer extends EntityRenderer<FlatLandsProjectile> {

    public FlatLandsProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(FlatLandsProjectile entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(FlatLandsProjectile entity) {
        return null;
    }
}