package mezz.jei.gui.input.handlers;

import java.util.Optional;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.runtime.IRecipesGui;
import mezz.jei.api.runtime.IScreenHelper;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.common.platform.IPlatformScreenHelper;
import mezz.jei.common.platform.Services;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class GuiAreaInputHandler implements IUserInputHandler {

    private final IFocusFactory focusFactory;

    private final IScreenHelper screenHelper;

    private final IRecipesGui recipesGui;

    public GuiAreaInputHandler(IScreenHelper screenHelper, IRecipesGui recipesGui, IFocusFactory focusFactory) {
        this.focusFactory = focusFactory;
        this.screenHelper = screenHelper;
        this.recipesGui = recipesGui;
    }

    @Override
    public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
        if (input.is(keyBindings.getLeftClick()) && screen instanceof AbstractContainerScreen<?> guiContainer) {
            IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
            int guiLeft = screenHelper.getGuiLeft(guiContainer);
            int guiTop = screenHelper.getGuiTop(guiContainer);
            double guiMouseX = input.getMouseX() - (double) guiLeft;
            double guiMouseY = input.getMouseY() - (double) guiTop;
            return this.screenHelper.getGuiClickableArea(guiContainer, guiMouseX, guiMouseY).findFirst().map(clickableArea -> {
                if (!input.isSimulate()) {
                    clickableArea.onClick(this.focusFactory, this.recipesGui);
                }
                ImmutableRect2i screenArea = new ImmutableRect2i(clickableArea.getArea()).addOffset(guiLeft, guiTop);
                return LimitedAreaInputHandler.create(this, screenArea);
            });
        } else {
            return Optional.empty();
        }
    }
}