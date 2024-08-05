package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BeeNestDestroyedTrigger extends SimpleCriterionTrigger<BeeNestDestroyedTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("bee_nest_destroyed");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public BeeNestDestroyedTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        Block $$3 = deserializeBlock(jsonObject0);
        ItemPredicate $$4 = ItemPredicate.fromJson(jsonObject0.get("item"));
        MinMaxBounds.Ints $$5 = MinMaxBounds.Ints.fromJson(jsonObject0.get("num_bees_inside"));
        return new BeeNestDestroyedTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4, $$5);
    }

    @Nullable
    private static Block deserializeBlock(JsonObject jsonObject0) {
        if (jsonObject0.has("block")) {
            ResourceLocation $$1 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "block"));
            return (Block) BuiltInRegistries.BLOCK.m_6612_($$1).orElseThrow(() -> new JsonSyntaxException("Unknown block type '" + $$1 + "'"));
        } else {
            return null;
        }
    }

    public void trigger(ServerPlayer serverPlayer0, BlockState blockState1, ItemStack itemStack2, int int3) {
        this.m_66234_(serverPlayer0, p_146660_ -> p_146660_.matches(blockState1, itemStack2, int3));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        @Nullable
        private final Block block;

        private final ItemPredicate item;

        private final MinMaxBounds.Ints numBees;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, @Nullable Block block1, ItemPredicate itemPredicate2, MinMaxBounds.Ints minMaxBoundsInts3) {
            super(BeeNestDestroyedTrigger.ID, contextAwarePredicate0);
            this.block = block1;
            this.item = itemPredicate2;
            this.numBees = minMaxBoundsInts3;
        }

        public static BeeNestDestroyedTrigger.TriggerInstance destroyedBeeNest(Block block0, ItemPredicate.Builder itemPredicateBuilder1, MinMaxBounds.Ints minMaxBoundsInts2) {
            return new BeeNestDestroyedTrigger.TriggerInstance(ContextAwarePredicate.ANY, block0, itemPredicateBuilder1.build(), minMaxBoundsInts2);
        }

        public boolean matches(BlockState blockState0, ItemStack itemStack1, int int2) {
            if (this.block != null && !blockState0.m_60713_(this.block)) {
                return false;
            } else {
                return !this.item.matches(itemStack1) ? false : this.numBees.matches(int2);
            }
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            if (this.block != null) {
                $$1.addProperty("block", BuiltInRegistries.BLOCK.getKey(this.block).toString());
            }
            $$1.add("item", this.item.serializeToJson());
            $$1.add("num_bees_inside", this.numBees.m_55328_());
            return $$1;
        }
    }
}