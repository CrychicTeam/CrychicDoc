package vectorwing.farmersdelight.common.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class TatamiBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final BooleanProperty PAIRED = BooleanProperty.create("paired");

    public TatamiBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49965_().any()).m_61124_(FACING, Direction.DOWN)).m_61124_(PAIRED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction face = context.m_43719_();
        BlockPos targetPos = context.getClickedPos().relative(face.getOpposite());
        BlockState targetState = context.m_43725_().getBlockState(targetPos);
        boolean pairing = false;
        if (context.m_43723_() != null && !context.m_43723_().m_6144_() && targetState.m_60734_() == this && !(Boolean) targetState.m_61143_(PAIRED)) {
            pairing = true;
        }
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_43719_().getOpposite())).m_61124_(PAIRED, pairing);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            if (placer != null && placer.m_6144_()) {
                return;
            }
            BlockPos facingPos = pos.relative((Direction) state.m_61143_(FACING));
            BlockState facingState = level.getBlockState(facingPos);
            if (facingState.m_60734_() == this && !(Boolean) facingState.m_61143_(PAIRED)) {
                level.setBlock(facingPos, (BlockState) ((BlockState) state.m_61124_(FACING, ((Direction) state.m_61143_(FACING)).getOpposite())).m_61124_(PAIRED, true), 3);
                level.m_6289_(pos, Blocks.AIR);
                state.m_60701_(level, pos, 3);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing.equals(stateIn.m_61143_(FACING)) && stateIn.m_61143_(PAIRED) && level.m_8055_(facingPos).m_60734_() != this ? (BlockState) stateIn.m_61124_(PAIRED, false) : super.m_7417_(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PAIRED);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }
}