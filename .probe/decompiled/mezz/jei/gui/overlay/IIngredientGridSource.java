package mezz.jei.gui.overlay;

import java.util.List;
import mezz.jei.api.ingredients.ITypedIngredient;
import org.jetbrains.annotations.Unmodifiable;

public interface IIngredientGridSource {

    @Unmodifiable
    List<ITypedIngredient<?>> getIngredientList();

    void addSourceListChangedListener(IIngredientGridSource.SourceListChangedListener var1);

    public interface SourceListChangedListener {

        void onSourceListChanged();
    }
}