package icyllis.modernui.text.method;

import icyllis.modernui.text.Layout;
import icyllis.modernui.text.NoCopySpan;
import icyllis.modernui.text.Selection;
import icyllis.modernui.text.Spannable;
import icyllis.modernui.text.style.ClickableSpan;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.widget.TextView;
import java.util.List;

public class LinkMovementMethod extends ScrollingMovementMethod {

    private static final int CLICK = 1;

    private static final int UP = 2;

    private static final int DOWN = 3;

    private static final int HIDE_FLOATING_TOOLBAR_DELAY_MS = 200;

    private static LinkMovementMethod sInstance;

    private static final Object FROM_BELOW = new NoCopySpan.Concrete();

    public static MovementMethod getInstance() {
        if (sInstance == null) {
            sInstance = new LinkMovementMethod();
        }
        return sInstance;
    }

    @Override
    public boolean canSelectArbitrarily() {
        return true;
    }

    @Override
    protected boolean up(TextView widget, Spannable buffer) {
        return this.action(2, widget, buffer) ? true : super.up(widget, buffer);
    }

    @Override
    protected boolean down(TextView widget, Spannable buffer) {
        return this.action(3, widget, buffer) ? true : super.down(widget, buffer);
    }

    @Override
    protected boolean left(TextView widget, Spannable buffer) {
        return this.action(2, widget, buffer) ? true : super.left(widget, buffer);
    }

    @Override
    protected boolean right(TextView widget, Spannable buffer) {
        return this.action(3, widget, buffer) ? true : super.right(widget, buffer);
    }

    private boolean action(int what, TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        int padding = widget.getTotalPaddingTop() + widget.getTotalPaddingBottom();
        int areaTop = widget.getScrollY();
        int areaBot = areaTop + widget.getHeight() - padding;
        int lineTop = layout.getLineForVertical(areaTop);
        int lineBot = layout.getLineForVertical(areaBot);
        int first = layout.getLineStart(lineTop);
        int last = layout.getLineEnd(lineBot);
        List<ClickableSpan> candidates = buffer.getSpans(first, last, ClickableSpan.class);
        int a = Selection.getSelectionStart(buffer);
        int b = Selection.getSelectionEnd(buffer);
        int selStart = Math.min(a, b);
        int selEnd = Math.max(a, b);
        if (selStart < 0 && buffer.getSpanStart(FROM_BELOW) >= 0) {
            selStart = selEnd = buffer.length();
        }
        if (selStart > last) {
            selEnd = Integer.MAX_VALUE;
            selStart = Integer.MAX_VALUE;
        }
        if (selEnd < first) {
            selEnd = -1;
            selStart = -1;
        }
        switch(what) {
            case 1:
                if (selStart == selEnd) {
                    return false;
                }
                List<ClickableSpan> links = buffer.getSpans(selStart, selEnd, ClickableSpan.class);
                if (links.size() != 1) {
                    return false;
                }
                ClickableSpan link = (ClickableSpan) links.get(0);
                link.onClick(widget);
                break;
            case 2:
                int bestStart = -1;
                int bestEnd = -1;
                for (ClickableSpan candidate : candidates) {
                    int end = buffer.getSpanEnd(candidate);
                    if ((end < selEnd || selStart == selEnd) && end > bestEnd) {
                        bestStart = buffer.getSpanStart(candidate);
                        bestEnd = end;
                    }
                }
                if (bestStart >= 0) {
                    Selection.setSelection(buffer, bestEnd, bestStart);
                    return true;
                }
                break;
            case 3:
                int bestStart = Integer.MAX_VALUE;
                int bestEnd = Integer.MAX_VALUE;
                for (ClickableSpan candidatex : candidates) {
                    int start = buffer.getSpanStart(candidatex);
                    if ((start > selStart || selStart == selEnd) && start < bestStart) {
                        bestStart = start;
                        bestEnd = buffer.getSpanEnd(candidatex);
                    }
                }
                if (bestEnd < Integer.MAX_VALUE) {
                    Selection.setSelection(buffer, bestStart, bestEnd);
                    return true;
                }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();
        if (action == 1 || action == 0) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();
            x += widget.getScrollX();
            y += widget.getScrollY();
            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, (float) x);
            List<ClickableSpan> links = buffer.getSpans(off, off, ClickableSpan.class);
            if (!links.isEmpty()) {
                ClickableSpan link = (ClickableSpan) links.get(0);
                if (action == 1) {
                    link.onClick(widget);
                } else {
                    Selection.setSelection(buffer, buffer.getSpanStart(link), buffer.getSpanEnd(link));
                }
                return true;
            }
            Selection.removeSelection(buffer);
        }
        return super.onTouchEvent(widget, buffer, event);
    }

    @Override
    public void initialize(TextView widget, Spannable text) {
        Selection.removeSelection(text);
        text.removeSpan(FROM_BELOW);
    }

    @Override
    public void onTakeFocus(TextView view, Spannable text, int dir) {
        Selection.removeSelection(text);
        if ((dir & 1) != 0) {
            text.setSpan(FROM_BELOW, 0, 0, 34);
        } else {
            text.removeSpan(FROM_BELOW);
        }
    }
}