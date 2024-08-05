package net.minecraft.world.level.levelgen.feature.trunkplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

public class CherryTrunkPlacer extends TrunkPlacer {

    private static final Codec<UniformInt> BRANCH_START_CODEC = ExtraCodecs.validate(UniformInt.CODEC, p_275181_ -> p_275181_.getMaxValue() - p_275181_.getMinValue() < 1 ? DataResult.error(() -> "Need at least 2 blocks variation for the branch starts to fit both branches") : DataResult.success(p_275181_));

    public static final Codec<CherryTrunkPlacer> CODEC = RecordCodecBuilder.create(p_273579_ -> m_70305_(p_273579_).and(p_273579_.group(IntProvider.codec(1, 3).fieldOf("branch_count").forGetter(p_272644_ -> p_272644_.branchCount), IntProvider.codec(2, 16).fieldOf("branch_horizontal_length").forGetter(p_273612_ -> p_273612_.branchHorizontalLength), IntProvider.codec(-16, 0, BRANCH_START_CODEC).fieldOf("branch_start_offset_from_top").forGetter(p_272705_ -> p_272705_.branchStartOffsetFromTop), IntProvider.codec(-16, 16).fieldOf("branch_end_offset_from_top").forGetter(p_273633_ -> p_273633_.branchEndOffsetFromTop))).apply(p_273579_, CherryTrunkPlacer::new));

    private final IntProvider branchCount;

    private final IntProvider branchHorizontalLength;

    private final UniformInt branchStartOffsetFromTop;

    private final UniformInt secondBranchStartOffsetFromTop;

    private final IntProvider branchEndOffsetFromTop;

    public CherryTrunkPlacer(int int0, int int1, int int2, IntProvider intProvider3, IntProvider intProvider4, UniformInt uniformInt5, IntProvider intProvider6) {
        super(int0, int1, int2);
        this.branchCount = intProvider3;
        this.branchHorizontalLength = intProvider4;
        this.branchStartOffsetFromTop = uniformInt5;
        this.secondBranchStartOffsetFromTop = UniformInt.of(uniformInt5.getMinValue(), uniformInt5.getMaxValue() - 1);
        this.branchEndOffsetFromTop = intProvider6;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.CHERRY_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, int int3, BlockPos blockPos4, TreeConfiguration treeConfiguration5) {
        m_226169_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, blockPos4.below(), treeConfiguration5);
        int $$6 = Math.max(0, int3 - 1 + this.branchStartOffsetFromTop.sample(randomSource2));
        int $$7 = Math.max(0, int3 - 1 + this.secondBranchStartOffsetFromTop.sample(randomSource2));
        if ($$7 >= $$6) {
            $$7++;
        }
        int $$8 = this.branchCount.sample(randomSource2);
        boolean $$9 = $$8 == 3;
        boolean $$10 = $$8 >= 2;
        int $$11;
        if ($$9) {
            $$11 = int3;
        } else if ($$10) {
            $$11 = Math.max($$6, $$7) + 1;
        } else {
            $$11 = $$6 + 1;
        }
        for (int $$14 = 0; $$14 < $$11; $$14++) {
            this.m_226187_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, blockPos4.above($$14), treeConfiguration5);
        }
        List<FoliagePlacer.FoliageAttachment> $$15 = new ArrayList();
        if ($$9) {
            $$15.add(new FoliagePlacer.FoliageAttachment(blockPos4.above($$11), 0, false));
        }
        BlockPos.MutableBlockPos $$16 = new BlockPos.MutableBlockPos();
        Direction $$17 = Direction.Plane.HORIZONTAL.getRandomDirection(randomSource2);
        Function<BlockState, BlockState> $$18 = p_273382_ -> (BlockState) p_273382_.m_263224_(RotatedPillarBlock.AXIS, $$17.getAxis());
        $$15.add(this.generateBranch(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, int3, blockPos4, treeConfiguration5, $$18, $$17, $$6, $$6 < $$11 - 1, $$16));
        if ($$10) {
            $$15.add(this.generateBranch(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, int3, blockPos4, treeConfiguration5, $$18, $$17.getOpposite(), $$7, $$7 < $$11 - 1, $$16));
        }
        return $$15;
    }

    private FoliagePlacer.FoliageAttachment generateBranch(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, int int3, BlockPos blockPos4, TreeConfiguration treeConfiguration5, Function<BlockState, BlockState> functionBlockStateBlockState6, Direction direction7, int int8, boolean boolean9, BlockPos.MutableBlockPos blockPosMutableBlockPos10) {
        blockPosMutableBlockPos10.set(blockPos4).move(Direction.UP, int8);
        int $$11 = int3 - 1 + this.branchEndOffsetFromTop.sample(randomSource2);
        boolean $$12 = boolean9 || $$11 < int8;
        int $$13 = this.branchHorizontalLength.sample(randomSource2) + ($$12 ? 1 : 0);
        BlockPos $$14 = blockPos4.relative(direction7, $$13).above($$11);
        int $$15 = $$12 ? 2 : 1;
        for (int $$16 = 0; $$16 < $$15; $$16++) {
            this.m_226175_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, blockPosMutableBlockPos10.move(direction7), treeConfiguration5, functionBlockStateBlockState6);
        }
        Direction $$17 = $$14.m_123342_() > blockPosMutableBlockPos10.m_123342_() ? Direction.UP : Direction.DOWN;
        while (true) {
            int $$18 = blockPosMutableBlockPos10.m_123333_($$14);
            if ($$18 == 0) {
                return new FoliagePlacer.FoliageAttachment($$14.above(), 0, false);
            }
            float $$19 = (float) Math.abs($$14.m_123342_() - blockPosMutableBlockPos10.m_123342_()) / (float) $$18;
            boolean $$20 = randomSource2.nextFloat() < $$19;
            blockPosMutableBlockPos10.move($$20 ? $$17 : direction7);
            this.m_226175_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, blockPosMutableBlockPos10, treeConfiguration5, $$20 ? Function.identity() : functionBlockStateBlockState6);
        }
    }
}