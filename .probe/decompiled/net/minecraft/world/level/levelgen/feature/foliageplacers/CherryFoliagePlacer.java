package net.minecraft.world.level.levelgen.feature.foliageplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class CherryFoliagePlacer extends FoliagePlacer {

    public static final Codec<CherryFoliagePlacer> CODEC = RecordCodecBuilder.create(p_273246_ -> m_68573_(p_273246_).and(p_273246_.group(IntProvider.codec(4, 16).fieldOf("height").forGetter(p_273527_ -> p_273527_.height), Codec.floatRange(0.0F, 1.0F).fieldOf("wide_bottom_layer_hole_chance").forGetter(p_273760_ -> p_273760_.wideBottomLayerHoleChance), Codec.floatRange(0.0F, 1.0F).fieldOf("corner_hole_chance").forGetter(p_273020_ -> p_273020_.wideBottomLayerHoleChance), Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_chance").forGetter(p_273148_ -> p_273148_.hangingLeavesChance), Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_extension_chance").forGetter(p_273098_ -> p_273098_.hangingLeavesExtensionChance))).apply(p_273246_, CherryFoliagePlacer::new));

    private final IntProvider height;

    private final float wideBottomLayerHoleChance;

    private final float cornerHoleChance;

    private final float hangingLeavesChance;

    private final float hangingLeavesExtensionChance;

    public CherryFoliagePlacer(IntProvider intProvider0, IntProvider intProvider1, IntProvider intProvider2, float float3, float float4, float float5, float float6) {
        super(intProvider0, intProvider1);
        this.height = intProvider2;
        this.wideBottomLayerHoleChance = float3;
        this.cornerHoleChance = float4;
        this.hangingLeavesChance = float5;
        this.hangingLeavesExtensionChance = float6;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.CHERRY_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader0, FoliagePlacer.FoliageSetter foliagePlacerFoliageSetter1, RandomSource randomSource2, TreeConfiguration treeConfiguration3, int int4, FoliagePlacer.FoliageAttachment foliagePlacerFoliageAttachment5, int int6, int int7, int int8) {
        boolean $$9 = foliagePlacerFoliageAttachment5.doubleTrunk();
        BlockPos $$10 = foliagePlacerFoliageAttachment5.pos().above(int8);
        int $$11 = int7 + foliagePlacerFoliageAttachment5.radiusOffset() - 1;
        this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$10, $$11 - 2, int6 - 3, $$9);
        this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$10, $$11 - 1, int6 - 4, $$9);
        for (int $$12 = int6 - 5; $$12 >= 0; $$12--) {
            this.m_225628_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$10, $$11, $$12, $$9);
        }
        this.m_272160_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$10, $$11, -1, $$9, this.hangingLeavesChance, this.hangingLeavesExtensionChance);
        this.m_272160_(levelSimulatedReader0, foliagePlacerFoliageSetter1, randomSource2, treeConfiguration3, $$10, $$11 - 1, -2, $$9, this.hangingLeavesChance, this.hangingLeavesExtensionChance);
    }

    @Override
    public int foliageHeight(RandomSource randomSource0, int int1, TreeConfiguration treeConfiguration2) {
        return this.height.sample(randomSource0);
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource randomSource0, int int1, int int2, int int3, int int4, boolean boolean5) {
        if (int2 == -1 && (int1 == int4 || int3 == int4) && randomSource0.nextFloat() < this.wideBottomLayerHoleChance) {
            return true;
        } else {
            boolean $$6 = int1 == int4 && int3 == int4;
            boolean $$7 = int4 > 2;
            return $$7 ? $$6 || int1 + int3 > int4 * 2 - 2 && randomSource0.nextFloat() < this.cornerHoleChance : $$6 && randomSource0.nextFloat() < this.cornerHoleChance;
        }
    }
}