package net.minecraftforge.client.event;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.level.GameType;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ClientPlayerChangeGameTypeEvent extends Event {

    private final PlayerInfo info;

    private final GameType currentGameType;

    private final GameType newGameType;

    @Internal
    public ClientPlayerChangeGameTypeEvent(PlayerInfo info, GameType currentGameType, GameType newGameType) {
        this.info = info;
        this.currentGameType = currentGameType;
        this.newGameType = newGameType;
    }

    public PlayerInfo getInfo() {
        return this.info;
    }

    public GameType getCurrentGameType() {
        return this.currentGameType;
    }

    public GameType getNewGameType() {
        return this.newGameType;
    }
}