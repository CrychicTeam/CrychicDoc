package mezz.jei.gui.input.handlers;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;
import java.util.Optional;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.screens.Screen;

public class CombinedInputHandler implements IUserInputHandler {

    private final List<IUserInputHandler> inputHandlers;

    public CombinedInputHandler(IUserInputHandler... inputHandlers) {
        this.inputHandlers = List.of(inputHandlers);
    }

    public CombinedInputHandler(List<IUserInputHandler> inputHandlers) {
        this.inputHandlers = List.copyOf(inputHandlers);
    }

    @Override
    public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
        return switch(input.getClickState()) {
            case IMMEDIATE, SIMULATE ->
                this.handleClickInternal(screen, input, keyBindings);
            case EXECUTE ->
                Optional.empty();
        };
    }

    private Optional<IUserInputHandler> handleClickInternal(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
        Optional<IUserInputHandler> firstHandled = Optional.empty();
        for (IUserInputHandler inputHandler : this.inputHandlers) {
            if (firstHandled.isEmpty()) {
                firstHandled = inputHandler.handleUserInput(screen, input, keyBindings);
                if (firstHandled.isEmpty()) {
                    inputHandler.handleMouseClickedOut(input.getKey());
                }
            } else {
                inputHandler.handleMouseClickedOut(input.getKey());
            }
        }
        return firstHandled;
    }

    @Override
    public void handleMouseClickedOut(InputConstants.Key key) {
        for (IUserInputHandler inputHandler : this.inputHandlers) {
            inputHandler.handleMouseClickedOut(key);
        }
    }

    @Override
    public Optional<IUserInputHandler> handleMouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        return this.inputHandlers.stream().flatMap(inputHandler -> inputHandler.handleMouseScrolled(mouseX, mouseY, scrollDelta).stream()).findFirst();
    }
}