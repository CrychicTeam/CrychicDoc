package com.simibubi.create.content.kinetics.steamEngine;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class SteamEngineBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    protected ScrollOptionBehaviour<WindmillBearingBlockEntity.RotationDirection> movementDirection;

    public WeakReference<PoweredShaftBlockEntity> target;

    public WeakReference<FluidTankBlockEntity> source;

    float prevAngle = 0.0F;

    public SteamEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.source = new WeakReference(null);
        this.target = new WeakReference(null);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.movementDirection = new ScrollOptionBehaviour(WindmillBearingBlockEntity.RotationDirection.class, Lang.translateDirect("contraptions.windmill.rotation_direction"), this, new SteamEngineValueBox());
        this.movementDirection.onlyActiveWhen(() -> {
            PoweredShaftBlockEntity shaft = this.getShaft();
            return shaft == null || !shaft.hasSource();
        });
        this.movementDirection.withCallback($ -> this.onDirectionChanged());
        behaviours.add(this.movementDirection);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.STEAM_ENGINE });
    }

    private void onDirectionChanged() {
    }

    @Override
    public void tick() {
        super.tick();
        FluidTankBlockEntity tank = this.getTank();
        PoweredShaftBlockEntity shaft = this.getShaft();
        if (tank != null && shaft != null) {
            boolean verticalTarget = false;
            BlockState shaftState = shaft.m_58900_();
            Direction.Axis targetAxis = Direction.Axis.X;
            if (shaftState.m_60734_() instanceof IRotate ir) {
                targetAxis = ir.getRotationAxis(shaftState);
            }
            verticalTarget = targetAxis == Direction.Axis.Y;
            BlockState blockState = this.m_58900_();
            if (AllBlocks.STEAM_ENGINE.has(blockState)) {
                Direction facing = SteamEngineBlock.getFacing(blockState);
                if (facing.getAxis() == Direction.Axis.Y) {
                    facing = (Direction) blockState.m_61143_(SteamEngineBlock.f_54117_);
                }
                float efficiency = Mth.clamp(tank.boiler.getEngineEfficiency(tank.getTotalTankSize()), 0.0F, 1.0F);
                if (efficiency > 0.0F) {
                    this.award(AllAdvancements.STEAM_ENGINE);
                }
                int conveyedSpeedLevel = efficiency == 0.0F ? 1 : (verticalTarget ? 1 : (int) GeneratingKineticBlockEntity.convertToDirection(1.0F, facing));
                if (targetAxis == Direction.Axis.Z) {
                    conveyedSpeedLevel *= -1;
                }
                if (this.movementDirection.get() == WindmillBearingBlockEntity.RotationDirection.COUNTER_CLOCKWISE) {
                    conveyedSpeedLevel *= -1;
                }
                float shaftSpeed = shaft.getTheoreticalSpeed();
                if (shaft.hasSource() && shaftSpeed != 0.0F && conveyedSpeedLevel != 0 && shaftSpeed > 0.0F != conveyedSpeedLevel > 0) {
                    this.movementDirection.setValue(1 - ((WindmillBearingBlockEntity.RotationDirection) this.movementDirection.get()).ordinal());
                    conveyedSpeedLevel *= -1;
                }
                shaft.update(this.f_58858_, conveyedSpeedLevel, efficiency);
                if (this.f_58857_.isClientSide) {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::spawnParticles);
                }
            }
        } else if (!this.f_58857_.isClientSide()) {
            if (shaft != null) {
                if (shaft.m_58899_().subtract(this.f_58858_).equals(shaft.enginePos)) {
                    if (shaft.engineEfficiency != 0.0F) {
                        Direction facingx = SteamEngineBlock.getFacing(this.m_58900_());
                        if (this.f_58857_.isLoaded(this.f_58858_.relative(facingx.getOpposite()))) {
                            shaft.update(this.f_58858_, 0, 0.0F);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void remove() {
        PoweredShaftBlockEntity shaft = this.getShaft();
        if (shaft != null) {
            shaft.remove(this.f_58858_);
        }
        super.remove();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(2.0);
    }

    public PoweredShaftBlockEntity getShaft() {
        PoweredShaftBlockEntity shaft = (PoweredShaftBlockEntity) this.target.get();
        if (shaft == null || shaft.m_58901_() || !shaft.canBePoweredBy(this.f_58858_)) {
            if (shaft != null) {
                this.target = new WeakReference(null);
            }
            Direction facing = SteamEngineBlock.getFacing(this.m_58900_());
            if (this.f_58857_.getBlockEntity(this.f_58858_.relative(facing, 2)) instanceof PoweredShaftBlockEntity ps && ps.canBePoweredBy(this.f_58858_)) {
                shaft = ps;
                this.target = new WeakReference(ps);
            }
        }
        return shaft;
    }

    public FluidTankBlockEntity getTank() {
        FluidTankBlockEntity tank = (FluidTankBlockEntity) this.source.get();
        if (tank == null || tank.m_58901_()) {
            if (tank != null) {
                this.source = new WeakReference(null);
            }
            Direction facing = SteamEngineBlock.getFacing(this.m_58900_());
            if (this.f_58857_.getBlockEntity(this.f_58858_.relative(facing.getOpposite())) instanceof FluidTankBlockEntity tankBe) {
                tank = tankBe;
                this.source = new WeakReference(tankBe);
            }
        }
        return tank == null ? null : tank.getControllerBE();
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnParticles() {
        Float targetAngle = this.getTargetAngle();
        PoweredShaftBlockEntity ste = (PoweredShaftBlockEntity) this.target.get();
        if (ste != null) {
            if (ste.isPoweredBy(this.f_58858_) && ste.engineEfficiency != 0.0F) {
                if (targetAngle != null) {
                    float angle = AngleHelper.deg((double) targetAngle.floatValue());
                    angle += angle < 0.0F ? -105.0F : 285.0F;
                    angle %= 360.0F;
                    PoweredShaftBlockEntity shaft = this.getShaft();
                    if (shaft != null && shaft.getSpeed() != 0.0F) {
                        if (!(angle >= 0.0F) || this.prevAngle > 180.0F && angle < 180.0F) {
                            if (!(angle < 0.0F) || this.prevAngle < -180.0F && angle > -180.0F) {
                                FluidTankBlockEntity sourceBE = (FluidTankBlockEntity) this.source.get();
                                if (sourceBE != null) {
                                    FluidTankBlockEntity controller = sourceBE.getControllerBE();
                                    if (controller != null && controller.boiler != null) {
                                        float volume = 3.0F / (float) Math.max(2, controller.boiler.attachedEngines / 6);
                                        float pitch = 1.18F - this.f_58857_.random.nextFloat() * 0.25F;
                                        this.f_58857_.playLocalSound((double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_(), SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, volume, pitch, false);
                                        AllSoundEvents.STEAM.playAt(this.f_58857_, this.f_58858_, volume / 16.0F, 0.8F, false);
                                    }
                                }
                                Direction facing = SteamEngineBlock.getFacing(this.m_58900_());
                                Vec3 offset = VecHelper.rotate(new Vec3(0.0, 0.0, 1.0).add(VecHelper.offsetRandomly(Vec3.ZERO, this.f_58857_.random, 1.0F).multiply(1.0, 1.0, 0.0).normalize().scale(0.5)), (double) AngleHelper.verticalAngle(facing), Direction.Axis.X);
                                offset = VecHelper.rotate(offset, (double) AngleHelper.horizontalAngle(facing), Direction.Axis.Y);
                                Vec3 v = offset.scale(0.5).add(Vec3.atCenterOf(this.f_58858_));
                                Vec3 m = offset.subtract(Vec3.atLowerCornerOf(facing.getNormal()).scale(0.75));
                                this.f_58857_.addParticle(new SteamJetParticleData(1.0F), v.x, v.y, v.z, m.x, m.y, m.z);
                                this.prevAngle = angle;
                            } else {
                                this.prevAngle = angle;
                            }
                        } else {
                            this.prevAngle = angle;
                        }
                    }
                }
            }
        }
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public Float getTargetAngle() {
        float angle = 0.0F;
        BlockState blockState = this.m_58900_();
        if (!AllBlocks.STEAM_ENGINE.has(blockState)) {
            return null;
        } else {
            Direction facing = SteamEngineBlock.getFacing(blockState);
            PoweredShaftBlockEntity shaft = this.getShaft();
            Direction.Axis facingAxis = facing.getAxis();
            Direction.Axis axis = Direction.Axis.Y;
            if (shaft == null) {
                return null;
            } else {
                axis = KineticBlockEntityRenderer.getRotationAxisOf(shaft);
                angle = KineticBlockEntityRenderer.getAngleForTe(shaft, shaft.m_58899_(), axis);
                if (axis == facingAxis) {
                    return null;
                } else {
                    if (axis.isHorizontal() && facingAxis == Direction.Axis.X ^ facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        angle *= -1.0F;
                    }
                    if (axis == Direction.Axis.X && facing == Direction.DOWN) {
                        angle *= -1.0F;
                    }
                    return angle;
                }
            }
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        PoweredShaftBlockEntity shaft = this.getShaft();
        return shaft == null ? false : shaft.addToEngineTooltip(tooltip, isPlayerSneaking);
    }
}