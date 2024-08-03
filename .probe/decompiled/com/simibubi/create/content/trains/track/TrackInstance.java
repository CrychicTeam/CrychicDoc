package com.simibubi.create.content.trains.track;

import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.light.LightUpdater;
import com.jozufozu.flywheel.util.box.GridAlignedBB;
import com.jozufozu.flywheel.util.box.ImmutableBox;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;

public class TrackInstance extends BlockEntityInstance<TrackBlockEntity> {

    private List<TrackInstance.BezierTrackInstance> instances;

    public TrackInstance(MaterialManager materialManager, TrackBlockEntity track) {
        super(materialManager, track);
        this.update();
    }

    public void update() {
        if (!((TrackBlockEntity) this.blockEntity).connections.isEmpty()) {
            this.remove();
            this.instances = ((TrackBlockEntity) this.blockEntity).connections.values().stream().map(this::createInstance).filter(Objects::nonNull).toList();
            LightUpdater.get(this.world).addListener(this);
        }
    }

    public ImmutableBox getVolume() {
        List<BlockPos> out = new ArrayList();
        out.addAll(((TrackBlockEntity) this.blockEntity).connections.keySet());
        out.addAll(((TrackBlockEntity) this.blockEntity).connections.keySet());
        return GridAlignedBB.containingAll(out);
    }

    public void updateLight() {
        if (this.instances != null) {
            this.instances.forEach(TrackInstance.BezierTrackInstance::updateLight);
        }
    }

    @Nullable
    private TrackInstance.BezierTrackInstance createInstance(BezierConnection bc) {
        return !bc.isPrimary() ? null : new TrackInstance.BezierTrackInstance(bc);
    }

    public void remove() {
        if (this.instances != null) {
            this.instances.forEach(TrackInstance.BezierTrackInstance::delete);
        }
    }

    private class BezierTrackInstance {

        private final ModelData[] ties;

        private final ModelData[] left;

        private final ModelData[] right;

        private final BlockPos[] tiesLightPos;

        private final BlockPos[] leftLightPos;

        private final BlockPos[] rightLightPos;

        @Nullable
        private TrackInstance.BezierTrackInstance.GirderInstance girder;

        private BezierTrackInstance(BezierConnection bc) {
            BlockPos tePosition = bc.tePositions.getFirst();
            this.girder = bc.hasGirder ? new TrackInstance.BezierTrackInstance.GirderInstance(bc) : null;
            PoseStack pose = new PoseStack();
            TransformStack.cast(pose).translate(TrackInstance.this.getInstancePosition());
            Material<ModelData> mat = TrackInstance.this.materialManager.cutout(RenderType.cutoutMipped()).material(Materials.TRANSFORMED);
            int segCount = bc.getSegmentCount();
            this.ties = new ModelData[segCount];
            this.left = new ModelData[segCount];
            this.right = new ModelData[segCount];
            this.tiesLightPos = new BlockPos[segCount];
            this.leftLightPos = new BlockPos[segCount];
            this.rightLightPos = new BlockPos[segCount];
            TrackMaterial.TrackModelHolder modelHolder = bc.getMaterial().getModelHolder();
            mat.getModel(modelHolder.tie()).createInstances(this.ties);
            mat.getModel(modelHolder.segment_left()).createInstances(this.left);
            mat.getModel(modelHolder.segment_right()).createInstances(this.right);
            BezierConnection.SegmentAngles[] segments = bc.getBakedSegments();
            for (int i = 1; i < segments.length; i++) {
                BezierConnection.SegmentAngles segment = segments[i];
                int modelIndex = i - 1;
                this.ties[modelIndex].setTransform(pose).mulPose(segment.tieTransform.pose()).mulNormal(segment.tieTransform.normal());
                this.tiesLightPos[modelIndex] = segment.lightPosition.offset(tePosition);
                for (boolean first : Iterate.trueAndFalse) {
                    PoseStack.Pose transform = segment.railTransforms.get(first);
                    (first ? this.left : this.right)[modelIndex].setTransform(pose).mulPose(transform.pose()).mulNormal(transform.normal());
                    (first ? this.leftLightPos : this.rightLightPos)[modelIndex] = segment.lightPosition.offset(tePosition);
                }
            }
            this.updateLight();
        }

        void delete() {
            for (ModelData d : this.ties) {
                d.delete();
            }
            for (ModelData d : this.left) {
                d.delete();
            }
            for (ModelData d : this.right) {
                d.delete();
            }
            if (this.girder != null) {
                this.girder.delete();
            }
        }

        void updateLight() {
            for (int i = 0; i < this.ties.length; i++) {
                this.ties[i].updateLight(TrackInstance.this.world, this.tiesLightPos[i]);
            }
            for (int i = 0; i < this.left.length; i++) {
                this.left[i].updateLight(TrackInstance.this.world, this.leftLightPos[i]);
            }
            for (int i = 0; i < this.right.length; i++) {
                this.right[i].updateLight(TrackInstance.this.world, this.rightLightPos[i]);
            }
            if (this.girder != null) {
                this.girder.updateLight();
            }
        }

        private class GirderInstance {

            private final Couple<ModelData[]> beams;

            private final Couple<Couple<ModelData[]>> beamCaps;

            private final BlockPos[] lightPos;

            private GirderInstance(BezierConnection bc) {
                BlockPos tePosition = bc.tePositions.getFirst();
                PoseStack pose = new PoseStack();
                ((TransformStack) TransformStack.cast(pose).translate(TrackInstance.this.getInstancePosition())).nudge((int) bc.tePositions.getFirst().asLong());
                Material<ModelData> mat = TrackInstance.this.materialManager.cutout(RenderType.cutoutMipped()).material(Materials.TRANSFORMED);
                int segCount = bc.getSegmentCount();
                this.beams = Couple.create(() -> new ModelData[segCount]);
                this.beamCaps = Couple.create(() -> Couple.create(() -> new ModelData[segCount]));
                this.lightPos = new BlockPos[segCount];
                this.beams.forEach(mat.getModel(AllPartialModels.GIRDER_SEGMENT_MIDDLE)::createInstances);
                this.beamCaps.forEachWithContext((c, topx) -> c.forEach(mat.getModel(topx ? AllPartialModels.GIRDER_SEGMENT_TOP : AllPartialModels.GIRDER_SEGMENT_BOTTOM)::createInstances));
                BezierConnection.GirderAngles[] bakedGirders = bc.getBakedGirders();
                for (int i = 1; i < bakedGirders.length; i++) {
                    BezierConnection.GirderAngles segment = bakedGirders[i];
                    int modelIndex = i - 1;
                    this.lightPos[modelIndex] = segment.lightPosition.offset(tePosition);
                    for (boolean first : Iterate.trueAndFalse) {
                        PoseStack.Pose beamTransform = segment.beams.get(first);
                        this.beams.get(first)[modelIndex].setTransform(pose).mulPose(beamTransform.pose()).mulNormal(beamTransform.normal());
                        for (boolean top : Iterate.trueAndFalse) {
                            PoseStack.Pose beamCapTransform = segment.beamCaps.get(top).get(first);
                            this.beamCaps.get(top).get(first)[modelIndex].setTransform(pose).mulPose(beamCapTransform.pose()).mulNormal(beamCapTransform.normal());
                        }
                    }
                }
                this.updateLight();
            }

            void delete() {
                this.beams.forEach(arr -> {
                    for (ModelData d : arr) {
                        d.delete();
                    }
                });
                this.beamCaps.forEach(c -> c.forEach(arr -> {
                    for (ModelData d : arr) {
                        d.delete();
                    }
                }));
            }

            void updateLight() {
                this.beams.forEach(arr -> {
                    for (int i = 0; i < arr.length; i++) {
                        arr[i].updateLight(TrackInstance.this.world, this.lightPos[i]);
                    }
                });
                this.beamCaps.forEach(c -> c.forEach(arr -> {
                    for (int i = 0; i < arr.length; i++) {
                        arr[i].updateLight(TrackInstance.this.world, this.lightPos[i]);
                    }
                }));
            }
        }
    }
}