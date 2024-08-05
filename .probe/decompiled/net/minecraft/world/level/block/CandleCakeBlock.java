package net.minecraft.world.level.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CandleCakeBlock extends AbstractCandleBlock {

    public static final BooleanProperty LIT = AbstractCandleBlock.LIT;

    protected static final float AABB_OFFSET = 1.0F;

    protected static final VoxelShape CAKE_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0);

    protected static final VoxelShape CANDLE_SHAPE = Block.box(7.0, 8.0, 7.0, 9.0, 14.0, 9.0);

    protected static final VoxelShape SHAPE = Shapes.or(CAKE_SHAPE, CANDLE_SHAPE);

    private static final Map<Block, CandleCakeBlock> BY_CANDLE = Maps.newHashMap();

    private static final Iterable<Vec3> PARTICLE_OFFSETS = ImmutableList.of(new Vec3(0.5, 1.0, 0.5));

    protected CandleCakeBlock(Block block0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LIT, false));
        BY_CANDLE.put(block0, this);
    }

    @Override
    protected Iterable<Vec3> getParticleOffsets(BlockState blockState0) {
        return PARTICLE_OFFSETS;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        ItemStack $$6 = player3.m_21120_(interactionHand4);
        if ($$6.is(Items.FLINT_AND_STEEL) || $$6.is(Items.FIRE_CHARGE)) {
            return InteractionResult.PASS;
        } else if (candleHit(blockHitResult5) && player3.m_21120_(interactionHand4).isEmpty() && (Boolean) blockState0.m_61143_(LIT)) {
            m_151899_(player3, blockState0, level1, blockPos2);
            return InteractionResult.sidedSuccess(level1.isClientSide);
        } else {
            InteractionResult $$7 = CakeBlock.eat(level1, blockPos2, Blocks.CAKE.defaultBlockState(), player3);
            if ($$7.consumesAction()) {
                m_49950_(blockState0, level1, blockPos2);
            }
            return $$7;
        }
    }

    private static boolean candleHit(BlockHitResult blockHitResult0) {
        return blockHitResult0.m_82450_().y - (double) blockHitResult0.getBlockPos().m_123342_() > 0.5;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(LIT);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return new ItemStack(Blocks.CAKE);
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
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return CakeBlock.FULL_CAKE_SIGNAL;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    public static BlockState byCandle(Block block0) {
        return ((CandleCakeBlock) BY_CANDLE.get(block0)).m_49966_();
    }

    public static boolean canLight(BlockState blockState0) {
        return blockState0.m_204338_(BlockTags.CANDLE_CAKES, p_152896_ -> p_152896_.m_61138_(LIT) && !(Boolean) blockState0.m_61143_(LIT));
    }
}