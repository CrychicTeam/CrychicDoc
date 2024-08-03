package com.mna.blocks.tileentities.renderers;

import com.mna.ManaAndArtifice;
import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.ManaweaveCacheTile;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.tools.render.ModelUtils;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ManaweaveCacheRenderer implements BlockEntityRenderer<ManaweaveCacheTile> {

    public static final ResourceLocation interior = RLoc.create("block/special/manaweave_cache_inner");

    public static final ResourceLocation exterior = RLoc.create("block/special/manaweave_cache_outer");

    public ManaweaveCacheRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(ManaweaveCacheTile tile, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Level world = tile.m_58904_();
        BlockPos pos = tile.m_58899_();
        BlockState state = tile.m_58900_();
        for (int i = 0; i < 4; i++) {
            this.renderOuterSegment(tile, partialTicks, i, bufferIn, world, pos, state, matrixStack, combinedLightIn, combinedOverlayIn);
        }
        if (tile.openPct() > 0.0F) {
            matrixStack.pushPose();
            matrixStack.translate(0.5, 0.0, 0.5);
            ModelUtils.renderModel(bufferIn, world, pos, state, interior, matrixStack, combinedLightIn, combinedOverlayIn);
            if (!tile.isBuff()) {
                matrixStack.translate(0.0, 1.5, 0.0);
                WorldRenderUtils.renderRadiant((float) ManaAndArtifice.instance.proxy.getGameTicks() + partialTicks, matrixStack, bufferIn, new int[] { 128, 128, 128 }, new int[] { 128, 128, 128 }, 67, 1.0F, false);
            }
            matrixStack.popPose();
        }
    }

    private void renderOuterSegment(ManaweaveCacheTile tile, float partialTicks, int quadrant, MultiBufferSource bufferIn, Level world, BlockPos pos, BlockState state, PoseStack matrixStack, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        float openPct = 0.0F;
        if (tile.isOpen()) {
            if (tile.openPct() == 0.0F) {
                openPct = 0.0F;
            } else if (tile.openPct() < 1.0F) {
                openPct = tile.openPct() + 0.1F * partialTicks;
            } else {
                openPct = 1.0F;
            }
        } else if (tile.openPct() == 0.0F) {
            openPct = 0.0F;
        } else if (tile.openPct() < 1.0F) {
            openPct = tile.openPct() - 0.1F * partialTicks;
        } else {
            openPct = 1.0F;
        }
        switch(quadrant) {
            case 0:
                matrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                matrixStack.translate(-0.5, 0.0, 0.5);
                break;
            case 1:
                matrixStack.translate(0.5, 0.0, 0.5);
                break;
            case 2:
                matrixStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
                matrixStack.translate(0.5, 0.0, -0.5);
                break;
            case 3:
                matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                matrixStack.translate(-0.5, 0.0, -0.5);
        }
        matrixStack.translate(0.25F * openPct, 0.0F, -0.25F * openPct);
        ModelUtils.renderModel(bufferIn, world, pos, state, exterior, matrixStack, combinedLight, combinedOverlay);
        ManaweavingPattern[] patterns = tile.getRequiredPatterns();
        if (patterns.length > 0) {
            matrixStack.translate(0.0F, 1.57F, 0.0F);
            matrixStack.translate(0.27F, 0.0F, -0.27F);
            matrixStack.scale(0.2F, 0.2F, 0.2F);
            for (int i = 0; i < patterns.length; i++) {
                if (patterns[i] != null) {
                    if (tile.isPatternAdded(i)) {
                        WorldRenderUtils.renderManaweavePatternNoTransparent(patterns[i], Axis.YP.rotationDegrees(-45.0F), matrixStack, bufferIn, false);
                    } else {
                        WorldRenderUtils.renderManaweavePatternNoTransparent(patterns[i], Axis.YP.rotationDegrees(-45.0F), matrixStack, bufferIn, false, new int[] { 255, 0, 0, 255 });
                    }
                }
                matrixStack.translate(0.0F, -1.25F, 0.0F);
            }
        }
        matrixStack.popPose();
    }
}