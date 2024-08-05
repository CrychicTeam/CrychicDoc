package mezz.jei.gui.input;

import java.util.stream.Stream;
import mezz.jei.common.input.IClickableIngredientInternal;

public interface IRecipeFocusSource {

    Stream<IClickableIngredientInternal<?>> getIngredientUnderMouse(double var1, double var3);
}