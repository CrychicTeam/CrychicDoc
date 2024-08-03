package com.craisinlord.integrated_api.world.structures;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.modinit.IAStructures;
import com.craisinlord.integrated_api.world.structures.pieces.manager.PieceLimitedJigsawManager;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptation;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptationType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.QuartPos;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.CheckerboardColumnBiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class BiomeFacingStructure extends JigsawStructure {

    public static final Codec<BiomeFacingStructure> CODEC = RecordCodecBuilder.create(instance -> instance.group(m_226567_(instance), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool), Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size), Codec.INT.optionalFieldOf("min_y_allowed").forGetter(structure -> structure.minYAllowed), Codec.INT.optionalFieldOf("max_y_allowed").forGetter(structure -> structure.maxYAllowed), Codec.intRange(1, 1000).optionalFieldOf("allowed_y_range_from_start").forGetter(structure -> structure.allowedYRangeFromStart), HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap), Codec.BOOL.fieldOf("cannot_spawn_in_liquid").orElse(false).forGetter(structure -> structure.cannotSpawnInLiquid), Codec.intRange(1, 100).optionalFieldOf("terrain_height_radius_check").forGetter(structure -> structure.terrainHeightCheckRadius), Codec.intRange(1, 1000).optionalFieldOf("allowed_terrain_height_range").forGetter(structure -> structure.allowedTerrainHeightRange), Codec.intRange(1, 100).optionalFieldOf("valid_biome_radius_check").forGetter(structure -> structure.biomeRadius), Codec.intRange(1, 512).optionalFieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter), EnhancedTerrainAdaptationType.ADAPTATION_CODEC.optionalFieldOf("enhanced_terrain_adaptation", EnhancedTerrainAdaptation.NONE).forGetter(structure -> structure.enhancedTerrainAdaptation), Codec.intRange(1, 100).fieldOf("target_biome_radius_check_blocks").orElse(24).forGetter(structure -> structure.targetBiomeRadius), RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("target_biomes").forGetter(structure -> structure.targetBiomes)).apply(instance, BiomeFacingStructure::new));

    private final int targetBiomeRadius;

    private final HolderSet<Biome> targetBiomes;

    public BiomeFacingStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> startPool, int size, Optional<Integer> minYAllowed, Optional<Integer> maxYAllowed, Optional<Integer> allowedYRangeFromStart, HeightProvider startHeight, Optional<Heightmap.Types> projectStartToHeightmap, boolean cannotSpawnInLiquid, Optional<Integer> terrainHeightCheckRadius, Optional<Integer> allowedTerrainHeightRange, Optional<Integer> biomeRadius, Optional<Integer> maxDistanceFromCenter, EnhancedTerrainAdaptation enhancedTerrainAdaptation, int targetBiomeRadius, HolderSet<Biome> targetBiomes) {
        super(config, startPool, size, minYAllowed, maxYAllowed, allowedYRangeFromStart, startHeight, projectStartToHeightmap, cannotSpawnInLiquid, terrainHeightCheckRadius, allowedTerrainHeightRange, biomeRadius, maxDistanceFromCenter, Optional.empty(), false, enhancedTerrainAdaptation);
        this.targetBiomeRadius = targetBiomeRadius;
        this.targetBiomes = targetBiomes;
    }

    private String rotationToBiome(BlockPos blockpos, Structure.GenerationContext context) {
        int validBiomeRange = this.targetBiomeRadius;
        int sectionY = blockpos.m_123342_();
        if (this.projectStartToHeightmap.isPresent()) {
            sectionY += context.chunkGenerator().getFirstOccupiedHeight(blockpos.m_123341_(), blockpos.m_123343_(), (Heightmap.Types) this.projectStartToHeightmap.get(), context.heightAccessor(), context.randomState());
        }
        sectionY = QuartPos.fromBlock(sectionY);
        int posZ = blockpos.m_123343_();
        int posX = blockpos.m_123341_();
        int sectionBiomeRange = (int) Math.ceil((double) (validBiomeRange / 4)) + 2;
        for (Holder<Biome> biome : context.biomeSource().getBiomesWithin(posX - sectionBiomeRange, sectionY, posZ - 2 * sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biome)) {
                return "NONE";
            }
        }
        for (Holder<Biome> biomex : context.biomeSource().getBiomesWithin(posX + sectionBiomeRange, sectionY, posZ - 2 * sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomex)) {
                return "NONE";
            }
        }
        for (Holder<Biome> biomexx : context.biomeSource().getBiomesWithin(posX + 2 * sectionBiomeRange, sectionY, posZ + sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomexx)) {
                return "CLOCKWISE_90";
            }
        }
        for (Holder<Biome> biomexxx : context.biomeSource().getBiomesWithin(posX + 2 * sectionBiomeRange, sectionY, posZ - sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomexxx)) {
                return "CLOCKWISE_90";
            }
        }
        for (Holder<Biome> biomexxxx : context.biomeSource().getBiomesWithin(posX - sectionBiomeRange, sectionY, posZ + 2 * sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomexxxx)) {
                return "CLOCKWISE_180";
            }
        }
        for (Holder<Biome> biomexxxxx : context.biomeSource().getBiomesWithin(posX + sectionBiomeRange, sectionY, posZ + 2 * sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomexxxxx)) {
                return "CLOCKWISE_180";
            }
        }
        for (Holder<Biome> biomexxxxxx : context.biomeSource().getBiomesWithin(posX - 2 * sectionBiomeRange, sectionY, posZ - sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomexxxxxx)) {
                return "COUNTERCLOCKWISE_90";
            }
        }
        for (Holder<Biome> biomexxxxxxx : context.biomeSource().getBiomesWithin(posX - 2 * sectionBiomeRange, sectionY, posZ + sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomexxxxxxx)) {
                return "COUNTERCLOCKWISE_90";
            }
        }
        for (Holder<Biome> biomexxxxxxxx : context.biomeSource().getBiomesWithin(posX - sectionBiomeRange, sectionY, posZ - sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomexxxxxxxx)) {
                return "COUNTERCLOCKWISE_90";
            }
        }
        for (Holder<Biome> biomexxxxxxxxx : context.biomeSource().getBiomesWithin(posX + sectionBiomeRange, sectionY, posZ - sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomexxxxxxxxx)) {
                return "CLOCKWISE_90";
            }
        }
        for (Holder<Biome> biomexxxxxxxxxx : context.biomeSource().getBiomesWithin(posX + sectionBiomeRange, sectionY, posZ + sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomexxxxxxxxxx)) {
                return "CLOCKWISE_90";
            }
        }
        for (Holder<Biome> biomexxxxxxxxxxx : context.biomeSource().getBiomesWithin(posX - sectionBiomeRange, sectionY, posZ + sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomexxxxxxxxxxx)) {
                return "COUNTERCLOCKWISE_90";
            }
        }
        for (Holder<Biome> biomexxxxxxxxxxxx : context.biomeSource().getBiomesWithin(posX - 2 * sectionBiomeRange, sectionY, posZ - 2 * sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomexxxxxxxxxxxx)) {
                return "NONE";
            }
        }
        for (Holder<Biome> biomexxxxxxxxxxxxx : context.biomeSource().getBiomesWithin(posX + 2 * sectionBiomeRange, sectionY, posZ - 2 * sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomexxxxxxxxxxxxx)) {
                return "NONE";
            }
        }
        for (Holder<Biome> biomexxxxxxxxxxxxxx : context.biomeSource().getBiomesWithin(posX + 2 * sectionBiomeRange, sectionY, posZ + 2 * sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomexxxxxxxxxxxxxx)) {
                return "CLOCKWISE_180";
            }
        }
        for (Holder<Biome> biomexxxxxxxxxxxxxxx : context.biomeSource().getBiomesWithin(posX - 2 * sectionBiomeRange, sectionY, posZ + 2 * sectionBiomeRange, sectionBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biomexxxxxxxxxxxxxxx)) {
                return "CLOCKWISE_180";
            }
        }
        IntegratedAPI.LOGGER.info("EVIL DETECTED YOU WILL BE EXTERMINATED");
        return "NONE";
    }

    private boolean checkBiome(BlockPos blockpos, Structure.GenerationContext context) {
        int validBiomeRange = this.targetBiomeRadius - 4;
        int sectionY = blockpos.m_123342_();
        if (this.projectStartToHeightmap.isPresent()) {
            sectionY += context.chunkGenerator().getFirstOccupiedHeight(blockpos.m_123341_(), blockpos.m_123343_(), (Heightmap.Types) this.projectStartToHeightmap.get(), context.heightAccessor(), context.randomState());
        }
        sectionY = QuartPos.fromBlock(sectionY);
        int posZ = blockpos.m_123343_();
        int posX = blockpos.m_123341_();
        for (Holder<Biome> biome : context.biomeSource().getBiomesWithin(posX, sectionY, posZ, validBiomeRange, context.randomState().sampler())) {
            if (this.targetBiomes.contains(biome)) {
                return true;
            }
        }
        return false;
    }

    @Override
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
        if (!this.checkBiome(blockPos, context)) {
            return false;
        } else {
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
            String rotationString = this.rotationToBiome(blockpos, context);
            if (rotationString == null) {
                throw new RuntimeException(new Exception("Integrated API Found Null Rotation for Biome Facing Structure. REPORT THIS TO CRAISIN!"));
            } else {
                int finalTopClipOff = topClipOff;
                int finalBottomClipOff = bottomClipOff;
                return PieceLimitedJigsawManager.assembleJigsawStructure(context, this.startPool, this.size, context.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(this), blockpos, false, this.projectStartToHeightmap, topClipOff, bottomClipOff, null, this.maxDistanceFromCenter, rotationString, this.buryingType, (structurePiecesBuilder, pieces) -> this.postLayoutAdjustments(structurePiecesBuilder, context, offsetY, blockpos, finalTopClipOff, finalBottomClipOff, pieces));
            }
        }
    }

    @Override
    public StructureType<?> type() {
        return IAStructures.BIOME_FACING_STRUCTURE.get();
    }
}