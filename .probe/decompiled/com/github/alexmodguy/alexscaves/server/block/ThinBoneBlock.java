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
import net.minecraft.world.level.block.RotatedPillarBlock;
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
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ThinBoneBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final IntegerProperty OFFSET = IntegerProperty.create("offset", 0, 2);

    public final Map<BlockState, VoxelShape> shapeMap = new HashMap();

    public static final VoxelShape SHAPE_X = Block.box(0.0, 6.0, 6.0, 16.0, 10.0, 10.0);

    public static final VoxelShape SHAPE_Y = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    public static final VoxelShape SHAPE_Z = Block.box(6.0, 6.0, 0.0, 10.0, 10.0, 16.0);

    public ThinBoneBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).requiresCorrectToolForDrops().strength(2.0F).sound(SoundType.BONE_BLOCK).noOcclusion());
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(OFFSET, 1)).m_61124_(f_55923_, Direction.Axis.Y));
    }

    protected VoxelShape getThinBoneShape(BlockState state) {
        if (this.shapeMap.containsKey(state)) {
            return (VoxelShape) this.shapeMap.get(state);
        } else {
            VoxelShape merge = switch((Direction.Axis) state.m_61143_(f_55923_)) {
                case X ->
                    SHAPE_X;
                case Y ->
                    SHAPE_Y;
                case Z ->
                    SHAPE_Z;
                default ->
                    SHAPE_Z;
            };
            if (state.m_61143_(f_55923_) != Direction.Axis.Y && (Integer) state.m_61143_(OFFSET) != 1) {
                int offset = state.m_61143_(OFFSET) == 0 ? -6 : 6;
                merge = merge.move(0.0, (double) ((float) offset / 16.0F), 0.0);
            } else {
                int offset = 0;
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
        return this.getThinBoneShape(state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        int offset = 0;
        if (context.m_43719_().getAxis().isHorizontal()) {
            Vec3 vec3 = context.m_43720_().subtract(Vec3.atLowerCornerOf(context.getClickedPos()));
            if (vec3.y < 0.33F) {
                offset = 0;
            } else if (vec3.y < 0.66F) {
                offset = 1;
            } else {
                offset = 2;
            }
        }
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, levelaccessor.m_6425_(blockpos).getType() == Fluids.WATER)).m_61124_(f_55923_, context.m_43719_().getAxis())).m_61124_(OFFSET, offset);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(WATERLOGGED, f_55923_, OFFSET);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }
}