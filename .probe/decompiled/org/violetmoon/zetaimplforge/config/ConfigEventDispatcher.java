package org.violetmoon.zetaimplforge.config;

import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.util.zetalist.ZetaList;

public class ConfigEventDispatcher {

    public static void configChanged(ModConfigEvent event) {
        for (Zeta z : ZetaList.INSTANCE.getZetas()) {
            String modid = z.modid;
            if (event.getConfig().getModId().equals(modid) && z.configInternals != null && System.currentTimeMillis() - z.configInternals.debounceTime() > 20L) {
                handleConfigChange(z);
            }
        }
    }

    public static void dispatchAllInitialLoads() {
        for (Zeta z : ZetaList.INSTANCE.getZetas()) {
            handleConfigChange(z);
        }
    }

    private static void handleConfigChange(Zeta z) {
        z.configManager.onReload();
        z.loadBus.fire(new ZConfigChanged());
    }
}