package net.mehvahdjukaar.supplementaries.common.block.blocks;

import com.google.common.base.Preconditions;
import net.mehvahdjukaar.moonlight.api.block.IBeeGrowable;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FlaxBlock extends CropBlock implements IBeeGrowable {

    public static final int DOUBLE_AGE = 4;

    private static final VoxelShape FULL_BOTTOM = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    private static final VoxelShape[] SHAPES_BOTTOM = new VoxelShape[] { Block.box(4.0, 0.0, 4.0, 12.0, 6.0, 12.0), Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0), Block.box(3.0, 0.0, 3.0, 13.0, 13.0, 13.0), Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0), Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0), FULL_BOTTOM, FULL_BOTTOM, FULL_BOTTOM };

    private static final VoxelShape[] SHAPES_TOP = new VoxelShape[] { FULL_BOTTOM, FULL_BOTTOM, FULL_BOTTOM, FULL_BOTTOM, Block.box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), Block.box(1.0, 0.0, 1.0, 15.0, 7.0, 15.0), Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0), Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0) };

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public FlaxBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(this.m_7959_(), 0)).m_61124_(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.m_61143_(HALF) == DoubleBlockHalf.LOWER ? SHAPES_BOTTOM[state.m_61143_(f_52244_)] : SHAPES_TOP[state.m_61143_(f_52244_)];
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        DoubleBlockHalf half = (DoubleBlockHalf) stateIn.m_61143_(HALF);
        if (facing.getAxis() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (facing == Direction.UP) || this.isSingle(stateIn) || facingState.m_60713_(this) && facingState.m_61143_(HALF) != half) {
            return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.m_60710_(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    public boolean isSingle(BlockState state) {
        return this.m_52305_(state) < 4;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        if (!state.m_60713_(this)) {
            throw new IllegalArgumentException("Somebody (a mod) passed a [" + Utils.getID(state.m_60734_()) + "] blockstate to a member function of [Flax] block. This breaks a contract");
        } else if (state.m_61143_(HALF) == DoubleBlockHalf.LOWER) {
            BlockState above = worldIn.m_8055_(pos.above());
            return above.m_60734_() == this && this.isSingle(above) ? false : super.canSurvive(state, worldIn, pos);
        } else if (this.isSingle(state)) {
            return false;
        } else {
            BlockState blockstate = worldIn.m_8055_(pos.below());
            return state.m_60734_() != this ? super.canSurvive(state, worldIn, pos) : blockstate.m_60713_(this) && blockstate.m_61143_(HALF) == DoubleBlockHalf.LOWER && this.m_52305_(state) == this.m_52305_(blockstate);
        }
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (!worldIn.isClientSide) {
            if (player.isCreative()) {
                removeBottomHalf(worldIn, pos, state, player);
            } else {
                m_49881_(state, worldIn, pos, null, player, player.m_21205_());
            }
        }
        super.m_5707_(worldIn, pos, state, player);
    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        super.m_6240_(worldIn, player, pos, Blocks.AIR.defaultBlockState(), te, stack);
    }

    protected static void removeBottomHalf(Level world, BlockPos pos, BlockState state, Player player) {
        DoubleBlockHalf doubleblockhalf = (DoubleBlockHalf) state.m_61143_(HALF);
        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
            BlockPos blockpos = pos.below();
            BlockState blockstate = world.getBlockState(blockpos);
            if (blockstate.m_60734_() == state.m_60734_() && blockstate.m_61143_(HALF) == DoubleBlockHalf.LOWER) {
                world.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                world.m_5898_(player, 2001, blockpos, Block.getId(blockstate));
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    public void placeAt(LevelAccessor worldIn, BlockPos pos, int flags) {
        worldIn.m_7731_(pos, (BlockState) this.m_49966_().m_61124_(HALF, DoubleBlockHalf.LOWER), flags);
        worldIn.m_7731_(pos.above(), (BlockState) this.m_49966_().m_61124_(HALF, DoubleBlockHalf.UPPER), flags);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (PlatHelper.isAreaLoaded(level, pos, 1)) {
            if (state.m_61143_(HALF) != DoubleBlockHalf.UPPER) {
                if (level.m_45524_(pos, 0) >= 9 && this.isValidBonemealTarget(level, pos, state, level.f_46443_)) {
                    float f = m_52272_(this, level, pos);
                    if (ForgeHelper.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0)) {
                        this.growCropBy(level, pos, state, 1);
                        ForgeHelper.onCropsGrowPost(level, pos, state);
                    }
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        Preconditions.checkArgument(state.m_60713_(this), "Some mod passed a block that is not this own to the use method. This is bad!");
        InteractionResult old = super.m_6227_(state, world, pos, player, hand, rayTraceResult);
        if (!old.consumesAction() && !this.isSingle(state) && state.m_61143_(HALF) == DoubleBlockHalf.UPPER) {
            InteractionResult ev = ForgeHelper.onRightClickBlock(player, hand, pos.below(), rayTraceResult);
            if (ev != null) {
                return ev;
            }
            BlockState below = world.getBlockState(pos.below());
            if (below.m_60713_(this)) {
                return this.use(below, world, pos.below(), player, hand, rayTraceResult);
            }
        }
        return old;
    }

    public boolean canGrowUp(BlockGetter worldIn, BlockPos downPos) {
        BlockState state = worldIn.getBlockState(downPos.above());
        return state.m_60734_() instanceof FlaxBlock || state.m_247087_();
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return !this.m_52307_(state) && (this.canGrowUp(worldIn, pos) || this.m_52305_(state) < 3);
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        this.growCropBy(level, pos, state, this.m_7125_(level));
    }

    public void growCropBy(Level level, BlockPos pos, BlockState state, int increment) {
        if (increment > 1 && this.isSingle(state) && !this.canGrowUp(level, pos)) {
            increment = 1;
        }
        if (state.m_61143_(HALF) == DoubleBlockHalf.UPPER) {
            pos = pos.below();
        }
        int newAge = this.m_52305_(state) + increment;
        newAge = Math.min(newAge, this.m_7419_());
        if (newAge >= 4) {
            level.setBlock(pos.above(), (BlockState) this.m_52289_(newAge).m_61124_(HALF, DoubleBlockHalf.UPPER), 2);
        }
        level.setBlock(pos, this.m_52289_(newAge), 3);
    }

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        return false;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return (ItemLike) ModRegistry.FLAX_SEEDS_ITEM.get();
    }

    @Override
    public boolean getPollinated(Level level, BlockPos pos, BlockState state) {
        this.growCropBy(level, pos, state, 1);
        return true;
    }
}