package com.mrcrayfish.catalogue.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;

public class CatalogueCheckBoxButton extends Checkbox {

    private static final ResourceLocation TEXTURE = new ResourceLocation("catalogue", "textures/gui/checkbox.png");

    private final CatalogueCheckBoxButton.OnPress onPress;

    public CatalogueCheckBoxButton(int x, int y, CatalogueCheckBoxButton.OnPress onPress) {
        super(x, y, 14, 14, CommonComponents.EMPTY, false);
        this.onPress = onPress;
    }

    @Override
    public void onPress() {
        super.onPress();
        this.onPress.onPress(this);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        graphics.blit(TEXTURE, this.m_252754_(), this.m_252907_(), this.m_198029_() ? 14.0F : 0.0F, this.m_93840_() ? 14.0F : 0.0F, 14, 14, 64, 64);
    }

    public interface OnPress {

        void onPress(Checkbox var1);
    }
}