package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.Arrays;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

public class ItemUsedOnLocationTrigger extends SimpleCriterionTrigger<ItemUsedOnLocationTrigger.TriggerInstance> {

    final ResourceLocation id;

    public ItemUsedOnLocationTrigger(ResourceLocation resourceLocation0) {
        this.id = resourceLocation0;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    public ItemUsedOnLocationTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ContextAwarePredicate $$3 = ContextAwarePredicate.fromElement("location", deserializationContext2, jsonObject0.get("location"), LootContextParamSets.ADVANCEMENT_LOCATION);
        if ($$3 == null) {
            throw new JsonParseException("Failed to parse 'location' field");
        } else {
            return new ItemUsedOnLocationTrigger.TriggerInstance(this.id, contextAwarePredicate1, $$3);
        }
    }

    public void trigger(ServerPlayer serverPlayer0, BlockPos blockPos1, ItemStack itemStack2) {
        ServerLevel $$3 = serverPlayer0.serverLevel();
        BlockState $$4 = $$3.m_8055_(blockPos1);
        LootParams $$5 = new LootParams.Builder($$3).withParameter(LootContextParams.ORIGIN, blockPos1.getCenter()).withParameter(LootContextParams.THIS_ENTITY, serverPlayer0).withParameter(LootContextParams.BLOCK_STATE, $$4).withParameter(LootContextParams.TOOL, itemStack2).create(LootContextParamSets.ADVANCEMENT_LOCATION);
        LootContext $$6 = new LootContext.Builder($$5).create(null);
        this.m_66234_(serverPlayer0, p_286596_ -> p_286596_.matches($$6));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ContextAwarePredicate location;

        public TriggerInstance(ResourceLocation resourceLocation0, ContextAwarePredicate contextAwarePredicate1, ContextAwarePredicate contextAwarePredicate2) {
            super(resourceLocation0, contextAwarePredicate1);
            this.location = contextAwarePredicate2;
        }

        public static ItemUsedOnLocationTrigger.TriggerInstance placedBlock(Block block0) {
            ContextAwarePredicate $$1 = ContextAwarePredicate.create(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block0).build());
            return new ItemUsedOnLocationTrigger.TriggerInstance(CriteriaTriggers.PLACED_BLOCK.id, ContextAwarePredicate.ANY, $$1);
        }

        public static ItemUsedOnLocationTrigger.TriggerInstance placedBlock(LootItemCondition.Builder... lootItemConditionBuilder0) {
            ContextAwarePredicate $$1 = ContextAwarePredicate.create((LootItemCondition[]) Arrays.stream(lootItemConditionBuilder0).map(LootItemCondition.Builder::m_6409_).toArray(LootItemCondition[]::new));
            return new ItemUsedOnLocationTrigger.TriggerInstance(CriteriaTriggers.PLACED_BLOCK.id, ContextAwarePredicate.ANY, $$1);
        }

        private static ItemUsedOnLocationTrigger.TriggerInstance itemUsedOnLocation(LocationPredicate.Builder locationPredicateBuilder0, ItemPredicate.Builder itemPredicateBuilder1, ResourceLocation resourceLocation2) {
            ContextAwarePredicate $$3 = ContextAwarePredicate.create(LocationCheck.checkLocation(locationPredicateBuilder0).build(), MatchTool.toolMatches(itemPredicateBuilder1).build());
            return new ItemUsedOnLocationTrigger.TriggerInstance(resourceLocation2, ContextAwarePredicate.ANY, $$3);
        }

        public static ItemUsedOnLocationTrigger.TriggerInstance itemUsedOnBlock(LocationPredicate.Builder locationPredicateBuilder0, ItemPredicate.Builder itemPredicateBuilder1) {
            return itemUsedOnLocation(locationPredicateBuilder0, itemPredicateBuilder1, CriteriaTriggers.ITEM_USED_ON_BLOCK.id);
        }

        public static ItemUsedOnLocationTrigger.TriggerInstance allayDropItemOnBlock(LocationPredicate.Builder locationPredicateBuilder0, ItemPredicate.Builder itemPredicateBuilder1) {
            return itemUsedOnLocation(locationPredicateBuilder0, itemPredicateBuilder1, CriteriaTriggers.ALLAY_DROP_ITEM_ON_BLOCK.id);
        }

        public boolean matches(LootContext lootContext0) {
            return this.location.matches(lootContext0);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("location", this.location.toJson(serializationContext0));
            return $$1;
        }
    }
}