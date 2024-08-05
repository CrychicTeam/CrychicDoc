package dev.latvian.mods.rhino;

public class CompilerEnvirons {

    private ErrorReporter errorReporter = DefaultErrorReporter.instance;

    public void initFromContext(Context cx) {
        this.setErrorReporter(cx.getErrorReporter());
    }

    public final ErrorReporter getErrorReporter() {
        return this.errorReporter;
    }

    public void setErrorReporter(ErrorReporter errorReporter) {
        if (errorReporter == null) {
            throw new IllegalArgumentException();
        } else {
            this.errorReporter = errorReporter;
        }
    }

    public final boolean isStrictMode() {
        return false;
    }
}