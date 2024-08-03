package me.jellysquid.mods.lithium.mixin.ai.pathing;

import me.jellysquid.mods.lithium.common.util.Pos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ PathNavigationRegion.class })
public abstract class ChunkCacheMixin implements BlockGetter {

    private static final BlockState DEFAULT_BLOCK = Blocks.AIR.defaultBlockState();

    @Shadow
    @Final
    protected ChunkAccess[][] chunks;

    @Shadow
    @Final
    protected int centerX;

    @Shadow
    @Final
    protected int centerZ;

    @Shadow
    @Final
    protected Level level;

    private ChunkAccess[] chunksFlat;

    private int xLen;

    private int zLen;

    private int bottomY;

    private int topY;

    @Inject(method = { "<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)V" }, at = { @At("RETURN") })
    private void init(Level world, BlockPos minPos, BlockPos maxPos, CallbackInfo ci) {
        this.xLen = 1 + Pos.ChunkCoord.fromBlockCoord(maxPos.m_123341_()) - Pos.ChunkCoord.fromBlockCoord(minPos.m_123341_());
        this.zLen = 1 + Pos.ChunkCoord.fromBlockCoord(maxPos.m_123343_()) - Pos.ChunkCoord.fromBlockCoord(minPos.m_123343_());
        this.chunksFlat = new ChunkAccess[this.xLen * this.zLen];
        for (int x = 0; x < this.xLen; x++) {
            System.arraycopy(this.chunks[x], 0, this.chunksFlat, x * this.zLen, this.zLen);
        }
        this.bottomY = this.m_141937_();
        this.topY = this.m_151558_();
    }

    @Overwrite
    @Override
    public BlockState getBlockState(BlockPos pos) {
        int y = pos.m_123342_();
        if (y >= this.bottomY && y < this.topY) {
            int x = pos.m_123341_();
            int z = pos.m_123343_();
            int chunkX = Pos.ChunkCoord.fromBlockCoord(x) - this.centerX;
            int chunkZ = Pos.ChunkCoord.fromBlockCoord(z) - this.centerZ;
            if (chunkX >= 0 && chunkX < this.xLen && chunkZ >= 0 && chunkZ < this.zLen) {
                ChunkAccess chunk = this.chunksFlat[chunkX * this.zLen + chunkZ];
                if (chunk != null) {
                    LevelChunkSection section = chunk.getSections()[Pos.SectionYIndex.fromBlockCoord(this, y)];
                    if (section != null) {
                        return section.getBlockState(x & 15, y & 15, z & 15);
                    }
                }
            }
        }
        return DEFAULT_BLOCK;
    }

    @Overwrite
    @Override
    public FluidState getFluidState(BlockPos pos) {
        return this.getBlockState(pos).m_60819_();
    }
}