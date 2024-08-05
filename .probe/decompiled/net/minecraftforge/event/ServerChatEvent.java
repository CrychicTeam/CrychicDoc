package net.minecraftforge.event;

import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus.Internal;

@Cancelable
public class ServerChatEvent extends Event {

    private final ServerPlayer player;

    private final String username;

    private final String rawText;

    private Component message;

    @Internal
    public ServerChatEvent(ServerPlayer player, String rawText, Component message) {
        this.player = player;
        this.username = player.m_36316_().getName();
        this.rawText = rawText;
        this.message = message;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }

    public String getUsername() {
        return this.username;
    }

    public String getRawText() {
        return this.rawText;
    }

    public void setMessage(Component message) {
        this.message = (Component) Objects.requireNonNull(message);
    }

    public Component getMessage() {
        return this.message;
    }
}