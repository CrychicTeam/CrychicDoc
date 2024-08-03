package dev.shadowsoffire.placebo.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;

public class JsonUtil {

    public static boolean checkAndLogEmpty(JsonElement e, ResourceLocation id, String type, Logger logger) {
        String s = e.toString();
        if (!s.isEmpty() && !"{}".equals(s)) {
            return true;
        } else {
            logger.error("Ignoring {} item with id {} as it is empty.  Please switch to a condition-false json instead of an empty one.", type, id);
            return false;
        }
    }

    public static boolean checkConditions(JsonElement e, ResourceLocation id, String type, Logger logger, ICondition.IContext context) {
        if (e.isJsonObject() && (!CraftingHelper.processConditions(e.getAsJsonObject(), "conditions", context) || !CraftingHelper.processConditions(e.getAsJsonObject(), "forge:conditions", context))) {
            logger.trace("Skipping loading {} item with id {} as it's conditions were not met", type, id);
            return false;
        } else {
            return true;
        }
    }

    public static <T> T getRegistryObject(JsonObject parent, String name, IForgeRegistry<T> registry) {
        String key = GsonHelper.getAsString(parent, name);
        T regObj = registry.getValue(new ResourceLocation(key));
        if (regObj == null) {
            throw new JsonSyntaxException("Failed to parse " + registry.getRegistryName() + " object with key " + key);
        } else {
            return regObj;
        }
    }

    @Deprecated
    public static <T> Object makeSerializer(IForgeRegistry<T> reg) {
        return new JsonUtil.SDS<>(reg);
    }

    @Deprecated
    public static <T> Object makeSerializer(com.google.gson.JsonDeserializer<T> jds, com.google.gson.JsonSerializer<T> js) {
        return new JsonUtil.SDS2(jds, js);
    }

    @Deprecated
    public interface JsonDeserializer<V> {

        V read(JsonObject var1);
    }

    @Deprecated
    public interface JsonSerializer<V> {

        JsonObject write(V var1);
    }

    @Deprecated
    public interface NetDeserializer<V> {

        V read(FriendlyByteBuf var1);
    }

    @Deprecated
    public interface NetSerializer<V> {

        void write(V var1, FriendlyByteBuf var2);
    }

    @Deprecated
    private static class SDS<T> implements com.google.gson.JsonDeserializer<T>, com.google.gson.JsonSerializer<T> {

        private final IForgeRegistry<T> reg;

        SDS(IForgeRegistry<T> reg) {
            this.reg = reg;
        }

        public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(this.reg.getKey(src).toString());
        }

        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            T regObj = this.reg.getValue(new ResourceLocation(json.getAsString()));
            if (regObj == null) {
                throw new JsonSyntaxException("Failed to parse " + this.reg.getRegistryName() + " object with key " + json.getAsString());
            } else {
                return regObj;
            }
        }
    }

    @Deprecated
    private static record SDS2<T>(com.google.gson.JsonDeserializer<T> jds, com.google.gson.JsonSerializer<T> js) implements com.google.gson.JsonDeserializer<T>, com.google.gson.JsonSerializer<T> {

        public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
            return this.js.serialize(src, typeOfSrc, context);
        }

        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return (T) this.jds.deserialize(json, typeOfT, context);
        }
    }
}