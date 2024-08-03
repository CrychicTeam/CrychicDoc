package noppes.npcs.api.gui;

public interface IButtonList extends IButton {

    IButtonList setValues(String... var1);

    String[] getValues();

    IButtonList setSelected(int var1);

    int getSelected();

    ITexturedRect getLeftTexture();

    ITexturedRect getRightTexture();
}