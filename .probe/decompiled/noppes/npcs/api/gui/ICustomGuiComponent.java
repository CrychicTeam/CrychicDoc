package noppes.npcs.api.gui;

import java.util.UUID;

public interface ICustomGuiComponent {

    int getID();

    ICustomGuiComponent setID(int var1);

    UUID getUniqueID();

    int getPosX();

    int getPosY();

    ICustomGuiComponent setPos(int var1, int var2);

    int getWidth();

    int getHeight();

    ICustomGuiComponent setSize(int var1, int var2);

    boolean hasHoverText();

    String[] getHoverText();

    ICustomGuiComponent setHoverText(String var1);

    ICustomGuiComponent setHoverText(String[] var1);

    boolean getEnabled();

    ICustomGuiComponent setEnabled(boolean var1);

    boolean getVisible();

    ICustomGuiComponent setVisible(boolean var1);

    int getType();
}