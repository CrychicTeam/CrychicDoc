package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.widgets.utils.Rect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class ScrollTextList extends EList<ScrollTextList.ScrollTextEntry> {

    protected ScrollTextList(int x, int y, int w, int h) {
        super(9, x, y, w, h);
    }

    public static ScrollTextList createFromString(String s, int x, int y, int w, int h) {
        String[] lines = s.split("\n");
        return createFromString(lines, x, y, w, h);
    }

    public static ScrollTextList createFromString(String[] lines, int x, int y, int w, int h) {
        ScrollTextList list = new ScrollTextList(x, y, w, h);
        Font f = Minecraft.getInstance().font;
        for (String line : lines) {
            int fullWidth = f.width(line);
            int lineWidth = w - list.padding.x() * 2;
            if (fullWidth <= lineWidth) {
                list.addLine(line);
            } else {
                String currentLine = line;
                String trimmedLine = f.plainSubstrByWidth(line, lineWidth);
                int ix;
                for (ix = 0; !trimmedLine.equals(currentLine); ix++) {
                    String tempLine = currentLine;
                    currentLine = currentLine.substring(trimmedLine.length());
                    if (!currentLine.startsWith(" ") && !trimmedLine.endsWith(" ")) {
                        int li = trimmedLine.lastIndexOf(" ");
                        trimmedLine = trimmedLine.substring(0, li);
                        currentLine = tempLine.substring(trimmedLine.length()).trim();
                        list.addLine(trimmedLine, ix);
                    } else {
                        list.addLine(trimmedLine, ix);
                    }
                    trimmedLine = f.plainSubstrByWidth(currentLine, lineWidth);
                }
                list.addLine(trimmedLine, ix);
            }
        }
        return list;
    }

    public void addLine(String line, int ix) {
        this.addLine((ix == 0 ? "" : " ") + line.trim());
    }

    public void addLine(String line) {
        this.addItem(new ScrollTextList.ScrollTextEntry(line, this));
    }

    public static class ScrollTextEntry extends EList.EListEntry<ScrollTextList.ScrollTextEntry> {

        protected String text;

        protected ScrollTextEntry(String text, EList<ScrollTextList.ScrollTextEntry> container) {
            super(container);
            this.text = text;
        }

        @Override
        public void renderWidget(@NotNull GuiGraphics guiGraphics, Rect r, float partialTick) {
            guiGraphics.drawString(this.font, this.text, r.left(), r.top(), this.getVariant().text());
        }

        public String text() {
            return this.text;
        }
    }
}