package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.PointerIcon;
import javax.annotation.Nonnull;

public class ImageButton extends ImageView {

    public ImageButton(Context context) {
        super(context);
        this.setFocusable(true);
        this.setClickable(true);
    }

    @Override
    public PointerIcon onResolvePointerIcon(@Nonnull MotionEvent event) {
        return this.isClickable() && this.isEnabled() ? PointerIcon.getSystemIcon(1002) : super.onResolvePointerIcon(event);
    }
}