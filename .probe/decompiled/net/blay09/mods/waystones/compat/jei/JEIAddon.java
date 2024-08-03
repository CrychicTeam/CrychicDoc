package net.blay09.mods.waystones.compat.jei;

import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEIAddon implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("waystones", "jei");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        registry.addRecipes(WarpPlateJeiRecipeCategory.TYPE, List.of(new AttunedShardJeiRecipe()));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new WarpPlateJeiRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }
}