package com.simibubi.create.content.kinetics.millstone;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;

public class MillstoneCogInstance extends SingleRotatingInstance<MillstoneBlockEntity> {

    public MillstoneCogInstance(MaterialManager materialManager, MillstoneBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        return this.getRotatingMaterial().getModel(AllPartialModels.MILLSTONE_COG, ((MillstoneBlockEntity) this.blockEntity).m_58900_());
    }
}