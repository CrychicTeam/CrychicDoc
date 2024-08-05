package dev.latvian.mods.kubejs.core;

import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.bindings.event.ServerEvents;
import dev.latvian.mods.kubejs.loot.BlockLootEventJS;
import dev.latvian.mods.kubejs.loot.ChestLootEventJS;
import dev.latvian.mods.kubejs.loot.EntityLootEventJS;
import dev.latvian.mods.kubejs.loot.FishingLootEventJS;
import dev.latvian.mods.kubejs.loot.GenericLootEventJS;
import dev.latvian.mods.kubejs.loot.GiftLootEventJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.server.DataExport;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataType;

public interface LootTablesKJS {

    static void kjs$postLootEvents(Map<ResourceLocation, JsonElement> map) {
        ServerEvents.GENERIC_LOOT_TABLES.post(ScriptType.SERVER, new GenericLootEventJS(map));
        ServerEvents.BLOCK_LOOT_TABLES.post(ScriptType.SERVER, new BlockLootEventJS(map));
        ServerEvents.ENTITY_LOOT_TABLES.post(ScriptType.SERVER, new EntityLootEventJS(map));
        ServerEvents.GIFT_LOOT_TABLES.post(ScriptType.SERVER, new GiftLootEventJS(map));
        ServerEvents.FISHING_LOOT_TABLES.post(ScriptType.SERVER, new FishingLootEventJS(map));
        ServerEvents.CHEST_LOOT_TABLES.post(ScriptType.SERVER, new ChestLootEventJS(map));
    }

    default void kjs$completeReload(Map<LootDataType<?>, Map<ResourceLocation, ?>> parsedMap, Map<LootDataId<?>, ?> elements) {
        if (DataExport.export != null) {
            for (Entry<LootDataId<?>, ?> entry : elements.entrySet()) {
                LootDataType<?> type = ((LootDataId) entry.getKey()).type();
                ResourceLocation id = ((LootDataId) entry.getKey()).location();
                try {
                    JsonElement lootJson = type.parser().toJsonTree(entry.getValue());
                    String fileName = "%s/%s/%s.json".formatted(type.directory(), id.getNamespace(), id.getPath());
                    DataExport.export.addJson(fileName, lootJson);
                } catch (Exception var9) {
                    ConsoleJS.SERVER.error("Failed to export loot table %s as JSON!".formatted(id), var9);
                }
            }
        }
    }
}