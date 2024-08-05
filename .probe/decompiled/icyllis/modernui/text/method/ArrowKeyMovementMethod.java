package icyllis.modernui.text.method;

import icyllis.modernui.text.Layout;
import icyllis.modernui.text.Selection;
import icyllis.modernui.text.Spannable;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.widget.TextView;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ArrowKeyMovementMethod extends BaseMovementMethod {

    private static ArrowKeyMovementMethod sInstance;

    private static final Object LAST_TAP_DOWN = new Object();

    public static MovementMethod getInstance() {
        if (sInstance == null) {
            sInstance = new ArrowKeyMovementMethod();
        }
        return sInstance;
    }

    private static boolean isSelecting(Spannable buffer) {
        return TextKeyListener.getMetaState(buffer, 1) != 0;
    }

    private static int getCurrentLineTop(Spannable buffer, Layout layout) {
        return layout.getLineTop(layout.getLineForOffset(Selection.getSelectionEnd(buffer)));
    }

    private static int getPageHeight(TextView widget) {
        return widget.getHeight();
    }

    @Override
    protected boolean left(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        return isSelecting(buffer) ? Selection.extendLeft(buffer, layout) : Selection.moveLeft(buffer, layout);
    }

    @Override
    protected boolean right(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        return isSelecting(buffer) ? Selection.extendRight(buffer, layout) : Selection.moveRight(buffer, layout);
    }

    @Override
    protected boolean up(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        return isSelecting(buffer) ? Selection.extendUp(buffer, layout) : Selection.moveUp(buffer, layout);
    }

    @Override
    protected boolean down(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        return isSelecting(buffer) ? Selection.extendDown(buffer, layout) : Selection.moveDown(buffer, layout);
    }

    @Override
    protected boolean pageUp(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        boolean selecting = isSelecting(buffer);
        int targetY = getCurrentLineTop(buffer, layout) - getPageHeight(widget);
        boolean handled = false;
        do {
            int previousSelectionEnd = Selection.getSelectionEnd(buffer);
            if (selecting) {
                Selection.extendUp(buffer, layout);
            } else {
                Selection.moveUp(buffer, layout);
            }
            if (Selection.getSelectionEnd(buffer) == previousSelectionEnd) {
                break;
            }
            handled = true;
        } while (getCurrentLineTop(buffer, layout) > targetY);
        return handled;
    }

    @Override
    protected boolean pageDown(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        boolean selecting = isSelecting(buffer);
        int targetY = getCurrentLineTop(buffer, layout) + getPageHeight(widget);
        boolean handled = false;
        do {
            int previousSelectionEnd = Selection.getSelectionEnd(buffer);
            if (selecting) {
                Selection.extendDown(buffer, layout);
            } else {
                Selection.moveDown(buffer, layout);
            }
            if (Selection.getSelectionEnd(buffer) == previousSelectionEnd) {
                break;
            }
            handled = true;
        } while (getCurrentLineTop(buffer, layout) < targetY);
        return handled;
    }

    @Override
    protected boolean top(TextView widget, Spannable buffer) {
        if (isSelecting(buffer)) {
            Selection.extendSelection(buffer, 0);
        } else {
            Selection.setSelection(buffer, 0);
        }
        return true;
    }

    @Override
    protected boolean bottom(TextView widget, Spannable buffer) {
        if (isSelecting(buffer)) {
            Selection.extendSelection(buffer, buffer.length());
        } else {
            Selection.setSelection(buffer, buffer.length());
        }
        return true;
    }

    @Override
    protected boolean lineStart(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        return isSelecting(buffer) ? Selection.extendToLeftEdge(buffer, layout) : Selection.moveToLeftEdge(buffer, layout);
    }

    @Override
    protected boolean lineEnd(TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        return isSelecting(buffer) ? Selection.extendToRightEdge(buffer, layout) : Selection.moveToRightEdge(buffer, layout);
    }

    @Override
    protected boolean leftWord(TextView widget, Spannable buffer) {
        int selectionEnd = widget.getSelectionEnd();
        WordIterator wordIterator = widget.getWordIterator();
        wordIterator.setCharSequence(buffer, selectionEnd, selectionEnd);
        return Selection.moveToPreceding(buffer, wordIterator, isSelecting(buffer));
    }

    @Override
    protected boolean rightWord(TextView widget, Spannable buffer) {
        int selectionEnd = widget.getSelectionEnd();
        WordIterator wordIterator = widget.getWordIterator();
        wordIterator.setCharSequence(buffer, selectionEnd, selectionEnd);
        return Selection.moveToFollowing(buffer, wordIterator, isSelecting(buffer));
    }

    @Override
    protected boolean home(TextView widget, Spannable buffer) {
        return this.lineStart(widget, buffer);
    }

    @Override
    protected boolean end(TextView widget, Spannable buffer) {
        return this.lineEnd(widget, buffer);
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int initialScrollX = -1;
        int initialScrollY = -1;
        int action = event.getAction();
        if (action == 1) {
            initialScrollX = Touch.getInitialScrollX(buffer);
            initialScrollY = Touch.getInitialScrollY(buffer);
        }
        boolean handled = Touch.onTouchEvent(widget, buffer, event);
        if (widget.didTouchFocusSelect()) {
            return handled;
        } else {
            if (action == 0) {
                if (event.isButtonPressed(1) || isSelecting(buffer)) {
                    if (!widget.isFocused() && !widget.requestFocus()) {
                        return handled;
                    }
                    int offset = widget.getOffsetForPosition(event.getX(), event.getY());
                    buffer.setSpan(LAST_TAP_DOWN, offset, offset, 34);
                    assert widget.getParent() != null;
                    widget.getParent().requestDisallowInterceptTouchEvent(true);
                }
            } else if (widget.isFocused()) {
                if (action == 2) {
                    if (handled && event.isButtonPressed(1) || isSelecting(buffer)) {
                        int startOffset = buffer.getSpanStart(LAST_TAP_DOWN);
                        widget.cancelLongPress();
                        int offset = widget.getOffsetForPosition(event.getX(), event.getY());
                        Selection.setSelection(buffer, startOffset, offset);
                        return true;
                    }
                } else if (action == 1) {
                    if ((initialScrollY < 0 || initialScrollY == widget.getScrollY()) && (initialScrollX < 0 || initialScrollX == widget.getScrollX())) {
                        if (event.isButtonPressed(1) || isSelecting(buffer)) {
                            int startOffset = buffer.getSpanStart(LAST_TAP_DOWN);
                            int endOffset = widget.getOffsetForPosition(event.getX(), event.getY());
                            Selection.setSelection(buffer, Math.min(startOffset, endOffset), Math.max(startOffset, endOffset));
                            buffer.removeSpan(LAST_TAP_DOWN);
                        }
                        return true;
                    }
                    widget.moveCursorToVisibleOffset();
                    return true;
                }
            }
            return handled;
        }
    }

    @Override
    public boolean canSelectArbitrarily() {
        return true;
    }

    @Override
    public void initialize(TextView widget, Spannable text) {
        Selection.setSelection(text, 0);
    }

    @Override
    public void onTakeFocus(TextView view, Spannable text, int dir) {
        if ((dir & 130) != 0) {
            if (view.getLayout() == null) {
                Selection.setSelection(text, text.length());
            }
        } else {
            Selection.setSelection(text, text.length());
        }
    }
}