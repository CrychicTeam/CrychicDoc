package de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry;

import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.ScrollArea;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class TextScrollAreaEntry extends ScrollAreaEntry {

    private static final Logger LOGGER = LogManager.getLogger();

    protected Component text;

    protected int textWidth;

    public Font font;

    protected Consumer<TextScrollAreaEntry> onClickCallback;

    public TextScrollAreaEntry(ScrollArea parent, @NotNull Component text, @NotNull Consumer<TextScrollAreaEntry> onClick) {
        super(parent, 0, 14);
        this.font = Minecraft.getInstance().font;
        this.onClickCallback = onClick;
        this.setText(text);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        super.render(graphics, mouseX, mouseY, partial);
        int centerY = this.getY() + this.getHeight() / 2;
        graphics.drawString(this.font, this.text, this.getX() + 5, centerY - 9 / 2, -1, false);
    }

    @Override
    public void onClick(ScrollAreaEntry entry) {
        this.onClickCallback.accept((TextScrollAreaEntry) entry);
    }

    public void setText(@NotNull Component text) {
        this.text = text;
        this.textWidth = this.font.width(this.text);
        this.setWidth(5 + this.textWidth + 5);
    }

    public Component getText() {
        return this.text;
    }

    public int getTextWidth() {
        return this.textWidth;
    }
}