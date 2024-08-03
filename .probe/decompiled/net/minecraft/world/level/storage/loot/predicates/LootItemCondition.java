package net.minecraft.world.level.storage.loot.predicates;

import java.util.function.Predicate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface LootItemCondition extends LootContextUser, Predicate<LootContext> {

    LootItemConditionType getType();

    @FunctionalInterface
    public interface Builder {

        LootItemCondition build();

        default LootItemCondition.Builder invert() {
            return InvertedLootItemCondition.invert(this);
        }

        default AnyOfCondition.Builder or(LootItemCondition.Builder lootItemConditionBuilder0) {
            return AnyOfCondition.anyOf(this, lootItemConditionBuilder0);
        }

        default AllOfCondition.Builder and(LootItemCondition.Builder lootItemConditionBuilder0) {
            return AllOfCondition.allOf(this, lootItemConditionBuilder0);
        }
    }
}