package com.simibubi.create.content.kinetics.simpleRelays.encased;

import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class EncasedCogInstance extends KineticBlockEntityInstance<KineticBlockEntity> {

    private boolean large;

    protected RotatingData rotatingModel;

    protected Optional<RotatingData> rotatingTopShaft;

    protected Optional<RotatingData> rotatingBottomShaft;

    public static EncasedCogInstance small(MaterialManager modelManager, KineticBlockEntity blockEntity) {
        return new EncasedCogInstance(modelManager, blockEntity, false);
    }

    public static EncasedCogInstance large(MaterialManager modelManager, KineticBlockEntity blockEntity) {
        return new EncasedCogInstance(modelManager, blockEntity, true);
    }

    public EncasedCogInstance(MaterialManager modelManager, KineticBlockEntity blockEntity, boolean large) {
        super(modelManager, blockEntity);
        this.large = large;
    }

    public void init() {
        this.rotatingModel = this.setup((RotatingData) this.getCogModel().createInstance());
        if (this.blockState.m_60734_() instanceof IRotate def) {
            this.rotatingTopShaft = Optional.empty();
            this.rotatingBottomShaft = Optional.empty();
            for (Direction d : Iterate.directionsInAxis(this.axis)) {
                if (def.hasShaftTowards(((KineticBlockEntity) this.blockEntity).m_58904_(), ((KineticBlockEntity) this.blockEntity).m_58899_(), this.blockState, d)) {
                    RotatingData data = this.setup((RotatingData) this.getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF, this.blockState, d).createInstance());
                    if (this.large) {
                        data.setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(this.axis, this.pos));
                    }
                    if (d.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        this.rotatingTopShaft = Optional.of(data);
                    } else {
                        this.rotatingBottomShaft = Optional.of(data);
                    }
                }
            }
        }
    }

    public void update() {
        this.updateRotation(this.rotatingModel);
        this.rotatingTopShaft.ifPresent(x$0 -> this.updateRotation(x$0));
        this.rotatingBottomShaft.ifPresent(x$0 -> this.updateRotation(x$0));
    }

    public void updateLight() {
        this.relight(this.pos, new FlatLit[] { this.rotatingModel });
        this.rotatingTopShaft.ifPresent(d -> this.relight(this.pos, new FlatLit[] { d }));
        this.rotatingBottomShaft.ifPresent(d -> this.relight(this.pos, new FlatLit[] { d }));
    }

    public void remove() {
        this.rotatingModel.delete();
        this.rotatingTopShaft.ifPresent(InstanceData::delete);
        this.rotatingBottomShaft.ifPresent(InstanceData::delete);
    }

    protected Instancer<RotatingData> getCogModel() {
        BlockState referenceState = ((KineticBlockEntity) this.blockEntity).m_58900_();
        Direction facing = Direction.fromAxisAndDirection((Direction.Axis) referenceState.m_61143_(BlockStateProperties.AXIS), Direction.AxisDirection.POSITIVE);
        PartialModel partial = this.large ? AllPartialModels.SHAFTLESS_LARGE_COGWHEEL : AllPartialModels.SHAFTLESS_COGWHEEL;
        return this.getRotatingMaterial().getModel(partial, referenceState, facing, () -> {
            PoseStack poseStack = new PoseStack();
            ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(poseStack).centre()).rotateToFace(facing)).multiply(Axis.XN.rotationDegrees(90.0F))).unCentre();
            return poseStack;
        });
    }
}