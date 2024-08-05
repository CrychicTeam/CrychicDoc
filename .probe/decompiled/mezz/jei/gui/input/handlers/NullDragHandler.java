package mezz.jei.gui.input.handlers;

import java.util.Optional;
import mezz.jei.gui.input.IDragHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.screens.Screen;

public class NullDragHandler implements IDragHandler {

    public static final NullDragHandler INSTANCE = new NullDragHandler();

    private NullDragHandler() {
    }

    @Override
    public Optional<IDragHandler> handleDragStart(Screen screen, UserInput input) {
        return Optional.empty();
    }

    @Override
    public boolean handleDragComplete(Screen screen, UserInput input) {
        return false;
    }
}