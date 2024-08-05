package com.simibubi.create.content.kinetics.gauge;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.ArrayList;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public abstract class GaugeInstance extends ShaftInstance<GaugeBlockEntity> implements DynamicInstance {

    protected final ArrayList<GaugeInstance.DialFace> faces = new ArrayList(2);

    protected PoseStack ms;

    protected GaugeInstance(MaterialManager materialManager, GaugeBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        GaugeBlock gaugeBlock = (GaugeBlock) this.blockState.m_60734_();
        Instancer<ModelData> dialModel = this.getTransformMaterial().getModel(AllPartialModels.GAUGE_DIAL, this.blockState);
        Instancer<ModelData> headModel = this.getHeadModel();
        this.ms = new PoseStack();
        TransformStack msr = TransformStack.cast(this.ms);
        msr.translate(this.getInstancePosition());
        float progress = Mth.lerp(AnimationTickHolder.getPartialTicks(), blockEntity.prevDialState, blockEntity.dialState);
        for (Direction facing : Iterate.directions) {
            if (gaugeBlock.shouldRenderHeadOnFace(this.world, this.pos, this.blockState, facing)) {
                GaugeInstance.DialFace face = this.makeFace(facing, dialModel, headModel);
                this.faces.add(face);
                face.setupTransform(msr, progress);
            }
        }
    }

    private GaugeInstance.DialFace makeFace(Direction face, Instancer<ModelData> dialModel, Instancer<ModelData> headModel) {
        return new GaugeInstance.DialFace(face, (ModelData) dialModel.createInstance(), (ModelData) headModel.createInstance());
    }

    public void beginFrame() {
        GaugeBlockEntity gaugeBlockEntity = (GaugeBlockEntity) this.blockEntity;
        if (!Mth.equal(gaugeBlockEntity.prevDialState, gaugeBlockEntity.dialState)) {
            float progress = Mth.lerp(AnimationTickHolder.getPartialTicks(), gaugeBlockEntity.prevDialState, gaugeBlockEntity.dialState);
            TransformStack msr = TransformStack.cast(this.ms);
            for (GaugeInstance.DialFace faceEntry : this.faces) {
                faceEntry.updateTransform(msr, progress);
            }
        }
    }

    @Override
    public void updateLight() {
        super.updateLight();
        this.relight(this.pos, this.faces.stream().flatMap(Couple::stream));
    }

    @Override
    public void remove() {
        super.remove();
        this.faces.forEach(GaugeInstance.DialFace::delete);
    }

    protected abstract Instancer<ModelData> getHeadModel();

    private class DialFace extends Couple<ModelData> {

        Direction face;

        public DialFace(Direction face, ModelData first, ModelData second) {
            super(first, second);
            this.face = face;
        }

        private void setupTransform(TransformStack msr, float progress) {
            float dialPivot = 0.359375F;
            msr.pushPose();
            this.rotateToFace(msr);
            this.getSecond().setTransform(GaugeInstance.this.ms);
            ((TransformStack) ((TransformStack) msr.translate(0.0, (double) dialPivot, (double) dialPivot)).rotate(Direction.EAST, (float) ((Math.PI / 2) * (double) (-progress)))).translate(0.0, (double) (-dialPivot), (double) (-dialPivot));
            this.getFirst().setTransform(GaugeInstance.this.ms);
            msr.popPose();
        }

        private void updateTransform(TransformStack msr, float progress) {
            float dialPivot = 0.359375F;
            msr.pushPose();
            ((TransformStack) ((TransformStack) this.rotateToFace(msr).translate(0.0, (double) dialPivot, (double) dialPivot)).rotate(Direction.EAST, (float) ((Math.PI / 2) * (double) (-progress)))).translate(0.0, (double) (-dialPivot), (double) (-dialPivot));
            this.getFirst().setTransform(GaugeInstance.this.ms);
            msr.popPose();
        }

        protected TransformStack rotateToFace(TransformStack msr) {
            return (TransformStack) ((TransformStack) ((TransformStack) msr.centre()).rotate(Direction.UP, (float) ((double) ((-this.face.toYRot() - 90.0F) / 180.0F) * Math.PI))).unCentre();
        }

        private void delete() {
            this.getFirst().delete();
            this.getSecond().delete();
        }
    }

    public static class Speed extends GaugeInstance {

        public Speed(MaterialManager materialManager, GaugeBlockEntity blockEntity) {
            super(materialManager, blockEntity);
        }

        @Override
        protected Instancer<ModelData> getHeadModel() {
            return this.getTransformMaterial().getModel(AllPartialModels.GAUGE_HEAD_SPEED, this.blockState);
        }
    }

    public static class Stress extends GaugeInstance {

        public Stress(MaterialManager materialManager, GaugeBlockEntity blockEntity) {
            super(materialManager, blockEntity);
        }

        @Override
        protected Instancer<ModelData> getHeadModel() {
            return this.getTransformMaterial().getModel(AllPartialModels.GAUGE_HEAD_STRESS, this.blockState);
        }
    }
}