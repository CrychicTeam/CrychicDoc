package pie.ilikepiefoo.events;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PlayerRespawnEventJS extends PlayerEventJS {

    private final ServerPlayer player;

    private final boolean conqueredEnd;

    public PlayerRespawnEventJS(ServerPlayer player, boolean conqueredEnd) {
        this.player = player;
        this.conqueredEnd = conqueredEnd;
    }

    public static PlayerRespawnEventJS of(ServerPlayer player, boolean conqueredEnd) {
        return new PlayerRespawnEventJS(player, conqueredEnd);
    }

    @Override
    public Player getEntity() {
        return this.player;
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