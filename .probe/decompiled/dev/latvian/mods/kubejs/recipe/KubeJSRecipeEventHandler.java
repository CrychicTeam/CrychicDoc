package dev.latvian.mods.kubejs.recipe;

import dev.architectury.registry.registries.DeferredRegister;
import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.recipe.special.ShapedKubeJSRecipe;
import dev.latvian.mods.kubejs.recipe.special.ShapelessKubeJSRecipe;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class KubeJSRecipeEventHandler {

    public static final DeferredRegister<RecipeSerializer<?>> REGISTER = DeferredRegister.create("kubejs", Registries.RECIPE_SERIALIZER);

    public static Supplier<RecipeSerializer<?>> SHAPED;

    public static Supplier<RecipeSerializer<?>> SHAPELESS;

    public static void init() {
        if (!CommonProperties.get().serverOnly) {
            registry();
        }
    }

    private static void registry() {
        SHAPED = REGISTER.register("shaped", ShapedKubeJSRecipe.SerializerKJS::new);
        SHAPELESS = REGISTER.register("shapeless", ShapelessKubeJSRecipe.SerializerKJS::new);
        REGISTER.register();
    }
}