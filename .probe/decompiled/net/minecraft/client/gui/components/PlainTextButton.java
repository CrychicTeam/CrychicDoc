package net.minecraft.client.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;

public class PlainTextButton extends Button {

    private final Font font;

    private final Component message;

    private final Component underlinedMessage;

    public PlainTextButton(int int0, int int1, int int2, int int3, Component component4, Button.OnPress buttonOnPress5, Font font6) {
        super(int0, int1, int2, int3, component4, buttonOnPress5, f_252438_);
        this.font = font6;
        this.message = component4;
        this.underlinedMessage = ComponentUtils.mergeStyles(component4.copy(), Style.EMPTY.withUnderlined(true));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        Component $$4 = this.m_198029_() ? this.underlinedMessage : this.message;
        guiGraphics0.drawString(this.font, $$4, this.m_252754_(), this.m_252907_(), 16777215 | Mth.ceil(this.f_93625_ * 255.0F) << 24);
    }
}