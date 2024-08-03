package dev.ftb.mods.ftblibrary.sidebar;

public class SidebarGuiButton {

    public final int buttonX;

    public final int buttonY;

    public final SidebarButton button;

    public int x;

    public int y;

    public SidebarGuiButton(int x, int y, SidebarButton b) {
        this.buttonX = x;
        this.buttonY = y;
        this.button = b;
    }
}