package snownee.jade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.impl.WailaCommonRegistration;
import snownee.jade.impl.config.PluginConfig;
import snownee.jade.impl.config.WailaConfig;
import snownee.jade.test.ExamplePlugin;
import snownee.jade.util.CommonProxy;
import snownee.jade.util.JsonConfig;

public class Jade {

    public static final String MODID = "jade";

    public static final Logger LOGGER = LogManager.getLogger("Jade");

    public static final JsonConfig<WailaConfig> CONFIG = new JsonConfig<>("jade/jade", WailaConfig.CODEC, null);

    public static int MAX_DISTANCE_SQR = 900;

    public static boolean FROZEN;

    public static void loadComplete() {
        if (!FROZEN) {
            FROZEN = true;
            if (CommonProxy.isDevEnv()) {
                LOGGER.info("Dev environment detected, loading test plugin");
                try {
                    IWailaPlugin plugin = new ExamplePlugin();
                    plugin.register(WailaCommonRegistration.INSTANCE);
                    if (CommonProxy.isPhysicallyClient()) {
                        plugin.registerClient(WailaClientRegistration.INSTANCE);
                    }
                } catch (Throwable var1) {
                }
            }
            WailaCommonRegistration.INSTANCE.priorities.sort(PluginConfig.INSTANCE.getKeys());
            WailaCommonRegistration.INSTANCE.loadComplete();
            if (CommonProxy.isPhysicallyClient()) {
                WailaClientRegistration.INSTANCE.loadComplete();
                WailaConfig.ConfigGeneral.init();
            }
            PluginConfig.INSTANCE.reload();
        }
    }
}