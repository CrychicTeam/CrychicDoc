package me.jellysquid.mods.lithium.mixin.gen.chunk_region;

import java.util.List;
import me.jellysquid.mods.lithium.common.util.Pos;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ WorldGenRegion.class })
public abstract class ChunkRegionMixin implements WorldGenLevel {

    @Shadow
    @Final
    private ChunkPos firstPos;

    @Shadow
    @Final
    private int size;

    private ChunkAccess[] chunksArr;

    private int minChunkX;

    private int minChunkZ;

    @Inject(method = { "<init>(Lnet/minecraft/server/world/ServerWorld;Ljava/util/List;Lnet/minecraft/world/chunk/ChunkStatus;I)V" }, at = { @At("RETURN") })
    private void init(ServerLevel world, List<ChunkAccess> chunks, ChunkStatus chunkStatus, int i, CallbackInfo ci) {
        this.minChunkX = this.firstPos.x;
        this.minChunkZ = this.firstPos.z;
        this.chunksArr = (ChunkAccess[]) chunks.toArray(new ChunkAccess[0]);
    }

    @Overwrite
    @Override
    public BlockState getBlockState(BlockPos pos) {
        int x = Pos.ChunkCoord.fromBlockCoord(pos.m_123341_()) - this.minChunkX;
        int z = Pos.ChunkCoord.fromBlockCoord(pos.m_123343_()) - this.minChunkZ;
        int w = this.size;
        if (x >= 0 && z >= 0 && x < w && z < w) {
            return this.chunksArr[x + z * w].m_8055_(pos);
        } else {
            throw new NullPointerException("No chunk exists at " + new ChunkPos(pos));
        }
    }

    @Overwrite
    @Override
    public ChunkAccess getChunk(int chunkX, int chunkZ) {
        int x = chunkX - this.minChunkX;
        int z = chunkZ - this.minChunkZ;
        int w = this.size;
        if (x >= 0 && z >= 0 && x < w && z < w) {
            return this.chunksArr[x + z * w];
        } else {
            throw new NullPointerException("No chunk exists at " + new ChunkPos(chunkX, chunkZ));
        }
    }

    @Override
    public ChunkAccess getChunk(BlockPos pos) {
        return this.getChunk(Pos.ChunkCoord.fromBlockCoord(pos.m_123341_()), Pos.ChunkCoord.fromBlockCoord(pos.m_123343_()));
    }
}