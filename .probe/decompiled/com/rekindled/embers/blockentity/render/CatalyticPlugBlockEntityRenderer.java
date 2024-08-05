package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.rekindled.embers.blockentity.CatalyticPlugBlockEntity;
import com.rekindled.embers.render.FluidCuboid;
import com.rekindled.embers.render.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Vector3f;

public class CatalyticPlugBlockEntityRenderer implements BlockEntityRenderer<CatalyticPlugBlockEntity> {

    FluidCuboid cube = new FluidCuboid(new Vector3f(0.0F, 0.499F, 0.0F), new Vector3f(3.0F, 6.0F, 3.0F), FluidCuboid.FLOWING_DOWN_FACES);

    public CatalyticPlugBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public void render(CatalyticPlugBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity != null && blockEntity.m_58904_().getBlockState(blockEntity.m_58899_()).m_61138_(BlockStateProperties.FACING)) {
            FluidStack fluidStack = blockEntity.tank.getFluid();
            int capacity = blockEntity.tank.getCapacity();
            if (!fluidStack.isEmpty() && capacity > 0) {
                float offset = blockEntity.renderOffset;
                if (!(offset > 1.2F) && !(offset < -1.2F)) {
                    blockEntity.renderOffset = 0.0F;
                } else {
                    offset -= (offset / 12.0F + 0.1F) * partialTick;
                    blockEntity.renderOffset = offset;
                }
                Direction facing = (Direction) blockEntity.m_58904_().getBlockState(blockEntity.m_58899_()).m_61143_(BlockStateProperties.FACING);
                RenderSystem.enableCull();
                poseStack.pushPose();
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(facing.getRotation());
                poseStack.translate(-0.5, -0.5, -0.5);
                poseStack.pushPose();
                poseStack.translate(0.5, 0.25, 0.1875);
                poseStack.mulPose(Axis.XP.rotationDegrees(-22.5F));
                poseStack.translate(-0.09375, 0.09375, -0.09375);
                FluidRenderer.renderScaledCuboid(poseStack, bufferSource, this.cube, fluidStack, offset, capacity, packedLight, packedOverlay, false);
                poseStack.popPose();
                poseStack.pushPose();
                poseStack.translate(0.1875, 0.25, 0.5);
                poseStack.mulPose(Axis.ZP.rotationDegrees(22.5F));
                poseStack.translate(-0.09375, 0.09375, -0.09375);
                FluidRenderer.renderScaledCuboid(poseStack, bufferSource, this.cube, fluidStack, offset, capacity, packedLight, packedOverlay, false);
                poseStack.popPose();
                poseStack.pushPose();
                poseStack.translate(0.5, 0.25, 0.8125);
                poseStack.mulPose(Axis.XP.rotationDegrees(22.5F));
                poseStack.translate(-0.09375, 0.09375, -0.09375);
                FluidRenderer.renderScaledCuboid(poseStack, bufferSource, this.cube, fluidStack, offset, capacity, packedLight, packedOverlay, false);
                poseStack.popPose();
                poseStack.pushPose();
                poseStack.translate(0.8125, 0.25, 0.5);
                poseStack.mulPose(Axis.ZP.rotationDegrees(-22.5F));
                poseStack.translate(-0.09375, 0.09375, -0.09375);
                FluidRenderer.renderScaledCuboid(poseStack, bufferSource, this.cube, fluidStack, offset, capacity, packedLight, packedOverlay, false);
                poseStack.popPose();
                poseStack.popPose();
            } else {
                blockEntity.renderOffset = 0.0F;
            }
        }
    }
}