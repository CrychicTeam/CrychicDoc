package mezz.jei.gui.input;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.Optional;
import mezz.jei.common.input.IInternalKeyMappings;
import net.minecraft.client.gui.screens.Screen;

public interface IUserInputHandler {

    Optional<IUserInputHandler> handleUserInput(Screen var1, UserInput var2, IInternalKeyMappings var3);

    default void handleMouseClickedOut(InputConstants.Key key) {
    }

    default Optional<IUserInputHandler> handleMouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        return Optional.empty();
    }
}