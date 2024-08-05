package dev.latvian.mods.rhino;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Map;

public class NativeJavaClass extends NativeJavaObject implements Function {

    static final String javaClassPropertyName = "__javaObject__";

    private Map<String, FieldAndMethods> staticFieldAndMethods;

    static Scriptable constructSpecific(Context cx, Scriptable scope, Object[] args, MemberBox ctor) {
        Object instance = constructInternal(cx, scope, args, ctor);
        Scriptable topLevel = ScriptableObject.getTopLevelScope(scope);
        return cx.getWrapFactory().wrapNewObject(topLevel, instance, cx);
    }

    static Object constructInternal(Context cx, Scriptable scope, Object[] args, MemberBox ctor) {
        Class<?>[] argTypes = ctor.argTypes;
        if (ctor.vararg) {
            Object[] newArgs = new Object[argTypes.length];
            for (int i = 0; i < argTypes.length - 1; i++) {
                newArgs[i] = Context.jsToJava(cx, args[i], argTypes[i]);
            }
            Object varArgs;
            if (args.length != argTypes.length || args[args.length - 1] != null && !(args[args.length - 1] instanceof NativeArray) && !(args[args.length - 1] instanceof NativeJavaArray)) {
                Class<?> componentType = argTypes[argTypes.length - 1].getComponentType();
                varArgs = Array.newInstance(componentType, args.length - argTypes.length + 1);
                for (int i = 0; i < Array.getLength(varArgs); i++) {
                    Object value = Context.jsToJava(cx, args[argTypes.length - 1 + i], componentType);
                    Array.set(varArgs, i, value);
                }
            } else {
                varArgs = Context.jsToJava(cx, args[args.length - 1], argTypes[argTypes.length - 1]);
            }
            newArgs[argTypes.length - 1] = varArgs;
            args = newArgs;
        } else {
            Object[] origArgs = args;
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                Object x = Context.jsToJava(cx, arg, argTypes[i]);
                if (x != arg) {
                    if (args == origArgs) {
                        args = (Object[]) origArgs.clone();
                    }
                    args[i] = x;
                }
            }
        }
        return ctor.newInstance(args, cx, scope);
    }

    private static Class<?> findNestedClass(Class<?> parentClass, String name) {
        String nestedClassName = parentClass.getName() + "$" + name;
        ClassLoader loader = parentClass.getClassLoader();
        return loader == null ? Kit.classOrNull(nestedClassName) : Kit.classOrNull(loader, nestedClassName);
    }

    public NativeJavaClass() {
    }

    public NativeJavaClass(Context cx, Scriptable scope, Class<?> cl) {
        this(cx, scope, cl, false);
    }

    public NativeJavaClass(Context cx, Scriptable scope, Class<?> cl, boolean isAdapter) {
        super(scope, cl, null, isAdapter, cx);
    }

    @Override
    protected void initMembers(Context cx, Scriptable scope) {
        Class<?> cl = (Class<?>) this.javaObject;
        this.members = JavaMembers.lookupClass(cx, scope, cl, cl, this.isAdapter);
        this.staticFieldAndMethods = this.members.getFieldAndMethodsObjects(this, cl, true, cx);
    }

    @Override
    public String getClassName() {
        return "JavaClass";
    }

    @Override
    public boolean has(Context cx, String name, Scriptable start) {
        return this.members.has(name, true) || "__javaObject__".equals(name);
    }

    @Override
    public Object get(Context cx, String name, Scriptable start) {
        if (name.equals("prototype")) {
            return null;
        } else {
            if (this.staticFieldAndMethods != null) {
                Object result = this.staticFieldAndMethods.get(name);
                if (result != null) {
                    return result;
                }
            }
            if (this.members.has(name, true)) {
                return this.members.get(this, name, this.javaObject, true, cx);
            } else {
                Scriptable scope = ScriptableObject.getTopLevelScope(start);
                if ("__javaObject__".equals(name)) {
                    return cx.getWrapFactory().wrap(cx, scope, this.javaObject, ScriptRuntime.ClassClass);
                } else {
                    Class<?> nestedClass = findNestedClass(this.getClassObject(), name);
                    if (nestedClass != null) {
                        Scriptable nestedValue = cx.getWrapFactory().wrapJavaClass(cx, scope, nestedClass);
                        nestedValue.setParentScope(this);
                        return nestedValue;
                    } else {
                        throw this.members.reportMemberNotFound(name, cx);
                    }
                }
            }
        }
    }

    @Override
    public void put(Context cx, String name, Scriptable start, Object value) {
        this.members.put(this, name, this.javaObject, value, true, cx);
    }

    @Override
    public Object[] getIds(Context cx) {
        return this.members.getIds(true);
    }

    public Class<?> getClassObject() {
        return (Class<?>) super.unwrap();
    }

    @Override
    public Object getDefaultValue(Context cx, Class<?> hint) {
        if (hint == null || hint == ScriptRuntime.StringClass) {
            return this.toString();
        } else if (hint == ScriptRuntime.BooleanClass) {
            return Boolean.TRUE;
        } else {
            return hint == ScriptRuntime.NumberClass ? ScriptRuntime.NaNobj : this;
        }
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length == 1 && args[0] instanceof Scriptable p) {
            Class<?> c = this.getClassObject();
            do {
                if (p instanceof Wrapper) {
                    Object o = ((Wrapper) p).unwrap();
                    if (c.isInstance(o)) {
                        return p;
                    }
                }
                p = p.getPrototype(cx);
            } while (p != null);
        }
        return this.construct(cx, scope, args);
    }

    @Override
    public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
        Class<?> classObject = this.getClassObject();
        int modifiers = classObject.getModifiers();
        if (!Modifier.isInterface(modifiers) && !Modifier.isAbstract(modifiers)) {
            NativeJavaMethod ctors = this.members.ctors;
            int index = ctors.findCachedFunction(cx, args);
            if (index < 0) {
                String sig = NativeJavaMethod.scriptSignature(args);
                throw Context.reportRuntimeError2("msg.no.java.ctor", classObject.getName(), sig, cx);
            } else {
                return constructSpecific(cx, scope, args, ctors.methods[index]);
            }
        } else if (args.length == 0) {
            throw Context.reportRuntimeError0("msg.adapter.zero.args", cx);
        } else {
            Scriptable topLevel = ScriptableObject.getTopLevelScope(this);
            String msg = "";
            try {
                if ("Dalvik".equals(System.getProperty("java.vm.name")) && classObject.isInterface()) {
                    Object obj = createInterfaceAdapter(cx, classObject, ScriptableObject.ensureScriptableObject(args[0], cx));
                    return cx.getWrapFactory().wrapAsJavaObject(cx, scope, obj, null);
                }
                Object v = topLevel.get(cx, "JavaAdapter", topLevel);
                if (v != NOT_FOUND) {
                    Function f = (Function) v;
                    Object[] adapterArgs = new Object[] { this, args[0] };
                    return f.construct(cx, topLevel, adapterArgs);
                }
            } catch (Exception var11) {
                String m = var11.getMessage();
                if (m != null) {
                    msg = m;
                }
            }
            throw Context.reportRuntimeError2("msg.cant.instantiate", msg, classObject.getName(), cx);
        }
    }

    public String toString() {
        return "[JavaClass " + this.getClassObject().getName() + "]";
    }

    @Override
    public boolean hasInstance(Context cx, Scriptable value) {
        if (value instanceof Wrapper && !(value instanceof NativeJavaClass)) {
            Object instance = ((Wrapper) value).unwrap();
            return this.getClassObject().isInstance(instance);
        } else {
            return false;
        }
    }
}