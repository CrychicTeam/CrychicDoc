package mezz.jei.gui.input.handlers;

import java.util.List;
import java.util.Optional;
import mezz.jei.gui.input.IDragHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public class DragRouter {

    private final List<IDragHandler> handlers;

    @Nullable
    private IDragHandler dragStartedCallback;

    public DragRouter(IDragHandler... handlers) {
        this.handlers = List.of(handlers);
    }

    public void handleGuiChange() {
        this.cancelDrag();
    }

    public boolean startDrag(Screen screen, UserInput input) {
        this.cancelDrag();
        this.dragStartedCallback = (IDragHandler) this.handlers.stream().map(i -> i.handleDragStart(screen, input)).flatMap(Optional::stream).findFirst().orElse(null);
        return this.dragStartedCallback != null;
    }

    public boolean completeDrag(Screen screen, UserInput input) {
        if (this.dragStartedCallback == null) {
            return false;
        } else {
            boolean result = this.dragStartedCallback.handleDragComplete(screen, input);
            this.dragStartedCallback = null;
            return result;
        }
    }

    public void cancelDrag() {
        if (this.dragStartedCallback != null) {
            this.dragStartedCallback.handleDragCanceled();
            this.dragStartedCallback = null;
        }
    }
}