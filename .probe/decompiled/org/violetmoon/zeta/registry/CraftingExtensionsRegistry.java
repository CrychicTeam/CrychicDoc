package org.violetmoon.zeta.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.violetmoon.zeta.recipe.IZetaCondition;
import org.violetmoon.zeta.recipe.IZetaConditionSerializer;
import org.violetmoon.zeta.recipe.IZetaIngredientSerializer;

public interface CraftingExtensionsRegistry {

    <T extends IZetaCondition> IZetaConditionSerializer<T> registerConditionSerializer(IZetaConditionSerializer<T> var1);

    <T extends Ingredient> IZetaIngredientSerializer<T> registerIngredientSerializer(ResourceLocation var1, IZetaIngredientSerializer<T> var2);

    ResourceLocation getID(IZetaIngredientSerializer<?> var1);
}