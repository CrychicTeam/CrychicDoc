package me.shedaniel.clothconfig2.gui.entries;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
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
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public class BooleanListEntry extends TooltipListEntry<Boolean> {

    private final AtomicBoolean bool;

    private final boolean original;

    private final Button buttonWidget;

    private final Button resetButton;

    private final Supplier<Boolean> defaultValue;

    private final List<AbstractWidget> widgets;

    @Deprecated
    @Internal
    public BooleanListEntry(Component fieldName, boolean bool, Component resetButtonKey, Supplier<Boolean> defaultValue, Consumer<Boolean> saveConsumer) {
        this(fieldName, bool, resetButtonKey, defaultValue, saveConsumer, null);
    }

    @Deprecated
    @Internal
    public BooleanListEntry(Component fieldName, boolean bool, Component resetButtonKey, Supplier<Boolean> defaultValue, Consumer<Boolean> saveConsumer, Supplier<Optional<Component[]>> tooltipSupplier) {
        this(fieldName, bool, resetButtonKey, defaultValue, saveConsumer, tooltipSupplier, false);
    }

    @Deprecated
    @Internal
    public BooleanListEntry(Component fieldName, boolean bool, Component resetButtonKey, Supplier<Boolean> defaultValue, Consumer<Boolean> saveConsumer, Supplier<Optional<Component[]>> tooltipSupplier, boolean requiresRestart) {
        super(fieldName, tooltipSupplier, requiresRestart);
        this.defaultValue = defaultValue;
        this.original = bool;
        this.bool = new AtomicBoolean(bool);
        this.buttonWidget = Button.builder(Component.empty(), widget -> this.bool.set(!this.bool.get())).bounds(0, 0, 150, 20).build();
        this.resetButton = Button.builder(resetButtonKey, widget -> this.bool.set((Boolean) defaultValue.get())).bounds(0, 0, Minecraft.getInstance().font.width(resetButtonKey) + 6, 20).build();
        this.saveCallback = saveConsumer;
        this.widgets = Lists.newArrayList(new AbstractWidget[] { this.buttonWidget, this.resetButton });
    }

    @Override
    public boolean isEdited() {
        return super.isEdited() || this.original != this.bool.get();
    }

    public Boolean getValue() {
        return this.bool.get();
    }

    @Override
    public Optional<Boolean> getDefaultValue() {
        return this.defaultValue == null ? Optional.empty() : Optional.ofNullable((Boolean) this.defaultValue.get());
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        Window window = Minecraft.getInstance().getWindow();
        this.resetButton.f_93623_ = this.isEditable() && this.getDefaultValue().isPresent() && (Boolean) this.defaultValue.get() != this.bool.get();
        this.resetButton.m_253211_(y);
        this.buttonWidget.f_93623_ = this.isEditable();
        this.buttonWidget.m_253211_(y);
        this.buttonWidget.m_93666_(this.getYesNoText(this.bool.get()));
        Component displayedFieldName = this.getDisplayedFieldName();
        if (Minecraft.getInstance().font.isBidirectional()) {
            graphics.drawString(Minecraft.getInstance().font, displayedFieldName.getVisualOrderText(), window.getGuiScaledWidth() - x - Minecraft.getInstance().font.width(displayedFieldName), y + 6, 16777215);
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

    public Component getYesNoText(boolean bool) {
        return Component.translatable("text.cloth-config.boolean.value." + bool);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return this.widgets;
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return this.widgets;
    }
}