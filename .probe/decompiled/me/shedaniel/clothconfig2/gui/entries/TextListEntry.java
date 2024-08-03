package me.shedaniel.clothconfig2.gui.entries;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.AbstractConfigScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public class TextListEntry extends TooltipListEntry<Object> {

    public static final int LINE_HEIGHT = 12;

    public static final int DISABLED_COLOR = (Integer) Objects.requireNonNull(ChatFormatting.DARK_GRAY.getColor());

    private final Font textRenderer = Minecraft.getInstance().font;

    private final int color;

    private final Component text;

    private int savedWidth = -1;

    private int savedX = -1;

    private int savedY = -1;

    private List<FormattedCharSequence> wrappedLines;

    @Deprecated
    @Internal
    public TextListEntry(Component fieldName, Component text) {
        this(fieldName, text, -1);
    }

    @Deprecated
    @Internal
    public TextListEntry(Component fieldName, Component text, int color) {
        this(fieldName, text, color, null);
    }

    @Deprecated
    @Internal
    public TextListEntry(Component fieldName, Component text, int color, Supplier<Optional<Component[]>> tooltipSupplier) {
        super(fieldName, tooltipSupplier);
        this.text = text;
        this.color = color;
        this.wrappedLines = Collections.emptyList();
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        if (this.savedWidth != entryWidth || this.savedX != x || this.savedY != y) {
            this.wrappedLines = this.textRenderer.split(this.text, entryWidth);
            this.savedWidth = entryWidth;
            this.savedX = x;
            this.savedY = y;
        }
        int yy = y + 7;
        int textColor = this.isEnabled() ? this.color : DISABLED_COLOR;
        for (FormattedCharSequence string : this.wrappedLines) {
            graphics.drawString(Minecraft.getInstance().font, string, x, yy, textColor);
            yy += 9 + 3;
        }
        Style style = this.getTextAt((double) mouseX, (double) mouseY);
        AbstractConfigScreen configScreen = this.getConfigScreen();
        if (style != null && configScreen != null) {
            graphics.renderComponentHoverEffect(Minecraft.getInstance().font, style, mouseX, mouseY);
        }
    }

    @Override
    public int getItemHeight() {
        if (this.savedWidth == -1) {
            return 12;
        } else {
            int lineCount = this.wrappedLines.size();
            return lineCount == 0 ? 0 : 14 + lineCount * 12;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            Style style = this.getTextAt(mouseX, mouseY);
            AbstractConfigScreen configScreen = this.getConfigScreen();
            if (configScreen != null && configScreen.handleComponentClicked(style)) {
                return true;
            }
        }
        return super.m_6375_(mouseX, mouseY, button);
    }

    @Nullable
    private Style getTextAt(double x, double y) {
        int lineCount = this.wrappedLines.size();
        if (lineCount > 0) {
            int textX = Mth.floor(x - (double) this.savedX);
            int textY = Mth.floor(y - 7.0 - (double) this.savedY);
            if (textX >= 0 && textY >= 0 && textX <= this.savedWidth && textY < 12 * lineCount + lineCount) {
                int line = textY / 12;
                if (line < this.wrappedLines.size()) {
                    FormattedCharSequence orderedText = (FormattedCharSequence) this.wrappedLines.get(line);
                    return this.textRenderer.getSplitter().componentStyleAtWidth(orderedText, textX);
                }
            }
        }
        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public Optional<Object> getDefaultValue() {
        return Optional.empty();
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.emptyList();
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return Collections.emptyList();
    }
}