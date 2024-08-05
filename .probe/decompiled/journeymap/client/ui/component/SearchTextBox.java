package journeymap.client.ui.component;

import net.minecraft.client.gui.Font;

public class SearchTextBox extends TextBoxButton {

    private final String initialValue;

    public SearchTextBox(String value, Font fontRenderer, int width, int height) {
        super(value, fontRenderer, width, height, true, true);
        this.initialValue = value;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        boolean clicked = super.mouseClicked(mouseX, mouseY, mouseButton);
        if (clicked && this.initialValue.contains(this.textBox.m_94155_())) {
            this.textBox.selectAll();
        }
        return clicked;
    }
}