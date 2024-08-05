package vazkii.patchouli.client.book.template.variable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import vazkii.patchouli.common.util.ItemStackUtil;

public class ItemStackArrayVariableSerializer extends GenericArrayVariableSerializer<ItemStack> {

    public ItemStackArrayVariableSerializer() {
        super(new ItemStackVariableSerializer(), ItemStack.class);
    }

    public ItemStack[] fromJson(JsonElement json) {
        if (!json.isJsonArray()) {
            return this.fromNonArray(json);
        } else {
            JsonArray array = json.getAsJsonArray();
            List<ItemStack> stacks = new ArrayList();
            for (JsonElement e : array) {
                stacks.addAll(Arrays.asList(this.fromNonArray(e)));
            }
            return (ItemStack[]) stacks.toArray(this.empty);
        }
    }

    public ItemStack[] fromNonArray(JsonElement json) {
        if (json.isJsonNull()) {
            return this.empty;
        } else if (json.isJsonPrimitive()) {
            return (ItemStack[]) ItemStackUtil.loadStackListFromString(json.getAsString()).toArray(this.empty);
        } else if (json.isJsonObject()) {
            return new ItemStack[] { ItemStackUtil.loadStackFromJson(json.getAsJsonObject()) };
        } else {
            throw new IllegalArgumentException("Can't make an ItemStack from an array!");
        }
    }
}