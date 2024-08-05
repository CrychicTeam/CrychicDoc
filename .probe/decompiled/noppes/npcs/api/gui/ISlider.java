package noppes.npcs.api.gui;

import noppes.npcs.api.function.gui.GuiComponentUpdate;

public interface ISlider extends ICustomGuiComponent {

    float getValue();

    ISlider setValue(float var1);

    String getFormat();

    ISlider setFormat(String var1);

    float getMin();

    ISlider setMin(float var1);

    float getMax();

    ISlider setMax(float var1);

    int getDecimals();

    ISlider setDecimals(int var1);

    ISlider setOnChange(GuiComponentUpdate<ISlider> var1);
}