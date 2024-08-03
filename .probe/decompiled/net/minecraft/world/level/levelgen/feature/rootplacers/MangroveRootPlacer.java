package net.minecraft.world.level.levelgen.feature.rootplacers;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class MangroveRootPlacer extends RootPlacer {

    public static final int ROOT_WIDTH_LIMIT = 8;

    public static final int ROOT_LENGTH_LIMIT = 15;

    public static final Codec<MangroveRootPlacer> CODEC = RecordCodecBuilder.create(p_225856_ -> m_225885_(p_225856_).and(MangroveRootPlacement.CODEC.fieldOf("mangrove_root_placement").forGetter(p_225849_ -> p_225849_.mangroveRootPlacement)).apply(p_225856_, MangroveRootPlacer::new));

    private final MangroveRootPlacement mangroveRootPlacement;

    public MangroveRootPlacer(IntProvider intProvider0, BlockStateProvider blockStateProvider1, Optional<AboveRootPlacement> optionalAboveRootPlacement2, MangroveRootPlacement mangroveRootPlacement3) {
        super(intProvider0, blockStateProvider1, optionalAboveRootPlacement2);
        this.mangroveRootPlacement = mangroveRootPlacement3;
    }

    @Override
    public boolean placeRoots(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, BlockPos blockPos3, BlockPos blockPos4, TreeConfiguration treeConfiguration5) {
        List<BlockPos> $$6 = Lists.newArrayList();
        BlockPos.MutableBlockPos $$7 = blockPos3.mutable();
        while ($$7.m_123342_() < blockPos4.m_123342_()) {
            if (!this.canPlaceRoot(levelSimulatedReader0, $$7)) {
                return false;
            }
            $$7.move(Direction.UP);
        }
        $$6.add(blockPos4.below());
        for (Direction $$8 : Direction.Plane.HORIZONTAL) {
            BlockPos $$9 = blockPos4.relative($$8);
            List<BlockPos> $$10 = Lists.newArrayList();
            if (!this.simulateRoots(levelSimulatedReader0, randomSource2, $$9, $$8, blockPos4, $$10, 0)) {
                return false;
            }
            $$6.addAll($$10);
            $$6.add(blockPos4.relative($$8));
        }
        for (BlockPos $$11 : $$6) {
            this.placeRoot(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$11, treeConfiguration5);
        }
        return true;
    }

    private boolean simulateRoots(LevelSimulatedReader levelSimulatedReader0, RandomSource randomSource1, BlockPos blockPos2, Direction direction3, BlockPos blockPos4, List<BlockPos> listBlockPos5, int int6) {
        int $$7 = this.mangroveRootPlacement.maxRootLength();
        if (int6 != $$7 && listBlockPos5.size() <= $$7) {
            for (BlockPos $$9 : this.potentialRootPositions(blockPos2, direction3, randomSource1, blockPos4)) {
                if (this.canPlaceRoot(levelSimulatedReader0, $$9)) {
                    listBlockPos5.add($$9);
                    if (!this.simulateRoots(levelSimulatedReader0, randomSource1, $$9, direction3, blockPos4, listBlockPos5, int6 + 1)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    protected List<BlockPos> potentialRootPositions(BlockPos blockPos0, Direction direction1, RandomSource randomSource2, BlockPos blockPos3) {
        BlockPos $$4 = blockPos0.below();
        BlockPos $$5 = blockPos0.relative(direction1);
        int $$6 = blockPos0.m_123333_(blockPos3);
        int $$7 = this.mangroveRootPlacement.maxRootWidth();
        float $$8 = this.mangroveRootPlacement.randomSkewChance();
        if ($$6 > $$7 - 3 && $$6 <= $$7) {
            return randomSource2.nextFloat() < $$8 ? List.of($$4, $$5.below()) : List.of($$4);
        } else if ($$6 > $$7) {
            return List.of($$4);
        } else if (randomSource2.nextFloat() < $$8) {
            return List.of($$4);
        } else {
            return randomSource2.nextBoolean() ? List.of($$5) : List.of($$4);
        }
    }

    @Override
    protected boolean canPlaceRoot(LevelSimulatedReader levelSimulatedReader0, BlockPos blockPos1) {
        return super.canPlaceRoot(levelSimulatedReader0, blockPos1) || levelSimulatedReader0.isStateAtPosition(blockPos1, p_225858_ -> p_225858_.m_204341_(this.mangroveRootPlacement.canGrowThrough()));
    }

    @Override
    protected void placeRoot(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, BlockPos blockPos3, TreeConfiguration treeConfiguration4) {
        if (levelSimulatedReader0.isStateAtPosition(blockPos3, p_225847_ -> p_225847_.m_204341_(this.mangroveRootPlacement.muddyRootsIn()))) {
            BlockState $$5 = this.mangroveRootPlacement.muddyRootsProvider().getState(randomSource2, blockPos3);
            biConsumerBlockPosBlockState1.accept(blockPos3, this.m_225870_(levelSimulatedReader0, blockPos3, $$5));
        } else {
            super.placeRoot(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, blockPos3, treeConfiguration4);
        }
    }

    @Override
    protected RootPlacerType<?> type() {
        return RootPlacerType.MANGROVE_ROOT_PLACER;
    }
}