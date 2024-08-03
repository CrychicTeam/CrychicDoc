package org.violetmoon.quark.content.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

public class MultiFoliageStraightTrunkPlacer extends TrunkPlacer {

    final int foliageDistance;

    final int maxBlobs;

    public static final Codec<MultiFoliageStraightTrunkPlacer> CODEC = RecordCodecBuilder.create(overengineered -> m_70305_(overengineered).and(overengineered.group(Codec.INT.fieldOf("foliageDistance").forGetter(x -> x.foliageDistance), Codec.INT.fieldOf("maxBlobs").forGetter(x -> x.maxBlobs))).apply(overengineered, MultiFoliageStraightTrunkPlacer::new));

    public static final TrunkPlacerType<MultiFoliageStraightTrunkPlacer> TYPE = new TrunkPlacerType<>(CODEC);

    public MultiFoliageStraightTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, int foliageDistance, int maxBlobs) {
        super(baseHeight, heightRandA, heightRandB);
        this.foliageDistance = foliageDistance;
        this.maxBlobs = maxBlobs;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TYPE;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> placer, RandomSource random, int idk, BlockPos rootPos, TreeConfiguration cfg) {
        m_226169_(level, placer, random, rootPos.below(), cfg);
        List<BlockPos> folliagePositions = new ArrayList();
        int placed = 0;
        int j = 0;
        for (int i = idk; i >= 0; i--) {
            BlockPos target = rootPos.above(i);
            this.m_226187_(level, placer, random, target, cfg);
            if (placed < this.maxBlobs) {
                if (j == 0) {
                    folliagePositions.add(target);
                    j = this.foliageDistance;
                    placed++;
                } else {
                    j--;
                }
            }
        }
        return (List<FoliagePlacer.FoliageAttachment>) folliagePositions.stream().map(p -> new FoliagePlacer.FoliageAttachment(p, 0, false)).collect(Collectors.toList());
    }
}