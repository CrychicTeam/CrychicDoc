package dev.latvian.mods.kubejs.recipe.ingredientaction;

import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

public class CustomIngredientAction extends IngredientAction {

    public static final Map<String, CustomIngredientActionCallback> MAP = new HashMap();

    public final String id;

    public CustomIngredientAction(String i) {
        this.id = i;
    }

    @Override
    public ItemStack transform(ItemStack old, int index, CraftingContainer container) {
        CustomIngredientActionCallback callback = (CustomIngredientActionCallback) MAP.get(this.id);
        return callback == null ? ItemStack.EMPTY : callback.transform(old, index, container).copy();
    }

    @Override
    public String getType() {
        return "custom";
    }

    @Override
    public void toJson(JsonObject json) {
        json.addProperty("id", this.id);
    }
}