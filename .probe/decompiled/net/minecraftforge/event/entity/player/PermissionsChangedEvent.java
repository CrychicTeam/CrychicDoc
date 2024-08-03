package net.minecraftforge.event.entity.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class PermissionsChangedEvent extends PlayerEvent {

    private final int newLevel;

    private final int oldLevel;

    public PermissionsChangedEvent(ServerPlayer player, int newLevel, int oldLevel) {
        super(player);
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public int getNewLevel() {
        return this.newLevel;
    }

    public int getOldLevel() {
        return this.oldLevel;
    }
}