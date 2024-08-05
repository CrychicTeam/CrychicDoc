package com.simibubi.create.content.kinetics.crank;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class HandCrankRenderer extends KineticBlockEntityRenderer<HandCrankBlockEntity> {

    public HandCrankRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(HandCrankBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (be.shouldRenderShaft()) {
            super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        }
        if (!Backend.canUseInstancing(be.m_58904_())) {
            Direction facing = (Direction) be.m_58900_().m_61143_(BlockStateProperties.FACING);
            kineticRotationTransform(be.getRenderedHandle(), be, facing.getAxis(), be.getIndependentAngle(partialTicks), light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
        }
    }
}