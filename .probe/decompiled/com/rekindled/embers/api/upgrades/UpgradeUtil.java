package com.rekindled.embers.api.upgrades;

import com.rekindled.embers.api.event.UpgradeEvent;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

public class UpgradeUtil {

    public static IUpgradeUtil IMPL;

    public static List<UpgradeContext> getUpgrades(Level world, BlockPos pos, Direction[] facings) {
        return IMPL.getUpgrades(world, pos, facings);
    }

    public static void getUpgrades(Level world, BlockPos pos, Direction[] facings, List<UpgradeContext> upgrades) {
        IMPL.getUpgrades(world, pos, facings, upgrades);
    }

    public static void collectUpgrades(Level world, BlockPos pos, Direction side, List<UpgradeContext> upgrades) {
        IMPL.collectUpgrades(world, pos, side, upgrades);
    }

    public static void collectUpgrades(Level world, BlockPos pos, Direction side, List<UpgradeContext> upgrades, int distanceLeft) {
        IMPL.collectUpgrades(world, pos, side, upgrades, distanceLeft);
    }

    public static void verifyUpgrades(BlockEntity tile, List<UpgradeContext> list) {
        IMPL.verifyUpgrades(tile, list);
    }

    public static double getTotalSpeedModifier(BlockEntity tile, List<UpgradeContext> list) {
        return IMPL.getTotalSpeedModifier(tile, list);
    }

    public static int getWorkTime(BlockEntity tile, int time, List<UpgradeContext> list) {
        return IMPL.getWorkTime(tile, time, list);
    }

    public static boolean doWork(BlockEntity tile, List<UpgradeContext> list) {
        return IMPL.doWork(tile, list);
    }

    public static boolean doTick(BlockEntity tile, List<UpgradeContext> list) {
        return IMPL.doTick(tile, list);
    }

    public static double getTotalEmberConsumption(BlockEntity tile, double ember, List<UpgradeContext> list) {
        return IMPL.getTotalEmberConsumption(tile, ember, list);
    }

    public static double getTotalEmberProduction(BlockEntity tile, double ember, List<UpgradeContext> list) {
        return IMPL.getTotalEmberProduction(tile, ember, list);
    }

    public static void transformOutput(BlockEntity tile, List<ItemStack> outputs, List<UpgradeContext> list) {
        IMPL.transformOutput(tile, outputs, list);
    }

    public static FluidStack transformOutput(BlockEntity tile, FluidStack output, List<UpgradeContext> list) {
        return IMPL.transformOutput(tile, output, list);
    }

    public static boolean getOtherParameter(BlockEntity tile, String type, boolean initial, List<UpgradeContext> list) {
        return IMPL.getOtherParameter(tile, type, initial, list);
    }

    public static double getOtherParameter(BlockEntity tile, String type, double initial, List<UpgradeContext> list) {
        return IMPL.getOtherParameter(tile, type, initial, list);
    }

    public static int getOtherParameter(BlockEntity tile, String type, int initial, List<UpgradeContext> list) {
        return IMPL.getOtherParameter(tile, type, initial, list);
    }

    public static String getOtherParameter(BlockEntity tile, String type, String initial, List<UpgradeContext> list) {
        return IMPL.getOtherParameter(tile, type, initial, list);
    }

    public static <T> T getOtherParameter(BlockEntity tile, String type, T initial, List<UpgradeContext> list) {
        return IMPL.getOtherParameter(tile, type, initial, list);
    }

    public static void throwEvent(BlockEntity tile, UpgradeEvent event, List<UpgradeContext> list) {
        IMPL.throwEvent(tile, event, list);
    }
}