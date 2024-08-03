package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SlideDownBlockTrigger extends SimpleCriterionTrigger<SlideDownBlockTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("slide_down_block");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public SlideDownBlockTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        Block $$3 = deserializeBlock(jsonObject0);
        StatePropertiesPredicate $$4 = StatePropertiesPredicate.fromJson(jsonObject0.get("state"));
        if ($$3 != null) {
            $$4.checkState($$3.getStateDefinition(), p_66983_ -> {
                throw new JsonSyntaxException("Block " + $$3 + " has no property " + p_66983_);
            });
        }
        return new SlideDownBlockTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4);
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

    public void trigger(ServerPlayer serverPlayer0, BlockState blockState1) {
        this.m_66234_(serverPlayer0, p_66986_ -> p_66986_.matches(blockState1));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        @Nullable
        private final Block block;

        private final StatePropertiesPredicate state;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, @Nullable Block block1, StatePropertiesPredicate statePropertiesPredicate2) {
            super(SlideDownBlockTrigger.ID, contextAwarePredicate0);
            this.block = block1;
            this.state = statePropertiesPredicate2;
        }

        public static SlideDownBlockTrigger.TriggerInstance slidesDownBlock(Block block0) {
            return new SlideDownBlockTrigger.TriggerInstance(ContextAwarePredicate.ANY, block0, StatePropertiesPredicate.ANY);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            if (this.block != null) {
                $$1.addProperty("block", BuiltInRegistries.BLOCK.getKey(this.block).toString());
            }
            $$1.add("state", this.state.serializeToJson());
            return $$1;
        }

        public boolean matches(BlockState blockState0) {
            return this.block != null && !blockState0.m_60713_(this.block) ? false : this.state.matches(blockState0);
        }
    }
}