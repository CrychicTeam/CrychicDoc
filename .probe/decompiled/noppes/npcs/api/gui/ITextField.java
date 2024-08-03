package noppes.npcs.api.gui;

import noppes.npcs.api.function.gui.GuiComponentUpdate;

public interface ITextField extends ICustomGuiComponent {

    String getText();

    ITextField setText(String var1);

    int getColor();

    ITextField setColor(int var1);

    ITextField setOnChange(GuiComponentUpdate<ITextField> var1);

    ITextField setOnFocusLost(GuiComponentUpdate<ITextField> var1);

    ITextField setFocused(boolean var1);

    boolean getFocused();

    ITextField setCharacterType(int var1);

    int getCharacterType();

    int getInteger();

    ITextField setInteger(int var1);

    float getFloat();

    ITextField setFloat(float var1);

    ITextField setMinMax(int var1, int var2);
}