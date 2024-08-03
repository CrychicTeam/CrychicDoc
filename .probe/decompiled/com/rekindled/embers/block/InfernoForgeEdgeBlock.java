package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class InfernoForgeEdgeBlock extends MechEdgeBlockBase {

    public static final VoxelShape NORTH_AABB = Shapes.or(Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 16.0), Block.box(2.5, 3.0, 2.0, 13.5, 10.0, 6.0));

    public static final VoxelShape NORTHEAST_AABB = Shapes.or(Block.box(0.0, 7.0, 6.0, 10.0, 12.0, 16.0), Block.box(0.0, 0.0, 4.0, 12.0, 7.0, 16.0));

    public static final VoxelShape EAST_AABB = Shapes.or(Block.box(0.0, 0.0, 0.0, 10.0, 16.0, 16.0), Block.box(10.0, 3.0, 2.5, 14.0, 10.0, 13.5));

    public static final VoxelShape SOUTHEAST_AABB = Shapes.or(Block.box(0.0, 7.0, 0.0, 10.0, 12.0, 10.0), Block.box(0.0, 0.0, 0.0, 12.0, 7.0, 12.0));

    public static final VoxelShape SOUTH_AABB = Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 10.0), Block.box(2.5, 3.0, 10.0, 13.5, 10.0, 14.0));

    public static final VoxelShape SOUTHWEST_AABB = Shapes.or(Block.box(6.0, 7.0, 0.0, 16.0, 12.0, 10.0), Block.box(4.0, 0.0, 0.0, 16.0, 7.0, 12.0));

    public static final VoxelShape WEST_AABB = Shapes.or(Block.box(6.0, 0.0, 0.0, 16.0, 16.0, 16.0), Block.box(2.0, 3.0, 2.5, 6.0, 10.0, 13.5));

    public static final VoxelShape NORTHWEST_AABB = Shapes.or(Block.box(6.0, 7.0, 6.0, 16.0, 12.0, 16.0), Block.box(4.0, 0.0, 4.0, 16.0, 7.0, 16.0));

    public static final VoxelShape[] SHAPES_TOP = new VoxelShape[] { NORTH_AABB, NORTHEAST_AABB, EAST_AABB, SOUTHEAST_AABB, SOUTH_AABB, SOUTHWEST_AABB, WEST_AABB, NORTHWEST_AABB };

    public static final VoxelShape CORNER_CENTER_AABB = Shapes.or(Block.box(4.0, 6.0, 4.0, 12.0, 10.0, 12.0), Block.box(2.0, 4.0, 2.0, 14.0, 6.0, 14.0), Block.box(2.0, 10.0, 2.0, 14.0, 12.0, 14.0));

    public static final VoxelShape NORTHEAST_BOTTOM_AABB = Shapes.or(Shapes.join(Block.box(0.0, 0.0, 1.0, 15.0, 16.0, 16.0), Block.box(4.0, 4.0, 1.0, 15.0, 12.0, 12.0), BooleanOp.ONLY_FIRST), CORNER_CENTER_AABB);

    public static final VoxelShape SOUTHEAST_BOTTOM_AABB = Shapes.or(Shapes.join(Block.box(0.0, 0.0, 0.0, 15.0, 16.0, 15.0), Block.box(4.0, 4.0, 4.0, 15.0, 12.0, 15.0), BooleanOp.ONLY_FIRST), CORNER_CENTER_AABB);

    public static final VoxelShape SOUTHWEST_BOTTOM_AABB = Shapes.or(Shapes.join(Block.box(1.0, 0.0, 0.0, 16.0, 16.0, 15.0), Block.box(1.0, 4.0, 4.0, 12.0, 12.0, 15.0), BooleanOp.ONLY_FIRST), CORNER_CENTER_AABB);

    public static final VoxelShape NORTHWEST_BOTTOM_AABB = Shapes.or(Shapes.join(Block.box(1.0, 0.0, 1.0, 16.0, 16.0, 16.0), Block.box(1.0, 4.0, 1.0, 12.0, 12.0, 12.0), BooleanOp.ONLY_FIRST), CORNER_CENTER_AABB);

    public static final VoxelShape[] SHAPES_BOTTOM = new VoxelShape[] { Shapes.block(), NORTHEAST_BOTTOM_AABB, Shapes.block(), SOUTHEAST_BOTTOM_AABB, Shapes.block(), SOUTHWEST_BOTTOM_AABB, Shapes.block(), NORTHWEST_BOTTOM_AABB };

    public InfernoForgeEdgeBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? SHAPES_BOTTOM[((MechEdgeBlockBase.MechEdge) state.m_61143_(EDGE)).index] : SHAPES_TOP[((MechEdgeBlockBase.MechEdge) state.m_61143_(EDGE)).index];
    }

    @Override
    public Block getCenterBlock() {
        return RegistryManager.INFERNO_FORGE.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(BlockStateProperties.DOUBLE_BLOCK_HALF);
    }
}