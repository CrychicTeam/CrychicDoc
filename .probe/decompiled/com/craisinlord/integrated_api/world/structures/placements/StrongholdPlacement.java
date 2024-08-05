package com.craisinlord.integrated_api.world.structures.placements;

import com.craisinlord.integrated_api.modinit.IAStructurePlacementType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public class StrongholdPlacement extends RandomSpreadStructurePlacement {

    public static final Codec<StrongholdPlacement> CODEC = RecordCodecBuilder.create(instance -> instance.group(Vec3i.offsetCodec(16).optionalFieldOf("locate_offset", Vec3i.ZERO).forGetter(rec$ -> ((StrongholdPlacement) rec$).m_227072_()), StructurePlacement.FrequencyReductionMethod.CODEC.optionalFieldOf("frequency_reduction_method", StructurePlacement.FrequencyReductionMethod.DEFAULT).forGetter(rec$ -> ((StrongholdPlacement) rec$).m_227073_()), Codec.floatRange(0.0F, 1.0F).optionalFieldOf("frequency", 1.0F).forGetter(rec$ -> ((StrongholdPlacement) rec$).m_227074_()), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("salt").forGetter(rec$ -> ((StrongholdPlacement) rec$).m_227075_()), StructurePlacement.ExclusionZone.CODEC.optionalFieldOf("exclusion_zone").forGetter(rec$ -> ((StrongholdPlacement) rec$).m_227076_()), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("spacing").forGetter(RandomSpreadStructurePlacement::m_205003_), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("separation").forGetter(RandomSpreadStructurePlacement::m_205004_), RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(RandomSpreadStructurePlacement::m_205005_), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("chunk_distance_to_first_ring").forGetter(StrongholdPlacement::chunkDistanceToFirstRing), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("ring_chunk_thickness").forGetter(StrongholdPlacement::ringChunkThickness), ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max_ring_section").forGetter(StrongholdPlacement::maxRingSection)).apply(instance, instance.stable(StrongholdPlacement::new)));

    private final int chunkDistanceToFirstRing;

    private final int ringChunkThickness;

    private final Optional<Integer> maxRingSection;

    public StrongholdPlacement(Vec3i locateOffset, StructurePlacement.FrequencyReductionMethod frequencyReductionMethod, Float frequency, Integer salt, Optional<StructurePlacement.ExclusionZone> exclusionZone, Integer spacing, Integer separation, RandomSpreadType randomSpreadType, Integer chunkDistanceToFirstRing, Integer ringChunkThickness, Optional<Integer> maxRingSection) {
        super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, randomSpreadType);
        this.chunkDistanceToFirstRing = chunkDistanceToFirstRing;
        this.ringChunkThickness = ringChunkThickness;
        this.maxRingSection = maxRingSection;
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState chunkGeneratorStructureState, int chunkX, int chunkZ) {
        long seed = chunkGeneratorStructureState.getLevelSeed();
        ChunkPos chunkPos = this.m_227008_(seed, chunkX, chunkZ);
        if (chunkPos.x == chunkX && chunkPos.z == chunkZ) {
            int chunkDistance = (int) Math.sqrt((double) (chunkX * chunkX + chunkZ * chunkZ));
            int shiftedChunkDistance = chunkDistance + (this.ringChunkThickness - this.chunkDistanceToFirstRing);
            int ringSection = shiftedChunkDistance / this.ringChunkThickness;
            return this.maxRingSection.isPresent() && ringSection > this.maxRingSection.get() ? false : ringSection % 2 == 1;
        } else {
            return false;
        }
    }

    @Override
    public StructurePlacementType<?> type() {
        return (StructurePlacementType<?>) IAStructurePlacementType.STRONGHOLD_PLACEMENT;
    }

    public int chunkDistanceToFirstRing() {
        return this.chunkDistanceToFirstRing;
    }

    public int ringChunkThickness() {
        return this.ringChunkThickness;
    }

    public Optional<Integer> maxRingSection() {
        return this.maxRingSection;
    }
}