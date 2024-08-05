package software.bernie.geckolib;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.network.GeckoLibNetwork;

public class GeckoLib {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "geckolib";

    public static volatile boolean hasInitialized;

    public static synchronized void initialize() {
        if (!hasInitialized) {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> GeckoLibCache::registerReloadListener);
            GeckoLibNetwork.init();
        }
        hasInitialized = true;
    }

    public static synchronized void shadowInit() {
        if (!hasInitialized) {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> GeckoLibCache::registerReloadListener);
        }
        hasInitialized = true;
    }
}