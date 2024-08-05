package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public class GoldTrapdoorBlock extends TrapDoorBlock {

    public GoldTrapdoorBlock(BlockBehaviour.Properties properties) {
        super(properties, BlockSetType.GOLD);
    }

    public boolean canBeOpened(BlockState state) {
        return !(Boolean) state.m_61143_(f_57516_);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (this.canBeOpened(state)) {
            state = (BlockState) state.m_61122_(f_57514_);
            worldIn.setBlock(pos, state, 2);
            if ((Boolean) state.m_61143_(f_57517_)) {
                worldIn.m_186469_(pos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
            }
            this.m_57527_(player, worldIn, pos, (Boolean) state.m_61143_(f_57514_));
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            boolean hasPower = worldIn.m_276867_(pos);
            if (hasPower != (Boolean) state.m_61143_(f_57516_)) {
                worldIn.setBlock(pos, (BlockState) state.m_61124_(f_57516_, hasPower), 2);
                if ((Boolean) state.m_61143_(f_57517_)) {
                    worldIn.m_186469_(pos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
                }
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = this.m_49966_();
        FluidState fluidstate = context.m_43725_().getFluidState(context.getClickedPos());
        Direction direction = context.m_43719_();
        if (!context.replacingClickedOnBlock() && direction.getAxis().isHorizontal()) {
            blockstate = (BlockState) ((BlockState) blockstate.m_61124_(f_54117_, direction)).m_61124_(f_57515_, context.m_43720_().y - (double) context.getClickedPos().m_123342_() > 0.5 ? Half.TOP : Half.BOTTOM);
        } else {
            blockstate = (BlockState) ((BlockState) blockstate.m_61124_(f_54117_, context.m_8125_().getOpposite())).m_61124_(f_57515_, direction == Direction.UP ? Half.BOTTOM : Half.TOP);
        }
        if (context.m_43725_().m_276867_(context.getClickedPos())) {
            blockstate = (BlockState) blockstate.m_61124_(f_57516_, Boolean.TRUE);
        }
        return (BlockState) blockstate.m_61124_(f_57517_, fluidstate.getType() == Fluids.WATER);
    }
}