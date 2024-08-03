package com.mna.api.blocks;

import com.mna.api.blocks.interfaces.ICutoutBlock;
import com.mna.api.blocks.tile.TileEntityWithInventory;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public abstract class WizardLabBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, ICutoutBlock {

    public static final BooleanProperty RIGHT = BooleanProperty.create("right");

    public static final BooleanProperty LEFT = BooleanProperty.create("left");

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public WizardLabBlock(BlockBehaviour.Properties blockProperties) {
        super(blockProperties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LEFT, false)).m_61124_(RIGHT, false)).m_61124_(WATERLOGGED, false));
    }

    @Nullable
    protected abstract MenuProvider getProvider(BlockState var1, Level var2, BlockPos var3, Player var4, InteractionHand var5, BlockHitResult var6);

    protected Consumer<FriendlyByteBuf> getContainerBufferWriter(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return null;
    }

    protected boolean canConnectToDirection(BlockState thisState, BlockState otherState, Direction dir) {
        return dir != Direction.UP && dir != Direction.DOWN ? otherState.m_60734_() instanceof WizardLabBlock && this.directionToLeftRightProperty(thisState, dir) != null : false;
    }

    protected boolean shouldPlayAmbient(BlockState state, Level world, BlockPos pos, RandomSource rnd) {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rnd) {
        if (this.shouldPlayAmbient(state, world, pos, rnd) && this.getAmbientSound() != null) {
            double d0 = (double) pos.m_123341_() + 0.5;
            double d1 = (double) pos.m_123342_();
            double d2 = (double) pos.m_123343_() + 0.5;
            if (rnd.nextDouble() < 0.1) {
                world.playLocalSound(d0, d1, d2, this.getAmbientSound(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            MenuProvider provider = this.getProvider(state, level, pos, player, hand, hitResult);
            if (provider == null) {
                return InteractionResult.SUCCESS;
            } else {
                Consumer<FriendlyByteBuf> dataWriter = this.getContainerBufferWriter(state, level, pos, player, hand, hitResult);
                if (dataWriter == null) {
                    NetworkHooks.openScreen((ServerPlayer) player, provider, pos);
                } else {
                    NetworkHooks.openScreen((ServerPlayer) player, provider, dataWriter);
                }
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEFT, RIGHT, WATERLOGGED, f_54117_);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
        return !(Boolean) state.m_61143_(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType pathType) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction blockFacingDirection = context.m_8125_().getOpposite();
        BlockGetter blockgetter = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.m_43725_().getFluidState(context.getClickedPos());
        Direction leftDir = blockFacingDirection.getCounterClockWise();
        Direction rightDir = blockFacingDirection.getClockWise();
        BlockState leftNeighborState = blockgetter.getBlockState(blockpos.offset(leftDir.getStepX(), leftDir.getStepY(), leftDir.getStepZ()));
        BlockState rightNeighborState = blockgetter.getBlockState(blockpos.offset(rightDir.getStepX(), rightDir.getStepY(), rightDir.getStepZ()));
        BlockState disconnectedState = (BlockState) ((BlockState) this.m_49966_().m_61124_(HorizontalDirectionalBlock.FACING, blockFacingDirection)).m_61124_(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(HorizontalDirectionalBlock.FACING, blockFacingDirection)).m_61124_(WATERLOGGED, fluidstate.getType() == Fluids.WATER)).m_61124_(LEFT, this.canConnectToDirection(disconnectedState, leftNeighborState, leftDir))).m_61124_(RIGHT, this.canConnectToDirection(disconnectedState, rightNeighborState, rightDir));
    }

    @Override
    public BlockState updateShape(BlockState myState, Direction direction, BlockState otherState, LevelAccessor accessor, BlockPos myPos, BlockPos otherPos) {
        if ((Boolean) myState.m_61143_(WATERLOGGED)) {
            accessor.scheduleTick(myPos, Fluids.WATER, Fluids.WATER.m_6718_(accessor));
        }
        BooleanProperty prop = this.directionToLeftRightProperty(myState, direction);
        return prop != null ? (BlockState) myState.m_61124_(prop, this.canConnectToDirection(myState, otherState, direction)) : myState;
    }

    @Nullable
    private BooleanProperty directionToLeftRightProperty(BlockState myState, Direction direction) {
        Direction myDir = (Direction) myState.m_61143_(f_54117_);
        Direction myLeft = myDir.getCounterClockWise();
        Direction myRight = myDir.getClockWise();
        if (direction == myLeft) {
            return LEFT;
        } else {
            return direction == myRight ? RIGHT : null;
        }
    }

    public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            this.dropInventory(worldIn, pos);
        }
        super.m_6810_(state, worldIn, pos, newState, isMoving);
    }

    private void dropInventory(Level world, BlockPos pos) {
        if (!world.isClientSide) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof TileEntityWithInventory) {
                Containers.dropContents(world, pos, (Container) tileEntity);
            }
        }
    }
}