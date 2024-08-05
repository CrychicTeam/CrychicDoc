package icyllis.modernui.text.method;

import icyllis.modernui.text.Layout;
import icyllis.modernui.text.Spannable;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.widget.TextView;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ScrollingMovementMethod extends BaseMovementMethod {

    private static final ScrollingMovementMethod sInstance = new ScrollingMovementMethod();

    public static MovementMethod getInstance() {
        return sInstance;
    }

    @Override
    protected boolean left(TextView widget, Spannable buffer) {
        return this.scrollLeft(widget, 1);
    }

    @Override
    protected boolean right(TextView widget, Spannable buffer) {
        return this.scrollRight(widget, 1);
    }

    @Override
    protected boolean up(TextView widget, Spannable buffer) {
        return this.scrollUp(widget, 1);
    }

    @Override
    protected boolean down(TextView widget, Spannable buffer) {
        return this.scrollDown(widget, 1);
    }

    @Override
    protected boolean pageUp(TextView widget, Spannable buffer) {
        return this.scrollPageUp(widget);
    }

    @Override
    protected boolean pageDown(TextView widget, Spannable buffer) {
        return this.scrollPageDown(widget);
    }

    @Override
    protected boolean top(TextView widget, Spannable buffer) {
        return this.scrollTop(widget);
    }

    @Override
    protected boolean bottom(TextView widget, Spannable buffer) {
        return this.scrollBottom(widget);
    }

    @Override
    protected boolean lineStart(TextView widget, Spannable buffer) {
        return this.scrollLineStart(widget);
    }

    @Override
    protected boolean lineEnd(TextView widget, Spannable buffer) {
        return this.scrollLineEnd(widget);
    }

    @Override
    protected boolean home(TextView widget, Spannable buffer) {
        return this.top(widget, buffer);
    }

    @Override
    protected boolean end(TextView widget, Spannable buffer) {
        return this.bottom(widget, buffer);
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        return Touch.onTouchEvent(widget, buffer, event);
    }

    @Override
    public void onTakeFocus(TextView widget, Spannable text, int dir) {
        Layout layout = widget.getLayout();
        if (layout != null && (dir & 2) != 0) {
            widget.scrollTo(widget.getScrollX(), layout.getLineTop(0));
        }
        if (layout != null && (dir & 1) != 0) {
            int padding = widget.getTotalPaddingTop() + widget.getTotalPaddingBottom();
            int line = layout.getLineCount() - 1;
            widget.scrollTo(widget.getScrollX(), layout.getLineTop(line + 1) - (widget.getHeight() - padding));
        }
    }
}