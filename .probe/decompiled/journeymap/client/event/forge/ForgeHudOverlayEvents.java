package journeymap.client.event.forge;

import journeymap.client.event.handlers.HudOverlayHandler;
import journeymap.client.ui.minimap.Effect;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeHudOverlayEvents implements ForgeEventHandlerManager.EventHandler {

    private boolean shouldPop = false;

    private final HudOverlayHandler overlayHandler = new HudOverlayHandler();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void preOverlayLow(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay().id().equals(VanillaGuiOverlay.POTION_ICONS.id())) {
            this.shouldPop = this.overlayHandler.preOverlay(event.getGuiGraphics());
        } else {
            this.shouldPop = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void preOverlay(RenderGuiEvent.Pre event) {
        if (Effect.getInstance().canPotionShift()) {
            this.shouldPop = false;
        }
        if (event.isCancelable() && !event.isCanceled()) {
            this.overlayHandler.onRenderOverlay(event.getGuiGraphics());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void postOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay().id().equals(VanillaGuiOverlay.POTION_ICONS.id()) && this.shouldPop && EventPriority.HIGHEST.equals(event.getPhase()) && Effect.getInstance().canPotionShift()) {
            this.overlayHandler.postOverlay(event.getGuiGraphics());
            this.shouldPop = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderOverlayDebug(CustomizeGuiOverlayEvent.DebugText event) {
        this.overlayHandler.onRenderOverlayDebug(event.getLeft());
    }
}