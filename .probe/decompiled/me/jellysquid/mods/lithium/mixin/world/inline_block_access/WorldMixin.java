package me.jellysquid.mods.lithium.mixin.world.inline_block_access;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ Level.class })
public abstract class WorldMixin implements LevelHeightAccessor {

    private static final BlockState OUTSIDE_WORLD_BLOCK = Blocks.VOID_AIR.defaultBlockState();

    private static final BlockState INSIDE_WORLD_DEFAULT_BLOCK = Blocks.AIR.defaultBlockState();

    @Shadow
    public abstract LevelChunk getChunk(int var1, int var2);

    @Overwrite
    public BlockState getBlockState(BlockPos pos) {
        LevelChunk worldChunk = this.getChunk(SectionPos.blockToSectionCoord(pos.m_123341_()), SectionPos.blockToSectionCoord(pos.m_123343_()));
        LevelChunkSection[] sections = worldChunk.m_7103_();
        int x = pos.m_123341_();
        int y = pos.m_123342_();
        int z = pos.m_123343_();
        int chunkY = this.m_151564_(y);
        if (chunkY >= 0 && chunkY < sections.length) {
            LevelChunkSection section = sections[chunkY];
            return section != null && !section.hasOnlyAir() ? section.getBlockState(x & 15, y & 15, z & 15) : INSIDE_WORLD_DEFAULT_BLOCK;
        } else {
            return OUTSIDE_WORLD_BLOCK;
        }
    }

    @Redirect(method = { "getFluidState" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isOutOfHeightLimit(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean skipFluidHeightLimitTest(Level world, BlockPos pos) {
        return world.m_151570_(pos);
    }
}