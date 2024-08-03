package mezz.jei.gui.input.handlers;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.Optional;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.core.util.TextHistory;
import mezz.jei.gui.input.GuiTextFieldFilter;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.screens.Screen;

public class TextFieldInputHandler implements IUserInputHandler {

    private final GuiTextFieldFilter textFieldFilter;

    public TextFieldInputHandler(GuiTextFieldFilter textFieldFilter) {
        this.textFieldFilter = textFieldFilter;
    }

    @Override
    public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
        return this.handleUserInputBoolean(input, keyBindings) ? Optional.of(this) : Optional.empty();
    }

    private boolean handleUserInputBoolean(UserInput input, IInternalKeyMappings keyBindings) {
        if (input.is(keyBindings.getEnterKey()) || input.is(keyBindings.getEscapeKey())) {
            return this.handleSetFocused(input, false);
        } else if (input.is(keyBindings.getFocusSearch())) {
            return this.handleSetFocused(input, true);
        } else if (input.is(keyBindings.getHoveredClearSearchBar()) && this.textFieldFilter.isMouseOver(input.getMouseX(), input.getMouseY())) {
            return this.handleHoveredClearSearchBar(input);
        } else if (input.callVanilla(this.textFieldFilter::m_5953_, this.textFieldFilter::m_6375_, this.textFieldFilter::m_7933_)) {
            this.handleSetFocused(input, true);
            return true;
        } else if (input.is(keyBindings.getPreviousSearch())) {
            return this.handleNavigateHistory(input, TextHistory.Direction.PREVIOUS);
        } else {
            return input.is(keyBindings.getNextSearch()) ? this.handleNavigateHistory(input, TextHistory.Direction.NEXT) : this.textFieldFilter.m_94204_() && input.isAllowedChatCharacter();
        }
    }

    private boolean handleSetFocused(UserInput input, boolean focused) {
        if (this.textFieldFilter.m_93696_() != focused) {
            if (!input.isSimulate()) {
                this.textFieldFilter.setFocused(focused);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean handleHoveredClearSearchBar(UserInput input) {
        if (!input.isSimulate()) {
            this.textFieldFilter.setValue("");
            this.textFieldFilter.setFocused(true);
        }
        return true;
    }

    private boolean handleNavigateHistory(UserInput input, TextHistory.Direction direction) {
        return this.textFieldFilter.m_93696_() ? (Boolean) this.textFieldFilter.getHistory(direction).map(newText -> {
            if (!input.isSimulate()) {
                this.textFieldFilter.setValue(newText);
            }
            return true;
        }).orElse(false) : false;
    }

    @Override
    public void handleMouseClickedOut(InputConstants.Key input) {
        this.textFieldFilter.setFocused(false);
    }
}