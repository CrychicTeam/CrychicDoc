package mezz.jei.library.load;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableTable;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryDecorator;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.api.runtime.IJeiFeatures;
import mezz.jei.api.runtime.IScreenHelper;
import mezz.jei.common.Internal;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.platform.IPlatformFluidHelperInternal;
import mezz.jei.common.platform.Services;
import mezz.jei.common.util.StackHelper;
import mezz.jei.core.util.LoggedTimer;
import mezz.jei.library.config.IModIdFormatConfig;
import mezz.jei.library.config.RecipeCategorySortingConfig;
import mezz.jei.library.focus.FocusFactory;
import mezz.jei.library.gui.GuiHelper;
import mezz.jei.library.helpers.ModIdHelper;
import mezz.jei.library.ingredients.subtypes.SubtypeInterpreters;
import mezz.jei.library.ingredients.subtypes.SubtypeManager;
import mezz.jei.library.load.registration.AdvancedRegistration;
import mezz.jei.library.load.registration.GuiHandlerRegistration;
import mezz.jei.library.load.registration.IngredientManagerBuilder;
import mezz.jei.library.load.registration.RecipeCatalystRegistration;
import mezz.jei.library.load.registration.RecipeCategoryRegistration;
import mezz.jei.library.load.registration.RecipeRegistration;
import mezz.jei.library.load.registration.RecipeTransferRegistration;
import mezz.jei.library.load.registration.SubtypeRegistration;
import mezz.jei.library.load.registration.VanillaCategoryExtensionRegistration;
import mezz.jei.library.plugins.vanilla.VanillaPlugin;
import mezz.jei.library.plugins.vanilla.VanillaRecipeFactory;
import mezz.jei.library.plugins.vanilla.crafting.CraftingRecipeCategory;
import mezz.jei.library.recipes.RecipeManager;
import mezz.jei.library.recipes.RecipeManagerInternal;
import mezz.jei.library.runtime.JeiHelpers;
import mezz.jei.library.startup.StartData;
import mezz.jei.library.transfer.RecipeTransferHandlerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Unmodifiable;

public class PluginLoader {

    private final StartData data;

    private final LoggedTimer timer;

    private final IIngredientManager ingredientManager;

    private final JeiHelpers jeiHelpers;

    public PluginLoader(StartData data, IModIdFormatConfig modIdFormatConfig, IColorHelper colorHelper) {
        this.data = data;
        this.timer = new LoggedTimer();
        IPlatformFluidHelperInternal<?> fluidHelper = Services.PLATFORM.getFluidHelper();
        List<IModPlugin> plugins = data.plugins();
        SubtypeRegistration subtypeRegistration = new SubtypeRegistration();
        PluginCaller.callOnPlugins("Registering item subtypes", plugins, p -> p.registerItemSubtypes(subtypeRegistration));
        PluginCaller.callOnPlugins("Registering fluid subtypes", plugins, p -> p.registerFluidSubtypes(subtypeRegistration, fluidHelper));
        SubtypeInterpreters subtypeInterpreters = subtypeRegistration.getInterpreters();
        SubtypeManager subtypeManager = new SubtypeManager(subtypeInterpreters);
        IngredientManagerBuilder ingredientManagerBuilder = new IngredientManagerBuilder(subtypeManager, colorHelper);
        PluginCaller.callOnPlugins("Registering ingredients", plugins, p -> p.registerIngredients(ingredientManagerBuilder));
        this.ingredientManager = ingredientManagerBuilder.build();
        StackHelper stackHelper = new StackHelper(subtypeManager);
        GuiHelper guiHelper = new GuiHelper(this.ingredientManager);
        FocusFactory focusFactory = new FocusFactory(this.ingredientManager);
        IModIdHelper modIdHelper = new ModIdHelper(modIdFormatConfig, this.ingredientManager);
        this.jeiHelpers = new JeiHelpers(guiHelper, stackHelper, modIdHelper, focusFactory, colorHelper, this.ingredientManager);
    }

    @Unmodifiable
    private List<IRecipeCategory<?>> createRecipeCategories(List<IModPlugin> plugins, VanillaPlugin vanillaPlugin) {
        RecipeCategoryRegistration recipeCategoryRegistration = new RecipeCategoryRegistration(this.jeiHelpers);
        PluginCaller.callOnPlugins("Registering categories", plugins, p -> p.registerCategories(recipeCategoryRegistration));
        CraftingRecipeCategory craftingCategory = (CraftingRecipeCategory) vanillaPlugin.getCraftingCategory().orElseThrow(() -> new NullPointerException("vanilla crafting category"));
        VanillaCategoryExtensionRegistration vanillaCategoryExtensionRegistration = new VanillaCategoryExtensionRegistration(craftingCategory, this.jeiHelpers);
        PluginCaller.callOnPlugins("Registering vanilla category extensions", plugins, p -> p.registerVanillaCategoryExtensions(vanillaCategoryExtensionRegistration));
        return recipeCategoryRegistration.getRecipeCategories();
    }

    public IScreenHelper createGuiScreenHelper(List<IModPlugin> plugins, IJeiHelpers jeiHelpers) {
        GuiHandlerRegistration guiHandlerRegistration = new GuiHandlerRegistration(jeiHelpers);
        PluginCaller.callOnPlugins("Registering gui handlers", plugins, p -> p.registerGuiHandlers(guiHandlerRegistration));
        return guiHandlerRegistration.createGuiScreenHelper(this.ingredientManager);
    }

    public ImmutableTable<Class<? extends AbstractContainerMenu>, RecipeType<?>, IRecipeTransferHandler<?, ?>> createRecipeTransferHandlers(List<IModPlugin> plugins) {
        IStackHelper stackHelper = this.jeiHelpers.getStackHelper();
        IRecipeTransferHandlerHelper handlerHelper = new RecipeTransferHandlerHelper(stackHelper);
        RecipeTransferRegistration recipeTransferRegistration = new RecipeTransferRegistration(stackHelper, handlerHelper, this.jeiHelpers, this.data.serverConnection());
        PluginCaller.callOnPlugins("Registering recipes transfer handlers", plugins, p -> p.registerRecipeTransferHandlers(recipeTransferRegistration));
        return recipeTransferRegistration.getRecipeTransferHandlers();
    }

    public RecipeManager createRecipeManager(List<IModPlugin> plugins, VanillaPlugin vanillaPlugin, RecipeCategorySortingConfig recipeCategorySortingConfig, IModIdHelper modIdHelper, IIngredientVisibility ingredientVisibility) {
        List<IRecipeCategory<?>> recipeCategories = this.createRecipeCategories(plugins, vanillaPlugin);
        RecipeCatalystRegistration recipeCatalystRegistration = new RecipeCatalystRegistration(this.ingredientManager, this.jeiHelpers);
        PluginCaller.callOnPlugins("Registering recipe catalysts", plugins, p -> p.registerRecipeCatalysts(recipeCatalystRegistration));
        ImmutableListMultimap<ResourceLocation, ITypedIngredient<?>> recipeCatalysts = recipeCatalystRegistration.getRecipeCatalysts();
        IJeiFeatures jeiFeatures = Internal.getJeiFeatures();
        AdvancedRegistration advancedRegistration = new AdvancedRegistration(this.jeiHelpers, jeiFeatures);
        PluginCaller.callOnPlugins("Registering advanced plugins", plugins, p -> p.registerAdvanced(advancedRegistration));
        List<IRecipeManagerPlugin> recipeManagerPlugins = advancedRegistration.getRecipeManagerPlugins();
        ImmutableListMultimap<RecipeType<?>, IRecipeCategoryDecorator<?>> recipeCategoryExtensions = advancedRegistration.getRecipeCategoryDecorators();
        this.timer.start("Building recipe registry");
        RecipeManagerInternal recipeManagerInternal = new RecipeManagerInternal(recipeCategories, recipeCatalysts, recipeCategoryExtensions, this.ingredientManager, recipeManagerPlugins, recipeCategorySortingConfig, ingredientVisibility);
        this.timer.stop();
        VanillaRecipeFactory vanillaRecipeFactory = new VanillaRecipeFactory(this.ingredientManager);
        RecipeRegistration recipeRegistration = new RecipeRegistration(this.jeiHelpers, this.ingredientManager, ingredientVisibility, vanillaRecipeFactory, recipeManagerInternal);
        PluginCaller.callOnPlugins("Registering recipes", plugins, p -> p.registerRecipes(recipeRegistration));
        Textures textures = Internal.getTextures();
        return new RecipeManager(recipeManagerInternal, modIdHelper, this.ingredientManager, textures, ingredientVisibility);
    }

    public IIngredientManager getIngredientManager() {
        return this.ingredientManager;
    }

    public JeiHelpers getJeiHelpers() {
        return this.jeiHelpers;
    }
}