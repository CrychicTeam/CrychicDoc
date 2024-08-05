package com.simibubi.create.content.redstone.analogLever;

import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.transform.Rotate;
import com.jozufozu.flywheel.util.transform.Translate;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class AnalogLeverInstance extends BlockEntityInstance<AnalogLeverBlockEntity> implements DynamicInstance {

    protected final ModelData handle;

    protected final ModelData indicator;

    final float rX;

    final float rY;

    public AnalogLeverInstance(MaterialManager materialManager, AnalogLeverBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        Material<ModelData> mat = this.getTransformMaterial();
        this.handle = (ModelData) mat.getModel(AllPartialModels.ANALOG_LEVER_HANDLE, this.blockState).createInstance();
        this.indicator = (ModelData) mat.getModel(AllPartialModels.ANALOG_LEVER_INDICATOR, this.blockState).createInstance();
        AttachFace face = (AttachFace) this.blockState.m_61143_(AnalogLeverBlock.f_53179_);
        this.rX = face == AttachFace.FLOOR ? 0.0F : (face == AttachFace.WALL ? 90.0F : 180.0F);
        this.rY = AngleHelper.horizontalAngle((Direction) this.blockState.m_61143_(AnalogLeverBlock.f_54117_));
        this.transform(this.indicator.loadIdentity());
        this.animateLever();
    }

    public void beginFrame() {
        if (!((AnalogLeverBlockEntity) this.blockEntity).clientState.settled()) {
            this.animateLever();
        }
    }

    protected void animateLever() {
        float state = ((AnalogLeverBlockEntity) this.blockEntity).clientState.getValue(AnimationTickHolder.getPartialTicks());
        this.indicator.setColor(Color.mixColors(2884352, 13434880, state / 15.0F));
        float angle = (float) ((double) (state / 15.0F * 90.0F / 180.0F) * Math.PI);
        ((ModelData) ((ModelData) this.transform(this.handle.loadIdentity())).translate(0.5, 0.0625, 0.5).rotate(Direction.EAST, angle)).translate(-0.5, -0.0625, -0.5);
    }

    public void remove() {
        this.handle.delete();
        this.indicator.delete();
    }

    public void updateLight() {
        this.relight(this.pos, new FlatLit[] { this.handle, this.indicator });
    }

    private <T extends Translate<T> & Rotate<T>> T transform(T msr) {
        return (T) ((Translate) ((Rotate) ((Translate) ((Rotate) ((Translate) ((Translate) msr.translate(this.getInstancePosition())).centre())).rotate(Direction.UP, (float) ((double) (this.rY / 180.0F) * Math.PI)))).rotate(Direction.EAST, (float) ((double) (this.rX / 180.0F) * Math.PI))).unCentre();
    }
}