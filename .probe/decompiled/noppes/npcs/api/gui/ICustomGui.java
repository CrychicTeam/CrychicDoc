package noppes.npcs.api.gui;

import noppes.npcs.api.entity.IPlayer;

public interface ICustomGui extends IComponentsWrapper {

    int getID();

    int getWidth();

    int getHeight();

    void setSize(int var1, int var2);

    void setDoesPauseGame(boolean var1);

    void setBackgroundTexture(String var1);

    void update();

    void update(ICustomGuiComponent var1);

    IComponentsScrollableWrapper getScrollingPanel();

    void openSubGui(ICustomGui var1);

    ICustomGui getSubGui();

    boolean hasSubGui();

    ICustomGui closeSubGui();

    void close();

    ICustomGui getParentGui();

    ICustomGui getRootGui();

    ICustomGui getActiveGui();

    IPlayer getPlayer();
}