package icyllis.arc3d.compiler;

public abstract class ErrorHandler {

    private char[] mSource;

    private int mErrors;

    private int mWarnings;

    public final char[] getSource() {
        return this.mSource;
    }

    public final void setSource(char[] source) {
        this.mSource = source;
    }

    public final int errorCount() {
        return this.mErrors;
    }

    public final int warningCount() {
        return this.mWarnings;
    }

    public final void reset() {
        this.mErrors = 0;
        this.mWarnings = 0;
    }

    public final void error(int pos, String msg) {
        this.error(Position.getStartOffset(pos), Position.getEndOffset(pos), msg);
    }

    public final void error(int start, int end, String msg) {
        assert start == -1 && end == -1 || start >= 0 && start <= end && end <= 8388607;
        if (!msg.contains("<POISON>")) {
            this.mErrors++;
            this.handleError(start, end, msg);
        }
    }

    public final void warning(int pos, String msg) {
        this.warning(Position.getStartOffset(pos), Position.getEndOffset(pos), msg);
    }

    public final void warning(int start, int end, String msg) {
        assert start == -1 && end == -1 || start >= 0 && start <= end && end <= 8388607;
        if (!msg.contains("<POISON>")) {
            this.mWarnings++;
            this.handleWarning(start, end, msg);
        }
    }

    protected abstract void handleError(int var1, int var2, String var3);

    protected abstract void handleWarning(int var1, int var2, String var3);
}