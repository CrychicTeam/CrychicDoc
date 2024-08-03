package dev.xkmc.l2hostility.compat.jei;

import dev.xkmc.l2hostility.init.network.LootDataToClient;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEICompat implements IModPlugin {

    public static final ResourceLocation ID = new ResourceLocation("l2hostility", "main");

    public final GLMRecipeCategory LOOT = new GLMRecipeCategory();

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(this.LOOT.init(helper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(this.LOOT.getRecipeType(), List.of(new EnvyLootRecipe(), new GluttonyLootRecipe()));
        registration.addRecipes(this.LOOT.getRecipeType(), LootDataToClient.LIST_CACHE);
    }
}