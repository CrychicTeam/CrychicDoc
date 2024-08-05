package net.minecraft.world.level.levelgen.feature.foliageplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class MegaPineFoliagePlacer extends FoliagePlacer {

    public static final Codec<MegaPineFoliagePlacer> CODEC = RecordCodecBuilder.create(p_68664_ -> m_68573_(p_68664_).and(IntProvider.codec(0, 24).fieldOf("crown_height").forGetter(p_161484_ -> p_161484_.crownHeight)).apply(p_68664_, MegaPineFoliagePlacer::new));

    private final IntProvider crownHeight;

    public MegaPineFoliagePlacer(IntProvider intProvider0, IntProvider intProvider1, IntProvider intProvider2) {
        super(intProvider0, intProvider1);
        this.crownHeight = intProvider2;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.MEGA_PINE_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader0, FoliagePlacer.FoliageSetter foliagePlacerFoliageSetter1, RandomSource randomSource2, TreeConfiguration treeConfiguration3, int int4, FoliagePlacer.FoliageAttachment foliagePlacerFoliageAttachment5, int int6, int int7, int int8) {
        BlockPos $$9 = foliagePlacerFoliageAttachment5.pos();
        int $$10 = 0;
        for (int $$11 = $$9.m_123342_() - int6 + int8; $$11 <= $$9.m_123342_() + int8; $$11++) {
            int $$12 = $$9.m_123342_() - $$11;
            int $$13 = int7 + foliagePlacerFoliageAttachment5.radiusOffset() + Mth.floor((float) $$12 / (float) int6 * 3.5F);
            int $$14;
            if ($$12 > 0 && $$13 == $$10 && ($$11 & 1) == 0) {
                $$14 = $$13 + 1;
            } else {
                $$14 = $$13;
            }
            this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, new BlockPos($$9.m_123341_(), $$11, $$9.m_123343_()), $$14, 0, foliagePlacerFoliageAttachment5.doubleTrunk());
            $$10 = $$13;
        }
    }

    @Override
    public int foliageHeight(RandomSource randomSource0, int int1, TreeConfiguration treeConfiguration2) {
        return this.crownHeight.sample(randomSource0);
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource0, int int1, int int2, int int3, int int4, boolean boolean5) {
        return int1 + int3 >= 7 ? true : int1 * int1 + int3 * int3 > int4 * int4;
    }
}