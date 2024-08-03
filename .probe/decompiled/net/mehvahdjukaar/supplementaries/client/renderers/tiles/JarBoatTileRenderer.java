package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.mehvahdjukaar.moonlight.api.client.util.RenderUtil;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.supplementaries.common.block.blocks.JarBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.JarBoatTile;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class JarBoatTileRenderer implements BlockEntityRenderer<JarBoatTile> {

    private final BlockRenderDispatcher blockRenderer;

    public JarBoatTileRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    public void render(JarBoatTile tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5, 0.5, 0.5);
        matrixStackIn.mulPose(RotHlpr.rot((int) (-((Direction) tile.m_58900_().m_61143_(JarBlock.FACING)).getOpposite().toYRot())));
        matrixStackIn.translate(0.0F, -0.1875F, 0.0F);
        float t = (float) (System.currentTimeMillis() % 360000L) / 1000.0F;
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(Mth.sin(t) * 1.7F));
        matrixStackIn.translate(-0.5, 0.0, -0.5);
        RenderUtil.renderModel(ClientRegistry.BOAT_MODEL, matrixStackIn, bufferIn, this.blockRenderer, combinedLightIn, combinedOverlayIn, false);
        matrixStackIn.popPose();
    }
}