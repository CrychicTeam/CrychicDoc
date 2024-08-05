package snownee.jade.addon;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import snownee.jade.gui.PluginsConfigScreen;

public class JadeAddonsClient {

    public static void init() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new PluginsConfigScreen(screen)));
    }
}