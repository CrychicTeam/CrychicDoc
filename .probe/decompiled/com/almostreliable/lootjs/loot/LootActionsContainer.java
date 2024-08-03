package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.ILootAction;
import com.almostreliable.lootjs.core.LootEntry;
import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.loot.action.AddLootAction;
import com.almostreliable.lootjs.loot.action.DropExperienceAction;
import com.almostreliable.lootjs.loot.action.ExplodeAction;
import com.almostreliable.lootjs.loot.action.LightningStrikeAction;
import com.almostreliable.lootjs.loot.action.ModifyLootAction;
import com.almostreliable.lootjs.loot.action.RemoveLootAction;
import com.almostreliable.lootjs.loot.action.ReplaceLootAction;
import com.almostreliable.lootjs.loot.action.WeightedAddLootAction;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public interface LootActionsContainer<A extends LootActionsContainer<?>> {

    default A addLoot(LootEntry... entries) {
        return this.addAction(new AddLootAction(entries));
    }

    default A addAlternativesLoot(LootEntry... entries) {
        return this.addAction(new AddLootAction(entries, AddLootAction.AddType.ALTERNATIVES));
    }

    default A addSequenceLoot(LootEntry... entries) {
        return this.addAction(new AddLootAction(entries, AddLootAction.AddType.SEQUENCE));
    }

    default A addWeightedLoot(NumberProvider numberProvider, boolean allowDuplicateLoot, LootEntry[] poolEntries) {
        SimpleWeightedRandomList.Builder<LootEntry> weightedListBuilder = SimpleWeightedRandomList.builder();
        for (LootEntry poolEntry : poolEntries) {
            weightedListBuilder.add(poolEntry, poolEntry.getWeight());
        }
        return this.addAction(new WeightedAddLootAction(numberProvider, weightedListBuilder.build(), allowDuplicateLoot));
    }

    default A addWeightedLoot(NumberProvider numberProvider, LootEntry[] poolEntries) {
        return this.addWeightedLoot(numberProvider, true, poolEntries);
    }

    default A addWeightedLoot(LootEntry[] poolEntries) {
        return this.addWeightedLoot(ConstantValue.exactly(1.0F), true, poolEntries);
    }

    default A removeLoot(ItemFilter filter) {
        return this.addAction(new RemoveLootAction(filter));
    }

    default A replaceLoot(ItemFilter filter, LootEntry lootEntry) {
        return this.replaceLoot(filter, lootEntry, false);
    }

    default A replaceLoot(ItemFilter filter, LootEntry lootEntry, boolean preserveCount) {
        return this.addAction(new ReplaceLootAction(filter, lootEntry, preserveCount));
    }

    default A modifyLoot(ItemFilter filter, ModifyLootAction.Callback callback) {
        return this.addAction(new ModifyLootAction(filter, callback));
    }

    default A triggerExplosion(float radius, boolean destroy, boolean fire) {
        Explosion.BlockInteraction mode = destroy ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP;
        return this.addAction(new ExplodeAction(radius, mode, fire));
    }

    default A triggerExplosion(float radius, Explosion.BlockInteraction mode, boolean fire) {
        return this.addAction(new ExplodeAction(radius, mode, fire));
    }

    default A triggerLightningStrike(boolean shouldDamage) {
        return this.addAction(new LightningStrikeAction(shouldDamage));
    }

    default A dropExperience(int amount) {
        return this.addAction(new DropExperienceAction(amount));
    }

    A addAction(ILootAction var1);
}