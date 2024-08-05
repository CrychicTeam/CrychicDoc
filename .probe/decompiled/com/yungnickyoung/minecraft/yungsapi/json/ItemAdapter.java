package com.yungnickyoung.minecraft.yungsapi.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.yungnickyoung.minecraft.yungsapi.YungsApiCommon;
import java.io.IOException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ItemAdapter extends TypeAdapter<Item> {

    public Item read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        } else {
            return resolveItem(reader.nextString());
        }
    }

    public void write(JsonWriter writer, Item item) throws IOException {
        if (item == null) {
            writer.nullValue();
        } else {
            String itemString = String.valueOf(item);
            writer.value(itemString);
        }
    }

    public static Item resolveItem(String itemString) {
        try {
            return BuiltInRegistries.ITEM.get(new ResourceLocation(itemString));
        } catch (Exception var3) {
            YungsApiCommon.LOGGER.error("JSON: Unable to read item '{}': {}", itemString, var3.toString());
            YungsApiCommon.LOGGER.error("Using air instead...");
            return Items.AIR;
        }
    }
}