package mezz.jei.api.gui.handlers;

import net.minecraft.client.gui.screens.Screen;

public interface IGuiProperties {

    Class<? extends Screen> getScreenClass();

    int getGuiLeft();

    int getGuiTop();

    int getGuiXSize();

    int getGuiYSize();

    int getScreenWidth();

    int getScreenHeight();
}