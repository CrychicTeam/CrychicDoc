package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.loot.action.LootItemFunctionWrapperAction;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.functions.SetLoreFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public interface LootFunctionsContainer<F extends LootFunctionsContainer<?>> {

    default F enchantRandomly() {
        return this.enchantRandomly(new Enchantment[0]);
    }

    default F enchantRandomly(Enchantment[] enchantments) {
        EnchantRandomlyFunction.Builder enchantRandomlyFunctionBuilder = EnchantRandomlyFunction.randomEnchantment();
        for (Enchantment enchantment : enchantments) {
            enchantRandomlyFunctionBuilder.withEnchantment(enchantment);
        }
        return this.addFunction(enchantRandomlyFunctionBuilder);
    }

    default F enchantWithLevels(NumberProvider numberProvider) {
        return this.enchantWithLevels(numberProvider, true);
    }

    default F enchantWithLevels(NumberProvider numberProvider, boolean allowTreasure) {
        EnchantWithLevelsFunction.Builder ewlBuilder = EnchantWithLevelsFunction.enchantWithLevels(numberProvider);
        if (allowTreasure) {
            ewlBuilder.allowTreasure();
        }
        return this.addFunction(ewlBuilder);
    }

    default F applyLootingBonus(NumberProvider numberProvider) {
        LootingEnchantFunction.Builder lootingEnchantBuilder = LootingEnchantFunction.lootingMultiplier(numberProvider);
        return this.addFunction(lootingEnchantBuilder);
    }

    default F applyBinomialDistributionBonus(Enchantment enchantment, float probability, int n) {
        LootItemConditionalFunction.Builder<?> applyBonusBuilder = ApplyBonusCount.addBonusBinomialDistributionCount(enchantment, probability, n);
        return this.addFunction(applyBonusBuilder);
    }

    default F applyOreBonus(Enchantment enchantment) {
        LootItemConditionalFunction.Builder<?> applyBonusBuilder = ApplyBonusCount.addOreBonusCount(enchantment);
        return this.addFunction(applyBonusBuilder);
    }

    default F applyBonus(Enchantment enchantment, int multiplier) {
        LootItemConditionalFunction.Builder<?> applyBonusBuilder = ApplyBonusCount.addUniformBonusCount(enchantment, multiplier);
        return this.addFunction(applyBonusBuilder);
    }

    default F simulateExplosionDecay() {
        return this.addFunction(ApplyExplosionDecay.explosionDecay());
    }

    default F smeltLoot() {
        return this.addFunction(SmeltItemFunction.smelted());
    }

    default F damage(NumberProvider numberProvider) {
        return this.addFunction(SetItemDamageFunction.setDamage(numberProvider));
    }

    default F addPotion(Potion potion) {
        Objects.requireNonNull(potion);
        return this.addFunction(SetPotionFunction.setPotion(potion));
    }

    default F addAttributes(Consumer<AddAttributesFunction.Builder> action) {
        AddAttributesFunction.Builder builder = new AddAttributesFunction.Builder();
        action.accept(builder);
        return this.addFunction(builder);
    }

    default F limitCount(@Nullable NumberProvider numberProviderMin, @Nullable NumberProvider numberProviderMax) {
        IntRange intRange = new IntRange(numberProviderMin, numberProviderMax);
        return this.addFunction(LimitCount.limitCount(intRange));
    }

    default F limitCount(NumberProvider numberProvider) {
        return this.addFunction(SetItemCountFunction.setCount(numberProvider));
    }

    default F addLore(Component... components) {
        SetLoreFunction.Builder builder = SetLoreFunction.setLore();
        for (Component c : components) {
            builder.addLine(c);
        }
        return this.addFunction(builder);
    }

    default F replaceLore(Component... components) {
        SetLoreFunction.Builder builder = SetLoreFunction.setLore();
        for (Component c : components) {
            builder.addLine(c);
        }
        return this.addFunction(builder.setReplace(true));
    }

    default F setName(Component component) {
        return this.addFunction(SetNameFunction.setName(component));
    }

    default F addNBT(CompoundTag tag) {
        return this.addFunction(SetNbtFunction.setTag(tag));
    }

    default F addNbt(CompoundTag tag) {
        return this.addFunction(SetNbtFunction.setTag(tag));
    }

    default F customFunction(JsonObject json) {
        LootItemFunction function = (LootItemFunction) LootJS.FUNCTION_GSON.fromJson(json, LootItemFunction.class);
        return this.addFunction(function);
    }

    default F functions(ItemFilter filter, Consumer<LootFunctionsContainer<F>> action) {
        final List<LootItemFunction> functions = new ArrayList();
        LootFunctionsContainer<F> lfc = new LootFunctionsContainer<F>() {

            @Override
            public F addFunction(LootItemFunction lootItemFunction) {
                functions.add(lootItemFunction);
                return (F) this;
            }

            @Override
            public F functions(ItemFilter filter, Consumer<LootFunctionsContainer<F>> action) {
                throw new UnsupportedOperationException("Nested `filteredFunctions` are not supported.");
            }
        };
        action.accept(lfc);
        return this.addFunction(new LootItemFunctionWrapperAction.CompositeLootItemFunction(functions, filter));
    }

    default F addFunction(LootItemFunction.Builder builder) {
        return this.addFunction(builder.build());
    }

    F addFunction(LootItemFunction var1);
}