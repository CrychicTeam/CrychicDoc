package net.minecraft.world.level.block.state.properties;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.Optional;

public class BooleanProperty extends Property<Boolean> {

    private final ImmutableSet<Boolean> values = ImmutableSet.of(true, false);

    protected BooleanProperty(String string0) {
        super(string0, Boolean.class);
    }

    @Override
    public Collection<Boolean> getPossibleValues() {
        return this.values;
    }

    public static BooleanProperty create(String string0) {
        return new BooleanProperty(string0);
    }

    @Override
    public Optional<Boolean> getValue(String string0) {
        return !"true".equals(string0) && !"false".equals(string0) ? Optional.empty() : Optional.of(Boolean.valueOf(string0));
    }

    public String getName(Boolean boolean0) {
        return boolean0.toString();
    }

    @Override
    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (object0 instanceof BooleanProperty && super.equals(object0)) {
            BooleanProperty $$1 = (BooleanProperty) object0;
            return this.values.equals($$1.values);
        } else {
            return false;
        }
    }

    @Override
    public int generateHashCode() {
        return 31 * super.generateHashCode() + this.values.hashCode();
    }
}