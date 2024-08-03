package org.violetmoon.quark.content.tweaks.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.violetmoon.quark.base.client.handler.ClientUtil;
import org.violetmoon.quark.content.tweaks.client.emote.EmoteDescriptor;

public final class NotButton {

    private final int x;

    private final int y;

    private final int width;

    private final int height;

    private final Object label;

    private final Runnable onClick;

    private boolean oldMouseDown;

    public NotButton(int x, int y, int width, int height, Object label, Runnable onClick) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.onClick = onClick;
    }

    public void draw(GuiGraphics gfx, int mouseX, int mouseY, boolean mouseDown) {
        boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        int bgColor = Minecraft.getInstance().options.getBackgroundColor(hovered ? -1610612736 : Integer.MIN_VALUE);
        gfx.fill(this.x, this.y, this.x + this.width, this.y + this.height, bgColor);
        gfx.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.label instanceof Component comp) {
            this.drawTextLabel(gfx, comp);
        } else if (this.label instanceof String s) {
            this.drawTextLabel(gfx, Component.literal(s));
        } else if (this.label instanceof EmoteDescriptor emote) {
            this.drawEmote(gfx, emote, hovered);
        }
        if (!this.oldMouseDown && mouseDown && hovered) {
            this.onClick.run();
        }
        this.oldMouseDown = mouseDown;
    }

    void drawTextLabel(GuiGraphics gfx, Component blah) {
        int var10003 = (this.x + this.x + this.width) / 2;
        gfx.drawCenteredString(Minecraft.getInstance().font, blah, var10003, (this.y + this.y + this.height) / 2 - 4, -1);
    }

    void drawEmote(GuiGraphics guiGraphics, EmoteDescriptor desc, boolean hovered) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(desc.texture, this.x + 4, this.y + 4, 0.0F, 0.0F, 16, 16, 16, 16);
        ResourceLocation tierTexture = desc.getTierTexture();
        if (tierTexture != null) {
            guiGraphics.blit(tierTexture, this.x + 4, this.y + 4, 0.0F, 0.0F, 16, 16, 16, 16);
        }
        if (hovered) {
            String name = desc.getLocalizedName();
            ClientUtil.drawChatBubble(guiGraphics, this.x, this.y, mc.font, name, 1.0F, false);
        }
    }
}