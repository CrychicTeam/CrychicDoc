package dev.xkmc.l2complements.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2complements.content.recipe.BurntRecipe;
import dev.xkmc.l2complements.content.recipe.DiffusionRecipe;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

public class LCRecipes {

    public static RegistryEntry<RecipeType<BurntRecipe>> RT_BURNT = L2Complements.REGISTRATE.recipe("burnt");

    public static RegistryEntry<RecipeType<DiffusionRecipe>> RT_DIFFUSION = L2Complements.REGISTRATE.recipe("diffusion");

    public static final RegistryEntry<BaseRecipe.RecType<BurntRecipe, BurntRecipe, BurntRecipe.Inv>> RS_BURNT = reg("burnt", () -> new BaseRecipe.RecType(BurntRecipe.class, RT_BURNT));

    public static final RegistryEntry<BaseRecipe.RecType<DiffusionRecipe, DiffusionRecipe, DiffusionRecipe.Inv>> RS_DIFFUSION = reg("diffusion", () -> new BaseRecipe.RecType(DiffusionRecipe.class, RT_DIFFUSION));

    public static void register() {
    }

    private static <A extends RecipeSerializer<?>> RegistryEntry<A> reg(String id, NonNullSupplier<A> sup) {
        return L2Complements.REGISTRATE.simple(id, ForgeRegistries.Keys.RECIPE_SERIALIZERS, sup);
    }
}