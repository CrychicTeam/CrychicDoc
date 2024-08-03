package com.simibubi.create.content.kinetics.gearbox;

import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.EnumMap;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class GearboxInstance extends KineticBlockEntityInstance<GearboxBlockEntity> {

    protected final EnumMap<Direction, RotatingData> keys = new EnumMap(Direction.class);

    protected Direction sourceFacing;

    public GearboxInstance(MaterialManager materialManager, GearboxBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        Direction.Axis boxAxis = (Direction.Axis) this.blockState.m_61143_(BlockStateProperties.AXIS);
        int blockLight = this.world.m_45517_(LightLayer.BLOCK, this.pos);
        int skyLight = this.world.m_45517_(LightLayer.SKY, this.pos);
        this.updateSourceFacing();
        Material<RotatingData> rotatingMaterial = this.getRotatingMaterial();
        for (Direction direction : Iterate.directions) {
            Direction.Axis axis = direction.getAxis();
            if (boxAxis != axis) {
                Instancer<RotatingData> shaft = rotatingMaterial.getModel(AllPartialModels.SHAFT_HALF, this.blockState, direction);
                RotatingData key = (RotatingData) shaft.createInstance();
                key.setRotationAxis(Direction.get(Direction.AxisDirection.POSITIVE, axis).step()).setRotationalSpeed(this.getSpeed(direction)).setRotationOffset(this.getRotationOffset(axis)).setColor(blockEntity).setPosition(this.getInstancePosition()).setBlockLight(blockLight).setSkyLight(skyLight);
                this.keys.put(direction, key);
            }
        }
    }

    private float getSpeed(Direction direction) {
        float speed = ((GearboxBlockEntity) this.blockEntity).getSpeed();
        if (speed != 0.0F && this.sourceFacing != null) {
            if (this.sourceFacing.getAxis() == direction.getAxis()) {
                speed *= this.sourceFacing == direction ? 1.0F : -1.0F;
            } else if (this.sourceFacing.getAxisDirection() == direction.getAxisDirection()) {
                speed *= -1.0F;
            }
        }
        return speed;
    }

    protected void updateSourceFacing() {
        if (((GearboxBlockEntity) this.blockEntity).hasSource()) {
            BlockPos source = ((GearboxBlockEntity) this.blockEntity).source.subtract(this.pos);
            this.sourceFacing = Direction.getNearest((float) source.m_123341_(), (float) source.m_123342_(), (float) source.m_123343_());
        } else {
            this.sourceFacing = null;
        }
    }

    public void update() {
        this.updateSourceFacing();
        for (Entry<Direction, RotatingData> key : this.keys.entrySet()) {
            Direction direction = (Direction) key.getKey();
            Direction.Axis axis = direction.getAxis();
            this.updateRotation((RotatingData) key.getValue(), axis, this.getSpeed(direction));
        }
    }

    public void updateLight() {
        this.relight(this.pos, this.keys.values().stream());
    }

    public void remove() {
        this.keys.values().forEach(InstanceData::delete);
        this.keys.clear();
    }
}