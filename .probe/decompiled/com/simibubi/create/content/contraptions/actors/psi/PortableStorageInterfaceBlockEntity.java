package com.simibubi.create.content.contraptions.actors.psi;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public abstract class PortableStorageInterfaceBlockEntity extends SmartBlockEntity {

    public static final int ANIMATION = 4;

    protected int transferTimer;

    protected float distance;

    protected LerpedFloat connectionAnimation;

    protected boolean powered;

    protected Entity connectedEntity;

    public int keepAlive = 0;

    public PortableStorageInterfaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.transferTimer = 0;
        this.connectionAnimation = LerpedFloat.linear().startWithValue(0.0);
        this.powered = false;
    }

    public void startTransferringTo(Contraption contraption, float distance) {
        if (this.connectedEntity != contraption.entity) {
            this.distance = Math.min(2.0F, distance);
            this.connectedEntity = contraption.entity;
            this.startConnecting();
            this.notifyUpdate();
        }
    }

    protected void stopTransferring() {
        this.connectedEntity = null;
        this.f_58857_.updateNeighborsAt(this.f_58858_, this.m_58900_().m_60734_());
    }

    public boolean canTransfer() {
        if (this.connectedEntity != null && !this.connectedEntity.isAlive()) {
            this.stopTransferring();
        }
        return this.connectedEntity != null && this.isConnected();
    }

    @Override
    public void initialize() {
        super.initialize();
        this.powered = this.f_58857_.m_276867_(this.f_58858_);
        if (!this.powered) {
            this.notifyContraptions();
        }
    }

    protected abstract void invalidateCapability();

    @Override
    public void tick() {
        super.tick();
        boolean wasConnected = this.isConnected();
        int timeUnit = this.getTransferTimeout();
        int animation = 4;
        if (this.keepAlive > 0) {
            this.keepAlive--;
            if (this.keepAlive == 0 && !this.f_58857_.isClientSide) {
                this.stopTransferring();
                this.transferTimer = 3;
                this.sendData();
                return;
            }
        }
        this.transferTimer = Math.min(this.transferTimer, 8 + timeUnit);
        boolean timerCanDecrement = this.transferTimer > 4 || this.transferTimer > 0 && this.keepAlive == 0 && (this.isVirtual() || !this.f_58857_.isClientSide || this.transferTimer != 4);
        if (timerCanDecrement && (!this.isVirtual() || this.transferTimer != 4)) {
            this.transferTimer--;
            if (this.transferTimer == 3) {
                this.sendData();
            }
            if (this.transferTimer <= 0 || this.powered) {
                this.stopTransferring();
            }
        }
        boolean isConnected = this.isConnected();
        if (wasConnected != isConnected && !this.f_58857_.isClientSide) {
            this.m_6596_();
        }
        float progress = 0.0F;
        if (isConnected) {
            progress = 1.0F;
        } else if (this.transferTimer >= timeUnit + animation) {
            progress = Mth.lerp((float) (this.transferTimer - timeUnit - animation) / (float) animation, 1.0F, 0.0F);
        } else if (this.transferTimer < animation) {
            progress = Mth.lerp((float) this.transferTimer / (float) animation, 0.0F, 1.0F);
        }
        this.connectionAnimation.setValue((double) progress);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.invalidateCapability();
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.transferTimer = compound.getInt("Timer");
        this.distance = compound.getFloat("Distance");
        boolean poweredPreviously = this.powered;
        this.powered = compound.getBoolean("Powered");
        if (clientPacket && this.powered != poweredPreviously && !this.powered) {
            this.notifyContraptions();
        }
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("Timer", this.transferTimer);
        compound.putFloat("Distance", this.distance);
        compound.putBoolean("Powered", this.powered);
    }

    public void neighbourChanged() {
        boolean isBlockPowered = this.f_58857_.m_276867_(this.f_58858_);
        if (isBlockPowered != this.powered) {
            this.powered = isBlockPowered;
            if (!this.powered) {
                this.notifyContraptions();
            }
            if (this.powered) {
                this.stopTransferring();
            }
            this.sendData();
        }
    }

    private void notifyContraptions() {
        this.f_58857_.m_45976_(AbstractContraptionEntity.class, new AABB(this.f_58858_).inflate(3.0)).forEach(AbstractContraptionEntity::refreshPSIs);
    }

    public boolean isPowered() {
        return this.powered;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(2.0);
    }

    public boolean isTransferring() {
        return this.transferTimer > 4;
    }

    boolean isConnected() {
        int timeUnit = this.getTransferTimeout();
        return this.transferTimer >= 4 && this.transferTimer <= timeUnit + 4;
    }

    float getExtensionDistance(float partialTicks) {
        return (float) (Math.pow((double) this.connectionAnimation.getValue(partialTicks), 2.0) * (double) this.distance / 2.0);
    }

    float getConnectionDistance() {
        return this.distance;
    }

    public void startConnecting() {
        this.transferTimer = 14;
    }

    public void onContentTransferred() {
        int timeUnit = this.getTransferTimeout();
        this.transferTimer = timeUnit + 4;
        this.award(AllAdvancements.PSI);
        this.sendData();
    }

    protected Integer getTransferTimeout() {
        return AllConfigs.server().logistics.psiTimeout.get();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.PSI });
    }
}