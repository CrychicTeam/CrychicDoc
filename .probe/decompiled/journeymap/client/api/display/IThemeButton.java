package journeymap.client.api.display;

import net.minecraft.client.gui.components.Button;

public interface IThemeButton {

    void setToggled(Boolean var1);

    Boolean getToggled();

    boolean isActive();

    void toggle();

    void setLabels(String var1, String var2);

    Button getButton();

    void setDrawButton(boolean var1);

    void setStaysOn(boolean var1);

    void setEnabled(boolean var1);

    void setTooltip(String... var1);

    public interface Action {

        void doAction(IThemeButton var1);
    }
}