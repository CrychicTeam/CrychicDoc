package vazkii.patchouli.client.book.text;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.common.book.Book;

public class Word {

    private final Book book;

    private final GuiBook gui;

    private final Component text;

    private final List<Word> linkCluster;

    private final Supplier<Boolean> onClick;

    public final int x;

    public final int y;

    public final int width;

    public final int height;

    public Word(GuiBook gui, Span span, MutableComponent text, int x, int y, int strWidth, List<Word> cluster) {
        this.book = gui.book;
        this.gui = gui;
        this.x = x;
        this.y = y;
        this.width = strWidth;
        this.height = 8;
        this.onClick = span.onClick;
        this.linkCluster = cluster;
        if (!span.tooltip.getString().isEmpty()) {
            text = text.withStyle(s -> s.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, span.tooltip)));
        }
        this.text = text;
    }

    public void render(GuiGraphics graphics, Font font, Style styleOverride, int mouseX, int mouseY) {
        MutableComponent toRender = this.text.copy().withStyle(styleOverride);
        if (this.isClusterHovered((double) mouseX, (double) mouseY)) {
            if (this.onClick != null) {
                toRender.withStyle(s -> s.withColor(TextColor.fromRgb(this.book.linkHoverColor)));
            }
            graphics.renderComponentHoverEffect(font, this.text.getStyle(), (int) this.gui.getRelativeX((double) mouseX), (int) this.gui.getRelativeY((double) mouseY));
        }
        graphics.drawString(font, toRender, this.x, this.y, -1, false);
    }

    public boolean click(double mouseX, double mouseY, int mouseButton) {
        return this.onClick != null && mouseButton == 0 && this.isHovered(mouseX, mouseY) ? (Boolean) this.onClick.get() : false;
    }

    private boolean isHovered(double mouseX, double mouseY) {
        return this.gui.isMouseInRelativeRange(mouseX, mouseY, this.x, this.y, this.width, this.height);
    }

    private boolean isClusterHovered(double mouseX, double mouseY) {
        if (this.linkCluster == null) {
            return this.isHovered(mouseX, mouseY);
        } else {
            for (Word w : this.linkCluster) {
                if (w.isHovered(mouseX, mouseY)) {
                    return true;
                }
            }
            return false;
        }
    }
}