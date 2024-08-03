package com.simibubi.create.content.fluids.pipes;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.ITransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasableBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchableWithBracket;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.Arrays;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

public class FluidPipeBlock extends PipeBlock implements SimpleWaterloggedBlock, IWrenchableWithBracket, IBE<FluidPipeBlockEntity>, EncasableBlock, ITransformableBlock {

    private static final VoxelShape OCCLUSION_BOX = Block.box(4.0, 4.0, 4.0, 12.0, 12.0, 12.0);

    public FluidPipeBlock(BlockBehaviour.Properties properties) {
        super(0.25F, properties);
        this.m_49959_((BlockState) super.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (this.tryRemoveBracket(context)) {
            return InteractionResult.SUCCESS;
        } else {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            Direction clickedFace = context.getClickedFace();
            Direction.Axis axis = this.getAxis(world, pos, state);
            if (axis == null) {
                Vec3 clickLocation = context.getClickLocation().subtract((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_());
                double closest = Float.MAX_VALUE;
                Direction argClosest = Direction.UP;
                for (Direction direction : Iterate.directions) {
                    if (clickedFace.getAxis() != direction.getAxis()) {
                        Vec3 centerOf = Vec3.atCenterOf(direction.getNormal());
                        double distance = centerOf.distanceToSqr(clickLocation);
                        if (distance < closest) {
                            closest = distance;
                            argClosest = direction;
                        }
                    }
                }
                axis = argClosest.getAxis();
            }
            if (clickedFace.getAxis() == axis) {
                return InteractionResult.PASS;
            } else {
                if (!world.isClientSide) {
                    this.withBlockEntityDo(world, pos, fpte -> fpte.getBehaviour(FluidTransportBehaviour.TYPE).interfaces.values().stream().filter(pc -> pc != null && pc.hasFlow()).findAny().ifPresent($ -> AllAdvancements.GLASS_PIPE.awardTo(context.getPlayer())));
                    FluidTransportBehaviour.cacheFlows(world, pos);
                    world.setBlockAndUpdate(pos, (BlockState) ((BlockState) AllBlocks.GLASS_FLUID_PIPE.getDefaultState().m_61124_(GlassFluidPipeBlock.f_55923_, axis)).m_61124_(BlockStateProperties.WATERLOGGED, (Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)));
                    FluidTransportBehaviour.loadFlows(world, pos);
                }
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.m_6402_(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        ItemStack heldItem = player.m_21120_(hand);
        InteractionResult result = this.tryEncase(state, world, pos, heldItem, player, hand, ray);
        return result.consumesAction() ? result : InteractionResult.PASS;
    }

    public BlockState getAxisState(Direction.Axis axis) {
        BlockState defaultState = this.m_49966_();
        for (Direction d : Iterate.directions) {
            defaultState = (BlockState) defaultState.m_61124_((Property) f_55154_.get(d), d.getAxis() == axis);
        }
        return defaultState;
    }

    @Nullable
    private Direction.Axis getAxis(BlockGetter world, BlockPos pos, BlockState state) {
        return FluidPropagator.getStraightPipeAxis(state);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        boolean blockTypeChanged = state.m_60734_() != newState.m_60734_();
        if (blockTypeChanged && !world.isClientSide) {
            FluidPropagator.propagateChangedPipe(world, pos, state);
        }
        if (state != newState && !isMoving) {
            this.removeBracket(world, pos, true).ifPresent(stack -> Block.popResource(world, pos, stack));
        }
        if (state.m_155947_() && (blockTypeChanged || !newState.m_155947_())) {
            world.removeBlockEntity(pos);
        }
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!world.isClientSide) {
            if (state != oldState) {
                world.m_186464_(pos, this, 1, TickPriority.HIGH);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block otherBlock, BlockPos neighborPos, boolean isMoving) {
        DebugPackets.sendNeighborsUpdatePacket(world, pos);
        Direction d = FluidPropagator.validateNeighbourChange(state, world, pos, otherBlock, neighborPos, isMoving);
        if (d != null) {
            if (isOpenAt(state, d)) {
                world.m_186464_(pos, this, 1, TickPriority.HIGH);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource r) {
        FluidPropagator.propagateChangedPipe(world, pos, state);
    }

    public static boolean isPipe(BlockState state) {
        return state.m_60734_() instanceof FluidPipeBlock;
    }

    public static boolean canConnectTo(BlockAndTintGetter world, BlockPos neighbourPos, BlockState neighbour, Direction direction) {
        if (FluidPropagator.hasFluidCapability(world, neighbourPos, direction.getOpposite())) {
            return true;
        } else if (VanillaFluidTargets.shouldPipesConnectTo(neighbour)) {
            return true;
        } else {
            FluidTransportBehaviour transport = BlockEntityBehaviour.get(world, neighbourPos, FluidTransportBehaviour.TYPE);
            BracketedBlockEntityBehaviour bracket = BlockEntityBehaviour.get(world, neighbourPos, BracketedBlockEntityBehaviour.TYPE);
            if (!isPipe(neighbour)) {
                return transport == null ? false : transport.canHaveFlowToward(neighbour, direction.getOpposite());
            } else {
                return bracket == null || !bracket.isBracketPresent() || FluidPropagator.getStraightPipeAxis(neighbour) == direction.getAxis();
            }
        }
    }

    public static boolean shouldDrawRim(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {
        BlockPos offsetPos = pos.relative(direction);
        BlockState facingState = world.m_8055_(offsetPos);
        if (facingState.m_60734_() instanceof EncasedPipeBlock) {
            return true;
        } else {
            return !isPipe(facingState) ? true : !canConnectTo(world, offsetPos, facingState, direction);
        }
    }

    public static boolean isOpenAt(BlockState state, Direction direction) {
        return (Boolean) state.m_61143_((Property) f_55154_.get(direction));
    }

    public static boolean isCornerOrEndPipe(BlockAndTintGetter world, BlockPos pos, BlockState state) {
        return isPipe(state) && FluidPropagator.getStraightPipeAxis(state) == null && !shouldDrawCasing(world, pos, state);
    }

    public static boolean shouldDrawCasing(BlockAndTintGetter world, BlockPos pos, BlockState state) {
        if (!isPipe(state)) {
            return false;
        } else {
            for (Direction.Axis axis : Iterate.axes) {
                int connections = 0;
                for (Direction direction : Iterate.directions) {
                    if (direction.getAxis() != axis && isOpenAt(state, direction)) {
                        connections++;
                    }
                }
                if (connections > 2) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_55148_, f_55149_, f_55150_, f_55151_, f_55152_, f_55153_, BlockStateProperties.WATERLOGGED);
        super.m_7926_(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState FluidState = context.m_43725_().getFluidState(context.getClickedPos());
        return (BlockState) this.updateBlockState(this.m_49966_(), context.getNearestLookingDirection(), null, context.m_43725_(), context.getClickedPos()).m_61124_(BlockStateProperties.WATERLOGGED, FluidState.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        if ((Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(world));
        }
        if (isOpenAt(state, direction) && neighbourState.m_61138_(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, this, 1, TickPriority.HIGH);
        }
        return this.updateBlockState(state, direction, direction.getOpposite(), world, pos);
    }

    public BlockState updateBlockState(BlockState state, Direction preferredDirection, @Nullable Direction ignore, BlockAndTintGetter world, BlockPos pos) {
        BracketedBlockEntityBehaviour bracket = BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);
        if (bracket != null && bracket.isBracketPresent()) {
            return state;
        } else {
            BlockState prevState = state;
            int prevStateSides = (int) Arrays.stream(Iterate.directions).map(f_55154_::get).filter(state::m_61143_).count();
            for (Direction d : Iterate.directions) {
                if (d != ignore) {
                    boolean shouldConnect = canConnectTo(world, pos.relative(d), world.m_8055_(pos.relative(d)), d);
                    state = (BlockState) state.m_61124_((Property) f_55154_.get(d), shouldConnect);
                }
            }
            Direction connectedDirection = null;
            for (Direction dx : Iterate.directions) {
                if (isOpenAt(state, dx)) {
                    if (connectedDirection != null) {
                        return state;
                    }
                    connectedDirection = dx;
                }
            }
            if (connectedDirection != null) {
                return (BlockState) state.m_61124_((Property) f_55154_.get(connectedDirection.getOpposite()), true);
            } else {
                return prevStateSides == 2 ? prevState : (BlockState) ((BlockState) state.m_61124_((Property) f_55154_.get(preferredDirection), true)).m_61124_((Property) f_55154_.get(preferredDirection.getOpposite()), true);
            }
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public Optional<ItemStack> removeBracket(BlockGetter world, BlockPos pos, boolean inOnReplacedContext) {
        BracketedBlockEntityBehaviour behaviour = BracketedBlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);
        if (behaviour == null) {
            return Optional.empty();
        } else {
            BlockState bracket = behaviour.removeBracket(inOnReplacedContext);
            return bracket == null ? Optional.empty() : Optional.of(new ItemStack(bracket.m_60734_()));
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<FluidPipeBlockEntity> getBlockEntityClass() {
        return FluidPipeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FluidPipeBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends FluidPipeBlockEntity>) AllBlockEntityTypes.FLUID_PIPE.get();
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return false;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return OCCLUSION_BOX;
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return FluidPipeBlockRotation.rotate(pState, pRotation);
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return FluidPipeBlockRotation.mirror(pState, pMirror);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return FluidPipeBlockRotation.transform(state, transform);
    }
}