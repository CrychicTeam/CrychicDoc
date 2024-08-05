package net.minecraft.world.level.block.state.properties;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.util.StringRepresentable;

public class EnumProperty<T extends Enum<T> & StringRepresentable> extends Property<T> {

    private final ImmutableSet<T> values;

    private final Map<String, T> names = Maps.newHashMap();

    protected EnumProperty(String string0, Class<T> classT1, Collection<T> collectionT2) {
        super(string0, classT1);
        this.values = ImmutableSet.copyOf(collectionT2);
        for (T $$3 : collectionT2) {
            String $$4 = $$3.getSerializedName();
            if (this.names.containsKey($$4)) {
                throw new IllegalArgumentException("Multiple values have the same name '" + $$4 + "'");
            }
            this.names.put($$4, $$3);
        }
    }

    @Override
    public Collection<T> getPossibleValues() {
        return this.values;
    }

    @Override
    public Optional<T> getValue(String string0) {
        return Optional.ofNullable((Enum) this.names.get(string0));
    }

    public String getName(T t0) {
        return t0.getSerializedName();
    }

    @Override
    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (object0 instanceof EnumProperty && super.equals(object0)) {
            EnumProperty<?> $$1 = (EnumProperty<?>) object0;
            return this.values.equals($$1.values) && this.names.equals($$1.names);
        } else {
            return false;
        }
    }

    @Override
    public int generateHashCode() {
        int $$0 = super.generateHashCode();
        $$0 = 31 * $$0 + this.values.hashCode();
        return 31 * $$0 + this.names.hashCode();
    }

    public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String string0, Class<T> classT1) {
        return create(string0, classT1, (Predicate<T>) (p_187560_ -> true));
    }

    public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String string0, Class<T> classT1, Predicate<T> predicateT2) {
        return create(string0, classT1, (Collection<T>) Arrays.stream((Enum[]) classT1.getEnumConstants()).filter(predicateT2).collect(Collectors.toList()));
    }

    public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String string0, Class<T> classT1, T... t2) {
        return create(string0, classT1, Lists.newArrayList(t2));
    }

    public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String string0, Class<T> classT1, Collection<T> collectionT2) {
        return new EnumProperty<>(string0, classT1, collectionT2);
    }
}