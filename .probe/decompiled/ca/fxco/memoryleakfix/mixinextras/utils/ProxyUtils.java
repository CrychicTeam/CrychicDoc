package ca.fxco.memoryleakfix.mixinextras.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class ProxyUtils {

    public static <T> T getProxy(Object impl, Class<T> interfaceClass) {
        if (interfaceClass.isInstance(impl)) {
            return (T) interfaceClass.cast(impl);
        } else {
            String simpleName = interfaceClass.getSimpleName();
            if (Arrays.stream(impl.getClass().getInterfaces()).anyMatch(it -> it.getName().endsWith('.' + simpleName))) {
                return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass }, (proxy, method, args) -> {
                    Method original = impl.getClass().getMethod(method.getName(), method.getParameterTypes());
                    original.setAccessible(true);
                    return original.invoke(impl, args);
                });
            } else {
                throw new UnsupportedOperationException(String.format("Cannot get a %s instance from %s", simpleName, impl));
            }
        }
    }
}