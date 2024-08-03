package io.github.lightman314.lightmanscurrency.datagen.common.loot.packs;

import io.github.lightman314.lightmanscurrency.common.loot.ConfigItemTier;
import io.github.lightman314.lightmanscurrency.common.loot.LCLootTables;
import io.github.lightman314.lightmanscurrency.common.loot.entries.ConfigLoot;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class EntityAddonLoot implements LootTableSubProvider {

    @Override
    public void generate(@Nonnull BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        LootPool.Builder t1 = GenerateEntityCoinPool(ConfigItemTier.T1, 1.0F, 10.0F, 0.75F);
        LootPool.Builder t2 = GenerateEntityCoinPool(ConfigItemTier.T2, 1.0F, 5.0F, 0.5F);
        LootPool.Builder t3 = GenerateEntityCoinPool(ConfigItemTier.T3, 1.0F, 5.0F, 0.25F);
        LootPool.Builder t4 = GenerateEntityCoinPool(ConfigItemTier.T4, 1.0F, 3.0F, 0.1F);
        LootPool.Builder t5 = GenerateEntityCoinPool(ConfigItemTier.T5, 1.0F, 3.0F, 0.05F);
        LootPool.Builder t6 = GenerateEntityCoinPool(ConfigItemTier.T6, 1.0F, 3.0F, 0.025F);
        consumer.accept(LCLootTables.ENTITY_DROPS_T1, LootTable.lootTable().setParamSet(LootContextParamSets.ENTITY).withPool(t1));
        consumer.accept(LCLootTables.ENTITY_DROPS_T2, LootTable.lootTable().withPool(t1).withPool(t2));
        consumer.accept(LCLootTables.ENTITY_DROPS_T3, LootTable.lootTable().withPool(t1).withPool(t2).withPool(t3));
        consumer.accept(LCLootTables.ENTITY_DROPS_T4, LootTable.lootTable().withPool(t1).withPool(t2).withPool(t3).withPool(t4));
        consumer.accept(LCLootTables.ENTITY_DROPS_T5, LootTable.lootTable().withPool(t1).withPool(t2).withPool(t3).withPool(t4).withPool(t5));
        consumer.accept(LCLootTables.ENTITY_DROPS_T6, LootTable.lootTable().withPool(t1).withPool(t2).withPool(t3).withPool(t4).withPool(t5).withPool(t6));
        consumer.accept(LCLootTables.BOSS_DROPS_T1, LootTable.lootTable().withPool(BossPool(ConfigItemTier.T1)));
        consumer.accept(LCLootTables.BOSS_DROPS_T2, LootTable.lootTable().withPool(BossPool(ConfigItemTier.T1)).withPool(BossPool(ConfigItemTier.T2)));
        consumer.accept(LCLootTables.BOSS_DROPS_T3, LootTable.lootTable().withPool(BossPool(ConfigItemTier.T1)).withPool(BossPool(ConfigItemTier.T2)).withPool(BossPool(ConfigItemTier.T3)));
        consumer.accept(LCLootTables.BOSS_DROPS_T4, LootTable.lootTable().withPool(BossPool(ConfigItemTier.T1)).withPool(BossPool(ConfigItemTier.T2)).withPool(BossPool(ConfigItemTier.T3)).withPool(BossPool(ConfigItemTier.T4)));
        consumer.accept(LCLootTables.BOSS_DROPS_T5, LootTable.lootTable().withPool(BossPool(ConfigItemTier.T1)).withPool(BossPool(ConfigItemTier.T2)).withPool(BossPool(ConfigItemTier.T3)).withPool(BossPool(ConfigItemTier.T4)).withPool(BossPool(ConfigItemTier.T5)));
        consumer.accept(LCLootTables.BOSS_DROPS_T6, LootTable.lootTable().withPool(BossPool(ConfigItemTier.T1)).withPool(BossPool(ConfigItemTier.T2)).withPool(BossPool(ConfigItemTier.T3)).withPool(BossPool(ConfigItemTier.T4)).withPool(BossPool(ConfigItemTier.T5)).withPool(GenerateEntityCoinPool(ConfigItemTier.T6, 1.0F, 5.0F, 1.0F)));
    }

    private static LootPool.Builder BossPool(ConfigItemTier tier) {
        return GenerateEntityCoinPool(tier, 10.0F, 30.0F, 1.0F);
    }

    private static LootPool.Builder GenerateEntityCoinPool(ConfigItemTier tier, float min, float max, float chance) {
        LootPool.Builder lootPoolBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(ConfigLoot.lootTableTier(tier).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))));
        if (chance < 1.0F) {
            lootPoolBuilder.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(chance, 0.01F));
        }
        return lootPoolBuilder;
    }
}