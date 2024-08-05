package com.simibubi.create.content.contraptions.pulley;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class PulleyRenderer extends AbstractPulleyRenderer<PulleyBlockEntity> {

    public PulleyRenderer(BlockEntityRendererProvider.Context context) {
        super(context, AllPartialModels.ROPE_HALF, AllPartialModels.ROPE_HALF_MAGNET);
    }

    protected Direction.Axis getShaftAxis(PulleyBlockEntity be) {
        return (Direction.Axis) be.m_58900_().m_61143_(PulleyBlock.HORIZONTAL_AXIS);
    }

    @Override
    protected PartialModel getCoil() {
        return AllPartialModels.ROPE_COIL;
    }

    protected SuperByteBuffer renderRope(PulleyBlockEntity be) {
        return CachedBufferer.block(AllBlocks.ROPE.getDefaultState());
    }

    protected SuperByteBuffer renderMagnet(PulleyBlockEntity be) {
        return CachedBufferer.block(AllBlocks.PULLEY_MAGNET.getDefaultState());
    }

    protected float getOffset(PulleyBlockEntity be, float partialTicks) {
        return getBlockEntityOffset(partialTicks, be);
    }

    protected boolean isRunning(PulleyBlockEntity be) {
        return isPulleyRunning(be);
    }

    public static boolean isPulleyRunning(PulleyBlockEntity be) {
        return be.running || be.mirrorParent != null || be.isVirtual();
    }

    public static float getBlockEntityOffset(float partialTicks, PulleyBlockEntity blockEntity) {
        float offset = blockEntity.getInterpolatedOffset(partialTicks);
        AbstractContraptionEntity attachedContraption = blockEntity.getAttachedContraption();
        if (attachedContraption != null) {
            PulleyContraption c = (PulleyContraption) attachedContraption.getContraption();
            double entityPos = Mth.lerp((double) partialTicks, attachedContraption.f_19791_, attachedContraption.m_20186_());
            offset = (float) (-(entityPos - (double) c.anchor.m_123342_() - (double) c.getInitialOffset()));
        }
        return offset;
    }

    @Override
    public int getViewDistance() {
        return 128;
    }
}