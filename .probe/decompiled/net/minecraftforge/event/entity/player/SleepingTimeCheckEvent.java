package net.minecraftforge.event.entity.player;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event.HasResult;

@HasResult
public class SleepingTimeCheckEvent extends PlayerEvent {

    private final Optional<BlockPos> sleepingLocation;

    public SleepingTimeCheckEvent(Player player, Optional<BlockPos> sleepingLocation) {
        super(player);
        this.sleepingLocation = sleepingLocation;
    }

    public Optional<BlockPos> getSleepingLocation() {
        return this.sleepingLocation;
    }
}