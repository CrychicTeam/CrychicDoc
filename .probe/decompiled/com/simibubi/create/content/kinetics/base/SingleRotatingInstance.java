package com.simibubi.create.content.kinetics.base;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.world.level.block.state.BlockState;

public class SingleRotatingInstance<T extends KineticBlockEntity> extends KineticBlockEntityInstance<T> {

    protected RotatingData rotatingModel;

    public SingleRotatingInstance(MaterialManager materialManager, T blockEntity) {
        super(materialManager, blockEntity);
    }

    public void init() {
        this.rotatingModel = this.setup((RotatingData) this.getModel().createInstance());
    }

    public void update() {
        this.updateRotation(this.rotatingModel);
    }

    public void updateLight() {
        this.relight(this.pos, new FlatLit[] { this.rotatingModel });
    }

    public void remove() {
        this.rotatingModel.delete();
    }

    protected BlockState getRenderedBlockState() {
        return this.blockState;
    }

    protected Instancer<RotatingData> getModel() {
        return this.getRotatingMaterial().getModel(this.getRenderedBlockState());
    }
}