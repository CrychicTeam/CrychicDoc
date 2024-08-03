package me.shedaniel.clothconfig2.impl.builders;

import java.util.Optional;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class TextDescriptionBuilder extends FieldBuilder<Component, TextListEntry, TextDescriptionBuilder> {

    private int color = -1;

    @Nullable
    private Supplier<Optional<Component[]>> tooltipSupplier = null;

    private final Component value;

    public TextDescriptionBuilder(Component resetButtonKey, Component fieldNameKey, Component value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }

    @Override
    public void requireRestart(boolean requireRestart) {
        throw new UnsupportedOperationException();
    }

    public TextDescriptionBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }

    public TextDescriptionBuilder setTooltip(Optional<Component[]> tooltip) {
        this.tooltipSupplier = () -> tooltip;
        return this;
    }

    public TextDescriptionBuilder setTooltip(Component... tooltip) {
        this.tooltipSupplier = () -> Optional.ofNullable(tooltip);
        return this;
    }

    public TextDescriptionBuilder setColor(int color) {
        this.color = color;
        return this;
    }

    @NotNull
    public TextListEntry build() {
        return this.finishBuilding(new TextListEntry(this.getFieldNameKey(), this.value, this.color, this.tooltipSupplier));
    }
}