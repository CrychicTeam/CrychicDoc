package dev.latvian.mods.kubejs.loot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.event.EventJS;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public abstract class LootEventJS extends EventJS {

    private final Map<ResourceLocation, JsonElement> lootMap;

    public LootEventJS(Map<ResourceLocation, JsonElement> c) {
        this.lootMap = c;
    }

    public void addJson(ResourceLocation id, JsonObject json) {
        this.lootMap.put(this.getDirectory().isEmpty() ? id : new ResourceLocation(id.getNamespace(), this.getDirectory() + "/" + id.getPath()), json);
    }

    public abstract String getType();

    public abstract String getDirectory();

    LootBuilder createLootBuilder(@Nullable ResourceLocation id, Consumer<LootBuilder> consumer) {
        LootBuilder builder = new LootBuilder(id == null ? null : (JsonElement) this.lootMap.get(id));
        builder.type = this.getType();
        consumer.accept(builder);
        return builder;
    }

    public void modify(ResourceLocation id, Consumer<LootBuilder> b) {
        LootBuilder builder = this.createLootBuilder(this.getDirectory().isEmpty() ? id : new ResourceLocation(id.getNamespace(), this.getDirectory() + "/" + id.getPath()), b);
        this.addJson(id, builder.toJson());
    }

    public void removeAll() {
        this.lootMap.clear();
    }
}