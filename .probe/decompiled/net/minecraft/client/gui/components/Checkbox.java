package net.minecraft.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class Checkbox extends AbstractButton {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/checkbox.png");

    private static final int TEXT_COLOR = 14737632;

    private boolean selected;

    private final boolean showLabel;

    public Checkbox(int int0, int int1, int int2, int int3, Component component4, boolean boolean5) {
        this(int0, int1, int2, int3, component4, boolean5, true);
    }

    public Checkbox(int int0, int int1, int int2, int int3, Component component4, boolean boolean5, boolean boolean6) {
        super(int0, int1, int2, int3, component4);
        this.selected = boolean5;
        this.showLabel = boolean6;
    }

    @Override
    public void onPress() {
        this.selected = !this.selected;
    }

    public boolean selected() {
        return this.selected;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
        narrationElementOutput0.add(NarratedElementType.TITLE, this.m_5646_());
        if (this.f_93623_) {
            if (this.m_93696_()) {
                narrationElementOutput0.add(NarratedElementType.USAGE, Component.translatable("narration.checkbox.usage.focused"));
            } else {
                narrationElementOutput0.add(NarratedElementType.USAGE, Component.translatable("narration.checkbox.usage.hovered"));
            }
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        Minecraft $$4 = Minecraft.getInstance();
        RenderSystem.enableDepthTest();
        Font $$5 = $$4.font;
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        RenderSystem.enableBlend();
        guiGraphics0.blit(TEXTURE, this.m_252754_(), this.m_252907_(), this.m_93696_() ? 20.0F : 0.0F, this.selected ? 20.0F : 0.0F, 20, this.f_93619_, 64, 64);
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.showLabel) {
            guiGraphics0.drawString($$5, this.m_6035_(), this.m_252754_() + 24, this.m_252907_() + (this.f_93619_ - 8) / 2, 14737632 | Mth.ceil(this.f_93625_ * 255.0F) << 24);
        }
    }
}