package net.mehvahdjukaar.supplementaries.integration.forge;

import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.CreateCompat;
import net.mehvahdjukaar.supplementaries.integration.FlywheelCompat;
import net.mehvahdjukaar.supplementaries.integration.QuarkClientCompat;
import net.mehvahdjukaar.supplementaries.integration.forge.configured.ModConfigSelectScreen;

public class CompatHandlerClientImpl {

    public static void doSetup() {
        if (CompatHandler.CONFIGURED && (Boolean) ClientConfigs.General.CUSTOM_CONFIGURED_SCREEN.get()) {
            ModConfigSelectScreen.registerConfigScreen("supplementaries", ModConfigSelectScreen::new);
        }
        if (CompatHandler.QUARK) {
            QuarkClientCompat.setupClient();
        }
        if (CompatHandler.CREATE) {
            CreateCompat.setupClient();
        }
        if (CompatHandler.FLYWHEEL) {
            FlywheelCompat.setupClient();
        }
    }

    public static void init() {
        if (CompatHandler.QUARK) {
            QuarkClientCompat.initClient();
        }
    }
}