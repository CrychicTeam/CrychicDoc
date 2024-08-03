package icyllis.modernui.widget;

import icyllis.modernui.view.View;

public interface Checkable {

    void setChecked(boolean var1);

    boolean isChecked();

    void toggle();

    @FunctionalInterface
    public interface OnCheckedChangeListener {

        void onCheckedChanged(View var1, boolean var2);
    }
}