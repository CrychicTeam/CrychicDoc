package icyllis.modernui.text;

import icyllis.modernui.text.method.WordIterator;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class Selection {

    public static final Object SELECTION_START = new NoCopySpan.Concrete();

    public static final Object SELECTION_END = new NoCopySpan.Concrete();

    private static final Object SELECTION_MEMORY = new NoCopySpan.Concrete();

    private Selection() {
    }

    public static int getSelectionStart(CharSequence text) {
        return text instanceof Spanned ? ((Spanned) text).getSpanStart(SELECTION_START) : -1;
    }

    public static int getSelectionEnd(CharSequence text) {
        return text instanceof Spanned ? ((Spanned) text).getSpanStart(SELECTION_END) : -1;
    }

    private static int getSelectionMemory(CharSequence text) {
        return text instanceof Spanned ? ((Spanned) text).getSpanStart(SELECTION_MEMORY) : -1;
    }

    public static void setSelection(Spannable text, int start, int stop) {
        setSelection(text, start, stop, -1);
    }

    private static void setSelection(Spannable text, int start, int stop, int memory) {
        int ostart = getSelectionStart(text);
        int oend = getSelectionEnd(text);
        if (ostart != start || oend != stop) {
            text.setSpan(SELECTION_START, start, start, 546);
            text.setSpan(SELECTION_END, stop, stop, 34);
            updateMemory(text, memory);
        }
    }

    private static void updateMemory(Spannable text, int memory) {
        if (memory > -1) {
            int currentMemory = getSelectionMemory(text);
            if (memory != currentMemory) {
                text.setSpan(SELECTION_MEMORY, memory, memory, 34);
                if (currentMemory == -1) {
                    TextWatcher watcher = new Selection.MemoryTextWatcher();
                    text.setSpan(watcher, 0, text.length(), 18);
                }
            }
        } else {
            removeMemory(text);
        }
    }

    private static void removeMemory(Spannable text) {
        text.removeSpan(SELECTION_MEMORY);
        for (Selection.MemoryTextWatcher watcher : text.getSpans(0, text.length(), Selection.MemoryTextWatcher.class)) {
            text.removeSpan(watcher);
        }
    }

    public static void setSelection(Spannable text, int index) {
        setSelection(text, index, index);
    }

    public static void selectAll(Spannable text) {
        setSelection(text, 0, text.length());
    }

    public static void extendSelection(Spannable text, int index) {
        extendSelection(text, index, -1);
    }

    private static void extendSelection(Spannable text, int index, int memory) {
        if (text.getSpanStart(SELECTION_END) != index) {
            text.setSpan(SELECTION_END, index, index, 34);
        }
        updateMemory(text, memory);
    }

    public static void removeSelection(Spannable text) {
        text.removeSpan(SELECTION_START, 512);
        text.removeSpan(SELECTION_END);
        removeMemory(text);
    }

    public static boolean moveUp(Spannable text, Layout layout) {
        int start = getSelectionStart(text);
        int end = getSelectionEnd(text);
        if (start == end) {
            int line = layout.getLineForOffset(end);
            if (line > 0) {
                setSelectionAndMemory(text, layout, line, end, -1, false);
                return true;
            } else if (end != 0) {
                setSelection(text, 0);
                return true;
            } else {
                return false;
            }
        } else {
            int min = Math.min(start, end);
            int max = Math.max(start, end);
            setSelection(text, min);
            return min != 0 || max != text.length();
        }
    }

    private static void setSelectionAndMemory(Spannable text, Layout layout, int line, int end, int direction, boolean extend) {
        int move;
        int newMemory;
        if (layout.getParagraphDirection(line) == layout.getParagraphDirection(line + direction)) {
            int memory = getSelectionMemory(text);
            if (memory > -1) {
                float h = layout.getPrimaryHorizontal(memory);
                move = layout.getOffsetForHorizontal(line + direction, h);
                newMemory = memory;
            } else {
                float h = layout.getPrimaryHorizontal(end);
                move = layout.getOffsetForHorizontal(line + direction, h);
                newMemory = end;
            }
        } else {
            move = layout.getLineStart(line + direction);
            newMemory = -1;
        }
        if (extend) {
            extendSelection(text, move, newMemory);
        } else {
            setSelection(text, move, move, newMemory);
        }
    }

    public static boolean moveDown(Spannable text, Layout layout) {
        int start = getSelectionStart(text);
        int end = getSelectionEnd(text);
        if (start == end) {
            int line = layout.getLineForOffset(end);
            if (line < layout.getLineCount() - 1) {
                setSelectionAndMemory(text, layout, line, end, 1, false);
                return true;
            } else if (end != text.length()) {
                setSelection(text, text.length());
                return true;
            } else {
                return false;
            }
        } else {
            int min = Math.min(start, end);
            int max = Math.max(start, end);
            setSelection(text, max);
            return min != 0 || max != text.length();
        }
    }

    public static boolean moveLeft(Spannable text, Layout layout) {
        int start = getSelectionStart(text);
        int end = getSelectionEnd(text);
        if (start != end) {
            setSelection(text, chooseHorizontal(layout, -1, start, end));
            return true;
        } else {
            int to = layout.getOffsetToLeftOf(end);
            if (to != end) {
                setSelection(text, to);
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean moveRight(Spannable text, Layout layout) {
        int start = getSelectionStart(text);
        int end = getSelectionEnd(text);
        if (start != end) {
            setSelection(text, chooseHorizontal(layout, 1, start, end));
            return true;
        } else {
            int to = layout.getOffsetToRightOf(end);
            if (to != end) {
                setSelection(text, to);
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean extendUp(Spannable text, Layout layout) {
        int end = getSelectionEnd(text);
        int line = layout.getLineForOffset(end);
        if (line > 0) {
            setSelectionAndMemory(text, layout, line, end, -1, true);
            return true;
        } else if (end != 0) {
            extendSelection(text, 0);
            return true;
        } else {
            return true;
        }
    }

    public static boolean extendDown(Spannable text, Layout layout) {
        int end = getSelectionEnd(text);
        int line = layout.getLineForOffset(end);
        if (line < layout.getLineCount() - 1) {
            setSelectionAndMemory(text, layout, line, end, 1, true);
            return true;
        } else if (end != text.length()) {
            extendSelection(text, text.length(), -1);
            return true;
        } else {
            return true;
        }
    }

    public static boolean extendLeft(Spannable text, Layout layout) {
        int end = getSelectionEnd(text);
        int to = layout.getOffsetToLeftOf(end);
        if (to != end) {
            extendSelection(text, to);
            return true;
        } else {
            return true;
        }
    }

    public static boolean extendRight(Spannable text, Layout layout) {
        int end = getSelectionEnd(text);
        int to = layout.getOffsetToRightOf(end);
        if (to != end) {
            extendSelection(text, to);
            return true;
        } else {
            return true;
        }
    }

    public static boolean extendToLeftEdge(Spannable text, Layout layout) {
        int where = findEdge(text, layout, -1);
        extendSelection(text, where);
        return true;
    }

    public static boolean extendToRightEdge(Spannable text, Layout layout) {
        int where = findEdge(text, layout, 1);
        extendSelection(text, where);
        return true;
    }

    public static boolean moveToLeftEdge(Spannable text, Layout layout) {
        int where = findEdge(text, layout, -1);
        setSelection(text, where);
        return true;
    }

    public static boolean moveToRightEdge(Spannable text, Layout layout) {
        int where = findEdge(text, layout, 1);
        setSelection(text, where);
        return true;
    }

    public static boolean moveToPreceding(Spannable text, WordIterator iter, boolean extendSelection) {
        int offset = iter.preceding(getSelectionEnd(text));
        if (offset != -1) {
            if (extendSelection) {
                extendSelection(text, offset);
            } else {
                setSelection(text, offset);
            }
        }
        return true;
    }

    public static boolean moveToFollowing(Spannable text, WordIterator iter, boolean extendSelection) {
        int offset = iter.following(getSelectionEnd(text));
        if (offset != -1) {
            if (extendSelection) {
                extendSelection(text, offset);
            } else {
                setSelection(text, offset);
            }
        }
        return true;
    }

    private static int findEdge(Spannable text, Layout layout, int dir) {
        int pt = getSelectionEnd(text);
        int line = layout.getLineForOffset(pt);
        int pdir = layout.getParagraphDirection(line);
        if (dir * pdir < 0) {
            return layout.getLineStart(line);
        } else {
            int end = layout.getLineEnd(line);
            return line == layout.getLineCount() - 1 ? end : end - 1;
        }
    }

    private static int chooseHorizontal(Layout layout, int direction, int off1, int off2) {
        int line1 = layout.getLineForOffset(off1);
        int line2 = layout.getLineForOffset(off2);
        if (line1 == line2) {
            float h1 = layout.getPrimaryHorizontal(off1);
            float h2 = layout.getPrimaryHorizontal(off2);
            if (direction < 0) {
                return h1 < h2 ? off1 : off2;
            } else {
                return h1 > h2 ? off1 : off2;
            }
        } else {
            int line = layout.getLineForOffset(off1);
            int textdir = layout.getParagraphDirection(line);
            return textdir == direction ? Math.max(off1, off2) : Math.min(off1, off2);
        }
    }

    private static final class MemoryTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            s.removeSpan(Selection.SELECTION_MEMORY);
            s.removeSpan(this);
        }
    }
}