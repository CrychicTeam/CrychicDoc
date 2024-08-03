package com.simibubi.create.content.kinetics.gauge;

import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.compat.computercraft.ComputerCraftProxy;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpeedGaugeBlockEntity extends GaugeBlockEntity {

    public AbstractComputerBehaviour computerBehaviour;

    public SpeedGaugeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(this.computerBehaviour = ComputerCraftProxy.behaviour(this));
    }

    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        float speed = Math.abs(this.getSpeed());
        this.dialTarget = getDialTarget(speed);
        this.color = Color.mixColors(IRotate.SpeedLevel.of(speed).getColor(), 16777215, 0.25F);
        this.m_6596_();
    }

    public static float getDialTarget(float speed) {
        speed = Math.abs(speed);
        float medium = AllConfigs.server().kinetics.mediumSpeed.get().floatValue();
        float fast = AllConfigs.server().kinetics.fastSpeed.get().floatValue();
        float max = AllConfigs.server().kinetics.maxRotationSpeed.get().floatValue();
        float target = 0.0F;
        if (speed == 0.0F) {
            target = 0.0F;
        } else if (speed < medium) {
            target = Mth.lerp(speed / medium, 0.0F, 0.45F);
        } else if (speed < fast) {
            target = Mth.lerp((speed - medium) / (fast - medium), 0.45F, 0.75F);
        } else {
            target = Mth.lerp((speed - fast) / (max - fast), 0.75F, 1.125F);
        }
        return target;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        Lang.translate("gui.speedometer.title").style(ChatFormatting.GRAY).forGoggles(tooltip);
        IRotate.SpeedLevel.getFormattedSpeedText(this.speed, this.isOverStressed()).forGoggles(tooltip);
        return true;
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