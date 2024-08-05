package se.mickelus.tetra.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiString;

public class GuiStringSmall2 extends GuiString {

    MutableComponent textComponent;

    public GuiStringSmall2(int x, int y, String string) {
        super(x, y, string);
        this.setString(string);
    }

    public GuiStringSmall2(int x, int y, String string, int color) {
        super(x, y, string, color);
        this.setString(string);
    }

    public GuiStringSmall2(int x, int y, String string, GuiAttachment attachment) {
        super(x, y, string, attachment);
        this.setString(string);
    }

    public GuiStringSmall2(int x, int y, String string, int color, GuiAttachment attachment) {
        super(x, y, string, color, attachment);
        this.setString(string);
    }

    @Override
    public void setString(String string) {
        if (string != null) {
            this.textComponent = Component.literal(string.toUpperCase()).withStyle(Style.EMPTY.withFont(new ResourceLocation("tetra", "ascii_small")));
            this.width = this.fontRenderer.width(this.textComponent);
            if (this.fixedWidth) {
                this.textComponent = (MutableComponent) this.fontRenderer.substrByWidth(this.textComponent, this.width);
            } else {
                this.width = this.fontRenderer.width(this.textComponent);
            }
        }
    }
}