package journeymap.client.ui.dialog;

import java.net.URLEncoder;
import journeymap.client.JourneymapClient;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.common.JM;
import journeymap.common.Journeymap;
import journeymap.common.version.VersionCheck;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.ControlsScreen;

public class FullscreenActions {

    public static void open() {
        UIManager.INSTANCE.openFullscreenMap();
    }

    public static void showCaveLayers() {
        UIManager.INSTANCE.openFullscreenMap().showCaveLayers();
    }

    public static void launchLocalhost() {
        String url = "http://localhost:" + JourneymapClient.getInstance().getWebMapProperties().port.get();
        Util.getPlatform().openUri(url);
    }

    public static void launchWebsite(String path) {
        String url = Journeymap.WEBSITE_URL + path;
        Util.getPlatform().openUri(url);
    }

    public static void toggleSearchBar() {
        UIManager.INSTANCE.openFullscreenMap().toggleSearchBar(true);
    }

    public static void openKeybindings() {
        UIManager.INSTANCE.closeAll();
        Fullscreen fullscreen = UIManager.INSTANCE.openFullscreenMap();
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new ControlsScreen(fullscreen, mc.options));
    }

    public static void tweet(String message) {
        String url = null;
        try {
            url = "http://twitter.com/home/?status=@JourneyMapMod+" + URLEncoder.encode(message, "UTF-8");
            Util.getPlatform().openUri(url);
        } catch (Throwable var3) {
            var3.printStackTrace();
        }
    }

    public static void discord() {
        String url = JM.DISCORD_URL;
        Util.getPlatform().openUri(url);
    }

    public static void launchDownloadWebsite() {
        String url = VersionCheck.getDownloadUrl();
        Util.getPlatform().openUri(url);
    }
}