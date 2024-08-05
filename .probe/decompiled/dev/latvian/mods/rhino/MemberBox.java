package dev.latvian.mods.rhino;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class MemberBox {

    transient Class<?>[] argTypes;

    transient Object delegateTo;

    transient boolean vararg;

    public transient Executable executable;

    public transient WrappedExecutable wrappedExecutable;

    private static Method searchAccessibleMethod(Method method, Class<?>[] params) {
        int modifiers = method.getModifiers();
        if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
            Class<?> c = method.getDeclaringClass();
            if (!Modifier.isPublic(c.getModifiers())) {
                String name = method.getName();
                Class<?>[] intfs = c.getInterfaces();
                int i = 0;
                for (int N = intfs.length; i != N; i++) {
                    Class<?> intf = intfs[i];
                    if (Modifier.isPublic(intf.getModifiers())) {
                        try {
                            return intf.getMethod(name, params);
                        } catch (NoSuchMethodException var12) {
                        } catch (SecurityException var13) {
                        }
                    }
                }
                while (true) {
                    c = c.getSuperclass();
                    if (c == null) {
                        break;
                    }
                    if (Modifier.isPublic(c.getModifiers())) {
                        try {
                            Method m = c.getMethod(name, params);
                            int mModifiers = m.getModifiers();
                            if (Modifier.isPublic(mModifiers) && !Modifier.isStatic(mModifiers)) {
                                return m;
                            }
                        } catch (NoSuchMethodException var10) {
                        } catch (SecurityException var11) {
                        }
                    }
                }
            }
        }
        return null;
    }

    MemberBox(Executable executable) {
        this.executable = executable;
        this.argTypes = executable.getParameterTypes();
        this.vararg = executable.isVarArgs();
    }

    MemberBox(WrappedExecutable wrappedExecutable) {
        Executable executable = wrappedExecutable.unwrap();
        if (executable != null) {
            this.executable = executable;
            this.argTypes = executable.getParameterTypes();
            this.vararg = executable.isVarArgs();
        } else {
            this.wrappedExecutable = wrappedExecutable;
            this.vararg = false;
        }
    }

    Constructor<?> ctor() {
        return (Constructor<?>) this.executable;
    }

    Member member() {
        return this.executable;
    }

    boolean isMethod() {
        return this.executable instanceof Method;
    }

    boolean isCtor() {
        return this.executable instanceof Constructor;
    }

    boolean isStatic() {
        return Modifier.isStatic(this.executable.getModifiers());
    }

    boolean isPublic() {
        return Modifier.isPublic(this.executable.getModifiers());
    }

    String getName() {
        return this.wrappedExecutable != null ? this.wrappedExecutable.toString() : this.executable.getName();
    }

    Class<?> getDeclaringClass() {
        return this.executable.getDeclaringClass();
    }

    Class<?> getReturnType() {
        return this.wrappedExecutable != null ? this.wrappedExecutable.getReturnType() : ((Method) this.executable).getReturnType();
    }

    String toJavaDeclaration() {
        StringBuilder sb = new StringBuilder();
        if (this.isMethod()) {
            sb.append(this.getReturnType());
            sb.append(' ');
            sb.append(this.getName());
        } else {
            String name = this.getDeclaringClass().getName();
            int lastDot = name.lastIndexOf(46);
            if (lastDot >= 0) {
                name = name.substring(lastDot + 1);
            }
            sb.append(name);
        }
        sb.append(JavaMembers.liveConnectSignature(this.argTypes));
        return sb.toString();
    }

    public String toString() {
        return this.executable.toString();
    }

    Object invoke(Object target, Object[] args, Context cx, Scriptable scope) {
        if (this.wrappedExecutable != null) {
            try {
                return this.wrappedExecutable.invoke(cx, scope, target, args);
            } catch (Exception var8) {
                throw Context.throwAsScriptRuntimeEx(var8, cx);
            }
        } else {
            Method method = (Method) this.executable;
            try {
                try {
                    return method.invoke(target, args);
                } catch (IllegalAccessException var9) {
                    Method accessible = searchAccessibleMethod(method, this.argTypes);
                    if (accessible != null) {
                        this.executable = accessible;
                        method = accessible;
                    } else if (!VMBridge.tryToMakeAccessible(target, method)) {
                        throw Context.throwAsScriptRuntimeEx(var9, cx);
                    }
                    return method.invoke(target, args);
                }
            } catch (InvocationTargetException var10) {
                Throwable e = var10;
                do {
                    e = ((InvocationTargetException) e).getTargetException();
                } while (e instanceof InvocationTargetException);
                throw Context.throwAsScriptRuntimeEx(e, cx);
            } catch (Exception var11) {
                throw Context.throwAsScriptRuntimeEx(var11, cx);
            }
        }
    }

    Object newInstance(Object[] args, Context cx, Scriptable scope) {
        if (this.wrappedExecutable != null) {
            try {
                return this.wrappedExecutable.construct(cx, scope, args);
            } catch (Exception var6) {
                throw Context.throwAsScriptRuntimeEx(var6, cx);
            }
        } else {
            Constructor<?> ctor = this.ctor();
            try {
                try {
                    return ctor.newInstance(args);
                } catch (IllegalAccessException var7) {
                    if (!VMBridge.tryToMakeAccessible(null, ctor)) {
                        throw Context.throwAsScriptRuntimeEx(var7, cx);
                    } else {
                        return ctor.newInstance(args);
                    }
                }
            } catch (Exception var8) {
                throw Context.throwAsScriptRuntimeEx(var8, cx);
            }
        }
    }
}