package me.jellysquid.mods.lithium.mixin.util.chunk_access;

import me.jellysquid.mods.lithium.common.world.ChunkView;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ LevelReader.class })
public interface WorldViewMixin extends ChunkView {

    @Shadow
    @Nullable
    ChunkAccess getChunk(int var1, int var2, ChunkStatus var3, boolean var4);

    @Nullable
    @Override
    default ChunkAccess getLoadedChunk(int chunkX, int chunkZ) {
        return this.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
    }
}