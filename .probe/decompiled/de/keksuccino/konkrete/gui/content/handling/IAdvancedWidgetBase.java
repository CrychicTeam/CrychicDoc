package de.keksuccino.konkrete.gui.content.handling;

import de.keksuccino.konkrete.input.CharData;
import de.keksuccino.konkrete.input.KeyboardData;

public interface IAdvancedWidgetBase {

    void onTick();

    void onKeyPress(KeyboardData var1);

    void onKeyReleased(KeyboardData var1);

    void onCharTyped(CharData var1);

    void onMouseClicked(double var1, double var3, int var5);
}