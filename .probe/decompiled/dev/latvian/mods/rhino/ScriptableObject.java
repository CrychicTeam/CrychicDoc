package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.annotations.JSConstructor;
import dev.latvian.mods.rhino.annotations.JSFunction;
import dev.latvian.mods.rhino.annotations.JSGetter;
import dev.latvian.mods.rhino.annotations.JSSetter;
import dev.latvian.mods.rhino.annotations.JSStaticFunction;
import dev.latvian.mods.rhino.mod.util.WrappedReflectionMethod;
import dev.latvian.mods.rhino.util.Deletable;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class ScriptableObject implements Scriptable, SymbolScriptable, ConstProperties {

    public static final int EMPTY = 0;

    public static final int READONLY = 1;

    public static final int DONTENUM = 2;

    public static final int PERMANENT = 4;

    public static final int UNINITIALIZED_CONST = 8;

    public static final int CONST = 13;

    private static final WrappedExecutable GET_ARRAY_LENGTH = (cx, scope, self, args) -> ((ScriptableObject) self).getExternalArrayLength();

    private static final Comparator<Object> KEY_COMPARATOR = new ScriptableObject.KeyComparator();

    private final transient SlotMapContainer slotMap;

    private Scriptable prototypeObject;

    private Scriptable parentScopeObject;

    private transient ExternalArrayData externalData;

    private volatile Map<Object, Object> associatedValues;

    private boolean isExtensible = true;

    private boolean isSealed = false;

    protected static ScriptableObject buildDataDescriptor(Scriptable scope, Object value, int attributes, Context cx) {
        ScriptableObject desc = new NativeObject(cx);
        ScriptRuntime.setBuiltinProtoAndParent(cx, scope, desc, TopLevel.Builtins.Object);
        desc.defineProperty(cx, "value", value, 0);
        desc.defineProperty(cx, "writable", (attributes & 1) == 0, 0);
        desc.defineProperty(cx, "enumerable", (attributes & 2) == 0, 0);
        desc.defineProperty(cx, "configurable", (attributes & 4) == 0, 0);
        return desc;
    }

    static void checkValidAttributes(int attributes) {
        int mask = 15;
        if ((attributes & -16) != 0) {
            throw new IllegalArgumentException(String.valueOf(attributes));
        }
    }

    private static SlotMapContainer createSlotMap(int initialSize) {
        return new SlotMapContainer(initialSize);
    }

    public static Object getDefaultValue(Scriptable object, Class<?> typeHint, Context cx) {
        for (int i = 0; i < 2; i++) {
            boolean tryToString;
            if (typeHint == ScriptRuntime.StringClass) {
                tryToString = i == 0;
            } else {
                tryToString = i == 1;
            }
            String methodName;
            if (tryToString) {
                methodName = "toString";
            } else {
                methodName = "valueOf";
            }
            if (getProperty(object, methodName, cx) instanceof Function fun) {
                Object var10 = fun.call(cx, fun.getParentScope(), object, ScriptRuntime.EMPTY_OBJECTS);
                if (var10 != null) {
                    if (!(var10 instanceof Scriptable)) {
                        return var10;
                    }
                    if (typeHint == ScriptRuntime.ScriptableClass || typeHint == ScriptRuntime.FunctionClass) {
                        return var10;
                    }
                    if (tryToString && var10 instanceof Wrapper) {
                        Object u = ((Wrapper) var10).unwrap();
                        if (u instanceof String) {
                            return u;
                        }
                    }
                }
            }
        }
        String arg = typeHint == null ? "undefined" : typeHint.getName();
        throw ScriptRuntime.typeError1(cx, "msg.default.value", arg);
    }

    public static <T extends Scriptable> void defineClass(Scriptable scope, Class<T> clazz, Context cx) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        defineClass(scope, clazz, false, false, cx);
    }

    public static <T extends Scriptable> void defineClass(Scriptable scope, Class<T> clazz, boolean sealed, Context cx) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        defineClass(scope, clazz, sealed, false, cx);
    }

    public static <T extends Scriptable> String defineClass(Scriptable scope, Class<T> clazz, boolean sealed, boolean mapInheritance, Context cx) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        BaseFunction ctor = buildClassCtor(scope, clazz, sealed, mapInheritance, cx);
        if (ctor == null) {
            return null;
        } else {
            String name = ctor.getClassPrototype(cx).getClassName();
            defineProperty(scope, name, ctor, 2, cx);
            return name;
        }
    }

    static <T extends Scriptable> BaseFunction buildClassCtor(Scriptable scope, Class<T> clazz, boolean sealed, boolean mapInheritance, Context cx) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Method[] methods = FunctionObject.getMethodList(clazz);
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.getName().equals("init")) {
                Class<?>[] parmTypes = method.getParameterTypes();
                if (parmTypes.length == 3 && parmTypes[0] == ScriptRuntime.ContextClass && parmTypes[1] == ScriptRuntime.ScriptableClass && parmTypes[2] == boolean.class && Modifier.isStatic(method.getModifiers())) {
                    Object[] args = new Object[] { cx, scope, sealed ? Boolean.TRUE : Boolean.FALSE };
                    method.invoke(null, args);
                    return null;
                }
                if (parmTypes.length == 1 && parmTypes[0] == ScriptRuntime.ScriptableClass && Modifier.isStatic(method.getModifiers())) {
                    Object[] args = new Object[] { scope };
                    method.invoke(null, args);
                    return null;
                }
            }
        }
        Constructor<?>[] ctors = clazz.getConstructors();
        Constructor<?> protoCtor = null;
        for (int ix = 0; ix < ctors.length; ix++) {
            if (ctors[ix].getParameterTypes().length == 0) {
                protoCtor = ctors[ix];
                break;
            }
        }
        if (protoCtor == null) {
            throw Context.reportRuntimeError1("msg.zero.arg.ctor", clazz.getName(), cx);
        } else {
            Scriptable proto = (Scriptable) protoCtor.newInstance(ScriptRuntime.EMPTY_OBJECTS);
            String className = proto.getClassName();
            Object existing = getProperty(getTopLevelScope(scope), className, cx);
            if (existing instanceof BaseFunction) {
                Object existingProto = ((BaseFunction) existing).getPrototypeProperty(cx);
                if (existingProto != null && clazz.equals(existingProto.getClass())) {
                    return (BaseFunction) existing;
                }
            }
            Scriptable superProto = null;
            if (mapInheritance) {
                Class<? super T> superClass = clazz.getSuperclass();
                if (ScriptRuntime.ScriptableClass.isAssignableFrom(superClass) && !Modifier.isAbstract(superClass.getModifiers())) {
                    Class<? extends Scriptable> superScriptable = extendsScriptable(superClass);
                    String name = defineClass(scope, (Class<T>) superScriptable, sealed, mapInheritance, cx);
                    if (name != null) {
                        superProto = getClassPrototype(scope, name, cx);
                    }
                }
            }
            if (superProto == null) {
                superProto = getObjectPrototype(scope, cx);
            }
            proto.setPrototype(superProto);
            String functionPrefix = "jsFunction_";
            String staticFunctionPrefix = "jsStaticFunction_";
            String getterPrefix = "jsGet_";
            String setterPrefix = "jsSet_";
            String ctorName = "jsConstructor";
            Member ctorMember = findAnnotatedMember(methods, JSConstructor.class);
            if (ctorMember == null) {
                ctorMember = findAnnotatedMember(ctors, JSConstructor.class);
            }
            if (ctorMember == null) {
                ctorMember = FunctionObject.findSingleMethod(methods, "jsConstructor", cx);
            }
            if (ctorMember == null) {
                if (ctors.length == 1) {
                    ctorMember = ctors[0];
                } else if (ctors.length == 2) {
                    if (ctors[0].getParameterTypes().length == 0) {
                        ctorMember = ctors[1];
                    } else if (ctors[1].getParameterTypes().length == 0) {
                        ctorMember = ctors[0];
                    }
                }
                if (ctorMember == null) {
                    throw Context.reportRuntimeError1("msg.ctor.multiple.parms", clazz.getName(), cx);
                }
            }
            FunctionObject ctor = new FunctionObject(className, ctorMember, scope, cx);
            if (ctor.isVarArgsMethod()) {
                throw Context.reportRuntimeError1("msg.varargs.ctor", ctorMember.getName(), cx);
            } else {
                ctor.initAsConstructor(scope, proto, cx);
                Method finishInit = null;
                HashSet<String> staticNames = new HashSet();
                HashSet<String> instanceNames = new HashSet();
                for (Method method : methods) {
                    if (method != ctorMember) {
                        String name = method.getName();
                        if (name.equals("finishInit")) {
                            Class<?>[] parmTypesx = method.getParameterTypes();
                            if (parmTypesx.length == 3 && parmTypesx[0] == ScriptRuntime.ScriptableClass && parmTypesx[1] == FunctionObject.class && parmTypesx[2] == ScriptRuntime.ScriptableClass && Modifier.isStatic(method.getModifiers())) {
                                finishInit = method;
                                continue;
                            }
                        }
                        if (name.indexOf(36) == -1 && !name.equals("jsConstructor")) {
                            Annotation annotation = null;
                            String prefix = null;
                            if (method.isAnnotationPresent(JSFunction.class)) {
                                annotation = method.getAnnotation(JSFunction.class);
                            } else if (method.isAnnotationPresent(JSStaticFunction.class)) {
                                annotation = method.getAnnotation(JSStaticFunction.class);
                            } else if (method.isAnnotationPresent(JSGetter.class)) {
                                annotation = method.getAnnotation(JSGetter.class);
                            } else if (method.isAnnotationPresent(JSSetter.class)) {
                                continue;
                            }
                            if (annotation == null) {
                                if (name.startsWith("jsFunction_")) {
                                    prefix = "jsFunction_";
                                } else if (name.startsWith("jsStaticFunction_")) {
                                    prefix = "jsStaticFunction_";
                                } else {
                                    if (!name.startsWith("jsGet_")) {
                                        continue;
                                    }
                                    prefix = "jsGet_";
                                }
                            }
                            boolean isStatic = annotation instanceof JSStaticFunction || prefix == "jsStaticFunction_";
                            HashSet<String> names = isStatic ? staticNames : instanceNames;
                            String propName = getPropertyName(name, prefix, annotation);
                            if (names.contains(propName)) {
                                throw Context.reportRuntimeError2("duplicate.defineClass.name", name, propName, cx);
                            }
                            names.add(propName);
                            if (!(annotation instanceof JSGetter) && prefix != "jsGet_") {
                                if (isStatic && !Modifier.isStatic(method.getModifiers())) {
                                    throw Context.reportRuntimeError("jsStaticFunction must be used with static method.", cx);
                                }
                                FunctionObject f = new FunctionObject(propName, method, proto, cx);
                                if (f.isVarArgsConstructor()) {
                                    throw Context.reportRuntimeError1("msg.varargs.fun", ctorMember.getName(), cx);
                                }
                                defineProperty((Scriptable) (isStatic ? ctor : proto), propName, f, 2, cx);
                                if (sealed) {
                                    f.sealObject(cx);
                                }
                            } else {
                                if (!(proto instanceof ScriptableObject)) {
                                    throw Context.reportRuntimeError2("msg.extend.scriptable", proto.getClass().toString(), propName, cx);
                                }
                                Method setter = findSetterMethod(methods, propName, "jsSet_");
                                int attr = 6 | (setter != null ? 0 : 1);
                                ((ScriptableObject) proto).defineProperty(cx, propName, null, WrappedReflectionMethod.of(method), WrappedReflectionMethod.of(setter), attr);
                            }
                        }
                    }
                }
                if (finishInit != null) {
                    Object[] finishArgs = new Object[] { scope, ctor, proto };
                    finishInit.invoke(null, finishArgs);
                }
                if (sealed) {
                    ctor.sealObject(cx);
                    if (proto instanceof ScriptableObject) {
                        ((ScriptableObject) proto).sealObject(cx);
                    }
                }
                return ctor;
            }
        }
    }

    private static Member findAnnotatedMember(AccessibleObject[] members, Class<? extends Annotation> annotation) {
        for (AccessibleObject member : members) {
            if (member.isAnnotationPresent(annotation)) {
                return (Member) member;
            }
        }
        return null;
    }

    private static Method findSetterMethod(Method[] methods, String name, String prefix) {
        String newStyleName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        for (Method method : methods) {
            JSSetter annotation = (JSSetter) method.getAnnotation(JSSetter.class);
            if (annotation != null && (name.equals(annotation.value()) || "".equals(annotation.value()) && newStyleName.equals(method.getName()))) {
                return method;
            }
        }
        String oldStyleName = prefix + name;
        for (Method methodx : methods) {
            if (oldStyleName.equals(methodx.getName())) {
                return methodx;
            }
        }
        return null;
    }

    private static String getPropertyName(String methodName, String prefix, Annotation annotation) {
        if (prefix != null) {
            return methodName.substring(prefix.length());
        } else {
            String propName = null;
            if (annotation instanceof JSGetter) {
                propName = ((JSGetter) annotation).value();
                if ((propName == null || propName.length() == 0) && methodName.length() > 3 && methodName.startsWith("get")) {
                    propName = methodName.substring(3);
                    if (Character.isUpperCase(propName.charAt(0))) {
                        if (propName.length() == 1) {
                            propName = propName.toLowerCase();
                        } else if (!Character.isUpperCase(propName.charAt(1))) {
                            propName = Character.toLowerCase(propName.charAt(0)) + propName.substring(1);
                        }
                    }
                }
            } else if (annotation instanceof JSFunction) {
                propName = ((JSFunction) annotation).value();
            } else if (annotation instanceof JSStaticFunction) {
                propName = ((JSStaticFunction) annotation).value();
            }
            if (propName == null || propName.length() == 0) {
                propName = methodName;
            }
            return propName;
        }
    }

    private static <T extends Scriptable> Class<T> extendsScriptable(Class<?> c) {
        return (Class<T>) (ScriptRuntime.ScriptableClass.isAssignableFrom(c) ? c : null);
    }

    public static void defineProperty(Scriptable destination, String propertyName, Object value, int attributes, Context cx) {
        if (destination instanceof ScriptableObject so) {
            so.defineProperty(cx, propertyName, value, attributes);
        } else {
            destination.put(cx, propertyName, destination, value);
        }
    }

    public static void defineConstProperty(Scriptable destination, String propertyName, Context cx) {
        if (destination instanceof ConstProperties cp) {
            cp.defineConst(cx, propertyName, destination);
        } else {
            defineProperty(destination, propertyName, Undefined.instance, 13, cx);
        }
    }

    protected static boolean isTrue(Object value, Context cx) {
        return value != NOT_FOUND && ScriptRuntime.toBoolean(cx, value);
    }

    protected static boolean isFalse(Object value, Context cx) {
        return !isTrue(value, cx);
    }

    protected static Scriptable ensureScriptable(Object arg, Context cx) {
        if (!(arg instanceof Scriptable)) {
            throw ScriptRuntime.typeError1(cx, "msg.arg.not.object", ScriptRuntime.typeof(cx, arg));
        } else {
            return (Scriptable) arg;
        }
    }

    protected static SymbolScriptable ensureSymbolScriptable(Object arg, Context cx) {
        if (!(arg instanceof SymbolScriptable)) {
            throw ScriptRuntime.typeError1(cx, "msg.object.not.symbolscriptable", ScriptRuntime.typeof(cx, arg));
        } else {
            return (SymbolScriptable) arg;
        }
    }

    protected static ScriptableObject ensureScriptableObject(Object arg, Context cx) {
        if (!(arg instanceof ScriptableObject)) {
            throw ScriptRuntime.typeError1(cx, "msg.arg.not.object", ScriptRuntime.typeof(cx, arg));
        } else {
            return (ScriptableObject) arg;
        }
    }

    public static Scriptable getObjectPrototype(Scriptable scope, Context cx) {
        return TopLevel.getBuiltinPrototype(getTopLevelScope(scope), TopLevel.Builtins.Object, cx);
    }

    public static Scriptable getFunctionPrototype(Scriptable scope, Context cx) {
        return TopLevel.getBuiltinPrototype(getTopLevelScope(scope), TopLevel.Builtins.Function, cx);
    }

    public static Scriptable getGeneratorFunctionPrototype(Scriptable scope, Context cx) {
        return TopLevel.getBuiltinPrototype(getTopLevelScope(scope), TopLevel.Builtins.GeneratorFunction, cx);
    }

    public static Scriptable getArrayPrototype(Scriptable scope, Context cx) {
        return TopLevel.getBuiltinPrototype(getTopLevelScope(scope), TopLevel.Builtins.Array, cx);
    }

    public static Scriptable getClassPrototype(Scriptable scope, String className, Context cx) {
        scope = getTopLevelScope(scope);
        Object ctor = getProperty(scope, className, cx);
        Object proto;
        if (ctor instanceof BaseFunction) {
            proto = ((BaseFunction) ctor).getPrototypeProperty(cx);
        } else {
            if (!(ctor instanceof Scriptable ctorObj)) {
                return null;
            }
            proto = ctorObj.get(cx, "prototype", ctorObj);
        }
        return proto instanceof Scriptable ? (Scriptable) proto : null;
    }

    public static Scriptable getTopLevelScope(Scriptable obj) {
        while (true) {
            Scriptable parent = obj.getParentScope();
            if (parent == null) {
                return obj;
            }
            obj = parent;
        }
    }

    public static Object getProperty(Scriptable obj, String name, Context cx) {
        Scriptable start = obj;
        Object result;
        do {
            result = obj.get(cx, name, start);
            if (result != NOT_FOUND) {
                break;
            }
            obj = obj.getPrototype(cx);
        } while (obj != null);
        return result;
    }

    public static Object getProperty(Scriptable obj, Symbol key, Context cx) {
        Scriptable start = obj;
        Object result;
        do {
            result = ensureSymbolScriptable(obj, cx).get(cx, key, start);
            if (result != NOT_FOUND) {
                break;
            }
            obj = obj.getPrototype(cx);
        } while (obj != null);
        return result;
    }

    public static Object getProperty(Scriptable obj, int index, Context cx) {
        Scriptable start = obj;
        Object result;
        do {
            result = obj.get(cx, index, start);
            if (result != NOT_FOUND) {
                break;
            }
            obj = obj.getPrototype(cx);
        } while (obj != null);
        return result;
    }

    public static boolean hasProperty(Scriptable obj, String name, Context cx) {
        return null != getBase(obj, name, cx);
    }

    public static void redefineProperty(Scriptable obj, String name, boolean isConst, Context cx) {
        Scriptable base = getBase(obj, name, cx);
        if (base != null) {
            if (base instanceof ConstProperties cp && cp.isConst(name)) {
                throw ScriptRuntime.typeError1(cx, "msg.const.redecl", name);
            }
            if (isConst) {
                throw ScriptRuntime.typeError1(cx, "msg.var.redecl", name);
            }
        }
    }

    public static boolean hasProperty(Scriptable obj, int index, Context cx) {
        return null != getBase(cx, obj, index);
    }

    public static boolean hasProperty(Scriptable obj, Symbol key, Context cx) {
        return null != getBase(cx, obj, key);
    }

    public static void putProperty(Scriptable obj, String name, Object value, Context cx) {
        Scriptable base = getBase(obj, name, cx);
        if (base == null) {
            base = obj;
        }
        base.put(cx, name, obj, value);
    }

    public static void putProperty(Scriptable obj, Symbol key, Object value, Context cx) {
        Scriptable base = getBase(cx, obj, key);
        if (base == null) {
            base = obj;
        }
        ensureSymbolScriptable(base, cx).put(cx, key, obj, value);
    }

    public static void putConstProperty(Scriptable obj, String name, Object value, Context cx) {
        Scriptable base = getBase(obj, name, cx);
        if (base == null) {
            base = obj;
        }
        if (base instanceof ConstProperties) {
            ((ConstProperties) base).putConst(cx, name, obj, value);
        }
    }

    public static void putProperty(Scriptable obj, int index, Object value, Context cx) {
        Scriptable base = getBase(cx, obj, index);
        if (base == null) {
            base = obj;
        }
        base.put(cx, index, obj, value);
    }

    public static boolean deleteProperty(Scriptable obj, String name, Context cx) {
        Scriptable base = getBase(obj, name, cx);
        if (base == null) {
            return true;
        } else {
            base.delete(cx, name);
            return !base.has(cx, name, obj);
        }
    }

    public static boolean deleteProperty(Scriptable obj, int index, Context cx) {
        Scriptable base = getBase(cx, obj, index);
        if (base == null) {
            return true;
        } else {
            base.delete(cx, index);
            return !base.has(cx, index, obj);
        }
    }

    public static Object[] getPropertyIds(Context cx, Scriptable obj) {
        if (obj == null) {
            return ScriptRuntime.EMPTY_OBJECTS;
        } else {
            Object[] result = obj.getIds(cx);
            ObjToIntMap map = null;
            while (true) {
                obj = obj.getPrototype(cx);
                if (obj == null) {
                    if (map != null) {
                        result = map.getKeys();
                    }
                    return result;
                }
                Object[] ids = obj.getIds(cx);
                if (ids.length != 0) {
                    if (map == null) {
                        if (result.length == 0) {
                            result = ids;
                            continue;
                        }
                        map = new ObjToIntMap(result.length + ids.length);
                        for (int i = 0; i != result.length; i++) {
                            map.intern(result[i]);
                        }
                        result = null;
                    }
                    for (int i = 0; i != ids.length; i++) {
                        map.intern(ids[i]);
                    }
                }
            }
        }
    }

    private static Scriptable getBase(Scriptable obj, String name, Context cx) {
        while (!obj.has(cx, name, obj)) {
            obj = obj.getPrototype(cx);
            if (obj != null) {
                continue;
            }
            break;
        }
        return obj;
    }

    private static Scriptable getBase(Context cx, Scriptable obj, int index) {
        while (!obj.has(cx, index, obj)) {
            obj = obj.getPrototype(cx);
            if (obj != null) {
                continue;
            }
            break;
        }
        return obj;
    }

    private static Scriptable getBase(Context cx, Scriptable obj, Symbol key) {
        while (!ensureSymbolScriptable(obj, cx).has(cx, key, obj)) {
            obj = obj.getPrototype(cx);
            if (obj != null) {
                continue;
            }
            break;
        }
        return obj;
    }

    public static Object getTopScopeValue(Scriptable scope, Object key, Context cx) {
        scope = getTopLevelScope(scope);
        do {
            if (scope instanceof ScriptableObject so) {
                Object value = so.getAssociatedValue(key);
                if (value != null) {
                    return value;
                }
            }
            scope = scope.getPrototype(cx);
        } while (scope != null);
        return null;
    }

    public ScriptableObject() {
        this.slotMap = createSlotMap(0);
    }

    public ScriptableObject(Scriptable scope, Scriptable prototype) {
        if (scope == null) {
            throw new IllegalArgumentException();
        } else {
            this.parentScopeObject = scope;
            this.prototypeObject = prototype;
            this.slotMap = createSlotMap(0);
        }
    }

    @Override
    public MemberType getTypeOf() {
        return this.avoidObjectDetection() ? MemberType.UNDEFINED : MemberType.OBJECT;
    }

    @Override
    public abstract String getClassName();

    @Override
    public boolean has(Context cx, String name, Scriptable start) {
        return null != this.slotMap.query(name, 0);
    }

    @Override
    public boolean has(Context cx, int index, Scriptable start) {
        return this.externalData != null ? index < this.externalData.getArrayLength() : null != this.slotMap.query(null, index);
    }

    @Override
    public boolean has(Context cx, Symbol key, Scriptable start) {
        return null != this.slotMap.query(key, 0);
    }

    @Override
    public Object get(Context cx, String name, Scriptable start) {
        ScriptableObject.Slot slot = this.slotMap.query(name, 0);
        return slot == null ? NOT_FOUND : slot.getValue(start, cx);
    }

    @Override
    public Object get(Context cx, int index, Scriptable start) {
        if (this.externalData != null) {
            return index < this.externalData.getArrayLength() ? this.externalData.getArrayElement(index) : NOT_FOUND;
        } else {
            ScriptableObject.Slot slot = this.slotMap.query(null, index);
            return slot == null ? NOT_FOUND : slot.getValue(start, cx);
        }
    }

    @Override
    public Object get(Context cx, Symbol key, Scriptable start) {
        ScriptableObject.Slot slot = this.slotMap.query(key, 0);
        return slot == null ? NOT_FOUND : slot.getValue(start, cx);
    }

    @Override
    public void put(Context cx, String name, Scriptable start, Object value) {
        if (!this.putImpl(cx, name, 0, start, value)) {
            if (start == this) {
                throw Kit.codeBug();
            } else {
                start.put(cx, name, start, value);
            }
        }
    }

    @Override
    public void put(Context cx, int index, Scriptable start, Object value) {
        if (this.externalData != null) {
            if (index < this.externalData.getArrayLength()) {
                this.externalData.setArrayElement(index, value);
            } else {
                throw new JavaScriptException(cx, ScriptRuntime.newNativeError(cx, this, TopLevel.NativeErrors.RangeError, new Object[] { "External array index out of bounds " }), null, 0);
            }
        } else if (!this.putImpl(cx, null, index, start, value)) {
            if (start == this) {
                throw Kit.codeBug();
            } else {
                start.put(cx, index, start, value);
            }
        }
    }

    @Override
    public void put(Context cx, Symbol key, Scriptable start, Object value) {
        if (!this.putImpl(cx, key, 0, start, value)) {
            if (start == this) {
                throw Kit.codeBug();
            } else {
                ensureSymbolScriptable(start, cx).put(cx, key, start, value);
            }
        }
    }

    @Override
    public void delete(Context cx, String name) {
        this.checkNotSealed(cx, name, 0);
        ScriptableObject.Slot s = this.slotMap.query(name, 0);
        this.slotMap.remove(name, 0, cx);
        Deletable.deleteObject(s == null ? null : s.value);
    }

    @Override
    public void delete(Context cx, int index) {
        this.checkNotSealed(cx, null, index);
        ScriptableObject.Slot s = this.slotMap.query(null, index);
        this.slotMap.remove(null, index, cx);
        Deletable.deleteObject(s == null ? null : s.value);
    }

    @Override
    public void delete(Context cx, Symbol key) {
        this.checkNotSealed(cx, key, 0);
        this.slotMap.remove(key, 0, cx);
    }

    @Override
    public void putConst(Context cx, String name, Scriptable start, Object value) {
        if (!this.putConstImpl(cx, name, 0, start, value, 1)) {
            if (start == this) {
                throw Kit.codeBug();
            } else {
                if (start instanceof ConstProperties) {
                    ((ConstProperties) start).putConst(cx, name, start, value);
                } else {
                    start.put(cx, name, start, value);
                }
            }
        }
    }

    @Override
    public void defineConst(Context cx, String name, Scriptable start) {
        if (!this.putConstImpl(cx, name, 0, start, Undefined.instance, 8)) {
            if (start == this) {
                throw Kit.codeBug();
            } else {
                if (start instanceof ConstProperties) {
                    ((ConstProperties) start).defineConst(cx, name, start);
                }
            }
        }
    }

    @Override
    public boolean isConst(String name) {
        ScriptableObject.Slot slot = this.slotMap.query(name, 0);
        return slot == null ? false : (slot.getAttributes() & 5) == 5;
    }

    public int getAttributes(Context cx, String name) {
        return this.findAttributeSlot(cx, name, 0, ScriptableObject.SlotAccess.QUERY).getAttributes();
    }

    public int getAttributes(Context cx, int index) {
        return this.findAttributeSlot(cx, null, index, ScriptableObject.SlotAccess.QUERY).getAttributes();
    }

    public int getAttributes(Context cx, Symbol sym) {
        return this.findAttributeSlot(cx, sym, ScriptableObject.SlotAccess.QUERY).getAttributes();
    }

    public void setAttributes(Context cx, String name, int attributes) {
        this.checkNotSealed(cx, name, 0);
        this.findAttributeSlot(cx, name, 0, ScriptableObject.SlotAccess.MODIFY).setAttributes(attributes);
    }

    public void setAttributes(Context cx, int index, int attributes) {
        this.checkNotSealed(cx, null, index);
        this.findAttributeSlot(cx, null, index, ScriptableObject.SlotAccess.MODIFY).setAttributes(attributes);
    }

    public void setAttributes(Context cx, Symbol key, int attributes) {
        this.checkNotSealed(cx, key, 0);
        this.findAttributeSlot(cx, key, ScriptableObject.SlotAccess.MODIFY).setAttributes(attributes);
    }

    public void setGetterOrSetter(Context cx, String name, int index, Callable getterOrSetter, boolean isSetter) {
        this.setGetterOrSetter(cx, name, index, getterOrSetter, isSetter, false);
    }

    private void setGetterOrSetter(Context cx, String name, int index, Callable getterOrSetter, boolean isSetter, boolean force) {
        if (name != null && index != 0) {
            throw new IllegalArgumentException(name);
        } else {
            if (!force) {
                this.checkNotSealed(cx, name, index);
            }
            if (this.isExtensible()) {
                gslot = (ScriptableObject.GetterSlot) this.slotMap.get(name, index, ScriptableObject.SlotAccess.MODIFY_GETTER_SETTER);
            } else if (!(this.slotMap.query(name, index) instanceof ScriptableObject.GetterSlot gslot)) {
                return;
            }
            if (!force) {
                int attributes = gslot.getAttributes();
                if ((attributes & 1) != 0) {
                    throw Context.reportRuntimeError1("msg.modify.readonly", name, cx);
                }
            }
            if (isSetter) {
                gslot.setter = getterOrSetter;
            } else {
                gslot.getter = getterOrSetter;
            }
            gslot.value = Undefined.instance;
        }
    }

    public Object getGetterOrSetter(String name, int index, boolean isSetter) {
        if (name != null && index != 0) {
            throw new IllegalArgumentException(name);
        } else {
            ScriptableObject.Slot slot = this.slotMap.query(name, index);
            if (slot == null) {
                return null;
            } else if (slot instanceof ScriptableObject.GetterSlot gslot) {
                Object result = isSetter ? gslot.setter : gslot.getter;
                return result != null ? result : Undefined.instance;
            } else {
                return Undefined.instance;
            }
        }
    }

    protected boolean isGetterOrSetter(String name, int index, boolean setter) {
        ScriptableObject.Slot slot = this.slotMap.query(name, index);
        if (slot instanceof ScriptableObject.GetterSlot) {
            return setter && ((ScriptableObject.GetterSlot) slot).setter != null ? true : !setter && ((ScriptableObject.GetterSlot) slot).getter != null;
        } else {
            return false;
        }
    }

    public ExternalArrayData getExternalArrayData() {
        return this.externalData;
    }

    public void setExternalArrayData(Context cx, ExternalArrayData array) {
        this.externalData = array;
        if (array == null) {
            this.delete(cx, "length");
        } else {
            this.defineProperty(cx, "length", null, GET_ARRAY_LENGTH, null, 3);
        }
    }

    public Object getExternalArrayLength() {
        return this.externalData == null ? 0 : this.externalData.getArrayLength();
    }

    @Override
    public Scriptable getPrototype(Context cx) {
        return this.prototypeObject;
    }

    @Override
    public void setPrototype(Scriptable m) {
        this.prototypeObject = m;
    }

    @Override
    public Scriptable getParentScope() {
        return this.parentScopeObject;
    }

    @Override
    public void setParentScope(Scriptable m) {
        this.parentScopeObject = m;
    }

    @Override
    public Object[] getIds(Context cx) {
        return this.getIds(cx, false, false);
    }

    @Override
    public Object[] getAllIds(Context cx) {
        return this.getIds(cx, true, false);
    }

    @Override
    public Object getDefaultValue(Context cx, Class<?> typeHint) {
        return getDefaultValue(this, typeHint, cx);
    }

    @Override
    public boolean hasInstance(Context cx, Scriptable instance) {
        return ScriptRuntime.jsDelegatesTo(cx, instance, this);
    }

    public boolean avoidObjectDetection() {
        return false;
    }

    protected Object equivalentValues(Object value) {
        return this == value ? Boolean.TRUE : NOT_FOUND;
    }

    public void defineProperty(Context cx, String propertyName, Object value, int attributes) {
        this.checkNotSealed(cx, propertyName, 0);
        this.put(cx, propertyName, this, value);
        this.setAttributes(cx, propertyName, attributes);
    }

    public void defineProperty(Context cx, Symbol key, Object value, int attributes) {
        this.checkNotSealed(cx, key, 0);
        this.put(cx, key, this, value);
        this.setAttributes(cx, key, attributes);
    }

    public void defineProperty(Context cx, String propertyName, Class<?> clazz, int attributes) {
        int length = propertyName.length();
        if (length == 0) {
            throw new IllegalArgumentException();
        } else {
            char[] buf = new char[3 + length];
            propertyName.getChars(0, length, buf, 3);
            buf[3] = Character.toUpperCase(buf[3]);
            buf[0] = 'g';
            buf[1] = 'e';
            buf[2] = 't';
            String getterName = new String(buf);
            buf[0] = 's';
            String setterName = new String(buf);
            Method[] methods = FunctionObject.getMethodList(clazz);
            WrappedExecutable getter = WrappedReflectionMethod.of(FunctionObject.findSingleMethod(methods, getterName, cx));
            WrappedExecutable setter = WrappedReflectionMethod.of(FunctionObject.findSingleMethod(methods, setterName, cx));
            if (setter == null) {
                attributes |= 1;
            }
            this.defineProperty(cx, propertyName, null, getter, setter, attributes);
        }
    }

    public void defineProperty(Context cx, String propertyName, Object delegateTo, WrappedExecutable getter, WrappedExecutable setter, int attributes) {
        MemberBox getterBox = null;
        if (getter != null) {
            getterBox = new MemberBox(getter);
            if (!getter.isStatic()) {
                getterBox.delegateTo = delegateTo;
            } else {
                getterBox.delegateTo = void.class;
            }
        }
        MemberBox setterBox = null;
        if (setter != null) {
            if (setter.getReturnType() != void.class) {
                throw Context.reportRuntimeError1("msg.setter.return", setter.toString(), cx);
            }
            setterBox = new MemberBox(setter);
            if (!setter.isStatic()) {
                setterBox.delegateTo = delegateTo;
            } else {
                setterBox.delegateTo = void.class;
            }
        }
        ScriptableObject.GetterSlot gslot = (ScriptableObject.GetterSlot) this.slotMap.get(propertyName, 0, ScriptableObject.SlotAccess.MODIFY_GETTER_SETTER);
        gslot.setAttributes(attributes);
        gslot.getter = getterBox;
        gslot.setter = setterBox;
    }

    public void defineOwnProperties(Context cx, ScriptableObject props) {
        Object[] ids = props.getIds(cx, false, true);
        ScriptableObject[] descs = new ScriptableObject[ids.length];
        int i = 0;
        for (int len = ids.length; i < len; i++) {
            Object descObj = ScriptRuntime.getObjectElem(cx, props, ids[i]);
            ScriptableObject desc = ensureScriptableObject(descObj, cx);
            this.checkPropertyDefinition(cx, desc);
            descs[i] = desc;
        }
        i = 0;
        for (int len = ids.length; i < len; i++) {
            this.defineOwnProperty(cx, ids[i], descs[i]);
        }
    }

    public void defineOwnProperty(Context cx, Object id, ScriptableObject desc) {
        this.checkPropertyDefinition(cx, desc);
        this.defineOwnProperty(cx, id, desc, true);
    }

    protected void defineOwnProperty(Context cx, Object id, ScriptableObject desc, boolean checkValid) {
        ScriptableObject.Slot slot = this.getSlot(cx, id, ScriptableObject.SlotAccess.QUERY);
        boolean isNew = slot == null;
        if (checkValid) {
            ScriptableObject current = slot == null ? null : slot.getPropertyDescriptor(cx, this);
            this.checkPropertyChange(cx, id, current, desc);
        }
        boolean isAccessor = this.isAccessorDescriptor(cx, desc);
        int attributes;
        if (slot == null) {
            slot = this.getSlot(cx, id, isAccessor ? ScriptableObject.SlotAccess.MODIFY_GETTER_SETTER : ScriptableObject.SlotAccess.MODIFY);
            attributes = this.applyDescriptorToAttributeBitset(cx, 7, desc);
        } else {
            attributes = this.applyDescriptorToAttributeBitset(cx, slot.getAttributes(), desc);
        }
        if (isAccessor) {
            if (!(slot instanceof ScriptableObject.GetterSlot)) {
                slot = this.getSlot(cx, id, ScriptableObject.SlotAccess.MODIFY_GETTER_SETTER);
            }
            ScriptableObject.GetterSlot gslot = (ScriptableObject.GetterSlot) slot;
            Object getter = getProperty(desc, "get", cx);
            if (getter != NOT_FOUND) {
                gslot.getter = getter;
            }
            Object setter = getProperty(desc, "set", cx);
            if (setter != NOT_FOUND) {
                gslot.setter = setter;
            }
            gslot.value = Undefined.instance;
            gslot.setAttributes(attributes);
        } else {
            if (slot instanceof ScriptableObject.GetterSlot && this.isDataDescriptor(desc, cx)) {
                slot = this.getSlot(cx, id, ScriptableObject.SlotAccess.CONVERT_ACCESSOR_TO_DATA);
            }
            Object value = getProperty(desc, "value", cx);
            if (value != NOT_FOUND) {
                slot.value = value;
            } else if (isNew) {
                slot.value = Undefined.instance;
            }
            slot.setAttributes(attributes);
        }
    }

    protected void checkPropertyDefinition(Context cx, ScriptableObject desc) {
        Object getter = getProperty(desc, "get", cx);
        if (getter != NOT_FOUND && getter != Undefined.instance && !(getter instanceof Callable)) {
            throw ScriptRuntime.notFunctionError(cx, getter);
        } else {
            Object setter = getProperty(desc, "set", cx);
            if (setter != NOT_FOUND && setter != Undefined.instance && !(setter instanceof Callable)) {
                throw ScriptRuntime.notFunctionError(cx, setter);
            } else if (this.isDataDescriptor(desc, cx) && this.isAccessorDescriptor(cx, desc)) {
                throw ScriptRuntime.typeError0(cx, "msg.both.data.and.accessor.desc");
            }
        }
    }

    protected void checkPropertyChange(Context cx, Object id, ScriptableObject current, ScriptableObject desc) {
        if (current == null) {
            if (!this.isExtensible()) {
                throw ScriptRuntime.typeError0(cx, "msg.not.extensible");
            }
        } else if (isFalse(current.get(cx, "configurable", current), cx)) {
            if (isTrue(getProperty(desc, "configurable", cx), cx)) {
                throw ScriptRuntime.typeError1(cx, "msg.change.configurable.false.to.true", id);
            }
            if (isTrue(current.get(cx, "enumerable", current), cx) != isTrue(getProperty(desc, "enumerable", cx), cx)) {
                throw ScriptRuntime.typeError1(cx, "msg.change.enumerable.with.configurable.false", id);
            }
            boolean isData = this.isDataDescriptor(desc, cx);
            boolean isAccessor = this.isAccessorDescriptor(cx, desc);
            if (isData || isAccessor) {
                if (isData && this.isDataDescriptor(current, cx)) {
                    if (isFalse(current.get(cx, "writable", current), cx)) {
                        if (isTrue(getProperty(desc, "writable", cx), cx)) {
                            throw ScriptRuntime.typeError1(cx, "msg.change.writable.false.to.true.with.configurable.false", id);
                        }
                        if (!this.sameValue(cx, getProperty(desc, "value", cx), current.get(cx, "value", current))) {
                            throw ScriptRuntime.typeError1(cx, "msg.change.value.with.writable.false", id);
                        }
                    }
                } else {
                    if (!isAccessor || !this.isAccessorDescriptor(cx, current)) {
                        if (this.isDataDescriptor(current, cx)) {
                            throw ScriptRuntime.typeError1(cx, "msg.change.property.data.to.accessor.with.configurable.false", id);
                        }
                        throw ScriptRuntime.typeError1(cx, "msg.change.property.accessor.to.data.with.configurable.false", id);
                    }
                    if (!this.sameValue(cx, getProperty(desc, "set", cx), current.get(cx, "set", current))) {
                        throw ScriptRuntime.typeError1(cx, "msg.change.setter.with.configurable.false", id);
                    }
                    if (!this.sameValue(cx, getProperty(desc, "get", cx), current.get(cx, "get", current))) {
                        throw ScriptRuntime.typeError1(cx, "msg.change.getter.with.configurable.false", id);
                    }
                }
            }
        }
    }

    protected boolean sameValue(Context cx, Object newValue, Object currentValue) {
        if (newValue == NOT_FOUND) {
            return true;
        } else {
            if (currentValue == NOT_FOUND) {
                currentValue = Undefined.instance;
            }
            if (currentValue instanceof Number && newValue instanceof Number) {
                double d1 = ((Number) currentValue).doubleValue();
                double d2 = ((Number) newValue).doubleValue();
                if (Double.isNaN(d1) && Double.isNaN(d2)) {
                    return true;
                }
                if (d1 == 0.0 && Double.doubleToLongBits(d1) != Double.doubleToLongBits(d2)) {
                    return false;
                }
            }
            return ScriptRuntime.shallowEq(cx, currentValue, newValue);
        }
    }

    protected int applyDescriptorToAttributeBitset(Context cx, int attributes, ScriptableObject desc) {
        Object enumerable = getProperty(desc, "enumerable", cx);
        if (enumerable != NOT_FOUND) {
            attributes = ScriptRuntime.toBoolean(cx, enumerable) ? attributes & -3 : attributes | 2;
        }
        Object writable = getProperty(desc, "writable", cx);
        if (writable != NOT_FOUND) {
            attributes = ScriptRuntime.toBoolean(cx, writable) ? attributes & -2 : attributes | 1;
        }
        Object configurable = getProperty(desc, "configurable", cx);
        if (configurable != NOT_FOUND) {
            attributes = ScriptRuntime.toBoolean(cx, configurable) ? attributes & -5 : attributes | 4;
        }
        return attributes;
    }

    protected boolean isDataDescriptor(ScriptableObject desc, Context cx) {
        return hasProperty(desc, "value", cx) || hasProperty(desc, "writable", cx);
    }

    protected boolean isAccessorDescriptor(Context cx, ScriptableObject desc) {
        return hasProperty(desc, "get", cx) || hasProperty(desc, "set", cx);
    }

    protected boolean isGenericDescriptor(Context cx, ScriptableObject desc) {
        return !this.isDataDescriptor(desc, cx) && !this.isAccessorDescriptor(cx, desc);
    }

    public void defineFunctionProperties(Context cx, String[] names, Class<?> clazz, int attributes) {
        Method[] methods = FunctionObject.getMethodList(clazz);
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            Method m = FunctionObject.findSingleMethod(methods, name, cx);
            if (m == null) {
                throw Context.reportRuntimeError2("msg.method.not.found", name, clazz.getName(), cx);
            }
            FunctionObject f = new FunctionObject(name, m, this, cx);
            this.defineProperty(cx, name, f, attributes);
        }
    }

    public boolean isExtensible() {
        return this.isExtensible;
    }

    public void preventExtensions() {
        this.isExtensible = false;
    }

    public void sealObject(Context cx) {
        if (!this.isSealed) {
            long stamp = this.slotMap.readLock();
            try {
                this.isSealed = true;
            } finally {
                this.slotMap.unlockRead(stamp);
            }
        }
    }

    public final boolean isSealed(Context cx) {
        return this.isSealed;
    }

    private void checkNotSealed(Context cx, Object key, int index) {
        if (this.isSealed(cx)) {
            String str = key != null ? key.toString() : Integer.toString(index);
            throw Context.reportRuntimeError1("msg.modify.sealed", str, cx);
        }
    }

    public final Object getAssociatedValue(Object key) {
        Map<Object, Object> h = this.associatedValues;
        return h == null ? null : h.get(key);
    }

    public final synchronized Object associateValue(Object key, Object value) {
        if (value == null) {
            throw new IllegalArgumentException();
        } else {
            Map<Object, Object> h = this.associatedValues;
            if (h == null) {
                h = new HashMap();
                this.associatedValues = h;
            }
            return Kit.initHash(h, key, value);
        }
    }

    private boolean putImpl(Context cx, Object key, int index, Scriptable start, Object value) {
        ScriptableObject.Slot slot;
        if (this != start) {
            slot = this.slotMap.query(key, index);
            if (!this.isExtensible && (slot == null || !(slot instanceof ScriptableObject.GetterSlot) && (slot.getAttributes() & 1) != 0) && cx.isStrictMode()) {
                throw ScriptRuntime.typeError0(cx, "msg.not.extensible");
            }
            if (slot == null) {
                return false;
            }
        } else if (!this.isExtensible) {
            slot = this.slotMap.query(key, index);
            if ((slot == null || !(slot instanceof ScriptableObject.GetterSlot) && (slot.getAttributes() & 1) != 0) && cx.isStrictMode()) {
                throw ScriptRuntime.typeError0(cx, "msg.not.extensible");
            }
            if (slot == null) {
                return true;
            }
        } else {
            if (this.isSealed) {
                this.checkNotSealed(cx, key, index);
            }
            slot = this.slotMap.get(key, index, ScriptableObject.SlotAccess.MODIFY);
        }
        return slot.setValue(value, this, start, cx);
    }

    private boolean putConstImpl(Context cx, String name, int index, Scriptable start, Object value, int constFlag) {
        assert constFlag != 0;
        if (!this.isExtensible && cx.isStrictMode()) {
            throw ScriptRuntime.typeError0(cx, "msg.not.extensible");
        } else {
            ScriptableObject.Slot slot;
            if (this != start) {
                slot = this.slotMap.query(name, index);
                if (slot == null) {
                    return false;
                }
            } else {
                if (this.isExtensible()) {
                    this.checkNotSealed(cx, name, index);
                    slot = this.slotMap.get(name, index, ScriptableObject.SlotAccess.MODIFY_CONST);
                    int attr = slot.getAttributes();
                    if ((attr & 1) == 0) {
                        throw Context.reportRuntimeError1("msg.var.redecl", name, cx);
                    }
                    if ((attr & 8) != 0) {
                        slot.value = value;
                        if (constFlag != 8) {
                            slot.setAttributes(attr & -9);
                        }
                    }
                    return true;
                }
                slot = this.slotMap.query(name, index);
                if (slot == null) {
                    return true;
                }
            }
            return slot.setValue(value, this, start, cx);
        }
    }

    private ScriptableObject.Slot findAttributeSlot(Context cx, String name, int index, ScriptableObject.SlotAccess accessType) {
        ScriptableObject.Slot slot = this.slotMap.get(name, index, accessType);
        if (slot == null) {
            String str = name != null ? name : Integer.toString(index);
            throw Context.reportRuntimeError1("msg.prop.not.found", str, cx);
        } else {
            return slot;
        }
    }

    private ScriptableObject.Slot findAttributeSlot(Context cx, Symbol key, ScriptableObject.SlotAccess accessType) {
        ScriptableObject.Slot slot = this.slotMap.get(key, 0, accessType);
        if (slot == null) {
            throw Context.reportRuntimeError1("msg.prop.not.found", key, cx);
        } else {
            return slot;
        }
    }

    Object[] getIds(Context cx, boolean getNonEnumerable, boolean getSymbols) {
        int externalLen = this.externalData == null ? 0 : this.externalData.getArrayLength();
        Object[] a;
        if (externalLen == 0) {
            a = ScriptRuntime.EMPTY_OBJECTS;
        } else {
            a = new Object[externalLen];
            for (int i = 0; i < externalLen; i++) {
                a[i] = i;
            }
        }
        if (this.slotMap.isEmpty()) {
            return a;
        } else {
            int c = externalLen;
            long stamp = this.slotMap.readLock();
            try {
                for (ScriptableObject.Slot slot : this.slotMap) {
                    if ((getNonEnumerable || (slot.getAttributes() & 2) == 0) && (getSymbols || !(slot.name instanceof Symbol))) {
                        if (c == externalLen) {
                            Object[] oldA = a;
                            a = new Object[this.slotMap.dirtySize() + externalLen];
                            if (oldA != null) {
                                System.arraycopy(oldA, 0, a, 0, externalLen);
                            }
                        }
                        a[c++] = slot.name != null ? slot.name : slot.indexOrHash;
                    }
                }
            } finally {
                this.slotMap.unlockRead(stamp);
            }
            Object[] result;
            if (c == a.length + externalLen) {
                result = a;
            } else {
                result = new Object[c];
                System.arraycopy(a, 0, result, 0, c);
            }
            if (cx != null) {
                Arrays.sort(result, KEY_COMPARATOR);
            }
            return result;
        }
    }

    protected ScriptableObject getOwnPropertyDescriptor(Context cx, Object id) {
        ScriptableObject.Slot slot = this.getSlot(cx, id, ScriptableObject.SlotAccess.QUERY);
        if (slot == null) {
            return null;
        } else {
            Scriptable scope = this.getParentScope();
            return slot.getPropertyDescriptor(cx, (Scriptable) (scope == null ? this : scope));
        }
    }

    protected ScriptableObject.Slot getSlot(Context cx, Object id, ScriptableObject.SlotAccess accessType) {
        if (id instanceof Symbol) {
            return this.slotMap.get(id, 0, accessType);
        } else {
            ScriptRuntime.StringIdOrIndex s = ScriptRuntime.toStringIdOrIndex(cx, id);
            return s.stringId == null ? this.slotMap.get(null, s.index, accessType) : this.slotMap.get(s.stringId, 0, accessType);
        }
    }

    public int size() {
        return this.slotMap.size();
    }

    public boolean isEmpty() {
        return this.slotMap.isEmpty();
    }

    public Object get(Context cx, Object key) {
        Object value = null;
        if (key instanceof String) {
            value = this.get(cx, (String) key, this);
        } else if (key instanceof Symbol) {
            value = this.get(cx, (Symbol) key, this);
        } else if (key instanceof Number) {
            value = this.get(cx, ((Number) key).intValue(), this);
        }
        if (value == NOT_FOUND || value == Undefined.instance) {
            return null;
        } else {
            return value instanceof Wrapper ? ((Wrapper) value).unwrap() : value;
        }
    }

    static final class GetterSlot extends ScriptableObject.Slot {

        Object getter;

        Object setter;

        GetterSlot(Object name, int indexOrHash, int attributes) {
            super(name, indexOrHash, attributes);
        }

        @Override
        ScriptableObject getPropertyDescriptor(Context cx, Scriptable scope) {
            int attr = this.getAttributes();
            ScriptableObject desc = new NativeObject(cx);
            ScriptRuntime.setBuiltinProtoAndParent(cx, scope, desc, TopLevel.Builtins.Object);
            desc.defineProperty(cx, "enumerable", (attr & 2) == 0, 0);
            desc.defineProperty(cx, "configurable", (attr & 4) == 0, 0);
            if (this.getter == null && this.setter == null) {
                desc.defineProperty(cx, "writable", (attr & 1) == 0, 0);
            }
            String fName = this.name == null ? "f" : this.name.toString();
            if (this.getter != null) {
                if (this.getter instanceof MemberBox) {
                    desc.defineProperty(cx, "get", new FunctionObject(fName, ((MemberBox) this.getter).member(), scope, cx), 0);
                } else if (this.getter instanceof Member) {
                    desc.defineProperty(cx, "get", new FunctionObject(fName, (Member) this.getter, scope, cx), 0);
                } else {
                    desc.defineProperty(cx, "get", this.getter, 0);
                }
            }
            if (this.setter != null) {
                if (this.setter instanceof MemberBox) {
                    desc.defineProperty(cx, "set", new FunctionObject(fName, ((MemberBox) this.setter).member(), scope, cx), 0);
                } else if (this.setter instanceof Member) {
                    desc.defineProperty(cx, "set", new FunctionObject(fName, (Member) this.setter, scope, cx), 0);
                } else {
                    desc.defineProperty(cx, "set", this.setter, 0);
                }
            }
            return desc;
        }

        @Override
        boolean setValue(Object value, Scriptable owner, Scriptable start, Context cx) {
            if (this.setter == null) {
                if (this.getter != null) {
                    if (cx.isStrictMode()) {
                        String prop = "";
                        if (this.name != null) {
                            prop = "[" + start.getClassName() + "]." + this.name;
                        }
                        throw ScriptRuntime.typeError2(cx, "msg.set.prop.no.setter", prop, ScriptRuntime.toString(cx, value));
                    } else {
                        return true;
                    }
                } else {
                    return super.setValue(value, owner, start, cx);
                }
            } else {
                if (this.setter instanceof MemberBox nativeSetter) {
                    Class<?>[] pTypes = nativeSetter.argTypes;
                    Class<?> valueType = pTypes[pTypes.length - 1];
                    int tag = FunctionObject.getTypeTag(valueType);
                    Object actualArg = FunctionObject.convertArg(cx, start, value, tag);
                    Object setterThis;
                    Object[] args;
                    if (nativeSetter.delegateTo == null) {
                        setterThis = start;
                        args = new Object[] { actualArg };
                    } else {
                        setterThis = nativeSetter.delegateTo;
                        args = new Object[] { cx, start, actualArg };
                    }
                    nativeSetter.invoke(setterThis, args, cx, start);
                } else if (this.setter instanceof Function f) {
                    f.call(cx, f.getParentScope(), start, new Object[] { value });
                }
                return true;
            }
        }

        @Override
        Object getValue(Scriptable start, Context cx) {
            if (this.getter != null) {
                if (this.getter instanceof MemberBox nativeGetter) {
                    Object[] args;
                    Object getterThis;
                    if (nativeGetter.delegateTo == null) {
                        getterThis = start;
                        args = ScriptRuntime.EMPTY_OBJECTS;
                    } else {
                        getterThis = nativeGetter.delegateTo;
                        args = new Object[] { cx, start };
                    }
                    return nativeGetter.invoke(getterThis, args, cx, start);
                }
                if (this.getter instanceof Function f) {
                    return f.call(cx, f.getParentScope(), start, ScriptRuntime.EMPTY_OBJECTS);
                }
            }
            return this.value;
        }
    }

    public static final class KeyComparator implements Comparator<Object>, Serializable {

        private static final long serialVersionUID = 6411335891523988149L;

        public int compare(Object o1, Object o2) {
            if (o1 instanceof Integer) {
                if (o2 instanceof Integer) {
                    int i1 = (Integer) o1;
                    int i2 = (Integer) o2;
                    if (i1 < i2) {
                        return -1;
                    } else {
                        return i1 > i2 ? 1 : 0;
                    }
                } else {
                    return -1;
                }
            } else {
                return o2 instanceof Integer ? 1 : 0;
            }
        }
    }

    static class Slot {

        Object name;

        int indexOrHash;

        Object value;

        transient ScriptableObject.Slot next;

        transient ScriptableObject.Slot orderedNext;

        private short attributes;

        Slot(Object name, int indexOrHash, int attributes) {
            this.name = name;
            this.indexOrHash = indexOrHash;
            this.attributes = (short) attributes;
        }

        boolean setValue(Object value, Scriptable owner, Scriptable start, Context cx) {
            if ((this.attributes & 1) != 0) {
                if (cx.isStrictMode()) {
                    throw ScriptRuntime.typeError1(cx, "msg.modify.readonly", this.name);
                } else {
                    return true;
                }
            } else if (owner == start) {
                this.value = value;
                return true;
            } else {
                return false;
            }
        }

        Object getValue(Scriptable start, Context cx) {
            return this.value;
        }

        int getAttributes() {
            return this.attributes;
        }

        synchronized void setAttributes(int value) {
            ScriptableObject.checkValidAttributes(value);
            this.attributes = (short) value;
        }

        ScriptableObject getPropertyDescriptor(Context cx, Scriptable scope) {
            return ScriptableObject.buildDataDescriptor(scope, this.value, this.attributes, cx);
        }
    }

    static enum SlotAccess {

        QUERY, MODIFY, MODIFY_CONST, MODIFY_GETTER_SETTER, CONVERT_ACCESSOR_TO_DATA
    }
}