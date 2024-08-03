package lio.playeranimatorapi;

import com.mojang.logging.LogUtils;
import lio.liosmultiloaderutils.MultiloaderUtils;
import lio.liosmultiloaderutils.utils.Platform;
import lio.playeranimatorapi.liolib.ModGeckoLibUtils;
import lio.playeranimatorapi.misc.ModEntityDataSerializers;
import org.slf4j.Logger;

public class ModInit {

    public static final String MOD_ID = "liosplayeranimatorapi";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static boolean liosplayeranimationapiloaded = false;

    private static void isLiosPlayerAnimatorApiLoaded() {
        liosplayeranimationapiloaded = true;
    }

    public static void init() {
        ModEntityDataSerializers.init();
        MultiloaderUtils.forceClientToHaveMod("liosplayeranimatorapi", Platform.getModVersion("liosplayeranimatorapi"));
        if (Platform.isModLoaded("liolib", "net.liopyu.liolib.LioLib")) {
            ModGeckoLibUtils.init();
            isLiosPlayerAnimatorApiLoaded();
        }
    }
}