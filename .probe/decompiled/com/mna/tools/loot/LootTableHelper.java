package com.mna.tools.loot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class LootTableHelper {

    private static LootTableHelper instance;

    private static LootDataManager lootDataManager;

    public static LootTableHelper instance() {
        if (instance == null) {
            instance = new LootTableHelper();
        }
        return instance;
    }

    public static List<LootPool> getPoolsOBF(LootTable table) {
        return (List<LootPool>) ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "f_79109_");
    }

    public NumberProvider getRolls(LootPool pool) {
        return pool.getRolls();
    }

    public NumberProvider getBonusRolls(LootPool pool) {
        return pool.getBonusRolls();
    }

    public static List<LootPool> getPools(LootTable table) {
        return getPoolsOBF(table);
    }

    public static List<LootPoolEntryContainer> getLootEntries(LootPool pool) {
        return Arrays.asList(pool.entries);
    }

    public static List<LootItemCondition> getLootConditions(LootPool pool) {
        return Arrays.asList(pool.conditions);
    }

    public static List<LootDrop> toDrops(ServerLevel serverLevel, LootTable table) {
        List<LootDrop> drops = new ArrayList();
        LootDataManager lootDataManager = getLootDataManager(serverLevel);
        LootContext baseContext = new LootContext.Builder(new LootParams.Builder(serverLevel).create(LootContextParamSets.EMPTY)).create(null);
        getPools(table).forEach(pool -> {
            float totalWeight = (float) getLootEntries(pool).stream().filter(entry -> entry instanceof LootPoolSingletonContainer).map(entry -> (LootPoolSingletonContainer) entry).mapToInt(entry -> entry.weight).sum();
            List<LootItemCondition> poolConditions = getLootConditions(pool);
            getLootEntries(pool).stream().filter(entry -> entry instanceof LootItem).map(entry -> (LootItem) entry).map(entry -> new LootDrop(serverLevel, entry.item, (float) entry.f_79675_ / totalWeight, entry.f_79636_, entry.f_79677_)).map(drop -> drop.addLootConditions(poolConditions)).forEach(d -> {
                d.minDrop = d.minDrop * pool.getRolls().getInt(baseContext);
                d.maxDrop = d.maxDrop * (pool.getRolls().getInt(baseContext) + pool.getBonusRolls().getInt(baseContext));
                drops.add(d);
            });
            getLootEntries(pool).stream().filter(entry -> entry instanceof LootTableReference).map(entry -> (LootTableReference) entry).map(entry -> toDrops(serverLevel, lootDataManager.m_278676_(entry.name))).forEach(drops::addAll);
        });
        drops.removeIf(Objects::isNull);
        return drops;
    }

    public static List<LootDrop> toDrops(ServerLevel serverLevel, ResourceLocation lootTable) {
        return toDrops(serverLevel, getLootDataManager(serverLevel).m_278676_(lootTable));
    }

    public static LootDataManager getLootDataManager(Level level) {
        if (level.getServer() == null) {
            if (lootDataManager == null) {
                lootDataManager = new LootDataManager();
                return lootDataManager;
            } else {
                return lootDataManager;
            }
        } else {
            return level.getServer().getLootData();
        }
    }
}