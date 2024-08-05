package net.minecraft.world.level.block.state.properties;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class IntegerProperty extends Property<Integer> {

    private final ImmutableSet<Integer> values;

    private final int min;

    private final int max;

    protected IntegerProperty(String string0, int int1, int int2) {
        super(string0, Integer.class);
        if (int1 < 0) {
            throw new IllegalArgumentException("Min value of " + string0 + " must be 0 or greater");
        } else if (int2 <= int1) {
            throw new IllegalArgumentException("Max value of " + string0 + " must be greater than min (" + int1 + ")");
        } else {
            this.min = int1;
            this.max = int2;
            Set<Integer> $$3 = Sets.newHashSet();
            for (int $$4 = int1; $$4 <= int2; $$4++) {
                $$3.add($$4);
            }
            this.values = ImmutableSet.copyOf($$3);
        }
    }

    @Override
    public Collection<Integer> getPossibleValues() {
        return this.values;
    }

    @Override
    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (object0 instanceof IntegerProperty && super.equals(object0)) {
            IntegerProperty $$1 = (IntegerProperty) object0;
            return this.values.equals($$1.values);
        } else {
            return false;
        }
    }

    @Override
    public int generateHashCode() {
        return 31 * super.generateHashCode() + this.values.hashCode();
    }

    public static IntegerProperty create(String string0, int int1, int int2) {
        return new IntegerProperty(string0, int1, int2);
    }

    @Override
    public Optional<Integer> getValue(String string0) {
        try {
            Integer $$1 = Integer.valueOf(string0);
            return $$1 >= this.min && $$1 <= this.max ? Optional.of($$1) : Optional.empty();
        } catch (NumberFormatException var3) {
            return Optional.empty();
        }
    }

    public String getName(Integer integer0) {
        return integer0.toString();
    }
}