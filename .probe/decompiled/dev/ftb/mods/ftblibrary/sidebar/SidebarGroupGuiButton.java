package dev.ftb.mods.ftblibrary.sidebar;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public class SidebarGroupGuiButton extends AbstractButton {

    public static Rect2i lastDrawnArea = new Rect2i(0, 0, 0, 0);

    private final List<SidebarGuiButton> buttons = new ArrayList();

    private SidebarGuiButton mouseOver;

    public SidebarGroupGuiButton() {
        super(0, 0, 0, 0, Component.empty());
    }

    @Override
    public void render(GuiGraphics graphics, int mx, int my, float partialTicks) {
        this.buttons.clear();
        this.mouseOver = null;
        int ry = 0;
        for (SidebarButtonGroup group : SidebarButtonManager.INSTANCE.getGroups()) {
            int rx = 0;
            boolean addedAny = false;
            for (SidebarButton button : group.getButtons()) {
                if (button.isActuallyVisible()) {
                    this.buttons.add(new SidebarGuiButton(rx, ry, button));
                    rx++;
                    addedAny = true;
                }
            }
            if (addedAny) {
                ry++;
            }
        }
        for (SidebarGuiButton buttonx : this.buttons) {
            buttonx.x = 1 + buttonx.buttonX * 17;
            buttonx.y = 1 + buttonx.buttonY * 17;
        }
        this.m_252865_(Integer.MAX_VALUE);
        this.m_253211_(Integer.MAX_VALUE);
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (SidebarGuiButton b : this.buttons) {
            if (b.x >= 0 && b.y >= 0) {
                this.m_252865_(Math.min(this.m_252754_(), b.x));
                this.m_253211_(Math.min(this.m_252907_(), b.y));
                maxX = Math.max(maxX, b.x + 16);
                maxY = Math.max(maxY, b.y + 16);
            }
            if (mx >= b.x && my >= b.y && mx < b.x + 16 && my < b.y + 16) {
                this.mouseOver = b;
            }
        }
        this.m_252865_(Math.max(0, this.m_252754_() - 2));
        this.m_253211_(Math.max(0, this.m_252907_() - 2));
        maxX += 2;
        maxY += 2;
        this.f_93618_ = maxX - this.m_252754_();
        this.f_93619_ = maxY - this.m_252907_();
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 500.0F);
        Font font = Minecraft.getInstance().font;
        for (SidebarGuiButton b : this.buttons) {
            GuiHelper.setupDrawing();
            b.button.getIcon().draw(graphics, b.x, b.y, 16, 16);
            if (b == this.mouseOver) {
                Color4I.WHITE.withAlpha(33).draw(graphics, b.x, b.y, 16, 16);
            }
            if (b.button.getCustomTextHandler() != null) {
                String text = (String) b.button.getCustomTextHandler().get();
                if (!text.isEmpty()) {
                    int nw = font.width(text);
                    int width = 16;
                    Color4I.LIGHT_RED.draw(graphics, b.x + width - nw, b.y - 1, nw + 1, 9);
                    graphics.drawString(font, text, b.x + width - nw + 1, b.y, -1);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }
        if (this.mouseOver != null) {
            GuiHelper.setupDrawing();
            int mx1 = mx + 10;
            int my1 = Math.max(3, my - 9);
            List<String> list = new ArrayList();
            list.add(I18n.get(this.mouseOver.button.getLangKey()));
            if (this.mouseOver.button.getTooltipHandler() != null) {
                this.mouseOver.button.getTooltipHandler().accept(list);
            }
            int tw = 0;
            for (String s : list) {
                tw = Math.max(tw, font.width(s));
            }
            graphics.pose().translate(0.0F, 0.0F, 500.0F);
            Color4I.DARK_GRAY.draw(graphics, mx1 - 3, my1 - 2, tw + 6, 2 + list.size() * 10);
            for (int i = 0; i < list.size(); i++) {
                graphics.drawString(font, (String) list.get(i), mx1, my1 + i * 10, -1);
            }
        }
        GuiHelper.setupDrawing();
        lastDrawnArea = new Rect2i(this.m_252754_(), this.m_252907_(), this.f_93618_, this.f_93619_);
        graphics.pose().popPose();
    }

    @Override
    public void onPress() {
        if (this.mouseOver != null) {
            this.mouseOver.button.onClicked(Screen.hasShiftDown());
        }
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.m_168802_(narrationElementOutput);
    }
}