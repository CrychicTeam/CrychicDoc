package com.almostreliable.morejs.features.villager;

import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

@RemapPrefixForJS("morejs$")
public interface OfferExtension {

    boolean morejs$isDisabled();

    void morejs$setDisabled(boolean var1);

    ItemStack morejs$getFirstInput();

    void morejs$setFirstInput(ItemStack var1);

    ItemStack morejs$getSecondInput();

    void morejs$setSecondInput(ItemStack var1);

    ItemStack morejs$getOutput();

    void morejs$setOutput(ItemStack var1);

    void morejs$setMaxUses(int var1);

    void morejs$setDemand(int var1);

    void morejs$setVillagerExperience(int var1);

    void morejs$setPriceMultiplier(float var1);

    void morejs$setRewardExp(boolean var1);

    boolean morejs$isRewardingExp();

    default void morejs$replaceEmeralds(Item replacement) {
        if (this.morejs$getFirstInput().getItem() == Items.EMERALD) {
            this.morejs$setFirstInput(new ItemStack(replacement, this.morejs$getFirstInput().getCount()));
        }
        if (this.morejs$getSecondInput().getItem() == Items.EMERALD) {
            this.morejs$setSecondInput(new ItemStack(replacement, this.morejs$getSecondInput().getCount()));
        }
        if (this.morejs$getOutput().getItem() == Items.EMERALD) {
            this.morejs$setOutput(new ItemStack(replacement, this.morejs$getOutput().getCount()));
        }
    }

    default void morejs$replaceItems(Ingredient filter, ItemStack itemStack) {
        if (filter.test(this.morejs$getFirstInput())) {
            this.morejs$setFirstInput(itemStack.copy());
        }
        if (filter.test(this.morejs$getSecondInput())) {
            this.morejs$setSecondInput(itemStack.copy());
        }
        if (filter.test(this.morejs$getOutput())) {
            this.morejs$setOutput(itemStack.copy());
        }
    }
}