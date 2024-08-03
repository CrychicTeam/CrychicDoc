package com.github.alexthe666.citadel.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class LinkButton extends Button {

    public ItemStack previewStack;

    public GuiBasicBook book;

    public LinkButton(GuiBasicBook book, int x, int y, int width, int height, Component component, ItemStack previewStack, Button.OnPress onPress) {
        super(x, y, width + (previewStack.isEmpty() ? 0 : 6), height, component, onPress, Button.DEFAULT_NARRATION);
        this.previewStack = previewStack;
        this.book = book;
    }

    public LinkButton(GuiBasicBook book, int x, int y, int width, int height, Component component, Button.OnPress onPress) {
        this(book, x, y, width, height, component, ItemStack.EMPTY, onPress);
    }

    public int getFGColor() {
        return this.f_93622_ ? this.book.getWidgetColor() : (this.f_93623_ ? 9729114 : 10526880);
    }

    private int getTextureY() {
        int i = 1;
        if (!this.f_93623_) {
            i = 0;
        } else if (this.m_198029_()) {
            i = 2;
        }
        return 46 + i * 20;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int guiX, int guiY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderTexture(0, this.book.getBookButtonsTexture());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        int i = this.getTextureY();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        guiGraphics.blit(this.book.getBookButtonsTexture(), this.m_252754_(), this.m_252907_(), 0, 46 + i * 20, this.f_93618_ / 2, this.f_93619_);
        guiGraphics.blit(this.book.getBookButtonsTexture(), this.m_252754_() + this.f_93618_ / 2, this.m_252907_(), 200 - this.f_93618_ / 2, 46 + i * 20, this.f_93618_ / 2, this.f_93619_);
        if (this.f_93622_) {
            int color = this.book.getWidgetColor();
            int r = (color & 0xFF0000) >> 16;
            int g = (color & 0xFF00) >> 8;
            int b = color & 0xFF;
            int var12 = 3;
            BookBlit.blitWithColor(guiGraphics, this.book.getBookButtonsTexture(), this.m_252754_(), this.m_252907_(), 0.0F, (float) (46 + var12 * 20), this.f_93618_ / 2, this.f_93619_, 256, 256, r, g, b, 255);
            BookBlit.blitWithColor(guiGraphics, this.book.getBookButtonsTexture(), this.m_252754_() + this.f_93618_ / 2, this.m_252907_(), (float) (200 - this.f_93618_ / 2), (float) (46 + var12 * 20), this.f_93618_ / 2, this.f_93619_, 256, 256, r, g, b, 255);
        }
        int j = this.getFGColor();
        int itemTextOffset = this.previewStack.isEmpty() ? 0 : 8;
        if (!this.previewStack.isEmpty()) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            guiGraphics.renderItem(this.previewStack, this.m_252754_() + 2, this.m_252907_() + 1);
        }
        drawTextOf(guiGraphics, font, this.m_6035_(), this.m_252754_() + itemTextOffset + this.f_93618_ / 2, this.m_252907_() + (this.f_93619_ - 8) / 2, j | Mth.ceil(this.f_93625_ * 255.0F) << 24);
    }

    public static void drawTextOf(GuiGraphics guiGraphics, Font font, Component component, int x, int y, int color) {
        FormattedCharSequence formattedcharsequence = component.getVisualOrderText();
        guiGraphics.drawString(font, formattedcharsequence, (float) (x - font.width(formattedcharsequence) / 2), (float) y, color, false);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
    }
}