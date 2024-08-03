package journeymap.client.event.forge;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.event.handlers.KeyEventHandler;
import journeymap.client.event.handlers.keymapping.KeyEvent;
import journeymap.common.Journeymap;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ParametersAreNonnullByDefault
public class ForgeKeyEvents implements KeyEvent, ForgeEventHandlerManager.EventHandler {

    private static final List<KeyMapping> keyList = Lists.newArrayList();

    private final KeyEventHandler keyEventHandler = new KeyEventHandler(this);

    @SubscribeEvent
    public void onGameKeyboardEvent(InputEvent.Key event) {
        int key = event.getKey();
        this.keyEventHandler.onGameKeyboardEvent(key);
    }

    @SubscribeEvent
    public void onGuiKeyboardEvent(ScreenEvent.KeyPressed.Post event) {
        int key = event.getKeyCode();
        boolean success = this.keyEventHandler.onGuiKeyboardEvent(event.getScreen(), key);
        event.setCanceled(success);
    }

    @SubscribeEvent
    public void onGuiMouseEvent(ScreenEvent.MouseButtonPressed.Post event) {
        int key = event.getButton();
        this.keyEventHandler.onMouseEvent(key, event.getScreen());
    }

    @SubscribeEvent
    public void onMouseEvent(InputEvent.MouseButton.Post event) {
        if (Minecraft.getInstance().screen == null && event.getAction() == 0) {
            int key = event.getButton();
            this.keyEventHandler.onMouseEvent(key, null);
        }
    }

    public KeyEventHandler getHandler() {
        return this.keyEventHandler;
    }

    @Override
    public KeyMapping register(KeyMapping keyMapping) {
        keyList.add(keyMapping);
        return keyMapping;
    }

    @SubscribeEvent
    public static void onKeyRegisterEvent(RegisterKeyMappingsEvent event) {
        Journeymap.getLogger().info("Registering Keybinds");
        keyList.forEach(event::register);
    }
}