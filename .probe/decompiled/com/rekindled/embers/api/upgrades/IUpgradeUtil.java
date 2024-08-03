package com.rekindled.embers.api.upgrades;

import com.rekindled.embers.api.event.UpgradeEvent;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

public interface IUpgradeUtil {

    List<UpgradeContext> getUpgrades(Level var1, BlockPos var2, Direction[] var3);

    void getUpgrades(Level var1, BlockPos var2, Direction[] var3, List<UpgradeContext> var4);

    void collectUpgrades(Level var1, BlockPos var2, Direction var3, List<UpgradeContext> var4);

    void collectUpgrades(Level var1, BlockPos var2, Direction var3, List<UpgradeContext> var4, int var5);

    void verifyUpgrades(BlockEntity var1, List<UpgradeContext> var2);

    int getWorkTime(BlockEntity var1, int var2, List<UpgradeContext> var3);

    double getTotalSpeedModifier(BlockEntity var1, List<UpgradeContext> var2);

    boolean doTick(BlockEntity var1, List<UpgradeContext> var2);

    boolean doWork(BlockEntity var1, List<UpgradeContext> var2);

    double getTotalEmberConsumption(BlockEntity var1, double var2, List<UpgradeContext> var4);

    double getTotalEmberProduction(BlockEntity var1, double var2, List<UpgradeContext> var4);

    void transformOutput(BlockEntity var1, List<ItemStack> var2, List<UpgradeContext> var3);

    FluidStack transformOutput(BlockEntity var1, FluidStack var2, List<UpgradeContext> var3);

    boolean getOtherParameter(BlockEntity var1, String var2, boolean var3, List<UpgradeContext> var4);

    double getOtherParameter(BlockEntity var1, String var2, double var3, List<UpgradeContext> var5);

    int getOtherParameter(BlockEntity var1, String var2, int var3, List<UpgradeContext> var4);

    String getOtherParameter(BlockEntity var1, String var2, String var3, List<UpgradeContext> var4);

    <T> T getOtherParameter(BlockEntity var1, String var2, T var3, List<UpgradeContext> var4);

    void throwEvent(BlockEntity var1, UpgradeEvent var2, List<UpgradeContext> var3);
}