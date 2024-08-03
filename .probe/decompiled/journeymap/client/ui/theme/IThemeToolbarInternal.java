package journeymap.client.ui.theme;

import journeymap.client.api.display.IThemeToolBar;

public interface IThemeToolbarInternal extends IThemeToolBar {

    @Deprecated
    @Override
    default int getHeight() {
        return this.getToolbarHeight();
    }

    int getToolbarHeight();

    @Deprecated
    @Override
    default int getWidth() {
        return this.getToolbarWidth();
    }

    int getToolbarWidth();

    @Deprecated
    @Override
    default int getX() {
        return this.getToolbarX();
    }

    int getToolbarX();

    @Deprecated
    @Override
    default int getY() {
        return this.getToolbarY();
    }

    int getToolbarY();

    @Deprecated
    @Override
    default void setPosition(int x, int y) {
        this.setToolbarPosition(x, y);
    }

    void setToolbarPosition(int var1, int var2);
}