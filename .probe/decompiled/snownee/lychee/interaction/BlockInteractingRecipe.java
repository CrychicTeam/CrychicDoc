package snownee.lychee.interaction;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Function;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import snownee.lychee.RecipeSerializers;
import snownee.lychee.RecipeTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.def.IntBoundsHelper;
import snownee.lychee.core.recipe.ItemAndBlockRecipe;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.core.recipe.type.LycheeRecipeType;

public class BlockInteractingRecipe extends ItemAndBlockRecipe<LycheeContext> {

    protected Ingredient otherInput = Ingredient.EMPTY;

    public BlockInteractingRecipe(ResourceLocation id) {
        super(id);
        this.maxRepeats = IntBoundsHelper.ONE;
    }

    @Override
    public boolean matches(LycheeContext ctx, Level pLevel) {
        return !super.matches(ctx, pLevel) ? false : this.otherInput.isEmpty() || this.otherInput.test(ctx.getItem(1));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.otherInput.isEmpty() ? super.getIngredients() : NonNullList.of(Ingredient.EMPTY, this.input, this.otherInput);
    }

    @Override
    public LycheeRecipe.Serializer<?> getSerializer() {
        return RecipeSerializers.BLOCK_INTERACTING;
    }

    @Override
    public LycheeRecipeType<?, ?> getType() {
        return RecipeTypes.BLOCK_INTERACTING;
    }

    public static class Serializer<T extends BlockInteractingRecipe> extends ItemAndBlockRecipe.Serializer<T> {

        public Serializer(Function<ResourceLocation, T> factory) {
            super(factory);
        }

        public void fromJson(T pRecipe, JsonObject pSerializedRecipe) {
            JsonElement element = pSerializedRecipe.get("item_in");
            if (element.isJsonObject()) {
                pRecipe.input = parseIngredientOrAir(pSerializedRecipe.get("item_in"));
            } else {
                JsonArray array = element.getAsJsonArray();
                Preconditions.checkArgument(array.size() <= 2, "Too many items in item_in");
                pRecipe.input = parseIngredientOrAir(array.get(0));
                if (array.size() == 2) {
                    pRecipe.otherInput = parseIngredientOrAir(array.get(1));
                }
            }
            pRecipe.block = BlockPredicateHelper.fromJson(pSerializedRecipe.get("block_in"));
        }

        public void fromNetwork(T pRecipe, FriendlyByteBuf pBuffer) {
            super.fromNetwork(pRecipe, pBuffer);
            pRecipe.otherInput = Ingredient.fromNetwork(pBuffer);
        }

        public void toNetwork0(FriendlyByteBuf pBuffer, T pRecipe) {
            super.toNetwork0(pBuffer, pRecipe);
            pRecipe.otherInput.toNetwork(pBuffer);
        }
    }
}