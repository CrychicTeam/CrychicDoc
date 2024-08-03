package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;

public class StructureBlockRenderer implements BlockEntityRenderer<StructureBlockEntity> {

    public StructureBlockRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
    }

    public void render(StructureBlockEntity structureBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        if (Minecraft.getInstance().player.m_36337_() || Minecraft.getInstance().player.m_5833_()) {
            BlockPos $$6 = structureBlockEntity0.getStructurePos();
            Vec3i $$7 = structureBlockEntity0.getStructureSize();
            if ($$7.getX() >= 1 && $$7.getY() >= 1 && $$7.getZ() >= 1) {
                if (structureBlockEntity0.getMode() == StructureMode.SAVE || structureBlockEntity0.getMode() == StructureMode.LOAD) {
                    double $$8 = (double) $$6.m_123341_();
                    double $$9 = (double) $$6.m_123343_();
                    double $$10 = (double) $$6.m_123342_();
                    double $$11 = $$10 + (double) $$7.getY();
                    double $$12;
                    double $$13;
                    switch(structureBlockEntity0.getMirror()) {
                        case LEFT_RIGHT:
                            $$12 = (double) $$7.getX();
                            $$13 = (double) (-$$7.getZ());
                            break;
                        case FRONT_BACK:
                            $$12 = (double) (-$$7.getX());
                            $$13 = (double) $$7.getZ();
                            break;
                        default:
                            $$12 = (double) $$7.getX();
                            $$13 = (double) $$7.getZ();
                    }
                    double $$30;
                    double $$31;
                    double $$32;
                    double $$33;
                    switch(structureBlockEntity0.getRotation()) {
                        case CLOCKWISE_90:
                            $$30 = $$13 < 0.0 ? $$8 : $$8 + 1.0;
                            $$31 = $$12 < 0.0 ? $$9 + 1.0 : $$9;
                            $$32 = $$30 - $$13;
                            $$33 = $$31 + $$12;
                            break;
                        case CLOCKWISE_180:
                            $$30 = $$12 < 0.0 ? $$8 : $$8 + 1.0;
                            $$31 = $$13 < 0.0 ? $$9 : $$9 + 1.0;
                            $$32 = $$30 - $$12;
                            $$33 = $$31 - $$13;
                            break;
                        case COUNTERCLOCKWISE_90:
                            $$30 = $$13 < 0.0 ? $$8 + 1.0 : $$8;
                            $$31 = $$12 < 0.0 ? $$9 : $$9 + 1.0;
                            $$32 = $$30 + $$13;
                            $$33 = $$31 - $$12;
                            break;
                        default:
                            $$30 = $$12 < 0.0 ? $$8 + 1.0 : $$8;
                            $$31 = $$13 < 0.0 ? $$9 + 1.0 : $$9;
                            $$32 = $$30 + $$12;
                            $$33 = $$31 + $$13;
                    }
                    float $$34 = 1.0F;
                    float $$35 = 0.9F;
                    float $$36 = 0.5F;
                    VertexConsumer $$37 = multiBufferSource3.getBuffer(RenderType.lines());
                    if (structureBlockEntity0.getMode() == StructureMode.SAVE || structureBlockEntity0.getShowBoundingBox()) {
                        LevelRenderer.renderLineBox(poseStack2, $$37, $$30, $$10, $$31, $$32, $$11, $$33, 0.9F, 0.9F, 0.9F, 1.0F, 0.5F, 0.5F, 0.5F);
                    }
                    if (structureBlockEntity0.getMode() == StructureMode.SAVE && structureBlockEntity0.getShowAir()) {
                        this.renderInvisibleBlocks(structureBlockEntity0, $$37, $$6, poseStack2);
                    }
                }
            }
        }
    }

    private void renderInvisibleBlocks(StructureBlockEntity structureBlockEntity0, VertexConsumer vertexConsumer1, BlockPos blockPos2, PoseStack poseStack3) {
        BlockGetter $$4 = structureBlockEntity0.m_58904_();
        BlockPos $$5 = structureBlockEntity0.m_58899_();
        BlockPos $$6 = $$5.offset(blockPos2);
        for (BlockPos $$7 : BlockPos.betweenClosed($$6, $$6.offset(structureBlockEntity0.getStructureSize()).offset(-1, -1, -1))) {
            BlockState $$8 = $$4.getBlockState($$7);
            boolean $$9 = $$8.m_60795_();
            boolean $$10 = $$8.m_60713_(Blocks.STRUCTURE_VOID);
            boolean $$11 = $$8.m_60713_(Blocks.BARRIER);
            boolean $$12 = $$8.m_60713_(Blocks.LIGHT);
            boolean $$13 = $$10 || $$11 || $$12;
            if ($$9 || $$13) {
                float $$14 = $$9 ? 0.05F : 0.0F;
                double $$15 = (double) ((float) ($$7.m_123341_() - $$5.m_123341_()) + 0.45F - $$14);
                double $$16 = (double) ((float) ($$7.m_123342_() - $$5.m_123342_()) + 0.45F - $$14);
                double $$17 = (double) ((float) ($$7.m_123343_() - $$5.m_123343_()) + 0.45F - $$14);
                double $$18 = (double) ((float) ($$7.m_123341_() - $$5.m_123341_()) + 0.55F + $$14);
                double $$19 = (double) ((float) ($$7.m_123342_() - $$5.m_123342_()) + 0.55F + $$14);
                double $$20 = (double) ((float) ($$7.m_123343_() - $$5.m_123343_()) + 0.55F + $$14);
                if ($$9) {
                    LevelRenderer.renderLineBox(poseStack3, vertexConsumer1, $$15, $$16, $$17, $$18, $$19, $$20, 0.5F, 0.5F, 1.0F, 1.0F, 0.5F, 0.5F, 1.0F);
                } else if ($$10) {
                    LevelRenderer.renderLineBox(poseStack3, vertexConsumer1, $$15, $$16, $$17, $$18, $$19, $$20, 1.0F, 0.75F, 0.75F, 1.0F, 1.0F, 0.75F, 0.75F);
                } else if ($$11) {
                    LevelRenderer.renderLineBox(poseStack3, vertexConsumer1, $$15, $$16, $$17, $$18, $$19, $$20, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F);
                } else if ($$12) {
                    LevelRenderer.renderLineBox(poseStack3, vertexConsumer1, $$15, $$16, $$17, $$18, $$19, $$20, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F);
                }
            }
        }
    }

    public boolean shouldRenderOffScreen(StructureBlockEntity structureBlockEntity0) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 96;
    }
}