package com.github.alexmodguy.alexscaves.server.block;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BoneRibsBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty UNDER = BooleanProperty.create("under");

    public final Map<BlockState, VoxelShape> shapeMap = new HashMap();

    private static final VoxelShape SHAPE_TOP = Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);

    private static final VoxelShape SHAPE_BOTTOM = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    private static final VoxelShape SHAPE_NORTH = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 2.0);

    private static final VoxelShape SHAPE_SOUTH = Block.box(0.0, 0.0, 14.0, 16.0, 16.0, 16.0);

    private static final VoxelShape SHAPE_WEST = Block.box(0.0, 0.0, 0.0, 2.0, 16.0, 16.0);

    private static final VoxelShape SHAPE_EAST = Block.box(14.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    public BoneRibsBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).requiresCorrectToolForDrops().strength(2.0F).sound(SoundType.BONE_BLOCK).noOcclusion());
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(UNDER, false)).m_61124_(f_54117_, Direction.NORTH));
    }

    protected VoxelShape getRibsShape(BlockState state) {
        if (this.shapeMap.containsKey(state)) {
            return (VoxelShape) this.shapeMap.get(state);
        } else {
            VoxelShape merge = state.m_61143_(UNDER) ? SHAPE_BOTTOM : SHAPE_TOP;
            switch((Direction) state.m_61143_(f_54117_)) {
                case NORTH:
                    merge = Shapes.join(merge, SHAPE_NORTH, BooleanOp.OR);
                    break;
                case EAST:
                    merge = Shapes.join(merge, SHAPE_EAST, BooleanOp.OR);
                    break;
                case SOUTH:
                    merge = Shapes.join(merge, SHAPE_SOUTH, BooleanOp.OR);
                    break;
                case WEST:
                    merge = Shapes.join(merge, SHAPE_WEST, BooleanOp.OR);
            }
            this.shapeMap.put(state, merge);
            return merge;
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        return super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.getRibsShape(state);
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos blockPos) {
        return 1.0F;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        Vec3 vec3 = context.m_43720_().subtract(Vec3.atLowerCornerOf(context.getClickedPos()));
        BlockState placedOn = levelaccessor.m_8055_(context.getClickedPos().relative(context.m_43719_().getOpposite()));
        Direction facing = context.m_43719_().getAxis().isHorizontal() ? context.m_43719_() : context.m_8125_().getOpposite();
        boolean under = context.m_43719_() == Direction.DOWN || vec3.y < 0.5;
        if (placedOn.m_60713_(this) && context.m_43719_().getAxis().isVertical()) {
            facing = (Direction) placedOn.m_61143_(f_54117_);
            under = !(Boolean) placedOn.m_61143_(UNDER);
        }
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, levelaccessor.m_6425_(blockpos).getType() == Fluids.WATER)).m_61124_(f_54117_, facing)).m_61124_(UNDER, under);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(f_54117_, rotation.rotate((Direction) state.m_61143_(f_54117_)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(f_54117_)));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(WATERLOGGED, UNDER, f_54117_);
    }

    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }
}