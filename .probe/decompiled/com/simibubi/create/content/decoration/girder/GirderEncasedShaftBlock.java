package com.simibubi.create.content.decoration.girder;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.HorizontalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GirderEncasedShaftBlock extends HorizontalAxisKineticBlock implements IBE<KineticBlockEntity>, SimpleWaterloggedBlock, IWrenchable, ISpecialBlockItemRequirement {

    public static final BooleanProperty TOP = GirderBlock.TOP;

    public static final BooleanProperty BOTTOM = GirderBlock.BOTTOM;

    public GirderEncasedShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) super.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, false)).m_61124_(TOP, false)).m_61124_(BOTTOM, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(TOP, BOTTOM, BlockStateProperties.WATERLOGGED));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.GIRDER_BEAM_SHAFT.get((Direction.Axis) pState.m_61143_(HORIZONTAL_AXIS));
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return Shapes.or(super.m_7947_(pState, pReader, pPos), AllShapes.EIGHT_VOXEL_POLE.get(Direction.Axis.Y));
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) AllBlocks.METAL_GIRDER.getDefaultState().m_61124_(BlockStateProperties.WATERLOGGED, (Boolean) originalState.m_61143_(BlockStateProperties.WATERLOGGED))).m_61124_(GirderBlock.X, originalState.m_61143_(HORIZONTAL_AXIS) == Direction.Axis.Z)).m_61124_(GirderBlock.Z, originalState.m_61143_(HORIZONTAL_AXIS) == Direction.Axis.X)).m_61124_(GirderBlock.AXIS, originalState.m_61143_(HORIZONTAL_AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X)).m_61124_(GirderBlock.BOTTOM, (Boolean) originalState.m_61143_(BOTTOM))).m_61124_(GirderBlock.TOP, (Boolean) originalState.m_61143_(TOP));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        InteractionResult onWrenched = super.onWrenched(state, context);
        Player player = context.getPlayer();
        if (onWrenched == InteractionResult.SUCCESS && player != null && !player.isCreative()) {
            player.getInventory().placeItemBackInInventory(AllBlocks.SHAFT.asStack());
        }
        return onWrenched;
    }

    @Override
    public Class<KineticBlockEntity> getBlockEntityClass() {
        return KineticBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends KineticBlockEntity>) AllBlockEntityTypes.ENCASED_SHAFT.get();
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
        Property<Boolean> updateProperty = direction == Direction.UP ? TOP : BOTTOM;
        if (direction.getAxis().isVertical()) {
            if (world.m_8055_(pos.relative(direction)).m_60816_(world, pos.relative(direction)).isEmpty()) {
                state = (BlockState) state.m_61124_(updateProperty, false);
            }
            return GirderBlock.updateVerticalProperty(world, pos, state, updateProperty, neighbourState, direction);
        } else {
            return state;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.m_43725_();
        BlockPos pos = context.getClickedPos();
        FluidState ifluidstate = level.getFluidState(pos);
        BlockState state = super.getStateForPlacement(context);
        return (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        return ItemRequirement.of(AllBlocks.SHAFT.getDefaultState(), be).union(ItemRequirement.of(AllBlocks.METAL_GIRDER.getDefaultState(), be));
    }
}