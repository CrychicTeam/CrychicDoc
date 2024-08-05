package com.yungnickyoung.minecraft.yungsapi.services;

import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;

public interface IAutoRegisterHelper {

    void invokeAllAutoRegisterMethods(String var1);

    void collectAllAutoRegisterFieldsInPackage(String var1);

    void processQueuedAutoRegEntries();

    void registerBrewingRecipe(Supplier<Potion> var1, Supplier<Item> var2, Supplier<Potion> var3);

    void addCompostableItem(Supplier<Item> var1, float var2);
}