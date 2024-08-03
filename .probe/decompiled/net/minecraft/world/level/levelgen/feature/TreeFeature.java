package net.minecraft.world.level.levelgen.feature;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

public class TreeFeature extends Feature<TreeConfiguration> {

    private static final int BLOCK_UPDATE_FLAGS = 19;

    public TreeFeature(Codec<TreeConfiguration> codecTreeConfiguration0) {
        super(codecTreeConfiguration0);
    }

    private static boolean isVine(LevelSimulatedReader levelSimulatedReader0, BlockPos blockPos1) {
        return levelSimulatedReader0.isStateAtPosition(blockPos1, p_225299_ -> p_225299_.m_60713_(Blocks.VINE));
    }

    public static boolean isAirOrLeaves(LevelSimulatedReader levelSimulatedReader0, BlockPos blockPos1) {
        return levelSimulatedReader0.isStateAtPosition(blockPos1, p_284924_ -> p_284924_.m_60795_() || p_284924_.m_204336_(BlockTags.LEAVES));
    }

    private static void setBlockKnownShape(LevelWriter levelWriter0, BlockPos blockPos1, BlockState blockState2) {
        levelWriter0.setBlock(blockPos1, blockState2, 19);
    }

    public static boolean validTreePos(LevelSimulatedReader levelSimulatedReader0, BlockPos blockPos1) {
        return levelSimulatedReader0.isStateAtPosition(blockPos1, p_284925_ -> p_284925_.m_60795_() || p_284925_.m_204336_(BlockTags.REPLACEABLE_BY_TREES));
    }

    private boolean doPlace(WorldGenLevel worldGenLevel0, RandomSource randomSource1, BlockPos blockPos2, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState3, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState4, FoliagePlacer.FoliageSetter foliagePlacerFoliageSetter5, TreeConfiguration treeConfiguration6) {
        int $$7 = treeConfiguration6.trunkPlacer.getTreeHeight(randomSource1);
        int $$8 = treeConfiguration6.foliagePlacer.foliageHeight(randomSource1, $$7, treeConfiguration6);
        int $$9 = $$7 - $$8;
        int $$10 = treeConfiguration6.foliagePlacer.foliageRadius(randomSource1, $$9);
        BlockPos $$11 = (BlockPos) treeConfiguration6.rootPlacer.map(p_225286_ -> p_225286_.getTrunkOrigin(blockPos2, randomSource1)).orElse(blockPos2);
        int $$12 = Math.min(blockPos2.m_123342_(), $$11.m_123342_());
        int $$13 = Math.max(blockPos2.m_123342_(), $$11.m_123342_()) + $$7 + 1;
        if ($$12 >= worldGenLevel0.m_141937_() + 1 && $$13 <= worldGenLevel0.m_151558_()) {
            OptionalInt $$14 = treeConfiguration6.minimumSize.minClippedHeight();
            int $$15 = this.getMaxFreeTreeHeight(worldGenLevel0, $$7, $$11, treeConfiguration6);
            if ($$15 >= $$7 || !$$14.isEmpty() && $$15 >= $$14.getAsInt()) {
                if (treeConfiguration6.rootPlacer.isPresent() && !((RootPlacer) treeConfiguration6.rootPlacer.get()).placeRoots(worldGenLevel0, biConsumerBlockPosBlockState3, randomSource1, blockPos2, $$11, treeConfiguration6)) {
                    return false;
                } else {
                    List<FoliagePlacer.FoliageAttachment> $$16 = treeConfiguration6.trunkPlacer.placeTrunk(worldGenLevel0, biConsumerBlockPosBlockState4, randomSource1, $$15, $$11, treeConfiguration6);
                    $$16.forEach(p_272582_ -> treeConfiguration6.foliagePlacer.createFoliage(worldGenLevel0, foliagePlacerFoliageSetter5, randomSource1, treeConfiguration6, $$15, p_272582_, $$8, $$10));
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getMaxFreeTreeHeight(LevelSimulatedReader levelSimulatedReader0, int int1, BlockPos blockPos2, TreeConfiguration treeConfiguration3) {
        BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
        for (int $$5 = 0; $$5 <= int1 + 1; $$5++) {
            int $$6 = treeConfiguration3.minimumSize.getSizeAtHeight(int1, $$5);
            for (int $$7 = -$$6; $$7 <= $$6; $$7++) {
                for (int $$8 = -$$6; $$8 <= $$6; $$8++) {
                    $$4.setWithOffset(blockPos2, $$7, $$5, $$8);
                    if (!treeConfiguration3.trunkPlacer.isFree(levelSimulatedReader0, $$4) || !treeConfiguration3.ignoreVines && isVine(levelSimulatedReader0, $$4)) {
                        return $$5 - 2;
                    }
                }
            }
        }
        return int1;
    }

    @Override
    protected void setBlock(LevelWriter levelWriter0, BlockPos blockPos1, BlockState blockState2) {
        setBlockKnownShape(levelWriter0, blockPos1, blockState2);
    }

    @Override
    public final boolean place(FeaturePlaceContext<TreeConfiguration> featurePlaceContextTreeConfiguration0) {
        final WorldGenLevel $$1 = featurePlaceContextTreeConfiguration0.level();
        RandomSource $$2 = featurePlaceContextTreeConfiguration0.random();
        BlockPos $$3 = featurePlaceContextTreeConfiguration0.origin();
        TreeConfiguration $$4 = featurePlaceContextTreeConfiguration0.config();
        Set<BlockPos> $$5 = Sets.newHashSet();
        Set<BlockPos> $$6 = Sets.newHashSet();
        final Set<BlockPos> $$7 = Sets.newHashSet();
        Set<BlockPos> $$8 = Sets.newHashSet();
        BiConsumer<BlockPos, BlockState> $$9 = (p_160555_, p_160556_) -> {
            $$5.add(p_160555_.immutable());
            $$1.m_7731_(p_160555_, p_160556_, 19);
        };
        BiConsumer<BlockPos, BlockState> $$10 = (p_160548_, p_160549_) -> {
            $$6.add(p_160548_.immutable());
            $$1.m_7731_(p_160548_, p_160549_, 19);
        };
        FoliagePlacer.FoliageSetter $$11 = new FoliagePlacer.FoliageSetter() {

            @Override
            public void set(BlockPos p_272825_, BlockState p_273311_) {
                $$7.add(p_272825_.immutable());
                $$1.m_7731_(p_272825_, p_273311_, 19);
            }

            @Override
            public boolean isSet(BlockPos p_272999_) {
                return $$7.contains(p_272999_);
            }
        };
        BiConsumer<BlockPos, BlockState> $$12 = (p_160543_, p_160544_) -> {
            $$8.add(p_160543_.immutable());
            $$1.m_7731_(p_160543_, p_160544_, 19);
        };
        boolean $$13 = this.doPlace($$1, $$2, $$3, $$9, $$10, $$11, $$4);
        if ($$13 && (!$$6.isEmpty() || !$$7.isEmpty())) {
            if (!$$4.decorators.isEmpty()) {
                TreeDecorator.Context $$14 = new TreeDecorator.Context($$1, $$12, $$2, $$6, $$7, $$5);
                $$4.decorators.forEach(p_225282_ -> p_225282_.place($$14));
            }
            return (Boolean) BoundingBox.encapsulatingPositions(Iterables.concat($$5, $$6, $$7, $$8)).map(p_225270_ -> {
                DiscreteVoxelShape $$5x = updateLeaves($$1, p_225270_, $$6, $$8, $$5);
                StructureTemplate.updateShapeAtEdge($$1, 3, $$5x, p_225270_.minX(), p_225270_.minY(), p_225270_.minZ());
                return true;
            }).orElse(false);
        } else {
            return false;
        }
    }

    private static DiscreteVoxelShape updateLeaves(LevelAccessor levelAccessor0, BoundingBox boundingBox1, Set<BlockPos> setBlockPos2, Set<BlockPos> setBlockPos3, Set<BlockPos> setBlockPos4) {
        DiscreteVoxelShape $$5 = new BitSetDiscreteVoxelShape(boundingBox1.getXSpan(), boundingBox1.getYSpan(), boundingBox1.getZSpan());
        int $$6 = 7;
        List<Set<BlockPos>> $$7 = Lists.newArrayList();
        for (int $$8 = 0; $$8 < 7; $$8++) {
            $$7.add(Sets.newHashSet());
        }
        for (BlockPos $$9 : Lists.newArrayList(Sets.union(setBlockPos3, setBlockPos4))) {
            if (boundingBox1.isInside($$9)) {
                $$5.fill($$9.m_123341_() - boundingBox1.minX(), $$9.m_123342_() - boundingBox1.minY(), $$9.m_123343_() - boundingBox1.minZ());
            }
        }
        BlockPos.MutableBlockPos $$10 = new BlockPos.MutableBlockPos();
        int $$11 = 0;
        ((Set) $$7.get(0)).addAll(setBlockPos2);
        while (true) {
            while ($$11 >= 7 || !((Set) $$7.get($$11)).isEmpty()) {
                if ($$11 >= 7) {
                    return $$5;
                }
                Iterator<BlockPos> $$12 = ((Set) $$7.get($$11)).iterator();
                BlockPos $$13 = (BlockPos) $$12.next();
                $$12.remove();
                if (boundingBox1.isInside($$13)) {
                    if ($$11 != 0) {
                        BlockState $$14 = levelAccessor0.m_8055_($$13);
                        setBlockKnownShape(levelAccessor0, $$13, (BlockState) $$14.m_61124_(BlockStateProperties.DISTANCE, $$11));
                    }
                    $$5.fill($$13.m_123341_() - boundingBox1.minX(), $$13.m_123342_() - boundingBox1.minY(), $$13.m_123343_() - boundingBox1.minZ());
                    for (Direction $$15 : Direction.values()) {
                        $$10.setWithOffset($$13, $$15);
                        if (boundingBox1.isInside($$10)) {
                            int $$16 = $$10.m_123341_() - boundingBox1.minX();
                            int $$17 = $$10.m_123342_() - boundingBox1.minY();
                            int $$18 = $$10.m_123343_() - boundingBox1.minZ();
                            if (!$$5.isFull($$16, $$17, $$18)) {
                                BlockState $$19 = levelAccessor0.m_8055_($$10);
                                OptionalInt $$20 = LeavesBlock.getOptionalDistanceAt($$19);
                                if (!$$20.isEmpty()) {
                                    int $$21 = Math.min($$20.getAsInt(), $$11 + 1);
                                    if ($$21 < 7) {
                                        ((Set) $$7.get($$21)).add($$10.immutable());
                                        $$11 = Math.min($$11, $$21);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            $$11++;
        }
    }
}