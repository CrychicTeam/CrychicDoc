package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.PointerIcon;
import javax.annotation.Nonnull;

public class Button extends TextView {

    public Button(Context context) {
        super(context);
        this.setFocusable(true);
        this.setClickable(true);
        this.setGravity(17);
    }

    @Override
    public PointerIcon onResolvePointerIcon(@Nonnull MotionEvent event) {
        return this.isClickable() && this.isEnabled() ? PointerIcon.getSystemIcon(1002) : super.onResolvePointerIcon(event);
    }
}