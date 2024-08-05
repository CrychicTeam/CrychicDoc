package dev.latvian.mods.kubejs.player;

import dev.architectury.event.events.common.ChatEvent;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

@Info("Invoked when a player sends a chat message.\n\nIf cancelled (`PlayerEvents.chat`), the message will not be sent.\n")
public class PlayerChatDecorateEventJS extends PlayerEventJS {

    private final ServerPlayer player;

    public ChatEvent.ChatComponent chatComponent;

    public PlayerChatDecorateEventJS(ServerPlayer player, ChatEvent.ChatComponent chatComponent) {
        this.player = player;
        this.chatComponent = chatComponent;
    }

    @Info("Gets the player that sent the message.")
    public ServerPlayer getEntity() {
        return this.player;
    }

    @Info("Gets the username of the player that sent the message.")
    public String getUsername() {
        return this.player.m_36316_().getName();
    }

    @Info("Gets the message that the player sent.")
    public String getMessage() {
        return this.chatComponent.get().getString();
    }

    @Info("Gets the message that the player sent.")
    public Component getComponent() {
        return this.chatComponent.get();
    }

    @Info("Sets the message that the player sent.")
    public void setMessage(Component text) {
        this.chatComponent.set(text);
    }

    @Info("Sets the message that the player sent.")
    public void setComponent(Component text) {
        this.chatComponent.set(text);
    }
}