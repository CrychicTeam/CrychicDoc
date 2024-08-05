package mezz.jei.api.runtime;

import java.util.Set;
import mezz.jei.api.ingredients.ITypedIngredient;

public interface IEditModeConfig {

    <V> boolean isIngredientHiddenUsingConfigFile(ITypedIngredient<V> var1);

    <V> Set<IEditModeConfig.HideMode> getIngredientHiddenUsingConfigFile(ITypedIngredient<V> var1);

    <V> void hideIngredientUsingConfigFile(ITypedIngredient<V> var1, IEditModeConfig.HideMode var2);

    <V> void showIngredientUsingConfigFile(ITypedIngredient<V> var1, IEditModeConfig.HideMode var2);

    public static enum HideMode {

        SINGLE, WILDCARD
    }
}