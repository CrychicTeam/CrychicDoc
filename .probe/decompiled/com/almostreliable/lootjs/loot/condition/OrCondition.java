package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.core.ILootCondition;
import com.almostreliable.lootjs.core.ILootContextData;
import com.almostreliable.lootjs.core.LootJSParamSets;
import com.almostreliable.lootjs.loot.results.Info;
import com.almostreliable.lootjs.loot.results.LootInfoCollector;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class OrCondition implements IExtendedLootCondition {

    private final ILootCondition[] conditions;

    public OrCondition(ILootCondition... conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        LootInfoCollector collector = context.getParamOrNull(LootJSParamSets.RESULT_COLLECTOR);
        for (ILootCondition condition : this.conditions) {
            Info info = LootInfoCollector.create(collector, condition);
            boolean result = condition.applyLootHandler(context, loot);
            LootInfoCollector.finalizeInfo(collector, info, result);
            if (result) {
                return true;
            }
        }
        return false;
    }

    public boolean test(LootContext context) {
        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
        return this.applyLootHandler(context, (List<ItemStack>) (data == null ? new ArrayList() : data.getGeneratedLoot()));
    }
}