package com.simibubi.create.content.contraptions.pulley;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlockEntity;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

public class HosePulleyInstance extends AbstractPulleyInstance<HosePulleyBlockEntity> {

    public HosePulleyInstance(MaterialManager dispatcher, HosePulleyBlockEntity blockEntity) {
        super(dispatcher, blockEntity);
    }

    @Override
    protected Instancer<OrientedData> getRopeModel() {
        return this.getOrientedMaterial().getModel(AllPartialModels.HOSE, this.blockState);
    }

    @Override
    protected Instancer<OrientedData> getMagnetModel() {
        return this.materialManager.defaultCutout().material(Materials.ORIENTED).getModel(AllPartialModels.HOSE_MAGNET, this.blockState);
    }

    @Override
    protected Instancer<OrientedData> getHalfMagnetModel() {
        return this.materialManager.defaultCutout().material(Materials.ORIENTED).getModel(AllPartialModels.HOSE_HALF_MAGNET, this.blockState);
    }

    @Override
    protected Instancer<OrientedData> getCoilModel() {
        return this.getOrientedMaterial().getModel(AllPartialModels.HOSE_COIL, this.blockState, this.rotatingAbout);
    }

    @Override
    protected Instancer<OrientedData> getHalfRopeModel() {
        return this.getOrientedMaterial().getModel(AllPartialModels.HOSE_HALF, this.blockState);
    }

    @Override
    protected float getOffset() {
        return ((HosePulleyBlockEntity) this.blockEntity).getInterpolatedOffset(AnimationTickHolder.getPartialTicks());
    }

    @Override
    protected boolean isRunning() {
        return true;
    }
}