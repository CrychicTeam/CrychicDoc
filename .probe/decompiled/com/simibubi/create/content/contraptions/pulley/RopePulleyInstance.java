package com.simibubi.create.content.contraptions.pulley;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

public class RopePulleyInstance extends AbstractPulleyInstance<PulleyBlockEntity> {

    public RopePulleyInstance(MaterialManager materialManager, PulleyBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    protected Instancer<OrientedData> getRopeModel() {
        return this.getOrientedMaterial().getModel(AllBlocks.ROPE.getDefaultState());
    }

    @Override
    protected Instancer<OrientedData> getMagnetModel() {
        return this.getOrientedMaterial().getModel(AllBlocks.PULLEY_MAGNET.getDefaultState());
    }

    @Override
    protected Instancer<OrientedData> getHalfMagnetModel() {
        return this.getOrientedMaterial().getModel(AllPartialModels.ROPE_HALF_MAGNET, this.blockState);
    }

    @Override
    protected Instancer<OrientedData> getCoilModel() {
        return this.getOrientedMaterial().getModel(AllPartialModels.ROPE_COIL, this.blockState, this.rotatingAbout);
    }

    @Override
    protected Instancer<OrientedData> getHalfRopeModel() {
        return this.getOrientedMaterial().getModel(AllPartialModels.ROPE_HALF, this.blockState);
    }

    @Override
    protected float getOffset() {
        float partialTicks = AnimationTickHolder.getPartialTicks();
        return PulleyRenderer.getBlockEntityOffset(partialTicks, (PulleyBlockEntity) this.blockEntity);
    }

    @Override
    protected boolean isRunning() {
        return PulleyRenderer.isPulleyRunning((PulleyBlockEntity) this.blockEntity);
    }
}