package com.mna.gui.base;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class SearchableGuiJeiDisable<T extends AbstractContainerMenu> extends GuiJEIDisable<T> {

    protected String currentSearchTerm = null;

    protected EditBox searchBox;

    protected Component searchValue;

    public SearchableGuiJeiDisable(T container, Inventory playerInv, Component title) {
        super(container, playerInv, title);
    }

    protected void initSearch(int searchX, int searchY) {
        this.initSearch(searchX, searchY, 130, 16);
    }

    protected void initSearch(int searchX, int searchY, int width, int height) {
        this.searchBox = new EditBox(this.f_96541_.font, searchX, searchY, width, height, this.searchValue);
        this.searchBox.setMaxLength(60);
        this.searchBox.setResponder(this::searchTermChanged);
        this.searchBox.setCanLoseFocus(false);
        this.searchBox.setFocused(true);
        this.m_7522_(this.searchBox);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        super.m_88315_(pGuiGraphics, p_230430_2_, p_230430_3_, p_230430_4_);
        this.searchBox.m_88315_(pGuiGraphics, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    protected abstract void searchTermChanged(String var1);

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.m_6375_(mouseX, mouseY, button);
        this.m_7522_(this.searchBox);
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.m_7379_();
            return true;
        } else if (this.searchBox.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            return this.searchBox.m_93696_() && this.searchBox.isVisible() && keyCode != 256 ? true : super.m_7933_(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        return this.m_7222_() != null && this.m_7222_().charTyped(pCodePoint, pModifiers);
    }
}