package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class ChorusFlowerBlock extends Block {

    public static final int DEAD_AGE = 5;

    public static final IntegerProperty AGE = BlockStateProperties.AGE_5;

    private final ChorusPlantBlock plant;

    protected ChorusFlowerBlock(ChorusPlantBlock chorusPlantBlock0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.plant = chorusPlantBlock0;
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(AGE, 0));
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!blockState0.m_60710_(serverLevel1, blockPos2)) {
            serverLevel1.m_46961_(blockPos2, true);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(AGE) < 5;
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        BlockPos $$4 = blockPos2.above();
        if (serverLevel1.m_46859_($$4) && $$4.m_123342_() < serverLevel1.m_151558_()) {
            int $$5 = (Integer) blockState0.m_61143_(AGE);
            if ($$5 < 5) {
                boolean $$6 = false;
                boolean $$7 = false;
                BlockState $$8 = serverLevel1.m_8055_(blockPos2.below());
                if ($$8.m_60713_(Blocks.END_STONE)) {
                    $$6 = true;
                } else if ($$8.m_60713_(this.plant)) {
                    int $$9 = 1;
                    for (int $$10 = 0; $$10 < 4; $$10++) {
                        BlockState $$11 = serverLevel1.m_8055_(blockPos2.below($$9 + 1));
                        if (!$$11.m_60713_(this.plant)) {
                            if ($$11.m_60713_(Blocks.END_STONE)) {
                                $$7 = true;
                            }
                            break;
                        }
                        $$9++;
                    }
                    if ($$9 < 2 || $$9 <= randomSource3.nextInt($$7 ? 5 : 4)) {
                        $$6 = true;
                    }
                } else if ($$8.m_60795_()) {
                    $$6 = true;
                }
                if ($$6 && allNeighborsEmpty(serverLevel1, $$4, null) && serverLevel1.m_46859_(blockPos2.above(2))) {
                    serverLevel1.m_7731_(blockPos2, this.plant.getStateForPlacement(serverLevel1, blockPos2), 2);
                    this.placeGrownFlower(serverLevel1, $$4, $$5);
                } else if ($$5 < 4) {
                    int $$12 = randomSource3.nextInt(4);
                    if ($$7) {
                        $$12++;
                    }
                    boolean $$13 = false;
                    for (int $$14 = 0; $$14 < $$12; $$14++) {
                        Direction $$15 = Direction.Plane.HORIZONTAL.getRandomDirection(randomSource3);
                        BlockPos $$16 = blockPos2.relative($$15);
                        if (serverLevel1.m_46859_($$16) && serverLevel1.m_46859_($$16.below()) && allNeighborsEmpty(serverLevel1, $$16, $$15.getOpposite())) {
                            this.placeGrownFlower(serverLevel1, $$16, $$5 + 1);
                            $$13 = true;
                        }
                    }
                    if ($$13) {
                        serverLevel1.m_7731_(blockPos2, this.plant.getStateForPlacement(serverLevel1, blockPos2), 2);
                    } else {
                        this.placeDeadFlower(serverLevel1, blockPos2);
                    }
                } else {
                    this.placeDeadFlower(serverLevel1, blockPos2);
                }
            }
        }
    }

    private void placeGrownFlower(Level level0, BlockPos blockPos1, int int2) {
        level0.setBlock(blockPos1, (BlockState) this.m_49966_().m_61124_(AGE, int2), 2);
        level0.m_46796_(1033, blockPos1, 0);
    }

    private void placeDeadFlower(Level level0, BlockPos blockPos1) {
        level0.setBlock(blockPos1, (BlockState) this.m_49966_().m_61124_(AGE, 5), 2);
        level0.m_46796_(1034, blockPos1, 0);
    }

    private static boolean allNeighborsEmpty(LevelReader levelReader0, BlockPos blockPos1, @Nullable Direction direction2) {
        for (Direction $$3 : Direction.Plane.HORIZONTAL) {
            if ($$3 != direction2 && !levelReader0.isEmptyBlock(blockPos1.relative($$3))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (direction1 != Direction.UP && !blockState0.m_60710_(levelAccessor3, blockPos4)) {
            levelAccessor3.scheduleTick(blockPos4, this, 1);
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockState $$3 = levelReader1.m_8055_(blockPos2.below());
        if (!$$3.m_60713_(this.plant) && !$$3.m_60713_(Blocks.END_STONE)) {
            if (!$$3.m_60795_()) {
                return false;
            } else {
                boolean $$4 = false;
                for (Direction $$5 : Direction.Plane.HORIZONTAL) {
                    BlockState $$6 = levelReader1.m_8055_(blockPos2.relative($$5));
                    if ($$6.m_60713_(this.plant)) {
                        if ($$4) {
                            return false;
                        }
                        $$4 = true;
                    } else if (!$$6.m_60795_()) {
                        return false;
                    }
                }
                return $$4;
            }
        } else {
            return true;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(AGE);
    }

    public static void generatePlant(LevelAccessor levelAccessor0, BlockPos blockPos1, RandomSource randomSource2, int int3) {
        levelAccessor0.m_7731_(blockPos1, ((ChorusPlantBlock) Blocks.CHORUS_PLANT).getStateForPlacement(levelAccessor0, blockPos1), 2);
        growTreeRecursive(levelAccessor0, blockPos1, randomSource2, blockPos1, int3, 0);
    }

    private static void growTreeRecursive(LevelAccessor levelAccessor0, BlockPos blockPos1, RandomSource randomSource2, BlockPos blockPos3, int int4, int int5) {
        ChorusPlantBlock $$6 = (ChorusPlantBlock) Blocks.CHORUS_PLANT;
        int $$7 = randomSource2.nextInt(4) + 1;
        if (int5 == 0) {
            $$7++;
        }
        for (int $$8 = 0; $$8 < $$7; $$8++) {
            BlockPos $$9 = blockPos1.above($$8 + 1);
            if (!allNeighborsEmpty(levelAccessor0, $$9, null)) {
                return;
            }
            levelAccessor0.m_7731_($$9, $$6.getStateForPlacement(levelAccessor0, $$9), 2);
            levelAccessor0.m_7731_($$9.below(), $$6.getStateForPlacement(levelAccessor0, $$9.below()), 2);
        }
        boolean $$10 = false;
        if (int5 < 4) {
            int $$11 = randomSource2.nextInt(4);
            if (int5 == 0) {
                $$11++;
            }
            for (int $$12 = 0; $$12 < $$11; $$12++) {
                Direction $$13 = Direction.Plane.HORIZONTAL.getRandomDirection(randomSource2);
                BlockPos $$14 = blockPos1.above($$7).relative($$13);
                if (Math.abs($$14.m_123341_() - blockPos3.m_123341_()) < int4 && Math.abs($$14.m_123343_() - blockPos3.m_123343_()) < int4 && levelAccessor0.m_46859_($$14) && levelAccessor0.m_46859_($$14.below()) && allNeighborsEmpty(levelAccessor0, $$14, $$13.getOpposite())) {
                    $$10 = true;
                    levelAccessor0.m_7731_($$14, $$6.getStateForPlacement(levelAccessor0, $$14), 2);
                    levelAccessor0.m_7731_($$14.relative($$13.getOpposite()), $$6.getStateForPlacement(levelAccessor0, $$14.relative($$13.getOpposite())), 2);
                    growTreeRecursive(levelAccessor0, $$14, randomSource2, blockPos3, int4, int5 + 1);
                }
            }
        }
        if (!$$10) {
            levelAccessor0.m_7731_(blockPos1.above($$7), (BlockState) Blocks.CHORUS_FLOWER.defaultBlockState().m_61124_(AGE, 5), 2);
        }
    }

    @Override
    public void onProjectileHit(Level level0, BlockState blockState1, BlockHitResult blockHitResult2, Projectile projectile3) {
        BlockPos $$4 = blockHitResult2.getBlockPos();
        if (!level0.isClientSide && projectile3.mayInteract(level0, $$4) && projectile3.m_6095_().is(EntityTypeTags.IMPACT_PROJECTILES)) {
            level0.m_46953_($$4, true, projectile3);
        }
    }
}