package mezz.jei.api.recipe.vanilla;

import java.util.List;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.network.chat.FormattedText;
import org.jetbrains.annotations.Unmodifiable;

public interface IJeiIngredientInfoRecipe {

    @Unmodifiable
    List<ITypedIngredient<?>> getIngredients();

    @Unmodifiable
    List<FormattedText> getDescription();
}