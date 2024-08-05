package dev.shadowsoffire.placebo.loot;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class PoolBuilder extends LootPool.Builder {

    static int k = 0;

    public PoolBuilder(int minRolls, int maxRolls) {
        this.m_165133_(UniformGenerator.between((float) minRolls, (float) maxRolls));
    }

    public PoolBuilder addEntries(LootPoolEntryContainer... entries) {
        for (LootPoolEntryContainer e : entries) {
            this.f_79067_.add(e);
        }
        return this;
    }

    public PoolBuilder addCondition(LootItemCondition... conditions) {
        for (LootItemCondition c : conditions) {
            this.f_79068_.add(c);
        }
        return this;
    }

    public PoolBuilder addFunc(LootItemFunction... conditions) {
        for (LootItemFunction c : conditions) {
            this.f_79069_.add(c);
        }
        return this;
    }
}