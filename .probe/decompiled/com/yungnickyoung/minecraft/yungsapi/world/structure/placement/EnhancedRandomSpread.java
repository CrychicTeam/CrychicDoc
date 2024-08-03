package com.yungnickyoung.minecraft.yungsapi.world.structure.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.yungsapi.module.StructurePlacementTypeModule;
import com.yungnickyoung.minecraft.yungsapi.world.structure.exclusion.EnhancedExclusionZone;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public class EnhancedRandomSpread extends RandomSpreadStructurePlacement {

    public static final Codec<EnhancedRandomSpread> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(Vec3i.offsetCodec(16).optionalFieldOf("locate_offset", Vec3i.ZERO).forGetter(placement -> placement.m_227072_()), StructurePlacement.FrequencyReductionMethod.CODEC.optionalFieldOf("frequency_reduction_method", StructurePlacement.FrequencyReductionMethod.DEFAULT).forGetter(placement -> placement.m_227073_()), Codec.floatRange(0.0F, 1.0F).optionalFieldOf("frequency", 1.0F).forGetter(placement -> placement.m_227074_()), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("salt").forGetter(placement -> placement.m_227075_()), StructurePlacement.ExclusionZone.CODEC.optionalFieldOf("exclusion_zone").forGetter(placement -> placement.m_227076_()), EnhancedExclusionZone.CODEC.optionalFieldOf("enhanced_exclusion_zone").forGetter(placement -> placement.enhancedExclusionZone), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("spacing").forGetter(RandomSpreadStructurePlacement::m_205003_), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("separation").forGetter(RandomSpreadStructurePlacement::m_205004_), RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(RandomSpreadStructurePlacement::m_205005_)).apply(builder, builder.stable(EnhancedRandomSpread::new))).flatXmap(verifySpacing(), DataResult::success).codec();

    private final Optional<EnhancedExclusionZone> enhancedExclusionZone;

    private static Function<EnhancedRandomSpread, DataResult<EnhancedRandomSpread>> verifySpacing() {
        return placement -> placement.m_205003_() <= placement.m_205004_() ? DataResult.error(() -> "EnhancedRandomSpread's spacing has to be larger than separation") : DataResult.success(placement);
    }

    public EnhancedRandomSpread(Vec3i locateOffset, StructurePlacement.FrequencyReductionMethod frequencyReductionMethod, Float frequency, Integer salt, Optional<StructurePlacement.ExclusionZone> exclusionZone, Optional<EnhancedExclusionZone> enhancedExclusionZone, Integer spacing, Integer separation, RandomSpreadType randomSpreadType) {
        super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, randomSpreadType);
        this.enhancedExclusionZone = enhancedExclusionZone;
    }

    @Override
    public StructurePlacementType<?> type() {
        return StructurePlacementTypeModule.ENHANCED_RANDOM_SPREAD;
    }

    @Override
    public boolean isStructureChunk(ChunkGeneratorStructureState chunkGeneratorStructureState, int x, int z) {
        return !super.m_255071_(chunkGeneratorStructureState, x, z) ? false : this.enhancedExclusionZone.isEmpty() || !((EnhancedExclusionZone) this.enhancedExclusionZone.get()).isPlacementForbidden(chunkGeneratorStructureState, x, z);
    }
}