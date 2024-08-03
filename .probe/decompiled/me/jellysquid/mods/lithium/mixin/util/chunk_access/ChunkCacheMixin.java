package me.jellysquid.mods.lithium.mixin.util.chunk_access;

import me.jellysquid.mods.lithium.common.world.ChunkView;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ PathNavigationRegion.class })
public abstract class ChunkCacheMixin implements ChunkView {

    @Shadow
    protected abstract ChunkAccess getChunk(int var1, int var2);

    @Nullable
    @Override
    public ChunkAccess getLoadedChunk(int chunkX, int chunkZ) {
        return this.getChunk(chunkX, chunkZ);
    }
}