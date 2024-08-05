package me.shedaniel.clothconfig2.gui.entries;

import java.util.Optional;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public class IntegerListEntry extends AbstractNumberListEntry<Integer> {

    @Deprecated
    @Internal
    public IntegerListEntry(Component fieldName, Integer value, Component resetButtonKey, Supplier<Integer> defaultValue, Consumer<Integer> saveConsumer) {
        super(fieldName, value, resetButtonKey, defaultValue);
        this.saveCallback = saveConsumer;
    }

    @Deprecated
    @Internal
    public IntegerListEntry(Component fieldName, Integer value, Component resetButtonKey, Supplier<Integer> defaultValue, Consumer<Integer> saveConsumer, Supplier<Optional<Component[]>> tooltipSupplier) {
        this(fieldName, value, resetButtonKey, defaultValue, saveConsumer, tooltipSupplier, false);
    }

    @Deprecated
    @Internal
    public IntegerListEntry(Component fieldName, Integer value, Component resetButtonKey, Supplier<Integer> defaultValue, Consumer<Integer> saveConsumer, Supplier<Optional<Component[]>> tooltipSupplier, boolean requiresRestart) {
        super(fieldName, value, resetButtonKey, defaultValue, tooltipSupplier, requiresRestart);
        this.saveCallback = saveConsumer;
    }

    @Override
    protected Entry<Integer, Integer> getDefaultRange() {
        return new SimpleEntry(-2147483647, Integer.MAX_VALUE);
    }

    public IntegerListEntry setMaximum(int maximum) {
        this.maximum = maximum;
        return this;
    }

    public IntegerListEntry setMinimum(int minimum) {
        this.minimum = minimum;
        return this;
    }

    public Integer getValue() {
        try {
            return Integer.valueOf(this.textFieldWidget.getValue());
        } catch (Exception var2) {
            return 0;
        }
    }

    @Override
    public Optional<Component> getError() {
        try {
            int i = Integer.parseInt(this.textFieldWidget.getValue());
            if (i > this.maximum) {
                return Optional.of(Component.translatable("text.cloth-config.error.too_large", this.maximum));
            }
            if (i < this.minimum) {
                return Optional.of(Component.translatable("text.cloth-config.error.too_small", this.minimum));
            }
        } catch (NumberFormatException var2) {
            return Optional.of(Component.translatable("text.cloth-config.error.not_valid_number_int"));
        }
        return super.getError();
    }
}