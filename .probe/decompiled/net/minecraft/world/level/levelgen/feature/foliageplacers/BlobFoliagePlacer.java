package net.minecraft.world.level.levelgen.feature.foliageplacers;

import com.mojang.datafixers.Products.P3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class BlobFoliagePlacer extends FoliagePlacer {

    public static final Codec<BlobFoliagePlacer> CODEC = RecordCodecBuilder.create(p_68427_ -> blobParts(p_68427_).apply(p_68427_, BlobFoliagePlacer::new));

    protected final int height;

    protected static <P extends BlobFoliagePlacer> P3<Mu<P>, IntProvider, IntProvider, Integer> blobParts(Instance<P> instanceP0) {
        return m_68573_(instanceP0).and(Codec.intRange(0, 16).fieldOf("height").forGetter(p_68412_ -> p_68412_.height));
    }

    public BlobFoliagePlacer(IntProvider intProvider0, IntProvider intProvider1, int int2) {
        super(intProvider0, intProvider1);
        this.height = int2;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.BLOB_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader0, FoliagePlacer.FoliageSetter foliagePlacerFoliageSetter1, RandomSource randomSource2, TreeConfiguration treeConfiguration3, int int4, FoliagePlacer.FoliageAttachment foliagePlacerFoliageAttachment5, int int6, int int7, int int8) {
        for (int $$9 = int8; $$9 >= int8 - int6; $$9--) {
            int $$10 = Math.max(int7 + foliagePlacerFoliageAttachment5.radiusOffset() - 1 - $$9 / 2, 0);
            this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, foliagePlacerFoliageAttachment5.pos(), $$10, $$9, foliagePlacerFoliageAttachment5.doubleTrunk());
        }
    }

    @Override
    public int foliageHeight(RandomSource randomSource0, int int1, TreeConfiguration treeConfiguration2) {
        return this.height;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource0, int int1, int int2, int int3, int int4, boolean boolean5) {
        return int1 == int4 && int3 == int4 && (randomSource0.nextInt(2) == 0 || int2 == 0);
    }
}