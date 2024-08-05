package net.minecraft.world.level.levelgen.feature.foliageplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class DarkOakFoliagePlacer extends FoliagePlacer {

    public static final Codec<DarkOakFoliagePlacer> CODEC = RecordCodecBuilder.create(p_68473_ -> m_68573_(p_68473_).apply(p_68473_, DarkOakFoliagePlacer::new));

    public DarkOakFoliagePlacer(IntProvider intProvider0, IntProvider intProvider1) {
        super(intProvider0, intProvider1);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.DARK_OAK_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader0, FoliagePlacer.FoliageSetter foliagePlacerFoliageSetter1, RandomSource randomSource2, TreeConfiguration treeConfiguration3, int int4, FoliagePlacer.FoliageAttachment foliagePlacerFoliageAttachment5, int int6, int int7, int int8) {
        BlockPos $$9 = foliagePlacerFoliageAttachment5.pos().above(int8);
        boolean $$10 = foliagePlacerFoliageAttachment5.doubleTrunk();
        if ($$10) {
            this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$9, int7 + 2, -1, $$10);
            this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$9, int7 + 3, 0, $$10);
            this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$9, int7 + 2, 1, $$10);
            if (randomSource2.nextBoolean()) {
                this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$9, int7, 2, $$10);
            }
        } else {
            this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$9, int7 + 2, -1, $$10);
            this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$9, int7 + 1, 0, $$10);
        }
    }

    @Override
    public int foliageHeight(RandomSource randomSource0, int int1, TreeConfiguration treeConfiguration2) {
        return 4;
    }

    @Override
    protected boolean shouldSkipLocationSigned(RandomSource randomSource0, int int1, int int2, int int3, int int4, boolean boolean5) {
        return int2 != 0 || !boolean5 || int1 != -int4 && int1 < int4 || int3 != -int4 && int3 < int4 ? super.shouldSkipLocationSigned(randomSource0, int1, int2, int3, int4, boolean5) : true;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource0, int int1, int int2, int int3, int int4, boolean boolean5) {
        if (int2 == -1 && !boolean5) {
            return int1 == int4 && int3 == int4;
        } else {
            return int2 == 1 ? int1 + int3 > int4 * 2 - 2 : false;
        }
    }
}