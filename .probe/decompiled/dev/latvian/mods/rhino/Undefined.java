package dev.latvian.mods.rhino;

import java.io.Serializable;
import java.lang.reflect.Proxy;

public class Undefined implements Serializable {

    public static final Object instance = new Undefined();

    public static final Scriptable SCRIPTABLE_UNDEFINED = (Scriptable) Proxy.newProxyInstance(Undefined.class.getClassLoader(), new Class[] { Scriptable.class }, (proxy, method, args) -> {
        if (method.getName().equals("toString")) {
            return "undefined";
        } else if (!method.getName().equals("equals")) {
            throw new UnsupportedOperationException("undefined doesn't support " + method.getName());
        } else {
            return args.length > 0 && isUndefined(args[0]);
        }
    });

    public static boolean isUndefined(Object obj) {
        return instance == obj || SCRIPTABLE_UNDEFINED == obj;
    }

    private Undefined() {
    }

    public Object readResolve() {
        return instance;
    }

    public boolean equals(Object obj) {
        return isUndefined(obj) || super.equals(obj);
    }

    public int hashCode() {
        return 0;
    }
}