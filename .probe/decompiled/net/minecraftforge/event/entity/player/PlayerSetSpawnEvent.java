package net.minecraftforge.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.Nullable;

@Cancelable
public class PlayerSetSpawnEvent extends PlayerEvent {

    private final ResourceKey<Level> spawnLevel;

    private final boolean forced;

    @Nullable
    private final BlockPos newSpawn;

    public PlayerSetSpawnEvent(Player player, ResourceKey<Level> spawnLevel, @Nullable BlockPos newSpawn, boolean forced) {
        super(player);
        this.spawnLevel = spawnLevel;
        this.newSpawn = newSpawn;
        this.forced = forced;
    }

    public boolean isForced() {
        return this.forced;
    }

    @Nullable
    public BlockPos getNewSpawn() {
        return this.newSpawn;
    }

    public ResourceKey<Level> getSpawnLevel() {
        return this.spawnLevel;
    }
}