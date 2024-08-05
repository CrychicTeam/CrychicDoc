package dev.xkmc.l2complements.compat;

import dev.xkmc.l2complements.content.recipe.BurntRecipe;
import dev.xkmc.l2complements.content.recipe.DiffusionRecipe;
import dev.xkmc.l2complements.init.registrate.LCBlocks;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2complements.init.registrate.LCRecipes;
import dev.xkmc.l2library.util.Proxy;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.ModList;

@JeiPlugin
public class LCJeiPlugin implements IModPlugin {

    public static LCJeiPlugin INSTANCE;

    public final ResourceLocation UID = new ResourceLocation("l2complements", "main");

    public final BurntRecipeCategory BURNT = new BurntRecipeCategory();

    public final DiffuseRecipeCategory DIFFUSE = new DiffuseRecipeCategory();

    public IGuiHelper GUI_HELPER;

    public LCJeiPlugin() {
        INSTANCE = this;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return this.UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        if (ModList.get().isLoaded("emi")) {
            registration.registerSubtypeInterpreter(Items.ENCHANTED_BOOK, LCEmiPlugin::partSubType);
        }
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(this.BURNT.init(helper));
        registration.addRecipeCategories(this.DIFFUSE.init(helper));
        this.GUI_HELPER = helper;
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel level = Proxy.getClientWorld();
        assert level != null;
        registration.addRecipes(this.BURNT.getRecipeType(), level.getRecipeManager().getAllRecipesFor((RecipeType<BurntRecipe>) LCRecipes.RT_BURNT.get()));
        registration.addRecipes(this.DIFFUSE.getRecipeType(), level.getRecipeManager().getAllRecipesFor((RecipeType<DiffusionRecipe>) LCRecipes.RT_DIFFUSION.get()));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(Items.LAVA_BUCKET.getDefaultInstance(), this.BURNT.getRecipeType());
        registration.addRecipeCatalyst(Items.FLINT_AND_STEEL.getDefaultInstance(), this.BURNT.getRecipeType());
        registration.addRecipeCatalyst(Items.FIRE_CHARGE.getDefaultInstance(), this.BURNT.getRecipeType());
        registration.addRecipeCatalyst(LCBlocks.ETERNAL_ANVIL.asStack(), RecipeTypes.ANVIL);
        registration.addRecipeCatalyst(LCItems.DIFFUSION_WAND.asStack(), this.DIFFUSE.getRecipeType());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    }
}