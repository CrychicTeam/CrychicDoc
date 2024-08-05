package vazkii.patchouli.common.item;

import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import vazkii.patchouli.common.recipe.ShapedBookRecipe;
import vazkii.patchouli.common.recipe.ShapelessBookRecipe;

public class PatchouliItems {

    public static final ResourceLocation BOOK_ID = new ResourceLocation("patchouli", "guide_book");

    public static final Item BOOK = new ItemModBook();

    public static void submitItemRegistrations(BiConsumer<ResourceLocation, Item> consumer) {
        consumer.accept(BOOK_ID, BOOK);
    }

    public static void submitRecipeSerializerRegistrations(BiConsumer<ResourceLocation, RecipeSerializer<?>> consumer) {
        consumer.accept(new ResourceLocation("patchouli", "shaped_book_recipe"), ShapedBookRecipe.SERIALIZER);
        consumer.accept(new ResourceLocation("patchouli", "shapeless_book_recipe"), ShapelessBookRecipe.SERIALIZER);
    }
}