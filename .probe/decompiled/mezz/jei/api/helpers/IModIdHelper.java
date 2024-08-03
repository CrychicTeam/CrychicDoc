package mezz.jei.api.helpers;

import java.util.List;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.network.chat.Component;

public interface IModIdHelper {

    String getModNameForModId(String var1);

    boolean isDisplayingModNameEnabled();

    String getFormattedModNameForModId(String var1);

    <T> List<Component> addModNameToIngredientTooltip(List<Component> var1, T var2, IIngredientHelper<T> var3);

    <T> List<Component> addModNameToIngredientTooltip(List<Component> var1, ITypedIngredient<T> var2);
}