package com.almostreliable.lootjs.core;

import java.util.List;
import java.util.Map;
import net.minecraft.world.item.ItemStack;

public interface ILootContextData {

    LootContextType getLootContextType();

    boolean isCanceled();

    void setCanceled(boolean var1);

    Map<String, Object> getCustomData();

    List<ItemStack> getGeneratedLoot();

    void setGeneratedLoot(List<ItemStack> var1);

    void reset();
}