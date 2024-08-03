package net.minecraft.world.level.storage.loot.providers.nbt;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class ContextNbtProvider implements NbtProvider {

    private static final String BLOCK_ENTITY_ID = "block_entity";

    private static final ContextNbtProvider.Getter BLOCK_ENTITY_PROVIDER = new ContextNbtProvider.Getter() {

        @Override
        public Tag get(LootContext p_165582_) {
            BlockEntity $$1 = p_165582_.getParamOrNull(LootContextParams.BLOCK_ENTITY);
            return $$1 != null ? $$1.saveWithFullMetadata() : null;
        }

        @Override
        public String getId() {
            return "block_entity";
        }

        @Override
        public Set<LootContextParam<?>> getReferencedContextParams() {
            return ImmutableSet.of(LootContextParams.BLOCK_ENTITY);
        }
    };

    public static final ContextNbtProvider BLOCK_ENTITY = new ContextNbtProvider(BLOCK_ENTITY_PROVIDER);

    final ContextNbtProvider.Getter getter;

    private static ContextNbtProvider.Getter forEntity(final LootContext.EntityTarget lootContextEntityTarget0) {
        return new ContextNbtProvider.Getter() {

            @Nullable
            @Override
            public Tag get(LootContext p_165589_) {
                Entity $$1 = p_165589_.getParamOrNull(lootContextEntityTarget0.getParam());
                return $$1 != null ? NbtPredicate.getEntityTagToCompare($$1) : null;
            }

            @Override
            public String getId() {
                return lootContextEntityTarget0.name();
            }

            @Override
            public Set<LootContextParam<?>> getReferencedContextParams() {
                return ImmutableSet.of(lootContextEntityTarget0.getParam());
            }
        };
    }

    private ContextNbtProvider(ContextNbtProvider.Getter contextNbtProviderGetter0) {
        this.getter = contextNbtProviderGetter0;
    }

    @Override
    public LootNbtProviderType getType() {
        return NbtProviders.CONTEXT;
    }

    @Nullable
    @Override
    public Tag get(LootContext lootContext0) {
        return this.getter.get(lootContext0);
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.getter.getReferencedContextParams();
    }

    public static NbtProvider forContextEntity(LootContext.EntityTarget lootContextEntityTarget0) {
        return new ContextNbtProvider(forEntity(lootContextEntityTarget0));
    }

    static ContextNbtProvider createFromContext(String string0) {
        if (string0.equals("block_entity")) {
            return new ContextNbtProvider(BLOCK_ENTITY_PROVIDER);
        } else {
            LootContext.EntityTarget $$1 = LootContext.EntityTarget.getByName(string0);
            return new ContextNbtProvider(forEntity($$1));
        }
    }

    interface Getter {

        @Nullable
        Tag get(LootContext var1);

        String getId();

        Set<LootContextParam<?>> getReferencedContextParams();
    }

    public static class InlineSerializer implements GsonAdapterFactory.InlineSerializer<ContextNbtProvider> {

        public JsonElement serialize(ContextNbtProvider contextNbtProvider0, JsonSerializationContext jsonSerializationContext1) {
            return new JsonPrimitive(contextNbtProvider0.getter.getId());
        }

        public ContextNbtProvider deserialize(JsonElement jsonElement0, JsonDeserializationContext jsonDeserializationContext1) {
            String $$2 = jsonElement0.getAsString();
            return ContextNbtProvider.createFromContext($$2);
        }
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ContextNbtProvider> {

        public void serialize(JsonObject jsonObject0, ContextNbtProvider contextNbtProvider1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.addProperty("target", contextNbtProvider1.getter.getId());
        }

        public ContextNbtProvider deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            String $$2 = GsonHelper.getAsString(jsonObject0, "target");
            return ContextNbtProvider.createFromContext($$2);
        }
    }
}