package dev.latvian.mods.rhino.ast;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.EvaluatorException;
import java.util.ArrayList;
import java.util.List;

public class ErrorCollector implements IdeErrorReporter {

    private final List<ParseProblem> errors = new ArrayList();

    @Override
    public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void warning(String message, String sourceName, int offset, int length) {
        this.errors.add(new ParseProblem(ParseProblem.Type.Warning, message, sourceName, offset, length));
    }

    @Override
    public void error(Context cx, String message, String sourceName, int line, String lineSource, int lineOffset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void error(String message, String sourceName, int fileOffset, int length) {
        this.errors.add(new ParseProblem(ParseProblem.Type.Error, message, sourceName, fileOffset, length));
    }

    @Override
    public EvaluatorException runtimeError(Context cx, String message, String sourceName, int line, String lineSource, int lineOffset) {
        throw new UnsupportedOperationException();
    }

    public List<ParseProblem> getErrors() {
        return this.errors;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.errors.size() * 100);
        for (ParseProblem pp : this.errors) {
            sb.append(pp.toString()).append("\n");
        }
        return sb.toString();
    }
}