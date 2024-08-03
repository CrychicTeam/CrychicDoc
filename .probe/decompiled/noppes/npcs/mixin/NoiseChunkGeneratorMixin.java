package noppes.npcs.mixin;

import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import noppes.npcs.NPCSpawning;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ NoiseBasedChunkGenerator.class })
public class NoiseChunkGeneratorMixin {

    @Inject(at = { @At("HEAD") }, method = { "spawnOriginalMobs" }, cancellable = false)
    private void spawnOriginalMobs(WorldGenRegion region, CallbackInfo ci) {
        ChunkPos chunkpos = region.getCenter();
        int x = chunkpos.x;
        int z = chunkpos.z;
        Biome biome = (Biome) region.m_204166_(new ChunkPos(x, z).getWorldPosition()).value();
        NPCSpawning.performLevelGenSpawning(region.getLevel(), biome, x, z, region.getRandom());
    }
}