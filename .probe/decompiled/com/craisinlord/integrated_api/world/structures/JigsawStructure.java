package com.craisinlord.integrated_api.world.structures;

import com.craisinlord.integrated_api.modinit.IAStructures;
import com.craisinlord.integrated_api.utils.GeneralUtils;
import com.craisinlord.integrated_api.world.structures.pieces.manager.PieceLimitedJigsawManager;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptation;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptationType;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.CheckerboardColumnBiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.NotNull;

public class JigsawStructure extends Structure {

    public static final Codec<JigsawStructure> CODEC = RecordCodecBuilder.create(instance -> instance.group(m_226567_(instance), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool), Codec.intRange(0, 128).fieldOf("size").forGetter(structure -> structure.size), Codec.INT.optionalFieldOf("min_y_allowed").forGetter(structure -> structure.minYAllowed), Codec.INT.optionalFieldOf("max_y_allowed").forGetter(structure -> structure.maxYAllowed), Codec.intRange(1, 1000).optionalFieldOf("allowed_y_range_from_start").forGetter(structure -> structure.allowedYRangeFromStart), HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap), Codec.BOOL.fieldOf("cannot_spawn_in_liquid").orElse(false).forGetter(structure -> structure.cannotSpawnInLiquid), Codec.intRange(1, 100).optionalFieldOf("terrain_height_radius_check").forGetter(structure -> structure.terrainHeightCheckRadius), Codec.intRange(1, 1000).optionalFieldOf("allowed_terrain_height_range").forGetter(structure -> structure.allowedTerrainHeightRange), Codec.intRange(1, 100).optionalFieldOf("valid_biome_radius_check").forGetter(structure -> structure.biomeRadius), Codec.intRange(1, 512).optionalFieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter), StringRepresentable.fromEnum(JigsawStructure.BURYING_TYPE::values).optionalFieldOf("burying_type").forGetter(structure -> structure.buryingType), Codec.BOOL.fieldOf("rotation_fixed").orElse(false).forGetter(structure -> structure.rotationFixed), EnhancedTerrainAdaptationType.ADAPTATION_CODEC.optionalFieldOf("enhanced_terrain_adaptation", EnhancedTerrainAdaptation.NONE).forGetter(structure -> structure.enhancedTerrainAdaptation)).apply(instance, JigsawStructure::new));

    public final Holder<StructureTemplatePool> startPool;

    public final int size;

    public final Optional<Integer> minYAllowed;

    public final Optional<Integer> maxYAllowed;

    public final Optional<Integer> allowedYRangeFromStart;

    public final HeightProvider startHeight;

    public final Optional<Heightmap.Types> projectStartToHeightmap;

    public final boolean cannotSpawnInLiquid;

    public final Optional<Integer> terrainHeightCheckRadius;

    public final Optional<Integer> allowedTerrainHeightRange;

    public final Optional<Integer> biomeRadius;

    public final Optional<Integer> maxDistanceFromCenter;

    public final Optional<JigsawStructure.BURYING_TYPE> buryingType;

    public final boolean rotationFixed;

    public final EnhancedTerrainAdaptation enhancedTerrainAdaptation;

    public JigsawStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> startPool, int size, Optional<Integer> minYAllowed, Optional<Integer> maxYAllowed, Optional<Integer> allowedYRangeFromStart, HeightProvider startHeight, Optional<Heightmap.Types> projectStartToHeightmap, boolean cannotSpawnInLiquid, Optional<Integer> terrainHeightCheckRadius, Optional<Integer> allowedTerrainHeightRange, Optional<Integer> biomeRadius, Optional<Integer> maxDistanceFromCenter, Optional<JigsawStructure.BURYING_TYPE> buryingType, boolean rotationFixed, EnhancedTerrainAdaptation enhancedTerrainAdaptation) {
        super(config);
        this.startPool = startPool;
        this.size = size;
        this.minYAllowed = minYAllowed;
        this.maxYAllowed = maxYAllowed;
        this.allowedYRangeFromStart = allowedYRangeFromStart;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.cannotSpawnInLiquid = cannotSpawnInLiquid;
        this.terrainHeightCheckRadius = terrainHeightCheckRadius;
        this.allowedTerrainHeightRange = allowedTerrainHeightRange;
        this.biomeRadius = biomeRadius;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.buryingType = buryingType;
        this.rotationFixed = rotationFixed;
        this.enhancedTerrainAdaptation = enhancedTerrainAdaptation;
        if (maxYAllowed.isPresent() && minYAllowed.isPresent() && (Integer) maxYAllowed.get() < (Integer) minYAllowed.get()) {
            throw new RuntimeException("    Integrated API: maxYAllowed cannot be less than minYAllowed.\n    Please correct this error as there's no way to spawn this structure properly\n        Structure pool of problematic structure: %s\n".formatted(startPool.value()));
        } else if (maxDistanceFromCenter.isPresent() && (Integer) maxDistanceFromCenter.get() + enhancedTerrainAdaptation.getKernelRadius() > 512) {
            throw new RuntimeException("Integrated API: Structure size including enhanced terrain adaptation must not exceed 512");
        }
    }

    protected boolean extraSpawningChecks(Structure.GenerationContext context, BlockPos blockPos) {
        ChunkPos chunkPos = context.chunkPos();
        if (this.biomeRadius.isPresent() && !(context.biomeSource() instanceof CheckerboardColumnBiomeSource)) {
            int validBiomeRange = (Integer) this.biomeRadius.get();
            int sectionY = blockPos.m_123342_();
            if (this.projectStartToHeightmap.isPresent()) {
                sectionY += context.chunkGenerator().getFirstOccupiedHeight(blockPos.m_123341_(), blockPos.m_123343_(), (Heightmap.Types) this.projectStartToHeightmap.get(), context.heightAccessor(), context.randomState());
            }
            sectionY = QuartPos.fromBlock(sectionY);
            for (int curChunkX = chunkPos.x - validBiomeRange; curChunkX <= chunkPos.x + validBiomeRange; curChunkX++) {
                for (int curChunkZ = chunkPos.z - validBiomeRange; curChunkZ <= chunkPos.z + validBiomeRange; curChunkZ++) {
                    Holder<Biome> biome = context.biomeSource().getNoiseBiome(QuartPos.fromSection(curChunkX), sectionY, QuartPos.fromSection(curChunkZ), context.randomState().sampler());
                    if (!context.validBiome().test(biome)) {
                        return false;
                    }
                }
            }
        }
        if (this.cannotSpawnInLiquid) {
            BlockPos centerOfChunk = chunkPos.getMiddleBlockPosition(0);
            int landHeight = context.chunkGenerator().getFirstOccupiedHeight(centerOfChunk.m_123341_(), centerOfChunk.m_123343_(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
            NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(centerOfChunk.m_123341_(), centerOfChunk.m_123343_(), context.heightAccessor(), context.randomState());
            BlockState topBlock = columnOfBlocks.getBlock(centerOfChunk.m_123342_() + landHeight);
            if (!topBlock.m_60819_().isEmpty()) {
                return false;
            }
        }
        if (this.terrainHeightCheckRadius.isPresent() && (this.allowedTerrainHeightRange.isPresent() || this.minYAllowed.isPresent())) {
            int maxTerrainHeight = Integer.MIN_VALUE;
            int minTerrainHeight = Integer.MAX_VALUE;
            int terrainCheckRange = (Integer) this.terrainHeightCheckRadius.get();
            for (int curChunkX = chunkPos.x - terrainCheckRange; curChunkX <= chunkPos.x + terrainCheckRange; curChunkX++) {
                for (int curChunkZx = chunkPos.z - terrainCheckRange; curChunkZx <= chunkPos.z + terrainCheckRange; curChunkZx++) {
                    int height = context.chunkGenerator().getBaseHeight((curChunkX << 4) + 7, (curChunkZx << 4) + 7, (Heightmap.Types) this.projectStartToHeightmap.orElse(Heightmap.Types.WORLD_SURFACE_WG), context.heightAccessor(), context.randomState());
                    maxTerrainHeight = Math.max(maxTerrainHeight, height);
                    minTerrainHeight = Math.min(minTerrainHeight, height);
                    if (this.minYAllowed.isPresent() && minTerrainHeight < (Integer) this.minYAllowed.get()) {
                        return false;
                    }
                    if (this.maxYAllowed.isPresent() && minTerrainHeight > (Integer) this.maxYAllowed.get()) {
                        return false;
                    }
                }
            }
            if (this.allowedTerrainHeightRange.isPresent() && maxTerrainHeight - minTerrainHeight > (Integer) this.allowedTerrainHeightRange.get()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        int offsetY = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), offsetY, context.chunkPos().getMinBlockZ());
        if (!this.extraSpawningChecks(context, blockpos)) {
            return Optional.empty();
        } else {
            int topClipOff = Integer.MAX_VALUE;
            int bottomClipOff = Integer.MIN_VALUE;
            if (this.allowedYRangeFromStart.isPresent()) {
                topClipOff = blockpos.m_123342_() + (Integer) this.allowedYRangeFromStart.get();
                bottomClipOff = blockpos.m_123342_() - (Integer) this.allowedYRangeFromStart.get();
            }
            if (this.maxYAllowed.isPresent()) {
                topClipOff = Math.min(topClipOff, (Integer) this.maxYAllowed.get());
            }
            if (this.minYAllowed.isPresent()) {
                bottomClipOff = Math.max(bottomClipOff, (Integer) this.minYAllowed.get());
            }
            String rotationString;
            if (this.rotationFixed) {
                rotationString = "NONE";
            } else {
                rotationString = "RANDOM";
            }
            int finalTopClipOff = topClipOff;
            int finalBottomClipOff = bottomClipOff;
            return PieceLimitedJigsawManager.assembleJigsawStructure(context, this.startPool, this.size, context.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(this), blockpos, false, this.projectStartToHeightmap, topClipOff, bottomClipOff, null, this.maxDistanceFromCenter, rotationString, this.buryingType, (structurePiecesBuilder, pieces) -> this.postLayoutAdjustments(structurePiecesBuilder, context, offsetY, blockpos, finalTopClipOff, finalBottomClipOff, pieces));
        }
    }

    protected void postLayoutAdjustments(StructurePiecesBuilder structurePiecesBuilder, Structure.GenerationContext context, int offsetY, BlockPos blockpos, int topClipOff, int bottomClipOff, List<PoolElementStructurePiece> pieces) {
        GeneralUtils.centerAllPieces(blockpos, pieces);
        if (!this.buryingType.isEmpty()) {
            if (this.buryingType.get() == JigsawStructure.BURYING_TYPE.LOWEST_CORNER) {
                Heightmap.Types heightMapToUse = (Heightmap.Types) this.projectStartToHeightmap.orElse(Heightmap.Types.WORLD_SURFACE_WG);
                BoundingBox box = ((PoolElementStructurePiece) pieces.get(0)).m_73547_();
                int highestLandPos = context.chunkGenerator().getFirstOccupiedHeight(box.minX(), box.minZ(), heightMapToUse, context.heightAccessor(), context.randomState());
                highestLandPos = Math.min(highestLandPos, context.chunkGenerator().getFirstOccupiedHeight(box.minX(), box.maxZ(), heightMapToUse, context.heightAccessor(), context.randomState()));
                highestLandPos = Math.min(highestLandPos, context.chunkGenerator().getFirstOccupiedHeight(box.maxX(), box.minZ(), heightMapToUse, context.heightAccessor(), context.randomState()));
                highestLandPos = Math.min(highestLandPos, context.chunkGenerator().getFirstOccupiedHeight(box.maxX(), box.maxZ(), heightMapToUse, context.heightAccessor(), context.randomState()));
                if (this.cannotSpawnInLiquid || heightMapToUse != Heightmap.Types.OCEAN_FLOOR_WG && heightMapToUse != Heightmap.Types.OCEAN_FLOOR) {
                    highestLandPos = Math.max(highestLandPos, context.chunkGenerator().getSeaLevel());
                } else {
                    int maxHeightForSubmerging = context.chunkGenerator().getSeaLevel() - box.getYSpan();
                    highestLandPos = Math.min(highestLandPos, maxHeightForSubmerging);
                }
                this.offsetToNewHeight(context, offsetY, pieces, box, highestLandPos);
            } else if (this.buryingType.get() == JigsawStructure.BURYING_TYPE.AVERAGE_LAND) {
                BoundingBox box = ((PoolElementStructurePiece) pieces.get(0)).m_73547_();
                BlockPos centerPos = new BlockPos(box.getCenter());
                int radius = (int) Math.sqrt((double) (box.getLength().getX() * box.getLength().getX() + box.getLength().getZ() * box.getLength().getZ())) / 2;
                Heightmap.Types heightMapToUse = (Heightmap.Types) this.projectStartToHeightmap.orElse(Heightmap.Types.WORLD_SURFACE_WG);
                List<Integer> landHeights = new ArrayList();
                for (int xOffset = -radius; xOffset <= radius; xOffset += radius / 2) {
                    for (int zOffset = -radius; zOffset <= radius; zOffset += radius / 2) {
                        int landHeight = context.chunkGenerator().getFirstOccupiedHeight(centerPos.m_123341_() + xOffset, centerPos.m_123343_() + zOffset, heightMapToUse, context.heightAccessor(), context.randomState());
                        landHeights.add(landHeight);
                    }
                }
                OptionalDouble avgHeightOptional = landHeights.stream().filter(height -> height > (Integer) this.minYAllowed.orElse(Integer.MIN_VALUE) && height < (Integer) this.maxYAllowed.orElse(Integer.MAX_VALUE)).mapToInt(Integer::intValue).average();
                if (this.maxYAllowed.isPresent() && avgHeightOptional.isEmpty()) {
                    avgHeightOptional = OptionalDouble.of((double) ((Integer) this.maxYAllowed.get()).intValue());
                }
                if (this.minYAllowed.isPresent() && avgHeightOptional.isEmpty()) {
                    avgHeightOptional = OptionalDouble.of((double) ((Integer) this.minYAllowed.get()).intValue());
                }
                if (avgHeightOptional.isPresent()) {
                    double avgHeight = avgHeightOptional.getAsDouble();
                    if (this.cannotSpawnInLiquid && heightMapToUse != Heightmap.Types.OCEAN_FLOOR_WG && heightMapToUse != Heightmap.Types.OCEAN_FLOOR) {
                        avgHeight = Math.max(avgHeight, (double) context.chunkGenerator().getSeaLevel());
                        if (this.maxYAllowed.isPresent()) {
                            avgHeight = Math.max(avgHeight, (double) ((Integer) this.maxYAllowed.get()).intValue());
                        }
                    }
                    int parentHeight = ((PoolElementStructurePiece) pieces.get(0)).m_73547_().minY();
                    int offsetAmount = (int) avgHeight - parentHeight + offsetY;
                    pieces.forEach(child -> GeneralUtils.movePieceProperly(child, 0, offsetAmount, 0));
                } else {
                    pieces.clear();
                }
            } else if (this.buryingType.get() == JigsawStructure.BURYING_TYPE.LOWEST_SIDE) {
                Heightmap.Types heightMapToUse = (Heightmap.Types) this.projectStartToHeightmap.orElse(Heightmap.Types.WORLD_SURFACE_WG);
                BoundingBox box = ((PoolElementStructurePiece) pieces.get(0)).m_73547_();
                BlockPos centerPos = box.getCenter();
                int highestLandPos = Integer.MAX_VALUE;
                highestLandPos = this.terrainHeight(context, heightMapToUse, box.minX(), centerPos.m_123343_(), this.minYAllowed, highestLandPos);
                highestLandPos = this.terrainHeight(context, heightMapToUse, centerPos.m_123341_(), box.maxZ(), this.minYAllowed, highestLandPos);
                highestLandPos = this.terrainHeight(context, heightMapToUse, centerPos.m_123341_(), box.minZ(), this.minYAllowed, highestLandPos);
                highestLandPos = this.terrainHeight(context, heightMapToUse, box.maxX(), centerPos.m_123343_(), this.minYAllowed, highestLandPos);
                if (this.minYAllowed.isPresent() && highestLandPos == Integer.MAX_VALUE) {
                    highestLandPos = (Integer) this.minYAllowed.get();
                }
                if (this.cannotSpawnInLiquid || heightMapToUse != Heightmap.Types.OCEAN_FLOOR_WG && heightMapToUse != Heightmap.Types.OCEAN_FLOOR) {
                    highestLandPos = Math.max(highestLandPos, context.chunkGenerator().getSeaLevel());
                } else {
                    int maxHeightForSubmerging = context.chunkGenerator().getSeaLevel() - box.getYSpan();
                    highestLandPos = Math.min(highestLandPos, maxHeightForSubmerging);
                }
                this.offsetToNewHeight(context, offsetY, pieces, box, highestLandPos);
            }
        }
    }

    private int terrainHeight(Structure.GenerationContext context, Heightmap.Types heightMapToUse, int x, int z, Optional<Integer> minYAllowed, int highestLandPos) {
        int landPos = context.chunkGenerator().getFirstOccupiedHeight(x, z, heightMapToUse, context.heightAccessor(), context.randomState());
        if (minYAllowed.isPresent()) {
            if (landPos >= (Integer) minYAllowed.get()) {
                highestLandPos = landPos;
            }
        } else {
            highestLandPos = Math.min(highestLandPos, landPos);
        }
        return highestLandPos;
    }

    private void offsetToNewHeight(Structure.GenerationContext context, int offsetY, List<PoolElementStructurePiece> pieces, BoundingBox box, int highestLandPos) {
        if (this.maxYAllowed.isPresent() && box.maxY() + offsetY < (Integer) this.minYAllowed.get()) {
            highestLandPos = Math.max(highestLandPos, (Integer) this.maxYAllowed.get());
        }
        if (this.minYAllowed.isPresent() && box.minY() + offsetY < (Integer) this.minYAllowed.get()) {
            highestLandPos = Math.min(highestLandPos, (Integer) this.minYAllowed.get());
        }
        WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
        random.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        int heightDiff = highestLandPos - box.minY();
        for (StructurePiece structurePiece : pieces) {
            GeneralUtils.movePieceProperly(structurePiece, 0, heightDiff + offsetY, 0);
        }
    }

    @NotNull
    @Override
    public BoundingBox adjustBoundingBox(@NotNull BoundingBox boundingBox) {
        return super.adjustBoundingBox(boundingBox).inflatedBy(this.enhancedTerrainAdaptation.getKernelRadius());
    }

    @Override
    public StructureType<?> type() {
        return IAStructures.JIGSAW_STRUCTURE.get();
    }

    public static enum BURYING_TYPE implements StringRepresentable {

        LOWEST_CORNER("LOWEST_CORNER"), AVERAGE_LAND("AVERAGE_LAND"), LOWEST_SIDE("LOWEST_SIDE");

        private final String name;

        private static final Map<String, JigsawStructure.BURYING_TYPE> BY_NAME = Util.make(Maps.newHashMap(), hashMap -> {
            JigsawStructure.BURYING_TYPE[] var1 = values();
            for (JigsawStructure.BURYING_TYPE type : var1) {
                hashMap.put(type.name, type);
            }
        });

        private BURYING_TYPE(String name) {
            this.name = name;
        }

        public static JigsawStructure.BURYING_TYPE byName(String name) {
            return (JigsawStructure.BURYING_TYPE) BY_NAME.get(name.toUpperCase(Locale.ROOT));
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}