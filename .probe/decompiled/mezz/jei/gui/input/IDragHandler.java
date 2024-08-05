package mezz.jei.gui.input;

import java.util.Optional;
import net.minecraft.client.gui.screens.Screen;

public interface IDragHandler {

    Optional<IDragHandler> handleDragStart(Screen var1, UserInput var2);

    boolean handleDragComplete(Screen var1, UserInput var2);

    default void handleDragCanceled() {
    }
}