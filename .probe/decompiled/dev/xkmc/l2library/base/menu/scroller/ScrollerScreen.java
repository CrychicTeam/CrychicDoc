package dev.xkmc.l2library.base.menu.scroller;

public interface ScrollerScreen {

    ScrollerMenu getMenu();

    int getGuiLeft();

    int getGuiTop();

    void scrollTo(int var1);
}