package com.almostreliable.lootjs.loot.action;

import com.almostreliable.lootjs.core.ILootHandler;
import com.almostreliable.lootjs.core.LootJSParamSets;
import com.almostreliable.lootjs.loot.results.Icon;
import com.almostreliable.lootjs.loot.results.Info;
import com.almostreliable.lootjs.loot.results.LootInfoCollector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class GroupedLootAction extends CompositeLootAction {

    protected final NumberProvider numberProvider;

    public GroupedLootAction(NumberProvider numberProvider, Collection<ILootHandler> handlers) {
        super(handlers);
        this.numberProvider = numberProvider;
    }

    @Override
    public boolean applyLootHandler(LootContext context, List<ItemStack> loot) {
        LootInfoCollector collector = context.getParamOrNull(LootJSParamSets.RESULT_COLLECTOR);
        int rolls = this.numberProvider.getInt(context);
        for (int i = 1; i <= rolls; i++) {
            Info info = this.createInfoIfMultipleRolls(rolls, i, collector);
            List<ItemStack> poolLoot = new ArrayList();
            this.run(context, poolLoot, collector);
            loot.addAll(poolLoot);
            LootInfoCollector.finalizeInfo(collector, info);
        }
        return true;
    }

    @Nullable
    private Info createInfoIfMultipleRolls(int rolls, int currentRoll, @Nullable LootInfoCollector collector) {
        return rolls == 1 ? null : LootInfoCollector.createInfo(collector, new Info.Composite(Icon.DICE, "Roll " + currentRoll + " out of " + rolls));
    }
}