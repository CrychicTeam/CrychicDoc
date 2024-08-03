package dev.latvian.mods.rhino;

final class InterpretedFunction extends NativeFunction implements Script {

    InterpreterData idata;

    static InterpretedFunction createScript(InterpreterData idata, Object staticSecurityDomain) {
        return new InterpretedFunction(idata, staticSecurityDomain);
    }

    static InterpretedFunction createFunction(Context cx, Scriptable scope, InterpreterData idata, Object staticSecurityDomain) {
        InterpretedFunction f = new InterpretedFunction(idata, staticSecurityDomain);
        f.initScriptFunction(cx, scope, f.idata.isES6Generator);
        return f;
    }

    static InterpretedFunction createFunction(Context cx, Scriptable scope, InterpretedFunction parent, int index) {
        InterpretedFunction f = new InterpretedFunction(parent, index);
        f.initScriptFunction(cx, scope, f.idata.isES6Generator);
        return f;
    }

    private InterpretedFunction(InterpreterData idata, Object staticSecurityDomain) {
        this.idata = idata;
        if (staticSecurityDomain != null) {
            throw new IllegalArgumentException();
        }
    }

    private InterpretedFunction(InterpretedFunction parent, int index) {
        this.idata = parent.idata.itsNestedFunctions[index];
    }

    @Override
    public String getFunctionName() {
        return this.idata.itsName == null ? "" : this.idata.itsName;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return !cx.hasTopCallScope() ? cx.doTopCall(scope, this, thisObj, args, this.idata.isStrict) : Interpreter.interpret(this, cx, scope, thisObj, args);
    }

    @Override
    public Object exec(Context cx, Scriptable scope) {
        if (!this.isScript()) {
            throw new IllegalStateException();
        } else {
            return !cx.hasTopCallScope() ? cx.doTopCall(scope, this, scope, ScriptRuntime.EMPTY_OBJECTS, this.idata.isStrict) : Interpreter.interpret(this, cx, scope, scope, ScriptRuntime.EMPTY_OBJECTS);
        }
    }

    public boolean isScript() {
        return this.idata.itsFunctionType == 0;
    }

    @Override
    public Object resumeGenerator(Context cx, Scriptable scope, int operation, Object state, Object value) {
        return Interpreter.resumeGenerator(cx, scope, operation, state, value);
    }

    @Override
    protected int getParamCount() {
        return this.idata.argCount;
    }

    @Override
    protected int getParamAndVarCount() {
        return this.idata.argNames.length;
    }

    @Override
    protected String getParamOrVarName(int index) {
        return this.idata.argNames[index];
    }

    @Override
    protected boolean getParamOrVarConst(int index) {
        return this.idata.argIsConst[index];
    }

    boolean hasFunctionNamed(String name) {
        for (int f = 0; f < this.idata.getFunctionCount(); f++) {
            InterpreterData functionData = this.idata.getFunction(f);
            if (!functionData.declaredAsFunctionExpression && name.equals(functionData.getFunctionName())) {
                return false;
            }
        }
        return true;
    }
}