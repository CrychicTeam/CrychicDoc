package com.simibubi.create.content.kinetics.saw;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SawInstance extends SingleRotatingInstance<SawBlockEntity> {

    public SawInstance(MaterialManager materialManager, SawBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        if (((Direction) this.blockState.m_61143_(BlockStateProperties.FACING)).getAxis().isHorizontal()) {
            BlockState referenceState = this.blockState.rotate(((SawBlockEntity) this.blockEntity).m_58904_(), ((SawBlockEntity) this.blockEntity).m_58899_(), Rotation.CLOCKWISE_180);
            Direction facing = (Direction) referenceState.m_61143_(BlockStateProperties.FACING);
            return this.getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF, referenceState, facing);
        } else {
            return this.getRotatingMaterial().getModel(this.shaft());
        }
    }
}