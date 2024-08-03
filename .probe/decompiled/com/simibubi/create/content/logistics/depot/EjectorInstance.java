package com.simibubi.create.content.logistics.depot;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.util.Mth;

public class EjectorInstance extends ShaftInstance<EjectorBlockEntity> implements DynamicInstance {

    protected final ModelData plate;

    private float lastProgress = Float.NaN;

    public EjectorInstance(MaterialManager dispatcher, EjectorBlockEntity blockEntity) {
        super(dispatcher, blockEntity);
        this.plate = (ModelData) this.getTransformMaterial().getModel(AllPartialModels.EJECTOR_TOP, this.blockState).createInstance();
        this.pivotPlate();
    }

    public void beginFrame() {
        float lidProgress = this.getLidProgress();
        if (!Mth.equal(lidProgress, this.lastProgress)) {
            this.pivotPlate(lidProgress);
            this.lastProgress = lidProgress;
        }
    }

    @Override
    public void updateLight() {
        super.updateLight();
        this.relight(this.pos, new FlatLit[] { this.plate });
    }

    @Override
    public void remove() {
        super.remove();
        this.plate.delete();
    }

    private void pivotPlate() {
        this.pivotPlate(this.getLidProgress());
    }

    private float getLidProgress() {
        return ((EjectorBlockEntity) this.blockEntity).getLidProgress(AnimationTickHolder.getPartialTicks());
    }

    private void pivotPlate(float lidProgress) {
        float angle = lidProgress * 70.0F;
        EjectorRenderer.applyLidAngle((KineticBlockEntity) this.blockEntity, angle, (ModelData) this.plate.loadIdentity().translate(this.getInstancePosition()));
    }
}