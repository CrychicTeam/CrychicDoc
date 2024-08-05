package com.simibubi.create.content.redstone.diodes;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.TickableInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.utility.Color;

public class BrassDiodeInstance extends BlockEntityInstance<BrassDiodeBlockEntity> implements TickableInstance {

    protected final ModelData indicator;

    protected int previousState;

    public BrassDiodeInstance(MaterialManager materialManager, BrassDiodeBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        this.indicator = (ModelData) materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(AllPartialModels.FLEXPEATER_INDICATOR, this.blockState).createInstance();
        ((ModelData) this.indicator.loadIdentity().translate(this.getInstancePosition())).setColor(this.getColor());
        this.previousState = blockEntity.state;
    }

    public void tick() {
        if (this.previousState != ((BrassDiodeBlockEntity) this.blockEntity).state) {
            this.indicator.setColor(this.getColor());
            this.previousState = ((BrassDiodeBlockEntity) this.blockEntity).state;
        }
    }

    public void updateLight() {
        this.relight(this.pos, new FlatLit[] { this.indicator });
    }

    public void remove() {
        this.indicator.delete();
    }

    protected int getColor() {
        return Color.mixColors(2884352, 13434880, ((BrassDiodeBlockEntity) this.blockEntity).getProgress());
    }
}