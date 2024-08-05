package com.simibubi.create.content.kinetics.crank;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class HandCrankInstance extends SingleRotatingInstance<HandCrankBlockEntity> implements DynamicInstance {

    private ModelData crank;

    private Direction facing = (Direction) this.blockState.m_61143_(BlockStateProperties.FACING);

    public HandCrankInstance(MaterialManager modelManager, HandCrankBlockEntity blockEntity) {
        super(modelManager, blockEntity);
        Instancer<ModelData> model = blockEntity.getRenderedHandleInstance(this.getTransformMaterial());
        this.crank = (ModelData) model.createInstance();
        this.rotateCrank();
    }

    public void beginFrame() {
        if (this.crank != null) {
            this.rotateCrank();
        }
    }

    private void rotateCrank() {
        Direction.Axis axis = this.facing.getAxis();
        float angle = ((HandCrankBlockEntity) this.blockEntity).getIndependentAngle(AnimationTickHolder.getPartialTicks());
        ((ModelData) ((ModelData) ((ModelData) this.crank.loadIdentity().translate(this.getInstancePosition())).centre()).rotate(Direction.get(Direction.AxisDirection.POSITIVE, axis), angle)).unCentre();
    }

    @Override
    public void init() {
        if (((HandCrankBlockEntity) this.blockEntity).shouldRenderShaft()) {
            super.init();
        }
    }

    @Override
    public void remove() {
        if (((HandCrankBlockEntity) this.blockEntity).shouldRenderShaft()) {
            super.remove();
        }
        if (this.crank != null) {
            this.crank.delete();
        }
    }

    @Override
    public void update() {
        if (((HandCrankBlockEntity) this.blockEntity).shouldRenderShaft()) {
            super.update();
        }
    }

    @Override
    public void updateLight() {
        if (((HandCrankBlockEntity) this.blockEntity).shouldRenderShaft()) {
            super.updateLight();
        }
        if (this.crank != null) {
            this.relight(this.pos, new FlatLit[] { this.crank });
        }
    }
}