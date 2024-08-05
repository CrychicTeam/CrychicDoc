package com.simibubi.create.content.logistics.chute;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.logistics.funnel.FunnelBlock;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;

public class ChuteBlock extends AbstractChuteBlock implements ProperWaterloggedBlock {

    public static final Property<ChuteBlock.Shape> SHAPE = EnumProperty.create("shape", ChuteBlock.Shape.class);

    public static final DirectionProperty FACING = BlockStateProperties.FACING_HOPPER;

    public ChuteBlock(BlockBehaviour.Properties p_i48440_1_) {
        super(p_i48440_1_);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(SHAPE, ChuteBlock.Shape.NORMAL)).m_61124_(FACING, Direction.DOWN)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public Direction getFacing(BlockState state) {
        return (Direction) state.m_61143_(FACING);
    }

    @Override
    public boolean isOpen(BlockState state) {
        return state.m_61143_(FACING) == Direction.DOWN || state.m_61143_(SHAPE) == ChuteBlock.Shape.INTERSECTION;
    }

    @Override
    public boolean isTransparent(BlockState state) {
        return state.m_61143_(SHAPE) == ChuteBlock.Shape.WINDOW;
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return this.fluidState(pState);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        ChuteBlock.Shape shape = (ChuteBlock.Shape) state.m_61143_(SHAPE);
        boolean down = state.m_61143_(FACING) == Direction.DOWN;
        if (shape == ChuteBlock.Shape.INTERSECTION) {
            return InteractionResult.PASS;
        } else {
            Level level = context.getLevel();
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else if (shape == ChuteBlock.Shape.ENCASED) {
                level.setBlockAndUpdate(context.getClickedPos(), (BlockState) state.m_61124_(SHAPE, ChuteBlock.Shape.NORMAL));
                level.m_46796_(2001, context.getClickedPos(), Block.getId(AllBlocks.INDUSTRIAL_IRON_BLOCK.getDefaultState()));
                return InteractionResult.SUCCESS;
            } else {
                if (down) {
                    level.setBlockAndUpdate(context.getClickedPos(), (BlockState) state.m_61124_(SHAPE, shape != ChuteBlock.Shape.NORMAL ? ChuteBlock.Shape.NORMAL : ChuteBlock.Shape.WINDOW));
                }
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ChuteBlock.Shape shape = (ChuteBlock.Shape) state.m_61143_(SHAPE);
        if (!AllBlocks.INDUSTRIAL_IRON_BLOCK.isIn(player.m_21120_(hand))) {
            return super.use(state, level, pos, player, hand, hitResult);
        } else if (shape == ChuteBlock.Shape.INTERSECTION || shape == ChuteBlock.Shape.ENCASED) {
            return super.use(state, level, pos, player, hand, hitResult);
        } else if (player != null && !level.isClientSide) {
            level.setBlockAndUpdate(pos, (BlockState) state.m_61124_(SHAPE, ChuteBlock.Shape.ENCASED));
            level.playSound(null, pos, SoundEvents.NETHERITE_BLOCK_HIT, SoundSource.BLOCKS, 0.5F, 1.05F);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = this.withWater(super.m_5573_(ctx), ctx);
        Direction face = ctx.m_43719_();
        if (face.getAxis().isHorizontal() && !ctx.m_7078_()) {
            Level world = ctx.m_43725_();
            BlockPos pos = ctx.getClickedPos();
            return this.updateChuteState((BlockState) state.m_61124_(FACING, face), world.getBlockState(pos.above()), world, pos);
        } else {
            return state;
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState above, LevelAccessor world, BlockPos pos, BlockPos p_196271_6_) {
        this.updateWater(world, state, pos);
        return super.updateShape(state, direction, above, world, pos, p_196271_6_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        super.m_7926_(p_206840_1_.add(SHAPE, FACING, WATERLOGGED));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState above = world.m_8055_(pos.above());
        return !isChute(above) || getChuteFacing(above) == Direction.DOWN;
    }

    @Override
    public BlockState updateChuteState(BlockState state, BlockState above, BlockGetter world, BlockPos pos) {
        if (!(state.m_60734_() instanceof ChuteBlock)) {
            return state;
        } else {
            Map<Direction, Boolean> connections = new HashMap();
            int amtConnections = 0;
            Direction facing = (Direction) state.m_61143_(FACING);
            boolean vertical = facing == Direction.DOWN;
            if (!vertical) {
                BlockState target = world.getBlockState(pos.below().relative(facing.getOpposite()));
                if (!isChute(target)) {
                    return (BlockState) ((BlockState) state.m_61124_(FACING, Direction.DOWN)).m_61124_(SHAPE, ChuteBlock.Shape.NORMAL);
                }
            }
            for (Direction direction : Iterate.horizontalDirections) {
                BlockState diagonalInputChute = world.getBlockState(pos.above().relative(direction));
                boolean value = diagonalInputChute.m_60734_() instanceof ChuteBlock && diagonalInputChute.m_61143_(FACING) == direction;
                connections.put(direction, value);
                if (value) {
                    amtConnections++;
                }
            }
            boolean noConnections = amtConnections == 0;
            if (vertical) {
                return (BlockState) state.m_61124_(SHAPE, noConnections ? (state.m_61143_(SHAPE) == ChuteBlock.Shape.INTERSECTION ? ChuteBlock.Shape.NORMAL : (ChuteBlock.Shape) state.m_61143_(SHAPE)) : ChuteBlock.Shape.INTERSECTION);
            } else if (noConnections) {
                return (BlockState) state.m_61124_(SHAPE, ChuteBlock.Shape.INTERSECTION);
            } else if ((Boolean) connections.get(Direction.NORTH) && (Boolean) connections.get(Direction.SOUTH)) {
                return (BlockState) state.m_61124_(SHAPE, ChuteBlock.Shape.INTERSECTION);
            } else if ((Boolean) connections.get(Direction.EAST) && (Boolean) connections.get(Direction.WEST)) {
                return (BlockState) state.m_61124_(SHAPE, ChuteBlock.Shape.INTERSECTION);
            } else {
                return amtConnections == 1 && connections.get(facing) && getChuteFacing(above) != Direction.DOWN && (!(above.m_60734_() instanceof FunnelBlock) || FunnelBlock.getFunnelFacing(above) != Direction.DOWN) ? (BlockState) state.m_61124_(SHAPE, state.m_61143_(SHAPE) == ChuteBlock.Shape.ENCASED ? ChuteBlock.Shape.ENCASED : ChuteBlock.Shape.NORMAL) : (BlockState) state.m_61124_(SHAPE, ChuteBlock.Shape.INTERSECTION);
            }
        }
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return (BlockState) pState.m_61124_(FACING, pRot.rotate((Direction) pState.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.m_60717_(pMirror.getRotation((Direction) pState.m_61143_(FACING)));
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public BlockEntityType<? extends ChuteBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends ChuteBlockEntity>) AllBlockEntityTypes.CHUTE.get();
    }

    public static enum Shape implements StringRepresentable {

        INTERSECTION, WINDOW, NORMAL, ENCASED;

        @Override
        public String getSerializedName() {
            return Lang.asId(this.name());
        }
    }
}