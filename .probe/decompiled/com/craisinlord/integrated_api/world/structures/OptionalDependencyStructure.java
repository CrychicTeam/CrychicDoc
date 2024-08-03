package com.craisinlord.integrated_api.world.structures;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.modinit.IAStructures;
import com.craisinlord.integrated_api.utils.PlatformHooks;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptation;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptationType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.CheckerboardColumnBiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class OptionalDependencyStructure extends JigsawStructure {

    public static final Codec<OptionalDependencyStructure> CODEC = RecordCodecBuilder.create(instance -> instance.group(m_226567_(instance), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool), Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size), Codec.INT.optionalFieldOf("min_y_allowed").forGetter(structure -> structure.minYAllowed), Codec.INT.optionalFieldOf("max_y_allowed").forGetter(structure -> structure.maxYAllowed), HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap), Codec.BOOL.fieldOf("cannot_spawn_in_liquid").orElse(false).forGetter(structure -> structure.cannotSpawnInLiquid), Codec.intRange(1, 100).optionalFieldOf("terrain_height_radius_check").forGetter(structure -> structure.terrainHeightCheckRadius), Codec.intRange(1, 1000).optionalFieldOf("allowed_terrain_height_range").forGetter(structure -> structure.allowedTerrainHeightRange), Codec.intRange(1, 100).optionalFieldOf("valid_biome_radius_check").forGetter(structure -> structure.biomeRadius), Codec.intRange(1, 512).optionalFieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter), Codec.STRING.optionalFieldOf("required_mods").forGetter(structure -> structure.requiredMod), Codec.STRING.optionalFieldOf("illegal_mods").forGetter(structure -> structure.illegalMod), Codec.BOOL.fieldOf("rotation_fixed").orElse(false).forGetter(structure -> structure.rotationFixed), EnhancedTerrainAdaptationType.ADAPTATION_CODEC.optionalFieldOf("enhanced_terrain_adaptation", EnhancedTerrainAdaptation.NONE).forGetter(structure -> structure.enhancedTerrainAdaptation)).apply(instance, OptionalDependencyStructure::new));

    public final Optional<String> requiredMod;

    public final Optional<String> illegalMod;

    public OptionalDependencyStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> startPool, int size, Optional<Integer> minYAllowed, Optional<Integer> maxYAllowed, HeightProvider startHeight, Optional<Heightmap.Types> projectStartToHeightmap, boolean cannotSpawnInLiquid, Optional<Integer> terrainHeightCheckRadius, Optional<Integer> allowedTerrainHeightRange, Optional<Integer> biomeRadius, Optional<Integer> maxDistanceFromCenter, Optional<String> requiredMod, Optional<String> illegalMod, boolean rotationFixed, EnhancedTerrainAdaptation enhancedTerrainAdaptation) {
        super(config, startPool, size, minYAllowed, maxYAllowed, Optional.empty(), startHeight, projectStartToHeightmap, cannotSpawnInLiquid, terrainHeightCheckRadius, allowedTerrainHeightRange, biomeRadius, maxDistanceFromCenter, Optional.empty(), rotationFixed, enhancedTerrainAdaptation);
        this.requiredMod = requiredMod;
        this.illegalMod = illegalMod;
    }

    private ArrayList<String> convertModList(String modlist) {
        int startChar = 0;
        ArrayList<String> convertedModList = new ArrayList();
        for (int i = 0; i < modlist.length(); i++) {
            if (modlist.charAt(i) == ',') {
                convertedModList.add(modlist.substring(startChar, i));
                startChar = i + 1;
            }
        }
        convertedModList.add(modlist.substring(startChar));
        return convertedModList;
    }

    @Override
    protected boolean extraSpawningChecks(Structure.GenerationContext context, BlockPos blockPos) {
        ChunkPos chunkPos = context.chunkPos();
        if (!this.requiredMod.isEmpty()) {
            ArrayList<String> requiredMods = this.convertModList((String) this.requiredMod.get());
            for (String mod : requiredMods) {
                if (!this.isLoaded(mod)) {
                    IntegratedAPI.LOGGER.debug("Attempted to spawn Integrated API structure but not all required mods " + requiredMods + " are present.");
                    return false;
                }
            }
        }
        if (!this.illegalMod.isEmpty()) {
            ArrayList<String> illegalMods = this.convertModList((String) this.illegalMod.get());
            for (String modx : illegalMods) {
                if (this.isLoaded(modx)) {
                    IntegratedAPI.LOGGER.debug("Attempted to spawn Integrated API structure but illegal mods " + illegalMods + " are present.");
                    return false;
                }
            }
        }
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

    public boolean isLoaded(String name) {
        return PlatformHooks.isModLoaded(name);
    }

    @Override
    public StructureType<?> type() {
        return IAStructures.OPTIONAL_DEPENDENCY_STRUCTURE.get();
    }
}