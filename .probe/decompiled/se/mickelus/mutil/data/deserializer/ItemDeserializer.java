package se.mickelus.mutil.data.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

@ParametersAreNonnullByDefault
public class ItemDeserializer implements JsonDeserializer<Item> {

    public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String string = json.getAsString();
        if (string != null) {
            ResourceLocation resourceLocation = new ResourceLocation(string);
            if (ForgeRegistries.ITEMS.containsKey(resourceLocation)) {
                return ForgeRegistries.ITEMS.getValue(resourceLocation);
            }
        }
        return null;
    }
}