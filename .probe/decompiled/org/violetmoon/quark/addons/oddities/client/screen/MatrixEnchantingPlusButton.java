package org.violetmoon.quark.addons.oddities.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class MatrixEnchantingPlusButton extends Button {

    public MatrixEnchantingPlusButton(int x, int y, Button.OnPress onPress) {
        super(new Button.Builder(Component.literal(""), onPress).size(50, 12).pos(x, y));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        boolean hovered = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
        if (this.f_93624_) {
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int u = 0;
            int v = 177;
            if (!this.f_93623_) {
                v += 12;
            } else if (hovered) {
                v += 24;
            }
            guiGraphics.blit(MatrixEnchantingScreen.BACKGROUND, this.m_252754_(), this.m_252907_(), u, v, this.f_93618_, this.f_93619_);
        }
    }
}