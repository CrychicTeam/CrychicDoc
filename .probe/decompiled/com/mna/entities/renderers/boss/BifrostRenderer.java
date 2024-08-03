package com.mna.entities.renderers.boss;

import com.mna.entities.boss.effects.Bifrost;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BifrostRenderer extends EntityRenderer<Bifrost> {

    public BifrostRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(Bifrost entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (!Minecraft.getInstance().isPaused()) {
            entityIn.spawnParticles(10, partialTicks);
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(Bifrost entity) {
        return null;
    }
}