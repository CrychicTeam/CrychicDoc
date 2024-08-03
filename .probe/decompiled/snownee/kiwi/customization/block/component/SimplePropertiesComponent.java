package snownee.kiwi.customization.block.component;

import com.google.common.base.Preconditions;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.DataResult.PartialResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import snownee.kiwi.customization.block.KBlockUtils;
import snownee.kiwi.customization.block.StringProperty;
import snownee.kiwi.customization.block.loader.KBlockComponents;
import snownee.kiwi.util.codec.CustomizationCodecs;
import snownee.kiwi.util.codec.JavaOps;

public record SimplePropertiesComponent(boolean useShapeForLightOcclusion, List<Pair<Property<?>, String>> properties) implements KBlockComponent {

    private static final Interner<SimplePropertiesComponent> INTERNER = Interners.newStrongInterner();

    public static final Codec<Pair<Property<?>, String>> SINGLE_CODEC = new Codec<Pair<Property<?>, String>>() {

        private static final Map<String, Direction> DIRECTION_STRINGS = (Map<String, Direction>) Direction.stream().collect(Collectors.toUnmodifiableMap(StringRepresentable::m_7912_, Function.identity()));

        public <T> DataResult<Pair<Pair<Property<?>, String>, T>> decode(DynamicOps<T> ops, T input) {
            DataResult<MapLike<T>> mapValue = ops.getMap(input);
            if (mapValue.error().isPresent()) {
                return DataResult.error(((PartialResult) mapValue.error().get())::message);
            } else {
                MapLike<T> map = (MapLike<T>) mapValue.result().orElseThrow();
                DataResult<String> commonValue = ops.getStringValue(map.get("common"));
                Object defaultValue = ops.convertTo(JavaOps.INSTANCE, map.get("default"));
                List<String> values = (List<String>) Codec.STRING.listOf().parse(ops, map.get("values")).result().orElse(null);
                if (values != null && values.size() < 2) {
                    return DataResult.error(() -> "Invalid values for property: " + values);
                } else {
                    if (defaultValue == null) {
                        if (values == null) {
                            return DataResult.error(() -> "Missing default value for property");
                        }
                        defaultValue = values.get(0);
                    }
                    Property<?> property;
                    if (commonValue.result().isPresent()) {
                        String s = (String) commonValue.result().get();
                        property = (Property<?>) KBlockUtils.COMMON_PROPERTIES.get(s);
                        if (property == null) {
                            return DataResult.error(() -> "Unknown common property: " + s);
                        }
                    } else {
                        String name = Util.getOrThrow(ops.getStringValue(map.get("name")), $ -> new IllegalStateException("Missing name for property"));
                        if (defaultValue instanceof Integer) {
                            int min = Util.getOrThrow(ops.getNumberValue(map.get("min")), $ -> new IllegalStateException("Missing min for integer property")).intValue();
                            int max = Util.getOrThrow(ops.getNumberValue(map.get("max")), $ -> new IllegalStateException("Missing max for integer property")).intValue();
                            property = IntegerProperty.create(name, min, max);
                        } else if (defaultValue instanceof Boolean) {
                            property = BooleanProperty.create(name);
                        } else {
                            if (values == null || !(defaultValue instanceof String s)) {
                                String msg = "Unsupported default value type: " + defaultValue.getClass();
                                return DataResult.error(() -> msg);
                            }
                            if (!DIRECTION_STRINGS.containsKey(s) || !DIRECTION_STRINGS.keySet().containsAll(values)) {
                                property = new StringProperty(name, values);
                            } else if (values.size() == DIRECTION_STRINGS.size()) {
                                property = DirectionProperty.create(name);
                            } else {
                                property = DirectionProperty.create(name, (Direction[]) values.stream().map(DIRECTION_STRINGS::get).toArray(Direction[]::new));
                            }
                        }
                        property = KBlockUtils.internProperty(property);
                    }
                    String defaultString;
                    try {
                        defaultString = KBlockUtils.getNameByValue(property, defaultValue);
                        Preconditions.checkArgument(property.getValue(defaultString).isPresent());
                    } catch (Exception var13) {
                        return DataResult.error(() -> "Invalid default value for property: " + var13.getMessage());
                    }
                    return DataResult.success(Pair.of(Pair.of(property, defaultString), ops.empty()));
                }
            }
        }

        public <T> DataResult<T> encode(Pair<Property<?>, String> input, DynamicOps<T> ops, T prefix) {
            RecordBuilder<T> mapBuilder = ops.mapBuilder();
            Property<?> property = (Property<?>) input.getFirst();
            String s = (String) KBlockUtils.COMMON_PROPERTIES.inverse().get(property);
            List<String> values = List.of();
            if (s == null) {
                mapBuilder.add("name", ops.createString(property.getName()));
                if (property instanceof IntegerProperty integerProperty) {
                    mapBuilder.add("min", ops.createInt(integerProperty.min));
                    mapBuilder.add("max", ops.createInt(integerProperty.max));
                } else if (!(property instanceof EnumProperty) && !(property instanceof StringProperty)) {
                    if (!(property instanceof BooleanProperty)) {
                        return DataResult.error(() -> "Unsupported property type: " + property);
                    }
                } else {
                    values = (List<String>) property.getPossibleValues().stream().map($ -> KBlockUtils.getNameByValue(property, $)).collect(Collectors.toCollection(ArrayList::new));
                }
            } else {
                mapBuilder.add("common", ops.createString(s));
            }
            T defaultValue;
            if (property instanceof IntegerProperty integerProperty) {
                defaultValue = (T) integerProperty.getValue((String) input.getSecond()).map(ops::createInt).orElse(null);
            } else if (property instanceof BooleanProperty booleanProperty) {
                defaultValue = (T) booleanProperty.getValue((String) input.getSecond()).map(ops::createBoolean).orElse(null);
            } else {
                defaultValue = (T) (property.getValue((String) input.getSecond()).isPresent() ? ops.createString((String) input.getSecond()) : null);
            }
            if (defaultValue == null) {
                return DataResult.error(() -> "Invalid value %s for property %s".formatted(input.getSecond(), property.getName()));
            } else {
                if (!values.isEmpty()) {
                    values.remove(input.getSecond());
                    values.add(0, (String) input.getSecond());
                    mapBuilder.add("values", ops.createList(values.stream().map(ops::createString)));
                } else {
                    mapBuilder.add("default", defaultValue);
                }
                return mapBuilder.build(prefix);
            }
        }
    };

    public static final Codec<SimplePropertiesComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.optionalFieldOf("shape_for_light_occlusion", false).forGetter(SimplePropertiesComponent::useShapeForLightOcclusion), ExtraCodecs.nonEmptyList(CustomizationCodecs.compactList(SINGLE_CODEC)).fieldOf("properties").forGetter(SimplePropertiesComponent::properties)).apply(instance, ($1, $2) -> (SimplePropertiesComponent) INTERNER.intern(new SimplePropertiesComponent($1, $2))));

    @Override
    public KBlockComponent.Type<?> type() {
        return KBlockComponents.SIMPLE_PROPERTIES.getOrCreate();
    }

    @Override
    public void injectProperties(Block block, StateDefinition.Builder<Block, BlockState> builder) {
        for (Pair<Property<?>, String> pair : this.properties) {
            builder.add((Property) pair.getFirst());
        }
    }

    @Override
    public BlockState registerDefaultState(BlockState state) {
        for (Pair<Property<?>, String> pair : this.properties) {
            state = KBlockUtils.setValueByString(state, ((Property) pair.getFirst()).getName(), (String) pair.getSecond());
        }
        return state;
    }
}