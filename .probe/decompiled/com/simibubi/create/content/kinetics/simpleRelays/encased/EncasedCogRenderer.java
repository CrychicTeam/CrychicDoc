package com.simibubi.create.content.kinetics.simpleRelays.encased;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class EncasedCogRenderer extends KineticBlockEntityRenderer<SimpleKineticBlockEntity> {

    private boolean large;

    public static EncasedCogRenderer small(BlockEntityRendererProvider.Context context) {
        return new EncasedCogRenderer(context, false);
    }

    public static EncasedCogRenderer large(BlockEntityRendererProvider.Context context) {
        return new EncasedCogRenderer(context, true);
    }

    public EncasedCogRenderer(BlockEntityRendererProvider.Context context, boolean large) {
        super(context);
        this.large = large;
    }

    protected void renderSafe(SimpleKineticBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        if (!Backend.canUseInstancing(be.m_58904_())) {
            BlockState blockState = be.m_58900_();
            if (blockState.m_60734_() instanceof IRotate def) {
                Direction.Axis axis = getRotationAxisOf(be);
                BlockPos pos = be.m_58899_();
                float angle = this.large ? BracketedKineticBlockEntityRenderer.getAngleForLargeCogShaft(be, axis) : getAngleForTe(be, pos, axis);
                for (Direction d : Iterate.directionsInAxis(getRotationAxisOf(be))) {
                    if (def.hasShaftTowards(be.m_58904_(), be.m_58899_(), blockState, d)) {
                        SuperByteBuffer shaft = CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, be.m_58900_(), d);
                        kineticRotationTransform(shaft, be, axis, angle, light);
                        shaft.renderInto(ms, buffer.getBuffer(RenderType.solid()));
                    }
                }
            }
        }
    }

    protected SuperByteBuffer getRotatedModel(SimpleKineticBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacingVertical(this.large ? AllPartialModels.SHAFTLESS_LARGE_COGWHEEL : AllPartialModels.SHAFTLESS_COGWHEEL, state, Direction.fromAxisAndDirection((Direction.Axis) state.m_61143_(EncasedCogwheelBlock.AXIS), Direction.AxisDirection.POSITIVE));
    }
}