package noppes.npcs.api.gui;

public interface ITexturedRect extends ICustomGuiComponent {

    String getTexture();

    ITexturedRect setTexture(String var1);

    float getScale();

    ITexturedRect setScale(float var1);

    int getTextureX();

    int getTextureY();

    ITexturedRect setTextureOffset(int var1, int var2);

    ITexturedRect setRepeatingTexture(int var1, int var2, int var3);
}