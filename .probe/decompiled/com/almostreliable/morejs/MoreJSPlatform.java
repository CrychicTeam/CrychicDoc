package com.almostreliable.morejs;

import com.almostreliable.morejs.features.villager.TradingManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface MoreJSPlatform {

    Platform getPlatform();

    boolean isModLoaded(String var1);

    boolean isDevelopmentEnvironment();

    float getEnchantmentPower(Level var1, BlockPos var2);

    int getEnchantmentCost(Level var1, BlockPos var2, int var3, int var4, ItemStack var5, int var6);

    TradingManager getTradingManager();
}