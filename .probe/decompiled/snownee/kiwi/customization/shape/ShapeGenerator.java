package snownee.kiwi.customization.shape;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface ShapeGenerator {

    VoxelShape getShape(BlockState var1, CollisionContext var2);

    static ShapeGenerator unit(VoxelShape shape) {
        return new ShapeGenerator.Unit(shape);
    }

    public static record Unit(VoxelShape shape) implements ShapeGenerator {

        public static VoxelShape unboxOrThrow(ShapeGenerator unit) {
            if (unit instanceof ShapeGenerator.Unit) {
                return ((ShapeGenerator.Unit) unit).shape;
            } else {
                throw new IllegalArgumentException("Not a unit shape");
            }
        }

        @Override
        public VoxelShape getShape(BlockState blockState, CollisionContext context) {
            return this.shape;
        }
    }
}