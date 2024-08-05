package net.minecraft.world.level.levelgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.blending.Blender;

public class DebugLevelSource extends ChunkGenerator {

    public static final Codec<DebugLevelSource> CODEC = RecordCodecBuilder.create(p_255576_ -> p_255576_.group(RegistryOps.retrieveElement(Biomes.PLAINS)).apply(p_255576_, p_255576_.stable(DebugLevelSource::new)));

    private static final int BLOCK_MARGIN = 2;

    private static final List<BlockState> ALL_BLOCKS = (List<BlockState>) StreamSupport.stream(BuiltInRegistries.BLOCK.spliterator(), false).flatMap(p_208208_ -> p_208208_.getStateDefinition().getPossibleStates().stream()).collect(Collectors.toList());

    private static final int GRID_WIDTH = Mth.ceil(Mth.sqrt((float) ALL_BLOCKS.size()));

    private static final int GRID_HEIGHT = Mth.ceil((float) ALL_BLOCKS.size() / (float) GRID_WIDTH);

    protected static final BlockState AIR = Blocks.AIR.defaultBlockState();

    protected static final BlockState BARRIER = Blocks.BARRIER.defaultBlockState();

    public static final int HEIGHT = 70;

    public static final int BARRIER_HEIGHT = 60;

    public DebugLevelSource(Holder.Reference<Biome> holderReferenceBiome0) {
        super(new FixedBiomeSource(holderReferenceBiome0));
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void buildSurface(WorldGenRegion worldGenRegion0, StructureManager structureManager1, RandomState randomState2, ChunkAccess chunkAccess3) {
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel worldGenLevel0, ChunkAccess chunkAccess1, StructureManager structureManager2) {
        BlockPos.MutableBlockPos $$3 = new BlockPos.MutableBlockPos();
        ChunkPos $$4 = chunkAccess1.getPos();
        int $$5 = $$4.x;
        int $$6 = $$4.z;
        for (int $$7 = 0; $$7 < 16; $$7++) {
            for (int $$8 = 0; $$8 < 16; $$8++) {
                int $$9 = SectionPos.sectionToBlockCoord($$5, $$7);
                int $$10 = SectionPos.sectionToBlockCoord($$6, $$8);
                worldGenLevel0.m_7731_($$3.set($$9, 60, $$10), BARRIER, 2);
                BlockState $$11 = getBlockStateFor($$9, $$10);
                worldGenLevel0.m_7731_($$3.set($$9, 70, $$10), $$11, 2);
            }
        }
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor0, Blender blender1, RandomState randomState2, StructureManager structureManager3, ChunkAccess chunkAccess4) {
        return CompletableFuture.completedFuture(chunkAccess4);
    }

    @Override
    public int getBaseHeight(int int0, int int1, Heightmap.Types heightmapTypes2, LevelHeightAccessor levelHeightAccessor3, RandomState randomState4) {
        return 0;
    }

    @Override
    public NoiseColumn getBaseColumn(int int0, int int1, LevelHeightAccessor levelHeightAccessor2, RandomState randomState3) {
        return new NoiseColumn(0, new BlockState[0]);
    }

    @Override
    public void addDebugScreenInfo(List<String> listString0, RandomState randomState1, BlockPos blockPos2) {
    }

    public static BlockState getBlockStateFor(int int0, int int1) {
        BlockState $$2 = AIR;
        if (int0 > 0 && int1 > 0 && int0 % 2 != 0 && int1 % 2 != 0) {
            int0 /= 2;
            int1 /= 2;
            if (int0 <= GRID_WIDTH && int1 <= GRID_HEIGHT) {
                int $$3 = Mth.abs(int0 * GRID_WIDTH + int1);
                if ($$3 < ALL_BLOCKS.size()) {
                    $$2 = (BlockState) ALL_BLOCKS.get($$3);
                }
            }
        }
        return $$2;
    }

    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion0, long long1, RandomState randomState2, BiomeManager biomeManager3, StructureManager structureManager4, ChunkAccess chunkAccess5, GenerationStep.Carving generationStepCarving6) {
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion worldGenRegion0) {
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getGenDepth() {
        return 384;
    }

    @Override
    public int getSeaLevel() {
        return 63;
    }
}