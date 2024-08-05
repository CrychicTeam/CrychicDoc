package com.rekindled.embers.api.upgrades;

import com.rekindled.embers.api.event.UpgradeEvent;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

public interface IUpgradeProvider {

    ResourceLocation getUpgradeId();

    default int getPriority() {
        return 0;
    }

    default int getLimit(BlockEntity tile) {
        return Integer.MAX_VALUE;
    }

    default double getSpeed(BlockEntity tile, double speed, int distance, int count) {
        return speed;
    }

    default boolean doTick(BlockEntity tile, List<UpgradeContext> upgrades, int distance, int count) {
        return false;
    }

    default boolean doWork(BlockEntity tile, List<UpgradeContext> upgrades, int distance, int count) {
        return false;
    }

    default double transformEmberConsumption(BlockEntity tile, double ember, int distance, int count) {
        return ember;
    }

    default double transformEmberProduction(BlockEntity tile, double ember, int distance, int count) {
        return ember;
    }

    default void transformOutput(BlockEntity tile, List<ItemStack> outputs, int distance, int count) {
    }

    default FluidStack transformOutput(BlockEntity tile, FluidStack output, int distance, int count) {
        return output;
    }

    default boolean getOtherParameter(BlockEntity tile, String type, boolean value, int distance, int count) {
        return value;
    }

    default double getOtherParameter(BlockEntity tile, String type, double value, int distance, int count) {
        return value;
    }

    default int getOtherParameter(BlockEntity tile, String type, int value, int distance, int count) {
        return value;
    }

    default String getOtherParameter(BlockEntity tile, String type, String value, int distance, int count) {
        return value;
    }

    default <T> T getOtherParameter(BlockEntity tile, String type, T value, int distance, int count) {
        return value;
    }

    default void throwEvent(BlockEntity tile, List<UpgradeContext> upgrades, UpgradeEvent event, int distance, int count) {
    }
}