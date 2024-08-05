package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.level.block.state.BlockState;

public class TntMinecartRenderer extends MinecartRenderer<MinecartTNT> {

    private final BlockRenderDispatcher blockRenderer;

    public TntMinecartRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, ModelLayers.TNT_MINECART);
        this.blockRenderer = entityRendererProviderContext0.getBlockRenderDispatcher();
    }

    protected void renderMinecartContents(MinecartTNT minecartTNT0, float float1, BlockState blockState2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        int $$6 = minecartTNT0.getFuse();
        if ($$6 > -1 && (float) $$6 - float1 + 1.0F < 10.0F) {
            float $$7 = 1.0F - ((float) $$6 - float1 + 1.0F) / 10.0F;
            $$7 = Mth.clamp($$7, 0.0F, 1.0F);
            $$7 *= $$7;
            $$7 *= $$7;
            float $$8 = 1.0F + $$7 * 0.3F;
            poseStack3.scale($$8, $$8, $$8);
        }
        renderWhiteSolidBlock(this.blockRenderer, blockState2, poseStack3, multiBufferSource4, int5, $$6 > -1 && $$6 / 5 % 2 == 0);
    }

    public static void renderWhiteSolidBlock(BlockRenderDispatcher blockRenderDispatcher0, BlockState blockState1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, boolean boolean5) {
        int $$6;
        if (boolean5) {
            $$6 = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
        } else {
            $$6 = OverlayTexture.NO_OVERLAY;
        }
        blockRenderDispatcher0.renderSingleBlock(blockState1, poseStack2, multiBufferSource3, int4, $$6);
    }
}