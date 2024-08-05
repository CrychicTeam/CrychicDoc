package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class CopyNameFunction extends LootItemConditionalFunction {

    final CopyNameFunction.NameSource source;

    CopyNameFunction(LootItemCondition[] lootItemCondition0, CopyNameFunction.NameSource copyNameFunctionNameSource1) {
        super(lootItemCondition0);
        this.source = copyNameFunctionNameSource1;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.COPY_NAME;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(this.source.param);
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        if (lootContext1.getParamOrNull(this.source.param) instanceof Nameable $$3 && $$3.hasCustomName()) {
            itemStack0.setHoverName($$3.getDisplayName());
        }
        return itemStack0;
    }

    public static LootItemConditionalFunction.Builder<?> copyName(CopyNameFunction.NameSource copyNameFunctionNameSource0) {
        return m_80683_(p_80191_ -> new CopyNameFunction(p_80191_, copyNameFunctionNameSource0));
    }

    public static enum NameSource {

        THIS("this", LootContextParams.THIS_ENTITY), KILLER("killer", LootContextParams.KILLER_ENTITY), KILLER_PLAYER("killer_player", LootContextParams.LAST_DAMAGE_PLAYER), BLOCK_ENTITY("block_entity", LootContextParams.BLOCK_ENTITY);

        public final String name;

        public final LootContextParam<?> param;

        private NameSource(String p_80206_, LootContextParam<?> p_80207_) {
            this.name = p_80206_;
            this.param = p_80207_;
        }

        public static CopyNameFunction.NameSource getByName(String p_80209_) {
            for (CopyNameFunction.NameSource $$1 : values()) {
                if ($$1.name.equals(p_80209_)) {
                    return $$1;
                }
            }
            throw new IllegalArgumentException("Invalid name source " + p_80209_);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<CopyNameFunction> {

        public void serialize(JsonObject jsonObject0, CopyNameFunction copyNameFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, copyNameFunction1, jsonSerializationContext2);
            jsonObject0.addProperty("source", copyNameFunction1.source.name);
        }

        public CopyNameFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            CopyNameFunction.NameSource $$3 = CopyNameFunction.NameSource.getByName(GsonHelper.getAsString(jsonObject0, "source"));
            return new CopyNameFunction(lootItemCondition2, $$3);
        }
    }
}