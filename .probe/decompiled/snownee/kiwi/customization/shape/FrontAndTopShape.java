package snownee.kiwi.customization.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import snownee.kiwi.util.VoxelUtil;

public record FrontAndTopShape(ShapeGenerator floor, ShapeGenerator ceiling, ShapeGenerator wall) implements ShapeGenerator {

    public static ShapeGenerator create(ShapeGenerator floor, ShapeGenerator ceiling, ShapeGenerator wall) {
        return new FrontAndTopShape(FrontAndTopShape.Child.create(floor), FrontAndTopShape.Child.create(ceiling), FrontAndTopShape.Child.create(wall));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, CollisionContext context) {
        FrontAndTop frontAndTop = (FrontAndTop) blockState.m_61143_(BlockStateProperties.ORIENTATION);
        return switch(frontAndTop.front()) {
            case UP ->
                this.ceiling.getShape(blockState, context);
            case DOWN ->
                this.floor.getShape(blockState, context);
            default ->
                this.wall.getShape(blockState, context);
        };
    }

    public static record Child(VoxelShape[] shapes) implements AbstractHorizontalShape {

        public static ShapeGenerator create(ShapeGenerator northGenerator) {
            VoxelShape north = ShapeGenerator.Unit.unboxOrThrow(northGenerator);
            if (VoxelUtil.isIsotropicHorizontally(north)) {
                return northGenerator;
            } else {
                VoxelShape[] shapes = new VoxelShape[4];
                shapes[Direction.NORTH.get2DDataValue()] = north;
                return new FrontAndTopShape.Child(shapes);
            }
        }

        @Override
        public Direction getDirection(BlockState blockState) {
            FrontAndTop frontAndTop = (FrontAndTop) blockState.m_61143_(BlockStateProperties.ORIENTATION);
            return frontAndTop.top().getAxis().isHorizontal() ? frontAndTop.top() : frontAndTop.front();
        }
    }

    public static record Unbaked(UnbakedShape floor, UnbakedShape ceiling, UnbakedShape wall) implements UnbakedShape {

        public static Codec<FrontAndTopShape.Unbaked> codec(UnbakedShapeCodec parentCodec) {
            return RecordCodecBuilder.create(instance -> instance.group(parentCodec.fieldOf("floor").forGetter(FrontAndTopShape.Unbaked::floor), parentCodec.fieldOf("ceiling").forGetter(FrontAndTopShape.Unbaked::ceiling), parentCodec.fieldOf("wall").forGetter(FrontAndTopShape.Unbaked::wall)).apply(instance, FrontAndTopShape.Unbaked::new));
        }

        @Override
        public ShapeGenerator bake(BakingContext context) {
            return FrontAndTopShape.create(this.floor.bake(context), this.ceiling.bake(context), this.wall.bake(context));
        }

        @Override
        public Stream<UnbakedShape> dependencies() {
            return Stream.of(this.floor, this.ceiling, this.wall);
        }
    }
}