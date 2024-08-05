package pie.ilikepiefoo.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IPlatformFluidHelper;
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
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import pie.ilikepiefoo.compat.jei.events.JEIEventJS;
import pie.ilikepiefoo.compat.jei.events.OnRuntimeAvailableEventJS;
import pie.ilikepiefoo.compat.jei.events.RegisterAdvancedEventJS;
import pie.ilikepiefoo.compat.jei.events.RegisterCategoriesEventJS;
import pie.ilikepiefoo.compat.jei.events.RegisterFluidSubtypeEventJS;
import pie.ilikepiefoo.compat.jei.events.RegisterGUIHandlersEventJS;
import pie.ilikepiefoo.compat.jei.events.RegisterIngredientsEventJS;
import pie.ilikepiefoo.compat.jei.events.RegisterItemSubtypeEventJS;
import pie.ilikepiefoo.compat.jei.events.RegisterRecipeCatalystsEventJS;
import pie.ilikepiefoo.compat.jei.events.RegisterRecipeTransferHandlersEventJS;
import pie.ilikepiefoo.compat.jei.events.RegisterRecipesEventJS;
import pie.ilikepiefoo.compat.jei.events.RegisterVanillaCategoryExtensionsEventJS;
import pie.ilikepiefoo.compat.jei.impl.CustomJSRecipe;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    @NotNull
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("kubejsadditions", "jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        JEIEvents.REGISTER_ITEM_SUBTYPES.post(new RegisterItemSubtypeEventJS(registration));
    }

    @Override
    public <T> void registerFluidSubtypes(ISubtypeRegistration registration, IPlatformFluidHelper<T> platformFluidHelper) {
        JEIEvents.REGISTER_FLUID_SUBTYPES.post(new RegisterFluidSubtypeEventJS(registration));
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        JEIEvents.REGISTER_INGREDIENTS.post(new RegisterIngredientsEventJS(registration));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        JEIEventJS.JEI_HELPERS = registration.getJeiHelpers();
        JEIEvents.REGISTER_CATEGORIES.post(new RegisterCategoriesEventJS(registration));
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        JEIEventJS.JEI_HELPERS = registration.getJeiHelpers();
        JEIEvents.REGISTER_VANILLA_CATEGORY_EXTENSIONS.post(new RegisterVanillaCategoryExtensionsEventJS(registration));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        JEIEventJS.JEI_HELPERS = registration.getJeiHelpers();
        RegisterRecipesEventJS event = new RegisterRecipesEventJS(registration);
        JEIEvents.REGISTER_RECIPES.post(event);
        for (CustomJSRecipe.CustomRecipeListBuilder builder : event.customRecipeListBuilders) {
            registration.addRecipes(builder.getRecipeType(), builder.getRecipes());
        }
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        JEIEventJS.JEI_HELPERS = registration.getJeiHelpers();
        JEIEvents.REGISTER_RECIPE_TRANSFER_HANDLERS.post(new RegisterRecipeTransferHandlersEventJS(registration));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        JEIEventJS.JEI_HELPERS = registration.getJeiHelpers();
        JEIEvents.REGISTER_RECIPE_CATALYSTS.post(new RegisterRecipeCatalystsEventJS(registration));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        JEIEventJS.JEI_HELPERS = registration.getJeiHelpers();
        JEIEvents.REGISTER_GUI_HANDLERS.post(new RegisterGUIHandlersEventJS(registration));
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        JEIEventJS.JEI_HELPERS = registration.getJeiHelpers();
        JEIEvents.REGISTER_ADVANCED.post(new RegisterAdvancedEventJS(registration));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        JEIEventJS.JEI_HELPERS = jeiRuntime.getJeiHelpers();
        JEIEvents.ON_RUNTIME_AVAILABLE.post(new OnRuntimeAvailableEventJS(jeiRuntime));
    }
}