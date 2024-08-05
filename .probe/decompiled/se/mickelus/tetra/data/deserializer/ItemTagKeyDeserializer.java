package se.mickelus.tetra.data.deserializer;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ItemTagKeyDeserializer implements JsonDeserializer<TagKey<Item>> {

    public static final TypeToken<TagKey<Item>> typeToken = new TypeToken<TagKey<Item>>() {
    };

    public TagKey<Item> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ItemTags.create(new ResourceLocation(json.getAsString()));
    }
}