package com.craisinlord.integrated_api.world.structures;

import com.craisinlord.integrated_api.modinit.IAStructures;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptation;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptationType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class OverLavaNetherStructure extends JigsawStructure {

    public static final Codec<OverLavaNetherStructure> CODEC = RecordCodecBuilder.create(instance -> instance.group(m_226567_(instance), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool), Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size), Codec.INT.optionalFieldOf("min_y_allowed").forGetter(structure -> structure.minYAllowed), Codec.INT.optionalFieldOf("max_y_allowed").forGetter(structure -> structure.maxYAllowed), Codec.intRange(1, 1000).optionalFieldOf("allowed_y_range_from_start").forGetter(structure -> structure.allowedYRangeFromStart), HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap), Codec.BOOL.fieldOf("cannot_spawn_in_liquid").orElse(false).forGetter(structure -> structure.cannotSpawnInLiquid), Codec.intRange(1, 100).optionalFieldOf("terrain_height_radius_check").forGetter(structure -> structure.terrainHeightCheckRadius), Codec.intRange(1, 1000).optionalFieldOf("allowed_terrain_height_range").forGetter(structure -> structure.allowedTerrainHeightRange), Codec.intRange(1, 100).optionalFieldOf("valid_biome_radius_check").forGetter(structure -> structure.biomeRadius), Codec.intRange(1, 512).optionalFieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter), StringRepresentable.fromEnum(JigsawStructure.BURYING_TYPE::values).optionalFieldOf("burying_type").forGetter(structure -> structure.buryingType), Codec.BOOL.fieldOf("rotation_fixed").orElse(false).forGetter(structure -> structure.rotationFixed), EnhancedTerrainAdaptationType.ADAPTATION_CODEC.optionalFieldOf("enhanced_terrain_adaptation", EnhancedTerrainAdaptation.NONE).forGetter(structure -> structure.enhancedTerrainAdaptation)).apply(instance, OverLavaNetherStructure::new));

    public OverLavaNetherStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> startPool, int size, Optional<Integer> minYAllowed, Optional<Integer> maxYAllowed, Optional<Integer> allowedYRangeFromStart, HeightProvider startHeight, Optional<Heightmap.Types> projectStartToHeightmap, boolean cannotSpawnInLiquid, Optional<Integer> terrainHeightCheckRadius, Optional<Integer> allowedTerrainHeightRange, Optional<Integer> biomeRadius, Optional<Integer> maxDistanceFromCenter, Optional<JigsawStructure.BURYING_TYPE> buryingType, boolean rotationFixed, EnhancedTerrainAdaptation enhancedTerrainAdaptation) {
        super(config, startPool, size, minYAllowed, maxYAllowed, allowedYRangeFromStart, startHeight, projectStartToHeightmap, cannotSpawnInLiquid, terrainHeightCheckRadius, allowedTerrainHeightRange, biomeRadius, maxDistanceFromCenter, buryingType, rotationFixed, enhancedTerrainAdaptation);
    }

    @Override
    protected boolean extraSpawningChecks(Structure.GenerationContext context, BlockPos blockPos) {
        boolean superCheck = super.extraSpawningChecks(context, blockPos);
        if (!superCheck) {
            return false;
        } else {
            int checkRadius = 16;
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (int xOffset = -checkRadius; xOffset <= checkRadius; xOffset += 8) {
                for (int zOffset = -checkRadius; zOffset <= checkRadius; zOffset += 8) {
                    NoiseColumn blockView = context.chunkGenerator().getBaseColumn(xOffset + blockPos.m_123341_(), zOffset + blockPos.m_123343_(), context.heightAccessor(), context.randomState());
                    for (int yOffset = 0; yOffset <= 30; yOffset += 5) {
                        mutable.set(blockPos).move(xOffset, yOffset, zOffset);
                        BlockState state = blockView.getBlock(mutable.m_123342_());
                        if (!state.m_60795_() && state.m_60819_().isEmpty()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    @Override
    public StructureType<?> type() {
        return IAStructures.OVER_LAVA_NETHER_STRUCTURE.get();
    }
}