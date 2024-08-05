package journeymap.client.ui.component;

import journeymap.common.properties.config.CustomField;
import net.minecraft.client.gui.Font;

public class TextFieldButton extends TextBoxButton implements IConfigFieldHolder<CustomField> {

    protected final CustomField field;

    private int WIDTH_BUFFER = 50;

    public TextFieldButton(CustomField field) {
        super(field.get().toString());
        this.field = field;
        if (field.isNumber()) {
            this.textBox = new TextBox(field.get().toString(), this.fontRenderer, this.f_93618_, this.f_93619_, field.isNumber(), field.allowNeg());
            this.textBox.setClamp(field.getMinValue(), field.getMaxValue());
        } else {
            this.textBox = new TextBox(field.get().toString(), this.fontRenderer, this.f_93618_, this.f_93619_);
        }
        this.textBox.setY(this.textBox.getY() - 1);
        this.textBox.setHeight(this.textBox.getHeight() - 4);
    }

    public void setValue(Object value) {
        if (!this.field.get().equals(value)) {
            this.field.set(value);
            this.field.save();
        }
        this.textBox.setText(value);
    }

    public void updateValue(Object value) {
        if (!this.field.get().equals(value)) {
            this.field.set(value);
            this.field.save();
        }
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        boolean press = this.textBox.charTyped(typedChar, keyCode);
        this.updateValue(this.textBox.m_94155_());
        return press;
    }

    @Override
    public boolean keyPressed(int key, int value, int modifier) {
        boolean press = this.textBox.keyPressed(key, value, modifier);
        this.updateValue(this.textBox.m_94155_());
        return press;
    }

    @Override
    public int getFitWidth(Font fr) {
        return fr.width(this.textBox.m_94155_()) + this.WIDTH_PAD + this.WIDTH_BUFFER;
    }

    public CustomField getConfigField() {
        return this.field;
    }

    @Override
    public void refresh() {
        this.setValue(this.field.get());
    }
}