package com.simibubi.create.content.trains.track;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TrackVoxelShapes {

    public static VoxelShape orthogonal() {
        return Block.box(-14.0, 0.0, 0.0, 30.0, 4.0, 16.0);
    }

    public static VoxelShape longOrthogonalX() {
        return Block.box(-3.3, 0.0, -14.0, 19.3, 4.0, 30.0);
    }

    public static VoxelShape longOrthogonalZ() {
        return Block.box(-14.0, 0.0, -3.3, 30.0, 4.0, 19.3);
    }

    public static VoxelShape longOrthogonalZOffset() {
        return Block.box(-14.0, 0.0, 0.0, 30.0, 4.0, 24.0);
    }

    public static VoxelShape ascending() {
        VoxelShape shape = Block.box(-14.0, 0.0, 0.0, 30.0, 4.0, 4.0);
        VoxelShape[] shapes = new VoxelShape[6];
        for (int i = 0; i < 6; i++) {
            int off = (i + 1) * 2;
            shapes[i] = Block.box(-14.0, (double) off, (double) off, 30.0, (double) (4 + off), (double) (4 + off));
        }
        return Shapes.or(shape, shapes);
    }

    public static VoxelShape diagonal() {
        VoxelShape shape = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
        VoxelShape[] shapes = new VoxelShape[12];
        int off = 0;
        for (int i = 0; i < 6; i++) {
            off = (i + 1) * 2;
            shapes[i * 2] = Block.box((double) off, 0.0, (double) off, (double) (16 + off), 4.0, (double) (16 + off));
            shapes[i * 2 + 1] = Block.box((double) (-off), 0.0, (double) (-off), (double) (16 - off), 4.0, (double) (16 - off));
        }
        shape = Shapes.or(shape, shapes);
        int var10 = 20;
        shape = Shapes.join(shape, Block.box((double) var10, 0.0, (double) var10, (double) (16 + var10), 4.0, (double) (16 + var10)), BooleanOp.ONLY_FIRST);
        shape = Shapes.join(shape, Block.box((double) (-var10), 0.0, (double) (-var10), (double) (16 - var10), 4.0, (double) (16 - var10)), BooleanOp.ONLY_FIRST);
        var10 = 8;
        shape = Shapes.or(shape, Block.box((double) var10, 0.0, (double) var10, (double) (16 + var10), 4.0, (double) (16 + var10)));
        shape = Shapes.or(shape, Block.box((double) (-var10), 0.0, (double) (-var10), (double) (16 - var10), 4.0, (double) (16 - var10)));
        return shape.optimize();
    }
}