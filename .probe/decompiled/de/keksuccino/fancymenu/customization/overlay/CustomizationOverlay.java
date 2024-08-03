package de.keksuccino.fancymenu.customization.overlay;

import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.events.screen.InitOrResizeScreenCompletedEvent;
import de.keksuccino.fancymenu.events.screen.RenderScreenEvent;
import de.keksuccino.fancymenu.events.screen.ScreenKeyPressedEvent;
import de.keksuccino.fancymenu.util.ConsumingSupplier;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import de.keksuccino.fancymenu.util.event.acara.EventListener;
import de.keksuccino.fancymenu.util.rendering.ui.screen.CustomizableScreen;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomizationOverlay {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<String, ConsumingSupplier<Screen, Boolean>> OVERLAY_VISIBILITY_CONTROLLERS = new HashMap();

    private static CustomizationOverlayMenuBar overlayMenuBar;

    private static DebugOverlay debugOverlay;

    public static void init() {
        EventHandler.INSTANCE.registerListenersOf(new CustomizationOverlay());
    }

    public static void rebuildOverlay() {
        overlayMenuBar = CustomizationOverlayUI.buildMenuBar(overlayMenuBar == null || overlayMenuBar.isExpanded());
        rebuildDebugOverlay();
    }

    public static void rebuildDebugOverlay() {
        if (debugOverlay != null) {
            debugOverlay.resetOverlay();
        }
        debugOverlay = CustomizationOverlayUI.buildDebugOverlay(overlayMenuBar);
    }

    @Nullable
    public static CustomizationOverlayMenuBar getCurrentMenuBarInstance() {
        return overlayMenuBar;
    }

    @Nullable
    public static DebugOverlay getCurrentDebugOverlayInstance() {
        return debugOverlay;
    }

    public static boolean isOverlayVisible(@Nullable Screen currentScreen) {
        if (FancyMenu.getOptions().modpackMode.getValue()) {
            return false;
        } else if (!FancyMenu.getOptions().showCustomizationOverlay.getValue()) {
            return false;
        } else if (currentScreen == null) {
            return false;
        } else {
            for (ConsumingSupplier<Screen, Boolean> s : OVERLAY_VISIBILITY_CONTROLLERS.values()) {
                if (!s.get(currentScreen)) {
                    return false;
                }
            }
            return true;
        }
    }

    @NotNull
    public static String registerOverlayVisibilityController(@NotNull ConsumingSupplier<Screen, Boolean> visibilityController) {
        String id = ScreenCustomization.generateUniqueIdentifier();
        OVERLAY_VISIBILITY_CONTROLLERS.put(id, (ConsumingSupplier) Objects.requireNonNull(visibilityController));
        return id;
    }

    public static void unregisterOverlayVisibilityController(@NotNull String identifier) {
        OVERLAY_VISIBILITY_CONTROLLERS.remove(Objects.requireNonNull(identifier));
    }

    @EventListener(priority = -1000)
    public void onInitScreenPost(InitOrResizeScreenCompletedEvent e) {
        if (!ScreenCustomization.isScreenBlacklisted(e.getScreen().getClass().getName()) && isOverlayVisible(e.getScreen())) {
            rebuildOverlay();
            if (overlayMenuBar != null && debugOverlay != null) {
                if (FancyMenu.getOptions().showCustomizationOverlay.getValue()) {
                    e.getWidgets().add(0, overlayMenuBar);
                    if (e.getScreen() instanceof CustomizableScreen c) {
                        c.removeOnInitChildrenFancyMenu().add(overlayMenuBar);
                    }
                }
                if (FancyMenu.getOptions().showDebugOverlay.getValue()) {
                    e.getWidgets().add(1, debugOverlay);
                    if (e.getScreen() instanceof CustomizableScreen c) {
                        c.removeOnInitChildrenFancyMenu().add(debugOverlay);
                    }
                }
            } else {
                LOGGER.error("[FANCYMENU] Failed to rebuild overlay!", new NullPointerException("Debug or Customization overlay was NULL!"));
            }
        }
    }

    @EventListener(priority = -1)
    public void onRenderPost(RenderScreenEvent.Post e) {
        if (!ScreenCustomization.isScreenBlacklisted(e.getScreen().getClass().getName()) && overlayMenuBar != null && debugOverlay != null && isOverlayVisible(e.getScreen())) {
            if (FancyMenu.getOptions().showDebugOverlay.getValue()) {
                debugOverlay.allowRender = true;
                debugOverlay.render(e.getGraphics(), e.getMouseX(), e.getMouseY(), e.getPartial());
                debugOverlay.allowRender = false;
            }
            if (FancyMenu.getOptions().showCustomizationOverlay.getValue()) {
                overlayMenuBar.allowRender = true;
                overlayMenuBar.render(e.getGraphics(), e.getMouseX(), e.getMouseY(), e.getPartial());
                overlayMenuBar.allowRender = false;
            }
        }
    }

    @EventListener
    public void onScreenKeyPressed(ScreenKeyPressedEvent e) {
        if (!ScreenCustomization.isScreenBlacklisted(e.getScreen().getClass().getName())) {
            String keyName = e.getKeyName();
            if (!FancyMenu.getOptions().modpackMode.getValue()) {
                if (keyName.equals("c") && Screen.hasControlDown() && Screen.hasAltDown()) {
                    FancyMenu.getOptions().showCustomizationOverlay.setValue(!FancyMenu.getOptions().showCustomizationOverlay.getValue());
                    ScreenCustomization.reInitCurrentScreen();
                }
                if (keyName.equals("d") && Screen.hasControlDown() && Screen.hasAltDown()) {
                    FancyMenu.getOptions().showDebugOverlay.setValue(!FancyMenu.getOptions().showDebugOverlay.getValue());
                    ScreenCustomization.reInitCurrentScreen();
                }
                if (keyName.equals("r") && Screen.hasControlDown() && Screen.hasAltDown()) {
                    ScreenCustomization.reloadFancyMenu();
                }
            }
        }
    }
}