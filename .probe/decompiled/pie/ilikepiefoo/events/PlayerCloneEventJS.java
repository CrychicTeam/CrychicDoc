package pie.ilikepiefoo.events;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PlayerCloneEventJS extends PlayerEventJS {

    private final ServerPlayer oldPlayer;

    private final ServerPlayer newPlayer;

    private final boolean conqueredEnd;

    public PlayerCloneEventJS(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean conqueredEnd) {
        this.oldPlayer = oldPlayer;
        this.newPlayer = newPlayer;
        this.conqueredEnd = conqueredEnd;
    }

    public static PlayerCloneEventJS of(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean conqueredEnd) {
        return new PlayerCloneEventJS(oldPlayer, newPlayer, conqueredEnd);
    }

    @Override
    public Player getEntity() {
        return this.newPlayer;
    }

    public Player getOldPlayer() {
        return this.oldPlayer;
    }

    public Player getNewPlayer() {
        return this.newPlayer;
    }

    public boolean leavingEnd() {
        return this.conqueredEnd;
    }

    public boolean returningFromEnd() {
        return this.conqueredEnd;
    }

    public boolean causedByPortal() {
        return this.conqueredEnd;
    }

    public boolean causedByDeath() {
        return !this.conqueredEnd;
    }
}