package com.simibubi.create.foundation.config.ui.entries;

import com.simibubi.create.foundation.config.ui.ConfigScreen;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.BoxElement;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.element.TextStencilElement;
import com.simibubi.create.foundation.gui.widget.BoxWidget;
import com.simibubi.create.foundation.gui.widget.ElementWidget;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.common.ForgeConfigSpec;

public class EnumEntry extends ValueEntry<Enum<?>> {

    protected static final int cycleWidth = 34;

    protected TextStencilElement valueText = new TextStencilElement(Minecraft.getInstance().font, "YEP").centered(true, true);

    protected BoxWidget cycleLeft;

    protected BoxWidget cycleRight;

    public EnumEntry(String label, ForgeConfigSpec.ConfigValue<Enum<?>> value, ForgeConfigSpec.ValueSpec spec) {
        super(label, value, spec);
        this.valueText.withElementRenderer((ms, width, height, alpha) -> UIRenderHelper.angledGradient(ms, 0.0F, 0, height / 2, height, width, Theme.p(Theme.Key.TEXT)));
        DelegatedStencilElement l = AllIcons.I_CONFIG_PREV.asStencil();
        this.cycleLeft = new BoxWidget(0, 0, 42, 16).<BoxWidget>withCustomBackground(Theme.c(Theme.Key.PONDER_BACKGROUND_FLAT)).<ElementWidget>showingElement(l).withCallback(() -> this.cycleValue(-1));
        l.withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.cycleLeft));
        DelegatedStencilElement r = AllIcons.I_CONFIG_NEXT.asStencil();
        this.cycleRight = new BoxWidget(0, 0, 42, 16).<BoxWidget>withCustomBackground(Theme.c(Theme.Key.PONDER_BACKGROUND_FLAT)).<ElementWidget>showingElement(r).withCallback(() -> this.cycleValue(1));
        r.at(26.0F, 0.0F);
        r.withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.cycleRight));
        this.listeners.add(this.cycleLeft);
        this.listeners.add(this.cycleRight);
        this.onReset();
    }

    protected void cycleValue(int direction) {
        Enum<?> e = this.getValue();
        Enum<?>[] options = (Enum<?>[]) e.getDeclaringClass().getEnumConstants();
        e = options[Math.floorMod(e.ordinal() + direction, options.length)];
        this.setValue(e);
        this.bumpCog((float) direction * 15.0F);
    }

    @Override
    protected void setEditable(boolean b) {
        super.setEditable(b);
        this.cycleLeft.f_93623_ = b;
        this.cycleLeft.animateGradientFromState();
        this.cycleRight.f_93623_ = b;
        this.cycleRight.animateGradientFromState();
    }

    @Override
    public void tick() {
        super.tick();
        this.cycleLeft.tick();
        this.cycleRight.tick();
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean p_230432_9_, float partialTicks) {
        super.render(graphics, index, y, x, width, height, mouseX, mouseY, p_230432_9_, partialTicks);
        this.cycleLeft.m_252865_(x + this.getLabelWidth(width) + 4);
        this.cycleLeft.m_253211_(y + 10);
        this.cycleLeft.m_88315_(graphics, mouseX, mouseY, partialTicks);
        this.valueText.<RenderElement>at((float) (this.cycleLeft.m_252754_() + 34 - 8), (float) (y + 10), 200.0F).<RenderElement>withBounds(width - this.getLabelWidth(width) - 68 - 28 - 4, 16).render(graphics);
        this.cycleRight.m_252865_(x + width - 68 - 28 + 10);
        this.cycleRight.m_253211_(y + 10);
        this.cycleRight.m_88315_(graphics, mouseX, mouseY, partialTicks);
        new BoxElement().<BoxElement>withBackground(Theme.c(Theme.Key.PONDER_BACKGROUND_FLAT)).<BoxElement>flatBorder(16777216).<RenderElement>withBounds(48, 6).<RenderElement>at((float) (this.cycleLeft.m_252754_() + 22), (float) (this.cycleLeft.m_252907_() + 5)).render(graphics);
    }

    public void onValueChange(Enum<?> newValue) {
        super.onValueChange(newValue);
        this.valueText.withText(ConfigScreen.toHumanReadable(newValue.name().toLowerCase(Locale.ROOT)));
    }
}