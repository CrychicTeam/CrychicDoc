package net.minecraft.world.level.block;

import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class SculkVeinBlock extends MultifaceBlock implements SculkBehaviour, SimpleWaterloggedBlock {

    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final MultifaceSpreader veinSpreader = new MultifaceSpreader(new SculkVeinBlock.SculkVeinSpreaderConfig(MultifaceSpreader.DEFAULT_SPREAD_ORDER));

    private final MultifaceSpreader sameSpaceSpreader = new MultifaceSpreader(new SculkVeinBlock.SculkVeinSpreaderConfig(MultifaceSpreader.SpreadType.SAME_POSITION));

    public SculkVeinBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false));
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return this.veinSpreader;
    }

    public MultifaceSpreader getSameSpaceSpreader() {
        return this.sameSpaceSpreader;
    }

    public static boolean regrow(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2, Collection<Direction> collectionDirection3) {
        boolean $$4 = false;
        BlockState $$5 = Blocks.SCULK_VEIN.defaultBlockState();
        for (Direction $$6 : collectionDirection3) {
            BlockPos $$7 = blockPos1.relative($$6);
            if (m_153829_(levelAccessor0, $$6, $$7, levelAccessor0.m_8055_($$7))) {
                $$5 = (BlockState) $$5.m_61124_(m_153933_($$6), true);
                $$4 = true;
            }
        }
        if (!$$4) {
            return false;
        } else {
            if (!blockState2.m_60819_().isEmpty()) {
                $$5 = (BlockState) $$5.m_61124_(WATERLOGGED, true);
            }
            levelAccessor0.m_7731_(blockPos1, $$5, 3);
            return true;
        }
    }

    @Override
    public void onDischarged(LevelAccessor levelAccessor0, BlockState blockState1, BlockPos blockPos2, RandomSource randomSource3) {
        if (blockState1.m_60713_(this)) {
            for (Direction $$4 : f_153806_) {
                BooleanProperty $$5 = m_153933_($$4);
                if ((Boolean) blockState1.m_61143_($$5) && levelAccessor0.m_8055_(blockPos2.relative($$4)).m_60713_(Blocks.SCULK)) {
                    blockState1 = (BlockState) blockState1.m_61124_($$5, false);
                }
            }
            if (!m_153960_(blockState1)) {
                FluidState $$6 = levelAccessor0.m_6425_(blockPos2);
                blockState1 = ($$6.isEmpty() ? Blocks.AIR : Blocks.WATER).defaultBlockState();
            }
            levelAccessor0.m_7731_(blockPos2, blockState1, 3);
            SculkBehaviour.super.onDischarged(levelAccessor0, blockState1, blockPos2, randomSource3);
        }
    }

    @Override
    public int attemptUseCharge(SculkSpreader.ChargeCursor sculkSpreaderChargeCursor0, LevelAccessor levelAccessor1, BlockPos blockPos2, RandomSource randomSource3, SculkSpreader sculkSpreader4, boolean boolean5) {
        if (boolean5 && this.attemptPlaceSculk(sculkSpreader4, levelAccessor1, sculkSpreaderChargeCursor0.getPos(), randomSource3)) {
            return sculkSpreaderChargeCursor0.getCharge() - 1;
        } else {
            return randomSource3.nextInt(sculkSpreader4.chargeDecayRate()) == 0 ? Mth.floor((float) sculkSpreaderChargeCursor0.getCharge() * 0.5F) : sculkSpreaderChargeCursor0.getCharge();
        }
    }

    private boolean attemptPlaceSculk(SculkSpreader sculkSpreader0, LevelAccessor levelAccessor1, BlockPos blockPos2, RandomSource randomSource3) {
        BlockState $$4 = levelAccessor1.m_8055_(blockPos2);
        TagKey<Block> $$5 = sculkSpreader0.replaceableBlocks();
        for (Direction $$6 : Direction.allShuffled(randomSource3)) {
            if (m_153900_($$4, $$6)) {
                BlockPos $$7 = blockPos2.relative($$6);
                BlockState $$8 = levelAccessor1.m_8055_($$7);
                if ($$8.m_204336_($$5)) {
                    BlockState $$9 = Blocks.SCULK.defaultBlockState();
                    levelAccessor1.m_7731_($$7, $$9, 3);
                    Block.pushEntitiesUp($$8, $$9, levelAccessor1, $$7);
                    levelAccessor1.playSound(null, $$7, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);
                    this.veinSpreader.spreadAll($$9, levelAccessor1, $$7, sculkSpreader0.isWorldGeneration());
                    Direction $$10 = $$6.getOpposite();
                    for (Direction $$11 : f_153806_) {
                        if ($$11 != $$10) {
                            BlockPos $$12 = $$7.relative($$11);
                            BlockState $$13 = levelAccessor1.m_8055_($$12);
                            if ($$13.m_60713_(this)) {
                                this.onDischarged(levelAccessor1, $$13, $$12, randomSource3);
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasSubstrateAccess(LevelAccessor levelAccessor0, BlockState blockState1, BlockPos blockPos2) {
        if (!blockState1.m_60713_(Blocks.SCULK_VEIN)) {
            return false;
        } else {
            for (Direction $$3 : f_153806_) {
                if (m_153900_(blockState1, $$3) && levelAccessor0.m_8055_(blockPos2.relative($$3)).m_204336_(BlockTags.SCULK_REPLACEABLE)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.updateShape(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        super.createBlockStateDefinition(stateDefinitionBuilderBlockBlockState0);
        stateDefinitionBuilderBlockBlockState0.add(WATERLOGGED);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState0, BlockPlaceContext blockPlaceContext1) {
        return !blockPlaceContext1.m_43722_().is(Items.SCULK_VEIN) || super.canBeReplaced(blockState0, blockPlaceContext1);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    class SculkVeinSpreaderConfig extends MultifaceSpreader.DefaultSpreaderConfig {

        private final MultifaceSpreader.SpreadType[] spreadTypes;

        public SculkVeinSpreaderConfig(MultifaceSpreader.SpreadType... multifaceSpreaderSpreadType0) {
            super(SculkVeinBlock.this);
            this.spreadTypes = multifaceSpreaderSpreadType0;
        }

        @Override
        public boolean stateCanBeReplaced(BlockGetter blockGetter0, BlockPos blockPos1, BlockPos blockPos2, Direction direction3, BlockState blockState4) {
            BlockState $$5 = blockGetter0.getBlockState(blockPos2.relative(direction3));
            if (!$$5.m_60713_(Blocks.SCULK) && !$$5.m_60713_(Blocks.SCULK_CATALYST) && !$$5.m_60713_(Blocks.MOVING_PISTON)) {
                if (blockPos1.m_123333_(blockPos2) == 2) {
                    BlockPos $$6 = blockPos1.relative(direction3.getOpposite());
                    if (blockGetter0.getBlockState($$6).m_60783_(blockGetter0, $$6, direction3)) {
                        return false;
                    }
                }
                FluidState $$7 = blockState4.m_60819_();
                if (!$$7.isEmpty() && !$$7.is(Fluids.WATER)) {
                    return false;
                } else {
                    return blockState4.m_204336_(BlockTags.FIRE) ? false : blockState4.m_247087_() || super.stateCanBeReplaced(blockGetter0, blockPos1, blockPos2, direction3, blockState4);
                }
            } else {
                return false;
            }
        }

        @Override
        public MultifaceSpreader.SpreadType[] getSpreadTypes() {
            return this.spreadTypes;
        }

        @Override
        public boolean isOtherBlockValidAsSource(BlockState blockState0) {
            return !blockState0.m_60713_(Blocks.SCULK_VEIN);
        }
    }
}