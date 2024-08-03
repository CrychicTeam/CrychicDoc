package io.github.lightman314.lightmanscurrency.datagen.common.loot.packs;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.common.loot.ConfigItemTier;
import io.github.lightman314.lightmanscurrency.common.loot.LCLootTables;
import io.github.lightman314.lightmanscurrency.common.loot.entries.ConfigLoot;
import java.util.List;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ChestAddonLoot implements LootTableSubProvider {

    @Override
    public void generate(@Nonnull BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        ChestAddonLoot.ChestLootEntryData T1 = new ChestAddonLoot.ChestLootEntryData(ConfigItemTier.T1, 1.0F, 10.0F, 1);
        ChestAddonLoot.ChestLootEntryData T2 = new ChestAddonLoot.ChestLootEntryData(ConfigItemTier.T2, 1.0F, 10.0F, 2);
        ChestAddonLoot.ChestLootEntryData T3 = new ChestAddonLoot.ChestLootEntryData(ConfigItemTier.T3, 1.0F, 10.0F, 3);
        ChestAddonLoot.ChestLootEntryData T4 = new ChestAddonLoot.ChestLootEntryData(ConfigItemTier.T4, 1.0F, 10.0F, 4);
        ChestAddonLoot.ChestLootEntryData T5 = new ChestAddonLoot.ChestLootEntryData(ConfigItemTier.T5, 1.0F, 8.0F, 5);
        ChestAddonLoot.ChestLootEntryData T6 = new ChestAddonLoot.ChestLootEntryData(ConfigItemTier.T6, 1.0F, 3.0F, 6);
        consumer.accept(LCLootTables.CHEST_DROPS_T1, LootTable.lootTable().withPool(GenerateChestCoinPool(ImmutableList.of(T1), 1.0F, 5.0F)));
        consumer.accept(LCLootTables.CHEST_DROPS_T2, LootTable.lootTable().withPool(GenerateChestCoinPool(ImmutableList.of(T1, T2), 1.0F, 5.0F)));
        consumer.accept(LCLootTables.CHEST_DROPS_T3, LootTable.lootTable().withPool(GenerateChestCoinPool(ImmutableList.of(T1, T2, T3), 2.0F, 6.0F)));
        consumer.accept(LCLootTables.CHEST_DROPS_T4, LootTable.lootTable().withPool(GenerateChestCoinPool(ImmutableList.of(T1, T2, T3, T4), 3.0F, 6.0F)));
        consumer.accept(LCLootTables.CHEST_DROPS_T5, LootTable.lootTable().withPool(GenerateChestCoinPool(ImmutableList.of(T1, T2, T3, T4, T5), 3.0F, 6.0F)));
        consumer.accept(LCLootTables.CHEST_DROPS_T6, LootTable.lootTable().withPool(GenerateChestCoinPool(ImmutableList.of(T1, T2, T3, T4, T5, T6), 3.0F, 6.0F)));
    }

    private static LootPool.Builder GenerateChestCoinPool(List<ChestAddonLoot.ChestLootEntryData> lootEntries, float minRolls, float maxRolls) {
        LootPool.Builder builder = LootPool.lootPool().setRolls(UniformGenerator.between(minRolls, maxRolls));
        for (ChestAddonLoot.ChestLootEntryData entry : lootEntries) {
            builder.add(ConfigLoot.lootTableTier(entry.tier).apply(SetItemCountFunction.setCount(UniformGenerator.between(entry.minCount, entry.maxCount))).setWeight(entry.weight));
        }
        return builder;
    }

    private static record ChestLootEntryData(ConfigItemTier tier, float minCount, float maxCount, int weight) {
    }
}