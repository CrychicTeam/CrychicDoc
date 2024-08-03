package com.simibubi.create.foundation.ponder.ui;

import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import com.simibubi.create.foundation.gui.widget.ElementWidget;
import com.simibubi.create.foundation.ponder.PonderChapter;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class ChapterLabel extends AbstractSimiWidget {

    private final PonderChapter chapter;

    private final PonderButton button;

    public ChapterLabel(PonderChapter chapter, int x, int y, BiConsumer<Integer, Integer> onClick) {
        super(x, y, 175, 38);
        this.button = new PonderButton(x + 4, y + 4, 30, 30).<ElementWidget>showing(chapter).withCallback(onClick);
        this.chapter = chapter;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        UIRenderHelper.streak(graphics, 0.0F, this.m_252754_(), this.m_252907_() + this.f_93619_ / 2, this.f_93619_ - 2, this.f_93618_);
        graphics.drawString(Minecraft.getInstance().font, this.chapter.getTitle(), this.m_252754_() + 50, this.m_252907_() + 20, Theme.i(Theme.Key.TEXT_ACCENT_SLIGHT), false);
        this.button.doRender(graphics, mouseX, mouseY, partialTicks);
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClick(double x, double y) {
        if (this.button.m_5953_(x, y)) {
            this.button.runCallback(x, y);
        }
    }
}