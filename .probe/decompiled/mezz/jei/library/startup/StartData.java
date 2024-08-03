package mezz.jei.library.startup;

import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.common.network.IConnectionToServer;

public record StartData(List<IModPlugin> plugins, IConnectionToServer serverConnection, IInternalKeyMappings keyBindings) {
}