package icyllis.modernui.mc;

import icyllis.modernui.ModernUI;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;

public abstract class ModernUIMod {

    public static final String BOOTSTRAP_DISABLE_TEXT_ENGINE = "modernui_mc_disableTextEngine";

    public static final String BOOTSTRAP_DISABLE_SMOOTH_SCROLLING = "modernui_mc_disableSmoothScrolling";

    public static final String BOOTSTRAP_DISABLE_ENHANCED_TEXT_FIELD = "modernui_mc_disableEnhancedTextField";

    public static volatile boolean sDevelopment;

    public static volatile boolean sDeveloperMode;

    protected static boolean sOptiFineLoaded;

    protected static boolean sIrisApiLoaded;

    protected static volatile boolean sLegendaryTooltipsLoaded;

    protected static volatile boolean sUntranslatedItemsLoaded;

    private static volatile Boolean sTextEngineEnabled;

    @Nonnull
    public static ResourceLocation location(String path) {
        return new ResourceLocation("modernui", path);
    }

    public static boolean isOptiFineLoaded() {
        return sOptiFineLoaded;
    }

    public static boolean isIrisApiLoaded() {
        return sIrisApiLoaded;
    }

    public static boolean isLegendaryTooltipsLoaded() {
        return sLegendaryTooltipsLoaded;
    }

    public static boolean isUntranslatedItemsLoaded() {
        return sUntranslatedItemsLoaded;
    }

    public static boolean isDeveloperMode() {
        return sDeveloperMode || sDevelopment;
    }

    public static String getBootstrapProperty(String key) {
        return ModernUIClient.getBootstrapProperty(key);
    }

    public static boolean isTextEngineEnabled() {
        if (sTextEngineEnabled == null) {
            synchronized (ModernUIMod.class) {
                if (sTextEngineEnabled == null) {
                    sTextEngineEnabled = !Boolean.parseBoolean(getBootstrapProperty("modernui_mc_disableTextEngine"));
                }
            }
        }
        return sTextEngineEnabled;
    }

    static {
        try {
            Class<?> clazz = Class.forName("optifine.Installer");
            sOptiFineLoaded = true;
            try {
                String version = (String) clazz.getMethod("getOptiFineVersion").invoke(null);
                ModernUI.LOGGER.info(ModernUI.MARKER, "OptiFine installed: {}", version);
            } catch (Exception var3) {
                ModernUI.LOGGER.info(ModernUI.MARKER, "OptiFine installed...");
            }
        } catch (Exception var4) {
        }
        try {
            Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            sIrisApiLoaded = true;
            ModernUI.LOGGER.info(ModernUI.MARKER, "Iris API installed...");
        } catch (Exception var2) {
        }
    }
}