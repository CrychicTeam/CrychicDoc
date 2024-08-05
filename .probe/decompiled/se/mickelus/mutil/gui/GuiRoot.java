package se.mickelus.mutil.gui;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class GuiRoot extends GuiElement {

    protected Minecraft mc;

    public GuiRoot(Minecraft mc) {
        super(0, 0, 0, 0);
        this.mc = mc;
    }

    public void draw(GuiGraphics graphics) {
        if (this.isVisible()) {
            Window window = this.mc.getWindow();
            this.width = window.getGuiScaledWidth();
            this.height = window.getGuiScaledHeight();
            double mouseX = this.mc.mouseHandler.xpos() * (double) this.width / (double) window.getScreenWidth();
            double mouseY = this.mc.mouseHandler.ypos() * (double) this.height / (double) window.getScreenHeight();
            this.drawChildren(graphics, 0, 0, this.width, this.height, (int) mouseX, (int) mouseY, 1.0F);
        }
    }
}