package icyllis.modernui.text.method;

import icyllis.modernui.text.Layout;
import icyllis.modernui.text.NoCopySpan;
import icyllis.modernui.text.Spannable;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.ViewConfiguration;
import icyllis.modernui.widget.TextView;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class Touch {

    private Touch() {
    }

    public static void scrollTo(TextView widget, Layout layout, int x, int y) {
        int horizontalPadding = widget.getTotalPaddingLeft() + widget.getTotalPaddingRight();
        int availableWidth = widget.getWidth() - horizontalPadding;
        int top = layout.getLineForVertical(y);
        Layout.Alignment a = layout.getParagraphAlignment(top);
        boolean ltr = layout.getParagraphDirection(top) > 0;
        int left;
        int right;
        if (widget.isHorizontallyScrollable()) {
            int verticalPadding = widget.getTotalPaddingTop() + widget.getTotalPaddingBottom();
            int bottom = layout.getLineForVertical(y + widget.getHeight() - verticalPadding);
            left = Integer.MAX_VALUE;
            right = 0;
            for (int i = top; i <= bottom; i++) {
                left = (int) Math.min((float) left, layout.getLineLeft(i));
                right = (int) Math.max((float) right, layout.getLineRight(i));
            }
        } else {
            left = 0;
            right = availableWidth;
        }
        int actualWidth = right - left;
        if (actualWidth < availableWidth) {
            if (a == Layout.Alignment.ALIGN_CENTER) {
                x = left - (availableWidth - actualWidth) / 2;
            } else if ((!ltr || a != Layout.Alignment.ALIGN_OPPOSITE) && (ltr || a != Layout.Alignment.ALIGN_NORMAL) && a != Layout.Alignment.ALIGN_RIGHT) {
                x = left;
            } else {
                x = left - (availableWidth - actualWidth);
            }
        } else {
            x = Math.min(x, right - availableWidth);
            x = Math.max(x, left);
        }
        widget.scrollTo(x, y);
    }

    public static boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        switch(event.getAction()) {
            case 0:
                for (Touch.DragState d : buffer.getSpans(0, buffer.length(), Touch.DragState.class)) {
                    buffer.removeSpan(d);
                }
                buffer.setSpan(new Touch.DragState(event.getX(), event.getY(), widget.getScrollX(), widget.getScrollY()), 0, 0, 17);
                return true;
            case 1:
                List<Touch.DragState> ds = buffer.getSpans(0, buffer.length(), Touch.DragState.class);
                for (Touch.DragState d : ds) {
                    buffer.removeSpan(d);
                }
                return !ds.isEmpty() && ((Touch.DragState) ds.get(0)).mUsed;
            case 2:
                List<Touch.DragState> ds = buffer.getSpans(0, buffer.length(), Touch.DragState.class);
                if (!ds.isEmpty()) {
                    if (!((Touch.DragState) ds.get(0)).mFarEnough) {
                        int slop = ViewConfiguration.get(widget.getContext()).getScaledTouchSlop();
                        if (Math.abs(event.getX() - ((Touch.DragState) ds.get(0)).mX) >= (float) slop || Math.abs(event.getY() - ((Touch.DragState) ds.get(0)).mY) >= (float) slop) {
                            ((Touch.DragState) ds.get(0)).mFarEnough = true;
                        }
                    }
                    if (((Touch.DragState) ds.get(0)).mFarEnough) {
                        ((Touch.DragState) ds.get(0)).mUsed = true;
                        boolean cap = event.isShiftPressed() || event.isButtonPressed(1) || TextKeyListener.getMetaState(buffer, 1) == 1;
                        float dx;
                        float dy;
                        if (cap) {
                            dx = event.getX() - ((Touch.DragState) ds.get(0)).mX;
                            dy = event.getY() - ((Touch.DragState) ds.get(0)).mY;
                        } else {
                            dx = ((Touch.DragState) ds.get(0)).mX - event.getX();
                            dy = ((Touch.DragState) ds.get(0)).mY - event.getY();
                        }
                        ((Touch.DragState) ds.get(0)).mX = event.getX();
                        ((Touch.DragState) ds.get(0)).mY = event.getY();
                        int nx = widget.getScrollX() + (int) dx;
                        int ny = widget.getScrollY() + (int) dy;
                        int padding = widget.getTotalPaddingTop() + widget.getTotalPaddingBottom();
                        Layout layout = widget.getLayout();
                        ny = Math.min(ny, layout.getHeight() - (widget.getHeight() - padding));
                        ny = Math.max(ny, 0);
                        int oldX = widget.getScrollX();
                        int oldY = widget.getScrollY();
                        scrollTo(widget, layout, nx, ny);
                        if (oldX != widget.getScrollX() || oldY != widget.getScrollY()) {
                            widget.cancelLongPress();
                        }
                        return true;
                    }
                }
            default:
                return false;
        }
    }

    public static int getInitialScrollX(Spannable buffer) {
        List<Touch.DragState> ds = buffer.getSpans(0, buffer.length(), Touch.DragState.class);
        return !ds.isEmpty() ? ((Touch.DragState) ds.get(0)).mScrollX : -1;
    }

    public static int getInitialScrollY(Spannable buffer) {
        List<Touch.DragState> ds = buffer.getSpans(0, buffer.length(), Touch.DragState.class);
        return !ds.isEmpty() ? ((Touch.DragState) ds.get(0)).mScrollY : -1;
    }

    private static class DragState implements NoCopySpan {

        private float mX;

        private float mY;

        private final int mScrollX;

        private final int mScrollY;

        private boolean mFarEnough;

        private boolean mUsed;

        private DragState(float x, float y, int scrollX, int scrollY) {
            this.mX = x;
            this.mY = y;
            this.mScrollX = scrollX;
            this.mScrollY = scrollY;
        }
    }
}