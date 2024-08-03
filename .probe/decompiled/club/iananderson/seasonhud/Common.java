package club.iananderson.seasonhud;

import club.iananderson.seasonhud.client.SeasonHUDClient;
import club.iananderson.seasonhud.platform.Services;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Common {

    public static final String MOD_ID = "seasonhud";

    public static final String MOD_NAME = "SeasonHUD";

    public static final Logger LOG = LoggerFactory.getLogger("SeasonHUD");

    public static final ResourceLocation SEASON_ICONS = new ResourceLocation("seasonhud", "season_icons");

    public static final Style SEASON_STYLE = Style.EMPTY.withFont(SEASON_ICONS);

    private static boolean curiosLoaded;

    private static boolean extrasLoaded;

    private static String platformName;

    public static void init() {
        curiosLoaded = Services.PLATFORM.isModLoaded("trinkets") || Services.PLATFORM.isModLoaded("curios");
        extrasLoaded = Services.PLATFORM.isModLoaded("seasonsextras");
        platformName = Services.PLATFORM.getPlatformName();
    }

    public static boolean extrasLoaded() {
        return extrasLoaded;
    }

    public static boolean curiosLoaded() {
        return curiosLoaded;
    }

    public static String platformName() {
        return platformName;
    }

    public static boolean vanillaShouldDrawHud() {
        return (SeasonHUDClient.mc.screen == null || SeasonHUDClient.mc.screen instanceof ChatScreen || SeasonHUDClient.mc.screen instanceof DeathScreen) && !SeasonHUDClient.mc.isPaused() && !SeasonHUDClient.mc.options.renderDebug && !SeasonHUDClient.mc.options.hideGui;
    }
}