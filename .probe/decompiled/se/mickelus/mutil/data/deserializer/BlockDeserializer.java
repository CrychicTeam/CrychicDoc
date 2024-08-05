package se.mickelus.mutil.data.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

@ParametersAreNonnullByDefault
public class BlockDeserializer implements JsonDeserializer<Block> {

    public Block deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String string = json.getAsString();
        if (string != null) {
            ResourceLocation resourceLocation = new ResourceLocation(string);
            if (ForgeRegistries.BLOCKS.containsKey(resourceLocation)) {
                return ForgeRegistries.BLOCKS.getValue(resourceLocation);
            }
        }
        return null;
    }
}