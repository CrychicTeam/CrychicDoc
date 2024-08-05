package me.jellysquid.mods.sodium.client.world;

import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import me.jellysquid.mods.sodium.client.world.biome.BiomeColorCache;
import me.jellysquid.mods.sodium.client.world.biome.BiomeColorSource;
import me.jellysquid.mods.sodium.client.world.biome.BiomeColorView;
import me.jellysquid.mods.sodium.client.world.biome.BiomeSlice;
import me.jellysquid.mods.sodium.client.world.cloned.ChunkRenderContext;
import me.jellysquid.mods.sodium.client.world.cloned.ClonedChunkSection;
import me.jellysquid.mods.sodium.client.world.cloned.ClonedChunkSectionCache;
import net.fabricmc.fabric.api.blockview.v2.FabricBlockView;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.model.data.ModelDataManager;
import org.embeddedt.embeddium.api.ChunkMeshEvent;
import org.embeddedt.embeddium.api.MeshAppender;
import org.embeddedt.embeddium.asm.OptionalInterface;
import org.jetbrains.annotations.Nullable;

@OptionalInterface({ FabricBlockView.class, RenderAttachedBlockView.class })
public class WorldSlice implements BlockAndTintGetter, BiomeColorView, FabricBlockView, RenderAttachedBlockView {

    private static final LightLayer[] LIGHT_TYPES = LightLayer.values();

    private static final int SECTION_BLOCK_COUNT = 4096;

    private static final int NEIGHBOR_BLOCK_RADIUS = 2;

    private static final int NEIGHBOR_CHUNK_RADIUS = Mth.roundToward(2, 16) >> 4;

    private static final int SECTION_ARRAY_LENGTH = 1 + NEIGHBOR_CHUNK_RADIUS * 2;

    private static final int SECTION_ARRAY_SIZE = SECTION_ARRAY_LENGTH * SECTION_ARRAY_LENGTH * SECTION_ARRAY_LENGTH;

    private static final int BLOCK_ARRAY_LENGTH = SECTION_ARRAY_LENGTH * 16;

    private static final int LOCAL_XYZ_BITS = 4;

    private static final BlockState EMPTY_BLOCK_STATE = Blocks.AIR.defaultBlockState();

    public final ClientLevel world;

    private final BiomeSlice biomeSlice;

    private final BiomeColorCache biomeColors;

    private final BlockState[][] blockArrays;

    @Nullable
    private final DataLayer[][] lightArrays;

    @Nullable
    private final Int2ReferenceMap<BlockEntity>[] blockEntityArrays;

    @Nullable
    private final Int2ReferenceMap<Object>[] blockEntityRenderDataArrays;

    private int originX;

    private int originY;

    private int originZ;

    private BoundingBox volume;

    public static ChunkRenderContext prepare(Level world, SectionPos origin, ClonedChunkSectionCache sectionCache) {
        LevelChunk chunk = world.getChunk(origin.m_123341_(), origin.m_123343_());
        LevelChunkSection section = chunk.m_7103_()[world.m_151566_(origin.m_123342_())];
        List<MeshAppender> meshAppenders = ChunkMeshEvent.post(world, origin);
        boolean isEmpty = (section == null || section.hasOnlyAir()) && meshAppenders.isEmpty();
        if (isEmpty) {
            return null;
        } else {
            BoundingBox volume = new BoundingBox(origin.minBlockX() - 2, origin.minBlockY() - 2, origin.minBlockZ() - 2, origin.maxBlockX() + 2, origin.maxBlockY() + 2, origin.maxBlockZ() + 2);
            int minChunkX = origin.m_123341_() - NEIGHBOR_CHUNK_RADIUS;
            int minChunkY = origin.m_123342_() - NEIGHBOR_CHUNK_RADIUS;
            int minChunkZ = origin.m_123343_() - NEIGHBOR_CHUNK_RADIUS;
            int maxChunkX = origin.m_123341_() + NEIGHBOR_CHUNK_RADIUS;
            int maxChunkY = origin.m_123342_() + NEIGHBOR_CHUNK_RADIUS;
            int maxChunkZ = origin.m_123343_() + NEIGHBOR_CHUNK_RADIUS;
            ClonedChunkSection[] sections = new ClonedChunkSection[SECTION_ARRAY_SIZE];
            for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
                for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                    for (int chunkY = minChunkY; chunkY <= maxChunkY; chunkY++) {
                        sections[getLocalSectionIndex(chunkX - minChunkX, chunkY - minChunkY, chunkZ - minChunkZ)] = sectionCache.acquire(chunkX, chunkY, chunkZ);
                    }
                }
            }
            return new ChunkRenderContext(origin, sections, volume).withMeshAppenders(meshAppenders);
        }
    }

    public WorldSlice(ClientLevel world) {
        this.world = world;
        this.blockArrays = new BlockState[SECTION_ARRAY_SIZE][4096];
        this.lightArrays = new DataLayer[SECTION_ARRAY_SIZE][LIGHT_TYPES.length];
        this.blockEntityArrays = new Int2ReferenceMap[SECTION_ARRAY_SIZE];
        this.blockEntityRenderDataArrays = new Int2ReferenceMap[SECTION_ARRAY_SIZE];
        this.biomeSlice = new BiomeSlice();
        this.biomeColors = new BiomeColorCache(this.biomeSlice, Minecraft.getInstance().options.biomeBlendRadius().get());
        for (BlockState[] blockArray : this.blockArrays) {
            Arrays.fill(blockArray, EMPTY_BLOCK_STATE);
        }
    }

    public void copyData(ChunkRenderContext context) {
        this.originX = context.getOrigin().m_123341_() - NEIGHBOR_CHUNK_RADIUS << 4;
        this.originY = context.getOrigin().m_123342_() - NEIGHBOR_CHUNK_RADIUS << 4;
        this.originZ = context.getOrigin().m_123343_() - NEIGHBOR_CHUNK_RADIUS << 4;
        this.volume = context.getVolume();
        for (int x = 0; x < SECTION_ARRAY_LENGTH; x++) {
            for (int y = 0; y < SECTION_ARRAY_LENGTH; y++) {
                for (int z = 0; z < SECTION_ARRAY_LENGTH; z++) {
                    this.copySectionData(context, getLocalSectionIndex(x, y, z));
                }
            }
        }
        this.biomeSlice.update(this.world, context);
        this.biomeColors.update(context);
    }

    private void copySectionData(ChunkRenderContext context, int sectionIndex) {
        ClonedChunkSection section = context.getSections()[sectionIndex];
        Objects.requireNonNull(section, "Chunk section must be non-null");
        try {
            this.unpackBlockData(this.blockArrays[sectionIndex], context, section);
        } catch (RuntimeException var5) {
            throw new IllegalStateException("Exception copying block data for section: " + section.getPosition(), var5);
        }
        this.lightArrays[sectionIndex][LightLayer.BLOCK.ordinal()] = section.getLightArray(LightLayer.BLOCK);
        this.lightArrays[sectionIndex][LightLayer.SKY.ordinal()] = section.getLightArray(LightLayer.SKY);
        this.blockEntityArrays[sectionIndex] = section.getBlockEntityMap();
        this.blockEntityRenderDataArrays[sectionIndex] = section.getBlockEntityRenderDataMap();
    }

    private void unpackBlockData(BlockState[] blockArray, ChunkRenderContext context, ClonedChunkSection section) {
        if (section.getBlockData() == null) {
            Arrays.fill(blockArray, EMPTY_BLOCK_STATE);
        } else {
            ReadableContainerExtended<BlockState> container = ReadableContainerExtended.of(section.getBlockData());
            SectionPos origin = context.getOrigin();
            SectionPos pos = section.getPosition();
            if (origin.equals(pos)) {
                container.sodium$unpack(blockArray);
            } else {
                BoundingBox bounds = context.getVolume();
                int minBlockX = Math.max(bounds.minX(), pos.minBlockX());
                int maxBlockX = Math.min(bounds.maxX(), pos.maxBlockX());
                int minBlockY = Math.max(bounds.minY(), pos.minBlockY());
                int maxBlockY = Math.min(bounds.maxY(), pos.maxBlockY());
                int minBlockZ = Math.max(bounds.minZ(), pos.minBlockZ());
                int maxBlockZ = Math.min(bounds.maxZ(), pos.maxBlockZ());
                container.sodium$unpack(blockArray, minBlockX & 15, minBlockY & 15, minBlockZ & 15, maxBlockX & 15, maxBlockY & 15, maxBlockZ & 15);
            }
        }
    }

    public void reset() {
        for (int sectionIndex = 0; sectionIndex < SECTION_ARRAY_LENGTH; sectionIndex++) {
            Arrays.fill(this.lightArrays[sectionIndex], null);
            this.blockEntityArrays[sectionIndex] = null;
        }
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return this.getBlockState(pos.m_123341_(), pos.m_123342_(), pos.m_123343_());
    }

    public BlockState getBlockState(int x, int y, int z) {
        int relX = x - this.originX;
        int relY = y - this.originY;
        int relZ = z - this.originZ;
        return !this.isInside(relX, relY, relZ) ? EMPTY_BLOCK_STATE : this.blockArrays[getLocalSectionIndex(relX >> 4, relY >> 4, relZ >> 4)][getLocalBlockIndex(relX & 15, relY & 15, relZ & 15)];
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return this.getBlockState(pos).m_60819_();
    }

    @Override
    public float getShade(Direction direction, boolean shaded) {
        return this.world.getShade(direction, shaded);
    }

    public float getShade(float normalX, float normalY, float normalZ, boolean shade) {
        return this.world.getShade(normalX, normalY, normalZ, shade);
    }

    @Override
    public LevelLightEngine getLightEngine() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getBrightness(LightLayer type, BlockPos pos) {
        int relX = pos.m_123341_() - this.originX;
        int relY = pos.m_123342_() - this.originY;
        int relZ = pos.m_123343_() - this.originZ;
        if (!this.isInside(relX, relY, relZ)) {
            return 0;
        } else {
            DataLayer lightArray = this.lightArrays[getLocalSectionIndex(relX >> 4, relY >> 4, relZ >> 4)][type.ordinal()];
            return lightArray == null ? 0 : lightArray.get(relX & 15, relY & 15, relZ & 15);
        }
    }

    @Override
    public int getRawBrightness(BlockPos pos, int ambientDarkness) {
        int relX = pos.m_123341_() - this.originX;
        int relY = pos.m_123342_() - this.originY;
        int relZ = pos.m_123343_() - this.originZ;
        if (!this.isInside(relX, relY, relZ)) {
            return 0;
        } else {
            DataLayer[] lightArrays = this.lightArrays[getLocalSectionIndex(relX >> 4, relY >> 4, relZ >> 4)];
            DataLayer skyLightArray = lightArrays[LightLayer.SKY.ordinal()];
            DataLayer blockLightArray = lightArrays[LightLayer.BLOCK.ordinal()];
            int localX = relX & 15;
            int localY = relY & 15;
            int localZ = relZ & 15;
            int skyLight = skyLightArray == null ? 0 : skyLightArray.get(localX, localY, localZ) - ambientDarkness;
            int blockLight = blockLightArray == null ? 0 : blockLightArray.get(localX, localY, localZ);
            return Math.max(blockLight, skyLight);
        }
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return this.getBlockEntity(pos.m_123341_(), pos.m_123342_(), pos.m_123343_());
    }

    public BlockEntity getBlockEntity(int x, int y, int z) {
        int relX = x - this.originX;
        int relY = y - this.originY;
        int relZ = z - this.originZ;
        if (!this.isInside(relX, relY, relZ)) {
            return null;
        } else {
            Int2ReferenceMap<BlockEntity> blockEntities = this.blockEntityArrays[getLocalSectionIndex(relX >> 4, relY >> 4, relZ >> 4)];
            return blockEntities == null ? null : (BlockEntity) blockEntities.get(getLocalBlockIndex(relX & 15, relY & 15, relZ & 15));
        }
    }

    @Override
    public int getBlockTint(BlockPos pos, ColorResolver resolver) {
        return this.biomeColors.getColor(resolver, pos.m_123341_(), pos.m_123342_(), pos.m_123343_());
    }

    @Override
    public int getHeight() {
        return this.world.m_141928_();
    }

    @Override
    public int getMinBuildHeight() {
        return this.world.m_141937_();
    }

    @Override
    public int getColor(BiomeColorSource source, int x, int y, int z) {
        return this.biomeColors.getColor(source, x, y, z);
    }

    @Nullable
    public ModelDataManager getModelDataManager() {
        return this.world.getModelDataManager();
    }

    public Holder<Biome> getBiomeFabric(BlockPos pos) {
        return this.biomeSlice.getBiome(pos.m_123341_(), pos.m_123342_(), pos.m_123343_());
    }

    public boolean hasBiomes() {
        return true;
    }

    public Object getBlockEntityRenderData(BlockPos pos) {
        int relX = pos.m_123341_() - this.originX;
        int relY = pos.m_123342_() - this.originY;
        int relZ = pos.m_123343_() - this.originZ;
        if (!this.isInside(relX, relY, relZ)) {
            return null;
        } else {
            Int2ReferenceMap<Object> blockEntityRenderDataMap = this.blockEntityRenderDataArrays[getLocalSectionIndex(relX >> 4, relY >> 4, relZ >> 4)];
            return blockEntityRenderDataMap == null ? null : blockEntityRenderDataMap.get(getLocalBlockIndex(relX & 15, relY & 15, relZ & 15));
        }
    }

    public static int getLocalBlockIndex(int x, int y, int z) {
        return y << 4 << 4 | z << 4 | x;
    }

    public static int getLocalSectionIndex(int x, int y, int z) {
        return y * SECTION_ARRAY_LENGTH * SECTION_ARRAY_LENGTH + z * SECTION_ARRAY_LENGTH + x;
    }

    private boolean isInside(int relX, int relY, int relZ) {
        return relX >= 0 && relX < BLOCK_ARRAY_LENGTH && relZ >= 0 && relZ < BLOCK_ARRAY_LENGTH && relY >= 0 && relY < BLOCK_ARRAY_LENGTH;
    }
}