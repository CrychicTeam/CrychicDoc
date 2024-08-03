package com.mna.entities.renderers.item;

import com.mna.entities.WandClone;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class WandCloneRenderer extends EntityRenderer<WandClone> {

    public WandCloneRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(WandClone entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        entityIn.spawnParticles();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(WandClone entity) {
        return null;
    }
}