package dev.latvian.mods.rhino;

public abstract class NativeFunction extends BaseFunction {

    public final void initScriptFunction(Context cx, Scriptable scope) {
        this.initScriptFunction(cx, scope, this.isGeneratorFunction());
    }

    public final void initScriptFunction(Context cx, Scriptable scope, boolean es6GeneratorFunction) {
        ScriptRuntime.setFunctionProtoAndParent(cx, scope, this, es6GeneratorFunction);
    }

    @Override
    public int getLength() {
        return this.getParamCount();
    }

    @Override
    public int getArity() {
        return this.getParamCount();
    }

    public Object resumeGenerator(Context cx, Scriptable scope, int operation, Object state, Object value) {
        throw new EvaluatorException(cx, "resumeGenerator() not implemented");
    }

    protected abstract int getParamCount();

    protected abstract int getParamAndVarCount();

    protected abstract String getParamOrVarName(int var1);

    protected boolean getParamOrVarConst(int index) {
        return false;
    }
}