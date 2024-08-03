package com.github.alexmodguy.alexscaves.server.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GalenaSpireBlock extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    public static final IntegerProperty SHAPE = IntegerProperty.create("shape", 0, 3);

    public static final VoxelShape SHAPE_0 = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    public static final VoxelShape SHAPE_1 = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    public static final VoxelShape SHAPE_2 = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    public static final VoxelShape SHAPE_3_TOP = Block.box(6.0, 9.0, 6.0, 10.0, 16.0, 10.0);

    public static final VoxelShape SHAPE_3_BOTTOM = Block.box(6.0, 0.0, 6.0, 10.0, 9.0, 10.0);

    public GalenaSpireBlock() {
        super(BlockBehaviour.Properties.of().mapColor(DyeColor.PURPLE).strength(1.5F).sound(SoundType.DEEPSLATE));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(DOWN, false)).m_61124_(SHAPE, 3));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.m_60824_(getter, pos);
        VoxelShape shape = SHAPE_0;
        switch(state.m_61143_(SHAPE)) {
            case 0:
                shape = SHAPE_0;
                break;
            case 1:
                shape = SHAPE_1;
                break;
            case 2:
                shape = SHAPE_2;
                break;
            case 3:
                shape = state.m_61143_(DOWN) ? SHAPE_3_TOP : SHAPE_3_BOTTOM;
        }
        return shape.move(vec3.x, vec3.y, vec3.z);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        boolean down = context.m_43719_() == Direction.DOWN;
        BlockPos blockpos = context.getClickedPos();
        BlockState above = levelaccessor.m_8055_(blockpos.above());
        BlockState below = levelaccessor.m_8055_(blockpos.below());
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(DOWN, down)).m_61124_(SHAPE, down ? this.getShapeInt(below, above, down) : this.getShapeInt(above, below, down))).m_61124_(WATERLOGGED, levelaccessor.m_6425_(blockpos).getType() == Fluids.WATER);
    }

    public int getShapeInt(BlockState above, BlockState below, boolean down) {
        if (above.m_60734_() == ACBlockRegistry.TESLA_BULB.get()) {
            return 2;
        } else if (!isGalenaSpireConnectable(above, down)) {
            return 3;
        } else {
            int aboveShape = (Integer) above.m_61143_(SHAPE);
            if (aboveShape <= 1) {
                boolean connectedUnder;
                if (down) {
                    connectedUnder = above.m_60734_() != ACBlockRegistry.GALENA_SPIRE.get();
                } else {
                    connectedUnder = below.m_60734_() != ACBlockRegistry.GALENA_SPIRE.get();
                }
                return connectedUnder ? 0 : 1;
            } else {
                return aboveShape - 1;
            }
        }
    }

    @Deprecated
    @Override
    public boolean canSurvive(BlockState state, LevelReader levelAccessor, BlockPos blockPos) {
        BlockState above = levelAccessor.m_8055_(blockPos.above());
        BlockState below = levelAccessor.m_8055_(blockPos.below());
        return state.m_61143_(DOWN) ? above.m_60783_(levelAccessor, blockPos.above(), Direction.UP) || isGalenaSpireConnectable(above, true) : below.m_60783_(levelAccessor, blockPos.below(), Direction.DOWN) || isGalenaSpireConnectable(below, false);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        BlockState prev = state.m_60710_(levelAccessor, blockPos) ? super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1) : Blocks.AIR.defaultBlockState();
        if (prev.m_60734_() == this) {
            BlockState above = levelAccessor.m_8055_(blockPos.above());
            BlockState below = levelAccessor.m_8055_(blockPos.below());
            boolean down = (Boolean) prev.m_61143_(DOWN);
            int shapeInt = down ? this.getShapeInt(below, above, down) : this.getShapeInt(above, below, down);
            prev = (BlockState) prev.m_61124_(SHAPE, shapeInt);
        }
        return prev;
    }

    public static boolean isGalenaSpireConnectable(BlockState state, boolean down) {
        return state.m_60734_() == ACBlockRegistry.GALENA_SPIRE.get() && (Boolean) state.m_61143_(DOWN) == down;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(SHAPE, DOWN, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }
}