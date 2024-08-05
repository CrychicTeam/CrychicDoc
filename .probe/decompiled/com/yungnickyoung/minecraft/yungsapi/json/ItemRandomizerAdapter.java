package com.yungnickyoung.minecraft.yungsapi.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.yungnickyoung.minecraft.yungsapi.api.world.randomize.ItemRandomizer;
import java.io.IOException;
import java.util.Map.Entry;
import net.minecraft.world.item.Item;

public class ItemRandomizerAdapter extends TypeAdapter<ItemRandomizer> {

    public ItemRandomizer read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        } else {
            ItemRandomizer randomizer = new ItemRandomizer();
            reader.beginObject();
            while (reader.hasNext()) {
                String var3 = reader.nextName();
                switch(var3) {
                    case "entries":
                        reader.beginObject();
                        while (reader.hasNext()) {
                            Item item = ItemAdapter.resolveItem(reader.nextName());
                            double probability = reader.nextDouble();
                            randomizer.addItem(item, (float) probability);
                        }
                        reader.endObject();
                        break;
                    case "defaultItem":
                        Item item = ItemAdapter.resolveItem(reader.nextString());
                        randomizer.setDefaultItem(item);
                }
            }
            reader.endObject();
            return randomizer;
        }
    }

    public void write(JsonWriter writer, ItemRandomizer randomizer) throws IOException {
        if (randomizer == null) {
            writer.nullValue();
        } else {
            writer.beginObject();
            writer.name("entries").beginObject();
            for (Entry<Item, Float> entry : randomizer.getEntriesAsMap().entrySet()) {
                writer.name(String.valueOf(entry.getKey())).value((Number) entry.getValue());
            }
            writer.endObject();
            String defaultItemString = String.valueOf(randomizer.getDefaultItem());
            writer.name("defaultItem").value(defaultItemString);
            writer.endObject();
        }
    }
}