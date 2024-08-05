package de.keksuccino.fancymenu.customization.panorama;

import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.events.ModReloadEvent;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import de.keksuccino.fancymenu.util.event.acara.EventListener;
import de.keksuccino.fancymenu.util.file.FileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PanoramaHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<String, LocalTexturePanoramaRenderer> PANORAMAS = new HashMap();

    public static final File PANORAMA_DIR = FileUtils.createDirectory(new File(FancyMenu.MOD_DIR, "panoramas"));

    public static void init() {
        updatePanoramas();
        EventHandler.INSTANCE.registerListenersOf(new PanoramaHandler());
    }

    public static void updatePanoramas() {
        PANORAMAS.clear();
        File[] files = (File[]) Objects.requireNonNullElse(PANORAMA_DIR.listFiles(), new File[0]);
        for (File panorama : files) {
            if (panorama.isDirectory()) {
                File propertiesFile = new File(panorama, "properties.txt");
                if (!propertiesFile.isFile()) {
                    propertiesFile = new File(panorama, "properties.txt.txt");
                }
                File imageDir = new File(panorama, "panorama");
                File overlayImageFile = new File(panorama, "overlay.png");
                if (!overlayImageFile.isFile()) {
                    overlayImageFile = null;
                }
                if (propertiesFile.isFile() && imageDir.isDirectory()) {
                    LocalTexturePanoramaRenderer renderer = LocalTexturePanoramaRenderer.build(propertiesFile, imageDir, overlayImageFile);
                    if (renderer != null) {
                        PANORAMAS.put(renderer.getName(), renderer);
                    } else {
                        LOGGER.error(buildErrorMessage(panorama, false, false, false) + " (failed to build renderer - was NULL)");
                    }
                } else {
                    LOGGER.error(buildErrorMessage(panorama, true, propertiesFile.isFile(), imageDir.isDirectory()));
                }
            } else {
                LOGGER.error(buildErrorMessage(panorama, false, false, false) + " (not a directory)");
            }
        }
    }

    private static String buildErrorMessage(@NotNull File panoramaDir, boolean addDetails, boolean propertiesFileFound, boolean imageDirFound) {
        String msg = "[FANCYMENU] Invalid panorama found: " + panoramaDir.getName();
        if (addDetails) {
            String details = "";
            if (!propertiesFileFound) {
                details = details + "Missing \"properties.txt\" file";
            }
            if (!details.isEmpty()) {
                details = details + "; ";
            }
            if (!imageDirFound) {
                details = details + "Missing \"panorama\" directory for images";
            }
            if (!details.isEmpty()) {
                msg = msg + " (" + details + ")";
            }
        }
        return msg;
    }

    @Nullable
    public static LocalTexturePanoramaRenderer getPanorama(@NotNull String name) {
        return (LocalTexturePanoramaRenderer) PANORAMAS.get(Objects.requireNonNull(name));
    }

    @NotNull
    public static List<LocalTexturePanoramaRenderer> getPanoramas() {
        return new ArrayList(PANORAMAS.values());
    }

    @NotNull
    public static List<String> getPanoramaNames() {
        return new ArrayList(PANORAMAS.keySet());
    }

    public static boolean panoramaExists(@NotNull String name) {
        return PANORAMAS.containsKey(Objects.requireNonNull(name));
    }

    @EventListener
    public void onMenuReload(ModReloadEvent e) {
        LOGGER.info("[FANCYMENU] Reloading panoramas..");
        updatePanoramas();
    }
}