package vazkii.patchouli.client.book.template.variable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import vazkii.patchouli.api.IVariableSerializer;
import vazkii.patchouli.common.util.ItemStackUtil;

public class ItemStackVariableSerializer implements IVariableSerializer<ItemStack> {

    public ItemStack fromJson(JsonElement json) {
        if (json.isJsonNull()) {
            return ItemStack.EMPTY;
        } else if (json.isJsonPrimitive()) {
            return ItemStackUtil.loadStackFromString(json.getAsString());
        } else if (json.isJsonObject()) {
            return ItemStackUtil.loadStackFromJson(json.getAsJsonObject());
        } else {
            throw new IllegalArgumentException("Can't make an ItemStack from an array!");
        }
    }

    public JsonElement toJson(ItemStack stack) {
        JsonObject ret = new JsonObject();
        ret.addProperty("item", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        if (stack.getCount() != 1) {
            ret.addProperty("count", stack.getCount());
        }
        if (stack.getTag() != null) {
            ret.addProperty("nbt", stack.getTag().toString());
        }
        return ret;
    }
}