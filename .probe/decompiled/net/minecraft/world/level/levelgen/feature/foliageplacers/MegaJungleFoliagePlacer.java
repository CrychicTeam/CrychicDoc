package net.minecraft.world.level.levelgen.feature.foliageplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class MegaJungleFoliagePlacer extends FoliagePlacer {

    public static final Codec<MegaJungleFoliagePlacer> CODEC = RecordCodecBuilder.create(p_68630_ -> m_68573_(p_68630_).and(Codec.intRange(0, 16).fieldOf("height").forGetter(p_161468_ -> p_161468_.height)).apply(p_68630_, MegaJungleFoliagePlacer::new));

    protected final int height;

    public MegaJungleFoliagePlacer(IntProvider intProvider0, IntProvider intProvider1, int int2) {
        super(intProvider0, intProvider1);
        this.height = int2;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.MEGA_JUNGLE_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader0, FoliagePlacer.FoliageSetter foliagePlacerFoliageSetter1, RandomSource randomSource2, TreeConfiguration treeConfiguration3, int int4, FoliagePlacer.FoliageAttachment foliagePlacerFoliageAttachment5, int int6, int int7, int int8) {
        int $$9 = foliagePlacerFoliageAttachment5.doubleTrunk() ? int6 : 1 + randomSource2.nextInt(2);
        for (int $$10 = int8; $$10 >= int8 - $$9; $$10--) {
            int $$11 = int7 + foliagePlacerFoliageAttachment5.radiusOffset() + 1 - $$10;
            this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, foliagePlacerFoliageAttachment5.pos(), $$11, $$10, foliagePlacerFoliageAttachment5.doubleTrunk());
        }
    }

    @Override
    public int foliageHeight(RandomSource randomSource0, int int1, TreeConfiguration treeConfiguration2) {
        return this.height;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource0, int int1, int int2, int int3, int int4, boolean boolean5) {
        return int1 + int3 >= 7 ? true : int1 * int1 + int3 * int3 > int4 * int4;
    }
}