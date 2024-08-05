package com.github.alexthe666.iceandfire.client.gui.bestiary;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ChangePageButton extends Button {

    private final boolean right;

    public int lastpage = 1;

    private final int color;

    public ChangePageButton(int x, int y, boolean right, int color, Button.OnPress press) {
        super(x, y, 23, 10, Component.literal(""), press, f_252438_);
        this.right = right;
        this.color = color;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics matrixStack, int mouseX, int mouseY, float partial) {
        if (this.f_93623_) {
            ResourceLocation resourceLocation = new ResourceLocation("iceandfire:textures/gui/bestiary/widgets.png");
            boolean flag = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            int i = 0;
            int j = 64;
            if (flag) {
                i += 23;
            }
            if (!this.right) {
                j += 13;
            }
            j += this.color * 23;
            matrixStack.blit(resourceLocation, this.m_252754_(), this.m_252907_(), i, j, this.f_93618_, this.f_93619_);
        }
    }
}