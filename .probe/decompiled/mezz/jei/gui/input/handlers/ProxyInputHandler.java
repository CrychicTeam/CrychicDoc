package mezz.jei.gui.input.handlers;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.Optional;
import java.util.function.Supplier;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.screens.Screen;

public class ProxyInputHandler implements IUserInputHandler {

    private final Supplier<IUserInputHandler> source;

    public ProxyInputHandler(Supplier<IUserInputHandler> source) {
        this.source = source;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("source", this.source.get()).toString();
    }

    @Override
    public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
        return ((IUserInputHandler) this.source.get()).handleUserInput(screen, input, keyBindings);
    }

    @Override
    public void handleMouseClickedOut(InputConstants.Key input) {
        ((IUserInputHandler) this.source.get()).handleMouseClickedOut(input);
    }

    @Override
    public Optional<IUserInputHandler> handleMouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        return ((IUserInputHandler) this.source.get()).handleMouseScrolled(mouseX, mouseY, scrollDelta);
    }
}