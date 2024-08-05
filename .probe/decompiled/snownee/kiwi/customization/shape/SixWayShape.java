package snownee.kiwi.customization.shape;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import snownee.kiwi.util.VoxelUtil;

public record SixWayShape(VoxelShape[] shapes, VoxelShape base, VoxelShape trueDown, VoxelShape falseDown) implements ShapeGenerator {

    private static final List<BooleanProperty> DIRECTION_PROPERTIES = List.of(BlockStateProperties.DOWN, BlockStateProperties.UP, BlockStateProperties.NORTH, BlockStateProperties.SOUTH, BlockStateProperties.WEST, BlockStateProperties.EAST);

    public static ShapeGenerator create(ShapeGenerator base_, ShapeGenerator trueDown_, ShapeGenerator falseDown_) {
        VoxelShape base = ShapeGenerator.Unit.unboxOrThrow(base_);
        VoxelShape trueDown = ShapeGenerator.Unit.unboxOrThrow(trueDown_);
        VoxelShape falseDown = ShapeGenerator.Unit.unboxOrThrow(falseDown_);
        Preconditions.checkArgument(!trueDown.isEmpty() || !falseDown.isEmpty());
        VoxelShape[] shapes = new VoxelShape[64];
        return new SixWayShape(shapes, base, trueDown, falseDown);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, CollisionContext context) {
        int index = 0;
        for (int i = 0; i < 6; i++) {
            if ((Boolean) blockState.m_61143_((Property) DIRECTION_PROPERTIES.get(i))) {
                index |= 1 << i;
            }
        }
        VoxelShape shape = this.shapes[index];
        if (shape == null) {
            synchronized (this.shapes) {
                shape = this.shapes[index];
                if (shape == null) {
                    shape = this.base;
                    for (int ix = 0; ix < 6; ix++) {
                        VoxelShape sideShape = VoxelUtil.rotate((index & 1 << ix) != 0 ? this.trueDown : this.falseDown, Direction.from3DDataValue(ix));
                        shape = Shapes.joinUnoptimized(shape, sideShape, BooleanOp.OR);
                    }
                    this.shapes[index] = shape = shape.optimize();
                }
            }
        }
        return shape;
    }

    public static record Unbaked(UnbakedShape base, UnbakedShape trueDown, UnbakedShape falseDown) implements UnbakedShape {

        public static Codec<SixWayShape.Unbaked> codec(UnbakedShapeCodec parentCodec) {
            return RecordCodecBuilder.create(instance -> instance.group(parentCodec.fieldOf("base").forGetter(SixWayShape.Unbaked::base), parentCodec.fieldOf("true_down").forGetter(SixWayShape.Unbaked::trueDown), parentCodec.fieldOf("false_down").forGetter(SixWayShape.Unbaked::falseDown)).apply(instance, SixWayShape.Unbaked::new));
        }

        @Override
        public ShapeGenerator bake(BakingContext context) {
            return SixWayShape.create(this.base.bake(context), this.trueDown.bake(context), this.falseDown.bake(context));
        }

        @Override
        public Stream<UnbakedShape> dependencies() {
            return Stream.of(this.base, this.trueDown, this.falseDown);
        }
    }
}