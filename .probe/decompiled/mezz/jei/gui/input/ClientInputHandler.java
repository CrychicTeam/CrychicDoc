package mezz.jei.gui.input;

import java.util.List;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.core.util.ReflectionUtil;
import mezz.jei.gui.input.handlers.DragRouter;
import mezz.jei.gui.input.handlers.UserInputRouter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;

public class ClientInputHandler {

    private final List<ICharTypedHandler> charTypedHandlers;

    private final UserInputRouter inputRouter;

    private final DragRouter dragRouter;

    private final IInternalKeyMappings keybindings;

    private final ReflectionUtil reflectionUtil = new ReflectionUtil();

    public ClientInputHandler(List<ICharTypedHandler> charTypedHandlers, UserInputRouter inputRouter, DragRouter dragRouter, IInternalKeyMappings keybindings) {
        this.charTypedHandlers = charTypedHandlers;
        this.inputRouter = inputRouter;
        this.dragRouter = dragRouter;
        this.keybindings = keybindings;
    }

    public void onInitGui() {
        this.inputRouter.handleGuiChange();
        this.dragRouter.handleGuiChange();
    }

    public boolean onKeyboardKeyPressedPre(Screen screen, UserInput input) {
        return !this.isContainerTextFieldFocused(screen) ? this.inputRouter.handleUserInput(screen, input, this.keybindings) : false;
    }

    public boolean onKeyboardKeyPressedPost(Screen screen, UserInput input) {
        return this.isContainerTextFieldFocused(screen) ? this.inputRouter.handleUserInput(screen, input, this.keybindings) : false;
    }

    public boolean onKeyboardCharTypedPre(Screen screen, char codePoint, int modifiers) {
        return !this.isContainerTextFieldFocused(screen) ? this.handleCharTyped(codePoint, modifiers) : false;
    }

    public void onKeyboardCharTypedPost(Screen screen, char codePoint, int modifiers) {
        if (this.isContainerTextFieldFocused(screen)) {
            this.handleCharTyped(codePoint, modifiers);
        }
    }

    public boolean onGuiMouseClicked(Screen screen, UserInput input) {
        boolean handled = this.inputRouter.handleUserInput(screen, input, this.keybindings);
        if (Minecraft.getInstance().screen == screen && input.is(this.keybindings.getLeftClick())) {
            handled |= this.dragRouter.startDrag(screen, input);
        }
        return handled;
    }

    public boolean onGuiMouseReleased(Screen screen, UserInput input) {
        boolean handled = this.inputRouter.handleUserInput(screen, input, this.keybindings);
        if (input.is(this.keybindings.getLeftClick())) {
            handled |= this.dragRouter.completeDrag(screen, input);
        }
        return handled;
    }

    public boolean onGuiMouseScroll(double mouseX, double mouseY, double scrollDelta) {
        return this.inputRouter.handleMouseScrolled(mouseX, mouseY, scrollDelta);
    }

    private boolean handleCharTyped(char codePoint, int modifiers) {
        return this.charTypedHandlers.stream().filter(ICharTypedHandler::hasKeyboardFocus).anyMatch(handler -> handler.onCharTyped(codePoint, modifiers));
    }

    private boolean isContainerTextFieldFocused(Screen screen) {
        return this.reflectionUtil.getFieldWithClass(screen, EditBox.class).anyMatch(textField -> textField.m_142518_() && textField.m_93696_());
    }
}