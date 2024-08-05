package com.simibubi.create.foundation.ponder.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.widget.BoxWidget;
import com.simibubi.create.foundation.gui.widget.ElementWidget;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class PonderButton extends BoxWidget {

    protected ItemStack item;

    protected PonderTag tag;

    protected KeyMapping shortcut;

    protected LerpedFloat flash = LerpedFloat.linear().startWithValue(0.0).chase(0.0, 0.1F, LerpedFloat.Chaser.EXP);

    public PonderButton(int x, int y) {
        this(x, y, 20, 20);
    }

    public PonderButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.z = 420.0F;
        this.paddingX = 2.0F;
        this.paddingY = 2.0F;
    }

    public <T extends PonderButton> T withShortcut(KeyMapping key) {
        this.shortcut = key;
        return (T) this;
    }

    public <T extends PonderButton> T showingTag(PonderTag tag) {
        return this.showing(this.tag = tag);
    }

    public <T extends PonderButton> T showing(ItemStack item) {
        this.item = item;
        return super.showingElement(GuiGameElement.of(item).scale(1.5).at(-4.0F, -4.0F));
    }

    @Override
    public <T extends ElementWidget> T showingElement(RenderElement element) {
        return super.showingElement(element);
    }

    public void flash() {
        this.flash.updateChaseTarget(1.0F);
    }

    public void dim() {
        this.flash.updateChaseTarget(0.0F);
    }

    @Override
    public void tick() {
        super.tick();
        this.flash.tickChaser();
    }

    @Override
    protected void beforeRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.beforeRender(graphics, mouseX, mouseY, partialTicks);
        float flashValue = this.flash.getValue(partialTicks);
        if (flashValue > 0.1F) {
            float sin = 0.5F + 0.5F * Mth.sin(((float) AnimationTickHolder.getTicks(true) + partialTicks) / 5.0F);
            sin *= flashValue;
            Color nc1 = new Color(255, 255, 255, Mth.clamp(this.gradientColor1.getAlpha() + 150, 0, 255));
            Color nc2 = new Color(155, 155, 155, Mth.clamp(this.gradientColor2.getAlpha() + 150, 0, 255));
            this.gradientColor1 = this.gradientColor1.mixWith(nc1, sin);
            this.gradientColor2 = this.gradientColor2.mixWith(nc2, sin);
        }
    }

    @Override
    public void doRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.doRender(graphics, mouseX, mouseY, partialTicks);
        float fadeValue = this.fade.getValue();
        if (!(fadeValue < 0.1F)) {
            if (this.shortcut != null) {
                PoseStack ms = graphics.pose();
                ms.pushPose();
                ms.translate(0.0F, 0.0F, this.z + 10.0F);
                graphics.drawCenteredString(Minecraft.getInstance().font, this.shortcut.getTranslatedKeyMessage(), this.m_252754_() + this.f_93618_ / 2 + 8, this.m_252907_() + this.f_93619_ - 6, Theme.c(Theme.Key.TEXT_DARKER).scaleAlpha(fadeValue).getRGB());
                ms.popPose();
            }
        }
    }

    public ItemStack getItem() {
        return this.item;
    }

    public PonderTag getTag() {
        return this.tag;
    }

    @Override
    public Theme.Key getDisabledTheme() {
        return Theme.Key.PONDER_BUTTON_DISABLE;
    }

    @Override
    public Theme.Key getIdleTheme() {
        return Theme.Key.PONDER_BUTTON_IDLE;
    }

    @Override
    public Theme.Key getHoverTheme() {
        return Theme.Key.PONDER_BUTTON_HOVER;
    }

    @Override
    public Theme.Key getClickTheme() {
        return Theme.Key.PONDER_BUTTON_CLICK;
    }
}