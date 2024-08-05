package com.simibubi.create.content.kinetics.press;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;
import org.joml.Quaternionf;

public class PressInstance extends ShaftInstance<MechanicalPressBlockEntity> implements DynamicInstance {

    private final OrientedData pressHead;

    public PressInstance(MaterialManager materialManager, MechanicalPressBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        this.pressHead = (OrientedData) materialManager.defaultSolid().material(Materials.ORIENTED).getModel(AllPartialModels.MECHANICAL_PRESS_HEAD, this.blockState).createInstance();
        Quaternionf q = Axis.YP.rotationDegrees(AngleHelper.horizontalAngle((Direction) this.blockState.m_61143_(MechanicalPressBlock.HORIZONTAL_FACING)));
        this.pressHead.setRotation(q);
        this.transformModels();
    }

    public void beginFrame() {
        this.transformModels();
    }

    private void transformModels() {
        float renderedHeadOffset = this.getRenderedHeadOffset((MechanicalPressBlockEntity) this.blockEntity);
        this.pressHead.setPosition(this.getInstancePosition()).nudge(0.0F, -renderedHeadOffset, 0.0F);
    }

    private float getRenderedHeadOffset(MechanicalPressBlockEntity press) {
        PressingBehaviour pressingBehaviour = press.getPressingBehaviour();
        return pressingBehaviour.getRenderedHeadOffset(AnimationTickHolder.getPartialTicks()) * pressingBehaviour.mode.headOffset;
    }

    @Override
    public void updateLight() {
        super.updateLight();
        this.relight(this.pos, new FlatLit[] { this.pressHead });
    }

    @Override
    public void remove() {
        super.remove();
        this.pressHead.delete();
    }
}