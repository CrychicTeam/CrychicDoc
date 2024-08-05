package me.shedaniel.clothconfig2.gui.entries;

import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class AbstractNumberListEntry<T> extends TextFieldListEntry<T> {

    private static final Function<String, String> stripCharacters = s -> {
        StringBuilder builder = new StringBuilder();
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c) || c == '-' || c == '.') {
                builder.append(c);
            }
        }
        return builder.toString();
    };

    protected T minimum;

    protected T maximum;

    @Deprecated
    @Internal
    protected AbstractNumberListEntry(Component fieldName, T original, Component resetButtonKey, Supplier<T> defaultValue) {
        super(fieldName, original, resetButtonKey, defaultValue);
        this.applyDefaultRange();
    }

    @Deprecated
    @Internal
    protected AbstractNumberListEntry(Component fieldName, T original, Component resetButtonKey, Supplier<T> defaultValue, Supplier<Optional<Component[]>> tooltipSupplier) {
        super(fieldName, original, resetButtonKey, defaultValue, tooltipSupplier);
        this.applyDefaultRange();
    }

    @Deprecated
    @Internal
    protected AbstractNumberListEntry(Component fieldName, T original, Component resetButtonKey, Supplier<T> defaultValue, Supplier<Optional<Component[]>> tooltipSupplier, boolean requiresRestart) {
        super(fieldName, original, resetButtonKey, defaultValue, tooltipSupplier, requiresRestart);
        this.applyDefaultRange();
    }

    protected abstract Entry<T, T> getDefaultRange();

    private void applyDefaultRange() {
        Entry<T, T> range = this.getDefaultRange();
        if (range != null) {
            this.minimum = (T) range.getKey();
            this.maximum = (T) range.getValue();
        }
    }

    @Override
    protected String stripAddText(String s) {
        return (String) stripCharacters.apply(s);
    }
}