package mezz.jei.api.gui.handlers;

import java.util.List;
import java.util.function.Consumer;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;

public interface IGhostIngredientHandler<T extends Screen> {

    <I> List<IGhostIngredientHandler.Target<I>> getTargetsTyped(T var1, ITypedIngredient<I> var2, boolean var3);

    void onComplete();

    default boolean shouldHighlightTargets() {
        return true;
    }

    public interface Target<I> extends Consumer<I> {

        Rect2i getArea();

        void accept(I var1);
    }
}