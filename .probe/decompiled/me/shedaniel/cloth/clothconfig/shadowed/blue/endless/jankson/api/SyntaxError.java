package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api;

public class SyntaxError extends Exception {

    int startLine = -1;

    int startColumn = -1;

    int line = -1;

    int column = -1;

    public SyntaxError(String message) {
        super(message);
    }

    public String getCompleteMessage() {
        StringBuilder message = new StringBuilder();
        if (this.startLine != -1 && this.startColumn != -1) {
            message.append("Started at line ");
            message.append(this.startLine + 1);
            message.append(", column ");
            message.append(this.startColumn + 1);
            message.append("; ");
        }
        if (this.line != -1 && this.column != -1) {
            message.append("Errored at line ");
            message.append(this.line + 1);
            message.append(", column ");
            message.append(this.column + 1);
            message.append("; ");
        }
        message.append(super.getMessage());
        return message.toString();
    }

    public String getLineMessage() {
        StringBuilder message = new StringBuilder();
        boolean hasStart = this.startLine != -1 && this.startColumn != -1;
        boolean hasEnd = this.line != -1 && this.column != -1;
        if (hasStart) {
            message.append("Started at line ");
            message.append(this.startLine + 1);
            message.append(", column ");
            message.append(this.startColumn + 1);
        }
        if (hasStart && hasEnd) {
            message.append("; ");
        }
        if (hasEnd) {
            message.append("Errored at line ");
            message.append(this.line + 1);
            message.append(", column ");
            message.append(this.column + 1);
        }
        return message.toString();
    }

    public void setStartParsing(int line, int column) {
        this.startLine = line;
        this.startColumn = column;
    }

    public void setEndParsing(int line, int column) {
        this.line = line;
        this.column = column;
    }
}