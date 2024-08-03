package mezz.jei.gui.input.handlers;

import java.util.Optional;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IEditModeConfig;
import mezz.jei.common.config.IClientToggleState;
import mezz.jei.common.input.IClickableIngredientInternal;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.input.CombinedRecipeFocusSource;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.screens.Screen;

public class EditInputHandler implements IUserInputHandler {

    private final CombinedRecipeFocusSource focusSource;

    private final IClientToggleState toggleState;

    private final IEditModeConfig editModeConfig;

    public EditInputHandler(CombinedRecipeFocusSource focusSource, IClientToggleState toggleState, IEditModeConfig editModeConfig) {
        this.focusSource = focusSource;
        this.toggleState = toggleState;
        this.editModeConfig = editModeConfig;
    }

    @Override
    public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
        if (!this.toggleState.isEditModeEnabled()) {
            return Optional.empty();
        } else if (input.is(keyBindings.getToggleHideIngredient())) {
            return this.handle(input, keyBindings, IEditModeConfig.HideMode.SINGLE);
        } else {
            return input.is(keyBindings.getToggleWildcardHideIngredient()) ? this.handle(input, keyBindings, IEditModeConfig.HideMode.WILDCARD) : Optional.empty();
        }
    }

    private Optional<IUserInputHandler> handle(UserInput input, IInternalKeyMappings keyBindings, IEditModeConfig.HideMode hideMode) {
        return this.focusSource.getIngredientUnderMouse(input, keyBindings).findFirst().map(clicked -> {
            if (!input.isSimulate()) {
                this.execute(clicked, hideMode);
            }
            ImmutableRect2i area = clicked.getArea();
            return LimitedAreaInputHandler.create(this, area);
        });
    }

    private <V> void execute(IClickableIngredientInternal<V> clicked, IEditModeConfig.HideMode hideMode) {
        ITypedIngredient<V> typedIngredient = clicked.getTypedIngredient();
        if (this.editModeConfig.isIngredientHiddenUsingConfigFile(typedIngredient)) {
            this.editModeConfig.showIngredientUsingConfigFile(typedIngredient, hideMode);
        } else {
            this.editModeConfig.hideIngredientUsingConfigFile(typedIngredient, hideMode);
        }
    }
}