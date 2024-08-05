package net.minecraftforge.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

public class OnDatapackSyncEvent extends Event {

    private final PlayerList playerList;

    @Nullable
    private final ServerPlayer player;

    public OnDatapackSyncEvent(PlayerList playerList, @Nullable ServerPlayer player) {
        this.playerList = playerList;
        this.player = player;
    }

    public PlayerList getPlayerList() {
        return this.playerList;
    }

    @Nullable
    public ServerPlayer getPlayer() {
        return this.player;
    }
}