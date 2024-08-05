package com.simibubi.create.content.fluids.hosePulley;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.pulley.AbstractPulleyRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class HosePulleyRenderer extends AbstractPulleyRenderer<HosePulleyBlockEntity> {

    public HosePulleyRenderer(BlockEntityRendererProvider.Context context) {
        super(context, AllPartialModels.HOSE_HALF, AllPartialModels.HOSE_HALF_MAGNET);
    }

    protected Direction.Axis getShaftAxis(HosePulleyBlockEntity be) {
        return ((Direction) be.m_58900_().m_61143_(HosePulleyBlock.HORIZONTAL_FACING)).getClockWise().getAxis();
    }

    @Override
    protected PartialModel getCoil() {
        return AllPartialModels.HOSE_COIL;
    }

    protected SuperByteBuffer renderRope(HosePulleyBlockEntity be) {
        return CachedBufferer.partial(AllPartialModels.HOSE, be.m_58900_());
    }

    protected SuperByteBuffer renderMagnet(HosePulleyBlockEntity be) {
        return CachedBufferer.partial(AllPartialModels.HOSE_MAGNET, be.m_58900_());
    }

    protected float getOffset(HosePulleyBlockEntity be, float partialTicks) {
        return be.getInterpolatedOffset(partialTicks);
    }

    protected boolean isRunning(HosePulleyBlockEntity be) {
        return true;
    }
}