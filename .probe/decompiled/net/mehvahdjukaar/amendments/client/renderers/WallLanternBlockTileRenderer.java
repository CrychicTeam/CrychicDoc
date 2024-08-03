package net.mehvahdjukaar.amendments.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.mehvahdjukaar.amendments.client.WallLanternModelsManager;
import net.mehvahdjukaar.amendments.common.block.WallLanternBlock;
import net.mehvahdjukaar.amendments.common.tile.WallLanternBlockTile;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.ShimmerCompat;
import net.mehvahdjukaar.moonlight.api.client.util.RenderUtil;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class WallLanternBlockTileRenderer implements BlockEntityRenderer<WallLanternBlockTile> {

    protected final BlockRenderDispatcher blockRenderer;

    public WallLanternBlockTileRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    public boolean shouldRender(WallLanternBlockTile blockEntity, Vec3 cameraPos) {
        return blockEntity.shouldRenderFancy(cameraPos);
    }

    public void renderLantern(WallLanternBlockTile tile, BlockState lanternState, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, boolean ceiling) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.875, 0.5);
        poseStack.mulPose(RotHlpr.rot((Direction) tile.m_58900_().m_61143_(WallLanternBlock.FACING)));
        float angle = tile.getAnimation().getAngle(partialTicks);
        poseStack.mulPose(Axis.ZP.rotationDegrees(angle));
        poseStack.translate(-0.5, -0.75 - tile.getAttachmentOffset(), -0.375);
        BakedModel model = WallLanternModelsManager.getModel(this.blockRenderer.getBlockModelShaper(), lanternState);
        Level level = tile.m_58904_();
        BlockPos pos = tile.m_58899_();
        if (CompatHandler.SHIMMER) {
            ShimmerCompat.renderWithBloom(poseStack, (p, b) -> RenderUtil.renderBlock(model, 0L, p, b, lanternState, level, pos, this.blockRenderer));
        } else {
            RenderUtil.renderBlock(model, 0L, poseStack, bufferIn, lanternState, level, pos, this.blockRenderer);
        }
        poseStack.popPose();
    }

    public void render(WallLanternBlockTile tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        this.renderLantern(tile, tile.getHeldBlock(), partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, false);
    }
}