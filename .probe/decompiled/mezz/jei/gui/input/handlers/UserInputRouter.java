package mezz.jei.gui.input.handlers;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import mezz.jei.common.config.DebugConfig;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserInputRouter {

    private static final Logger LOGGER = LogManager.getLogger();

    private final CombinedInputHandler combinedInputHandler;

    private final Map<InputConstants.Key, IUserInputHandler> pending = new HashMap();

    public UserInputRouter(IUserInputHandler... inputHandlers) {
        this.combinedInputHandler = new CombinedInputHandler(inputHandlers);
    }

    public boolean handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
        if (DebugConfig.isDebugInputsEnabled()) {
            LOGGER.debug("Received user input: {}", input);
        }
        return switch(input.getClickState()) {
            case IMMEDIATE ->
                this.handleImmediateClick(screen, input, keyBindings);
            case SIMULATE ->
                this.handleSimulateClick(screen, input, keyBindings);
            case EXECUTE ->
                this.handleExecuteClick(screen, input, keyBindings);
        };
    }

    private boolean handleImmediateClick(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
        IUserInputHandler oldClick = (IUserInputHandler) this.pending.remove(input.getKey());
        if (oldClick != null && DebugConfig.isDebugInputsEnabled()) {
            LOGGER.debug("Canceled previous user input: {}", oldClick);
        }
        return (Boolean) this.combinedInputHandler.handleUserInput(screen, input, keyBindings).map(callback -> {
            if (DebugConfig.isDebugInputsEnabled()) {
                LOGGER.debug("Immediate click handled by: {}\n{}", callback, input);
            }
            return true;
        }).orElse(false);
    }

    private boolean handleSimulateClick(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
        IUserInputHandler oldClick = (IUserInputHandler) this.pending.remove(input.getKey());
        if (oldClick != null && DebugConfig.isDebugInputsEnabled()) {
            LOGGER.debug("Canceled pending user input: {}", oldClick);
        }
        return (Boolean) this.combinedInputHandler.handleUserInput(screen, input, keyBindings).map(callback -> {
            this.pending.put(input.getKey(), callback);
            if (DebugConfig.isDebugInputsEnabled()) {
                LOGGER.debug("Click successfully simulated by: {}\n{}", callback, input);
            }
            return true;
        }).orElse(false);
    }

    private boolean handleExecuteClick(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
        return (Boolean) Optional.ofNullable((IUserInputHandler) this.pending.remove(input.getKey())).flatMap(inputHandler -> inputHandler.handleUserInput(screen, input, keyBindings)).map(callback -> {
            if (DebugConfig.isDebugInputsEnabled()) {
                LOGGER.debug("Click successfully executed by: {}\n{}", callback, input);
            }
            return true;
        }).orElse(false);
    }

    public void handleGuiChange() {
        if (DebugConfig.isDebugInputsEnabled()) {
            LOGGER.debug("The GUI has changed, clearing all pending clicks");
        }
        for (InputConstants.Key key : this.pending.keySet()) {
            this.combinedInputHandler.handleMouseClickedOut(key);
        }
        this.pending.clear();
    }

    public boolean handleMouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        return (Boolean) this.combinedInputHandler.handleMouseScrolled(mouseX, mouseY, scrollDelta).map(callback -> {
            if (DebugConfig.isDebugInputsEnabled()) {
                LOGGER.debug("Scroll handled by: {}", callback);
            }
            return true;
        }).orElse(false);
    }
}