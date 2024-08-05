package journeymap.client.event.forge;

import journeymap.client.event.handlers.ChatEventHandler;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeChatEvents implements ForgeEventHandlerManager.EventHandler {

    private final ChatEventHandler chatEventHandler = new ChatEventHandler();

    public boolean onChatEvent(String message) {
        return this.chatEventHandler.onChatEvent("/" + message);
    }

    @SubscribeEvent
    public void invoke(ClientChatReceivedEvent event) {
        Component component = this.chatEventHandler.onClientChatEventReceived(event.getMessage());
        if (component != null) {
            event.setMessage(component);
        }
    }

    public ChatEventHandler getHandler() {
        return this.chatEventHandler;
    }
}