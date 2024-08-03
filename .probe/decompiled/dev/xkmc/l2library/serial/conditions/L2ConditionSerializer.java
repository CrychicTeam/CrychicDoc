package dev.xkmc.l2library.serial.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.xkmc.l2serial.serialization.codec.JsonCodec;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public record L2ConditionSerializer<T extends ICondition>(ResourceLocation id, Class<T> cls) implements IConditionSerializer<T> {

    @Override
    public void write(JsonObject json, T value) {
        JsonElement elem = JsonCodec.toJson(value, this.cls);
        assert elem != null;
        for (Entry<String, JsonElement> e : elem.getAsJsonObject().entrySet()) {
            json.add((String) e.getKey(), (JsonElement) e.getValue());
        }
    }

    @Override
    public T read(JsonObject json) {
        T ans = (T) JsonCodec.from(json, this.cls, null);
        assert ans != null;
        return ans;
    }

    @Override
    public ResourceLocation getID() {
        return this.id;
    }
}