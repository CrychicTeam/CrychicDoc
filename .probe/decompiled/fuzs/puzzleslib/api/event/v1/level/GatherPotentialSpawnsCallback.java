package fuzs.puzzleslib.api.event.v1.level;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;

@FunctionalInterface
public interface GatherPotentialSpawnsCallback {

    EventInvoker<GatherPotentialSpawnsCallback> EVENT = EventInvoker.lookup(GatherPotentialSpawnsCallback.class);

    void onGatherPotentialSpawns(ServerLevel var1, StructureManager var2, ChunkGenerator var3, MobCategory var4, BlockPos var5, List<MobSpawnSettings.SpawnerData> var6);
}