package com.simibubi.create.content.kinetics.transmission;

import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.ArrayList;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;

public class SplitShaftInstance extends KineticBlockEntityInstance<SplitShaftBlockEntity> {

    protected final ArrayList<RotatingData> keys = new ArrayList(2);

    public SplitShaftInstance(MaterialManager modelManager, SplitShaftBlockEntity blockEntity) {
        super(modelManager, blockEntity);
        float speed = blockEntity.getSpeed();
        Material<RotatingData> rotatingMaterial = this.getRotatingMaterial();
        for (Direction dir : Iterate.directionsInAxis(this.getRotationAxis())) {
            Instancer<RotatingData> half = rotatingMaterial.getModel(AllPartialModels.SHAFT_HALF, this.blockState, dir);
            float splitSpeed = speed * blockEntity.getRotationSpeedModifier(dir);
            this.keys.add(this.setup((RotatingData) half.createInstance(), splitSpeed));
        }
    }

    public void update() {
        Block block = this.blockState.m_60734_();
        Direction.Axis boxAxis = ((IRotate) block).getRotationAxis(this.blockState);
        Direction[] directions = Iterate.directionsInAxis(boxAxis);
        for (int i : Iterate.zeroAndOne) {
            this.updateRotation((RotatingData) this.keys.get(i), ((SplitShaftBlockEntity) this.blockEntity).getSpeed() * ((SplitShaftBlockEntity) this.blockEntity).getRotationSpeedModifier(directions[i]));
        }
    }

    public void updateLight() {
        this.relight(this.pos, this.keys.stream());
    }

    public void remove() {
        this.keys.forEach(InstanceData::delete);
        this.keys.clear();
    }
}