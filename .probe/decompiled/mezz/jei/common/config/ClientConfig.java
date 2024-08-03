package mezz.jei.common.config;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.function.Supplier;
import mezz.jei.common.config.file.IConfigCategoryBuilder;
import mezz.jei.common.config.file.IConfigSchemaBuilder;
import mezz.jei.common.config.file.serializers.EnumSerializer;
import mezz.jei.common.config.file.serializers.ListSerializer;
import mezz.jei.common.platform.Services;
import org.jetbrains.annotations.Nullable;

public final class ClientConfig implements IClientConfig {

    @Nullable
    private static IClientConfig instance;

    private final Supplier<Boolean> centerSearchBarEnabled;

    private final Supplier<Boolean> lowMemorySlowSearchEnabled;

    private final Supplier<Boolean> catchRenderErrorsEnabled;

    private final Supplier<Boolean> cheatToHotbarUsingHotkeysEnabled;

    private final Supplier<Boolean> addBookmarksToFrontEnabled;

    private final Supplier<Boolean> lookupFluidContentsEnabled;

    private final Supplier<Boolean> lookupBlockTagsEnabled;

    private final Supplier<GiveMode> giveMode;

    private final Supplier<Integer> maxRecipeGuiHeight;

    private final Supplier<List<IngredientSortStage>> ingredientSorterStages;

    public ClientConfig(IConfigSchemaBuilder schema) {
        instance = this;
        boolean isDev = Services.PLATFORM.getModHelper().isInDev();
        IConfigCategoryBuilder advanced = schema.addCategory("advanced");
        this.centerSearchBarEnabled = advanced.addBoolean("CenterSearch", false, "Display search bar in the center");
        this.lowMemorySlowSearchEnabled = advanced.addBoolean("LowMemorySlowSearchEnabled", false, "Set low-memory mode (makes search very slow, but uses less RAM)");
        this.catchRenderErrorsEnabled = advanced.addBoolean("CatchRenderErrorsEnabled", !isDev, "Catch render errors from ingredients and attempt to recover from them instead of crashing.");
        this.cheatToHotbarUsingHotkeysEnabled = advanced.addBoolean("CheatToHotbarUsingHotkeysEnabled", false, "Enable cheating items into the hotbar by using the shift+number keys.");
        this.addBookmarksToFrontEnabled = advanced.addBoolean("AddBookmarksToFrontEnabled", true, "Enable adding new bookmarks to the front of the bookmark list.");
        this.lookupFluidContentsEnabled = advanced.addBoolean("lookupFluidContentsEnabled", false, "When looking up recipes with items that contain fluids, also look up recipes for the fluids.");
        this.lookupBlockTagsEnabled = advanced.addBoolean("lookupBlockTagsEnabled", true, "When searching for item tags, also include tags for the default blocks contained in the items.");
        this.giveMode = advanced.addEnum("GiveMode", GiveMode.defaultGiveMode, "How items should be handed to you");
        this.maxRecipeGuiHeight = advanced.addInteger("RecipeGuiHeight", 350, 175, Integer.MAX_VALUE, "Max. recipe gui height");
        IConfigCategoryBuilder sorting = schema.addCategory("sorting");
        this.ingredientSorterStages = sorting.addList("IngredientSortStages", IngredientSortStage.defaultStages, new ListSerializer<>(new EnumSerializer(IngredientSortStage.class)), "Sorting order for the ingredient list");
    }

    @Deprecated
    public static IClientConfig getInstance() {
        Preconditions.checkNotNull(instance);
        return instance;
    }

    @Override
    public boolean isCenterSearchBarEnabled() {
        return (Boolean) this.centerSearchBarEnabled.get();
    }

    @Override
    public boolean isLowMemorySlowSearchEnabled() {
        return (Boolean) this.lowMemorySlowSearchEnabled.get();
    }

    @Override
    public boolean isCatchRenderErrorsEnabled() {
        return (Boolean) this.catchRenderErrorsEnabled.get();
    }

    @Override
    public boolean isCheatToHotbarUsingHotkeysEnabled() {
        return (Boolean) this.cheatToHotbarUsingHotkeysEnabled.get();
    }

    @Override
    public boolean isAddingBookmarksToFrontEnabled() {
        return (Boolean) this.addBookmarksToFrontEnabled.get();
    }

    @Override
    public boolean isLookupFluidContentsEnabled() {
        return (Boolean) this.lookupFluidContentsEnabled.get();
    }

    @Override
    public boolean isLookupBlockTagsEnabled() {
        return (Boolean) this.lookupBlockTagsEnabled.get();
    }

    @Override
    public GiveMode getGiveMode() {
        return (GiveMode) this.giveMode.get();
    }

    @Override
    public int getMaxRecipeGuiHeight() {
        return (Integer) this.maxRecipeGuiHeight.get();
    }

    @Override
    public List<IngredientSortStage> getIngredientSorterStages() {
        return (List<IngredientSortStage>) this.ingredientSorterStages.get();
    }
}