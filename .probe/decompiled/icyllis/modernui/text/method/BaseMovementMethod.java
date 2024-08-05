package icyllis.modernui.text.method;

import icyllis.modernui.text.Layout;
import icyllis.modernui.text.Spannable;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.widget.TextView;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class BaseMovementMethod implements MovementMethod {

    protected BaseMovementMethod() {
    }

    @Override
    public boolean canSelectArbitrarily() {
        return false;
    }

    @Override
    public void initialize(TextView widget, Spannable text) {
    }

    @Override
    public boolean onKeyDown(TextView widget, Spannable text, int keyCode, KeyEvent event) {
        int movementMods = event.getModifiers() & -2;
        return this.handleMovementKey(widget, text, keyCode, movementMods);
    }

    @Override
    public boolean onKeyUp(TextView widget, Spannable text, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onTakeFocus(TextView widget, Spannable text, int direction) {
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable text, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onGenericMotionEvent(TextView widget, Spannable text, MotionEvent event) {
        if (event.getAction() == 8) {
            float vscroll;
            float hscroll;
            if (event.isShiftPressed()) {
                vscroll = 0.0F;
                hscroll = event.getAxisValue(9);
            } else {
                vscroll = -event.getAxisValue(9);
                hscroll = event.getAxisValue(10);
            }
            boolean handled = false;
            if (hscroll < 0.0F) {
                handled = this.scrollLeft(widget, (int) Math.ceil((double) (-hscroll)));
            } else if (hscroll > 0.0F) {
                handled = this.scrollRight(widget, (int) Math.ceil((double) hscroll));
            }
            if (vscroll < 0.0F) {
                handled |= this.scrollUp(widget, (int) Math.ceil((double) (-vscroll)));
            } else if (vscroll > 0.0F) {
                handled |= this.scrollDown(widget, (int) Math.ceil((double) vscroll));
            }
            return handled;
        } else {
            return false;
        }
    }

    protected boolean handleMovementKey(TextView widget, Spannable buffer, int keyCode, int movementMods) {
        switch(keyCode) {
            case 262:
                if (movementMods == 0) {
                    return this.right(widget, buffer);
                }
                if ((movementMods & KeyEvent.META_CTRL_ON) != 0) {
                    return this.rightWord(widget, buffer);
                }
                if ((movementMods & 4) != 0) {
                    return this.lineEnd(widget, buffer);
                }
                break;
            case 263:
                if (movementMods == 0) {
                    return this.left(widget, buffer);
                }
                if ((movementMods & KeyEvent.META_CTRL_ON) != 0) {
                    return this.leftWord(widget, buffer);
                }
                if ((movementMods & 4) != 0) {
                    return this.lineStart(widget, buffer);
                }
                break;
            case 264:
                if (movementMods == 0) {
                    return this.down(widget, buffer);
                }
                if ((movementMods & 4) != 0) {
                    return this.bottom(widget, buffer);
                }
                break;
            case 265:
                if (movementMods == 0) {
                    return this.up(widget, buffer);
                }
                if ((movementMods & 4) != 0) {
                    return this.top(widget, buffer);
                }
                break;
            case 266:
                if (movementMods == 0) {
                    return this.pageUp(widget, buffer);
                }
                if ((movementMods & 4) != 0) {
                    return this.top(widget, buffer);
                }
                break;
            case 267:
                if (movementMods == 0) {
                    return this.pageDown(widget, buffer);
                }
                if ((movementMods & 4) != 0) {
                    return this.bottom(widget, buffer);
                }
                break;
            case 268:
                if (movementMods == 0) {
                    return this.home(widget, buffer);
                }
                if ((movementMods & KeyEvent.META_CTRL_ON) != 0) {
                    return this.top(widget, buffer);
                }
                break;
            case 269:
                if (movementMods == 0) {
                    return this.end(widget, buffer);
                }
                if ((movementMods & KeyEvent.META_CTRL_ON) != 0) {
                    return this.bottom(widget, buffer);
                }
        }
        return false;
    }

    protected boolean left(TextView widget, Spannable buffer) {
        return false;
    }

    protected boolean right(TextView widget, Spannable buffer) {
        return false;
    }

    protected boolean up(TextView widget, Spannable buffer) {
        return false;
    }

    protected boolean down(TextView widget, Spannable buffer) {
        return false;
    }

    protected boolean pageUp(TextView widget, Spannable buffer) {
        return false;
    }

    protected boolean pageDown(TextView widget, Spannable buffer) {
        return false;
    }

    protected boolean top(TextView widget, Spannable buffer) {
        return false;
    }

    protected boolean bottom(TextView widget, Spannable buffer) {
        return false;
    }

    protected boolean lineStart(TextView widget, Spannable buffer) {
        return false;
    }

    protected boolean lineEnd(TextView widget, Spannable buffer) {
        return false;
    }

    protected boolean leftWord(TextView widget, Spannable buffer) {
        return false;
    }

    protected boolean rightWord(TextView widget, Spannable buffer) {
        return false;
    }

    protected boolean home(TextView widget, Spannable buffer) {
        return false;
    }

    protected boolean end(TextView widget, Spannable buffer) {
        return false;
    }

    private int getTopLine(TextView widget) {
        return widget.getLayout().getLineForVertical(widget.getScrollY());
    }

    private int getBottomLine(TextView widget) {
        return widget.getLayout().getLineForVertical(widget.getScrollY() + this.getInnerHeight(widget));
    }

    private int getInnerWidth(TextView widget) {
        return widget.getWidth() - widget.getTotalPaddingLeft() - widget.getTotalPaddingRight();
    }

    private int getInnerHeight(TextView widget) {
        return widget.getHeight() - widget.getTotalPaddingTop() - widget.getTotalPaddingBottom();
    }

    private int getCharacterWidth(TextView widget) {
        return (int) Math.ceil((double) widget.getPaint().getFontMetricsInt(null));
    }

    private int getScrollBoundsLeft(TextView widget) {
        Layout layout = widget.getLayout();
        int topLine = this.getTopLine(widget);
        int bottomLine = this.getBottomLine(widget);
        if (topLine > bottomLine) {
            return 0;
        } else {
            int left = Integer.MAX_VALUE;
            for (int line = topLine; line <= bottomLine; line++) {
                int lineLeft = (int) Math.floor((double) layout.getLineLeft(line));
                if (lineLeft < left) {
                    left = lineLeft;
                }
            }
            return left;
        }
    }

    private int getScrollBoundsRight(TextView widget) {
        Layout layout = widget.getLayout();
        int topLine = this.getTopLine(widget);
        int bottomLine = this.getBottomLine(widget);
        if (topLine > bottomLine) {
            return 0;
        } else {
            int right = Integer.MIN_VALUE;
            for (int line = topLine; line <= bottomLine; line++) {
                int lineRight = (int) Math.ceil((double) layout.getLineRight(line));
                if (lineRight > right) {
                    right = lineRight;
                }
            }
            return right;
        }
    }

    protected boolean scrollLeft(TextView widget, int amount) {
        int minScrollX = this.getScrollBoundsLeft(widget);
        int scrollX = widget.getScrollX();
        if (scrollX > minScrollX) {
            scrollX = Math.max(scrollX - this.getCharacterWidth(widget) * amount, minScrollX);
            widget.scrollTo(scrollX, widget.getScrollY());
            return true;
        } else {
            return false;
        }
    }

    protected boolean scrollRight(TextView widget, int amount) {
        int maxScrollX = this.getScrollBoundsRight(widget) - this.getInnerWidth(widget);
        int scrollX = widget.getScrollX();
        if (scrollX < maxScrollX) {
            scrollX = Math.min(scrollX + this.getCharacterWidth(widget) * amount, maxScrollX);
            widget.scrollTo(scrollX, widget.getScrollY());
            return true;
        } else {
            return false;
        }
    }

    protected boolean scrollUp(TextView widget, int amount) {
        Layout layout = widget.getLayout();
        int top = widget.getScrollY();
        int topLine = layout.getLineForVertical(top);
        if (layout.getLineTop(topLine) == top) {
            topLine--;
        }
        if (topLine >= 0) {
            topLine = Math.max(topLine - amount + 1, 0);
            Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(topLine));
            return true;
        } else {
            return false;
        }
    }

    protected boolean scrollDown(TextView widget, int amount) {
        Layout layout = widget.getLayout();
        int innerHeight = this.getInnerHeight(widget);
        int bottom = widget.getScrollY() + innerHeight;
        int bottomLine = layout.getLineForVertical(bottom);
        if (layout.getLineTop(bottomLine + 1) < bottom + 1) {
            bottomLine++;
        }
        int limit = layout.getLineCount() - 1;
        if (bottomLine <= limit) {
            bottomLine = Math.min(bottomLine + amount - 1, limit);
            Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(bottomLine + 1) - innerHeight);
            return true;
        } else {
            return false;
        }
    }

    protected boolean scrollPageUp(TextView widget) {
        Layout layout = widget.getLayout();
        int top = widget.getScrollY() - this.getInnerHeight(widget);
        int topLine = layout.getLineForVertical(top);
        if (topLine >= 0) {
            Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(topLine));
            return true;
        } else {
            return false;
        }
    }

    protected boolean scrollPageDown(TextView widget) {
        Layout layout = widget.getLayout();
        int innerHeight = this.getInnerHeight(widget);
        int bottom = widget.getScrollY() + innerHeight + innerHeight;
        int bottomLine = layout.getLineForVertical(bottom);
        if (bottomLine <= layout.getLineCount() - 1) {
            Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(bottomLine + 1) - innerHeight);
            return true;
        } else {
            return false;
        }
    }

    protected boolean scrollTop(TextView widget) {
        Layout layout = widget.getLayout();
        if (this.getTopLine(widget) >= 0) {
            Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(0));
            return true;
        } else {
            return false;
        }
    }

    protected boolean scrollBottom(TextView widget) {
        Layout layout = widget.getLayout();
        int lineCount = layout.getLineCount();
        if (this.getBottomLine(widget) <= lineCount - 1) {
            Touch.scrollTo(widget, layout, widget.getScrollX(), layout.getLineTop(lineCount) - this.getInnerHeight(widget));
            return true;
        } else {
            return false;
        }
    }

    protected boolean scrollLineStart(TextView widget) {
        int minScrollX = this.getScrollBoundsLeft(widget);
        int scrollX = widget.getScrollX();
        if (scrollX > minScrollX) {
            widget.scrollTo(minScrollX, widget.getScrollY());
            return true;
        } else {
            return false;
        }
    }

    protected boolean scrollLineEnd(TextView widget) {
        int maxScrollX = this.getScrollBoundsRight(widget) - this.getInnerWidth(widget);
        int scrollX = widget.getScrollX();
        if (scrollX < maxScrollX) {
            widget.scrollTo(maxScrollX, widget.getScrollY());
            return true;
        } else {
            return false;
        }
    }
}