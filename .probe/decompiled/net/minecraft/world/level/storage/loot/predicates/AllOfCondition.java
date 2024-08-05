package net.minecraft.world.level.storage.loot.predicates;

public class AllOfCondition extends CompositeLootItemCondition {

    AllOfCondition(LootItemCondition[] lootItemCondition0) {
        super(lootItemCondition0, LootItemConditions.andConditions(lootItemCondition0));
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.ALL_OF;
    }

    public static AllOfCondition.Builder allOf(LootItemCondition.Builder... lootItemConditionBuilder0) {
        return new AllOfCondition.Builder(lootItemConditionBuilder0);
    }

    public static class Builder extends CompositeLootItemCondition.Builder {

        public Builder(LootItemCondition.Builder... lootItemConditionBuilder0) {
            super(lootItemConditionBuilder0);
        }

        @Override
        public AllOfCondition.Builder and(LootItemCondition.Builder lootItemConditionBuilder0) {
            this.m_286010_(lootItemConditionBuilder0);
            return this;
        }

        @Override
        protected LootItemCondition create(LootItemCondition[] lootItemCondition0) {
            return new AllOfCondition(lootItemCondition0);
        }
    }

    public static class Serializer extends CompositeLootItemCondition.Serializer<AllOfCondition> {

        protected AllOfCondition create(LootItemCondition[] lootItemCondition0) {
            return new AllOfCondition(lootItemCondition0);
        }
    }
}