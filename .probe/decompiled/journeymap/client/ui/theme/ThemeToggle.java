package journeymap.client.ui.theme;

import journeymap.client.Constants;
import journeymap.common.properties.config.BooleanField;
import net.minecraft.client.gui.components.Button;

public class ThemeToggle extends ThemeButton {

    public ThemeToggle(Theme theme, String rawlabel, String iconName, Button.OnPress onPress) {
        super(theme, Constants.getString(rawlabel), Constants.getString(rawlabel), iconName, null, onPress);
    }

    public ThemeToggle(Theme theme, String labelOn, String labelOff, String iconName, Button.OnPress onPress) {
        super(theme, Constants.getString(labelOn), Constants.getString(labelOff), iconName, null, onPress);
    }

    public ThemeToggle(Theme theme, String rawlabel, String iconName, BooleanField field, Button.OnPress onPress) {
        super(theme, Constants.getString(rawlabel), Constants.getString(rawlabel), iconName, field, onPress);
        if (field != null) {
            this.setToggled(field.get());
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return this.toggled && this.staysOn ? false : super.m_6375_(mouseX, mouseY, mouseButton);
    }

    @Override
    protected String getPathPattern() {
        return "control/%stoggle_%s.png";
    }

    @Override
    protected Theme.Control.ButtonSpec getButtonSpec(Theme theme) {
        return theme.control.toggle;
    }
}