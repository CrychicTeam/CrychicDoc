package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.supplementaries.common.block.tiles.KeyLockableTile;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.QuarkCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;

public class GoldDoorBlock extends DoorBlock {

    public GoldDoorBlock(BlockBehaviour.Properties builder) {
        super(builder, BlockSetType.GOLD);
    }

    public boolean canBeOpened(BlockState state) {
        return !(Boolean) state.m_61143_(f_52729_);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (this.canBeOpened(state)) {
            tryOpenDoubleDoor(level, state, pos);
            state = (BlockState) state.m_61122_(f_52727_);
            level.setBlock(pos, state, 10);
            this.m_245755_(player, level, pos, (Boolean) state.m_61143_(f_52727_));
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        boolean hasPower = worldIn.m_276867_(pos) || worldIn.m_276867_(pos.relative(state.m_61143_(f_52730_) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
        if (blockIn != this && hasPower != (Boolean) state.m_61143_(f_52729_)) {
            worldIn.setBlock(pos, (BlockState) state.m_61124_(f_52729_, hasPower), 2);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null) {
            state.m_61124_(f_52727_, false);
        }
        return state;
    }

    public static void tryOpenDoubleDoor(Level world, BlockState state, BlockPos pos) {
        if (CompatHandler.QUARK && QuarkCompat.isDoubleDoorEnabled() || CompatHandler.DOUBLEDOORS) {
            Direction direction = (Direction) state.m_61143_(DoorBlock.FACING);
            boolean isOpen = (Boolean) state.m_61143_(DoorBlock.OPEN);
            DoorHingeSide isMirrored = (DoorHingeSide) state.m_61143_(DoorBlock.HINGE);
            BlockPos mirrorPos = pos.relative(isMirrored == DoorHingeSide.RIGHT ? direction.getCounterClockWise() : direction.getClockWise());
            BlockPos doorPos = state.m_61143_(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? mirrorPos : mirrorPos.below();
            BlockState other = world.getBlockState(doorPos);
            if (other.m_60734_() == state.m_60734_() && other.m_61143_(DoorBlock.FACING) == direction && !(Boolean) other.m_61143_(DoorBlock.POWERED) && (Boolean) other.m_61143_(DoorBlock.OPEN) == isOpen && other.m_61143_(DoorBlock.HINGE) != isMirrored) {
                BlockState newState = (BlockState) other.m_61122_(DoorBlock.OPEN);
                world.setBlock(doorPos, newState, 10);
            }
        }
    }

    public static void tryOpenDoubleDoorKey(Level world, BlockState state, BlockPos pos, Player player, InteractionHand hand) {
        if (CompatHandler.QUARK && QuarkCompat.isDoubleDoorEnabled() || CompatHandler.DOUBLEDOORS) {
            Direction direction = (Direction) state.m_61143_(DoorBlock.FACING);
            boolean isOpen = (Boolean) state.m_61143_(DoorBlock.OPEN);
            DoorHingeSide isMirrored = (DoorHingeSide) state.m_61143_(DoorBlock.HINGE);
            BlockPos mirrorPos = pos.relative(isMirrored == DoorHingeSide.RIGHT ? direction.getCounterClockWise() : direction.getClockWise());
            BlockPos doorPos = state.m_61143_(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? mirrorPos : mirrorPos.below();
            BlockState other = world.getBlockState(doorPos);
            if (other.m_60734_() == state.m_60734_() && other.m_61143_(DoorBlock.FACING) == direction && (Boolean) other.m_61143_(DoorBlock.OPEN) == isOpen && other.m_61143_(DoorBlock.HINGE) != isMirrored && world.getBlockEntity(doorPos) instanceof KeyLockableTile keyLockableTile && keyLockableTile.handleAction(player, hand, "door")) {
                BlockState newState = (BlockState) other.m_61122_(DoorBlock.OPEN);
                world.setBlock(doorPos, newState, 10);
            }
        }
    }
}