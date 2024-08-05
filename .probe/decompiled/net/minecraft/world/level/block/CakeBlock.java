package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CakeBlock extends Block {

    public static final int MAX_BITES = 6;

    public static final IntegerProperty BITES = BlockStateProperties.BITES;

    public static final int FULL_CAKE_SIGNAL = getOutputSignal(0);

    protected static final float AABB_OFFSET = 1.0F;

    protected static final float AABB_SIZE_PER_BITE = 2.0F;

    protected static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[] { Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0), Block.box(3.0, 0.0, 1.0, 15.0, 8.0, 15.0), Block.box(5.0, 0.0, 1.0, 15.0, 8.0, 15.0), Block.box(7.0, 0.0, 1.0, 15.0, 8.0, 15.0), Block.box(9.0, 0.0, 1.0, 15.0, 8.0, 15.0), Block.box(11.0, 0.0, 1.0, 15.0, 8.0, 15.0), Block.box(13.0, 0.0, 1.0, 15.0, 8.0, 15.0) };

    protected CakeBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(BITES, 0));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE_BY_BITE[blockState0.m_61143_(BITES)];
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        ItemStack $$6 = player3.m_21120_(interactionHand4);
        Item $$7 = $$6.getItem();
        if ($$6.is(ItemTags.CANDLES) && (Integer) blockState0.m_61143_(BITES) == 0) {
            Block $$8 = Block.byItem($$7);
            if ($$8 instanceof CandleBlock) {
                if (!player3.isCreative()) {
                    $$6.shrink(1);
                }
                level1.playSound(null, blockPos2, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level1.setBlockAndUpdate(blockPos2, CandleCakeBlock.byCandle($$8));
                level1.m_142346_(player3, GameEvent.BLOCK_CHANGE, blockPos2);
                player3.awardStat(Stats.ITEM_USED.get($$7));
                return InteractionResult.SUCCESS;
            }
        }
        if (level1.isClientSide) {
            if (eat(level1, blockPos2, blockState0, player3).consumesAction()) {
                return InteractionResult.SUCCESS;
            }
            if ($$6.isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }
        return eat(level1, blockPos2, blockState0, player3);
    }

    protected static InteractionResult eat(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2, Player player3) {
        if (!player3.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            player3.awardStat(Stats.EAT_CAKE_SLICE);
            player3.getFoodData().eat(2, 0.1F);
            int $$4 = (Integer) blockState2.m_61143_(BITES);
            levelAccessor0.gameEvent(player3, GameEvent.EAT, blockPos1);
            if ($$4 < 6) {
                levelAccessor0.m_7731_(blockPos1, (BlockState) blockState2.m_61124_(BITES, $$4 + 1), 3);
            } else {
                levelAccessor0.m_7471_(blockPos1, false);
                levelAccessor0.gameEvent(player3, GameEvent.BLOCK_DESTROY, blockPos1);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1 == Direction.DOWN && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return levelReader1.m_8055_(blockPos2.below()).m_280296_();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(BITES);
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return getOutputSignal((Integer) blockState0.m_61143_(BITES));
    }

    public static int getOutputSignal(int int0) {
        return (7 - int0) * 2;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}