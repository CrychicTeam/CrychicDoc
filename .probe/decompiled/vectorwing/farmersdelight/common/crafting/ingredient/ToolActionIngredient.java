package vectorwing.farmersdelight.common.crafting.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ToolActionIngredient extends Ingredient {

    public static final ToolActionIngredient.Serializer SERIALIZER = new ToolActionIngredient.Serializer();

    public final ToolAction toolAction;

    public ToolActionIngredient(ToolAction toolAction) {
        super(ForgeRegistries.ITEMS.getValues().stream().map(ItemStack::new).filter(stack -> stack.canPerformAction(toolAction)).map(Ingredient.ItemValue::new));
        this.toolAction = toolAction;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return stack != null && stack.canPerformAction(this.toolAction);
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(SERIALIZER).toString());
        json.addProperty("action", this.toolAction.name());
        return json;
    }

    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements IIngredientSerializer<ToolActionIngredient> {

        public ToolActionIngredient parse(JsonObject json) {
            return new ToolActionIngredient(ToolAction.get(json.get("action").getAsString()));
        }

        public ToolActionIngredient parse(FriendlyByteBuf buffer) {
            return new ToolActionIngredient(ToolAction.get(buffer.readUtf()));
        }

        public void write(FriendlyByteBuf buffer, ToolActionIngredient ingredient) {
            buffer.writeUtf(ingredient.toolAction.name());
        }
    }
}