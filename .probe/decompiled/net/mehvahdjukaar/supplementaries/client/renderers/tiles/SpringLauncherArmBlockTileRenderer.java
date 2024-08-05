package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SpringLauncherHeadBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SpringLauncherArmBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SpringLauncherArmBlockTileRenderer implements BlockEntityRenderer<SpringLauncherArmBlockTile> {

    private final BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

    public SpringLauncherArmBlockTileRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(SpringLauncherArmBlockTile tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5, 0.5, 0.5);
        matrixStackIn.mulPose(RotHlpr.rot(tile.getDirection()));
        matrixStackIn.translate(-0.5, -0.5, -0.5);
        matrixStackIn.translate(0.0, 0.0, -tile.getRenderOffset(partialTicks));
        boolean flag1 = tile.getExtending() == tile.getAge() < 2;
        BlockState state = (BlockState) ((BlockState) ((Block) ModRegistry.SPRING_LAUNCHER_HEAD.get()).defaultBlockState().m_61124_(SpringLauncherHeadBlock.FACING, Direction.NORTH)).m_61124_(BlockStateProperties.SHORT, flag1);
        this.blockRenderer.renderSingleBlock(state, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        matrixStackIn.popPose();
    }
}