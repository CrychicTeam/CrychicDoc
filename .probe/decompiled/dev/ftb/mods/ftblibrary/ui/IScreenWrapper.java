package dev.ftb.mods.ftblibrary.ui;

public interface IScreenWrapper extends IOpenableScreen {

    BaseScreen getGui();

    @Override
    default void openGui() {
        this.getGui().openGui();
    }

    @Override
    default void closeGui(boolean openPrevScreen) {
        this.getGui().closeGui(openPrevScreen);
    }
}