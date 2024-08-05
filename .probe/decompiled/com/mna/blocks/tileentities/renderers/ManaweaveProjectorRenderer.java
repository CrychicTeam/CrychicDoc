package com.mna.blocks.tileentities.renderers;

import com.mna.blocks.tileentities.ManaweaveProjectorTile;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ManaweaveProjectorRenderer implements BlockEntityRenderer<ManaweaveProjectorTile> {

    public ManaweaveProjectorRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(ManaweaveProjectorTile tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ManaweavingPattern pattern = tileEntityIn.getPattern();
        if (pattern != null) {
            float baseScale = 0.2F;
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 0.7, 0.5);
            matrixStackIn.scale(baseScale, baseScale, baseScale);
            WorldRenderUtils.renderManaweavePattern(pattern, Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation(), matrixStackIn, bufferIn, false);
            matrixStackIn.popPose();
        }
    }
}