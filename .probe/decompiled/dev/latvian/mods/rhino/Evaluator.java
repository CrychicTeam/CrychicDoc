package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.ast.ScriptNode;
import java.util.List;

public interface Evaluator {

    Object compile(CompilerEnvirons var1, ScriptNode var2, boolean var3, Context var4);

    Function createFunctionObject(Context var1, Scriptable var2, Object var3, Object var4);

    Script createScriptObject(Object var1, Object var2);

    void captureStackInfo(Context var1, RhinoException var2);

    String getSourcePositionFromStack(Context var1, int[] var2);

    String getPatchedStack(RhinoException var1, String var2);

    List<String> getScriptStack(RhinoException var1);

    void setEvalScriptFlag(Script var1);
}