package com.simibubi.create.content.kinetics.fan;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class FanInstance extends KineticBlockEntityInstance<EncasedFanBlockEntity> {

    protected final RotatingData shaft;

    protected final RotatingData fan;

    final Direction direction = (Direction) this.blockState.m_61143_(BlockStateProperties.FACING);

    private final Direction opposite = this.direction.getOpposite();

    public FanInstance(MaterialManager materialManager, EncasedFanBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        this.shaft = (RotatingData) this.getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF, this.blockState, this.opposite).createInstance();
        this.fan = (RotatingData) materialManager.defaultCutout().material(AllMaterialSpecs.ROTATING).getModel(AllPartialModels.ENCASED_FAN_INNER, this.blockState, this.opposite).createInstance();
        this.setup(this.shaft);
        this.setup(this.fan, this.getFanSpeed());
    }

    private float getFanSpeed() {
        float speed = ((EncasedFanBlockEntity) this.blockEntity).getSpeed() * 5.0F;
        if (speed > 0.0F) {
            speed = Mth.clamp(speed, 80.0F, 1280.0F);
        }
        if (speed < 0.0F) {
            speed = Mth.clamp(speed, -1280.0F, -80.0F);
        }
        return speed;
    }

    public void update() {
        this.updateRotation(this.shaft);
        this.updateRotation(this.fan, this.getFanSpeed());
    }

    public void updateLight() {
        BlockPos behind = this.pos.relative(this.opposite);
        this.relight(behind, new FlatLit[] { this.shaft });
        BlockPos inFront = this.pos.relative(this.direction);
        this.relight(inFront, new FlatLit[] { this.fan });
    }

    public void remove() {
        this.shaft.delete();
        this.fan.delete();
    }
}