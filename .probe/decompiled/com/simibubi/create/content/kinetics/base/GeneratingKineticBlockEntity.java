package com.simibubi.create.content.kinetics.base;

import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class GeneratingKineticBlockEntity extends KineticBlockEntity {

    public boolean reActivateSource;

    public GeneratingKineticBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected void notifyStressCapacityChange(float capacity) {
        this.getOrCreateNetwork().updateCapacityFor(this, capacity);
    }

    @Override
    public void removeSource() {
        if (this.hasSource() && this.isSource()) {
            this.reActivateSource = true;
        }
        super.removeSource();
    }

    @Override
    public void setSource(BlockPos source) {
        super.setSource(source);
        if (this.f_58857_.getBlockEntity(source) instanceof KineticBlockEntity sourceBE) {
            if (this.reActivateSource && Math.abs(sourceBE.getSpeed()) >= Math.abs(this.getGeneratedSpeed())) {
                this.reActivateSource = false;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.reActivateSource) {
            this.updateGeneratedRotation();
            this.reActivateSource = false;
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if (!IRotate.StressImpact.isEnabled()) {
            return added;
        } else {
            float stressBase = this.calculateAddedStressCapacity();
            if (Mth.equal(stressBase, 0.0F)) {
                return added;
            } else {
                Lang.translate("gui.goggles.generator_stats").forGoggles(tooltip);
                Lang.translate("tooltip.capacityProvided").style(ChatFormatting.GRAY).forGoggles(tooltip);
                float speed = this.getTheoreticalSpeed();
                if (speed != this.getGeneratedSpeed() && speed != 0.0F) {
                    stressBase *= this.getGeneratedSpeed() / speed;
                }
                speed = Math.abs(speed);
                float stressTotal = stressBase * speed;
                Lang.number((double) stressTotal).translate("generic.unit.stress").style(ChatFormatting.AQUA).space().add(Lang.translate("gui.goggles.at_current_speed").style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
                return true;
            }
        }
    }

    public void updateGeneratedRotation() {
        float speed = this.getGeneratedSpeed();
        float prevSpeed = this.speed;
        if (this.f_58857_ != null && !this.f_58857_.isClientSide) {
            if (prevSpeed != speed) {
                if (!this.hasSource()) {
                    IRotate.SpeedLevel levelBefore = IRotate.SpeedLevel.of(this.speed);
                    IRotate.SpeedLevel levelafter = IRotate.SpeedLevel.of(speed);
                    if (levelBefore != levelafter) {
                        this.effects.queueRotationIndicators();
                    }
                }
                this.applyNewSpeed(prevSpeed, speed);
            }
            if (this.hasNetwork() && speed != 0.0F) {
                KineticNetwork network = this.getOrCreateNetwork();
                this.notifyStressCapacityChange(this.calculateAddedStressCapacity());
                this.getOrCreateNetwork().updateStressFor(this, this.calculateStressApplied());
                network.updateStress();
            }
            this.onSpeedChanged(prevSpeed);
            this.sendData();
        }
    }

    public void applyNewSpeed(float prevSpeed, float speed) {
        if (speed == 0.0F) {
            if (this.hasSource()) {
                this.notifyStressCapacityChange(0.0F);
                this.getOrCreateNetwork().updateStressFor(this, this.calculateStressApplied());
            } else {
                this.detachKinetics();
                this.setSpeed(0.0F);
                this.setNetwork(null);
            }
        } else if (prevSpeed == 0.0F) {
            this.setSpeed(speed);
            this.setNetwork(this.createNetworkId());
            this.attachKinetics();
        } else if (this.hasSource()) {
            if (Math.abs(prevSpeed) >= Math.abs(speed)) {
                if (Math.signum(prevSpeed) != Math.signum(speed)) {
                    this.f_58857_.m_46961_(this.f_58858_, true);
                }
            } else {
                this.detachKinetics();
                this.setSpeed(speed);
                this.source = null;
                this.setNetwork(this.createNetworkId());
                this.attachKinetics();
            }
        } else {
            this.detachKinetics();
            this.setSpeed(speed);
            this.attachKinetics();
        }
    }

    public Long createNetworkId() {
        return this.f_58858_.asLong();
    }
}