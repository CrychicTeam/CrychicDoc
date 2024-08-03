package de.keksuccino.fancymenu.customization.layer;

import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.animation.AnimationHandler;
import de.keksuccino.fancymenu.customization.customgui.CustomGuiBaseScreen;
import de.keksuccino.fancymenu.customization.screen.identifier.ScreenIdentifierHandler;
import de.keksuccino.fancymenu.events.screen.InitOrResizeScreenStartingEvent;
import de.keksuccino.fancymenu.util.MinecraftResourceReloadObserver;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import de.keksuccino.fancymenu.util.event.acara.EventListener;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScreenCustomizationLayerHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    protected static final Map<String, ScreenCustomizationLayer> LAYERS = new HashMap();

    private static volatile boolean neverReloaded = true;

    private static volatile boolean resourceReload = false;

    private ScreenCustomizationLayerHandler() {
    }

    public static void init() {
        EventHandler.INSTANCE.registerListenersOf(new ScreenCustomizationLayerHandler());
        MinecraftResourceReloadObserver.addReloadListener(ScreenCustomizationLayerHandler::onMinecraftReload);
    }

    private static void onMinecraftReload(@NotNull MinecraftResourceReloadObserver.ReloadAction reloadAction) {
        if (reloadAction == MinecraftResourceReloadObserver.ReloadAction.STARTING) {
            LOGGER.info("[FANCYMENU] Minecraft resource reload: STARTING");
            resourceReload = true;
        } else {
            neverReloaded = false;
            resourceReload = false;
            ScreenCustomization.setIsNewMenu(true);
            LOGGER.info("[FANCYMENU] Updating animation sizes..");
            AnimationHandler.updateAnimationSizes();
            LOGGER.info("[FANCYMENU] Minecraft resource reload: FINISHED");
        }
    }

    public static boolean isBeforeFinishInitialMinecraftReload() {
        return neverReloaded;
    }

    public static boolean isMinecraftCurrentlyReloading() {
        return resourceReload;
    }

    public static void registerScreen(@NotNull Screen screen) {
        String identifier = ScreenIdentifierHandler.getIdentifierOfScreen(screen);
        if (!LAYERS.containsKey(identifier)) {
            ScreenCustomizationLayer layer = new ScreenCustomizationLayer(identifier);
            registerLayer(identifier, layer);
        }
    }

    public static void registerLayer(@NotNull ScreenCustomizationLayer layer) {
        registerLayer(layer.getScreenIdentifier(), layer);
    }

    public static void registerLayer(@NotNull String screenIdentifier, @NotNull ScreenCustomizationLayer layer) {
        screenIdentifier = ScreenIdentifierHandler.getBestIdentifier(screenIdentifier);
        if (!LAYERS.containsKey(screenIdentifier)) {
            LOGGER.info("[FANCYMENU] ScreenCustomizationLayer registered: " + screenIdentifier);
        } else {
            LOGGER.warn("[FANCYMENU] ScreenCustomizationLayer replaced: " + screenIdentifier);
        }
        LAYERS.put(screenIdentifier, layer);
    }

    public static boolean isLayerRegistered(@NotNull String screenIdentifier) {
        return LAYERS.containsKey(screenIdentifier);
    }

    @Nullable
    public static ScreenCustomizationLayer getActiveLayer() {
        Screen s = Minecraft.getInstance().screen;
        return s != null && !ScreenCustomization.isScreenBlacklisted(s) ? getLayerOfScreen(s) : null;
    }

    @Nullable
    public static ScreenCustomizationLayer getLayerOfScreen(@NotNull Screen screen) {
        return screen instanceof CustomGuiBaseScreen custom ? getLayer(custom.getIdentifier()) : getLayerOfScreen(screen.getClass());
    }

    @Nullable
    public static ScreenCustomizationLayer getLayerOfScreen(@NotNull Class<? extends Screen> screenClass) {
        if (screenClass == CustomGuiBaseScreen.class) {
            throw new IllegalArgumentException("Tried to get ScreenCustomizationLayer of CustomGuiBaseScreen class! This is not possible!");
        } else {
            return getLayer(screenClass.getName());
        }
    }

    @Nullable
    public static ScreenCustomizationLayer getLayer(@NotNull String screenIdentifier) {
        return (ScreenCustomizationLayer) LAYERS.get(ScreenIdentifierHandler.getBestIdentifier(screenIdentifier));
    }

    @EventListener
    public void onScreenInitOrResizeStarting(InitOrResizeScreenStartingEvent e) {
        this.autoRegisterScreenLayer(e.getScreen());
    }

    protected void autoRegisterScreenLayer(Screen screen) {
        if (screen != null) {
            if (ScreenCustomization.isScreenBlacklisted(screen)) {
                return;
            }
            if (screen instanceof CustomGuiBaseScreen c) {
                if (!isLayerRegistered(c.getIdentifier())) {
                    registerLayer(new ScreenCustomizationLayer(c.getIdentifier()));
                }
            } else {
                registerScreen(screen);
            }
        }
    }
}