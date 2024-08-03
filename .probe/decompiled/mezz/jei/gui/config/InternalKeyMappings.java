package mezz.jei.gui.config;

import java.util.function.Consumer;
import mezz.jei.api.runtime.IJeiKeyMapping;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.common.input.keys.IJeiKeyMappingCategoryBuilder;
import mezz.jei.common.input.keys.JeiKeyConflictContext;
import mezz.jei.common.input.keys.JeiKeyModifier;
import mezz.jei.common.input.keys.JeiMultiKeyMapping;
import mezz.jei.common.platform.IPlatformInputHelper;
import mezz.jei.common.platform.Services;
import mezz.jei.common.util.Translator;
import net.minecraft.client.KeyMapping;

public final class InternalKeyMappings implements IInternalKeyMappings {

    private final IJeiKeyMapping toggleOverlay;

    private final IJeiKeyMapping focusSearch;

    private final IJeiKeyMapping toggleCheatMode;

    private final IJeiKeyMapping toggleEditMode;

    private final IJeiKeyMapping toggleCheatModeConfigButton;

    private final IJeiKeyMapping recipeBack;

    private final IJeiKeyMapping previousCategory;

    private final IJeiKeyMapping nextCategory;

    private final IJeiKeyMapping previousRecipePage;

    private final IJeiKeyMapping nextRecipePage;

    private final IJeiKeyMapping previousPage;

    private final IJeiKeyMapping nextPage;

    private final IJeiKeyMapping bookmark;

    private final IJeiKeyMapping toggleBookmarkOverlay;

    private final IJeiKeyMapping showRecipe;

    private final IJeiKeyMapping showUses;

    private final IJeiKeyMapping cheatOneItem;

    private final IJeiKeyMapping cheatItemStack;

    private final IJeiKeyMapping toggleHideIngredient;

    private final IJeiKeyMapping toggleWildcardHideIngredient;

    private final IJeiKeyMapping hoveredClearSearchBar;

    private final IJeiKeyMapping previousSearch;

    private final IJeiKeyMapping nextSearch;

    private final IJeiKeyMapping copyRecipeId;

    private final IJeiKeyMapping closeRecipeGui;

    private final IJeiKeyMapping escapeKey;

    private final IJeiKeyMapping leftClick;

    private final IJeiKeyMapping rightClick;

    private final IJeiKeyMapping enterKey;

    public InternalKeyMappings(Consumer<KeyMapping> registerMethod) {
        IPlatformInputHelper inputHelper = Services.PLATFORM.getInputHelper();
        String overlaysCategoryName = Translator.translateToLocal("jei.key.category.overlays");
        IJeiKeyMappingCategoryBuilder overlay = inputHelper.createKeyMappingCategoryBuilder(overlaysCategoryName);
        String mouseHoverCategoryName = Translator.translateToLocal("jei.key.category.mouse.hover");
        IJeiKeyMappingCategoryBuilder mouseHover = inputHelper.createKeyMappingCategoryBuilder(mouseHoverCategoryName);
        String searchCategoryName = Translator.translateToLocal("jei.key.category.search");
        IJeiKeyMappingCategoryBuilder search = inputHelper.createKeyMappingCategoryBuilder(searchCategoryName);
        String cheatModeCategoryName = Translator.translateToLocal("jei.key.category.cheat.mode");
        IJeiKeyMappingCategoryBuilder cheat = inputHelper.createKeyMappingCategoryBuilder(cheatModeCategoryName);
        String hoverConfigButtonCategoryName = Translator.translateToLocal("jei.key.category.hover.config.button");
        IJeiKeyMappingCategoryBuilder hoverConfig = inputHelper.createKeyMappingCategoryBuilder(hoverConfigButtonCategoryName);
        String editModeCategoryName = Translator.translateToLocal("jei.key.category.edit.mode");
        IJeiKeyMappingCategoryBuilder editMode = inputHelper.createKeyMappingCategoryBuilder(editModeCategoryName);
        String recipeCategoryName = Translator.translateToLocal("jei.key.category.recipe.gui");
        IJeiKeyMappingCategoryBuilder recipeCategory = inputHelper.createKeyMappingCategoryBuilder(recipeCategoryName);
        String devToolsCategoryName = Translator.translateToLocal("jei.key.category.dev.tools");
        IJeiKeyMappingCategoryBuilder devTools = inputHelper.createKeyMappingCategoryBuilder(devToolsCategoryName);
        this.toggleOverlay = overlay.createMapping("key.jei.toggleOverlay").setContext(JeiKeyConflictContext.GUI).setModifier(JeiKeyModifier.CONTROL_OR_COMMAND).buildKeyboardKey(79).register(registerMethod);
        this.focusSearch = overlay.createMapping("key.jei.focusSearch").setContext(JeiKeyConflictContext.GUI).setModifier(JeiKeyModifier.CONTROL_OR_COMMAND).buildKeyboardKey(70).register(registerMethod);
        this.previousPage = overlay.createMapping("key.jei.previousPage").setContext(JeiKeyConflictContext.GUI).buildUnbound().register(registerMethod);
        this.nextPage = overlay.createMapping("key.jei.nextPage").setContext(JeiKeyConflictContext.GUI).buildUnbound().register(registerMethod);
        this.toggleBookmarkOverlay = overlay.createMapping("key.jei.toggleBookmarkOverlay").setContext(JeiKeyConflictContext.GUI).buildUnbound().register(registerMethod);
        this.bookmark = mouseHover.createMapping("key.jei.bookmark").setContext(JeiKeyConflictContext.JEI_GUI_HOVER).buildKeyboardKey(65).register(registerMethod);
        IJeiKeyMapping showRecipe1 = mouseHover.createMapping("key.jei.showRecipe").setContext(JeiKeyConflictContext.JEI_GUI_HOVER).buildKeyboardKey(82).register(registerMethod);
        IJeiKeyMapping showRecipe2 = mouseHover.createMapping("key.jei.showRecipe2").setContext(JeiKeyConflictContext.JEI_GUI_HOVER).buildMouseLeft().register(registerMethod);
        IJeiKeyMapping showUses1 = mouseHover.createMapping("key.jei.showUses").setContext(JeiKeyConflictContext.JEI_GUI_HOVER).buildKeyboardKey(85).register(registerMethod);
        IJeiKeyMapping showUses2 = mouseHover.createMapping("key.jei.showUses2").setContext(JeiKeyConflictContext.JEI_GUI_HOVER).buildMouseRight().register(registerMethod);
        this.hoveredClearSearchBar = search.createMapping("key.jei.clearSearchBar").setContext(JeiKeyConflictContext.JEI_GUI_HOVER_SEARCH).buildMouseRight().register(registerMethod);
        this.previousSearch = search.createMapping("key.jei.previousSearch").setContext(JeiKeyConflictContext.GUI).buildKeyboardKey(265).register(registerMethod);
        this.nextSearch = search.createMapping("key.jei.nextSearch").setContext(JeiKeyConflictContext.GUI).buildKeyboardKey(264).register(registerMethod);
        this.toggleCheatMode = cheat.createMapping("key.jei.toggleCheatMode").setContext(JeiKeyConflictContext.GUI).buildUnbound().register(registerMethod);
        IJeiKeyMapping cheatOneItem1 = cheat.createMapping("key.jei.cheatOneItem").setContext(JeiKeyConflictContext.JEI_GUI_HOVER_CHEAT_MODE).buildMouseLeft().register(registerMethod);
        IJeiKeyMapping cheatOneItem2 = cheat.createMapping("key.jei.cheatOneItem2").setContext(JeiKeyConflictContext.JEI_GUI_HOVER_CHEAT_MODE).buildMouseRight().register(registerMethod);
        IJeiKeyMapping cheatItemStack1 = cheat.createMapping("key.jei.cheatItemStack").setContext(JeiKeyConflictContext.JEI_GUI_HOVER_CHEAT_MODE).setModifier(JeiKeyModifier.SHIFT).buildMouseLeft().register(registerMethod);
        IJeiKeyMapping cheatItemStack2 = cheat.createMapping("key.jei.cheatItemStack2").setContext(JeiKeyConflictContext.JEI_GUI_HOVER_CHEAT_MODE).buildMouseMiddle().register(registerMethod);
        this.toggleCheatModeConfigButton = hoverConfig.createMapping("key.jei.toggleCheatModeConfigButton").setContext(JeiKeyConflictContext.JEI_GUI_HOVER_CONFIG_BUTTON).setModifier(JeiKeyModifier.CONTROL_OR_COMMAND).buildMouseLeft().register(registerMethod);
        this.toggleEditMode = editMode.createMapping("key.jei.toggleEditMode").setContext(JeiKeyConflictContext.GUI).buildUnbound().register(registerMethod);
        this.toggleHideIngredient = editMode.createMapping("key.jei.toggleHideIngredient").setContext(JeiKeyConflictContext.JEI_GUI_HOVER).setModifier(JeiKeyModifier.CONTROL_OR_COMMAND).buildMouseLeft().register(registerMethod);
        this.toggleWildcardHideIngredient = editMode.createMapping("key.jei.toggleWildcardHideIngredient").setContext(JeiKeyConflictContext.JEI_GUI_HOVER).setModifier(JeiKeyModifier.CONTROL_OR_COMMAND).buildMouseRight().register(registerMethod);
        this.recipeBack = recipeCategory.createMapping("key.jei.recipeBack").setContext(JeiKeyConflictContext.GUI).buildKeyboardKey(259).register(registerMethod);
        this.previousRecipePage = recipeCategory.createMapping("key.jei.previousRecipePage").setContext(JeiKeyConflictContext.GUI).buildKeyboardKey(266).register(registerMethod);
        this.nextRecipePage = recipeCategory.createMapping("key.jei.nextRecipePage").setContext(JeiKeyConflictContext.GUI).buildKeyboardKey(267).register(registerMethod);
        this.previousCategory = recipeCategory.createMapping("key.jei.previousCategory").setContext(JeiKeyConflictContext.GUI).setModifier(JeiKeyModifier.SHIFT).buildKeyboardKey(266).register(registerMethod);
        this.nextCategory = recipeCategory.createMapping("key.jei.nextCategory").setContext(JeiKeyConflictContext.GUI).setModifier(JeiKeyModifier.SHIFT).buildKeyboardKey(267).register(registerMethod);
        this.closeRecipeGui = recipeCategory.createMapping("key.jei.closeRecipeGui").setContext(JeiKeyConflictContext.GUI).buildKeyboardKey(256).register(registerMethod);
        this.copyRecipeId = devTools.createMapping("key.jei.copy.recipe.id").setContext(JeiKeyConflictContext.GUI).buildUnbound().register(registerMethod);
        this.showRecipe = new JeiMultiKeyMapping(showRecipe1, showRecipe2);
        this.showUses = new JeiMultiKeyMapping(showUses1, showUses2);
        this.cheatOneItem = new JeiMultiKeyMapping(cheatOneItem1, cheatOneItem2);
        this.cheatItemStack = new JeiMultiKeyMapping(cheatItemStack1, cheatItemStack2);
        String jeiHiddenInternalCategoryName = "jei.key.category.hidden.internal";
        IJeiKeyMappingCategoryBuilder jeiHidden = inputHelper.createKeyMappingCategoryBuilder(jeiHiddenInternalCategoryName);
        this.escapeKey = jeiHidden.createMapping("key.jei.internal.escape.key").setContext(JeiKeyConflictContext.GUI).buildKeyboardKey(256);
        this.leftClick = jeiHidden.createMapping("key.jei.internal.left.click").setContext(JeiKeyConflictContext.GUI).buildMouseLeft();
        this.rightClick = jeiHidden.createMapping("key.jei.internal.right.click").setContext(JeiKeyConflictContext.GUI).buildMouseRight();
        this.enterKey = new JeiMultiKeyMapping(jeiHidden.createMapping("key.jei.internal.enter.key").setContext(JeiKeyConflictContext.GUI).buildKeyboardKey(257), jeiHidden.createMapping("key.jei.internal.enter.key2").setContext(JeiKeyConflictContext.GUI).buildKeyboardKey(335));
    }

    @Override
    public IJeiKeyMapping getToggleOverlay() {
        return this.toggleOverlay;
    }

    @Override
    public IJeiKeyMapping getFocusSearch() {
        return this.focusSearch;
    }

    @Override
    public IJeiKeyMapping getToggleCheatMode() {
        return this.toggleCheatMode;
    }

    @Override
    public IJeiKeyMapping getToggleEditMode() {
        return this.toggleEditMode;
    }

    @Override
    public IJeiKeyMapping getToggleCheatModeConfigButton() {
        return this.toggleCheatModeConfigButton;
    }

    @Override
    public IJeiKeyMapping getRecipeBack() {
        return this.recipeBack;
    }

    @Override
    public IJeiKeyMapping getPreviousCategory() {
        return this.previousCategory;
    }

    @Override
    public IJeiKeyMapping getNextCategory() {
        return this.nextCategory;
    }

    @Override
    public IJeiKeyMapping getPreviousRecipePage() {
        return this.previousRecipePage;
    }

    @Override
    public IJeiKeyMapping getNextRecipePage() {
        return this.nextRecipePage;
    }

    @Override
    public IJeiKeyMapping getPreviousPage() {
        return this.previousPage;
    }

    @Override
    public IJeiKeyMapping getNextPage() {
        return this.nextPage;
    }

    @Override
    public IJeiKeyMapping getCloseRecipeGui() {
        return this.closeRecipeGui;
    }

    @Override
    public IJeiKeyMapping getBookmark() {
        return this.bookmark;
    }

    @Override
    public IJeiKeyMapping getToggleBookmarkOverlay() {
        return this.toggleBookmarkOverlay;
    }

    @Override
    public IJeiKeyMapping getShowRecipe() {
        return this.showRecipe;
    }

    @Override
    public IJeiKeyMapping getShowUses() {
        return this.showUses;
    }

    @Override
    public IJeiKeyMapping getCheatOneItem() {
        return this.cheatOneItem;
    }

    @Override
    public IJeiKeyMapping getCheatItemStack() {
        return this.cheatItemStack;
    }

    @Override
    public IJeiKeyMapping getToggleHideIngredient() {
        return this.toggleHideIngredient;
    }

    @Override
    public IJeiKeyMapping getToggleWildcardHideIngredient() {
        return this.toggleWildcardHideIngredient;
    }

    @Override
    public IJeiKeyMapping getHoveredClearSearchBar() {
        return this.hoveredClearSearchBar;
    }

    @Override
    public IJeiKeyMapping getPreviousSearch() {
        return this.previousSearch;
    }

    @Override
    public IJeiKeyMapping getNextSearch() {
        return this.nextSearch;
    }

    @Override
    public IJeiKeyMapping getCopyRecipeId() {
        return this.copyRecipeId;
    }

    @Override
    public IJeiKeyMapping getEscapeKey() {
        return this.escapeKey;
    }

    @Override
    public IJeiKeyMapping getLeftClick() {
        return this.leftClick;
    }

    @Override
    public IJeiKeyMapping getRightClick() {
        return this.rightClick;
    }

    @Override
    public IJeiKeyMapping getEnterKey() {
        return this.enterKey;
    }
}