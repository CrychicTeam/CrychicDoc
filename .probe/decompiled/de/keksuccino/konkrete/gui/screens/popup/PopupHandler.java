package de.keksuccino.konkrete.gui.screens.popup;

import de.keksuccino.konkrete.input.MouseInput;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PopupHandler {

    private static Popup popup;

    private static boolean initDone = false;

    public static void init() {
        if (!initDone) {
            MinecraftForge.EVENT_BUS.register(PopupHandler.class);
            initDone = true;
        }
    }

    @SubscribeEvent
    public static void onRender(ScreenEvent.Render.Post e) {
        if (popup != null && popup.isDisplayed()) {
            MouseInput.blockVanillaInput("popupgui");
            RenderUtils.setZLevelPre(e.getGuiGraphics().pose(), 500);
            popup.render(e.getGuiGraphics(), e.getMouseX(), e.getMouseY(), e.getScreen());
            RenderUtils.setZLevelPost(e.getGuiGraphics().pose());
        } else {
            MouseInput.unblockVanillaInput("popupgui");
        }
    }

    public static boolean isPopupActive() {
        return popup == null ? false : popup.isDisplayed();
    }

    public static Popup getCurrentPopup() {
        return popup;
    }

    public static void displayPopup(Popup p) {
        if (popup != null) {
            popup.setDisplayed(false);
        }
        popup = p;
        popup.setDisplayed(true);
    }
}