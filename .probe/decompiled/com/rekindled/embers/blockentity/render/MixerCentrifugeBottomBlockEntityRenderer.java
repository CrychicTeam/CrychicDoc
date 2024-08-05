package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rekindled.embers.blockentity.MixerCentrifugeBottomBlockEntity;
import com.rekindled.embers.render.FluidCuboid;
import com.rekindled.embers.render.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.joml.Vector3f;

public class MixerCentrifugeBottomBlockEntityRenderer implements BlockEntityRenderer<MixerCentrifugeBottomBlockEntity> {

    FluidCuboid cubeNorth = new FluidCuboid(new Vector3f(7.0F, 12.0F, 3.0F), new Vector3f(9.0F, 15.0F, 4.0F), FluidCuboid.DEFAULT_FACES);

    FluidCuboid cubeSouth = new FluidCuboid(new Vector3f(7.0F, 12.0F, 12.0F), new Vector3f(9.0F, 15.0F, 13.0F), FluidCuboid.DEFAULT_FACES);

    FluidCuboid cubeEast = new FluidCuboid(new Vector3f(12.0F, 12.0F, 7.0F), new Vector3f(13.0F, 15.0F, 9.0F), FluidCuboid.DEFAULT_FACES);

    FluidCuboid cubeWest = new FluidCuboid(new Vector3f(3.0F, 12.0F, 7.0F), new Vector3f(4.0F, 15.0F, 9.0F), FluidCuboid.DEFAULT_FACES);

    FluidCuboid[] cubes = new FluidCuboid[] { this.cubeNorth, this.cubeSouth, this.cubeEast, this.cubeWest };

    public MixerCentrifugeBottomBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public void render(MixerCentrifugeBottomBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity != null) {
            for (int i = 0; i < blockEntity.getTanks().length; i++) {
                MixerCentrifugeBottomBlockEntity.MixerFluidTank fluidStack = blockEntity.getTanks()[i];
                int capacity = fluidStack.getCapacity();
                if (!fluidStack.isEmpty() && capacity > 0) {
                    float offset = fluidStack.renderOffset;
                    if (!(offset > 1.2F) && !(offset < -1.2F)) {
                        fluidStack.renderOffset = 0.0F;
                    } else {
                        offset -= (offset / 12.0F + 0.1F) * partialTick;
                        fluidStack.renderOffset = offset;
                    }
                    FluidRenderer.renderScaledCuboid(poseStack, bufferSource, this.cubes[i], fluidStack.getFluid(), offset, capacity, packedLight, packedOverlay, false);
                } else {
                    fluidStack.renderOffset = 0.0F;
                }
            }
        }
    }
}