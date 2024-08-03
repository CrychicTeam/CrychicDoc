package dev.latvian.mods.rhino;

public interface ErrorReporter {

    void warning(String var1, String var2, int var3, String var4, int var5);

    void error(Context var1, String var2, String var3, int var4, String var5, int var6);

    EvaluatorException runtimeError(Context var1, String var2, String var3, int var4, String var5, int var6);
}