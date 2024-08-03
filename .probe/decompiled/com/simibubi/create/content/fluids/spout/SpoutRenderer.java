package com.simibubi.create.content.fluids.spout;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;

public class SpoutRenderer extends SafeBlockEntityRenderer<SpoutBlockEntity> {

    static final PartialModel[] BITS = new PartialModel[] { AllPartialModels.SPOUT_TOP, AllPartialModels.SPOUT_MIDDLE, AllPartialModels.SPOUT_BOTTOM };

    public SpoutRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(SpoutBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        SmartFluidTankBehaviour tank = be.tank;
        if (tank != null) {
            SmartFluidTankBehaviour.TankSegment primaryTank = tank.getPrimaryTank();
            FluidStack fluidStack = primaryTank.getRenderedFluid();
            float level = primaryTank.getFluidLevel().getValue(partialTicks);
            if (!fluidStack.isEmpty() && level != 0.0F) {
                boolean top = fluidStack.getFluid().getFluidType().isLighterThanAir();
                level = Math.max(level, 0.175F);
                float min = 0.15625F;
                float max = min + 0.6875F;
                float yOffset = 0.6875F * level;
                ms.pushPose();
                if (!top) {
                    ms.translate(0.0F, yOffset, 0.0F);
                } else {
                    ms.translate(0.0F, max - min, 0.0F);
                }
                FluidRenderer.renderFluidBox(fluidStack, min, min - yOffset, min, max, min, max, buffer, ms, light, false);
                ms.popPose();
            }
            int processingTicks = be.processingTicks;
            float processingPT = (float) processingTicks - partialTicks;
            float processingProgress = 1.0F - (processingPT - 5.0F) / 10.0F;
            processingProgress = Mth.clamp(processingProgress, 0.0F, 1.0F);
            float radius = 0.0F;
            if (processingTicks != -1) {
                radius = (float) (Math.pow((double) (2.0F * processingProgress - 1.0F), 2.0) - 1.0);
                AABB bb = new AABB(0.5, 0.5, 0.5, 0.5, -1.2, 0.5).inflate((double) (radius / 32.0F));
                FluidRenderer.renderFluidBox(fluidStack, (float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ, buffer, ms, light, true);
            }
            float squeeze = radius;
            if (processingPT < 0.0F) {
                squeeze = 0.0F;
            } else if (processingPT < 2.0F) {
                squeeze = Mth.lerp(processingPT / 2.0F, 0.0F, -1.0F);
            } else if (processingPT < 10.0F) {
                squeeze = -1.0F;
            }
            ms.pushPose();
            for (PartialModel bit : BITS) {
                CachedBufferer.partial(bit, be.m_58900_()).light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
                ms.translate(0.0F, -3.0F * squeeze / 32.0F, 0.0F);
            }
            ms.popPose();
        }
    }
}