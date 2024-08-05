package noppes.npcs.api.gui;

import noppes.npcs.api.function.gui.GuiComponentClicked;
import noppes.npcs.api.item.IItemStack;

public interface IButton extends ICustomGuiComponent {

    String getLabel();

    IButton setLabel(String var1);

    ITexturedRect getTextureRect();

    void setTextureRect(ITexturedRect var1);

    @Deprecated
    String getTexture();

    @Deprecated
    boolean hasTexture();

    @Deprecated
    IButton setTexture(String var1);

    @Deprecated
    int getTextureX();

    @Deprecated
    int getTextureY();

    @Deprecated
    IButton setTextureOffset(int var1, int var2);

    int getTextureHoverOffset();

    IButton setTextureHoverOffset(int var1);

    IItemStack getDisplayItem();

    IButton setDisplayItem(IItemStack var1);

    IButton setOnPress(GuiComponentClicked<IButton> var1);
}