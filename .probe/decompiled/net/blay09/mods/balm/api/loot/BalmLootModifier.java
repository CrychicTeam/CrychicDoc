package net.blay09.mods.balm.api.loot;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

@FunctionalInterface
public interface BalmLootModifier {

    void apply(LootContext var1, List<ItemStack> var2);
}