package com.simibubi.create.content.fluids.pipes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.PipeConnection;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.fluids.FluidStack;

public class TransparentStraightPipeRenderer extends SafeBlockEntityRenderer<StraightPipeBlockEntity> {

    public TransparentStraightPipeRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(StraightPipeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        FluidTransportBehaviour pipe = be.getBehaviour(FluidTransportBehaviour.TYPE);
        if (pipe != null) {
            for (Direction side : Iterate.directions) {
                PipeConnection.Flow flow = pipe.getFlow(side);
                if (flow != null) {
                    FluidStack fluidStack = flow.fluid;
                    if (!fluidStack.isEmpty()) {
                        LerpedFloat progress = flow.progress;
                        if (progress != null) {
                            float value = progress.getValue(partialTicks);
                            boolean inbound = flow.inbound;
                            if (value == 1.0F) {
                                if (inbound) {
                                    PipeConnection.Flow opposite = pipe.getFlow(side.getOpposite());
                                    if (opposite == null) {
                                        value -= 1.0E-6F;
                                    }
                                } else {
                                    FluidTransportBehaviour adjacent = BlockEntityBehaviour.get(be.m_58904_(), be.m_58899_().relative(side), FluidTransportBehaviour.TYPE);
                                    if (adjacent == null) {
                                        value -= 1.0E-6F;
                                    } else {
                                        PipeConnection.Flow other = adjacent.getFlow(side.getOpposite());
                                        if (other == null || !other.inbound && !other.complete) {
                                            value -= 1.0E-6F;
                                        }
                                    }
                                }
                            }
                            FluidRenderer.renderFluidStream(fluidStack, side, 0.1875F, value, inbound, buffer, ms, light);
                        }
                    }
                }
            }
        }
    }
}