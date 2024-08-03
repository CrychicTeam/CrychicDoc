package com.github.einjerjar.mc.keymap.client.gui.screen;

import com.github.einjerjar.mc.keymap.client.gui.widgets.CategoryListWidget;
import com.github.einjerjar.mc.keymap.client.gui.widgets.KeyWidget;
import com.github.einjerjar.mc.keymap.client.gui.widgets.KeymapListWidget;
import com.github.einjerjar.mc.keymap.client.gui.widgets.VirtualKeyboardWidget;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.KeyType;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeymapRegistry;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import com.github.einjerjar.mc.keymap.keys.sources.KeymappingNotifier;
import com.github.einjerjar.mc.keymap.keys.sources.category.CategorySource;
import com.github.einjerjar.mc.keymap.keys.sources.category.CategorySources;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSource;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSources;
import com.github.einjerjar.mc.keymap.keys.wrappers.categories.CategoryHolder;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;
import com.github.einjerjar.mc.keymap.utils.VKUtil;
import com.github.einjerjar.mc.widgets.EButton;
import com.github.einjerjar.mc.widgets.EInput;
import com.github.einjerjar.mc.widgets.EScreen;
import com.github.einjerjar.mc.widgets.EWidget;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class KeymapScreen extends EScreen {

    protected int lastKeyCode;

    protected KeyComboData lastKeyComboData;

    protected VirtualKeyboardWidget vkBasic;

    protected VirtualKeyboardWidget vkExtra;

    protected VirtualKeyboardWidget vkMouse;

    protected VirtualKeyboardWidget vkNumpad;

    protected KeymapListWidget listKm;

    protected CategoryListWidget listCat;

    protected EButton btnReset;

    protected EButton btnResetAll;

    protected EButton btnClearSearch;

    protected EButton btnOpenSettings;

    protected EButton btnOpenLayouts;

    protected EButton btnOpenHelp;

    protected EButton btnOpenCredits;

    protected EInput inpSearch;

    protected List<VirtualKeyboardWidget> vks;

    public KeymapScreen(Screen parent) {
        super(parent, Component.translatable("keymap.scrMain"));
    }

    @Override
    protected void onInit() {
        KeyLayout layout = KeyLayout.getLayoutWithCode(KeymapConfig.instance().customLayout());
        KeymappingNotifier.load();
        this.scr = this.scrFromWidth(Math.min(450, this.f_96543_));
        this.initVks(layout);
        int spaceLeft = this.scr.w() - this.padding.x() * 3 - this.vkBasic.rect().w();
        this.listKm = new KeymapListWidget(9, this.vkBasic.right() + this.padding.x(), this.vkBasic.top(), spaceLeft, this.scr.h() - this.padding.y() * 4 - 32);
        this.listCat = new CategoryListWidget(9, this.vkNumpad.right() + this.padding.x(), this.vkNumpad.top(), this.vkBasic.right() - this.vkNumpad.right() - this.padding.x(), this.vkNumpad.rect().h());
        this.listKm.onItemSelected(this::onListKmSelected);
        this.listKm.onItemSelected(this::onListKmSelected);
        this.listCat.onItemSelected(this::onListCatSelected);
        this.inpSearch = new EInput(this.listKm.left(), this.scr.y() + this.padding.y(), this.listKm.rect().w() - 16 - this.padding.x(), 16);
        this.inpSearch.onChanged(this::onSearchChanged);
        this.btnReset = new EButton(Component.translatable("keymap.btnReset"), this.listKm.left(), this.listKm.bottom() + this.padding.y(), (this.listKm.rect().w() - this.padding.x()) / 2, 16);
        this.btnResetAll = new EButton(Component.translatable("keymap.btnResetAll"), this.btnReset.right() + this.padding.x(), this.listKm.bottom() + this.padding.y(), (this.listKm.rect().w() - this.padding.x()) / 2, 16);
        int vkSplit = (this.vkBasic.rect().w() - this.padding.x()) / 4;
        this.btnOpenSettings = new EButton(Component.translatable("keymap.btnOpenSettings"), this.scr.x() + this.padding.x(), this.scr.y() + this.padding.y(), vkSplit, 16);
        this.btnOpenLayouts = new EButton(Component.translatable("keymap.btnOpenLayouts"), this.btnOpenSettings.right() + this.padding.x(), this.scr.y() + this.padding.y(), vkSplit, 16);
        this.btnOpenCredits = new EButton(Component.translatable("keymap.btnOpenCredits"), this.btnOpenLayouts.right() + this.padding.x(), this.scr.y() + this.padding.y(), vkSplit, 16);
        this.btnOpenHelp = new EButton(Component.translatable("keymap.btnOpenHelp"), this.btnOpenCredits.right() + this.padding.x(), this.scr.y() + this.padding.y(), this.vkBasic.right() - this.btnOpenCredits.right() - this.padding.x(), 16);
        this.btnClearSearch = new EButton(Component.translatable("keymap.btnClearSearch"), this.listKm.right() - 16, this.scr.y() + this.padding.y(), 16, 16);
        if (KeymapConfig.instance().showHelpTooltips()) {
            this.btnReset.setTooltip(Component.translatable("keymap.btnResetTip"));
            this.btnResetAll.setTooltip(Component.translatable("keymap.btnResetAllTip"));
            this.btnOpenSettings.setTooltip(Component.translatable("keymap.btnOpenSettingsTip"));
            this.btnOpenLayouts.setTooltip(Component.translatable("keymap.btnOpenLayoutsTip"));
            this.btnOpenCredits.setTooltip(Component.translatable("keymap.btnOpenCreditsTip"));
            this.btnOpenHelp.setTooltip(Component.translatable("keymap.btnOpenHelpTip"));
            this.btnClearSearch.setTooltip(Component.translatable("keymap.btnClearSearchTip2"));
        }
        this.btnReset.clickAction(this::onBtnResetClicked);
        this.btnResetAll.clickAction(this::onBtnResetAllClicked);
        this.btnOpenSettings.clickAction(this::onBtnOpenSettingsClicked);
        this.btnOpenLayouts.clickAction(this::onBtnOpenLayoutsClicked);
        this.btnOpenCredits.clickAction(this::onBtnOpenCreditsClicked);
        this.btnOpenHelp.clickAction(this::onBtnOpenHelpClicked);
        this.btnClearSearch.clickAction(this::onBtnClearSearchClicked);
        assert this.f_96541_ != null;
        for (KeymapSource source : KeymapSources.sources()) {
            if (source.canUseSource()) {
                for (KeyHolder kh : source.getKeyHolders()) {
                    this.listKm.addItem(new KeymapListWidget.KeymapListEntry(kh, this.listKm));
                }
            }
        }
        for (CategorySource sourcex : CategorySources.sources()) {
            if (sourcex.canUseSource()) {
                for (CategoryHolder categoryHolder : sourcex.getCategoryHolders()) {
                    this.listCat.addItem(new CategoryListWidget.CategoryListEntry(categoryHolder, this.listCat));
                }
            }
        }
        this.listKm.updateFilteredList();
        this.listKm.sort();
        this.m_142416_(this.listKm);
        this.m_142416_(this.listCat);
        this.m_142416_(this.btnReset);
        this.m_142416_(this.btnResetAll);
        this.m_142416_(this.btnOpenSettings);
        this.m_142416_(this.btnOpenLayouts);
        this.m_142416_(this.btnOpenCredits);
        this.m_142416_(this.btnOpenHelp);
        this.m_142416_(this.btnClearSearch);
        this.m_142416_(this.inpSearch);
    }

    protected void initVks(KeyLayout layout) {
        this.vks = VKUtil.genLayout(layout, this.scr.x() + this.padding.x(), this.scr.y() + this.padding.y() * 2 + 16, this::onVKKeyClicked, this::onVKSpecialClicked);
        for (VirtualKeyboardWidget vk : this.vks) {
            for (KeyWidget k : vk.childKeys()) {
                this.m_142416_(k);
            }
        }
        this.vkBasic = (VirtualKeyboardWidget) this.vks.get(0);
        this.vkExtra = (VirtualKeyboardWidget) this.vks.get(1);
        this.vkMouse = (VirtualKeyboardWidget) this.vks.get(2);
        this.vkNumpad = (VirtualKeyboardWidget) this.vks.get(3);
    }

    @Override
    public void onClose() {
        KeymappingNotifier.clearSubscribers();
        super.onClose();
    }

    protected void onBtnOpenCreditsClicked(EWidget eWidget) {
        assert this.f_96541_ != null;
        this.f_96541_.setScreen(new CreditsScreen(this));
    }

    protected void onBtnOpenHelpClicked(EWidget source) {
        assert this.f_96541_ != null;
        this.f_96541_.setScreen(new HelpScreen(this));
    }

    protected void onBtnClearSearchClicked(EWidget source) {
        if (this.inpSearch.text().isEmpty()) {
            this.onClose();
        } else {
            this.inpSearch.text("");
            this.m_7522_(null);
        }
    }

    protected void onListCatSelected(EWidget source) {
        this.inpSearch.text(this.listCat.itemSelected().category().getFilterSlug());
        EWidget f = (EWidget) this.m_7222_();
        if (f != null) {
            f.focused(false);
        }
        this.m_7522_(this.inpSearch);
        this.inpSearch.focused(true);
        this.listKm.setItemSelected(null);
    }

    protected void onSearchChanged(EInput source, String newText) {
        this.listKm.filterString(newText);
        if (newText.isEmpty()) {
            this.btnClearSearch.setTooltip(Component.translatable("keymap.btnClearSearchTip2"));
        } else {
            this.btnClearSearch.setTooltip(Component.translatable("keymap.btnClearSearchTip"));
        }
        this.listKm.setItemSelected(null);
    }

    protected void onBtnOpenSettingsClicked(EWidget source) {
        assert this.f_96541_ != null;
        this.f_96541_.setScreen(new ConfigScreen(this));
    }

    protected void onBtnOpenLayoutsClicked(EWidget source) {
        assert this.f_96541_ != null;
        this.f_96541_.setScreen(new LayoutSelectionScreen(this));
    }

    protected void onBtnResetClicked(EWidget source) {
        this.listKm.resetKey();
        this.m_7522_(null);
    }

    protected void onBtnResetAllClicked(EWidget source) {
        this.listKm.resetAllKeys();
        this.m_7522_(null);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        EWidget focus = (EWidget) this.m_7222_();
        if (focus == null && keyCode == 70 && m_96637_()) {
            this.m_7522_(this.inpSearch);
            this.inpSearch.focused(true);
            return true;
        } else if (focus != null && focus.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (focus != null && keyCode == 256 && focus != this.listKm) {
            focus.focused(false);
            this.m_7522_(null);
            return true;
        } else if (keyCode == 256 && focus == null) {
            this.onClose();
            return true;
        } else {
            KeyComboData kd = new KeyComboData(keyCode, KeyType.KEYBOARD, m_96639_(), m_96638_(), m_96637_());
            this.lastKeyCode = keyCode;
            this.lastKeyComboData = kd;
            return !KeymapRegistry.MODIFIER_KEYS().contains(keyCode);
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (this.m_7222_() != this.listKm) {
            return super.m_7920_(keyCode, scanCode, modifiers);
        } else if (this.lastKeyComboData == null) {
            return false;
        } else if (KeymapRegistry.MODIFIER_KEYS().contains(keyCode) && this.lastKeyCode != keyCode) {
            return false;
        } else {
            this.listKm.setKey(this.lastKeyComboData);
            this.m_7522_(null);
            return super.m_7920_(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        if (this.m_7222_() == this.listKm && this.hoveredWidget != this.listKm) {
            this.listKm.setItemSelected(null);
        }
        return false;
    }

    private void onVKKeyClicked(VirtualKeyboardWidget source) {
        if (source.lastActionFrom() != null) {
            KeyComboData kd = new KeyComboData(source.lastActionFrom().key().code(), source.lastActionFrom().key().mouse() ? KeyType.MOUSE : KeyType.KEYBOARD, false, false, false);
            if (!this.listKm.setKey(kd)) {
                this.inpSearch.text(String.format("[%s]", source.lastActionFrom().mcKey().getDisplayName().getString().toLowerCase()));
            }
        }
    }

    private void onVKSpecialClicked(VirtualKeyboardWidget source, KeyWidget keySource, int button) {
        if (keySource != null) {
            if (keySource.key().code() == -2) {
                KeyComboData kd = new KeyComboData(button, KeyType.MOUSE, false, false, false);
                this.listKm.setKey(kd);
            }
        }
    }

    private void onListKmSelected(EWidget source) {
    }

    @Override
    protected void preRenderScreen(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, this.f_96543_, this.f_96544_, 1426063360);
        if (this.scr != null) {
            this.drawOutline(guiGraphics, this.scr, -1);
        }
    }
}