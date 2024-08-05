package net.minecraft.world.level.levelgen.feature.foliageplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class SpruceFoliagePlacer extends FoliagePlacer {

    public static final Codec<SpruceFoliagePlacer> CODEC = RecordCodecBuilder.create(p_68735_ -> m_68573_(p_68735_).and(IntProvider.codec(0, 24).fieldOf("trunk_height").forGetter(p_161553_ -> p_161553_.trunkHeight)).apply(p_68735_, SpruceFoliagePlacer::new));

    private final IntProvider trunkHeight;

    public SpruceFoliagePlacer(IntProvider intProvider0, IntProvider intProvider1, IntProvider intProvider2) {
        super(intProvider0, intProvider1);
        this.trunkHeight = intProvider2;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.SPRUCE_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader0, FoliagePlacer.FoliageSetter foliagePlacerFoliageSetter1, RandomSource randomSource2, TreeConfiguration treeConfiguration3, int int4, FoliagePlacer.FoliageAttachment foliagePlacerFoliageAttachment5, int int6, int int7, int int8) {
        BlockPos $$9 = foliagePlacerFoliageAttachment5.pos();
        int $$10 = randomSource2.nextInt(2);
        int $$11 = 1;
        int $$12 = 0;
        for (int $$13 = int8; $$13 >= -int6; $$13--) {
            this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$9, $$10, $$13, foliagePlacerFoliageAttachment5.doubleTrunk());
            if ($$10 >= $$11) {
                $$10 = $$12;
                $$12 = 1;
                $$11 = Math.min($$11 + 1, int7 + foliagePlacerFoliageAttachment5.radiusOffset());
            } else {
                $$10++;
            }
        }
    }

    @Override
    public int foliageHeight(RandomSource randomSource0, int int1, TreeConfiguration treeConfiguration2) {
        return Math.max(4, int1 - this.trunkHeight.sample(randomSource0));
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource0, int int1, int int2, int int3, int int4, boolean boolean5) {
        return int1 == int4 && int3 == int4 && int4 > 0;
    }
}