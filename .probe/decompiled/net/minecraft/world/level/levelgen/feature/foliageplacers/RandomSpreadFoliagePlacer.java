package net.minecraft.world.level.levelgen.feature.foliageplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class RandomSpreadFoliagePlacer extends FoliagePlacer {

    public static final Codec<RandomSpreadFoliagePlacer> CODEC = RecordCodecBuilder.create(p_161522_ -> m_68573_(p_161522_).and(p_161522_.group(IntProvider.codec(1, 512).fieldOf("foliage_height").forGetter(p_161537_ -> p_161537_.foliageHeight), Codec.intRange(0, 256).fieldOf("leaf_placement_attempts").forGetter(p_161524_ -> p_161524_.leafPlacementAttempts))).apply(p_161522_, RandomSpreadFoliagePlacer::new));

    private final IntProvider foliageHeight;

    private final int leafPlacementAttempts;

    public RandomSpreadFoliagePlacer(IntProvider intProvider0, IntProvider intProvider1, IntProvider intProvider2, int int3) {
        super(intProvider0, intProvider1);
        this.foliageHeight = intProvider2;
        this.leafPlacementAttempts = int3;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.RANDOM_SPREAD_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader0, FoliagePlacer.FoliageSetter foliagePlacerFoliageSetter1, RandomSource randomSource2, TreeConfiguration treeConfiguration3, int int4, FoliagePlacer.FoliageAttachment foliagePlacerFoliageAttachment5, int int6, int int7, int int8) {
        BlockPos $$9 = foliagePlacerFoliageAttachment5.pos();
        BlockPos.MutableBlockPos $$10 = $$9.mutable();
        for (int $$11 = 0; $$11 < this.leafPlacementAttempts; $$11++) {
            $$10.setWithOffset($$9, randomSource2.nextInt(int7) - randomSource2.nextInt(int7), randomSource2.nextInt(int6) - randomSource2.nextInt(int6), randomSource2.nextInt(int7) - randomSource2.nextInt(int7));
            m_272253_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$10);
        }
    }

    @Override
    public int foliageHeight(RandomSource randomSource0, int int1, TreeConfiguration treeConfiguration2) {
        return this.foliageHeight.sample(randomSource0);
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource0, int int1, int int2, int int3, int int4, boolean boolean5) {
        return false;
    }
}