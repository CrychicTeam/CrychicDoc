package journeymap.client.ui.component;

import journeymap.common.properties.config.BooleanField;

public class BooleanPropertyButton extends OnOffButton implements IConfigFieldHolder<BooleanField> {

    final BooleanField booleanField;

    public BooleanPropertyButton(String labelOn, String labelOff, BooleanField field, net.minecraft.client.gui.components.Button.OnPress onPress) {
        super(labelOn, labelOff, field != null && field.get(), onPress);
        this.booleanField = field;
    }

    public BooleanField getField() {
        return this.booleanField;
    }

    @Override
    public void toggle() {
        if (this.isEnabled()) {
            if (this.booleanField != null) {
                this.setToggled(Boolean.valueOf(this.booleanField.toggleAndSave()));
            } else {
                this.setToggled(Boolean.valueOf(!this.toggled));
            }
        }
    }

    @Override
    public void refresh() {
        if (this.booleanField != null) {
            this.setToggled(this.booleanField.get());
        }
    }

    public void setValue(Boolean value) {
        if (this.booleanField == null) {
            this.toggled = value;
        } else {
            this.booleanField.set(value);
            this.booleanField.save();
        }
    }

    public BooleanField getConfigField() {
        return this.booleanField;
    }
}