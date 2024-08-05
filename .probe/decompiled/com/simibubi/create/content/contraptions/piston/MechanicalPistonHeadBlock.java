package com.simibubi.create.content.contraptions.piston;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MechanicalPistonHeadBlock extends WrenchableDirectionalBlock implements SimpleWaterloggedBlock {

    public static final EnumProperty<PistonType> TYPE = BlockStateProperties.PISTON_TYPE;

    public MechanicalPistonHeadBlock(BlockBehaviour.Properties p_i48415_1_) {
        super(p_i48415_1_);
        this.m_49959_((BlockState) super.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return AllBlocks.PISTON_EXTENSION_POLE.asStack();
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        Direction direction = (Direction) state.m_61143_(f_52588_);
        BlockPos pistonBase = null;
        for (int offset = 1; offset < MechanicalPistonBlock.maxAllowedPistonPoles(); offset++) {
            BlockPos currentPos = pos.relative(direction.getOpposite(), offset);
            BlockState block = worldIn.getBlockState(currentPos);
            if (!MechanicalPistonBlock.isExtensionPole(block) || direction.getAxis() != ((Direction) block.m_61143_(BlockStateProperties.FACING)).getAxis()) {
                if (MechanicalPistonBlock.isPiston(block) && block.m_61143_(BlockStateProperties.FACING) == direction) {
                    pistonBase = currentPos;
                }
                break;
            }
        }
        if (pos != null && pistonBase != null) {
            BlockPos basePos = pistonBase;
            BlockPos.betweenClosedStream(pistonBase, pos).filter(p -> !p.equals(pos) && !p.equals(basePos)).forEach(p -> worldIn.m_46961_(p, !player.isCreative()));
            worldIn.setBlockAndUpdate(basePos, (BlockState) worldIn.getBlockState(basePos).m_61124_(MechanicalPistonBlock.STATE, MechanicalPistonBlock.PistonState.RETRACTED));
        }
        super.m_5707_(worldIn, pos, state, player);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.MECHANICAL_PISTON_HEAD.get((Direction) state.m_61143_(f_52588_));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        if ((Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(world));
        }
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState FluidState = context.m_43725_().getFluidState(context.getClickedPos());
        return (BlockState) super.getStateForPlacement(context).m_61124_(BlockStateProperties.WATERLOGGED, FluidState.getType() == Fluids.WATER);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}