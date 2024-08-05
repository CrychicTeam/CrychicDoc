package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.util.Set;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class LootItemBlockStatePropertyCondition implements LootItemCondition {

    final Block block;

    final StatePropertiesPredicate properties;

    LootItemBlockStatePropertyCondition(Block block0, StatePropertiesPredicate statePropertiesPredicate1) {
        this.block = block0;
        this.properties = statePropertiesPredicate1;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.BLOCK_STATE_PROPERTY;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.BLOCK_STATE);
    }

    public boolean test(LootContext lootContext0) {
        BlockState $$1 = lootContext0.getParamOrNull(LootContextParams.BLOCK_STATE);
        return $$1 != null && $$1.m_60713_(this.block) && this.properties.matches($$1);
    }

    public static LootItemBlockStatePropertyCondition.Builder hasBlockStateProperties(Block block0) {
        return new LootItemBlockStatePropertyCondition.Builder(block0);
    }

    public static class Builder implements LootItemCondition.Builder {

        private final Block block;

        private StatePropertiesPredicate properties = StatePropertiesPredicate.ANY;

        public Builder(Block block0) {
            this.block = block0;
        }

        public LootItemBlockStatePropertyCondition.Builder setProperties(StatePropertiesPredicate.Builder statePropertiesPredicateBuilder0) {
            this.properties = statePropertiesPredicateBuilder0.build();
            return this;
        }

        @Override
        public LootItemCondition build() {
            return new LootItemBlockStatePropertyCondition(this.block, this.properties);
        }
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootItemBlockStatePropertyCondition> {

        public void serialize(JsonObject jsonObject0, LootItemBlockStatePropertyCondition lootItemBlockStatePropertyCondition1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.addProperty("block", BuiltInRegistries.BLOCK.getKey(lootItemBlockStatePropertyCondition1.block).toString());
            jsonObject0.add("properties", lootItemBlockStatePropertyCondition1.properties.serializeToJson());
        }

        public LootItemBlockStatePropertyCondition deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            ResourceLocation $$2 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "block"));
            Block $$3 = (Block) BuiltInRegistries.BLOCK.m_6612_($$2).orElseThrow(() -> new IllegalArgumentException("Can't find block " + $$2));
            StatePropertiesPredicate $$4 = StatePropertiesPredicate.fromJson(jsonObject0.get("properties"));
            $$4.checkState($$3.getStateDefinition(), p_81790_ -> {
                throw new JsonSyntaxException("Block " + $$3 + " has no property " + p_81790_);
            });
            return new LootItemBlockStatePropertyCondition($$3, $$4);
        }
    }
}