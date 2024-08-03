package snownee.kiwi.customization.block;

import com.google.common.collect.ImmutableSortedSet;
import java.util.Collection;
import java.util.Optional;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import snownee.kiwi.util.NotNullByDefault;

@NotNullByDefault
public class StringProperty extends Property<String> {

    private final ImmutableSortedSet<String> values;

    public StringProperty(String pName, Collection<String> values) {
        super(pName, String.class);
        this.values = ImmutableSortedSet.copyOf(values.stream().map(String::intern).toList());
    }

    public static StringProperty convert(EnumProperty<?> property) {
        return KBlockUtils.internProperty(new StringProperty(property.m_61708_(), property.getPossibleValues().stream().map(v -> KBlockUtils.getNameByValue(property, v)).toList()));
    }

    @Override
    public Collection<String> getPossibleValues() {
        return this.values;
    }

    public String getName(String value) {
        return value;
    }

    @Override
    public Optional<String> getValue(String key) {
        return this.values.contains(key) ? Optional.of(key) : Optional.empty();
    }

    @Override
    public boolean equals(Object pOther) {
        if (this == pOther) {
            return true;
        } else {
            return !(pOther instanceof StringProperty stringProperty) ? false : this.m_61708_().equals(stringProperty.m_61708_()) && this.values.equals(stringProperty.values);
        }
    }

    @Override
    public int generateHashCode() {
        return 31 * this.m_61708_().hashCode() + this.values.hashCode();
    }
}