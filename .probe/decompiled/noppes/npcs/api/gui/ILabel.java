package noppes.npcs.api.gui;

public interface ILabel extends ICustomGuiComponent {

    String getText();

    ILabel setText(String var1);

    int getColor();

    ILabel setColor(int var1);

    float getScale();

    ILabel setScale(float var1);

    boolean getCentered();

    ILabel setCentered(boolean var1);
}