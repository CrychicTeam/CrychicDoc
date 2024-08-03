package dev.latvian.mods.rhino;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class InterfaceAdapter {

    private final Object proxyHelper;

    static Object create(Context cx, Class<?> cl, ScriptableObject object) {
        if (!cl.isInterface()) {
            throw new IllegalArgumentException();
        } else {
            Scriptable topScope = cx.getTopCallOrThrow();
            InterfaceAdapter adapter = (InterfaceAdapter) cx.getInterfaceAdapter(cl);
            if (adapter == null) {
                Method[] methods = cl.getMethods();
                if (object instanceof Callable) {
                    int length = methods.length;
                    if (length == 0) {
                        throw Context.reportRuntimeError1("msg.no.empty.interface.conversion", cl.getName(), cx);
                    }
                    if (length > 1) {
                        String methodName = null;
                        for (Method method : methods) {
                            if (isFunctionalMethodCandidate(method)) {
                                if (methodName == null) {
                                    methodName = method.getName();
                                } else if (!methodName.equals(method.getName())) {
                                    throw Context.reportRuntimeError1("msg.no.function.interface.conversion", cl.getName(), cx);
                                }
                            }
                        }
                    }
                }
                adapter = new InterfaceAdapter(cx, cl);
                cx.cacheInterfaceAdapter(cl, adapter);
            }
            return VMBridge.newInterfaceProxy(adapter.proxyHelper, adapter, object, topScope, cx);
        }
    }

    private static boolean isFunctionalMethodCandidate(Method method) {
        return !method.getName().equals("equals") && !method.getName().equals("hashCode") && !method.getName().equals("toString") ? Modifier.isAbstract(method.getModifiers()) : false;
    }

    private InterfaceAdapter(Context cx, Class<?> cl) {
        this.proxyHelper = VMBridge.getInterfaceProxyHelper(cx, new Class[] { cl });
    }

    public Object invoke(Context cx, Object target, Scriptable topScope, Object thisObject, Method method, Object[] args) {
        if (!(target instanceof Callable functionx)) {
            Scriptable s = (Scriptable) target;
            String methodName = method.getName();
            Object value = ScriptableObject.getProperty(s, methodName, cx);
            if (value == Scriptable.NOT_FOUND) {
                Context.reportWarning(ScriptRuntime.getMessage1("msg.undefined.function.interface", methodName), cx);
                Class<?> resultType = method.getReturnType();
                if (resultType == void.class) {
                    return null;
                }
                return Context.jsToJava(cx, null, resultType);
            }
            if (!(value instanceof Callable functionx)) {
                throw Context.reportRuntimeError1("msg.not.function.interface", methodName, cx);
            }
        }
        WrapFactory wf = cx.getWrapFactory();
        if (args == null) {
            args = ScriptRuntime.EMPTY_OBJECTS;
        } else {
            int i = 0;
            for (int N = args.length; i != N; i++) {
                Object arg = args[i];
                if (!(arg instanceof String) && !(arg instanceof Number) && !(arg instanceof Boolean)) {
                    args[i] = wf.wrap(cx, topScope, arg, null);
                }
            }
        }
        Scriptable thisObj = wf.wrapAsJavaObject(cx, topScope, thisObject, null);
        Object result = cx.callSync(functionx, topScope, thisObj, args);
        Class<?> javaResultType = method.getReturnType();
        if (javaResultType == void.class) {
            result = null;
        } else {
            result = Context.jsToJava(cx, result, javaResultType);
        }
        return result;
    }
}