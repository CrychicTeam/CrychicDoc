package net.minecraftforge.event.entity.player;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class PlayerSleepInBedEvent extends PlayerEvent {

    private Player.BedSleepingProblem result = null;

    private final Optional<BlockPos> pos;

    public PlayerSleepInBedEvent(Player player, Optional<BlockPos> pos) {
        super(player);
        this.pos = pos;
    }

    public Player.BedSleepingProblem getResultStatus() {
        return this.result;
    }

    public void setResult(Player.BedSleepingProblem result) {
        this.result = result;
    }

    public BlockPos getPos() {
        return (BlockPos) this.pos.orElse(null);
    }

    public Optional<BlockPos> getOptionalPos() {
        return this.pos;
    }
}