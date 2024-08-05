package net.minecraft.world.level.levelgen.feature.foliageplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class BushFoliagePlacer extends BlobFoliagePlacer {

    public static final Codec<BushFoliagePlacer> CODEC = RecordCodecBuilder.create(p_68454_ -> m_68413_(p_68454_).apply(p_68454_, BushFoliagePlacer::new));

    public BushFoliagePlacer(IntProvider intProvider0, IntProvider intProvider1, int int2) {
        super(intProvider0, intProvider1, int2);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.BUSH_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader0, FoliagePlacer.FoliageSetter foliagePlacerFoliageSetter1, RandomSource randomSource2, TreeConfiguration treeConfiguration3, int int4, FoliagePlacer.FoliageAttachment foliagePlacerFoliageAttachment5, int int6, int int7, int int8) {
        for (int $$9 = int8; $$9 >= int8 - int6; $$9--) {
            int $$10 = int7 + foliagePlacerFoliageAttachment5.radiusOffset() - 1 - $$9;
            this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, foliagePlacerFoliageAttachment5.pos(), $$10, $$9, foliagePlacerFoliageAttachment5.doubleTrunk());
        }
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource0, int int1, int int2, int int3, int int4, boolean boolean5) {
        return int1 == int4 && int3 == int4 && randomSource0.nextInt(2) == 0;
    }
}