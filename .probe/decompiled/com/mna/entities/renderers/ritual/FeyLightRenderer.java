package com.mna.entities.renderers.ritual;

import com.mna.entities.rituals.FeyLight;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FeyLightRenderer extends EntityRenderer<FeyLight> {

    public FeyLightRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(FeyLight entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        WorldRenderUtils.renderRadiant(entityIn, matrixStackIn, bufferIn, new int[] { 52, 55, 99 }, new int[] { 100, 62, 98 }, 255, 0.2F);
    }

    public ResourceLocation getTextureLocation(FeyLight entity) {
        return null;
    }
}