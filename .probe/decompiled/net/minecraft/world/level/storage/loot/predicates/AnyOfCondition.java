package net.minecraft.world.level.storage.loot.predicates;

public class AnyOfCondition extends CompositeLootItemCondition {

    AnyOfCondition(LootItemCondition[] lootItemCondition0) {
        super(lootItemCondition0, LootItemConditions.orConditions(lootItemCondition0));
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.ANY_OF;
    }

    public static AnyOfCondition.Builder anyOf(LootItemCondition.Builder... lootItemConditionBuilder0) {
        return new AnyOfCondition.Builder(lootItemConditionBuilder0);
    }

    public static class Builder extends CompositeLootItemCondition.Builder {

        public Builder(LootItemCondition.Builder... lootItemConditionBuilder0) {
            super(lootItemConditionBuilder0);
        }

        @Override
        public AnyOfCondition.Builder or(LootItemCondition.Builder lootItemConditionBuilder0) {
            this.m_286010_(lootItemConditionBuilder0);
            return this;
        }

        @Override
        protected LootItemCondition create(LootItemCondition[] lootItemCondition0) {
            return new AnyOfCondition(lootItemCondition0);
        }
    }

    public static class Serializer extends CompositeLootItemCondition.Serializer<AnyOfCondition> {

        protected AnyOfCondition create(LootItemCondition[] lootItemCondition0) {
            return new AnyOfCondition(lootItemCondition0);
        }
    }
}