package com.simibubi.create.content.kinetics.deployer;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.api.instance.TickableInstance;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class DeployerInstance extends ShaftInstance<DeployerBlockEntity> implements DynamicInstance, TickableInstance {

    final Direction facing = (Direction) this.blockState.m_61143_(DirectionalKineticBlock.FACING);

    final float yRot;

    final float xRot;

    final float zRot;

    protected final OrientedData pole;

    protected OrientedData hand;

    PartialModel currentHand;

    float progress;

    public DeployerInstance(MaterialManager materialManager, DeployerBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        boolean rotatePole = (Boolean) this.blockState.m_61143_(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE) ^ this.facing.getAxis() == Direction.Axis.Z;
        this.yRot = AngleHelper.horizontalAngle(this.facing);
        this.xRot = this.facing == Direction.UP ? 270.0F : (this.facing == Direction.DOWN ? 90.0F : 0.0F);
        this.zRot = rotatePole ? 90.0F : 0.0F;
        this.pole = (OrientedData) this.getOrientedMaterial().getModel(AllPartialModels.DEPLOYER_POLE, this.blockState).createInstance();
        this.currentHand = ((DeployerBlockEntity) this.blockEntity).getHandPose();
        this.hand = (OrientedData) this.getOrientedMaterial().getModel(this.currentHand, this.blockState).createInstance();
        this.progress = this.getProgress(AnimationTickHolder.getPartialTicks());
        updateRotation(this.pole, this.hand, this.yRot, this.xRot, this.zRot);
        this.updatePosition();
    }

    public void tick() {
        PartialModel handPose = ((DeployerBlockEntity) this.blockEntity).getHandPose();
        if (this.currentHand != handPose) {
            this.currentHand = handPose;
            this.getOrientedMaterial().getModel(this.currentHand, this.blockState).stealInstance(this.hand);
        }
    }

    public void beginFrame() {
        float newProgress = this.getProgress(AnimationTickHolder.getPartialTicks());
        if (!Mth.equal(newProgress, this.progress)) {
            this.progress = newProgress;
            this.updatePosition();
        }
    }

    @Override
    public void updateLight() {
        super.updateLight();
        this.relight(this.pos, new FlatLit[] { this.hand, this.pole });
    }

    @Override
    public void remove() {
        super.remove();
        this.hand.delete();
        this.pole.delete();
    }

    private float getProgress(float partialTicks) {
        if (((DeployerBlockEntity) this.blockEntity).state == DeployerBlockEntity.State.EXPANDING) {
            float f = 1.0F - ((float) ((DeployerBlockEntity) this.blockEntity).timer - partialTicks * (float) ((DeployerBlockEntity) this.blockEntity).getTimerSpeed()) / 1000.0F;
            if (((DeployerBlockEntity) this.blockEntity).fistBump) {
                f *= f;
            }
            return f;
        } else {
            return ((DeployerBlockEntity) this.blockEntity).state == DeployerBlockEntity.State.RETRACTING ? ((float) ((DeployerBlockEntity) this.blockEntity).timer - partialTicks * (float) ((DeployerBlockEntity) this.blockEntity).getTimerSpeed()) / 1000.0F : 0.0F;
        }
    }

    private void updatePosition() {
        float handLength = this.currentHand == AllPartialModels.DEPLOYER_HAND_POINTING ? 0.0F : (this.currentHand == AllPartialModels.DEPLOYER_HAND_HOLDING ? 0.25F : 0.1875F);
        float distance = Math.min(Mth.clamp(this.progress, 0.0F, 1.0F) * (((DeployerBlockEntity) this.blockEntity).reach + handLength), 1.3125F);
        Vec3i facingVec = this.facing.getNormal();
        BlockPos blockPos = this.getInstancePosition();
        float x = (float) blockPos.m_123341_() + (float) facingVec.getX() * distance;
        float y = (float) blockPos.m_123342_() + (float) facingVec.getY() * distance;
        float z = (float) blockPos.m_123343_() + (float) facingVec.getZ() * distance;
        this.pole.setPosition(x, y, z);
        this.hand.setPosition(x, y, z);
    }

    static void updateRotation(OrientedData pole, OrientedData hand, float yRot, float xRot, float zRot) {
        Quaternionf q = Axis.YP.rotationDegrees(yRot);
        q.mul(Axis.XP.rotationDegrees(xRot));
        hand.setRotation(q);
        q.mul(Axis.ZP.rotationDegrees(zRot));
        pole.setRotation(q);
    }
}