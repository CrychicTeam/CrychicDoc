package snownee.kiwi.customization.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.VoxelShape;
import snownee.kiwi.util.VoxelUtil;

public record HorizontalShape(VoxelShape[] shapes) implements AbstractHorizontalShape {

    public static ShapeGenerator create(ShapeGenerator northGenerator) {
        VoxelShape north = ShapeGenerator.Unit.unboxOrThrow(northGenerator);
        if (VoxelUtil.isIsotropicHorizontally(north)) {
            return northGenerator;
        } else {
            VoxelShape[] shapes = new VoxelShape[4];
            shapes[Direction.NORTH.get2DDataValue()] = north;
            return new HorizontalShape(shapes);
        }
    }

    @Override
    public Direction getDirection(BlockState blockState) {
        return (Direction) blockState.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
    }

    public static record Unbaked(UnbakedShape wrapped) implements UnbakedShape {

        public static Codec<HorizontalShape.Unbaked> codec(UnbakedShapeCodec parentCodec) {
            return RecordCodecBuilder.create(instance -> instance.group(parentCodec.fieldOf("north").forGetter(HorizontalShape.Unbaked::wrapped)).apply(instance, HorizontalShape.Unbaked::new));
        }

        @Override
        public ShapeGenerator bake(BakingContext context) {
            return HorizontalShape.create(this.wrapped.bake(context));
        }

        @Override
        public Stream<UnbakedShape> dependencies() {
            return Stream.of(this.wrapped);
        }
    }
}