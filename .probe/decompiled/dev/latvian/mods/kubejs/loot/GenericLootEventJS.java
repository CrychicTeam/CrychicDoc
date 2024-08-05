package dev.latvian.mods.kubejs.loot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;

public class GenericLootEventJS extends LootEventJS {

    public GenericLootEventJS(Map<ResourceLocation, JsonElement> c) {
        super(c);
    }

    @Override
    public String getType() {
        return "minecraft:generic";
    }

    @Override
    public String getDirectory() {
        return "";
    }

    public void addGeneric(ResourceLocation id, Consumer<LootBuilder> b) {
        LootBuilder builder = this.createLootBuilder(null, b);
        JsonObject json = builder.toJson();
        this.addJson(builder.customId == null ? id : builder.customId, json);
    }
}