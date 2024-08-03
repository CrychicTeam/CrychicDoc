package me.shedaniel.clothconfig2.gui.entries;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public class SelectionListEntry<T> extends TooltipListEntry<T> {

    private final ImmutableList<T> values;

    private final AtomicInteger index;

    private final int original;

    private final Button buttonWidget;

    private final Button resetButton;

    private final Supplier<T> defaultValue;

    private final List<AbstractWidget> widgets;

    private final Function<T, Component> nameProvider;

    @Deprecated
    @Internal
    public SelectionListEntry(Component fieldName, T[] valuesArray, T value, Component resetButtonKey, Supplier<T> defaultValue, Consumer<T> saveConsumer) {
        this(fieldName, valuesArray, value, resetButtonKey, defaultValue, saveConsumer, null);
    }

    @Deprecated
    @Internal
    public SelectionListEntry(Component fieldName, T[] valuesArray, T value, Component resetButtonKey, Supplier<T> defaultValue, Consumer<T> saveConsumer, Function<T, Component> nameProvider) {
        this(fieldName, valuesArray, value, resetButtonKey, defaultValue, saveConsumer, nameProvider, null);
    }

    @Deprecated
    @Internal
    public SelectionListEntry(Component fieldName, T[] valuesArray, T value, Component resetButtonKey, Supplier<T> defaultValue, Consumer<T> saveConsumer, Function<T, Component> nameProvider, Supplier<Optional<Component[]>> tooltipSupplier) {
        this(fieldName, valuesArray, value, resetButtonKey, defaultValue, saveConsumer, nameProvider, tooltipSupplier, false);
    }

    @Deprecated
    @Internal
    public SelectionListEntry(Component fieldName, T[] valuesArray, T value, Component resetButtonKey, Supplier<T> defaultValue, Consumer<T> saveConsumer, Function<T, Component> nameProvider, Supplier<Optional<Component[]>> tooltipSupplier, boolean requiresRestart) {
        super(fieldName, tooltipSupplier, requiresRestart);
        if (valuesArray != null) {
            this.values = ImmutableList.copyOf(valuesArray);
        } else {
            this.values = ImmutableList.of(value);
        }
        this.defaultValue = defaultValue;
        this.index = new AtomicInteger(this.values.indexOf(value));
        this.index.compareAndSet(-1, 0);
        this.original = this.values.indexOf(value);
        this.buttonWidget = Button.builder(Component.empty(), widget -> {
            this.index.incrementAndGet();
            this.index.compareAndSet(this.values.size(), 0);
        }).bounds(0, 0, 150, 20).build();
        this.resetButton = Button.builder(resetButtonKey, widget -> this.index.set(this.getDefaultIndex())).bounds(0, 0, Minecraft.getInstance().font.width(resetButtonKey) + 6, 20).build();
        this.saveCallback = saveConsumer;
        this.widgets = Lists.newArrayList(new AbstractWidget[] { this.buttonWidget, this.resetButton });
        this.nameProvider = nameProvider == null ? t -> Component.translatable(t instanceof SelectionListEntry.Translatable ? ((SelectionListEntry.Translatable) t).getKey() : t.toString()) : nameProvider;
    }

    @Override
    public boolean isEdited() {
        return super.isEdited() || !Objects.equals(this.index.get(), this.original);
    }

    @Override
    public T getValue() {
        return (T) this.values.get(this.index.get());
    }

    @Override
    public Optional<T> getDefaultValue() {
        return this.defaultValue == null ? Optional.empty() : Optional.ofNullable(this.defaultValue.get());
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        Window window = Minecraft.getInstance().getWindow();
        this.resetButton.f_93623_ = this.isEditable() && this.getDefaultValue().isPresent() && this.getDefaultIndex() != this.index.get();
        this.resetButton.m_253211_(y);
        this.buttonWidget.f_93623_ = this.isEditable();
        this.buttonWidget.m_253211_(y);
        this.buttonWidget.m_93666_((Component) this.nameProvider.apply(this.getValue()));
        Component displayedFieldName = this.getDisplayedFieldName();
        if (Minecraft.getInstance().font.isBidirectional()) {
            graphics.drawString(Minecraft.getInstance().font, displayedFieldName.getVisualOrderText(), window.getGuiScaledWidth() - x - Minecraft.getInstance().font.width(displayedFieldName), y + 6, this.getPreferredTextColor());
            this.resetButton.m_252865_(x);
            this.buttonWidget.m_252865_(x + this.resetButton.m_5711_() + 2);
        } else {
            graphics.drawString(Minecraft.getInstance().font, displayedFieldName.getVisualOrderText(), x, y + 6, this.getPreferredTextColor());
            this.resetButton.m_252865_(x + entryWidth - this.resetButton.m_5711_());
            this.buttonWidget.m_252865_(x + entryWidth - 150);
        }
        this.buttonWidget.m_93674_(150 - this.resetButton.m_5711_() - 2);
        this.resetButton.m_88315_(graphics, mouseX, mouseY, delta);
        this.buttonWidget.m_88315_(graphics, mouseX, mouseY, delta);
    }

    private int getDefaultIndex() {
        return Math.max(0, this.values.indexOf(this.defaultValue.get()));
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return this.widgets;
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return this.widgets;
    }

    public interface Translatable {

        @NotNull
        String getKey();
    }
}