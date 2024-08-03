package dev.latvian.mods.rhino.ast;

import dev.latvian.mods.rhino.ErrorReporter;

public interface IdeErrorReporter extends ErrorReporter {

    void warning(String var1, String var2, int var3, int var4);

    void error(String var1, String var2, int var3, int var4);
}