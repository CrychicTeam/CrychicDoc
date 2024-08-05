package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class FallingBlockRenderer extends EntityRenderer<FallingBlockEntity> {

    private final BlockRenderDispatcher dispatcher;

    public FallingBlockRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.f_114477_ = 0.5F;
        this.dispatcher = entityRendererProviderContext0.getBlockRenderDispatcher();
    }

    public void render(FallingBlockEntity fallingBlockEntity0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        BlockState $$6 = fallingBlockEntity0.getBlockState();
        if ($$6.m_60799_() == RenderShape.MODEL) {
            Level $$7 = fallingBlockEntity0.m_9236_();
            if ($$6 != $$7.getBlockState(fallingBlockEntity0.m_20183_()) && $$6.m_60799_() != RenderShape.INVISIBLE) {
                poseStack3.pushPose();
                BlockPos $$8 = BlockPos.containing(fallingBlockEntity0.m_20185_(), fallingBlockEntity0.m_20191_().maxY, fallingBlockEntity0.m_20189_());
                poseStack3.translate(-0.5, 0.0, -0.5);
                this.dispatcher.getModelRenderer().tesselateBlock($$7, this.dispatcher.getBlockModel($$6), $$6, $$8, poseStack3, multiBufferSource4.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType($$6)), false, RandomSource.create(), $$6.m_60726_(fallingBlockEntity0.getStartPos()), OverlayTexture.NO_OVERLAY);
                poseStack3.popPose();
                super.render(fallingBlockEntity0, float1, float2, poseStack3, multiBufferSource4, int5);
            }
        }
    }

    public ResourceLocation getTextureLocation(FallingBlockEntity fallingBlockEntity0) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}