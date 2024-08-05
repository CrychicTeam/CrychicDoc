package dev.xkmc.l2artifacts.compat;

import com.google.common.base.Preconditions;
import mezz.jei.api.gui.handlers.IGuiProperties;
import net.minecraft.client.gui.screens.Screen;

public class GuiProperties implements IGuiProperties {

    private final Class<? extends Screen> screenClass;

    private final int guiLeft;

    private final int guiTop;

    private final int guiXSize;

    private final int guiYSize;

    private final int screenWidth;

    private final int screenHeight;

    public GuiProperties(Class<? extends Screen> screenClass, int guiLeft, int guiTop, int guiXSize, int guiYSize, int screenWidth, int screenHeight) {
        Preconditions.checkArgument(guiLeft >= 0, "guiLeft must be >= 0");
        Preconditions.checkArgument(guiTop >= 0, "guiTop must be >= 0");
        Preconditions.checkArgument(guiXSize > 0, "guiXSize must be > 0");
        Preconditions.checkArgument(guiYSize > 0, "guiYSize must be > 0");
        Preconditions.checkArgument(screenWidth > 0, "screenWidth must be > 0");
        Preconditions.checkArgument(screenHeight > 0, "screenHeight must be > 0");
        this.screenClass = screenClass;
        this.guiLeft = guiLeft;
        this.guiTop = guiTop;
        this.guiXSize = guiXSize;
        this.guiYSize = guiYSize;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @Override
    public Class<? extends Screen> getScreenClass() {
        return this.screenClass;
    }

    @Override
    public int getGuiLeft() {
        return this.guiLeft;
    }

    @Override
    public int getGuiTop() {
        return this.guiTop;
    }

    @Override
    public int getGuiXSize() {
        return this.guiXSize;
    }

    @Override
    public int getGuiYSize() {
        return this.guiYSize;
    }

    @Override
    public int getScreenWidth() {
        return this.screenWidth;
    }

    @Override
    public int getScreenHeight() {
        return this.screenHeight;
    }
}