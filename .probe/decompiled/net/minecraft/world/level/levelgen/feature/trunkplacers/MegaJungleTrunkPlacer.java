package net.minecraft.world.level.levelgen.feature.trunkplacers;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

public class MegaJungleTrunkPlacer extends GiantTrunkPlacer {

    public static final Codec<MegaJungleTrunkPlacer> CODEC = RecordCodecBuilder.create(p_70206_ -> m_70305_(p_70206_).apply(p_70206_, MegaJungleTrunkPlacer::new));

    public MegaJungleTrunkPlacer(int int0, int int1, int int2) {
        super(int0, int1, int2);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.MEGA_JUNGLE_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, int int3, BlockPos blockPos4, TreeConfiguration treeConfiguration5) {
        List<FoliagePlacer.FoliageAttachment> $$6 = Lists.newArrayList();
        $$6.addAll(super.placeTrunk(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, int3, blockPos4, treeConfiguration5));
        for (int $$7 = int3 - 2 - randomSource2.nextInt(4); $$7 > int3 / 2; $$7 -= 2 + randomSource2.nextInt(4)) {
            float $$8 = randomSource2.nextFloat() * (float) (Math.PI * 2);
            int $$9 = 0;
            int $$10 = 0;
            for (int $$11 = 0; $$11 < 5; $$11++) {
                $$9 = (int) (1.5F + Mth.cos($$8) * (float) $$11);
                $$10 = (int) (1.5F + Mth.sin($$8) * (float) $$11);
                BlockPos $$12 = blockPos4.offset($$9, $$7 - 3 + $$11 / 2, $$10);
                this.m_226187_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$12, treeConfiguration5);
            }
            $$6.add(new FoliagePlacer.FoliageAttachment(blockPos4.offset($$9, $$7, $$10), -2, false));
        }
        return $$6;
    }
}