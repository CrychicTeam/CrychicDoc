package me.jellysquid.mods.lithium.mixin.util.block_entity_retrieval;

import me.jellysquid.mods.lithium.common.world.blockentity.BlockEntityGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ Level.class })
public abstract class WorldMixin implements BlockEntityGetter, LevelAccessor {

    @Shadow
    @Final
    public boolean isClientSide;

    @Shadow
    @Final
    private Thread thread;

    @Shadow
    public abstract LevelChunk getChunk(int var1, int var2);

    @Shadow
    @Nullable
    @Override
    public abstract ChunkAccess getChunk(int var1, int var2, ChunkStatus var3, boolean var4);

    @Override
    public BlockEntity getLoadedExistingBlockEntity(BlockPos pos) {
        if (!this.m_151570_(pos) && (this.isClientSide || Thread.currentThread() == this.thread)) {
            ChunkAccess chunk = this.getChunk(SectionPos.blockToSectionCoord(pos.m_123341_()), SectionPos.blockToSectionCoord(pos.m_123343_()), ChunkStatus.FULL, false);
            if (chunk != null) {
                return chunk.m_7702_(pos);
            }
        }
        return null;
    }
}