package vazkii.patchouli.common.recipe;

import com.google.gson.JsonObject;
import java.util.function.BiFunction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.book.BookRegistry;
import vazkii.patchouli.common.item.PatchouliItems;

public record BookRecipeSerializer<T extends Recipe<?>, U extends T>(RecipeSerializer<T> compose, BiFunction<T, ResourceLocation, U> converter) implements RecipeSerializer<U> {

    @NotNull
    @Override
    public U fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
        if (!json.has("result")) {
            JsonObject object = new JsonObject();
            object.addProperty("item", PatchouliItems.BOOK_ID.toString());
            json.add("result", object);
        }
        T recipe = this.compose().fromJson(id, json);
        ResourceLocation outputBook = new ResourceLocation(GsonHelper.getAsString(json, "book"));
        if (!BookRegistry.INSTANCE.books.containsKey(outputBook)) {
            PatchouliAPI.LOGGER.warn("Book {} in recipe {} does not exist!", outputBook, id);
        }
        return (U) ((Recipe) this.converter().apply(recipe, outputBook));
    }

    @NotNull
    @Override
    public U fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
        T recipe = this.compose().fromNetwork(id, buf);
        return (U) ((Recipe) this.converter().apply(recipe, null));
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull U recipe) {
        this.compose().toNetwork(buf, (T) recipe);
    }
}