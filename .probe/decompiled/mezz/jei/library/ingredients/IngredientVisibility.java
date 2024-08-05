package mezz.jei.library.ingredients;

import java.util.stream.Stream;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IEditModeConfig;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.common.Constants;
import mezz.jei.common.config.IClientToggleState;
import mezz.jei.core.util.WeakList;
import mezz.jei.library.config.EditModeConfig;
import net.minecraft.resources.ResourceLocation;

public class IngredientVisibility implements IIngredientVisibility {

    private final IngredientBlacklistInternal blacklist;

    private final IClientToggleState toggleState;

    private final IEditModeConfig editModeConfig;

    private final IIngredientManager ingredientManager;

    private final WeakList<IIngredientVisibility.IListener> listeners = new WeakList<>();

    public IngredientVisibility(IngredientBlacklistInternal blacklist, IClientToggleState toggleState, EditModeConfig editModeConfig, IIngredientManager ingredientManager) {
        this.blacklist = blacklist;
        this.toggleState = toggleState;
        this.editModeConfig = editModeConfig;
        this.ingredientManager = ingredientManager;
        blacklist.registerListener(this::notifyListenersOfVisibilityChange);
        editModeConfig.registerListener(this::notifyListenersOfVisibilityChange);
    }

    @Override
    public <V> boolean isIngredientVisible(ITypedIngredient<V> typedIngredient) {
        IIngredientType<V> ingredientType = typedIngredient.getType();
        IIngredientHelper<V> ingredientHelper = this.ingredientManager.getIngredientHelper(ingredientType);
        return this.isIngredientVisible(typedIngredient, ingredientHelper);
    }

    @Override
    public <V> boolean isIngredientVisible(IIngredientType<V> ingredientType, V ingredient) {
        IIngredientHelper<V> ingredientHelper = this.ingredientManager.getIngredientHelper(ingredientType);
        return (Boolean) TypedIngredient.createAndFilterInvalid(this.ingredientManager, ingredientType, ingredient).map(i -> this.isIngredientVisible(i, ingredientHelper)).orElse(false);
    }

    public <V> boolean isIngredientVisible(ITypedIngredient<V> typedIngredient, IIngredientHelper<V> ingredientHelper) {
        if (this.blacklist.isIngredientBlacklistedByApi(typedIngredient, ingredientHelper)) {
            return false;
        } else if (!ingredientHelper.isIngredientOnServer(typedIngredient.getIngredient())) {
            return false;
        } else {
            Stream<ResourceLocation> tags = ingredientHelper.getTagStream(typedIngredient.getIngredient());
            return tags.anyMatch(Constants.HIDDEN_INGREDIENT_TAG::equals) ? false : this.toggleState.isEditModeEnabled() || !this.editModeConfig.isIngredientHiddenUsingConfigFile(typedIngredient);
        }
    }

    @Override
    public void registerListener(IIngredientVisibility.IListener listener) {
        this.listeners.add(listener);
    }

    private <T> void notifyListenersOfVisibilityChange(ITypedIngredient<T> ingredient, boolean visible) {
        this.listeners.forEach(listener -> listener.onIngredientVisibilityChanged(ingredient, visible));
    }
}