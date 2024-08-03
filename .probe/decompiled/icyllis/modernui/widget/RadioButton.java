package icyllis.modernui.widget;

import icyllis.modernui.core.Context;

public class RadioButton extends CompoundButton {

    public RadioButton(Context context) {
        super(context);
    }

    @Override
    public void toggle() {
        if (!this.isChecked()) {
            super.toggle();
        }
    }
}