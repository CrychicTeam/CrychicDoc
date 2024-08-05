package eu.midnightdust.core.config;

import eu.midnightdust.lib.config.MidnightConfig;
import eu.midnightdust.lib.util.PlatformFunctions;

public class MidnightLibConfig extends MidnightConfig {

    @MidnightConfig.Entry
    public static MidnightLibConfig.ConfigButton config_screen_list = PlatformFunctions.isModLoaded("modmenu") ? MidnightLibConfig.ConfigButton.MODMENU : MidnightLibConfig.ConfigButton.TRUE;

    public static enum ConfigButton {

        TRUE, FALSE, MODMENU
    }
}