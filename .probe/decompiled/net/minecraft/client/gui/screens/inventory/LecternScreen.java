package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.LecternMenu;
import net.minecraft.world.item.ItemStack;

public class LecternScreen extends BookViewScreen implements MenuAccess<LecternMenu> {

    private final LecternMenu menu;

    private final ContainerListener listener = new ContainerListener() {

        @Override
        public void slotChanged(AbstractContainerMenu p_99054_, int p_99055_, ItemStack p_99056_) {
            LecternScreen.this.bookChanged();
        }

        @Override
        public void dataChanged(AbstractContainerMenu p_169772_, int p_169773_, int p_169774_) {
            if (p_169773_ == 0) {
                LecternScreen.this.pageChanged();
            }
        }
    };

    public LecternScreen(LecternMenu lecternMenu0, Inventory inventory1, Component component2) {
        this.menu = lecternMenu0;
    }

    public LecternMenu getMenu() {
        return this.menu;
    }

    @Override
    protected void init() {
        super.init();
        this.menu.m_38893_(this.listener);
    }

    @Override
    public void onClose() {
        this.f_96541_.player.closeContainer();
        super.m_7379_();
    }

    @Override
    public void removed() {
        super.m_7861_();
        this.menu.m_38943_(this.listener);
    }

    @Override
    protected void createMenuControls() {
        if (this.f_96541_.player.m_36326_()) {
            this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_99033_ -> this.onClose()).bounds(this.f_96543_ / 2 - 100, 196, 98, 20).build());
            this.m_142416_(Button.builder(Component.translatable("lectern.take_book"), p_99024_ -> this.sendButtonClick(3)).bounds(this.f_96543_ / 2 + 2, 196, 98, 20).build());
        } else {
            super.createMenuControls();
        }
    }

    @Override
    protected void pageBack() {
        this.sendButtonClick(1);
    }

    @Override
    protected void pageForward() {
        this.sendButtonClick(2);
    }

    @Override
    protected boolean forcePage(int int0) {
        if (int0 != this.menu.getPage()) {
            this.sendButtonClick(100 + int0);
            return true;
        } else {
            return false;
        }
    }

    private void sendButtonClick(int int0) {
        this.f_96541_.gameMode.handleInventoryButtonClick(this.menu.f_38840_, int0);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    void bookChanged() {
        ItemStack $$0 = this.menu.getBook();
        this.m_98288_(BookViewScreen.BookAccess.fromItem($$0));
    }

    void pageChanged() {
        this.m_98275_(this.menu.getPage());
    }

    @Override
    protected void closeScreen() {
        this.f_96541_.player.closeContainer();
    }
}