package com.simibubi.create.content.redstone.link;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RedstoneLinkBlock extends WrenchableDirectionalBlock implements IBE<RedstoneLinkBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final BooleanProperty RECEIVER = BooleanProperty.create("receiver");

    public RedstoneLinkBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(POWERED, false)).m_61124_(RECEIVER, false));
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            Direction blockFacing = (Direction) state.m_61143_(f_52588_);
            if (fromPos.equals(pos.relative(blockFacing.getOpposite())) && !this.canSurvive(state, worldIn, pos)) {
                worldIn.m_46961_(pos, true);
            } else {
                if (!worldIn.m_183326_().willTickThisTick(pos, this)) {
                    worldIn.m_186460_(pos, this, 0);
                }
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource r) {
        this.updateTransmittedSignal(state, worldIn, pos);
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.m_60734_() != oldState.m_60734_() && !isMoving) {
            this.updateTransmittedSignal(state, worldIn, pos);
        }
    }

    public void updateTransmittedSignal(BlockState state, Level worldIn, BlockPos pos) {
        if (!worldIn.isClientSide) {
            if (!(Boolean) state.m_61143_(RECEIVER)) {
                int power = this.getPower(worldIn, pos);
                boolean previouslyPowered = (Boolean) state.m_61143_(POWERED);
                if (previouslyPowered != power > 0) {
                    worldIn.setBlock(pos, (BlockState) state.m_61122_(POWERED), 2);
                }
                this.withBlockEntityDo(worldIn, pos, be -> be.transmit(power));
            }
        }
    }

    private int getPower(Level worldIn, BlockPos pos) {
        int power = 0;
        for (Direction direction : Iterate.directions) {
            power = Math.max(worldIn.m_277185_(pos.relative(direction), direction), power);
        }
        for (Direction direction : Iterate.directions) {
            power = Math.max(worldIn.m_277185_(pos.relative(direction), Direction.UP), power);
        }
        return power;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return (Boolean) state.m_61143_(POWERED) && (Boolean) state.m_61143_(RECEIVER);
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return side != blockState.m_61143_(f_52588_) ? 0 : this.getSignal(blockState, blockAccess, pos, side);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return !state.m_61143_(RECEIVER) ? 0 : (Integer) this.getBlockEntityOptional(blockAccess, pos).map(RedstoneLinkBlockEntity::getReceivedSignal).orElse(0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, RECEIVER);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        return player.m_6144_() ? this.toggleMode(state, worldIn, pos) : InteractionResult.PASS;
    }

    public InteractionResult toggleMode(BlockState state, Level worldIn, BlockPos pos) {
        return worldIn.isClientSide ? InteractionResult.SUCCESS : this.onBlockEntityUse(worldIn, pos, be -> {
            Boolean wasReceiver = (Boolean) state.m_61143_(RECEIVER);
            boolean blockPowered = worldIn.m_276867_(pos);
            worldIn.setBlock(pos, (BlockState) ((BlockState) state.m_61122_(RECEIVER)).m_61124_(POWERED, blockPowered), 3);
            be.transmit(wasReceiver ? 0 : this.getPower(worldIn, pos));
            return InteractionResult.SUCCESS;
        });
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return this.toggleMode(state, context.getLevel(), context.getClickedPos()) == InteractionResult.SUCCESS ? InteractionResult.SUCCESS : super.onWrenched(state, context);
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction _targetedFace) {
        return originalState;
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return side != null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos neighbourPos = pos.relative(((Direction) state.m_61143_(f_52588_)).getOpposite());
        BlockState neighbour = worldIn.m_8055_(neighbourPos);
        return !neighbour.m_247087_();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.m_49966_();
        return (BlockState) state.m_61124_(f_52588_, context.m_43719_());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.REDSTONE_BRIDGE.get((Direction) state.m_61143_(f_52588_));
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<RedstoneLinkBlockEntity> getBlockEntityClass() {
        return RedstoneLinkBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RedstoneLinkBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends RedstoneLinkBlockEntity>) AllBlockEntityTypes.REDSTONE_LINK.get();
    }
}