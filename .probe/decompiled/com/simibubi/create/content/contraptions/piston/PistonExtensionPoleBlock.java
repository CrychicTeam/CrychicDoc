package com.simibubi.create.content.contraptions.piston;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PoleHelper;
import java.util.function.Predicate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PistonExtensionPoleBlock extends WrenchableDirectionalBlock implements IWrenchable, SimpleWaterloggedBlock {

    private static final int placementHelperId = PlacementHelpers.register(PistonExtensionPoleBlock.PlacementHelper.get());

    public PistonExtensionPoleBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(f_52588_, Direction.UP)).m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        Direction.Axis axis = ((Direction) state.m_61143_(f_52588_)).getAxis();
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        BlockPos pistonHead = null;
        BlockPos pistonBase = null;
        for (int modifier : new int[] { 1, -1 }) {
            for (int offset = modifier; modifier * offset < MechanicalPistonBlock.maxAllowedPistonPoles(); offset += modifier) {
                BlockPos currentPos = pos.relative(direction, offset);
                BlockState block = worldIn.getBlockState(currentPos);
                if (!MechanicalPistonBlock.isExtensionPole(block) || axis != ((Direction) block.m_61143_(f_52588_)).getAxis()) {
                    if (MechanicalPistonBlock.isPiston(block) && ((Direction) block.m_61143_(BlockStateProperties.FACING)).getAxis() == axis) {
                        pistonBase = currentPos;
                    }
                    if (MechanicalPistonBlock.isPistonHead(block) && ((Direction) block.m_61143_(BlockStateProperties.FACING)).getAxis() == axis) {
                        pistonHead = currentPos;
                    }
                    break;
                }
            }
        }
        if (pistonHead != null && pistonBase != null && worldIn.getBlockState(pistonHead).m_61143_(BlockStateProperties.FACING) == worldIn.getBlockState(pistonBase).m_61143_(BlockStateProperties.FACING)) {
            BlockPos basePos = pistonBase;
            BlockPos.betweenClosedStream(pistonBase, pistonHead).filter(p -> !p.equals(pos) && !p.equals(basePos)).forEach(p -> worldIn.m_46961_(p, !player.isCreative()));
            worldIn.setBlockAndUpdate(basePos, (BlockState) worldIn.getBlockState(basePos).m_61124_(MechanicalPistonBlock.STATE, MechanicalPistonBlock.PistonState.RETRACTED));
            if (worldIn.getBlockEntity(basePos) instanceof MechanicalPistonBlockEntity baseBE) {
                baseBE.offset = 0.0F;
                baseBE.onLengthBroken();
            }
        }
        super.m_5707_(worldIn, pos, state, player);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.FOUR_VOXEL_POLE.get(((Direction) state.m_61143_(f_52588_)).getAxis());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState FluidState = context.m_43725_().getFluidState(context.getClickedPos());
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(f_52588_, context.m_43719_().getOpposite())).m_61124_(BlockStateProperties.WATERLOGGED, FluidState.getType() == Fluids.WATER);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        ItemStack heldItem = player.m_21120_(hand);
        IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
        return placementHelper.matchesItem(heldItem) && !player.m_6144_() ? placementHelper.getOffset(player, world, state, pos, ray).placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, ray) : InteractionResult.PASS;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        if ((Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(world));
        }
        return state;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @MethodsReturnNonnullByDefault
    public static class PlacementHelper extends PoleHelper<Direction> {

        private static final PistonExtensionPoleBlock.PlacementHelper instance = new PistonExtensionPoleBlock.PlacementHelper();

        public static PistonExtensionPoleBlock.PlacementHelper get() {
            return instance;
        }

        private PlacementHelper() {
            super(AllBlocks.PISTON_EXTENSION_POLE::has, state -> ((Direction) state.m_61143_(DirectionalBlock.FACING)).getAxis(), DirectionalBlock.FACING);
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return AllBlocks.PISTON_EXTENSION_POLE::isIn;
        }
    }
}