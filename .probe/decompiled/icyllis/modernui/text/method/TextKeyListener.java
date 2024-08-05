package icyllis.modernui.text.method;

import com.ibm.icu.lang.UCharacter;
import icyllis.modernui.graphics.text.Emoji;
import icyllis.modernui.graphics.text.FontCollection;
import icyllis.modernui.text.Editable;
import icyllis.modernui.text.Layout;
import icyllis.modernui.text.NoCopySpan;
import icyllis.modernui.text.Selection;
import icyllis.modernui.text.Spannable;
import icyllis.modernui.text.Spanned;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.text.style.ReplacementSpan;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.widget.TextView;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class TextKeyListener {

    private static final TextKeyListener sInstance = new TextKeyListener();

    private static final int PRESSED = 16777233;

    private static final Object CAP = new NoCopySpan.Concrete();

    private static final Object ALT = new NoCopySpan.Concrete();

    private static final int PRESSED_RETURN_VALUE = 1;

    private static final int STATE_START = 0;

    private static final int STATE_LF = 1;

    private static final int STATE_BEFORE_KEYCAP = 2;

    private static final int STATE_BEFORE_VS_AND_KEYCAP = 3;

    private static final int STATE_BEFORE_EMOJI_MODIFIER = 4;

    private static final int STATE_BEFORE_VS_AND_EMOJI_MODIFIER = 5;

    private static final int STATE_BEFORE_VS = 6;

    private static final int STATE_BEFORE_EMOJI = 7;

    private static final int STATE_BEFORE_ZWJ = 8;

    private static final int STATE_BEFORE_VS_AND_ZWJ = 9;

    private static final int STATE_ODD_NUMBERED_RIS = 10;

    private static final int STATE_EVEN_NUMBERED_RIS = 11;

    private static final int STATE_IN_TAG_SEQUENCE = 12;

    private static final int STATE_FINISHED = 13;

    private TextKeyListener() {
    }

    public static TextKeyListener getInstance() {
        return sInstance;
    }

    public static void resetMetaState(Spannable text) {
        text.removeSpan(CAP);
        text.removeSpan(ALT);
    }

    public static int getMetaState(CharSequence text, int meta) {
        return switch(meta) {
            case 1 ->
                getActive(text, CAP);
            case 4 ->
                getActive(text, ALT);
            default ->
                0;
        };
    }

    private static int getActive(CharSequence text, Object meta) {
        if (text instanceof Spanned sp) {
            int flag = sp.getSpanFlags(meta);
            return flag != 0 ? 1 : 0;
        } else {
            return 0;
        }
    }

    public static boolean isMetaTracker(Object what) {
        return what == CAP || what == ALT;
    }

    public boolean onKeyDown(TextView view, Editable content, int keyCode, KeyEvent event) {
        boolean handled = switch(keyCode) {
            case 259 ->
                this.backspace(view, content, event);
            case 261 ->
                this.forwardDelete(view, content, event);
            default ->
                false;
        };
        if (handled) {
            return true;
        } else if (keyCode == 340 || keyCode == 344) {
            this.press(content, CAP);
            return true;
        } else if (keyCode != 342 && keyCode != 346) {
            return false;
        } else {
            this.press(content, ALT);
            return true;
        }
    }

    private void press(Editable content, Object what) {
        content.setSpan(what, 0, 0, 16777233);
    }

    public boolean onKeyUp(TextView view, Editable content, int keyCode, KeyEvent event) {
        if (keyCode == 340 || keyCode == 344) {
            this.release(content, CAP);
            return true;
        } else if (keyCode != 342 && keyCode != 346) {
            return false;
        } else {
            this.release(content, ALT);
            return true;
        }
    }

    private void release(Editable content, Object what) {
        content.removeSpan(what);
    }

    public boolean backspace(TextView view, Editable content, KeyEvent event) {
        return this.backspaceOrForwardDelete(view, content, event, false);
    }

    public boolean forwardDelete(TextView view, Editable content, KeyEvent event) {
        return this.backspaceOrForwardDelete(view, content, event, true);
    }

    private boolean backspaceOrForwardDelete(TextView view, Editable content, KeyEvent event, boolean isForwardDelete) {
        if ((event.getModifiers() & ~(5 | KeyEvent.META_CTRL_ON)) != 0) {
            return false;
        } else if (this.deleteSelection(content)) {
            return true;
        } else {
            boolean isCtrlActive = event.isCtrlPressed();
            boolean isShiftActive = event.isShiftPressed();
            boolean isAltActive = event.isAltPressed();
            if (isCtrlActive) {
                return !isAltActive && !isShiftActive ? this.deleteUntilWordBoundary(view, content, isForwardDelete) : false;
            } else if (isAltActive && this.deleteLine(view, content)) {
                return true;
            } else {
                int start = Selection.getSelectionEnd(content);
                int end;
                if (isForwardDelete) {
                    TextPaint paint = view.getPaint();
                    end = getOffsetForDeleteKey(content, start, paint);
                } else {
                    end = getOffsetForBackspaceKey(content, start);
                }
                if (start != end) {
                    content.delete(Math.min(start, end), Math.max(start, end));
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    private boolean deleteSelection(Editable content) {
        int selectionStart = Selection.getSelectionStart(content);
        int selectionEnd = Selection.getSelectionEnd(content);
        if (selectionEnd < selectionStart) {
            int temp = selectionEnd;
            selectionEnd = selectionStart;
            selectionStart = temp;
        }
        if (selectionStart != selectionEnd) {
            content.delete(selectionStart, selectionEnd);
            return true;
        } else {
            return false;
        }
    }

    private boolean deleteUntilWordBoundary(TextView view, Editable content, boolean isForwardDelete) {
        int currentCursorOffset = Selection.getSelectionStart(content);
        if (currentCursorOffset != Selection.getSelectionEnd(content)) {
            return false;
        } else if ((isForwardDelete || currentCursorOffset != 0) && (!isForwardDelete || currentCursorOffset != content.length())) {
            WordIterator wordIterator = view.getWordIterator();
            if (wordIterator == null) {
                wordIterator = new WordIterator();
            }
            int deleteFrom;
            int deleteTo;
            if (isForwardDelete) {
                deleteFrom = currentCursorOffset;
                wordIterator.setCharSequence(content, currentCursorOffset, content.length());
                deleteTo = wordIterator.following(currentCursorOffset);
                if (deleteTo == -1) {
                    deleteTo = content.length();
                }
            } else {
                deleteTo = currentCursorOffset;
                wordIterator.setCharSequence(content, 0, currentCursorOffset);
                deleteFrom = wordIterator.preceding(currentCursorOffset);
                if (deleteFrom == -1) {
                    deleteFrom = 0;
                }
            }
            content.delete(deleteFrom, deleteTo);
            return true;
        } else {
            return false;
        }
    }

    private boolean deleteLine(TextView view, Editable content) {
        Layout layout = view.getLayout();
        if (layout != null) {
            int line = layout.getLineForOffset(Selection.getSelectionStart(content));
            int start = layout.getLineStart(line);
            int end = layout.getLineEnd(line);
            if (end != start) {
                content.delete(start, end);
                return true;
            }
        }
        return false;
    }

    private static int getOffsetForDeleteKey(Editable text, int offset, TextPaint paint) {
        int len = text.length();
        if (offset >= len - 1) {
            return len;
        } else {
            offset = paint.getTextRunCursor(text, offset, len, offset, 0);
            return adjustReplacementSpan(text, offset, false);
        }
    }

    private static int getOffsetForBackspaceKey(Editable text, int offset) {
        if (offset <= 1) {
            return 0;
        } else {
            int deleteCharCount = 0;
            int lastSeenVSCharCount = 0;
            int state = 0;
            int tmpOffset = offset;
            do {
                int codePoint = Character.codePointBefore(text, tmpOffset);
                tmpOffset -= Character.charCount(codePoint);
                switch(state) {
                    case 0:
                        deleteCharCount = Character.charCount(codePoint);
                        if (codePoint == 10) {
                            state = 1;
                        } else if (FontCollection.isVariationSelector(codePoint)) {
                            state = 6;
                        } else if (Emoji.isRegionalIndicatorSymbol(codePoint)) {
                            state = 10;
                        } else if (Emoji.isEmojiModifier(codePoint)) {
                            state = 4;
                        } else if (codePoint == 8419) {
                            state = 2;
                        } else if (Emoji.isEmoji(codePoint)) {
                            state = 7;
                        } else if (codePoint == 917631) {
                            state = 12;
                        } else {
                            state = 13;
                        }
                        break;
                    case 1:
                        if (codePoint == 13) {
                            deleteCharCount++;
                        }
                        state = 13;
                        break;
                    case 2:
                        if (FontCollection.isVariationSelector(codePoint)) {
                            lastSeenVSCharCount = Character.charCount(codePoint);
                            state = 3;
                        } else {
                            if (Emoji.isKeycapBase(codePoint)) {
                                deleteCharCount += Character.charCount(codePoint);
                            }
                            state = 13;
                        }
                        break;
                    case 3:
                        if (Emoji.isKeycapBase(codePoint)) {
                            deleteCharCount += lastSeenVSCharCount + Character.charCount(codePoint);
                        }
                        state = 13;
                        break;
                    case 4:
                        if (FontCollection.isVariationSelector(codePoint)) {
                            lastSeenVSCharCount = Character.charCount(codePoint);
                            state = 5;
                        } else {
                            if (Emoji.isEmojiModifierBase(codePoint)) {
                                deleteCharCount += Character.charCount(codePoint);
                            }
                            state = 13;
                        }
                        break;
                    case 5:
                        if (Emoji.isEmojiModifierBase(codePoint)) {
                            deleteCharCount += lastSeenVSCharCount + Character.charCount(codePoint);
                        }
                        state = 13;
                        break;
                    case 6:
                        if (Emoji.isEmoji(codePoint)) {
                            deleteCharCount += Character.charCount(codePoint);
                            state = 7;
                        } else {
                            if (!FontCollection.isVariationSelector(codePoint) && UCharacter.getCombiningClass(codePoint) == 0) {
                                deleteCharCount += Character.charCount(codePoint);
                            }
                            state = 13;
                        }
                        break;
                    case 7:
                        if (codePoint == 8205) {
                            state = 8;
                        } else {
                            state = 13;
                        }
                        break;
                    case 8:
                        if (Emoji.isEmoji(codePoint)) {
                            deleteCharCount += Character.charCount(codePoint) + 1;
                            state = Emoji.isEmojiModifier(codePoint) ? 4 : 7;
                        } else if (FontCollection.isVariationSelector(codePoint)) {
                            lastSeenVSCharCount = Character.charCount(codePoint);
                            state = 9;
                        } else {
                            state = 13;
                        }
                        break;
                    case 9:
                        if (Emoji.isEmoji(codePoint)) {
                            deleteCharCount += lastSeenVSCharCount + 1 + Character.charCount(codePoint);
                            lastSeenVSCharCount = 0;
                            state = 7;
                        } else {
                            state = 13;
                        }
                        break;
                    case 10:
                        if (Emoji.isRegionalIndicatorSymbol(codePoint)) {
                            deleteCharCount += 2;
                            state = 11;
                        } else {
                            state = 13;
                        }
                        break;
                    case 11:
                        if (Emoji.isRegionalIndicatorSymbol(codePoint)) {
                            deleteCharCount -= 2;
                            state = 10;
                        } else {
                            state = 13;
                        }
                        break;
                    case 12:
                        if (Emoji.isTagSpecChar(codePoint)) {
                            deleteCharCount += 2;
                        } else if (Emoji.isEmoji(codePoint)) {
                            deleteCharCount += Character.charCount(codePoint);
                            state = 13;
                        } else {
                            deleteCharCount = 2;
                            state = 13;
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("state " + state + " is unknown");
                }
            } while (tmpOffset > 0 && state != 13);
            return adjustReplacementSpan(text, offset - deleteCharCount, true);
        }
    }

    private static int adjustReplacementSpan(Editable text, int offset, boolean moveToStart) {
        for (ReplacementSpan span : text.getSpans(offset, offset, ReplacementSpan.class)) {
            int start = text.getSpanStart(span);
            int end = text.getSpanEnd(span);
            if (start < offset && end > offset) {
                offset = moveToStart ? start : end;
            }
        }
        return offset;
    }
}