package net.minecraftforge.common.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.stream.Stream;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class StrictNBTIngredient extends AbstractIngredient {

    private final ItemStack stack;

    protected StrictNBTIngredient(ItemStack stack) {
        super(Stream.of(new Ingredient.ItemValue(stack)));
        this.stack = stack;
    }

    public static StrictNBTIngredient of(ItemStack stack) {
        return new StrictNBTIngredient(stack);
    }

    @Override
    public boolean test(@Nullable ItemStack input) {
        return input == null ? false : this.stack.getItem() == input.getItem() && this.stack.getDamageValue() == input.getDamageValue() && this.stack.areShareTagsEqual(input);
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return StrictNBTIngredient.Serializer.INSTANCE;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(StrictNBTIngredient.Serializer.INSTANCE).toString());
        json.addProperty("item", ForgeRegistries.ITEMS.getKey(this.stack.getItem()).toString());
        json.addProperty("count", this.stack.getCount());
        if (this.stack.hasTag()) {
            json.addProperty("nbt", this.stack.getTag().toString());
        }
        return json;
    }

    public static class Serializer implements IIngredientSerializer<StrictNBTIngredient> {

        public static final StrictNBTIngredient.Serializer INSTANCE = new StrictNBTIngredient.Serializer();

        public StrictNBTIngredient parse(FriendlyByteBuf buffer) {
            return new StrictNBTIngredient(buffer.readItem());
        }

        public StrictNBTIngredient parse(JsonObject json) {
            return new StrictNBTIngredient(CraftingHelper.getItemStack(json, true));
        }

        public void write(FriendlyByteBuf buffer, StrictNBTIngredient ingredient) {
            buffer.writeItem(ingredient.stack);
        }
    }
}