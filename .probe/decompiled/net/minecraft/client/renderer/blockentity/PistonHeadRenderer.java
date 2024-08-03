package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;

public class PistonHeadRenderer implements BlockEntityRenderer<PistonMovingBlockEntity> {

    private final BlockRenderDispatcher blockRenderer;

    public PistonHeadRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        this.blockRenderer = blockEntityRendererProviderContext0.getBlockRenderDispatcher();
    }

    public void render(PistonMovingBlockEntity pistonMovingBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        Level $$6 = pistonMovingBlockEntity0.m_58904_();
        if ($$6 != null) {
            BlockPos $$7 = pistonMovingBlockEntity0.m_58899_().relative(pistonMovingBlockEntity0.getMovementDirection().getOpposite());
            BlockState $$8 = pistonMovingBlockEntity0.getMovedState();
            if (!$$8.m_60795_()) {
                ModelBlockRenderer.enableCaching();
                poseStack2.pushPose();
                poseStack2.translate(pistonMovingBlockEntity0.getXOff(float1), pistonMovingBlockEntity0.getYOff(float1), pistonMovingBlockEntity0.getZOff(float1));
                if ($$8.m_60713_(Blocks.PISTON_HEAD) && pistonMovingBlockEntity0.getProgress(float1) <= 4.0F) {
                    $$8 = (BlockState) $$8.m_61124_(PistonHeadBlock.SHORT, pistonMovingBlockEntity0.getProgress(float1) <= 0.5F);
                    this.renderBlock($$7, $$8, poseStack2, multiBufferSource3, $$6, false, int5);
                } else if (pistonMovingBlockEntity0.isSourcePiston() && !pistonMovingBlockEntity0.isExtending()) {
                    PistonType $$9 = $$8.m_60713_(Blocks.STICKY_PISTON) ? PistonType.STICKY : PistonType.DEFAULT;
                    BlockState $$10 = (BlockState) ((BlockState) Blocks.PISTON_HEAD.defaultBlockState().m_61124_(PistonHeadBlock.TYPE, $$9)).m_61124_(PistonHeadBlock.f_52588_, (Direction) $$8.m_61143_(PistonBaseBlock.f_52588_));
                    $$10 = (BlockState) $$10.m_61124_(PistonHeadBlock.SHORT, pistonMovingBlockEntity0.getProgress(float1) >= 0.5F);
                    this.renderBlock($$7, $$10, poseStack2, multiBufferSource3, $$6, false, int5);
                    BlockPos $$11 = $$7.relative(pistonMovingBlockEntity0.getMovementDirection());
                    poseStack2.popPose();
                    poseStack2.pushPose();
                    $$8 = (BlockState) $$8.m_61124_(PistonBaseBlock.EXTENDED, true);
                    this.renderBlock($$11, $$8, poseStack2, multiBufferSource3, $$6, true, int5);
                } else {
                    this.renderBlock($$7, $$8, poseStack2, multiBufferSource3, $$6, false, int5);
                }
                poseStack2.popPose();
                ModelBlockRenderer.clearCache();
            }
        }
    }

    private void renderBlock(BlockPos blockPos0, BlockState blockState1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, Level level4, boolean boolean5, int int6) {
        RenderType $$7 = ItemBlockRenderTypes.getMovingBlockRenderType(blockState1);
        VertexConsumer $$8 = multiBufferSource3.getBuffer($$7);
        this.blockRenderer.getModelRenderer().tesselateBlock(level4, this.blockRenderer.getBlockModel(blockState1), blockState1, blockPos0, poseStack2, $$8, boolean5, RandomSource.create(), blockState1.m_60726_(blockPos0), int6);
    }

    @Override
    public int getViewDistance() {
        return 68;
    }
}