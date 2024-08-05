package mezz.jei.gui.input.handlers;

import java.util.Optional;
import mezz.jei.common.Internal;
import mezz.jei.common.config.IClientToggleState;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.common.network.packets.PacketRequestCheatPermission;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.screens.Screen;

public class GlobalInputHandler implements IUserInputHandler {

    private final IClientToggleState toggleState;

    public GlobalInputHandler(IClientToggleState toggleState) {
        this.toggleState = toggleState;
    }

    @Override
    public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
        if (input.is(keyBindings.getToggleOverlay())) {
            if (!input.isSimulate()) {
                this.toggleState.toggleOverlayEnabled();
            }
            return Optional.of(this);
        } else if (input.is(keyBindings.getToggleBookmarkOverlay())) {
            if (!input.isSimulate()) {
                this.toggleState.toggleBookmarkEnabled();
            }
            return Optional.of(this);
        } else if (input.is(keyBindings.getToggleCheatMode())) {
            if (!input.isSimulate()) {
                this.toggleState.toggleCheatItemsEnabled();
                if (this.toggleState.isCheatItemsEnabled()) {
                    IConnectionToServer serverConnection = Internal.getServerConnection();
                    serverConnection.sendPacketToServer(new PacketRequestCheatPermission());
                }
            }
            return Optional.of(this);
        } else if (input.is(keyBindings.getToggleEditMode())) {
            if (!input.isSimulate()) {
                this.toggleState.toggleEditModeEnabled();
            }
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }
}