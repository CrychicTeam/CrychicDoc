package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrystalCellEdgeBlock extends MechEdgeBlockBase {

    public static final VoxelShape NORTH_AABB = Shapes.or(Block.box(2.0, 0.0, 4.0, 14.0, 13.0, 16.0), Block.box(0.0, 0.0, 2.0, 2.0, 15.0, 16.0), Block.box(14.0, 0.0, 2.0, 16.0, 15.0, 16.0), Block.box(2.0, 0.0, 0.0, 14.0, 4.0, 2.0), Block.box(2.0, 0.0, 1.0, 14.0, 8.0, 16.0), Block.box(3.0, 13.0, 6.0, 13.0, 15.0, 16.0));

    public static final VoxelShape NORTHEAST_AABB = Shapes.or(BOTTOM_AABB, Block.box(0.0, 0.0, 1.0, 15.0, 8.0, 16.0), Block.box(0.0, 0.0, 4.0, 12.0, 13.0, 16.0), Block.box(0.0, 0.0, 6.0, 10.0, 15.0, 16.0));

    public static final VoxelShape EAST_AABB = Shapes.or(Block.box(0.0, 0.0, 2.0, 12.0, 13.0, 14.0), Block.box(0.0, 0.0, 0.0, 14.0, 15.0, 2.0), Block.box(0.0, 0.0, 14.0, 14.0, 15.0, 16.0), Block.box(14.0, 0.0, 2.0, 16.0, 4.0, 14.0), Block.box(0.0, 0.0, 2.0, 15.0, 8.0, 14.0), Block.box(0.0, 13.0, 3.0, 10.0, 15.0, 13.0));

    public static final VoxelShape SOUTHEAST_AABB = Shapes.or(BOTTOM_AABB, Block.box(0.0, 0.0, 0.0, 15.0, 8.0, 15.0), Block.box(0.0, 0.0, 0.0, 12.0, 13.0, 12.0), Block.box(0.0, 0.0, 0.0, 10.0, 15.0, 10.0));

    public static final VoxelShape SOUTH_AABB = Shapes.or(Block.box(2.0, 0.0, 0.0, 14.0, 13.0, 12.0), Block.box(0.0, 0.0, 0.0, 2.0, 15.0, 14.0), Block.box(14.0, 0.0, 0.0, 16.0, 15.0, 14.0), Block.box(2.0, 0.0, 14.0, 14.0, 4.0, 16.0), Block.box(2.0, 0.0, 0.0, 14.0, 8.0, 15.0), Block.box(3.0, 13.0, 0.0, 13.0, 15.0, 10.0));

    public static final VoxelShape SOUTHWEST_AABB = Shapes.or(BOTTOM_AABB, Block.box(1.0, 0.0, 0.0, 16.0, 8.0, 15.0), Block.box(4.0, 0.0, 0.0, 16.0, 13.0, 12.0), Block.box(6.0, 0.0, 0.0, 16.0, 15.0, 10.0));

    public static final VoxelShape WEST_AABB = Shapes.or(Block.box(4.0, 0.0, 2.0, 16.0, 13.0, 14.0), Block.box(2.0, 0.0, 0.0, 16.0, 15.0, 2.0), Block.box(2.0, 0.0, 14.0, 16.0, 15.0, 16.0), Block.box(0.0, 0.0, 2.0, 2.0, 4.0, 14.0), Block.box(1.0, 0.0, 2.0, 16.0, 8.0, 14.0), Block.box(6.0, 13.0, 3.0, 16.0, 15.0, 13.0));

    public static final VoxelShape NORTHWEST_AABB = Shapes.or(BOTTOM_AABB, Block.box(1.0, 0.0, 1.0, 16.0, 8.0, 16.0), Block.box(4.0, 0.0, 4.0, 16.0, 13.0, 16.0), Block.box(6.0, 0.0, 6.0, 16.0, 15.0, 16.0));

    public static final VoxelShape[] SHAPES = new VoxelShape[] { NORTH_AABB, NORTHEAST_AABB, EAST_AABB, SOUTHEAST_AABB, SOUTH_AABB, SOUTHWEST_AABB, WEST_AABB, NORTHWEST_AABB };

    public CrystalCellEdgeBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[((MechEdgeBlockBase.MechEdge) state.m_61143_(EDGE)).index];
    }

    @Override
    public Block getCenterBlock() {
        return RegistryManager.CRYSTAL_CELL.get();
    }
}