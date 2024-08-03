package dev.latvian.mods.rhino;

public class EvaluatorException extends RhinoException {

    private static final long serialVersionUID = -8743165779676009808L;

    public EvaluatorException(Context cx, String detail) {
        super(cx, detail);
    }

    public EvaluatorException(Context cx, String detail, String sourceName, int lineNumber) {
        this(cx, detail, sourceName, lineNumber, null, 0);
    }

    public EvaluatorException(Context cx, String detail, String sourceName, int lineNumber, String lineSource, int columnNumber) {
        super(cx, detail);
        this.recordErrorOrigin(sourceName, lineNumber, lineSource, columnNumber);
    }
}