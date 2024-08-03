package com.simibubi.create.content.contraptions.bearing;

import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class WindmillBearingBlockEntity extends MechanicalBearingBlockEntity {

    protected ScrollOptionBehaviour<WindmillBearingBlockEntity.RotationDirection> movementDirection;

    protected float lastGeneratedSpeed;

    protected boolean queuedReassembly;

    public WindmillBearingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void updateGeneratedRotation() {
        super.updateGeneratedRotation();
        this.lastGeneratedSpeed = this.getGeneratedSpeed();
        this.queuedReassembly = false;
    }

    @Override
    public void onSpeedChanged(float prevSpeed) {
        boolean cancelAssembly = this.assembleNextTick;
        super.onSpeedChanged(prevSpeed);
        this.assembleNextTick = cancelAssembly;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_58857_.isClientSide()) {
            if (this.queuedReassembly) {
                this.queuedReassembly = false;
                if (!this.running) {
                    this.assembleNextTick = true;
                }
            }
        }
    }

    public void disassembleForMovement() {
        if (this.running) {
            this.disassemble();
            this.queuedReassembly = true;
        }
    }

    @Override
    public float getGeneratedSpeed() {
        if (!this.running) {
            return 0.0F;
        } else if (this.movedContraption == null) {
            return this.lastGeneratedSpeed;
        } else {
            int sails = ((BearingContraption) this.movedContraption.getContraption()).getSailBlocks() / AllConfigs.server().kinetics.windmillSailsPerRPM.get();
            return (float) Mth.clamp(sails, 1, 16) * this.getAngleSpeedDirection();
        }
    }

    @Override
    protected boolean isWindmill() {
        return true;
    }

    protected float getAngleSpeedDirection() {
        WindmillBearingBlockEntity.RotationDirection rotationDirection = WindmillBearingBlockEntity.RotationDirection.values()[this.movementDirection.getValue()];
        return (float) (rotationDirection == WindmillBearingBlockEntity.RotationDirection.CLOCKWISE ? 1 : -1);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putFloat("LastGenerated", this.lastGeneratedSpeed);
        compound.putBoolean("QueueAssembly", this.queuedReassembly);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        if (!this.wasMoved) {
            this.lastGeneratedSpeed = compound.getFloat("LastGenerated");
        }
        this.queuedReassembly = compound.getBoolean("QueueAssembly");
        super.read(compound, clientPacket);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.remove(this.movementMode);
        this.movementDirection = new ScrollOptionBehaviour(WindmillBearingBlockEntity.RotationDirection.class, Lang.translateDirect("contraptions.windmill.rotation_direction"), this, this.getMovementModeSlot());
        this.movementDirection.withCallback($ -> this.onDirectionChanged());
        behaviours.add(this.movementDirection);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.WINDMILL, AllAdvancements.WINDMILL_MAXED });
    }

    private void onDirectionChanged() {
        if (this.running) {
            if (!this.f_58857_.isClientSide) {
                this.updateGeneratedRotation();
            }
        }
    }

    @Override
    public boolean isWoodenTop() {
        return true;
    }

    public static enum RotationDirection implements INamedIconOptions {

        CLOCKWISE(AllIcons.I_REFRESH), COUNTER_CLOCKWISE(AllIcons.I_ROTATE_CCW);

        private String translationKey;

        private AllIcons icon;

        private RotationDirection(AllIcons icon) {
            this.icon = icon;
            this.translationKey = "generic." + Lang.asId(this.name());
        }

        @Override
        public AllIcons getIcon() {
            return this.icon;
        }

        @Override
        public String getTranslationKey() {
            return this.translationKey;
        }
    }
}