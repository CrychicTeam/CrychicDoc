package snownee.loquat.client;

import snownee.kiwi.config.KiwiConfig;

@KiwiConfig(type = KiwiConfig.ConfigType.CLIENT)
public class LoquatClientConfig {

    @KiwiConfig.Path("restrict.notification")
    public static boolean restrictNotification = true;
}