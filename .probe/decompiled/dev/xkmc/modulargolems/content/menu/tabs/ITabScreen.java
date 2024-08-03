package dev.xkmc.modulargolems.content.menu.tabs;

import net.minecraft.client.gui.screens.Screen;

public interface ITabScreen {

    int getGuiLeft();

    int getGuiTop();

    int screenWidth();

    int screenHeight();

    int getXSize();

    int getYSize();

    default Screen asScreen() {
        return (Screen) this;
    }
}