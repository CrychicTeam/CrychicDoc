package com.simibubi.create.content.kinetics.press;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MechanicalPressRenderer extends KineticBlockEntityRenderer<MechanicalPressBlockEntity> {

    public MechanicalPressRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public boolean shouldRenderOffScreen(MechanicalPressBlockEntity be) {
        return true;
    }

    protected void renderSafe(MechanicalPressBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        if (!Backend.canUseInstancing(be.m_58904_())) {
            BlockState blockState = be.m_58900_();
            PressingBehaviour pressingBehaviour = be.getPressingBehaviour();
            float renderedHeadOffset = pressingBehaviour.getRenderedHeadOffset(partialTicks) * pressingBehaviour.mode.headOffset;
            SuperByteBuffer headRender = CachedBufferer.partialFacing(AllPartialModels.MECHANICAL_PRESS_HEAD, blockState, (Direction) blockState.m_61143_(BlockStateProperties.HORIZONTAL_FACING));
            headRender.translate(0.0, (double) (-renderedHeadOffset), 0.0).light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
        }
    }

    protected BlockState getRenderedBlockState(MechanicalPressBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }
}