package me.shedaniel.clothconfig2.gui.entries;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public class IntegerSliderEntry extends TooltipListEntry<Integer> {

    protected IntegerSliderEntry.Slider sliderWidget;

    protected Button resetButton;

    protected AtomicInteger value;

    protected final long orginial;

    private int minimum;

    private int maximum;

    private final Supplier<Integer> defaultValue;

    private Function<Integer, Component> textGetter = integer -> Component.literal(String.format("Value: %d", integer));

    private final List<AbstractWidget> widgets;

    @Deprecated
    @Internal
    public IntegerSliderEntry(Component fieldName, int minimum, int maximum, int value, Component resetButtonKey, Supplier<Integer> defaultValue, Consumer<Integer> saveConsumer) {
        this(fieldName, minimum, maximum, value, resetButtonKey, defaultValue, saveConsumer, null);
    }

    @Deprecated
    @Internal
    public IntegerSliderEntry(Component fieldName, int minimum, int maximum, int value, Component resetButtonKey, Supplier<Integer> defaultValue, Consumer<Integer> saveConsumer, Supplier<Optional<Component[]>> tooltipSupplier) {
        this(fieldName, minimum, maximum, value, resetButtonKey, defaultValue, saveConsumer, tooltipSupplier, false);
    }

    @Deprecated
    @Internal
    public IntegerSliderEntry(Component fieldName, int minimum, int maximum, int value, Component resetButtonKey, Supplier<Integer> defaultValue, Consumer<Integer> saveConsumer, Supplier<Optional<Component[]>> tooltipSupplier, boolean requiresRestart) {
        super(fieldName, tooltipSupplier, requiresRestart);
        this.orginial = (long) value;
        this.defaultValue = defaultValue;
        this.value = new AtomicInteger(value);
        this.saveCallback = saveConsumer;
        this.maximum = maximum;
        this.minimum = minimum;
        this.sliderWidget = new IntegerSliderEntry.Slider(0, 0, 152, 20, ((double) this.value.get() - (double) minimum) / (double) Math.abs(maximum - minimum));
        this.resetButton = Button.builder(resetButtonKey, widget -> this.setValue((Integer) defaultValue.get())).bounds(0, 0, Minecraft.getInstance().font.width(resetButtonKey) + 6, 20).build();
        this.sliderWidget.m_93666_((Component) this.textGetter.apply(this.value.get()));
        this.widgets = Lists.newArrayList(new AbstractWidget[] { this.sliderWidget, this.resetButton });
    }

    public Function<Integer, Component> getTextGetter() {
        return this.textGetter;
    }

    public IntegerSliderEntry setTextGetter(Function<Integer, Component> textGetter) {
        this.textGetter = textGetter;
        this.sliderWidget.m_93666_((Component) textGetter.apply(this.value.get()));
        return this;
    }

    public Integer getValue() {
        return this.value.get();
    }

    @Deprecated
    public void setValue(int value) {
        this.sliderWidget.setValue((double) (Mth.clamp(value, this.minimum, this.maximum) - this.minimum) / (double) Math.abs(this.maximum - this.minimum));
        this.value.set(Math.min(Math.max(value, this.minimum), this.maximum));
        this.sliderWidget.updateMessage();
    }

    @Override
    public boolean isEdited() {
        return super.isEdited() || (long) this.getValue().intValue() != this.orginial;
    }

    @Override
    public Optional<Integer> getDefaultValue() {
        return this.defaultValue == null ? Optional.empty() : Optional.ofNullable((Integer) this.defaultValue.get());
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return this.widgets;
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return this.widgets;
    }

    public IntegerSliderEntry setMaximum(int maximum) {
        this.maximum = maximum;
        return this;
    }

    public IntegerSliderEntry setMinimum(int minimum) {
        this.minimum = minimum;
        return this;
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        Window window = Minecraft.getInstance().getWindow();
        this.resetButton.f_93623_ = this.isEditable() && this.getDefaultValue().isPresent() && (Integer) this.defaultValue.get() != this.value.get();
        this.resetButton.m_253211_(y);
        this.sliderWidget.f_93623_ = this.isEditable();
        this.sliderWidget.m_253211_(y);
        Component displayedFieldName = this.getDisplayedFieldName();
        if (Minecraft.getInstance().font.isBidirectional()) {
            graphics.drawString(Minecraft.getInstance().font, displayedFieldName.getVisualOrderText(), window.getGuiScaledWidth() - x - Minecraft.getInstance().font.width(displayedFieldName), y + 6, this.getPreferredTextColor());
            this.resetButton.m_252865_(x);
            this.sliderWidget.m_252865_(x + this.resetButton.m_5711_() + 1);
        } else {
            graphics.drawString(Minecraft.getInstance().font, displayedFieldName.getVisualOrderText(), x, y + 6, this.getPreferredTextColor());
            this.resetButton.m_252865_(x + entryWidth - this.resetButton.m_5711_());
            this.sliderWidget.m_252865_(x + entryWidth - 150);
        }
        this.sliderWidget.m_93674_(150 - this.resetButton.m_5711_() - 2);
        this.resetButton.m_88315_(graphics, mouseX, mouseY, delta);
        this.sliderWidget.m_88315_(graphics, mouseX, mouseY, delta);
    }

    private class Slider extends AbstractSliderButton {

        protected Slider(int int_1, int int_2, int int_3, int int_4, double double_1) {
            super(int_1, int_2, int_3, int_4, Component.empty(), double_1);
        }

        @Override
        public void updateMessage() {
            this.m_93666_((Component) IntegerSliderEntry.this.textGetter.apply(IntegerSliderEntry.this.value.get()));
        }

        @Override
        protected void applyValue() {
            IntegerSliderEntry.this.value.set((int) ((double) IntegerSliderEntry.this.minimum + (double) Math.abs(IntegerSliderEntry.this.maximum - IntegerSliderEntry.this.minimum) * this.f_93577_));
        }

        @Override
        public boolean keyPressed(int int_1, int int_2, int int_3) {
            return !IntegerSliderEntry.this.isEditable() ? false : super.keyPressed(int_1, int_2, int_3);
        }

        @Override
        public boolean mouseDragged(double double_1, double double_2, int int_1, double double_3, double double_4) {
            return !IntegerSliderEntry.this.isEditable() ? false : super.m_7979_(double_1, double_2, int_1, double_3, double_4);
        }

        public double getProgress() {
            return this.f_93577_;
        }

        public void setProgress(double integer) {
            this.f_93577_ = integer;
        }

        @Override
        public void setValue(double integer) {
            this.f_93577_ = integer;
        }
    }
}