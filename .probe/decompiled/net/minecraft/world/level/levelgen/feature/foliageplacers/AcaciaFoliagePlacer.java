package net.minecraft.world.level.levelgen.feature.foliageplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class AcaciaFoliagePlacer extends FoliagePlacer {

    public static final Codec<AcaciaFoliagePlacer> CODEC = RecordCodecBuilder.create(p_68380_ -> m_68573_(p_68380_).apply(p_68380_, AcaciaFoliagePlacer::new));

    public AcaciaFoliagePlacer(IntProvider intProvider0, IntProvider intProvider1) {
        super(intProvider0, intProvider1);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.ACACIA_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader0, FoliagePlacer.FoliageSetter foliagePlacerFoliageSetter1, RandomSource randomSource2, TreeConfiguration treeConfiguration3, int int4, FoliagePlacer.FoliageAttachment foliagePlacerFoliageAttachment5, int int6, int int7, int int8) {
        boolean $$9 = foliagePlacerFoliageAttachment5.doubleTrunk();
        BlockPos $$10 = foliagePlacerFoliageAttachment5.pos().above(int8);
        this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$10, int7 + foliagePlacerFoliageAttachment5.radiusOffset(), -1 - int6, $$9);
        this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$10, int7 - 1, -int6, $$9);
        this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$10, int7 + foliagePlacerFoliageAttachment5.radiusOffset() - 1, 0, $$9);
    }

    @Override
    public int foliageHeight(RandomSource randomSource0, int int1, TreeConfiguration treeConfiguration2) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource0, int int1, int int2, int int3, int int4, boolean boolean5) {
        return int2 == 0 ? (int1 > 1 || int3 > 1) && int1 != 0 && int3 != 0 : int1 == int4 && int3 == int4 && int4 > 0;
    }
}