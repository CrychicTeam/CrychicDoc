package se.mickelus.tetra.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import se.mickelus.tetra.blocks.scroll.ScrollData;
import se.mickelus.tetra.blocks.scroll.ScrollItem;

@ParametersAreNonnullByDefault
public class ScrollIngredient extends Ingredient {

    private final ItemStack itemStack;

    private final ScrollData data;

    protected ScrollIngredient(ItemStack itemStack, ScrollData data) {
        super(Stream.of(new Ingredient.ItemValue(itemStack)));
        this.itemStack = itemStack;
        this.data = data;
    }

    @Override
    public boolean test(@Nullable ItemStack input) {
        if (input != null && input.getItem() == ScrollItem.instance) {
            ScrollData inputData = ScrollData.read(input);
            return this.data.key.equals(inputData.key);
        } else {
            return false;
        }
    }

    public boolean isSimple() {
        return false;
    }

    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return ScrollIngredient.Serializer.instance;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        this.data.write(json);
        return json;
    }

    public static class Serializer implements IIngredientSerializer<ScrollIngredient> {

        public static final ScrollIngredient.Serializer instance = new ScrollIngredient.Serializer();

        public ScrollIngredient parse(JsonObject json) {
            ScrollData data = ScrollData.read(json);
            ItemStack itemStack = new ItemStack(ScrollItem.instance);
            data.write(itemStack);
            return new ScrollIngredient(itemStack, data);
        }

        public ScrollIngredient parse(FriendlyByteBuf buffer) {
            ItemStack itemStack = buffer.readItem();
            return new ScrollIngredient(itemStack, ScrollData.read(itemStack));
        }

        public void write(FriendlyByteBuf buffer, ScrollIngredient ingredient) {
            buffer.writeItem(ingredient.itemStack);
        }
    }
}