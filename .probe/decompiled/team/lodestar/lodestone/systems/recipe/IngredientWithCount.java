package team.lodestar.lodestone.systems.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import team.lodestar.lodestone.helpers.ItemHelper;

public class IngredientWithCount implements IRecipeComponent {

    public final Ingredient ingredient;

    public final int count;

    public IngredientWithCount(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(this.getItem(), this.getCount(), this.ingredient.getItems()[0].getTag());
    }

    @Override
    public List<ItemStack> getStacks() {
        return ItemHelper.copyWithNewCount(List.of(this.ingredient.getItems()), this.getCount());
    }

    @Override
    public Item getItem() {
        return this.ingredient.getItems()[0].getItem();
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return this.ingredient.test(stack) && stack.getCount() >= this.getCount();
    }

    public static IngredientWithCount read(FriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        int count = buffer.readByte();
        return new IngredientWithCount(ingredient, count);
    }

    public void write(FriendlyByteBuf buffer) {
        this.ingredient.toNetwork(buffer);
        buffer.writeByte(this.count);
    }

    public static IngredientWithCount deserialize(JsonObject object) {
        Ingredient input = object.has("ingredient_list") ? Ingredient.fromJson(object.get("ingredient_list")) : Ingredient.fromJson(object);
        int count = GsonHelper.getAsInt(object, "count", 1);
        return new IngredientWithCount(input, count);
    }

    public JsonObject serialize() {
        JsonObject object = new JsonObject();
        JsonElement serialize = this.ingredient.toJson();
        if (serialize.isJsonObject()) {
            object = serialize.getAsJsonObject();
        } else {
            object.add("ingredient_list", this.ingredient.toJson());
        }
        object.addProperty("count", this.count);
        return object;
    }
}