package dev.latvian.mods.rhino.ast;

public class ParseProblem {

    private ParseProblem.Type type;

    private String message;

    private String sourceName;

    private int offset;

    private int length;

    public ParseProblem(ParseProblem.Type type, String message, String sourceName, int offset, int length) {
        this.setType(type);
        this.setMessage(message);
        this.setSourceName(sourceName);
        this.setFileOffset(offset);
        this.setLength(length);
    }

    public ParseProblem.Type getType() {
        return this.type;
    }

    public void setType(ParseProblem.Type type) {
        this.type = type;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public void setSourceName(String name) {
        this.sourceName = name;
    }

    public int getFileOffset() {
        return this.offset;
    }

    public void setFileOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String toString() {
        return this.sourceName + ":offset=" + this.offset + ",length=" + this.length + "," + (this.type == ParseProblem.Type.Error ? "error: " : "warning: ") + this.message;
    }

    public static enum Type {

        Error, Warning
    }
}