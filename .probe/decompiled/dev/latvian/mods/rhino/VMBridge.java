package dev.latvian.mods.rhino;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class VMBridge {

    public static boolean tryToMakeAccessible(Object target, AccessibleObject accessible) {
        if (accessible.canAccess(target)) {
            return true;
        } else {
            try {
                accessible.setAccessible(true);
            } catch (Exception var3) {
            }
            return accessible.canAccess(target);
        }
    }

    public static Object getInterfaceProxyHelper(Context cx, Class<?>[] interfaces) {
        ClassLoader loader = interfaces[0].getClassLoader();
        Class<?> cl = Proxy.getProxyClass(loader, interfaces);
        try {
            return cl.getConstructor(InvocationHandler.class);
        } catch (NoSuchMethodException var6) {
            throw new IllegalStateException(var6);
        }
    }

    public static Object newInterfaceProxy(Object proxyHelper, InterfaceAdapter adapter, Object target, Scriptable topScope, Context cx) {
        Constructor<?> c = (Constructor<?>) proxyHelper;
        InvocationHandler handler = (proxy, method, args) -> {
            if (method.getDeclaringClass() == Object.class) {
                String methodName = method.getName();
                if (methodName.equals("equals")) {
                    Object other = args[0];
                    return proxy == other;
                }
                if (methodName.equals("hashCode")) {
                    return target.hashCode();
                }
                if (methodName.equals("toString")) {
                    return "Proxy[" + target.toString() + "]";
                }
            }
            return method.isDefault() ? InvocationHandler.invokeDefault(proxy, method, args) : adapter.invoke(cx, target, topScope, proxy, method, args);
        };
        try {
            return c.newInstance(handler);
        } catch (InvocationTargetException var9) {
            throw Context.throwAsScriptRuntimeEx(var9, cx);
        } catch (IllegalAccessException var10) {
            throw new IllegalStateException(var10);
        } catch (InstantiationException var11) {
            throw new IllegalStateException(var11);
        }
    }
}