package com.simibubi.create.content.fluids.pipes;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.equipment.wrench.IWrenchableWithBracket;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.Map;
import java.util.Optional;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

public class AxisPipeBlock extends RotatedPillarBlock implements IWrenchableWithBracket, IAxisPipe {

    public AxisPipeBlock(BlockBehaviour.Properties p_i48339_1_) {
        super(p_i48339_1_);
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
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!AllBlocks.COPPER_CASING.isIn(player.m_21120_(hand))) {
            return InteractionResult.PASS;
        } else if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockState newState = AllBlocks.ENCASED_FLUID_PIPE.getDefaultState();
            for (Direction d : Iterate.directionsInAxis(this.getAxis(state))) {
                newState = (BlockState) newState.m_61124_((Property) EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(d), true);
            }
            FluidTransportBehaviour.cacheFlows(world, pos);
            world.setBlockAndUpdate(pos, newState);
            FluidTransportBehaviour.loadFlows(world, pos);
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.m_6402_(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!world.isClientSide) {
            if (state != oldState) {
                world.m_186464_(pos, this, 1, TickPriority.HIGH);
            }
        }
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return AllBlocks.FLUID_PIPE.asStack();
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

    public static boolean isOpenAt(BlockState state, Direction d) {
        return d.getAxis() == state.m_61143_(f_55923_);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource r) {
        FluidPropagator.propagateChangedPipe(world, pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return AllShapes.EIGHT_VOXEL_POLE.get((Direction.Axis) state.m_61143_(f_55923_));
    }

    public BlockState toRegularPipe(LevelAccessor world, BlockPos pos, BlockState state) {
        Direction side = Direction.get(Direction.AxisDirection.POSITIVE, (Direction.Axis) state.m_61143_(f_55923_));
        Map<Direction, BooleanProperty> facingToPropertyMap = FluidPipeBlock.f_55154_;
        return ((FluidPipeBlock) AllBlocks.FLUID_PIPE.get()).updateBlockState((BlockState) ((BlockState) AllBlocks.FLUID_PIPE.getDefaultState().m_61124_((Property) facingToPropertyMap.get(side), true)).m_61124_((Property) facingToPropertyMap.get(side.getOpposite()), true), side, null, world, pos);
    }

    @Override
    public Direction.Axis getAxis(BlockState state) {
        return (Direction.Axis) state.m_61143_(f_55923_);
    }

    @Override
    public Optional<ItemStack> removeBracket(BlockGetter world, BlockPos pos, boolean inOnReplacedContext) {
        BracketedBlockEntityBehaviour behaviour = BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);
        if (behaviour == null) {
            return Optional.empty();
        } else {
            BlockState bracket = behaviour.removeBracket(inOnReplacedContext);
            return bracket == null ? Optional.empty() : Optional.of(new ItemStack(bracket.m_60734_()));
        }
    }
}