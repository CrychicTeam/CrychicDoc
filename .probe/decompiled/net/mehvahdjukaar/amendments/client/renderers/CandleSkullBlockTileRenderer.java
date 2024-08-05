package net.mehvahdjukaar.amendments.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.amendments.common.block.WallCandleSkullBlock;
import net.mehvahdjukaar.amendments.common.tile.CandleSkullBlockTile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CandleSkullBlockTileRenderer extends SkullWithWaxTileRenderer<CandleSkullBlockTile> {

    public CandleSkullBlockTileRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public void render(CandleSkullBlockTile tile, float pPartialTicks, PoseStack poseStack, MultiBufferSource buffer, int pCombinedLight, int pCombinedOverlay) {
        super.render(tile, pPartialTicks, poseStack, buffer, pCombinedLight, pCombinedOverlay);
        BlockState blockstate = tile.m_58900_();
        BlockState candle = tile.getCandle();
        if (!candle.m_60795_()) {
            candle = (BlockState) ((BlockState) candle.m_61124_(CandleBlock.LIT, (Boolean) blockstate.m_61143_(CandleBlock.LIT))).m_61124_(CandleBlock.CANDLES, (Integer) blockstate.m_61143_(CandleBlock.CANDLES));
            float yaw;
            if (blockstate.m_61138_(WallCandleSkullBlock.FACING)) {
                yaw = ((Direction) blockstate.m_61143_(WallCandleSkullBlock.FACING)).toYRot();
            } else {
                yaw = -22.5F * (float) ((Integer) blockstate.m_61143_(SkullBlock.ROTATION)).intValue();
            }
            this.renderWax(poseStack, buffer, pCombinedLight, tile.getWaxTexture(), yaw);
            poseStack.translate(0.0, 0.5, 0.0);
            this.blockRenderer.renderSingleBlock(candle, poseStack, buffer, pCombinedLight, pCombinedOverlay);
        }
    }
}