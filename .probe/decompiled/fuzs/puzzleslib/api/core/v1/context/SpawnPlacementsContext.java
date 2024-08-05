package fuzs.puzzleslib.api.core.v1.context;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

@FunctionalInterface
public interface SpawnPlacementsContext {

    <T extends Mob> void registerSpawnPlacement(EntityType<T> var1, SpawnPlacements.Type var2, Heightmap.Types var3, SpawnPlacements.SpawnPredicate<T> var4);
}