package com.mna.tools.loot;

import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;

public class LootConditionHelper {

    public static void applyCondition(LootItemCondition condition, LootDrop lootDrop) {
        if (condition instanceof LootItemKilledByPlayerCondition) {
            lootDrop.addConditional(Conditional.playerKill);
        } else if (condition instanceof LootItemRandomChanceCondition) {
            lootDrop.chance = ((LootItemRandomChanceCondition) condition).probability;
        } else if (condition instanceof LootItemRandomChanceWithLootingCondition) {
            lootDrop.chance = ((LootItemRandomChanceWithLootingCondition) condition).percent;
            lootDrop.addConditional(Conditional.affectedByLooting);
        } else if (condition instanceof LootItemBlockStatePropertyCondition) {
        }
    }
}