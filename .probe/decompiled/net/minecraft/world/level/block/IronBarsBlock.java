package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IronBarsBlock extends CrossCollisionBlock {

    protected IronBarsBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(1.0F, 1.0F, 16.0F, 16.0F, 16.0F, blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_52309_, false)).m_61124_(f_52310_, false)).m_61124_(f_52311_, false)).m_61124_(f_52312_, false)).m_61124_(f_52313_, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockGetter $$1 = blockPlaceContext0.m_43725_();
        BlockPos $$2 = blockPlaceContext0.getClickedPos();
        FluidState $$3 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        BlockPos $$4 = $$2.north();
        BlockPos $$5 = $$2.south();
        BlockPos $$6 = $$2.west();
        BlockPos $$7 = $$2.east();
        BlockState $$8 = $$1.getBlockState($$4);
        BlockState $$9 = $$1.getBlockState($$5);
        BlockState $$10 = $$1.getBlockState($$6);
        BlockState $$11 = $$1.getBlockState($$7);
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(f_52309_, this.attachsTo($$8, $$8.m_60783_($$1, $$4, Direction.SOUTH)))).m_61124_(f_52311_, this.attachsTo($$9, $$9.m_60783_($$1, $$5, Direction.NORTH)))).m_61124_(f_52312_, this.attachsTo($$10, $$10.m_60783_($$1, $$6, Direction.EAST)))).m_61124_(f_52310_, this.attachsTo($$11, $$11.m_60783_($$1, $$7, Direction.WEST)))).m_61124_(f_52313_, $$3.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(f_52313_)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return direction1.getAxis().isHorizontal() ? (BlockState) blockState0.m_61124_((Property) f_52314_.get(direction1), this.attachsTo(blockState2, blockState2.m_60783_(levelAccessor3, blockPos5, direction1.getOpposite()))) : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return Shapes.empty();
    }

    @Override
    public boolean skipRendering(BlockState blockState0, BlockState blockState1, Direction direction2) {
        if (blockState1.m_60713_(this)) {
            if (!direction2.getAxis().isHorizontal()) {
                return true;
            }
            if ((Boolean) blockState0.m_61143_((Property) f_52314_.get(direction2)) && (Boolean) blockState1.m_61143_((Property) f_52314_.get(direction2.getOpposite()))) {
                return true;
            }
        }
        return super.m_6104_(blockState0, blockState1, direction2);
    }

    public final boolean attachsTo(BlockState blockState0, boolean boolean1) {
        return !m_152463_(blockState0) && boolean1 || blockState0.m_60734_() instanceof IronBarsBlock || blockState0.m_204336_(BlockTags.WALLS);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_52309_, f_52310_, f_52312_, f_52311_, f_52313_);
    }
}