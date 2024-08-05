package com.github.einjerjar.mc.keymap.client.gui.screen;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.client.gui.widgets.VirtualKeyboardWidget;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import com.github.einjerjar.mc.keymap.keys.sources.KeymappingNotifier;
import com.github.einjerjar.mc.keymap.utils.VKUtil;
import com.github.einjerjar.mc.widgets.EButton;
import com.github.einjerjar.mc.widgets.ELabel;
import com.github.einjerjar.mc.widgets.EScreen;
import com.github.einjerjar.mc.widgets.EWidget;
import com.github.einjerjar.mc.widgets.ValueMapList;
import com.github.einjerjar.mc.widgets.utils.Styles;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class LayoutSelectionScreen extends EScreen {

    protected List<VirtualKeyboardWidget> vks;

    protected VirtualKeyboardWidget vkBasic;

    protected VirtualKeyboardWidget vkExtra;

    protected VirtualKeyboardWidget vkMouse;

    protected VirtualKeyboardWidget vkNumpad;

    protected ValueMapList listLayouts;

    protected EButton btnSave;

    protected EButton btnCancel;

    protected EButton btnClose;

    protected ELabel lblScreenLabel;

    protected ELabel lblCreditTitle;

    protected ELabel lblCreditName;

    public LayoutSelectionScreen(Screen parent) {
        super(parent, Component.literal("Keymap Layout"));
    }

    @Override
    protected void onInit() {
        KeyLayout layout = KeyLayout.getLayoutWithCode(KeymapConfig.instance().customLayout());
        KeymappingNotifier.load();
        this.scr = this.scrFromWidth(Math.min(450, this.f_96543_));
        this.initVks(layout);
        int spaceLeft = this.scr.w() - this.padding.x() * 3 - this.vkBasic.rect().w();
        this.listLayouts = new ValueMapList(9, this.vkBasic.right() + this.padding.x(), this.vkBasic.top(), spaceLeft, this.scr.h() - this.padding.y() * 4 - 32, false);
        for (Entry<String, KeyLayout> v : KeyLayout.layouts().entrySet()) {
            this.listLayouts.addItem(new ValueMapList.ValueMapEntry<>(((KeyLayout) KeyLayout.layouts().get(v.getKey())).meta().name(), (String) v.getKey(), this.listLayouts));
        }
        this.listLayouts.onItemSelected(this::onLayoutSelected);
        this.listLayouts.setItemSelectedWithValue(KeymapConfig.instance().customLayout());
        this.btnSave = new EButton(Component.translatable("keymap.btnSave"), this.listLayouts.left(), this.listLayouts.bottom() + this.padding.y(), (this.listLayouts.rect().w() - this.padding.x()) / 2, 16);
        this.btnCancel = new EButton(Component.translatable("keymap.btnCancel"), this.listLayouts.right() - this.btnSave.rect().w(), this.btnSave.top(), this.btnSave.rect().w(), 16);
        this.btnSave.clickAction(this::onBtnSaveClicked);
        this.btnCancel.clickAction(this::onBtnCancelClicked);
        this.lblScreenLabel = new ELabel(Component.translatable("keymap.scrLayout"), this.scr.left(), this.scr.top() + this.padding.y(), this.scr.w(), 16);
        this.lblCreditTitle = new ELabel(Component.translatable("keymap.lblCredits"), this.vkNumpad.right() + this.padding.x(), this.vkNumpad.top() + this.padding.y() * 2, this.vkBasic.right() - this.vkNumpad.right() - this.padding.x(), 9);
        this.lblCreditName = new ELabel(Component.literal(this.qAuthor(layout)).withStyle(Styles.headerBold()), this.lblCreditTitle.left(), this.lblCreditTitle.bottom() + this.padding.y(), this.lblCreditTitle.rect().w(), 9);
        this.lblScreenLabel.center(true);
        this.lblCreditTitle.center(true);
        this.lblCreditName.center(true);
        this.creditVis(layout);
        this.btnClose = new EButton(Component.translatable("keymap.btnClearSearch"), this.listLayouts.right() - 16, this.scr.y() + this.padding.y(), 16, 16);
        this.btnClose.clickAction(this::onBtnCloseClicked);
        this.m_142416_(this.listLayouts);
        this.m_142416_(this.btnSave);
        this.m_142416_(this.btnCancel);
        this.m_142416_(this.lblScreenLabel);
        this.m_142416_(this.lblCreditTitle);
        this.m_142416_(this.lblCreditName);
        this.m_142416_(this.btnClose);
    }

    protected void initVks(KeyLayout layout) {
        this.vks = VKUtil.genLayout(layout, this.scr.x() + this.padding.x(), this.scr.y() + this.padding.y() * 2 + 16);
        for (VirtualKeyboardWidget vk : this.vks) {
            this.m_142416_(vk);
        }
        this.vkBasic = (VirtualKeyboardWidget) this.vks.get(0);
        this.vkExtra = (VirtualKeyboardWidget) this.vks.get(1);
        this.vkMouse = (VirtualKeyboardWidget) this.vks.get(2);
        this.vkNumpad = (VirtualKeyboardWidget) this.vks.get(3);
    }

    protected void onBtnCloseClicked(EWidget source) {
        this.onClose();
    }

    protected String qAuthor(KeyLayout layout) {
        return layout.meta().author() == null ? "" : layout.meta().author();
    }

    protected void creditVis(KeyLayout layout) {
        String qa = this.qAuthor(layout);
        this.lblCreditTitle.visible(!qa.isBlank());
        this.lblCreditName.visible(!qa.isBlank());
        this.lblCreditName.text(Component.literal(qa).withStyle(Styles.headerBold()));
    }

    protected void onBtnSaveClicked(EWidget source) {
        ValueMapList.ValueMapEntry<String> selected = (ValueMapList.ValueMapEntry<String>) this.listLayouts.itemSelected();
        if (selected == null) {
            Keymap.logger().error("Cant save empty value!!");
        } else {
            KeymapConfig.instance().customLayout(selected.value());
            KeymapConfig.save();
            this.onClose();
        }
    }

    protected void onBtnCancelClicked(EWidget source) {
        this.onClose();
    }

    protected void onLayoutSelected(EWidget source) {
        ValueMapList.ValueMapEntry<String> selected = (ValueMapList.ValueMapEntry<String>) this.listLayouts.itemSelected();
        KeyLayout layout = KeyLayout.getLayoutWithCode(selected.value());
        for (VirtualKeyboardWidget vk : this.vks) {
            this.m_169411_(vk.destroy());
        }
        KeymappingNotifier.clearSubscribers();
        this.initVks(layout);
        this.creditVis(layout);
    }

    @Override
    public void onClose() {
        KeymappingNotifier.clearSubscribers();
        if (KeymapConfig.instance().firstOpenDone()) {
            super.onClose();
        } else {
            KeymapConfig.instance().firstOpenDone(true);
            KeymapConfig.save();
            Minecraft.getInstance().setScreen(new KeymapScreen(this.parent()));
        }
    }

    @Override
    protected void preRenderScreen(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, this.f_96543_, this.f_96544_, 1426063360);
        if (this.scr != null) {
            this.drawOutline(guiGraphics, this.scr, -1);
        }
    }
}