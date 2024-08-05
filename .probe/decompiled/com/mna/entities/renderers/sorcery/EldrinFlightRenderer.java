package com.mna.entities.renderers.sorcery;

import com.mna.entities.utility.EldrinFlight;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class EldrinFlightRenderer extends EntityRenderer<EldrinFlight> {

    public EldrinFlightRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(EldrinFlight entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        WorldRenderUtils.renderRadiant(entityIn, matrixStackIn, bufferIn, new int[] { 190, 228, 237 }, new int[] { 52, 201, 235 }, 200, 0.05F);
    }

    public ResourceLocation getTextureLocation(EldrinFlight entity) {
        return new ResourceLocation("");
    }
}