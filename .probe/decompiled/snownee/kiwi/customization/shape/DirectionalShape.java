package snownee.kiwi.customization.shape;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import snownee.kiwi.util.VoxelUtil;
import snownee.kiwi.util.codec.CustomizationCodecs;

public record DirectionalShape(VoxelShape[] shapes, String property) implements ShapeGenerator {

    public static ShapeGenerator create(ShapeGenerator upGenerator, String property) {
        Preconditions.checkArgument(property.equals("facing") || property.equals("orientation"), "Unknown property: " + property);
        VoxelShape up = ShapeGenerator.Unit.unboxOrThrow(upGenerator);
        if (Shapes.block() == up) {
            return upGenerator;
        } else {
            VoxelShape[] shapes = new VoxelShape[6];
            shapes[Direction.DOWN.get3DDataValue()] = VoxelUtil.rotate(up, Direction.UP);
            return new DirectionalShape(shapes, property);
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState, CollisionContext context) {
        String index = this.property;
        Direction direction = switch(index) {
            case "facing" ->
                (Direction) blockState.m_61143_(BlockStateProperties.FACING);
            case "orientation" ->
                JigsawBlock.getFrontFacing(blockState);
            default ->
                throw new IllegalArgumentException("Unknown property: " + this.property);
        };
        int index = direction.get3DDataValue();
        VoxelShape shape = this.shapes[index];
        if (shape == null) {
            synchronized (this.shapes) {
                shape = this.shapes[index];
                if (shape == null) {
                    this.shapes[index] = shape = VoxelUtil.rotate(this.shapes[Direction.DOWN.get3DDataValue()], direction);
                }
            }
        }
        return shape;
    }

    public static record Unbaked(UnbakedShape wrapped, String property) implements UnbakedShape {

        public static Codec<DirectionalShape.Unbaked> codec(UnbakedShapeCodec parentCodec) {
            return RecordCodecBuilder.create(instance -> instance.group(parentCodec.fieldOf("up").forGetter(DirectionalShape.Unbaked::wrapped), CustomizationCodecs.strictOptionalField(ExtraCodecs.NON_EMPTY_STRING, "property", "facing").forGetter(DirectionalShape.Unbaked::property)).apply(instance, DirectionalShape.Unbaked::new));
        }

        @Override
        public ShapeGenerator bake(BakingContext context) {
            return DirectionalShape.create(this.wrapped.bake(context), this.property);
        }

        @Override
        public Stream<UnbakedShape> dependencies() {
            return Stream.of(this.wrapped);
        }
    }
}