package me.jellysquid.mods.sodium.client.world.cloned;

import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMaps;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Map.Entry;
import me.jellysquid.mods.sodium.client.world.ReadableContainerExtended;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.fabricmc.fabric.api.blockview.v2.RenderDataBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.levelgen.DebugLevelSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClonedChunkSection {

    private static final DataLayer DEFAULT_SKY_LIGHT_ARRAY = new DataLayer(15);

    private static final DataLayer DEFAULT_BLOCK_LIGHT_ARRAY = new DataLayer(0);

    private static final PalettedContainer<BlockState> DEFAULT_STATE_CONTAINER = new PalettedContainer<>(Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState(), PalettedContainer.Strategy.SECTION_STATES);

    private static final boolean HAS_FABRIC_RENDER_DATA;

    private final SectionPos pos;

    @Nullable
    private final Int2ReferenceMap<BlockEntity> blockEntityMap;

    @Nullable
    private final Int2ReferenceMap<Object> blockEntityRenderDataMap;

    @Nullable
    private final DataLayer[] lightDataArrays;

    @Nullable
    private final PalettedContainerRO<BlockState> blockData;

    @Nullable
    private final PalettedContainerRO<Holder<Biome>> biomeData;

    private long lastUsedTimestamp = Long.MAX_VALUE;

    public ClonedChunkSection(Level world, LevelChunk chunk, @Nullable LevelChunkSection section, SectionPos pos) {
        this.pos = pos;
        PalettedContainerRO<BlockState> blockData = null;
        PalettedContainerRO<Holder<Biome>> biomeData = null;
        Int2ReferenceMap<BlockEntity> blockEntityMap = null;
        Int2ReferenceMap<Object> blockEntityRenderDataMap = null;
        if (section != null) {
            if (!section.hasOnlyAir()) {
                if (!world.isDebug()) {
                    blockData = ReadableContainerExtended.clone(section.getStates());
                } else {
                    blockData = constructDebugWorldContainer(pos);
                }
                blockEntityMap = copyBlockEntities(chunk, pos);
                if (blockEntityMap != null) {
                    blockEntityRenderDataMap = copyBlockEntityRenderData(blockEntityMap);
                }
            }
            biomeData = ReadableContainerExtended.clone(section.getBiomes());
        }
        this.blockData = blockData;
        this.biomeData = biomeData;
        this.blockEntityMap = blockEntityMap;
        this.blockEntityRenderDataMap = blockEntityRenderDataMap;
        this.lightDataArrays = copyLightData(world, pos);
    }

    @NotNull
    private static PalettedContainer<BlockState> constructDebugWorldContainer(SectionPos pos) {
        if (pos.m_123342_() != 3 && pos.m_123342_() != 4) {
            return DEFAULT_STATE_CONTAINER;
        } else {
            PalettedContainer<BlockState> container = new PalettedContainer<>(Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState(), PalettedContainer.Strategy.SECTION_STATES);
            if (pos.m_123342_() == 3) {
                BlockState barrier = Blocks.BARRIER.defaultBlockState();
                for (int z = 0; z < 16; z++) {
                    for (int x = 0; x < 16; x++) {
                        container.getAndSetUnchecked(x, 12, z, barrier);
                    }
                }
            } else if (pos.m_123342_() == 4) {
                for (int z = 0; z < 16; z++) {
                    for (int x = 0; x < 16; x++) {
                        container.getAndSetUnchecked(x, 6, z, DebugLevelSource.getBlockStateFor(SectionPos.sectionToBlockCoord(pos.m_123341_(), x), SectionPos.sectionToBlockCoord(pos.m_123343_(), z)));
                    }
                }
            }
            return container;
        }
    }

    @NotNull
    private static DataLayer[] copyLightData(Level world, SectionPos pos) {
        DataLayer[] arrays = new DataLayer[2];
        arrays[LightLayer.BLOCK.ordinal()] = copyLightArray(world, LightLayer.BLOCK, pos);
        if (world.dimensionType().hasSkyLight()) {
            arrays[LightLayer.SKY.ordinal()] = copyLightArray(world, LightLayer.SKY, pos);
        }
        return arrays;
    }

    @NotNull
    private static DataLayer copyLightArray(Level world, LightLayer type, SectionPos pos) {
        DataLayer array = world.getLightEngine().getLayerListener(type).getDataLayerData(pos);
        if (array == null) {
            array = switch(type) {
                case SKY ->
                    DEFAULT_SKY_LIGHT_ARRAY;
                case BLOCK ->
                    DEFAULT_BLOCK_LIGHT_ARRAY;
            };
        }
        return array;
    }

    @Nullable
    private static Int2ReferenceMap<BlockEntity> copyBlockEntities(LevelChunk chunk, SectionPos chunkCoord) {
        BoundingBox box = new BoundingBox(chunkCoord.minBlockX(), chunkCoord.minBlockY(), chunkCoord.minBlockZ(), chunkCoord.maxBlockX(), chunkCoord.maxBlockY(), chunkCoord.maxBlockZ());
        Int2ReferenceOpenHashMap<BlockEntity> blockEntities = null;
        for (Entry<BlockPos, BlockEntity> entry : chunk.getBlockEntities().entrySet()) {
            BlockPos pos = (BlockPos) entry.getKey();
            BlockEntity entity = (BlockEntity) entry.getValue();
            if (box.isInside(pos)) {
                if (blockEntities == null) {
                    blockEntities = new Int2ReferenceOpenHashMap();
                }
                blockEntities.put(WorldSlice.getLocalBlockIndex(pos.m_123341_() & 15, pos.m_123342_() & 15, pos.m_123343_() & 15), entity);
            }
        }
        if (blockEntities != null) {
            blockEntities.trim();
        }
        return blockEntities;
    }

    @Nullable
    private static Int2ReferenceMap<Object> copyBlockEntityRenderData(Int2ReferenceMap<BlockEntity> blockEntities) {
        if (!HAS_FABRIC_RENDER_DATA) {
            return null;
        } else {
            Int2ReferenceOpenHashMap<Object> blockEntityRenderDataMap = null;
            ObjectIterator var2 = Int2ReferenceMaps.fastIterable(blockEntities).iterator();
            while (var2.hasNext()) {
                it.unimi.dsi.fastutil.ints.Int2ReferenceMap.Entry<BlockEntity> entry = (it.unimi.dsi.fastutil.ints.Int2ReferenceMap.Entry<BlockEntity>) var2.next();
                Object data = ((RenderDataBlockEntity) entry.getValue()).getRenderData();
                if (data != null) {
                    if (blockEntityRenderDataMap == null) {
                        blockEntityRenderDataMap = new Int2ReferenceOpenHashMap();
                    }
                    blockEntityRenderDataMap.put(entry.getIntKey(), data);
                }
            }
            if (blockEntityRenderDataMap != null) {
                blockEntityRenderDataMap.trim();
            }
            return blockEntityRenderDataMap;
        }
    }

    public SectionPos getPosition() {
        return this.pos;
    }

    @Nullable
    public PalettedContainerRO<BlockState> getBlockData() {
        return this.blockData;
    }

    @Nullable
    public PalettedContainerRO<Holder<Biome>> getBiomeData() {
        return this.biomeData;
    }

    @Nullable
    public Int2ReferenceMap<BlockEntity> getBlockEntityMap() {
        return this.blockEntityMap;
    }

    @Nullable
    public Int2ReferenceMap<Object> getBlockEntityRenderDataMap() {
        return this.blockEntityRenderDataMap;
    }

    @Nullable
    public DataLayer getLightArray(LightLayer lightType) {
        return this.lightDataArrays[lightType.ordinal()];
    }

    public long getLastUsedTimestamp() {
        return this.lastUsedTimestamp;
    }

    public void setLastUsedTimestamp(long timestamp) {
        this.lastUsedTimestamp = timestamp;
    }

    static {
        boolean hasRenderData;
        try {
            hasRenderData = RenderDataBlockEntity.class.isAssignableFrom(BlockEntity.class);
        } catch (Throwable var2) {
            hasRenderData = false;
        }
        HAS_FABRIC_RENDER_DATA = hasRenderData;
    }
}