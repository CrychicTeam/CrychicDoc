package me.jellysquid.mods.lithium.mixin.collections.block_entity_tickers;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.ticks.LevelChunkTicks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LevelChunk.class })
public class WorldChunkMixin {

    @Mutable
    @Shadow
    @Final
    private Map<BlockPos, ?> tickersInLevel;

    @Inject(method = { "<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/chunk/UpgradeData;Lnet/minecraft/world/tick/ChunkTickScheduler;Lnet/minecraft/world/tick/ChunkTickScheduler;J[Lnet/minecraft/world/chunk/ChunkSection;Lnet/minecraft/world/chunk/WorldChunk$EntityLoader;Lnet/minecraft/world/gen/chunk/BlendingData;)V" }, at = { @At("RETURN") })
    @Coerce
    private void createFastUtilMap(Level world, ChunkPos pos, UpgradeData upgradeData, LevelChunkTicks<?> blockTickScheduler, LevelChunkTicks<?> fluidTickScheduler, long inhabitedTime, LevelChunkSection[] sectionArrayInitializer, LevelChunk.PostLoadProcessor entityLoader, BlendingData blendingData, CallbackInfo ci) {
        this.tickersInLevel = new Object2ObjectOpenHashMap();
    }
}