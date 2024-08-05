package snownee.kiwi.customization.shape;

import java.util.stream.Stream;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface UnbakedShape {

    ShapeGenerator bake(BakingContext var1);

    Stream<UnbakedShape> dependencies();

    public static record Inlined(VoxelShape shape) implements UnbakedShape {

        @Override
        public ShapeGenerator bake(BakingContext context) {
            return ShapeGenerator.unit(this.shape);
        }

        @Override
        public Stream<UnbakedShape> dependencies() {
            return Stream.empty();
        }
    }
}