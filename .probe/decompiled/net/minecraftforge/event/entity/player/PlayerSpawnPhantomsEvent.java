package net.minecraftforge.event.entity.player;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event.HasResult;
import net.minecraftforge.eventbus.api.Event.Result;
import org.jetbrains.annotations.NotNull;

@HasResult
public class PlayerSpawnPhantomsEvent extends PlayerEvent {

    private int phantomsToSpawn;

    public PlayerSpawnPhantomsEvent(Player player, int phantomsToSpawn) {
        super(player);
        this.phantomsToSpawn = phantomsToSpawn;
    }

    public int getPhantomsToSpawn() {
        return this.phantomsToSpawn;
    }

    public void setPhantomsToSpawn(int phantomsToSpawn) {
        this.phantomsToSpawn = phantomsToSpawn;
    }

    public void setResult(@NotNull Result result) {
        super.setResult(result);
    }
}