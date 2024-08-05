package com.simibubi.create.content.contraptions.elevator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.contraptions.pulley.AbstractPulleyRenderer;
import com.simibubi.create.content.contraptions.pulley.PulleyRenderer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ElevatorPulleyRenderer extends KineticBlockEntityRenderer<ElevatorPulleyBlockEntity> {

    public ElevatorPulleyRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(ElevatorPulleyBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState state = this.getRenderedBlockState(be);
        RenderType type = this.getRenderType(be, state);
        if (type != null) {
            renderRotatingBuffer(be, this.getRotatedModel(be, state), ms, buffer.getBuffer(type), light);
        }
        float offset = PulleyRenderer.getBlockEntityOffset(partialTicks, be);
        boolean running = PulleyRenderer.isPulleyRunning(be);
        SpriteShiftEntry beltShift = AllSpriteShifts.ELEVATOR_BELT;
        SpriteShiftEntry coilShift = AllSpriteShifts.ELEVATOR_COIL;
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        Level world = be.m_58904_();
        BlockState blockState = be.m_58900_();
        BlockPos pos = be.m_58899_();
        float blockStateAngle = 180.0F + AngleHelper.horizontalAngle((Direction) blockState.m_61143_(ElevatorPulleyBlock.HORIZONTAL_FACING));
        SuperByteBuffer magnet = CachedBufferer.partial(AllPartialModels.ELEVATOR_MAGNET, blockState);
        if (running || offset == 0.0F) {
            AbstractPulleyRenderer.renderAt(world, (SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) magnet.centre()).rotateY((double) blockStateAngle)).unCentre(), offset, pos, ms, vb);
        }
        SuperByteBuffer rotatedCoil = this.getRotatedCoil(be);
        if (offset == 0.0F) {
            rotatedCoil.light(light).renderInto(ms, vb);
        } else {
            float spriteSize = beltShift.getTarget().getV1() - beltShift.getTarget().getV0();
            double coilScroll = (double) (-(offset + 0.1875F)) - Math.floor((double) ((offset + 0.1875F) * -2.0F)) / 2.0;
            double beltScroll = (-((double) offset + 0.5) - Math.floor(-((double) offset + 0.5))) / 2.0;
            rotatedCoil.shiftUVScrolling(coilShift, (float) coilScroll * spriteSize).light(light).renderInto(ms, vb);
            SuperByteBuffer halfRope = CachedBufferer.partial(AllPartialModels.ELEVATOR_BELT_HALF, blockState);
            SuperByteBuffer rope = CachedBufferer.partial(AllPartialModels.ELEVATOR_BELT, blockState);
            float f = offset % 1.0F;
            if (f < 0.25F || f > 0.75F) {
                ((SuperByteBuffer) ((SuperByteBuffer) halfRope.centre()).rotateY((double) blockStateAngle)).unCentre();
                AbstractPulleyRenderer.renderAt(world, halfRope.shiftUVScrolling(beltShift, (float) beltScroll * spriteSize), f > 0.75F ? f - 1.0F : f, pos, ms, vb);
            }
            if (running) {
                for (int i = 0; (float) i < offset - 0.25F; i++) {
                    ((SuperByteBuffer) ((SuperByteBuffer) rope.centre()).rotateY((double) blockStateAngle)).unCentre();
                    AbstractPulleyRenderer.renderAt(world, rope.shiftUVScrolling(beltShift, (float) beltScroll * spriteSize), offset - (float) i, pos, ms, vb);
                }
            }
        }
    }

    protected BlockState getRenderedBlockState(ElevatorPulleyBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }

    protected SuperByteBuffer getRotatedCoil(KineticBlockEntity be) {
        BlockState blockState = be.m_58900_();
        return CachedBufferer.partialFacing(AllPartialModels.ELEVATOR_COIL, blockState, (Direction) blockState.m_61143_(ElevatorPulleyBlock.HORIZONTAL_FACING));
    }

    @Override
    public int getViewDistance() {
        return 128;
    }

    public boolean shouldRenderOffScreen(ElevatorPulleyBlockEntity p_188185_1_) {
        return true;
    }
}