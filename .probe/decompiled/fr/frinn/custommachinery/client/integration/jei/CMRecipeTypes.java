package fr.frinn.custommachinery.client.integration.jei;

import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.common.crafting.craft.CustomCraftRecipe;
import fr.frinn.custommachinery.common.crafting.machine.CustomMachineRecipe;
import java.util.HashMap;
import java.util.Map;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class CMRecipeTypes {

    private static final Map<Class<? extends IMachineRecipe>, Map<ResourceLocation, RecipeType<? extends IMachineRecipe>>> TYPES = new HashMap();

    @Nullable
    public static <T extends IMachineRecipe> RecipeType<T> get(Class<T> recipeClass, ResourceLocation id) {
        Map<ResourceLocation, RecipeType<? extends IMachineRecipe>> map = (Map<ResourceLocation, RecipeType<? extends IMachineRecipe>>) TYPES.get(recipeClass);
        return map == null ? null : (RecipeType) map.get(id);
    }

    @Nullable
    public static RecipeType<CustomMachineRecipe> machine(ResourceLocation id) {
        return get(CustomMachineRecipe.class, id);
    }

    @Nullable
    public static RecipeType<CustomCraftRecipe> craft(ResourceLocation id) {
        return get(CustomCraftRecipe.class, id);
    }

    @Nullable
    public static RecipeType<? extends IMachineRecipe> fromID(ResourceLocation id) {
        return (RecipeType<? extends IMachineRecipe>) TYPES.values().stream().filter(map -> map.containsKey(id)).findFirst().map(map -> (RecipeType) map.get(id)).orElse(null);
    }

    public static <T extends IMachineRecipe> RecipeType<T> create(ResourceLocation id, Class<T> recipeClass) {
        RecipeType<T> type = RecipeType.create(id.getNamespace(), id.getPath(), recipeClass);
        ((Map) TYPES.computeIfAbsent(recipeClass, c -> new HashMap())).put(id, type);
        return type;
    }
}