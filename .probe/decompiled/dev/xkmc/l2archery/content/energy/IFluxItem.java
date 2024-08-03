package dev.xkmc.l2archery.content.energy;

import dev.xkmc.l2archery.content.feature.bow.FluxFeature;
import dev.xkmc.l2archery.init.data.LangData;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.energy.IEnergyStorage;

public interface IFluxItem extends IEnergyContainerItem, IForgeItem {

    @Override
    default Capability<? extends IEnergyStorage> getEnergyCapability() {
        return ForgeCapabilities.ENERGY;
    }

    @Nullable
    FluxFeature getFluxFeature(ItemStack var1);

    int getStorageRank(ItemStack var1);

    int getConsumptionRank(ItemStack var1);

    @Override
    default int getExtract(ItemStack container) {
        FluxFeature fluxFeature = this.getFluxFeature(container);
        return fluxFeature == null ? 0 : fluxFeature.extract();
    }

    @Override
    default int getReceive(ItemStack container) {
        FluxFeature fluxFeature = this.getFluxFeature(container);
        return fluxFeature == null ? 0 : fluxFeature.receive();
    }

    @Override
    default int getMaxEnergyStored(ItemStack container) {
        FluxFeature fluxFeature = this.getFluxFeature(container);
        return fluxFeature == null ? 0 : (int) (Math.pow(2.0, (double) this.getStorageRank(container)) * (double) fluxFeature.maxEnergy());
    }

    default int getEnergyPerUse(ItemStack container) {
        FluxFeature fluxFeature = this.getFluxFeature(container);
        return fluxFeature == null ? 100 : (int) (Math.pow(2.0, (double) this.getConsumptionRank(container)) * (double) fluxFeature.perUsed());
    }

    default boolean hasEnergy(ItemStack stack, int amount) {
        return this.getEnergyStored(stack) >= amount;
    }

    default boolean useEnergy(ItemStack stack, int amount, boolean simulate) {
        if (simulate) {
            return this.hasEnergy(stack, amount);
        } else if (this.hasEnergy(stack, amount)) {
            this.extractEnergy(stack, amount, false);
            return true;
        } else {
            return false;
        }
    }

    default boolean useEnergy(ItemStack stack, int amount, Entity entity) {
        if (entity instanceof Player player && player.isCreative()) {
            return true;
        }
        return this.useEnergy(stack, amount, false);
    }

    @Override
    default <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return this.useEnergy(stack, amount * this.getEnergyPerUse(stack), entity) ? 0 : amount;
    }

    @Override
    default ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new EnergyContainerItemWrapper(stack, this, this.getEnergyCapability());
    }

    default void tooltipDelegate(ItemStack stack, List<Component> tooltip) {
        if (this.getFluxFeature(stack) != null) {
            tooltip.add(LangData.ENERGY_STORED.get(getScaledNumber((long) this.getEnergyStored(stack)), getScaledNumber((long) this.getMaxEnergyStored(stack))));
            tooltip.add(LangData.ENERGY_CONSUME.get(getScaledNumber((long) this.getEnergyPerUse(stack))));
        }
    }

    static String getScaledNumber(long number) {
        if (number >= 1000000000L) {
            return number / 1000000000L + "." + number % 1000000000L / 100000000L + number % 100000000L / 10000000L + "G";
        } else if (number >= 1000000L) {
            return number / 1000000L + "." + number % 1000000L / 100000L + number % 100000L / 10000L + "M";
        } else {
            return number >= 1000L ? number / 1000L + "." + number % 1000L / 100L + number % 100L / 10L + "k" : String.valueOf(number);
        }
    }
}