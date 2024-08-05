package com.github.alexthe666.citadel.repack.jcodec.common.logging;

public class Message {

    private LogLevel level;

    private String fileName;

    private String className;

    private int lineNumber;

    private String message;

    private String methodName;

    private Object[] args;

    public Message(LogLevel level, String fileName, String className, String methodName, int lineNumber, String message, Object[] args) {
        this.level = level;
        this.fileName = fileName;
        this.className = className;
        this.methodName = methodName;
        this.message = methodName;
        this.lineNumber = lineNumber;
        this.message = message;
        this.args = args;
    }

    public LogLevel getLevel() {
        return this.level;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getClassName() {
        return this.className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public String getMessage() {
        return this.message;
    }

    public Object[] getArgs() {
        return this.args;
    }
}