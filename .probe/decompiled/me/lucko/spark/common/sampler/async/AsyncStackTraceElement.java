package me.lucko.spark.common.sampler.async;

public class AsyncStackTraceElement {

    public static final String NATIVE_CALL = "native";

    private final String className;

    private final String methodName;

    private final String methodDescription;

    public AsyncStackTraceElement(String className, String methodName, String methodDescription) {
        this.className = className;
        this.methodName = methodName;
        this.methodDescription = methodDescription;
    }

    public String getClassName() {
        return this.className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getMethodDescription() {
        return this.methodDescription;
    }
}