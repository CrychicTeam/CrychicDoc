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

public class CaminiteGaugeEdgeBlock extends MechEdgeBlockBase {

    public static final VoxelShape X_AABB = Shapes.or(Block.box(0.0, 0.0, 4.0, 4.0, 16.0, 12.0), Block.box(12.0, 0.0, 4.0, 16.0, 16.0, 12.0), Block.box(4.0, 0.0, 6.0, 12.0, 16.0, 10.0));

    public static final VoxelShape Z_AABB = Shapes.or(Block.box(4.0, 0.0, 0.0, 12.0, 16.0, 4.0), Block.box(4.0, 0.0, 12.0, 12.0, 16.0, 16.0), Block.box(6.0, 0.0, 4.0, 10.0, 16.0, 12.0));

    public static final VoxelShape NORTHEAST_AABB = Shapes.or(Block.box(0.0, 0.0, 4.0, 12.0, 16.0, 12.0), Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 16.0));

    public static final VoxelShape SOUTHEAST_AABB = Shapes.or(Block.box(0.0, 0.0, 4.0, 12.0, 16.0, 12.0), Block.box(4.0, 0.0, 0.0, 12.0, 16.0, 12.0));

    public static final VoxelShape SOUTHWEST_AABB = Shapes.or(Block.box(4.0, 0.0, 4.0, 16.0, 16.0, 12.0), Block.box(4.0, 0.0, 0.0, 12.0, 16.0, 12.0));

    public static final VoxelShape NORTHWEST_AABB = Shapes.or(Block.box(4.0, 0.0, 4.0, 16.0, 16.0, 12.0), Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 16.0));

    public static final VoxelShape[] SHAPES = new VoxelShape[] { X_AABB, NORTHEAST_AABB, Z_AABB, SOUTHEAST_AABB, X_AABB, SOUTHWEST_AABB, Z_AABB, NORTHWEST_AABB };

    public CaminiteGaugeEdgeBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[((MechEdgeBlockBase.MechEdge) state.m_61143_(EDGE)).index];
    }

    @Override
    public Block getCenterBlock() {
        return RegistryManager.CAMINITE_GAUGE.get();
    }
}