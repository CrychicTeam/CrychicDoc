package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.rekindled.embers.blockentity.InfernoForgeTopBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.data.ModelData;

public class InfernoForgeTopBlockEntityRenderer implements BlockEntityRenderer<InfernoForgeTopBlockEntity> {

    public static BakedModel hatch;

    public InfernoForgeTopBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public void render(InfernoForgeTopBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
        Level level = blockEntity.m_58904_();
        BlockState blockState = level.getBlockState(blockEntity.m_58899_());
        if (blockState.m_61138_(BlockStateProperties.HORIZONTAL_AXIS)) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            Direction.Axis axis = (Direction.Axis) blockState.m_61143_(BlockStateProperties.HORIZONTAL_AXIS);
            if (axis == Direction.Axis.Z) {
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            }
            double openTime = 7.0;
            double openAmount = 0.0;
            double openTicks = (double) level.getGameTime() + (double) partialTick - (double) blockEntity.lastToggle;
            if (openTicks <= 0.0) {
                if (!blockEntity.open) {
                    openAmount = 1.0;
                }
            } else if (openTicks <= openTime) {
                if (blockEntity.open) {
                    openAmount = Math.sin(openTicks * Math.PI / (openTime * 2.0));
                } else {
                    openAmount = 1.0 - Math.sin(openTicks * Math.PI / (openTime * 2.0));
                }
            } else if (blockEntity.open) {
                openAmount = 1.0;
            }
            poseStack.translate(-0.5, -0.5, -0.5);
            if (hatch != null) {
                poseStack.translate(0.0, 0.0, -openAmount * 0.4375);
                blockrendererdispatcher.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(Sheets.solidBlockSheet()), blockState, hatch, 0.0F, 0.0F, 0.0F, packedLight, packedOverlay, ModelData.EMPTY, Sheets.solidBlockSheet());
                poseStack.translate(1.0, 0.0, 1.0 + openAmount * 2.0 * 0.4375);
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                blockrendererdispatcher.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(Sheets.solidBlockSheet()), blockState, hatch, 0.0F, 0.0F, 0.0F, packedLight, packedOverlay, ModelData.EMPTY, Sheets.solidBlockSheet());
            }
            poseStack.popPose();
        }
    }
}