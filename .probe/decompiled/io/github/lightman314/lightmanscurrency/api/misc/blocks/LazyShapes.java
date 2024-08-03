package io.github.lightman314.lightmanscurrency.api.misc.blocks;

import io.github.lightman314.lightmanscurrency.util.TriFunction;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

public class LazyShapes {

    public static final VoxelShape SHORT_BOX = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    public static final Function<Direction, VoxelShape> SHORT_BOX_SHAPE = lazySingleShape(SHORT_BOX);

    public static final VoxelShape BOX = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    public static final Function<Direction, VoxelShape> BOX_SHAPE = lazySingleShape(BOX);

    public static final VoxelShape TALL_BOX = Block.box(0.0, 0.0, 0.0, 16.0, 32.0, 16.0);

    public static final BiFunction<Direction, Boolean, VoxelShape> TALL_BOX_SHAPE = lazyTallSingleShape(TALL_BOX);

    public static final VoxelShape WIDE_BOX_NORTH = Block.box(0.0, 0.0, 0.0, 32.0, 16.0, 16.0);

    public static final VoxelShape WIDE_BOX_EAST = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 32.0);

    public static final VoxelShape WIDE_BOX_SOUTH = Block.box(-16.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    public static final VoxelShape WIDE_BOX_WEST = Block.box(0.0, 0.0, -16.0, 16.0, 16.0, 16.0);

    public static final BiFunction<Direction, Boolean, VoxelShape> WIDE_BOX_SHAPE = lazyWideDirectionalShape(WIDE_BOX_NORTH, WIDE_BOX_EAST, WIDE_BOX_SOUTH, WIDE_BOX_WEST);

    public static final VoxelShape TALL_WIDE_BOX_NORTH = Block.box(0.0, 0.0, 0.0, 32.0, 32.0, 16.0);

    public static final VoxelShape TALL_WIDE_BOX_EAST = Block.box(0.0, 0.0, 0.0, 16.0, 32.0, 32.0);

    public static final VoxelShape TALL_WIDE_BOX_SOUTH = Block.box(-16.0, 0.0, 0.0, 16.0, 32.0, 16.0);

    public static final VoxelShape TALL_WIDE_BOX_WEST = Block.box(0.0, 0.0, -16.0, 16.0, 32.0, 16.0);

    public static final TriFunction<Direction, Boolean, Boolean, VoxelShape> TALL_WIDE_BOX_SHAPE = lazyTallWideDirectionalShape(TALL_WIDE_BOX_NORTH, TALL_WIDE_BOX_EAST, TALL_WIDE_BOX_SOUTH, TALL_WIDE_BOX_WEST);

    public static Function<Direction, VoxelShape> lazySingleShape(VoxelShape shape) {
        return facing -> shape;
    }

    public static Function<Direction, VoxelShape> lazyDirectionalShape(VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west) {
        return new LazyShapes.LazyDirectionShapeHandler(north, east, south, west);
    }

    public static Function<Direction, VoxelShape> lazyDirectionalShape(VoxelShape northSouth, VoxelShape eastWest) {
        return new LazyShapes.LazyDirectionShapeHandler(northSouth, eastWest, northSouth, eastWest);
    }

    public static BiFunction<Direction, Boolean, VoxelShape> lazyTallSingleShape(VoxelShape shape) {
        return (facing, isBottom) -> isBottom ? shape : moveDown(shape);
    }

    public static BiFunction<Direction, Boolean, VoxelShape> lazyTallDirectionalShape(VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west) {
        return new LazyShapes.LazyDirectionTallShapeHandler(north, east, south, west);
    }

    public static BiFunction<Direction, Boolean, VoxelShape> lazyWideDirectionalShape(VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west) {
        return new LazyShapes.LazyDirectionWideShapeHandler(north, east, south, west);
    }

    public static TriFunction<Direction, Boolean, Boolean, VoxelShape> lazyTallWideDirectionalShape(BiFunction<Direction, Boolean, VoxelShape> tallShape) {
        return new LazyShapes.LazyDirectionTallWideShapeHandler(tallShape);
    }

    public static TriFunction<Direction, Boolean, Boolean, VoxelShape> lazyTallWideDirectionalShape(VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west) {
        return new LazyShapes.LazyDirectionTallWideShapeHandler(north, east, south, west);
    }

    public static VoxelShape moveDown(VoxelShape shape) {
        return shape.move(0.0, -1.0, 0.0);
    }

    protected static class LazyDirectionShapeHandler implements Function<Direction, VoxelShape> {

        private final VoxelShape north;

        private final VoxelShape east;

        private final VoxelShape south;

        private final VoxelShape west;

        public LazyDirectionShapeHandler(VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west) {
            this.north = north;
            this.east = east;
            this.south = south;
            this.west = west;
        }

        public VoxelShape apply(Direction facing) {
            return switch(facing) {
                case EAST ->
                    this.east;
                case SOUTH ->
                    this.south;
                case WEST ->
                    this.west;
                default ->
                    this.north;
            };
        }
    }

    protected static class LazyDirectionTallShapeHandler implements BiFunction<Direction, Boolean, VoxelShape> {

        private final Function<Direction, VoxelShape> lazyShape;

        public LazyDirectionTallShapeHandler(VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west) {
            this.lazyShape = LazyShapes.lazyDirectionalShape(north, east, south, west);
        }

        public VoxelShape apply(Direction facing, Boolean isBottom) {
            VoxelShape shape = (VoxelShape) this.lazyShape.apply(facing);
            return isBottom ? shape : LazyShapes.moveDown(shape);
        }
    }

    protected static class LazyDirectionTallWideShapeHandler implements TriFunction<Direction, Boolean, Boolean, VoxelShape> {

        private final BiFunction<Direction, Boolean, VoxelShape> lazyShape;

        public LazyDirectionTallWideShapeHandler(BiFunction<Direction, Boolean, VoxelShape> tallShape) {
            this.lazyShape = tallShape;
        }

        public LazyDirectionTallWideShapeHandler(VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west) {
            this.lazyShape = LazyShapes.lazyTallDirectionalShape(north, east, south, west);
        }

        public VoxelShape apply(Direction facing, Boolean isBottom, Boolean isLeft) {
            VoxelShape shape = (VoxelShape) this.lazyShape.apply(facing, isBottom);
            if (isLeft) {
                return shape;
            } else {
                Vector3f offset = IRotatableBlock.getLeftVect(facing);
                return shape.move((double) offset.x(), (double) offset.y(), (double) offset.z());
            }
        }
    }

    protected static class LazyDirectionWideShapeHandler implements BiFunction<Direction, Boolean, VoxelShape> {

        private final Function<Direction, VoxelShape> lazyShape;

        public LazyDirectionWideShapeHandler(VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west) {
            this.lazyShape = LazyShapes.lazyDirectionalShape(north, east, south, west);
        }

        public VoxelShape apply(Direction facing, Boolean isLeft) {
            VoxelShape shape = (VoxelShape) this.lazyShape.apply(facing);
            if (isLeft) {
                return shape;
            } else {
                Vector3f offset = IRotatableBlock.getLeftVect(facing);
                return shape.move((double) offset.x(), (double) offset.y(), (double) offset.z());
            }
        }
    }
}