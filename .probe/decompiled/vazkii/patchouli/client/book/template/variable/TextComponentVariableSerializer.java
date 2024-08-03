package vazkii.patchouli.client.book.template.variable;

import com.google.gson.JsonElement;
import net.minecraft.network.chat.Component;
import vazkii.patchouli.api.IVariableSerializer;

public class TextComponentVariableSerializer implements IVariableSerializer<Component> {

    public Component fromJson(JsonElement json) {
        if (json.isJsonNull()) {
            return Component.literal("");
        } else {
            return json.isJsonPrimitive() && json.getAsJsonPrimitive().isString() ? Component.literal(json.getAsString()) : Component.Serializer.fromJson(json);
        }
    }

    public JsonElement toJson(Component stack) {
        return Component.Serializer.toJsonTree(stack);
    }
}