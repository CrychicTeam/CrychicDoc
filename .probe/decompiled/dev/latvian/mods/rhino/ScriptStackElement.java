package dev.latvian.mods.rhino;

public final class ScriptStackElement {

    public final String fileName;

    public final String functionName;

    public final int lineNumber;

    public ScriptStackElement(String fileName, String functionName, int lineNumber) {
        this.fileName = fileName;
        this.functionName = functionName;
        this.lineNumber = lineNumber;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.renderMozillaStyle(sb);
        return sb.toString();
    }

    public void renderJavaStyle(StringBuilder sb) {
        sb.append("\tat ").append(this.fileName);
        if (this.lineNumber > -1) {
            sb.append(':').append(this.lineNumber);
        }
        if (this.functionName != null) {
            sb.append(" (").append(this.functionName).append(')');
        }
    }

    public void renderMozillaStyle(StringBuilder sb) {
        if (this.functionName != null) {
            sb.append(this.functionName).append("()");
        }
        sb.append('@').append(this.fileName);
        if (this.lineNumber > -1) {
            sb.append(':').append(this.lineNumber);
        }
    }
}