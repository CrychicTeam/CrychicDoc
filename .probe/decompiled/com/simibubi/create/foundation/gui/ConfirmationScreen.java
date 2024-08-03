package com.simibubi.create.foundation.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.element.BoxElement;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.element.TextStencilElement;
import com.simibubi.create.foundation.gui.widget.BoxWidget;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;

public class ConfirmationScreen extends AbstractSimiScreen {

    private Screen source;

    private Consumer<ConfirmationScreen.Response> action = _success -> {
    };

    private List<FormattedText> text = new ArrayList();

    private boolean centered = false;

    private int x;

    private int y;

    private int textWidth;

    private int textHeight;

    private boolean tristate;

    private BoxWidget confirm;

    private BoxWidget confirmDontSave;

    private BoxWidget cancel;

    private BoxElement textBackground;

    public ConfirmationScreen removeTextLines(int amount) {
        if (amount > this.text.size()) {
            return this.clearText();
        } else {
            this.text.subList(this.text.size() - amount, this.text.size()).clear();
            return this;
        }
    }

    public ConfirmationScreen clearText() {
        this.text.clear();
        return this;
    }

    public ConfirmationScreen addText(FormattedText text) {
        this.text.add(text);
        return this;
    }

    public ConfirmationScreen withText(FormattedText text) {
        return this.clearText().addText(text);
    }

    public ConfirmationScreen at(int x, int y) {
        this.x = Math.max(x, 0);
        this.y = Math.max(y, 0);
        this.centered = false;
        return this;
    }

    public ConfirmationScreen centered() {
        this.centered = true;
        return this;
    }

    public ConfirmationScreen withAction(Consumer<Boolean> action) {
        this.action = r -> action.accept(r == ConfirmationScreen.Response.Confirm);
        return this;
    }

    public ConfirmationScreen withThreeActions(Consumer<ConfirmationScreen.Response> action) {
        this.action = action;
        this.tristate = true;
        return this;
    }

    public void open(@Nonnull Screen source) {
        this.source = source;
        Minecraft client = source.getMinecraft();
        this.m_6575_(client, client.getWindow().getGuiScaledWidth(), client.getWindow().getGuiScaledHeight());
        this.f_96541_.screen = this;
    }

    @Override
    public void tick() {
        super.tick();
        this.source.tick();
    }

    @Override
    protected void init() {
        super.init();
        ArrayList<FormattedText> copy = new ArrayList(this.text);
        this.text.clear();
        copy.forEach(t -> this.text.addAll(this.f_96547_.getSplitter().splitLines(t, 300, Style.EMPTY)));
        this.textHeight = this.text.size() * (9 + 1) + 4;
        this.textWidth = 300;
        if (this.centered) {
            this.x = this.f_96543_ / 2 - this.textWidth / 2 - 2;
            this.y = this.f_96544_ / 2 - this.textHeight / 2 - 16;
        } else {
            this.x = Math.max(0, this.x - this.textWidth / 2);
            this.y = Math.max(0, this.y = this.y - this.textHeight);
        }
        if (this.x + this.textWidth > this.f_96543_) {
            this.x = this.f_96543_ - this.textWidth;
        }
        if (this.y + this.textHeight + 30 > this.f_96544_) {
            this.y = this.f_96544_ - this.textHeight - 30;
        }
        int buttonX = this.x + this.textWidth / 2 - 6 - (int) (70.0F * (this.tristate ? 1.5F : 1.0F));
        TextStencilElement confirmText = new TextStencilElement(this.f_96547_, this.tristate ? "Save" : "Confirm").centered(true, true);
        this.confirm = new BoxWidget(buttonX, this.y + this.textHeight + 6, 70, 16).withCallback(() -> this.accept(ConfirmationScreen.Response.Confirm));
        this.confirm.showingElement(confirmText.withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.confirm)));
        this.m_142416_(this.confirm);
        buttonX += 82;
        if (this.tristate) {
            TextStencilElement confirmDontSaveText = new TextStencilElement(this.f_96547_, "Don't Save").centered(true, true);
            this.confirmDontSave = new BoxWidget(buttonX, this.y + this.textHeight + 6, 70, 16).withCallback(() -> this.accept(ConfirmationScreen.Response.ConfirmDontSave));
            this.confirmDontSave.showingElement(confirmDontSaveText.withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.confirmDontSave)));
            this.m_142416_(this.confirmDontSave);
            buttonX += 82;
        }
        TextStencilElement cancelText = new TextStencilElement(this.f_96547_, "Cancel").centered(true, true);
        this.cancel = new BoxWidget(buttonX, this.y + this.textHeight + 6, 70, 16).withCallback(() -> this.accept(ConfirmationScreen.Response.Cancel));
        this.cancel.showingElement(cancelText.withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.cancel)));
        this.m_142416_(this.cancel);
        this.textBackground = new BoxElement().<BoxElement>gradientBorder(Theme.p(Theme.Key.BUTTON_DISABLE)).<RenderElement>withBounds(this.f_96543_ + 10, this.textHeight + 35).at(-5.0F, (float) (this.y - 5));
        if (this.text.size() == 1) {
            this.x = (this.f_96543_ - this.f_96547_.width((FormattedText) this.text.get(0))) / 2;
        }
    }

    @Override
    public void onClose() {
        this.accept(ConfirmationScreen.Response.Cancel);
    }

    private void accept(ConfirmationScreen.Response success) {
        this.f_96541_.screen = this.source;
        this.action.accept(success);
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.textBackground.render(graphics);
        int offset = 9 + 1;
        int lineY = this.y - offset;
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate(0.0F, 0.0F, 200.0F);
        for (FormattedText line : this.text) {
            lineY += offset;
            if (line != null) {
                graphics.drawString(this.f_96547_, line.getString(), this.x, lineY, 15395562, false);
            }
        }
        ms.popPose();
    }

    @Override
    protected void renderWindowBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.endFrame();
        this.source.render(graphics, 0, 0, 10.0F);
        this.prepareFrame();
        graphics.fillGradient(0, 0, this.f_96543_, this.f_96544_, 1880100880, -2146430960);
    }

    @Override
    protected void prepareFrame() {
        UIRenderHelper.swapAndBlitColor(this.f_96541_.getMainRenderTarget(), UIRenderHelper.framebuffer);
        RenderSystem.clear(1280, Minecraft.ON_OSX);
    }

    @Override
    protected void endFrame() {
        UIRenderHelper.swapAndBlitColor(UIRenderHelper.framebuffer, this.f_96541_.getMainRenderTarget());
    }

    @Override
    public void resize(@Nonnull Minecraft client, int width, int height) {
        super.m_6574_(client, width, height);
        this.source.resize(client, width, height);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    public static enum Response {

        Confirm, ConfirmDontSave, Cancel
    }
}