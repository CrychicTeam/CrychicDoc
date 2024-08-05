package fuzs.puzzleslib.impl.item;

import com.google.gson.JsonObject;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public interface CopyTagRecipe {

    String SHAPED_RECIPE_SERIALIZER_ID = "copy_tag_shaped_recipe";

    String SHAPELESS_RECIPE_SERIALIZER_ID = "copy_tag_shapeless_recipe";

    static RecipeSerializer<?> getModSerializer(String modId, String recipeSerializerId) {
        RecipeSerializer<?> recipeSerializer = BuiltInRegistries.RECIPE_SERIALIZER.get(new ResourceLocation(modId, recipeSerializerId));
        Objects.requireNonNull(recipeSerializer, "%s serializer for %s is null".formatted(recipeSerializerId, modId));
        return recipeSerializer;
    }

    static void registerSerializers(BiConsumer<String, Supplier<RecipeSerializer<?>>> registrar) {
        registrar.accept("copy_tag_shaped_recipe", (Supplier) () -> new CopyTagRecipe.Serializer<>(new ShapedRecipe.Serializer(), CopyTagShapedRecipe::new));
        registrar.accept("copy_tag_shapeless_recipe", (Supplier) () -> new CopyTagRecipe.Serializer<>(new ShapelessRecipe.Serializer(), CopyTagShapelessRecipe::new));
    }

    Ingredient getCopyTagSource();

    default void tryCopyTagToResult(ItemStack result, CraftingContainer craftingContainer) {
        for (int i = 0; i < craftingContainer.m_6643_(); i++) {
            ItemStack itemStack = craftingContainer.m_8020_(i);
            if (this.getCopyTagSource().test(itemStack) && itemStack.hasTag()) {
                result.setTag(itemStack.getTag().copy());
                return;
            }
        }
    }

    public static record Serializer<T extends CraftingRecipe, S extends CraftingRecipe & CopyTagRecipe>(RecipeSerializer<T> serializer, BiFunction<T, Ingredient, S> factory) implements RecipeSerializer<S> {

        public S fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            T recipe = this.serializer.fromJson(recipeId, serializedRecipe);
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getNonNull(serializedRecipe, "copy_from"));
            return (S) this.factory.apply(recipe, ingredient);
        }

        public S fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            T recipe = this.serializer.fromNetwork(recipeId, buffer);
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            return (S) this.factory.apply(recipe, ingredient);
        }

        public void toNetwork(FriendlyByteBuf buffer, S recipe) {
            this.serializer.toNetwork(buffer, (T) recipe);
            recipe.getCopyTagSource().toNetwork(buffer);
        }
    }
}