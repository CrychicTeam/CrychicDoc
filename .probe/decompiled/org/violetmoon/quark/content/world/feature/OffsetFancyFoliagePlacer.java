package org.violetmoon.quark.content.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class OffsetFancyFoliagePlacer extends FancyFoliagePlacer {

    public static final Codec<OffsetFancyFoliagePlacer> CODEC = RecordCodecBuilder.create(i -> m_68413_(i).apply(i, OffsetFancyFoliagePlacer::new));

    public static final FoliagePlacerType<OffsetFancyFoliagePlacer> TYPE = new FoliagePlacerType<>(CODEC);

    public OffsetFancyFoliagePlacer(IntProvider intProvider, IntProvider intProvider1, int i) {
        super(intProvider, intProvider1, i);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return TYPE;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader levelSimulatedReader, FoliagePlacer.FoliageSetter foliageSetter, RandomSource randomSource, TreeConfiguration treeConfiguration, int i, FoliagePlacer.FoliageAttachment foliageAttachment, int i1, int i2, int offset) {
        super.createFoliage(levelSimulatedReader, foliageSetter, randomSource, treeConfiguration, i, foliageAttachment, i1, i2, -3);
    }
}