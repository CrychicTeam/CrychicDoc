package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.world.Container;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface Basket extends Container {

    VoxelShape[] COLLECTION_AREA_SHAPES = new VoxelShape[] { Block.box(0.0, -16.0, 0.0, 16.0, 16.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 32.0, 16.0), Block.box(0.0, 0.0, -16.0, 16.0, 16.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 32.0), Block.box(-16.0, 0.0, 0.0, 16.0, 16.0, 16.0), Block.box(0.0, 0.0, 0.0, 32.0, 16.0, 16.0) };

    default VoxelShape getFacingCollectionArea(int facingIndex) {
        return COLLECTION_AREA_SHAPES[facingIndex];
    }

    double getLevelX();

    double getLevelY();

    double getLevelZ();
}