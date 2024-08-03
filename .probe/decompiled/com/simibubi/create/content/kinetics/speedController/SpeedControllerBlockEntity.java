package com.simibubi.create.content.kinetics.speedController;

import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.compat.computercraft.ComputerCraftProxy;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpeedControllerBlockEntity extends KineticBlockEntity {

    public static final int DEFAULT_SPEED = 16;

    public ScrollValueBehaviour targetSpeed;

    public AbstractComputerBehaviour computerBehaviour;

    boolean hasBracket = false;

    public SpeedControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        this.updateBracket();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        Integer max = AllConfigs.server().kinetics.maxRotationSpeed.get();
        this.targetSpeed = new KineticScrollValueBehaviour(Lang.translateDirect("kinetics.speed_controller.rotation_speed"), this, new SpeedControllerBlockEntity.ControllerValueBoxTransform());
        this.targetSpeed.between(-max, max);
        this.targetSpeed.value = 16;
        this.targetSpeed.withCallback(i -> this.updateTargetRotation());
        behaviours.add(this.targetSpeed);
        behaviours.add(this.computerBehaviour = ComputerCraftProxy.behaviour(this));
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.SPEED_CONTROLLER });
    }

    private void updateTargetRotation() {
        if (this.hasNetwork()) {
            this.getOrCreateNetwork().remove(this);
        }
        RotationPropagator.handleRemoved(this.f_58857_, this.f_58858_, this);
        this.removeSource();
        this.attachKinetics();
        if (this.isCogwheelPresent() && this.getSpeed() != 0.0F) {
            this.award(AllAdvancements.SPEED_CONTROLLER);
        }
    }

    public static float getConveyedSpeed(KineticBlockEntity cogWheel, KineticBlockEntity speedControllerIn, boolean targetingController) {
        if (!(speedControllerIn instanceof SpeedControllerBlockEntity)) {
            return 0.0F;
        } else {
            float speed = speedControllerIn.getTheoreticalSpeed();
            float wheelSpeed = cogWheel.getTheoreticalSpeed();
            float desiredOutputSpeed = getDesiredOutputSpeed(cogWheel, speedControllerIn, targetingController);
            float compareSpeed = targetingController ? speed : wheelSpeed;
            if (desiredOutputSpeed >= 0.0F && compareSpeed >= 0.0F) {
                return Math.max(desiredOutputSpeed, compareSpeed);
            } else {
                return desiredOutputSpeed < 0.0F && compareSpeed < 0.0F ? Math.min(desiredOutputSpeed, compareSpeed) : desiredOutputSpeed;
            }
        }
    }

    public static float getDesiredOutputSpeed(KineticBlockEntity cogWheel, KineticBlockEntity speedControllerIn, boolean targetingController) {
        SpeedControllerBlockEntity speedController = (SpeedControllerBlockEntity) speedControllerIn;
        float targetSpeed = (float) speedController.targetSpeed.getValue();
        float speed = speedControllerIn.getTheoreticalSpeed();
        float wheelSpeed = cogWheel.getTheoreticalSpeed();
        if (targetSpeed == 0.0F) {
            return 0.0F;
        } else if (targetingController && wheelSpeed == 0.0F) {
            return 0.0F;
        } else if (!speedController.hasSource()) {
            return targetingController ? targetSpeed : 0.0F;
        } else {
            boolean wheelPowersController = speedController.source.equals(cogWheel.m_58899_());
            if (wheelPowersController) {
                return targetingController ? targetSpeed : wheelSpeed;
            } else {
                return targetingController ? speed : targetSpeed;
            }
        }
    }

    public void updateBracket() {
        if (this.f_58857_ != null && this.f_58857_.isClientSide) {
            this.hasBracket = this.isCogwheelPresent();
        }
    }

    private boolean isCogwheelPresent() {
        BlockState stateAbove = this.f_58857_.getBlockState(this.f_58858_.above());
        return ICogWheel.isDedicatedCogWheel(stateAbove.m_60734_()) && ICogWheel.isLargeCog(stateAbove) && ((Direction.Axis) stateAbove.m_61143_(CogWheelBlock.AXIS)).isHorizontal();
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return this.computerBehaviour.isPeripheralCap(cap) ? this.computerBehaviour.getPeripheralCapability() : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.computerBehaviour.removePeripheral();
    }

    private class ControllerValueBoxTransform extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8.0, 11.0, 15.5);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction.getAxis().isVertical() ? false : state.m_61143_(SpeedControllerBlock.HORIZONTAL_AXIS) != direction.getAxis();
        }

        @Override
        public float getScale() {
            return 0.5F;
        }
    }
}