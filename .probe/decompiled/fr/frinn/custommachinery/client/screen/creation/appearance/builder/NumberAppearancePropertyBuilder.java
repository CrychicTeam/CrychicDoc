package fr.frinn.custommachinery.client.screen.creation.appearance.builder;

import fr.frinn.custommachinery.api.machine.MachineAppearanceProperty;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.appearance.IAppearancePropertyBuilder;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class NumberAppearancePropertyBuilder<T extends Number> implements IAppearancePropertyBuilder<T> {

    private final Component title;

    private final MachineAppearanceProperty<T> type;

    private final T min;

    private final T max;

    public NumberAppearancePropertyBuilder(Component title, MachineAppearanceProperty<T> type, T min, T max) {
        this.title = title;
        this.type = type;
        this.min = min;
        this.max = max;
    }

    @Override
    public Component title() {
        return this.title;
    }

    @Override
    public MachineAppearanceProperty<T> getType() {
        return this.type;
    }

    @Override
    public AbstractWidget makeWidget(BaseScreen parent, int x, int y, int width, int height, Supplier<T> supplier, Consumer<T> consumer) {
        double value = Mth.map(((Number) supplier.get()).doubleValue(), this.min.doubleValue(), this.max.doubleValue(), 0.0, 1.0);
        return new AbstractSliderButton(x, y, width, height, Component.empty().append(this.title).append(": " + (int) ((Number) supplier.get()).doubleValue()), value) {

            private double value() {
                return Mth.map(this.f_93577_, 0.0, 1.0, NumberAppearancePropertyBuilder.this.min.doubleValue(), NumberAppearancePropertyBuilder.this.max.doubleValue());
            }

            @Override
            protected void updateMessage() {
                this.m_93666_(Component.empty().append(NumberAppearancePropertyBuilder.this.title).append(": " + (int) this.value()));
            }

            @Override
            protected void applyValue() {
                consumer.accept((Number) this.value());
            }
        };
    }
}