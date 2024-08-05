package net.minecraftforge.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Event.HasResult;

@HasResult
public class SleepingLocationCheckEvent extends LivingEvent {

    private final BlockPos sleepingLocation;

    public SleepingLocationCheckEvent(LivingEntity player, BlockPos sleepingLocation) {
        super(player);
        this.sleepingLocation = sleepingLocation;
    }

    public BlockPos getSleepingLocation() {
        return this.sleepingLocation;
    }
}