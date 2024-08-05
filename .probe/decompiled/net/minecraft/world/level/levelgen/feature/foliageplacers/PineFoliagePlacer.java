package net.minecraft.world.level.levelgen.feature.foliageplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class PineFoliagePlacer extends FoliagePlacer {

    public static final Codec<PineFoliagePlacer> CODEC = RecordCodecBuilder.create(p_68698_ -> m_68573_(p_68698_).and(IntProvider.codec(0, 24).fieldOf("height").forGetter(p_161500_ -> p_161500_.height)).apply(p_68698_, PineFoliagePlacer::new));

    private final IntProvider height;

    public PineFoliagePlacer(IntProvider intProvider0, IntProvider intProvider1, IntProvider intProvider2) {
        super(intProvider0, intProvider1);
        this.height = intProvider2;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.PINE_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader0, FoliagePlacer.FoliageSetter foliagePlacerFoliageSetter1, RandomSource randomSource2, TreeConfiguration treeConfiguration3, int int4, FoliagePlacer.FoliageAttachment foliagePlacerFoliageAttachment5, int int6, int int7, int int8) {
        int $$9 = 0;
        for (int $$10 = int8; $$10 >= int8 - int6; $$10--) {
            this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, foliagePlacerFoliageAttachment5.pos(), $$9, $$10, foliagePlacerFoliageAttachment5.doubleTrunk());
            if ($$9 >= 1 && $$10 == int8 - int6 + 1) {
                $$9--;
            } else if ($$9 < int7 + foliagePlacerFoliageAttachment5.radiusOffset()) {
                $$9++;
            }
        }
    }

    @Override
    public int foliageRadius(RandomSource randomSource0, int int1) {
        return super.foliageRadius(randomSource0, int1) + randomSource0.nextInt(Math.max(int1 + 1, 1));
    }

    @Override
    public int foliageHeight(RandomSource randomSource0, int int1, TreeConfiguration treeConfiguration2) {
        return this.height.sample(randomSource0);
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource0, int int1, int int2, int int3, int int4, boolean boolean5) {
        return int1 == int4 && int3 == int4 && int4 > 0;
    }
}