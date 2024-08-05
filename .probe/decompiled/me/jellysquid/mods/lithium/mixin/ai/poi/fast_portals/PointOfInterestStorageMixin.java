package me.jellysquid.mods.lithium.mixin.ai.poi.fast_portals;

import com.mojang.datafixers.DataFixer;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.nio.file.Path;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiSection;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.storage.SectionStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ PoiManager.class })
public abstract class PointOfInterestStorageMixin extends SectionStorage<PoiSection> {

    @Shadow
    @Final
    private LongSet loadedChunks;

    @Unique
    private final LongSet preloadedCenterChunks = new LongOpenHashSet();

    @Unique
    private int preloadRadius = 0;

    public PointOfInterestStorageMixin(Path path, DataFixer dataFixer, boolean dsync, RegistryAccess registryManager, LevelHeightAccessor world) {
        super(path, PoiSection::m_27295_, PoiSection::new, dataFixer, DataFixTypes.POI_CHUNK, dsync, registryManager, world);
    }

    @Overwrite
    public void ensureLoadedAndValid(LevelReader worldView, BlockPos pos, int radius) {
        if (this.preloadRadius != radius) {
            this.preloadedCenterChunks.clear();
            this.preloadRadius = radius;
        }
        long chunkPos = ChunkPos.asLong(pos);
        if (!this.preloadedCenterChunks.contains(chunkPos)) {
            int chunkX = SectionPos.blockToSectionCoord(pos.m_123341_());
            int chunkZ = SectionPos.blockToSectionCoord(pos.m_123343_());
            int chunkRadius = Math.floorDiv(radius, 16);
            int maxHeight = this.f_156618_.getMaxSection() - 1;
            int minHeight = this.f_156618_.getMinSection();
            int x = chunkX - chunkRadius;
            for (int xMax = chunkX + chunkRadius; x <= xMax; x++) {
                int z = chunkZ - chunkRadius;
                for (int zMax = chunkZ + chunkRadius; z <= zMax; z++) {
                    this.lithium$preloadChunkIfAnySubChunkContainsPOI(worldView, x, z, minHeight, maxHeight);
                }
            }
            this.preloadedCenterChunks.add(chunkPos);
        }
    }

    @Unique
    private void lithium$preloadChunkIfAnySubChunkContainsPOI(LevelReader worldView, int x, int z, int minSubChunk, int maxSubChunk) {
        ChunkPos chunkPos = new ChunkPos(x, z);
        long longChunkPos = chunkPos.toLong();
        if (!this.loadedChunks.contains(longChunkPos)) {
            for (int y = minSubChunk; y <= maxSubChunk; y++) {
                Optional<PoiSection> section = this.m_63823_(SectionPos.asLong(x, y, z));
                if (section.isPresent()) {
                    boolean result = ((PoiSection) section.get()).isValid();
                    if (result) {
                        if (this.loadedChunks.add(longChunkPos)) {
                            worldView.getChunk(x, z, ChunkStatus.EMPTY);
                        }
                        break;
                    }
                }
            }
        }
    }
}