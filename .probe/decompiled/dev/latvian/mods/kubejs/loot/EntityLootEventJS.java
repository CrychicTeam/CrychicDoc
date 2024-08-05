package dev.latvian.mods.kubejs.loot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class EntityLootEventJS extends LootEventJS {

    public EntityLootEventJS(Map<ResourceLocation, JsonElement> c) {
        super(c);
    }

    @Override
    public String getType() {
        return "minecraft:entity";
    }

    @Override
    public String getDirectory() {
        return "entities";
    }

    public void addEntity(EntityType<?> type, Consumer<LootBuilder> b) {
        LootBuilder builder = this.createLootBuilder(null, b);
        JsonObject json = builder.toJson();
        ResourceLocation entityId = builder.customId == null ? RegistryInfo.ENTITY_TYPE.getId(type) : builder.customId;
        if (entityId != null) {
            this.addJson(entityId, json);
        }
    }

    public void modifyEntity(EntityType<?> type, Consumer<LootBuilder> b) {
        ResourceLocation entityId = RegistryInfo.ENTITY_TYPE.getId(type);
        if (entityId != null) {
            this.modify(entityId, b);
        }
    }
}