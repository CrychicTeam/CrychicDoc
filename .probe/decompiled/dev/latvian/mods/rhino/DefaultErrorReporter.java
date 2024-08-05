package dev.latvian.mods.rhino;

class DefaultErrorReporter implements ErrorReporter {

    static final DefaultErrorReporter instance = new DefaultErrorReporter();

    private boolean forEval;

    private ErrorReporter chainedReporter;

    static ErrorReporter forEval(ErrorReporter reporter) {
        DefaultErrorReporter r = new DefaultErrorReporter();
        r.forEval = true;
        r.chainedReporter = reporter;
        return r;
    }

    private DefaultErrorReporter() {
    }

    @Override
    public void warning(String message, String sourceURI, int line, String lineText, int lineOffset) {
        if (this.chainedReporter != null) {
            this.chainedReporter.warning(message, sourceURI, line, lineText, lineOffset);
        }
    }

    @Override
    public void error(Context cx, String message, String sourceURI, int line, String lineText, int lineOffset) {
        if (this.forEval) {
            String error = "SyntaxError";
            String TYPE_ERROR_NAME = "TypeError";
            String DELIMETER = ": ";
            String prefix = "TypeError: ";
            if (message.startsWith("TypeError: ")) {
                error = "TypeError";
                message = message.substring("TypeError: ".length());
            }
            throw ScriptRuntime.constructError(cx, error, message, sourceURI, line, lineText, lineOffset);
        } else if (this.chainedReporter != null) {
            this.chainedReporter.error(cx, message, sourceURI, line, lineText, lineOffset);
        } else {
            throw this.runtimeError(cx, message, sourceURI, line, lineText, lineOffset);
        }
    }

    @Override
    public EvaluatorException runtimeError(Context cx, String message, String sourceURI, int line, String lineText, int lineOffset) {
        return this.chainedReporter != null ? this.chainedReporter.runtimeError(cx, message, sourceURI, line, lineText, lineOffset) : new EvaluatorException(cx, message, sourceURI, line, lineText, lineOffset);
    }
}