package dev.latvian.mods.rhino;

public class EcmaError extends RhinoException {

    private static final long serialVersionUID = -6261226256957286699L;

    private final String errorName;

    private final String errorMessage;

    EcmaError(Context cx, String errorName, String errorMessage, String sourceName, int lineNumber, String lineSource, int columnNumber) {
        super(cx);
        this.recordErrorOrigin(sourceName, lineNumber, lineSource, columnNumber);
        this.errorName = errorName;
        this.errorMessage = errorMessage;
    }

    @Override
    public String details() {
        return this.errorName + ": " + this.errorMessage;
    }

    public String getName() {
        return this.errorName;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}