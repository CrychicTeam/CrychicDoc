package com.craisinlord.integrated_api.world.structures;

import com.craisinlord.integrated_api.modinit.IAStructures;
import com.craisinlord.integrated_api.utils.GeneralUtils;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptation;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptationType;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class NetherJigsawStructure extends JigsawStructure {

    public static final Codec<NetherJigsawStructure> CODEC = RecordCodecBuilder.create(instance -> instance.group(m_226567_(instance), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool), Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size), Codec.INT.optionalFieldOf("min_y_allowed").forGetter(structure -> structure.minYAllowed), Codec.INT.optionalFieldOf("max_y_allowed").forGetter(structure -> structure.maxYAllowed), Codec.intRange(1, 1000).optionalFieldOf("allowed_y_range_from_start").forGetter(structure -> structure.allowedYRangeFromStart), HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight), Codec.BOOL.fieldOf("cannot_spawn_in_liquid").orElse(false).forGetter(structure -> structure.cannotSpawnInLiquid), Codec.intRange(1, 100).optionalFieldOf("valid_biome_radius_check").forGetter(structure -> structure.biomeRadius), Codec.intRange(1, 512).optionalFieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter), Codec.intRange(0, 100).optionalFieldOf("ledge_offset_y").forGetter(structure -> structure.ledgeOffsetY), StringRepresentable.fromEnum(NetherJigsawStructure.LAND_SEARCH_DIRECTION::values).fieldOf("land_search_direction").forGetter(structure -> structure.searchDirection), Codec.BOOL.fieldOf("rotation_fixed").orElse(false).forGetter(structure -> structure.rotationFixed), EnhancedTerrainAdaptationType.ADAPTATION_CODEC.optionalFieldOf("enhanced_terrain_adaptation", EnhancedTerrainAdaptation.NONE).forGetter(structure -> structure.enhancedTerrainAdaptation)).apply(instance, NetherJigsawStructure::new));

    public final Optional<Integer> ledgeOffsetY;

    public final NetherJigsawStructure.LAND_SEARCH_DIRECTION searchDirection;

    public NetherJigsawStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> startPool, int size, Optional<Integer> minYAllowed, Optional<Integer> maxYAllowed, Optional<Integer> allowedYRangeFromStart, HeightProvider startHeight, boolean cannotSpawnInLiquid, Optional<Integer> biomeRadius, Optional<Integer> maxDistanceFromCenter, Optional<Integer> ledgeOffsetY, NetherJigsawStructure.LAND_SEARCH_DIRECTION searchDirection, boolean rotationFixed, EnhancedTerrainAdaptation enhancedTerrainAdaptation) {
        super(config, startPool, size, minYAllowed, maxYAllowed, allowedYRangeFromStart, startHeight, Optional.empty(), cannotSpawnInLiquid, Optional.empty(), Optional.empty(), biomeRadius, maxDistanceFromCenter, Optional.empty(), rotationFixed, enhancedTerrainAdaptation);
        this.ledgeOffsetY = ledgeOffsetY;
        this.searchDirection = searchDirection;
    }

    @Override
    protected void postLayoutAdjustments(StructurePiecesBuilder structurePiecesBuilder, Structure.GenerationContext context, int offsetY, BlockPos blockpos, int topClipOff, int bottomClipOff, List<PoolElementStructurePiece> pieces) {
        GeneralUtils.centerAllPieces(blockpos, pieces);
        WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
        random.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        BlockPos placementPos;
        if (this.searchDirection == NetherJigsawStructure.LAND_SEARCH_DIRECTION.HIGHEST_LAND) {
            placementPos = GeneralUtils.getHighestLand(context.chunkGenerator(), context.randomState(), structurePiecesBuilder.getBoundingBox(), context.heightAccessor(), !this.cannotSpawnInLiquid);
        } else {
            placementPos = GeneralUtils.getLowestLand(context.chunkGenerator(), context.randomState(), structurePiecesBuilder.getBoundingBox(), context.heightAccessor(), !this.cannotSpawnInLiquid);
        }
        if (placementPos.m_123342_() < GeneralUtils.getMaxTerrainLimit(context.chunkGenerator()) && placementPos.m_123342_() > context.chunkGenerator().getSeaLevel() + 1) {
            int yDiff = placementPos.m_123342_() + (Integer) this.ledgeOffsetY.orElse(0) - ((PoolElementStructurePiece) pieces.get(0)).m_73547_().minY();
            pieces.forEach(piece -> piece.move(0, yDiff, 0));
        } else {
            int yDiff = context.chunkGenerator().getSeaLevel() + (Integer) this.ledgeOffsetY.orElse(0) - ((PoolElementStructurePiece) pieces.get(0)).m_73547_().minY();
            pieces.forEach(piece -> piece.move(0, yDiff, 0));
        }
        pieces.forEach(piece -> piece.move(0, offsetY, 0));
    }

    @Override
    public StructureType<?> type() {
        return IAStructures.NETHER_JIGSAW_STRUCTURE.get();
    }

    public static enum LAND_SEARCH_DIRECTION implements StringRepresentable {

        HIGHEST_LAND("HIGHEST_LAND"), LOWEST_LAND("LOWEST_LAND");

        private final String name;

        private static final Map<String, NetherJigsawStructure.LAND_SEARCH_DIRECTION> BY_NAME = Util.make(Maps.newHashMap(), hashMap -> {
            NetherJigsawStructure.LAND_SEARCH_DIRECTION[] var1 = values();
            for (NetherJigsawStructure.LAND_SEARCH_DIRECTION type : var1) {
                hashMap.put(type.name, type);
            }
        });

        private LAND_SEARCH_DIRECTION(String name) {
            this.name = name;
        }

        public static NetherJigsawStructure.LAND_SEARCH_DIRECTION byName(String name) {
            return (NetherJigsawStructure.LAND_SEARCH_DIRECTION) BY_NAME.get(name.toUpperCase(Locale.ROOT));
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}