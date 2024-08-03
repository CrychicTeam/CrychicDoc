package me.shedaniel.clothconfig2.impl.builders;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class BooleanToggleBuilder extends AbstractFieldBuilder<Boolean, BooleanListEntry, BooleanToggleBuilder> {

    @Nullable
    private Function<Boolean, Component> yesNoTextSupplier = null;

    public BooleanToggleBuilder(Component resetButtonKey, Component fieldNameKey, boolean value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }

    public BooleanToggleBuilder setErrorSupplier(Function<Boolean, Optional<Component>> errorSupplier) {
        return (BooleanToggleBuilder) super.setErrorSupplier(errorSupplier);
    }

    public BooleanToggleBuilder requireRestart() {
        return (BooleanToggleBuilder) super.requireRestart();
    }

    public BooleanToggleBuilder setSaveConsumer(Consumer<Boolean> saveConsumer) {
        return (BooleanToggleBuilder) super.setSaveConsumer(saveConsumer);
    }

    public BooleanToggleBuilder setDefaultValue(Supplier<Boolean> defaultValue) {
        return (BooleanToggleBuilder) super.setDefaultValue(defaultValue);
    }

    public BooleanToggleBuilder setDefaultValue(boolean defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }

    public BooleanToggleBuilder setTooltipSupplier(Function<Boolean, Optional<Component[]>> tooltipSupplier) {
        return (BooleanToggleBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public BooleanToggleBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (BooleanToggleBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public BooleanToggleBuilder setTooltip(Optional<Component[]> tooltip) {
        return (BooleanToggleBuilder) super.setTooltip(tooltip);
    }

    public BooleanToggleBuilder setTooltip(Component... tooltip) {
        return (BooleanToggleBuilder) super.setTooltip(tooltip);
    }

    @Nullable
    public Function<Boolean, Component> getYesNoTextSupplier() {
        return this.yesNoTextSupplier;
    }

    public BooleanToggleBuilder setYesNoTextSupplier(@Nullable Function<Boolean, Component> yesNoTextSupplier) {
        this.yesNoTextSupplier = yesNoTextSupplier;
        return this;
    }

    @NotNull
    public BooleanListEntry build() {
        BooleanListEntry entry = new BooleanListEntry(this.getFieldNameKey(), this.value, this.getResetButtonKey(), this.defaultValue, this.getSaveConsumer(), null, this.isRequireRestart()) {

            @Override
            public Component getYesNoText(boolean bool) {
                return BooleanToggleBuilder.this.yesNoTextSupplier == null ? super.getYesNoText(bool) : (Component) BooleanToggleBuilder.this.yesNoTextSupplier.apply(bool);
            }
        };
        entry.setTooltipSupplier(() -> (Optional) this.getTooltipSupplier().apply(entry.getValue()));
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> (Optional) this.errorSupplier.apply(entry.getValue()));
        }
        return this.finishBuilding(entry);
    }
}