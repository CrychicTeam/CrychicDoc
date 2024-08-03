package yalter.mousetweaks.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import yalter.mousetweaks.Logger;
import yalter.mousetweaks.Main;
import yalter.mousetweaks.MouseButton;

@Mod("mousetweaks")
public class MouseTweaksForge {

    public MouseTweaksForge() {
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "ANY", (remote, isServer) -> true));
        if (FMLEnvironment.dist != Dist.CLIENT) {
            Logger.Log("Disabled because not running on the client.");
        } else {
            Main.initialize();
            MinecraftForge.EVENT_BUS.register(this);
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, ClientHelper::createConfigScreenFactory);
        }
    }

    @SubscribeEvent
    public void onGuiMouseClickedPre(ScreenEvent.MouseButtonPressed.Pre event) {
        Logger.DebugLog("onGuiMouseClickedPre button = " + event.getButton());
        MouseButton button = MouseButton.fromEventButton(event.getButton());
        if (button != null && Main.onMouseClicked(event.getScreen(), event.getMouseX(), event.getMouseY(), button)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onGuiMouseReleasedPre(ScreenEvent.MouseButtonReleased.Pre event) {
        Logger.DebugLog("onGuiMouseReleasedPre button = " + event.getButton());
        MouseButton button = MouseButton.fromEventButton(event.getButton());
        if (button != null && Main.onMouseReleased(event.getScreen(), event.getMouseX(), event.getMouseY(), button)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onGuiMouseScrollPost(ScreenEvent.MouseScrolled.Post event) {
        Logger.DebugLog("onGuiMouseScrollPost delta = " + event.getScrollDelta());
        Main.onMouseScrolled(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDelta());
    }

    @SubscribeEvent
    public void onGuiMouseDragPre(ScreenEvent.MouseDragged.Pre event) {
        Logger.DebugLog("onGuiMouseDragPre button = " + event.getMouseButton() + ", dx = " + event.getDragX() + ", dy = " + event.getDragY());
        MouseButton button = MouseButton.fromEventButton(event.getMouseButton());
        if (button != null && Main.onMouseDrag(event.getScreen(), event.getMouseX(), event.getMouseY(), button)) {
            event.setCanceled(true);
        }
    }
}