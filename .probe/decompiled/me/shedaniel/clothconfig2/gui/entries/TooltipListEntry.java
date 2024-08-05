package me.shedaniel.clothconfig2.gui.entries;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.Tooltip;
import me.shedaniel.math.Point;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public abstract class TooltipListEntry<T> extends AbstractConfigListEntry<T> {

    @Nullable
    private Supplier<Optional<Component[]>> tooltipSupplier;

    @Deprecated
    @Internal
    public TooltipListEntry(Component fieldName, @Nullable Supplier<Optional<Component[]>> tooltipSupplier) {
        this(fieldName, tooltipSupplier, false);
    }

    @Deprecated
    @Internal
    public TooltipListEntry(Component fieldName, @Nullable Supplier<Optional<Component[]>> tooltipSupplier, boolean requiresRestart) {
        super(fieldName, requiresRestart);
        this.tooltipSupplier = tooltipSupplier;
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        if (this.isMouseInside(mouseX, mouseY, x, y, entryWidth, entryHeight)) {
            this.getTooltip(mouseX, mouseY).map(lines -> Tooltip.of(new Point(mouseX, mouseY), this.wrapLinesToScreen(lines))).ifPresent(this::addTooltip);
        }
    }

    public Optional<Component[]> getTooltip() {
        Stream<Component> tooltipStream = Stream.ofNullable(this.tooltipSupplier).map(Supplier::get).flatMap(Optional::stream).flatMap(Arrays::stream);
        Component disabled = this.isEnabled() ? null : Component.translatable("text.cloth-config.disabled_tooltip");
        Component[] lines = (Component[]) Stream.concat(tooltipStream, Stream.ofNullable(disabled)).toArray(Component[]::new);
        return lines.length < 1 ? Optional.empty() : Optional.of(lines);
    }

    public Optional<Component[]> getTooltip(int mouseX, int mouseY) {
        return this.getTooltip();
    }

    @Nullable
    public Supplier<Optional<Component[]>> getTooltipSupplier() {
        return this.tooltipSupplier;
    }

    public void setTooltipSupplier(@Nullable Supplier<Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
    }
}