package mezz.jei.common.gui.textures;

import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.common.gui.elements.DrawableNineSliceTexture;
import mezz.jei.common.gui.elements.DrawableSprite;
import mezz.jei.common.gui.elements.HighResolutionDrawable;
import net.minecraft.resources.ResourceLocation;

public class Textures {

    private final JeiSpriteUploader spriteUploader;

    private final IDrawableStatic slot;

    private final DrawableNineSliceTexture recipeCatalystSlotBackground;

    private final DrawableNineSliceTexture ingredientListSlotBackground;

    private final DrawableNineSliceTexture bookmarkListSlotBackground;

    private final IDrawableStatic tabSelected;

    private final IDrawableStatic tabUnselected;

    private final DrawableNineSliceTexture buttonDisabled;

    private final DrawableNineSliceTexture buttonEnabled;

    private final DrawableNineSliceTexture buttonHighlight;

    private final DrawableNineSliceTexture recipeGuiBackground;

    private final DrawableNineSliceTexture ingredientListBackground;

    private final DrawableNineSliceTexture bookmarkListBackground;

    private final DrawableNineSliceTexture recipeBackground;

    private final DrawableNineSliceTexture searchBackground;

    private final HighResolutionDrawable shapelessIcon;

    private final IDrawableStatic arrowPrevious;

    private final IDrawableStatic arrowNext;

    private final IDrawableStatic recipeTransfer;

    private final IDrawableStatic configButtonIcon;

    private final IDrawableStatic configButtonCheatIcon;

    private final IDrawableStatic bookmarkButtonDisabledIcon;

    private final IDrawableStatic bookmarkButtonEnabledIcon;

    private final IDrawableStatic infoIcon;

    private final DrawableNineSliceTexture catalystTab;

    private final IDrawableStatic flameIcon;

    public Textures(JeiSpriteUploader spriteUploader) {
        this.spriteUploader = spriteUploader;
        this.slot = this.createGuiSprite("slot", 18, 18);
        this.recipeCatalystSlotBackground = this.createNineSliceGuiSprite("recipe_catalyst_slot_background", 18, 18, 4, 4, 4, 4);
        this.ingredientListSlotBackground = this.createNineSliceGuiSprite("ingredient_list_slot_background", 18, 18, 4, 4, 4, 4);
        this.bookmarkListSlotBackground = this.createNineSliceGuiSprite("bookmark_list_slot_background", 18, 18, 4, 4, 4, 4);
        this.tabSelected = this.createGuiSprite("tab_selected", 24, 24);
        this.tabUnselected = this.createGuiSprite("tab_unselected", 24, 24);
        this.buttonDisabled = this.createNineSliceGuiSprite("button_disabled", 20, 20, 6, 6, 6, 6);
        this.buttonEnabled = this.createNineSliceGuiSprite("button_enabled", 20, 20, 6, 6, 6, 6);
        this.buttonHighlight = this.createNineSliceGuiSprite("button_highlight", 20, 20, 6, 6, 6, 6);
        this.recipeGuiBackground = this.createNineSliceGuiSprite("gui_background", 64, 64, 16, 16, 16, 16);
        this.ingredientListBackground = this.createNineSliceGuiSprite("ingredient_list_background", 64, 64, 16, 16, 16, 16);
        this.bookmarkListBackground = this.createNineSliceGuiSprite("bookmark_list_background", 64, 64, 16, 16, 16, 16);
        this.recipeBackground = this.createNineSliceGuiSprite("single_recipe_background", 64, 64, 16, 16, 16, 16);
        this.searchBackground = this.createNineSliceGuiSprite("search_background", 20, 20, 6, 6, 6, 6);
        this.catalystTab = this.createNineSliceGuiSprite("catalyst_tab", 28, 28, 8, 9, 8, 8);
        DrawableSprite rawShapelessIcon = this.createGuiSprite("icons/shapeless_icon", 36, 36).trim(1, 2, 1, 1);
        this.shapelessIcon = new HighResolutionDrawable(rawShapelessIcon, 4);
        this.arrowPrevious = this.createGuiSprite("icons/arrow_previous", 9, 9).trim(0, 0, 1, 1);
        this.arrowNext = this.createGuiSprite("icons/arrow_next", 9, 9).trim(0, 0, 1, 1);
        this.recipeTransfer = this.createGuiSprite("icons/recipe_transfer", 7, 7);
        this.configButtonIcon = this.createGuiSprite("icons/config_button", 16, 16);
        this.configButtonCheatIcon = this.createGuiSprite("icons/config_button_cheat", 16, 16);
        this.bookmarkButtonDisabledIcon = this.createGuiSprite("icons/bookmark_button_disabled", 16, 16);
        this.bookmarkButtonEnabledIcon = this.createGuiSprite("icons/bookmark_button_enabled", 16, 16);
        this.infoIcon = this.createGuiSprite("icons/info", 16, 16);
        this.flameIcon = this.createGuiSprite("icons/flame", 14, 14);
    }

    private ResourceLocation createSprite(String name) {
        return new ResourceLocation("jei", name);
    }

    private DrawableSprite createGuiSprite(String name, int width, int height) {
        ResourceLocation location = this.createSprite(name);
        return new DrawableSprite(this.spriteUploader, location, width, height);
    }

    private DrawableNineSliceTexture createNineSliceGuiSprite(String name, int width, int height, int left, int right, int top, int bottom) {
        ResourceLocation location = this.createSprite(name);
        return new DrawableNineSliceTexture(this.spriteUploader, location, width, height, left, right, top, bottom);
    }

    public IDrawableStatic getSlotDrawable() {
        return this.slot;
    }

    public IDrawableStatic getTabSelected() {
        return this.tabSelected;
    }

    public IDrawableStatic getTabUnselected() {
        return this.tabUnselected;
    }

    public HighResolutionDrawable getShapelessIcon() {
        return this.shapelessIcon;
    }

    public IDrawableStatic getArrowPrevious() {
        return this.arrowPrevious;
    }

    public IDrawableStatic getArrowNext() {
        return this.arrowNext;
    }

    public IDrawableStatic getRecipeTransfer() {
        return this.recipeTransfer;
    }

    public IDrawableStatic getConfigButtonIcon() {
        return this.configButtonIcon;
    }

    public IDrawableStatic getConfigButtonCheatIcon() {
        return this.configButtonCheatIcon;
    }

    public IDrawableStatic getBookmarkButtonDisabledIcon() {
        return this.bookmarkButtonDisabledIcon;
    }

    public IDrawableStatic getBookmarkButtonEnabledIcon() {
        return this.bookmarkButtonEnabledIcon;
    }

    public DrawableNineSliceTexture getButtonForState(boolean enabled, boolean hovered) {
        if (!enabled) {
            return this.buttonDisabled;
        } else {
            return hovered ? this.buttonHighlight : this.buttonEnabled;
        }
    }

    public DrawableNineSliceTexture getRecipeGuiBackground() {
        return this.recipeGuiBackground;
    }

    public DrawableNineSliceTexture getIngredientListBackground() {
        return this.ingredientListBackground;
    }

    public DrawableNineSliceTexture getBookmarkListBackground() {
        return this.bookmarkListBackground;
    }

    public DrawableNineSliceTexture getRecipeBackground() {
        return this.recipeBackground;
    }

    public DrawableNineSliceTexture getSearchBackground() {
        return this.searchBackground;
    }

    public IDrawableStatic getInfoIcon() {
        return this.infoIcon;
    }

    public DrawableNineSliceTexture getCatalystTab() {
        return this.catalystTab;
    }

    public DrawableNineSliceTexture getRecipeCatalystSlotBackground() {
        return this.recipeCatalystSlotBackground;
    }

    public DrawableNineSliceTexture getIngredientListSlotBackground() {
        return this.ingredientListSlotBackground;
    }

    public DrawableNineSliceTexture getBookmarkListSlotBackground() {
        return this.bookmarkListSlotBackground;
    }

    public IDrawableStatic getFlameIcon() {
        return this.flameIcon;
    }

    public JeiSpriteUploader getSpriteUploader() {
        return this.spriteUploader;
    }
}