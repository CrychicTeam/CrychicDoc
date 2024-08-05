package net.minecraftforge.client.event;

import java.util.UUID;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus.Internal;

@Cancelable
public class ClientChatReceivedEvent extends Event {

    private Component message;

    private final ChatType.Bound boundChatType;

    private final UUID sender;

    @Internal
    public ClientChatReceivedEvent(ChatType.Bound boundChatType, Component message, UUID sender) {
        this.boundChatType = boundChatType;
        this.message = message;
        this.sender = sender;
    }

    public Component getMessage() {
        return this.message;
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public ChatType.Bound getBoundChatType() {
        return this.boundChatType;
    }

    public UUID getSender() {
        return this.sender;
    }

    public boolean isSystem() {
        return this.sender.equals(Util.NIL_UUID);
    }

    public static class Player extends ClientChatReceivedEvent {

        private final PlayerChatMessage playerChatMessage;

        @Internal
        public Player(ChatType.Bound boundChatType, Component message, PlayerChatMessage playerChatMessage, UUID sender) {
            super(boundChatType, message, sender);
            this.playerChatMessage = playerChatMessage;
        }

        public PlayerChatMessage getPlayerChatMessage() {
            return this.playerChatMessage;
        }
    }

    public static class System extends ClientChatReceivedEvent {

        private final boolean overlay;

        @Internal
        public System(ChatType.Bound boundChatType, Component message, boolean overlay) {
            super(boundChatType, message, Util.NIL_UUID);
            this.overlay = overlay;
        }

        public boolean isOverlay() {
            return this.overlay;
        }
    }
}