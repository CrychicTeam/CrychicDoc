package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.core.ILootContextData;
import com.almostreliable.lootjs.core.LootJSParamSets;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class ContainsLootCondition implements IExtendedLootCondition {

    private final Predicate<ItemStack> predicate;

    private final boolean exact;

    public ContainsLootCondition(Predicate<ItemStack> predicate, boolean exact) {
        this.predicate = predicate;
        this.exact = exact;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        return this.exact ? this.matchExact(loot) : this.match(loot);
    }

    public boolean test(LootContext context) {
        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
        return data == null ? false : this.applyLootHandler(context, data.getGeneratedLoot());
    }

    private boolean match(List<ItemStack> generatedLoot) {
        for (ItemStack itemStack : generatedLoot) {
            if (this.predicate.test(itemStack)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchExact(List<ItemStack> generatedLoot) {
        for (ItemStack itemStack : generatedLoot) {
            if (!this.predicate.test(itemStack)) {
                return false;
            }
        }
        return true;
    }
}