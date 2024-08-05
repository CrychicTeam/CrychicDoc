package com.simibubi.create.content.trains.bogey;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class StandardBogeyRenderer {

    public static class CommonStandardBogeyRenderer extends BogeyRenderer.CommonRenderer {

        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager, CarriageBogey carriageBogey) {
            this.createModelInstance(materialManager, (BlockState) AllBlocks.SHAFT.getDefaultState().m_61124_(ShaftBlock.AXIS, Direction.Axis.Z), 2);
        }

        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
            boolean inInstancedContraption = vb == null;
            BogeyRenderer.BogeyModelData[] shafts = this.getTransform((BlockState) AllBlocks.SHAFT.getDefaultState().m_61124_(ShaftBlock.AXIS, Direction.Axis.Z), ms, inInstancedContraption, 2);
            for (int i : Iterate.zeroAndOne) {
                ((BogeyRenderer.BogeyModelData) ((BogeyRenderer.BogeyModelData) ((BogeyRenderer.BogeyModelData) shafts[i].translate(-0.5, 0.25, (double) (i * -1)).centre()).rotateZ((double) wheelAngle)).unCentre()).render(ms, light, vb);
            }
        }
    }

    public static class LargeStandardBogeyRenderer extends BogeyRenderer {

        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager, CarriageBogey carriageBogey) {
            this.createModelInstance(materialManager, new PartialModel[] { AllPartialModels.LARGE_BOGEY_WHEELS, AllPartialModels.BOGEY_DRIVE, AllPartialModels.BOGEY_PISTON, AllPartialModels.BOGEY_PIN });
            this.createModelInstance(materialManager, (BlockState) AllBlocks.SHAFT.getDefaultState().m_61124_(ShaftBlock.AXIS, Direction.Axis.X), 2);
        }

        @Override
        public BogeySizes.BogeySize getSize() {
            return BogeySizes.LARGE;
        }

        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
            boolean inInstancedContraption = vb == null;
            BogeyRenderer.BogeyModelData[] secondaryShafts = this.getTransform((BlockState) AllBlocks.SHAFT.getDefaultState().m_61124_(ShaftBlock.AXIS, Direction.Axis.X), ms, inInstancedContraption, 2);
            for (int i : Iterate.zeroAndOne) {
                ((BogeyRenderer.BogeyModelData) ((BogeyRenderer.BogeyModelData) ((BogeyRenderer.BogeyModelData) secondaryShafts[i].translate(-0.5, 0.25, (double) (0.5F + (float) (i * -2))).centre()).rotateX((double) wheelAngle)).unCentre()).render(ms, light, vb);
            }
            this.getTransform(AllPartialModels.BOGEY_DRIVE, ms, inInstancedContraption).render(ms, light, vb);
            this.getTransform(AllPartialModels.BOGEY_PISTON, ms, inInstancedContraption).translate(0.0, 0.0, 0.25 * Math.sin((double) AngleHelper.rad((double) wheelAngle))).render(ms, light, vb);
            if (!inInstancedContraption) {
                ms.pushPose();
            }
            ((BogeyRenderer.BogeyModelData) this.getTransform(AllPartialModels.LARGE_BOGEY_WHEELS, ms, inInstancedContraption).translate(0.0, 1.0, 0.0).rotateX((double) wheelAngle)).render(ms, light, vb);
            ((BogeyRenderer.BogeyModelData) ((BogeyRenderer.BogeyModelData) this.getTransform(AllPartialModels.BOGEY_PIN, ms, inInstancedContraption).translate(0.0, 1.0, 0.0).rotateX((double) wheelAngle)).translate(0.0, 0.25, 0.0).rotateX((double) (-wheelAngle))).render(ms, light, vb);
            if (!inInstancedContraption) {
                ms.popPose();
            }
        }
    }

    public static class SmallStandardBogeyRenderer extends BogeyRenderer {

        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager, CarriageBogey carriageBogey) {
            this.createModelInstance(materialManager, AllPartialModels.SMALL_BOGEY_WHEELS, 2);
            this.createModelInstance(materialManager, new PartialModel[] { AllPartialModels.BOGEY_FRAME });
        }

        @Override
        public BogeySizes.BogeySize getSize() {
            return BogeySizes.SMALL;
        }

        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
            boolean inInstancedContraption = vb == null;
            this.getTransform(AllPartialModels.BOGEY_FRAME, ms, inInstancedContraption).render(ms, light, vb);
            BogeyRenderer.BogeyModelData[] wheels = this.getTransform(AllPartialModels.SMALL_BOGEY_WHEELS, ms, inInstancedContraption, 2);
            for (int side : Iterate.positiveAndNegative) {
                if (!inInstancedContraption) {
                    ms.pushPose();
                }
                ((BogeyRenderer.BogeyModelData) wheels[(side + 1) / 2].translate(0.0, 0.75, (double) side).rotateX((double) wheelAngle)).render(ms, light, vb);
                if (!inInstancedContraption) {
                    ms.popPose();
                }
            }
        }
    }
}