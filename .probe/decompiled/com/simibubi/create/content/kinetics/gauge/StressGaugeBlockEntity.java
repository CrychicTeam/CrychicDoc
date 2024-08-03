package com.simibubi.create.content.kinetics.gauge;

import com.simibubi.create.AllPackets;
import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.compat.computercraft.ComputerCraftProxy;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StressGaugeBlockEntity extends GaugeBlockEntity {

    public AbstractComputerBehaviour computerBehaviour;

    static BlockPos lastSent;

    public StressGaugeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(this.computerBehaviour = ComputerCraftProxy.behaviour(this));
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.STRESSOMETER, AllAdvancements.STRESSOMETER_MAXED });
    }

    @Override
    public void updateFromNetwork(float maxStress, float currentStress, int networkSize) {
        super.updateFromNetwork(maxStress, currentStress, networkSize);
        if (!IRotate.StressImpact.isEnabled()) {
            this.dialTarget = 0.0F;
        } else if (this.isOverStressed()) {
            this.dialTarget = 1.125F;
        } else if (maxStress == 0.0F) {
            this.dialTarget = 0.0F;
        } else {
            this.dialTarget = currentStress / maxStress;
        }
        if (this.dialTarget > 0.0F) {
            if (this.dialTarget < 0.5F) {
                this.color = Color.mixColors(65280, 16776960, this.dialTarget * 2.0F);
            } else if (this.dialTarget < 1.0F) {
                this.color = Color.mixColors(16776960, 16711680, this.dialTarget * 2.0F - 1.0F);
            } else {
                this.color = 16711680;
            }
        }
        this.sendData();
        this.m_6596_();
    }

    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        if (this.getSpeed() == 0.0F) {
            this.dialTarget = 0.0F;
            this.m_6596_();
        } else {
            this.updateFromNetwork(this.capacity, this.stress, this.getOrCreateNetwork().getSize());
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (!IRotate.StressImpact.isEnabled()) {
            return false;
        } else {
            super.addToGoggleTooltip(tooltip, isPlayerSneaking);
            double capacity = (double) this.getNetworkCapacity();
            double stressFraction = (double) this.getNetworkStress() / (capacity == 0.0 ? 1.0 : capacity);
            Lang.translate("gui.stressometer.title").style(ChatFormatting.GRAY).forGoggles(tooltip);
            if (this.getTheoreticalSpeed() == 0.0F) {
                Lang.text(TooltipHelper.makeProgressBar(3, 0)).translate("gui.stressometer.no_rotation").style(ChatFormatting.DARK_GRAY).forGoggles(tooltip);
            } else {
                IRotate.StressImpact.getFormattedStressText(stressFraction).forGoggles(tooltip);
                Lang.translate("gui.stressometer.capacity").style(ChatFormatting.GRAY).forGoggles(tooltip);
                double remainingCapacity = capacity - (double) this.getNetworkStress();
                LangBuilder su = Lang.translate("generic.unit.stress");
                LangBuilder stressTip = Lang.number(remainingCapacity).add(su).style(IRotate.StressImpact.of(stressFraction).getRelativeColor());
                if (remainingCapacity != capacity) {
                    stressTip.text(ChatFormatting.GRAY, " / ").add(Lang.number(capacity).add(su).style(ChatFormatting.DARK_GRAY));
                }
                stressTip.forGoggles(tooltip, 1);
            }
            if (!this.f_58858_.equals(lastSent)) {
                AllPackets.getChannel().sendToServer(new GaugeObservedPacket(lastSent = this.f_58858_));
            }
            return true;
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if (clientPacket && this.f_58858_ != null && this.f_58858_.equals(lastSent)) {
            lastSent = null;
        }
    }

    public float getNetworkStress() {
        return this.stress;
    }

    public float getNetworkCapacity() {
        return this.capacity;
    }

    public void onObserved() {
        this.award(AllAdvancements.STRESSOMETER);
        if (Mth.equal(this.dialTarget, 1.0F)) {
            this.award(AllAdvancements.STRESSOMETER_MAXED);
        }
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return this.computerBehaviour.isPeripheralCap(cap) ? this.computerBehaviour.getPeripheralCapability() : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.computerBehaviour.removePeripheral();
    }
}