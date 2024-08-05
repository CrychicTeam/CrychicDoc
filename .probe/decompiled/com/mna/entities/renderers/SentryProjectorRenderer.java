package com.mna.entities.renderers;

import com.mna.entities.projectile.SentryProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SentryProjectorRenderer extends EntityRenderer<SentryProjectile> {

    public SentryProjectorRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(SentryProjectile entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        entityIn.spawnParticles(10, partialTicks);
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(SentryProjectile pEntity) {
        return null;
    }
}