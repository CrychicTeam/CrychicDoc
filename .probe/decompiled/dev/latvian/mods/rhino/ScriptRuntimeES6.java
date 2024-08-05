package dev.latvian.mods.rhino;

public class ScriptRuntimeES6 {

    public static Object requireObjectCoercible(Context cx, Object val, IdFunctionObject idFuncObj) {
        if (val != null && !Undefined.isUndefined(val)) {
            return val;
        } else {
            throw ScriptRuntime.typeError2(cx, "msg.called.null.or.undefined", idFuncObj.getTag(), idFuncObj.getFunctionName());
        }
    }
}