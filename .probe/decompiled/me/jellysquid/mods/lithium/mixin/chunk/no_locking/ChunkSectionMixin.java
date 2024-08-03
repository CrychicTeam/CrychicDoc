package me.jellysquid.mods.lithium.mixin.chunk.no_locking;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ LevelChunkSection.class })
public abstract class ChunkSectionMixin {

    @Shadow
    public abstract BlockState setBlockState(int var1, int var2, int var3, BlockState var4, boolean var5);

    @Redirect(method = { "setBlockState(IIILnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkSection;setBlockState(IIILnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;"))
    private BlockState setBlockStateNoLocking(LevelChunkSection chunkSection, int x, int y, int z, BlockState state, boolean lock) {
        return this.setBlockState(x, y, z, state, false);
    }
}