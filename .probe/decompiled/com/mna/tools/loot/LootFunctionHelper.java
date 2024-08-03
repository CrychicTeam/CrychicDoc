package com.mna.tools.loot;

import java.util.stream.IntStream;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class LootFunctionHelper {

    private static final int STATISTICAL_TEST = 100;

    public static void applyFunction(ServerLevel serverLevel, LootItemFunction lootFunction, LootDrop lootDrop) {
        LootContext randContext = new LootContext.Builder(new LootParams.Builder(serverLevel).create(LootContextParamSets.EMPTY)).create(null);
        if (lootFunction instanceof SetItemCountFunction) {
            lootDrop.minDrop = getMin(serverLevel, ((SetItemCountFunction) lootFunction).value);
            if (lootDrop.minDrop < 0) {
                lootDrop.minDrop = 0;
            }
            lootDrop.item.setCount(Math.max(lootDrop.minDrop, 1));
            lootDrop.maxDrop = getMax(serverLevel, ((SetItemCountFunction) lootFunction).value);
        } else if (lootFunction instanceof SetItemDamageFunction) {
            ((SetItemDamageFunction) lootFunction).run(lootDrop.item, randContext);
        } else if (lootFunction instanceof EnchantRandomlyFunction || lootFunction instanceof EnchantWithLevelsFunction) {
            lootDrop.enchanted = true;
        } else if (!(lootFunction instanceof SmeltItemFunction)) {
            if (lootFunction instanceof LootingEnchantFunction) {
                lootDrop.addConditional(Conditional.affectedByLooting);
            } else if (lootFunction instanceof ICustomLootFunction) {
                ((ICustomLootFunction) lootFunction).apply(lootDrop);
            } else {
                try {
                    lootDrop.item = (ItemStack) lootFunction.apply(lootDrop.item, null);
                } catch (NullPointerException var5) {
                }
            }
        }
    }

    public static int getMin(ServerLevel serverLevel, NumberProvider randomRange) {
        LootContext randContext = new LootContext.Builder(new LootParams.Builder(serverLevel).create(LootContextParamSets.EMPTY)).create(null);
        if (randomRange instanceof ConstantValue) {
            return randomRange.getInt(randContext);
        } else if (randomRange instanceof UniformGenerator) {
            return Mth.floor((float) ((UniformGenerator) randomRange).min.getInt(randContext));
        } else {
            return randomRange instanceof BinomialDistributionGenerator ? 0 : IntStream.iterate(0, i -> randomRange.getInt(randContext)).limit(100L).min().orElse(0);
        }
    }

    public static int getMax(ServerLevel serverLevel, NumberProvider randomRange) {
        LootContext randContext = new LootContext.Builder(new LootParams.Builder(serverLevel).create(LootContextParamSets.EMPTY)).create(null);
        if (randomRange instanceof ConstantValue) {
            return randomRange.getInt(randContext);
        } else if (randomRange instanceof UniformGenerator) {
            return Mth.floor((float) ((UniformGenerator) randomRange).max.getInt(randContext));
        } else {
            return randomRange instanceof BinomialDistributionGenerator ? ((BinomialDistributionGenerator) randomRange).n.getInt(randContext) : IntStream.iterate(0, i -> randomRange.getInt(randContext)).limit(100L).max().orElse(0);
        }
    }
}