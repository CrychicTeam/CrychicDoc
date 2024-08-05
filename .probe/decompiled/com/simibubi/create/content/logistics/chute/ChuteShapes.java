package com.simibubi.create.content.logistics.chute;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChuteShapes {

    static Map<BlockState, VoxelShape> cache = new HashMap();

    static Map<BlockState, VoxelShape> collisionCache = new HashMap();

    public static final VoxelShape INTERSECTION_MASK = Block.box(0.0, -16.0, 0.0, 16.0, 16.0, 16.0);

    public static final VoxelShape COLLISION_MASK = Block.box(0.0, 0.0, 0.0, 16.0, 24.0, 16.0);

    public static final VoxelShape PANEL = Block.box(1.0, -15.0, 0.0, 15.0, 4.0, 1.0);

    public static VoxelShape createShape(BlockState state) {
        if (AllBlocks.SMART_CHUTE.has(state)) {
            return Shapes.block();
        } else {
            Direction direction = (Direction) state.m_61143_(ChuteBlock.FACING);
            ChuteBlock.Shape shape = (ChuteBlock.Shape) state.m_61143_(ChuteBlock.SHAPE);
            boolean intersection = shape == ChuteBlock.Shape.INTERSECTION || shape == ChuteBlock.Shape.ENCASED;
            if (direction == Direction.DOWN) {
                return intersection ? Shapes.block() : AllShapes.CHUTE;
            } else {
                VoxelShape combineWith = intersection ? Shapes.block() : Shapes.empty();
                VoxelShape result = Shapes.or(combineWith, AllShapes.CHUTE_SLOPE.get(direction));
                if (intersection) {
                    result = Shapes.joinUnoptimized(INTERSECTION_MASK, result, BooleanOp.AND);
                }
                return result;
            }
        }
    }

    public static VoxelShape getShape(BlockState state) {
        if (cache.containsKey(state)) {
            return (VoxelShape) cache.get(state);
        } else {
            VoxelShape createdShape = createShape(state);
            cache.put(state, createdShape);
            return createdShape;
        }
    }

    public static VoxelShape getCollisionShape(BlockState state) {
        if (collisionCache.containsKey(state)) {
            return (VoxelShape) collisionCache.get(state);
        } else {
            VoxelShape createdShape = Shapes.joinUnoptimized(COLLISION_MASK, getShape(state), BooleanOp.AND);
            collisionCache.put(state, createdShape);
            return createdShape;
        }
    }

    public static VoxelShape createSlope() {
        VoxelShape shape = Shapes.empty();
        for (int i = 0; i < 16; i++) {
            float offset = (float) i / 16.0F;
            shape = Shapes.join(shape, PANEL.move(0.0, (double) offset, (double) offset), BooleanOp.OR);
        }
        return shape;
    }
}