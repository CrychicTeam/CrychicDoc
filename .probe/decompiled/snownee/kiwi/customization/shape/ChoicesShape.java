package snownee.kiwi.customization.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import snownee.kiwi.customization.block.KBlockUtils;

public record ChoicesShape(List<String> keys, Map<String, ShapeGenerator> valueMap) implements ShapeGenerator {

    public static <T extends Comparable<T>> ShapeGenerator chooseOneProperty(Property<T> property, Map<T, ShapeGenerator> valueMap) {
        return new ChoicesShape(List.of(property.getName()), (Map<String, ShapeGenerator>) property.getPossibleValues().stream().collect(Collectors.toUnmodifiableMap(property::m_6940_, valueMap::get)));
    }

    public static ShapeGenerator chooseBooleanProperty(BooleanProperty property, ShapeGenerator trueShape, ShapeGenerator falseShape) {
        return new ChoicesShape(List.of(property.m_61708_()), Map.of("true", trueShape, "false", falseShape));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, CollisionContext context) {
        String key;
        if (this.keys.size() == 1) {
            key = KBlockUtils.getValueString(blockState, (String) this.keys.get(0));
        } else {
            key = String.join(",", (CharSequence[]) this.keys.stream().map(k -> KBlockUtils.getValueString(blockState, k)).toArray(String[]::new));
        }
        return ((ShapeGenerator) this.valueMap.get(key)).getShape(blockState, context);
    }

    public static record Unbaked(List<String> keys, Map<String, UnbakedShape> choices) implements UnbakedShape {

        public static Codec<ChoicesShape.Unbaked> codec(UnbakedShapeCodec parentCodec) {
            return ExtraCodecs.validate(RecordCodecBuilder.create(instance -> instance.group(ExtraCodecs.NON_EMPTY_STRING.listOf().fieldOf("keys").forGetter(ChoicesShape.Unbaked::keys), Codec.unboundedMap(ExtraCodecs.NON_EMPTY_STRING, parentCodec).fieldOf("choices").forGetter(ChoicesShape.Unbaked::choices)).apply(instance, ChoicesShape.Unbaked::new)), $ -> {
                if ($.keys().isEmpty()) {
                    return DataResult.error(() -> "Keys must not be empty");
                } else if ($.choices().isEmpty()) {
                    throw new IllegalArgumentException("Choices must not be empty");
                } else {
                    return DataResult.success($);
                }
            });
        }

        @Override
        public ShapeGenerator bake(BakingContext context) {
            return new ChoicesShape(this.keys, (Map<String, ShapeGenerator>) this.choices.entrySet().stream().collect(Collectors.toUnmodifiableMap(Entry::getKey, e -> ((UnbakedShape) e.getValue()).bake(context))));
        }

        @Override
        public Stream<UnbakedShape> dependencies() {
            return this.choices.values().stream();
        }
    }
}