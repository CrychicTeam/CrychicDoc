package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.rekindled.embers.blockentity.AutomaticHammerBlockEntity;
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

public class AutomaticHammerBlockEntityRenderer implements BlockEntityRenderer<AutomaticHammerBlockEntity> {

    public static BakedModel hammer;

    public AutomaticHammerBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public void render(AutomaticHammerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
        Level level = blockEntity.m_58904_();
        BlockState blockState = level.getBlockState(blockEntity.m_58899_());
        if (blockState.m_61138_(BlockStateProperties.HORIZONTAL_FACING)) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            Direction facing = (Direction) blockState.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
            if (facing.getAxis() == Direction.Axis.Z) {
                poseStack.mulPose(Axis.YP.rotationDegrees((float) (facing.get2DDataValue() * 90 + 180)));
            } else {
                poseStack.mulPose(Axis.YP.rotationDegrees((float) (facing.get2DDataValue() * 90)));
            }
            poseStack.translate(0.0, 0.0, -0.25);
            float hammerAngle = 0.0F;
            double processTicks = (double) ((float) (blockEntity.startTime + (long) blockEntity.processTime - level.getGameTime()) - partialTick);
            if (processTicks > (double) ((float) blockEntity.processTime / 2.0F)) {
                hammerAngle = (float) (-90.0 * (1.0 + 1.0 * Math.cos(processTicks * Math.PI / (double) blockEntity.processTime)));
            } else if (processTicks > 0.0) {
                hammerAngle = (float) (-90.0 * (1.0 - 1.0 * Math.cos(processTicks * Math.PI / (double) blockEntity.processTime)));
            }
            poseStack.mulPose(Axis.XP.rotationDegrees(hammerAngle));
            poseStack.translate(-0.5, -0.5, -0.5);
            if (hammer != null) {
                blockrendererdispatcher.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(Sheets.solidBlockSheet()), blockState, hammer, 0.0F, 0.0F, 0.0F, packedLight, packedOverlay, ModelData.EMPTY, Sheets.solidBlockSheet());
            }
            poseStack.popPose();
        }
    }
}