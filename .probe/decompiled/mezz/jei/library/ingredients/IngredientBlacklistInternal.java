package mezz.jei.library.ingredients;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.core.util.WeakList;

public class IngredientBlacklistInternal implements IIngredientManager.IIngredientListener {

    private final Set<String> ingredientBlacklist = new HashSet();

    private final WeakList<IngredientBlacklistInternal.IListener> listeners = new WeakList<>();

    public <V> void addIngredientToBlacklist(ITypedIngredient<V> typedIngredient, IIngredientHelper<V> ingredientHelper) {
        V ingredient = typedIngredient.getIngredient();
        String uniqueName = ingredientHelper.getUniqueId(ingredient, UidContext.Ingredient);
        if (this.ingredientBlacklist.add(uniqueName)) {
            this.notifyListenersOfVisibilityChange(typedIngredient, false);
        }
    }

    public <V> void removeIngredientFromBlacklist(ITypedIngredient<V> typedIngredient, IIngredientHelper<V> ingredientHelper) {
        V ingredient = typedIngredient.getIngredient();
        String uniqueName = ingredientHelper.getUniqueId(ingredient, UidContext.Ingredient);
        if (this.ingredientBlacklist.remove(uniqueName)) {
            this.notifyListenersOfVisibilityChange(typedIngredient, true);
        }
    }

    public <V> boolean isIngredientBlacklistedByApi(ITypedIngredient<V> typedIngredient, IIngredientHelper<V> ingredientHelper) {
        V ingredient = typedIngredient.getIngredient();
        String uid = ingredientHelper.getUniqueId(ingredient, UidContext.Ingredient);
        String uidWild = ingredientHelper.getWildcardId(ingredient);
        return uid.equals(uidWild) ? this.ingredientBlacklist.contains(uid) : this.ingredientBlacklist.contains(uid) || this.ingredientBlacklist.contains(uidWild);
    }

    public void registerListener(IngredientBlacklistInternal.IListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public <V> void onIngredientsAdded(IIngredientHelper<V> ingredientHelper, Collection<ITypedIngredient<V>> ingredients) {
        for (ITypedIngredient<V> ingredient : ingredients) {
            this.removeIngredientFromBlacklist(ingredient, ingredientHelper);
        }
    }

    @Override
    public <V> void onIngredientsRemoved(IIngredientHelper<V> ingredientHelper, Collection<ITypedIngredient<V>> ingredients) {
        for (ITypedIngredient<V> ingredient : ingredients) {
            this.addIngredientToBlacklist(ingredient, ingredientHelper);
        }
    }

    private <T> void notifyListenersOfVisibilityChange(ITypedIngredient<T> ingredient, boolean visible) {
        this.listeners.forEach(listener -> listener.onIngredientVisibilityChanged(ingredient, visible));
    }

    public interface IListener {

        <V> void onIngredientVisibilityChanged(ITypedIngredient<V> var1, boolean var2);
    }
}