package net.minecraft.world.level.storage.loot.providers.nbt;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class StorageNbtProvider implements NbtProvider {

    final ResourceLocation id;

    StorageNbtProvider(ResourceLocation resourceLocation0) {
        this.id = resourceLocation0;
    }

    @Override
    public LootNbtProviderType getType() {
        return NbtProviders.STORAGE;
    }

    @Nullable
    @Override
    public Tag get(LootContext lootContext0) {
        return lootContext0.getLevel().getServer().getCommandStorage().get(this.id);
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of();
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<StorageNbtProvider> {

        public void serialize(JsonObject jsonObject0, StorageNbtProvider storageNbtProvider1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.addProperty("source", storageNbtProvider1.id.toString());
        }

        public StorageNbtProvider deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            String $$2 = GsonHelper.getAsString(jsonObject0, "source");
            return new StorageNbtProvider(new ResourceLocation($$2));
        }
    }
}