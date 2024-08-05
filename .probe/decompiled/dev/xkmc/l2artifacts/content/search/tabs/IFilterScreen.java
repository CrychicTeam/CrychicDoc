package dev.xkmc.l2artifacts.content.search.tabs;

import net.minecraft.client.gui.screens.Screen;

public interface IFilterScreen {

    int getGuiLeft();

    int getGuiTop();

    int screenWidth();

    int screenHeight();

    int getXSize();

    int getYSize();

    default Screen asScreen() {
        return (Screen) this;
    }

    default void onSwitch() {
    }
}