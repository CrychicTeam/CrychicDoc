package com.simibubi.create.content.fluids.tank;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidTankRenderer extends SafeBlockEntityRenderer<FluidTankBlockEntity> {

    public FluidTankRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(FluidTankBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (be.isController()) {
            if (!be.window) {
                if (be.boiler.isActive()) {
                    this.renderAsBoiler(be, partialTicks, ms, buffer, light, overlay);
                }
            } else {
                LerpedFloat fluidLevel = be.getFluidLevel();
                if (fluidLevel != null) {
                    float capHeight = 0.25F;
                    float tankHullWidth = 0.0703125F;
                    float minPuddleHeight = 0.0625F;
                    float totalHeight = (float) be.height - 2.0F * capHeight - minPuddleHeight;
                    float level = fluidLevel.getValue(partialTicks);
                    if (!(level < 1.0F / (512.0F * totalHeight))) {
                        float clampedLevel = Mth.clamp(level * totalHeight, 0.0F, totalHeight);
                        FluidTank tank = be.tankInventory;
                        FluidStack fluidStack = tank.getFluid();
                        if (!fluidStack.isEmpty()) {
                            boolean top = fluidStack.getFluid().getFluidType().isLighterThanAir();
                            float xMax = tankHullWidth + (float) be.width - 2.0F * tankHullWidth;
                            float yMin = totalHeight + capHeight + minPuddleHeight - clampedLevel;
                            float yMax = yMin + clampedLevel;
                            if (top) {
                                yMin += totalHeight - clampedLevel;
                                yMax += totalHeight - clampedLevel;
                            }
                            float zMax = tankHullWidth + (float) be.width - 2.0F * tankHullWidth;
                            ms.pushPose();
                            ms.translate(0.0F, clampedLevel - totalHeight, 0.0F);
                            FluidRenderer.renderFluidBox(fluidStack, tankHullWidth, yMin, tankHullWidth, xMax, yMax, zMax, buffer, ms, light, false);
                            ms.popPose();
                        }
                    }
                }
            }
        }
    }

    protected void renderAsBoiler(FluidTankBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = be.m_58900_();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        ms.pushPose();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate((double) ((float) be.width / 2.0F), 0.5, (double) ((float) be.width / 2.0F));
        float dialPivot = 0.359375F;
        float progress = be.boiler.gauge.getValue(partialTicks);
        for (Direction d : Iterate.horizontalDirections) {
            ms.pushPose();
            ((SuperByteBuffer) ((SuperByteBuffer) CachedBufferer.partial(AllPartialModels.BOILER_GAUGE, blockState).rotateY((double) d.toYRot())).unCentre()).translate((double) ((float) be.width / 2.0F - 0.375F), 0.0, 0.0).light(light).renderInto(ms, vb);
            ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) CachedBufferer.partial(AllPartialModels.BOILER_GAUGE_DIAL, blockState).rotateY((double) d.toYRot())).unCentre()).translate((double) ((float) be.width / 2.0F - 0.375F), 0.0, 0.0).translate(0.0, (double) dialPivot, (double) dialPivot).rotateX((double) (-90.0F * progress))).translate(0.0, (double) (-dialPivot), (double) (-dialPivot)).light(light).renderInto(ms, vb);
            ms.popPose();
        }
        ms.popPose();
    }

    public boolean shouldRenderOffScreen(FluidTankBlockEntity be) {
        return be.isController();
    }
}