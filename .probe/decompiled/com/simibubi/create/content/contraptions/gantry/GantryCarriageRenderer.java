package com.simibubi.create.content.contraptions.gantry;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class GantryCarriageRenderer extends KineticBlockEntityRenderer<GantryCarriageBlockEntity> {

    public GantryCarriageRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(GantryCarriageBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        if (!Backend.canUseInstancing(be.m_58904_())) {
            BlockState state = be.m_58900_();
            Direction facing = (Direction) state.m_61143_(GantryCarriageBlock.FACING);
            Boolean alongFirst = (Boolean) state.m_61143_(GantryCarriageBlock.AXIS_ALONG_FIRST_COORDINATE);
            Direction.Axis rotationAxis = getRotationAxisOf(be);
            BlockPos visualPos = facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? be.m_58899_() : be.m_58899_().relative(facing.getOpposite());
            float angleForBE = getAngleForBE(be, visualPos, rotationAxis);
            Direction.Axis gantryAxis = Direction.Axis.X;
            for (Direction.Axis axis : Iterate.axes) {
                if (axis != rotationAxis && axis != facing.getAxis()) {
                    gantryAxis = axis;
                }
            }
            if (gantryAxis == Direction.Axis.X && facing == Direction.UP) {
                angleForBE *= -1.0F;
            }
            if (gantryAxis == Direction.Axis.Y && (facing == Direction.NORTH || facing == Direction.EAST)) {
                angleForBE *= -1.0F;
            }
            SuperByteBuffer cogs = CachedBufferer.partial(AllPartialModels.GANTRY_COGS, state);
            ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) cogs.centre()).rotateY((double) AngleHelper.horizontalAngle(facing))).rotateX(facing == Direction.UP ? 0.0 : (facing == Direction.DOWN ? 180.0 : 90.0))).rotateY(alongFirst ^ facing.getAxis() == Direction.Axis.X ? 0.0 : 90.0)).translate(0.0, -0.5625, 0.0).rotateX((double) (-angleForBE))).translate(0.0, 0.5625, 0.0).unCentre();
            cogs.light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
        }
    }

    public static float getAngleForBE(KineticBlockEntity be, BlockPos pos, Direction.Axis axis) {
        float time = AnimationTickHolder.getRenderTime(be.m_58904_());
        float offset = getRotationOffsetForPosition(be, pos, axis);
        return (time * be.getSpeed() * 3.0F / 20.0F + offset) % 360.0F;
    }

    protected BlockState getRenderedBlockState(GantryCarriageBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }
}