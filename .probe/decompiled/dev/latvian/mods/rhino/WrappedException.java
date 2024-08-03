package dev.latvian.mods.rhino;

public class WrappedException extends EvaluatorException {

    private static final long serialVersionUID = -1551979216966520648L;

    private final Throwable exception;

    public WrappedException(Context cx, Throwable exception) {
        super(cx, "Wrapped " + exception);
        this.exception = exception;
        this.initCause(exception);
        int[] linep = new int[] { 0 };
        String sourceName = Context.getSourcePositionFromStack(cx, linep);
        int lineNumber = linep[0];
        if (sourceName != null) {
            this.initSourceName(sourceName);
        }
        if (lineNumber != 0) {
            this.initLineNumber(lineNumber);
        }
    }

    public Throwable getWrappedException() {
        return this.exception;
    }
}