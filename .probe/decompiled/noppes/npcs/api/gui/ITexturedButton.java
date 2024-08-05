package noppes.npcs.api.gui;

public interface ITexturedButton extends IButton {

    @Override
    String getTexture();

    ITexturedButton setTexture(String var1);

    @Override
    int getTextureX();

    @Override
    int getTextureY();

    ITexturedButton setTextureOffset(int var1, int var2);
}