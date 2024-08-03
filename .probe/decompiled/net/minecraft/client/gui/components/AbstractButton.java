package net.minecraft.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public abstract class AbstractButton extends AbstractWidget {

    protected static final int TEXTURE_Y_OFFSET = 46;

    protected static final int TEXTURE_WIDTH = 200;

    protected static final int TEXTURE_HEIGHT = 20;

    protected static final int TEXTURE_BORDER_X = 20;

    protected static final int TEXTURE_BORDER_Y = 4;

    protected static final int TEXT_MARGIN = 2;

    public AbstractButton(int int0, int int1, int int2, int int3, Component component4) {
        super(int0, int1, int2, int3, component4);
    }

    public abstract void onPress();

    @Override
    protected void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        Minecraft $$4 = Minecraft.getInstance();
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        guiGraphics0.blitNineSliced(f_93617_, this.m_252754_(), this.m_252907_(), this.m_5711_(), this.m_93694_(), 20, 4, 200, 20, 0, this.getTextureY());
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int $$5 = this.f_93623_ ? 16777215 : 10526880;
        this.renderString(guiGraphics0, $$4.font, $$5 | Mth.ceil(this.f_93625_ * 255.0F) << 24);
    }

    public void renderString(GuiGraphics guiGraphics0, Font font1, int int2) {
        this.m_280372_(guiGraphics0, font1, 2, int2);
    }

    private int getTextureY() {
        int $$0 = 1;
        if (!this.f_93623_) {
            $$0 = 0;
        } else if (this.m_198029_()) {
            $$0 = 2;
        }
        return 46 + $$0 * 20;
    }

    @Override
    public void onClick(double double0, double double1) {
        this.onPress();
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (!this.f_93623_ || !this.f_93624_) {
            return false;
        } else if (CommonInputs.selected(int0)) {
            this.m_7435_(Minecraft.getInstance().getSoundManager());
            this.onPress();
            return true;
        } else {
            return false;
        }
    }
}