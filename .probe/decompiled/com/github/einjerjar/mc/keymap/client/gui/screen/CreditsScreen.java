package com.github.einjerjar.mc.keymap.client.gui.screen;

import com.github.einjerjar.mc.keymap.objects.Credits;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.widgets.EButton;
import com.github.einjerjar.mc.widgets.ELabel;
import com.github.einjerjar.mc.widgets.EScreen;
import com.github.einjerjar.mc.widgets.EWidget;
import com.github.einjerjar.mc.widgets.ScrollTextList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class CreditsScreen extends EScreen {

    protected ScrollTextList listHelp;

    protected ELabel lblTitle;

    protected EButton btnClose;

    protected CreditsScreen(Screen parent) {
        super(parent, Component.translatable("keymap.scrCredits"));
    }

    @Override
    protected void onInit() {
        this.scr = this.scrFromWidth(Math.min(450, this.f_96543_));
        this.lblTitle = new ELabel(this.scr.left() + this.padding.x(), this.scr.top() + this.padding.y(), this.scr.w() - this.padding.x() * 2, 16);
        this.lblTitle.text(Component.translatable("keymap.lblCredits"));
        this.lblTitle.center(true);
        StringBuilder layoutCredits = new StringBuilder(Utils.translate("keymap.lblCreditsLayout"));
        StringBuilder languageCredits = new StringBuilder(Utils.translate("keymap.lblCreditsLanguage"));
        StringBuilder coreCredits = new StringBuilder(Utils.translate("keymap.lblCreditsCore"));
        for (Credits.LayoutCredits lay : Credits.instance().layout()) {
            layoutCredits.append("\n\n- ").append(lay.key());
            for (String name : lay.name()) {
                layoutCredits.append("\n   - ").append(name);
            }
        }
        for (Credits.LanguageCredits lang : Credits.instance().language()) {
            languageCredits.append("\n\n- ").append(lang.lang());
            for (String name : lang.name()) {
                languageCredits.append("\n   - ").append(name);
            }
        }
        for (Credits.CoreCredits contrib : Credits.instance().core()) {
            coreCredits.append("\n\n- ").append(contrib.name());
            for (String c : contrib.contributions()) {
                coreCredits.append("\n   - ").append(c);
            }
        }
        this.listHelp = ScrollTextList.createFromString(coreCredits + "\n\n\n" + layoutCredits + "\n\n\n" + languageCredits, this.lblTitle.left(), this.lblTitle.bottom() + this.padding.y(), this.lblTitle.rect().w(), this.scr.h() - this.padding.y() * 3 - 16);
        this.btnClose = new EButton(Component.translatable("keymap.btnClearSearch"), this.listHelp.right() - 16, this.scr.y() + this.padding.y(), 16, 16);
        this.btnClose.clickAction(this::onBtnCloseClicked);
        this.m_142416_(this.lblTitle);
        this.m_142416_(this.listHelp);
        this.m_142416_(this.btnClose);
    }

    protected void onBtnCloseClicked(EWidget source) {
        this.m_7379_();
    }

    @Override
    protected void preRenderScreen(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, this.f_96543_, this.f_96544_, 1426063360);
        if (this.scr != null) {
            this.drawOutline(guiGraphics, this.scr, -1);
        }
    }
}