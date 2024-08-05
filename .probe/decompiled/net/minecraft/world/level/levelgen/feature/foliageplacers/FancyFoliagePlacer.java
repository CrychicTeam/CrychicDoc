package net.minecraft.world.level.levelgen.feature.foliageplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class FancyFoliagePlacer extends BlobFoliagePlacer {

    public static final Codec<FancyFoliagePlacer> CODEC = RecordCodecBuilder.create(p_68518_ -> m_68413_(p_68518_).apply(p_68518_, FancyFoliagePlacer::new));

    public FancyFoliagePlacer(IntProvider intProvider0, IntProvider intProvider1, int int2) {
        super(intProvider0, intProvider1, int2);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.FANCY_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader0, FoliagePlacer.FoliageSetter foliagePlacerFoliageSetter1, RandomSource randomSource2, TreeConfiguration treeConfiguration3, int int4, FoliagePlacer.FoliageAttachment foliagePlacerFoliageAttachment5, int int6, int int7, int int8) {
        for (int $$9 = int8; $$9 >= int8 - int6; $$9--) {
            int $$10 = int7 + ($$9 != int8 && $$9 != int8 - int6 ? 1 : 0);
            this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, foliagePlacerFoliageAttachment5.pos(), $$10, $$9, foliagePlacerFoliageAttachment5.doubleTrunk());
        }
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource0, int int1, int int2, int int3, int int4, boolean boolean5) {
        return Mth.square((float) int1 + 0.5F) + Mth.square((float) int3 + 0.5F) > (float) (int4 * int4);
    }
}