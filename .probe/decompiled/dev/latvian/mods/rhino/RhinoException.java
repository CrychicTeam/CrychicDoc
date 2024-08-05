package dev.latvian.mods.rhino;

import java.io.CharArrayWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RhinoException extends RuntimeException {

    private static final Pattern JAVA_STACK_PATTERN = Pattern.compile("_c_(.*)_\\d+");

    private static final long serialVersionUID = 1883500631321581169L;

    Object interpreterStackInfo;

    int[] interpreterLineData;

    private String sourceName;

    private int lineNumber;

    private String lineSource;

    private int columnNumber;

    static String formatStackTrace(ScriptStackElement[] stack, String message) {
        StringBuilder buffer = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        for (ScriptStackElement elem : stack) {
            elem.renderJavaStyle(buffer);
            buffer.append(lineSeparator);
        }
        return buffer.toString();
    }

    RhinoException(Context cx) {
        Evaluator e = Context.createInterpreter();
        if (e != null) {
            e.captureStackInfo(cx, this);
        }
    }

    RhinoException(Context cx, String details) {
        super(details);
        Evaluator e = Context.createInterpreter();
        if (e != null) {
            e.captureStackInfo(cx, this);
        }
    }

    public final String getMessage() {
        String details = this.details();
        if (this.sourceName != null && this.lineNumber > 0) {
            StringBuilder buf = new StringBuilder(details);
            buf.append(" (");
            buf.append(this.sourceName);
            if (this.lineNumber > 0) {
                buf.append('#');
                buf.append(this.lineNumber);
            }
            buf.append(')');
            return buf.toString();
        } else {
            return details;
        }
    }

    public String details() {
        return super.getMessage();
    }

    public final String sourceName() {
        return this.sourceName;
    }

    public final void initSourceName(String sourceName) {
        if (sourceName == null) {
            throw new IllegalArgumentException();
        } else if (this.sourceName != null) {
            throw new IllegalStateException();
        } else {
            this.sourceName = sourceName;
        }
    }

    public final int lineNumber() {
        return this.lineNumber;
    }

    public final void initLineNumber(int lineNumber) {
        if (lineNumber <= 0) {
            throw new IllegalArgumentException(String.valueOf(lineNumber));
        } else if (this.lineNumber > 0) {
            throw new IllegalStateException();
        } else {
            this.lineNumber = lineNumber;
        }
    }

    public final int columnNumber() {
        return this.columnNumber;
    }

    public final void initColumnNumber(int columnNumber) {
        if (columnNumber <= 0) {
            throw new IllegalArgumentException(String.valueOf(columnNumber));
        } else if (this.columnNumber > 0) {
            throw new IllegalStateException();
        } else {
            this.columnNumber = columnNumber;
        }
    }

    public final String lineSource() {
        return this.lineSource;
    }

    public final void initLineSource(String lineSource) {
        if (lineSource == null) {
            throw new IllegalArgumentException();
        } else if (this.lineSource != null) {
            throw new IllegalStateException();
        } else {
            this.lineSource = lineSource;
        }
    }

    final void recordErrorOrigin(String sourceName, int lineNumber, String lineSource, int columnNumber) {
        if (lineNumber == -1) {
            lineNumber = 0;
        }
        if (sourceName != null) {
            this.initSourceName(sourceName);
        }
        if (lineNumber != 0) {
            this.initLineNumber(lineNumber);
        }
        if (lineSource != null) {
            this.initLineSource(lineSource);
        }
        if (columnNumber != 0) {
            this.initColumnNumber(columnNumber);
        }
    }

    private String generateStackTrace() {
        CharArrayWriter writer = new CharArrayWriter();
        super.printStackTrace(new PrintWriter(writer));
        String origStackTrace = writer.toString();
        Evaluator e = Context.createInterpreter();
        return e != null ? e.getPatchedStack(this, origStackTrace) : null;
    }

    public String getScriptStackTrace() {
        return this.getScriptStackTrace(-1, null);
    }

    public String getScriptStackTrace(int limit, String functionName) {
        ScriptStackElement[] stack = this.getScriptStack(limit, functionName);
        return formatStackTrace(stack, this.details());
    }

    public ScriptStackElement[] getScriptStack() {
        return this.getScriptStack(-1, null);
    }

    public ScriptStackElement[] getScriptStack(int limit, String hideFunction) {
        List<ScriptStackElement> list = new ArrayList();
        ScriptStackElement[][] interpreterStack = null;
        if (this.interpreterStackInfo != null) {
            Evaluator interpreter = Context.createInterpreter();
            if (interpreter instanceof Interpreter) {
                interpreterStack = ((Interpreter) interpreter).getScriptStackElements(this);
            }
        }
        int interpreterStackIndex = 0;
        StackTraceElement[] stack = this.getStackTrace();
        int count = 0;
        boolean printStarted = hideFunction == null;
        for (StackTraceElement e : stack) {
            String fileName = e.getFileName();
            if (e.getMethodName().startsWith("_c_") && e.getLineNumber() > -1 && fileName != null && !fileName.endsWith(".java")) {
                String methodName = e.getMethodName();
                Matcher match = JAVA_STACK_PATTERN.matcher(methodName);
                methodName = !"_c_script_0".equals(methodName) && match.find() ? match.group(1) : null;
                if (!printStarted && hideFunction.equals(methodName)) {
                    printStarted = true;
                } else if (printStarted && (limit < 0 || count < limit)) {
                    list.add(new ScriptStackElement(fileName, methodName, e.getLineNumber()));
                    count++;
                }
            } else if ("dev.latvian.mods.rhino.Interpreter".equals(e.getClassName()) && "interpretLoop".equals(e.getMethodName()) && interpreterStack != null && interpreterStack.length > interpreterStackIndex) {
                for (ScriptStackElement elem : interpreterStack[interpreterStackIndex++]) {
                    if (!printStarted && hideFunction.equals(elem.functionName)) {
                        printStarted = true;
                    } else if (printStarted && (limit < 0 || count < limit)) {
                        list.add(elem);
                        count++;
                    }
                }
            }
        }
        return (ScriptStackElement[]) list.toArray(new ScriptStackElement[list.size()]);
    }

    public void printStackTrace(PrintWriter s) {
        if (this.interpreterStackInfo == null) {
            super.printStackTrace(s);
        } else {
            s.print(this.generateStackTrace());
        }
    }

    public void printStackTrace(PrintStream s) {
        if (this.interpreterStackInfo == null) {
            super.printStackTrace(s);
        } else {
            s.print(this.generateStackTrace());
        }
    }
}