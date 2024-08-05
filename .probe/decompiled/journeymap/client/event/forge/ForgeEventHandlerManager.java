package journeymap.client.event.forge;

import java.util.ArrayList;
import java.util.HashMap;
import journeymap.client.JourneymapClient;
import journeymap.client.cartography.color.ColorManager;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraftforge.common.MinecraftForge;

public class ForgeEventHandlerManager {

    private static HashMap<Class<? extends ForgeEventHandlerManager.EventHandler>, ForgeEventHandlerManager.EventHandler> handlers = new HashMap();

    public static void registerHandlers() {
        register(JourneymapClient.getInstance().getKeyEvents());
        register(new ForgeChatEvents());
        register(new ForgeHudOverlayEvents());
        register(new ForgeWorldEvent());
        register(new ForgeChunkEvents());
        register(new ForgeClientTickEvent());
        register(new ForgeRenderLevelLastEvent());
        register(new ForgeLoggedInEvent());
        register(new ForgeTextureStitchedEvent());
        register(new ForgePopupCustomEvents());
        ColorManager.INSTANCE.getDeclaringClass();
    }

    public static void unregisterAll() {
        for (Class<? extends ForgeEventHandlerManager.EventHandler> handlerClass : new ArrayList(handlers.keySet())) {
            unregister(handlerClass);
        }
    }

    public static void register(ForgeEventHandlerManager.EventHandler handler) {
        Class<? extends ForgeEventHandlerManager.EventHandler> handlerClass = handler.getClass();
        if (handlers.containsKey(handlerClass)) {
            Journeymap.getLogger().warn("Handler already registered: " + handlerClass.getName());
        } else {
            try {
                MinecraftForge.EVENT_BUS.register(handler);
                Journeymap.getLogger().debug("Handler registered: " + handlerClass.getName());
                handlers.put(handler.getClass(), handler);
            } catch (Throwable var3) {
                Journeymap.getLogger().error(handlerClass.getName() + " registration FAILED: " + LogFormatter.toString(var3));
            }
        }
    }

    public static void unregister(Class<? extends ForgeEventHandlerManager.EventHandler> handlerClass) {
        ForgeEventHandlerManager.EventHandler handler = (ForgeEventHandlerManager.EventHandler) handlers.remove(handlerClass);
        if (handler != null) {
            try {
                MinecraftForge.EVENT_BUS.unregister(handler);
                Journeymap.getLogger().debug("Handler unregistered: " + handlerClass.getName());
            } catch (Throwable var3) {
                Journeymap.getLogger().error(handler + " unregistration FAILED: " + LogFormatter.toString(var3));
            }
        }
    }

    public static HashMap<Class<? extends ForgeEventHandlerManager.EventHandler>, ForgeEventHandlerManager.EventHandler> getHandlers() {
        return handlers;
    }

    public interface EventHandler {
    }
}