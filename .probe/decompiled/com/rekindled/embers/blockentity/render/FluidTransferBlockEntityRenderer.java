package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rekindled.embers.blockentity.FluidTransferBlockEntity;
import com.rekindled.embers.render.FluidCuboid;
import com.rekindled.embers.render.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.joml.Vector3f;

public class FluidTransferBlockEntityRenderer implements BlockEntityRenderer<FluidTransferBlockEntity> {

    FluidCuboid cube = new FluidCuboid(new Vector3f(4.0F, 4.0F, 4.0F), new Vector3f(12.0F, 12.0F, 12.0F), FluidCuboid.DEFAULT_FACES);

    public FluidTransferBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public void render(FluidTransferBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity != null && !blockEntity.filterFluid.isEmpty()) {
            FluidRenderer.renderScaledCuboid(poseStack, bufferSource, this.cube, blockEntity.filterFluid, 0.0F, blockEntity.filterFluid.getAmount(), packedLight, packedOverlay, false);
        }
    }
}