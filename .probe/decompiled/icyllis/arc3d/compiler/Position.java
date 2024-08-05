package icyllis.arc3d.compiler;

public final class Position {

    public static final int NO_POS = -1;

    public static final int MAX_OFFSET = 8388607;

    public static int range(int start, int end) {
        return start >= 0 && start <= end && end <= 8388607 ? start | Math.min(end - start, 255) << 24 : -1;
    }

    public static int getStartOffset(int pos) {
        return pos == -1 ? -1 : pos & 16777215;
    }

    public static int getEndOffset(int pos) {
        return pos == -1 ? -1 : (pos & 16777215) + (pos >>> 24);
    }

    public static int getLine(int pos, char[] source) {
        if (pos != -1 && source != null) {
            int offset = Math.min(pos & 16777215, source.length);
            int line = 1;
            for (int i = 0; i < offset; i++) {
                if (source[i] == '\n') {
                    line++;
                }
            }
            return line;
        } else {
            return -1;
        }
    }

    private Position() {
    }
}