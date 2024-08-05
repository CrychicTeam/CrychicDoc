package io.github.apace100.origins.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.apace100.origins.registry.ModLoot;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OriginLootCondition implements LootItemCondition {

    private final ResourceLocation origin;

    @Nullable
    private final ResourceLocation layer;

    private OriginLootCondition(ResourceLocation origin) {
        this(origin, null);
    }

    private OriginLootCondition(ResourceLocation origin, @Nullable ResourceLocation layer) {
        this.origin = origin;
        this.layer = layer;
    }

    @NotNull
    @Override
    public LootItemConditionType getType() {
        return ModLoot.ORIGIN_LOOT_CONDITION.get();
    }

    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
        return entity == null ? false : (Boolean) IOriginContainer.get(entity).map(container -> container.getOrigins().entrySet().stream().anyMatch(entry -> (this.layer == null || this.layer.equals(((ResourceKey) entry.getKey()).location())) && this.origin.equals(((ResourceKey) entry.getValue()).location()))).orElse(false);
    }

    public static LootItemCondition.Builder builder(String originId) {
        return builder(new ResourceLocation(originId));
    }

    public static LootItemCondition.Builder builder(ResourceLocation origin) {
        return () -> new OriginLootCondition(origin);
    }

    public static LootItemCondition.Builder builder(String originId, String layerId) {
        return builder(new ResourceLocation(originId), new ResourceLocation(layerId));
    }

    public static LootItemCondition.Builder builder(ResourceLocation origin, ResourceLocation layer) {
        return () -> new OriginLootCondition(origin, layer);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<OriginLootCondition> {

        public void serialize(JsonObject jsonObject, OriginLootCondition originLootCondition, @NotNull JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("origin", originLootCondition.origin.toString());
            if (originLootCondition.layer != null) {
                jsonObject.addProperty("layer", originLootCondition.layer.toString());
            }
        }

        @NotNull
        public OriginLootCondition deserialize(@NotNull JsonObject jsonObject, @NotNull JsonDeserializationContext jsonDeserializationContext) {
            ResourceLocation origin = new ResourceLocation(GsonHelper.getAsString(jsonObject, "origin"));
            if (jsonObject.has("layer")) {
                ResourceLocation layer = new ResourceLocation(GsonHelper.getAsString(jsonObject, "layer"));
                return new OriginLootCondition(origin, layer);
            } else {
                return new OriginLootCondition(origin);
            }
        }
    }
}