package net.minecraftforge.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

public class MultiItemValue implements Ingredient.Value {

    private Collection<ItemStack> items;

    public MultiItemValue(Collection<ItemStack> items) {
        this.items = Collections.unmodifiableCollection(items);
    }

    @Override
    public Collection<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public JsonObject serialize() {
        if (this.items.size() == 1) {
            return this.toJson((ItemStack) this.items.iterator().next());
        } else {
            JsonObject ret = new JsonObject();
            JsonArray array = new JsonArray();
            this.items.forEach(stack -> array.add(this.toJson(stack)));
            ret.add("items", array);
            return ret;
        }
    }

    private JsonObject toJson(ItemStack stack) {
        JsonObject ret = new JsonObject();
        ret.addProperty("item", ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
        if (stack.getCount() != 1) {
            ret.addProperty("count", stack.getCount());
        }
        if (stack.getTag() != null) {
            ret.addProperty("nbt", stack.getTag().toString());
        }
        return ret;
    }
}