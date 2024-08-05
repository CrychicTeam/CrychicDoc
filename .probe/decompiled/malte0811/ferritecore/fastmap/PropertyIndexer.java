package malte0811.ferritecore.fastmap;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public abstract class PropertyIndexer<T extends Comparable<T>> {

    private static final Map<Property<?>, PropertyIndexer<?>> KNOWN_INDEXERS = new Object2ObjectOpenCustomHashMap(Util.identityStrategy());

    private final Property<T> property;

    private final int numValues;

    protected final T[] valuesInOrder;

    public static <T extends Comparable<T>> PropertyIndexer<T> makeIndexer(Property<T> prop) {
        synchronized (KNOWN_INDEXERS) {
            return (PropertyIndexer<T>) KNOWN_INDEXERS.computeIfAbsent(prop, propInner -> {
                PropertyIndexer<?> result = null;
                if (propInner instanceof BooleanProperty boolProp) {
                    result = new PropertyIndexer.BoolIndexer(boolProp);
                } else if (propInner instanceof IntegerProperty intProp) {
                    result = new PropertyIndexer.IntIndexer(intProp);
                } else if (PropertyIndexer.WeirdVanillaDirectionIndexer.isApplicable(propInner)) {
                    result = new PropertyIndexer.WeirdVanillaDirectionIndexer(propInner);
                } else if (propInner instanceof EnumProperty<?> enumProp) {
                    result = new PropertyIndexer.EnumIndexer<>(enumProp);
                }
                return (PropertyIndexer) (result != null && result.isValid() ? result : new PropertyIndexer.GenericIndexer(propInner));
            });
        }
    }

    protected PropertyIndexer(Property<T> property, T[] valuesInOrder) {
        this.property = property;
        this.numValues = property.getPossibleValues().size();
        this.valuesInOrder = valuesInOrder;
    }

    public Property<T> getProperty() {
        return this.property;
    }

    public int numValues() {
        return this.numValues;
    }

    @Nullable
    public final T byIndex(int index) {
        return index >= 0 && index < this.valuesInOrder.length ? this.valuesInOrder[index] : null;
    }

    public abstract int toIndex(T var1);

    protected boolean isValid() {
        Collection<T> allowed = this.getProperty().getPossibleValues();
        int index = 0;
        for (T val : allowed) {
            if (this.toIndex(val) != index || !val.equals(this.byIndex(index))) {
                return false;
            }
            index++;
        }
        return true;
    }

    private static class BoolIndexer extends PropertyIndexer<Boolean> {

        private static final Boolean[] VALUES = new Boolean[] { true, false };

        protected BoolIndexer(BooleanProperty property) {
            super(property, VALUES);
        }

        public int toIndex(Boolean value) {
            return value ? 0 : 1;
        }
    }

    private static class EnumIndexer<E extends Enum<E> & StringRepresentable> extends PropertyIndexer<E> {

        private final int ordinalOffset;

        protected EnumIndexer(EnumProperty<E> property) {
            super(property, (E[]) property.getPossibleValues().toArray(new Enum[0]));
            this.ordinalOffset = property.getPossibleValues().stream().mapToInt(rec$ -> ((Enum) rec$).ordinal()).min().orElse(0);
        }

        public int toIndex(E value) {
            return value.ordinal() - this.ordinalOffset;
        }
    }

    private static class GenericIndexer<T extends Comparable<T>> extends PropertyIndexer<T> {

        private final Map<Comparable<?>, Integer> toValueIndex;

        protected GenericIndexer(Property<T> property) {
            super(property, (T[]) property.getPossibleValues().toArray(new Comparable[0]));
            Builder<Comparable<?>, Integer> toValueIndex = ImmutableMap.builder();
            for (int i = 0; i < this.valuesInOrder.length; i++) {
                toValueIndex.put(this.valuesInOrder[i], i);
            }
            this.toValueIndex = toValueIndex.build();
        }

        @Override
        public int toIndex(T value) {
            return (Integer) this.toValueIndex.getOrDefault(value, -1);
        }
    }

    private static class IntIndexer extends PropertyIndexer<Integer> {

        private final int min;

        protected IntIndexer(IntegerProperty property) {
            super(property, (Integer[]) property.getPossibleValues().toArray(new Integer[0]));
            this.min = (Integer) property.getPossibleValues().stream().min(Comparator.naturalOrder()).orElse(0);
        }

        public int toIndex(Integer value) {
            return value - this.min;
        }
    }

    private static class WeirdVanillaDirectionIndexer extends PropertyIndexer<Direction> {

        private static final Direction[] ORDER = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN };

        public WeirdVanillaDirectionIndexer(Property<Direction> prop) {
            super(prop, ORDER);
            Preconditions.checkState(this.isValid());
        }

        static boolean isApplicable(Property<?> prop) {
            Collection<?> values = prop.getPossibleValues();
            return values.size() != ORDER.length ? false : Arrays.equals(ORDER, values.toArray());
        }

        public int toIndex(Direction value) {
            return switch(value) {
                case NORTH ->
                    0;
                case EAST ->
                    1;
                case SOUTH ->
                    2;
                case WEST ->
                    3;
                case UP ->
                    4;
                case DOWN ->
                    5;
            };
        }
    }
}