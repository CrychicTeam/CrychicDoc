package net.minecraft.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class BlockRenderDispatcher implements ResourceManagerReloadListener {

    private final BlockModelShaper blockModelShaper;

    private final ModelBlockRenderer modelRenderer;

    private final BlockEntityWithoutLevelRenderer blockEntityRenderer;

    private final LiquidBlockRenderer liquidBlockRenderer;

    private final RandomSource random = RandomSource.create();

    private final BlockColors blockColors;

    public BlockRenderDispatcher(BlockModelShaper blockModelShaper0, BlockEntityWithoutLevelRenderer blockEntityWithoutLevelRenderer1, BlockColors blockColors2) {
        this.blockModelShaper = blockModelShaper0;
        this.blockEntityRenderer = blockEntityWithoutLevelRenderer1;
        this.blockColors = blockColors2;
        this.modelRenderer = new ModelBlockRenderer(this.blockColors);
        this.liquidBlockRenderer = new LiquidBlockRenderer();
    }

    public BlockModelShaper getBlockModelShaper() {
        return this.blockModelShaper;
    }

    public void renderBreakingTexture(BlockState blockState0, BlockPos blockPos1, BlockAndTintGetter blockAndTintGetter2, PoseStack poseStack3, VertexConsumer vertexConsumer4) {
        if (blockState0.m_60799_() == RenderShape.MODEL) {
            BakedModel $$5 = this.blockModelShaper.getBlockModel(blockState0);
            long $$6 = blockState0.m_60726_(blockPos1);
            this.modelRenderer.tesselateBlock(blockAndTintGetter2, $$5, blockState0, blockPos1, poseStack3, vertexConsumer4, true, this.random, $$6, OverlayTexture.NO_OVERLAY);
        }
    }

    public void renderBatched(BlockState blockState0, BlockPos blockPos1, BlockAndTintGetter blockAndTintGetter2, PoseStack poseStack3, VertexConsumer vertexConsumer4, boolean boolean5, RandomSource randomSource6) {
        try {
            RenderShape $$7 = blockState0.m_60799_();
            if ($$7 == RenderShape.MODEL) {
                this.modelRenderer.tesselateBlock(blockAndTintGetter2, this.getBlockModel(blockState0), blockState0, blockPos1, poseStack3, vertexConsumer4, boolean5, randomSource6, blockState0.m_60726_(blockPos1), OverlayTexture.NO_OVERLAY);
            }
        } catch (Throwable var11) {
            CrashReport $$9 = CrashReport.forThrowable(var11, "Tesselating block in world");
            CrashReportCategory $$10 = $$9.addCategory("Block being tesselated");
            CrashReportCategory.populateBlockDetails($$10, blockAndTintGetter2, blockPos1, blockState0);
            throw new ReportedException($$9);
        }
    }

    public void renderLiquid(BlockPos blockPos0, BlockAndTintGetter blockAndTintGetter1, VertexConsumer vertexConsumer2, BlockState blockState3, FluidState fluidState4) {
        try {
            this.liquidBlockRenderer.tesselate(blockAndTintGetter1, blockPos0, vertexConsumer2, blockState3, fluidState4);
        } catch (Throwable var9) {
            CrashReport $$6 = CrashReport.forThrowable(var9, "Tesselating liquid in world");
            CrashReportCategory $$7 = $$6.addCategory("Block being tesselated");
            CrashReportCategory.populateBlockDetails($$7, blockAndTintGetter1, blockPos0, null);
            throw new ReportedException($$6);
        }
    }

    public ModelBlockRenderer getModelRenderer() {
        return this.modelRenderer;
    }

    public BakedModel getBlockModel(BlockState blockState0) {
        return this.blockModelShaper.getBlockModel(blockState0);
    }

    public void renderSingleBlock(BlockState blockState0, PoseStack poseStack1, MultiBufferSource multiBufferSource2, int int3, int int4) {
        RenderShape $$5 = blockState0.m_60799_();
        if ($$5 != RenderShape.INVISIBLE) {
            switch($$5) {
                case MODEL:
                    BakedModel $$6 = this.getBlockModel(blockState0);
                    int $$7 = this.blockColors.getColor(blockState0, null, null, 0);
                    float $$8 = (float) ($$7 >> 16 & 0xFF) / 255.0F;
                    float $$9 = (float) ($$7 >> 8 & 0xFF) / 255.0F;
                    float $$10 = (float) ($$7 & 0xFF) / 255.0F;
                    this.modelRenderer.renderModel(poseStack1.last(), multiBufferSource2.getBuffer(ItemBlockRenderTypes.getRenderType(blockState0, false)), blockState0, $$6, $$8, $$9, $$10, int3, int4);
                    break;
                case ENTITYBLOCK_ANIMATED:
                    this.blockEntityRenderer.renderByItem(new ItemStack(blockState0.m_60734_()), ItemDisplayContext.NONE, poseStack1, multiBufferSource2, int3, int4);
            }
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager0) {
        this.liquidBlockRenderer.setupSprites();
    }
}