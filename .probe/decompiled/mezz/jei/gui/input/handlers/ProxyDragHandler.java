package mezz.jei.gui.input.handlers;

import java.util.Optional;
import java.util.function.Supplier;
import mezz.jei.gui.input.IDragHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.screens.Screen;

public class ProxyDragHandler implements IDragHandler {

    private final Supplier<IDragHandler> source;

    public ProxyDragHandler(Supplier<IDragHandler> source) {
        this.source = source;
    }

    @Override
    public Optional<IDragHandler> handleDragStart(Screen screen, UserInput input) {
        return ((IDragHandler) this.source.get()).handleDragStart(screen, input);
    }

    @Override
    public boolean handleDragComplete(Screen screen, UserInput input) {
        return ((IDragHandler) this.source.get()).handleDragComplete(screen, input);
    }

    @Override
    public void handleDragCanceled() {
        ((IDragHandler) this.source.get()).handleDragCanceled();
    }
}