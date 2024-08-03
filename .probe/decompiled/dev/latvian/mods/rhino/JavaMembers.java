package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapForJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class JavaMembers {

    public final Context localContext;

    private final Class<?> cl;

    private final Map<String, Object> members;

    private final Map<String, Object> staticMembers;

    NativeJavaMethod ctors;

    private Map<String, FieldAndMethods> fieldAndMethods;

    private Map<String, FieldAndMethods> staticFieldAndMethods;

    public static String javaSignature(Class<?> type) {
        if (!type.isArray()) {
            return type.getName();
        } else {
            int arrayDimension = 0;
            do {
                arrayDimension++;
                type = type.getComponentType();
            } while (type.isArray());
            String name = type.getName();
            String suffix = "[]";
            if (arrayDimension == 1) {
                return name.concat(suffix);
            } else {
                int length = name.length() + arrayDimension * suffix.length();
                StringBuilder sb = new StringBuilder(length);
                sb.append(name);
                while (arrayDimension != 0) {
                    arrayDimension--;
                    sb.append(suffix);
                }
                return sb.toString();
            }
        }
    }

    public static String liveConnectSignature(Class<?>[] argTypes) {
        int N = argTypes.length;
        if (N == 0) {
            return "()";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append('(');
            for (int i = 0; i != N; i++) {
                if (i != 0) {
                    sb.append(',');
                }
                sb.append(javaSignature(argTypes[i]));
            }
            sb.append(')');
            return sb.toString();
        }
    }

    private static MemberBox findGetter(boolean isStatic, Map<String, Object> ht, String prefix, String propertyName) {
        String getterName = prefix.concat(propertyName);
        return ht.containsKey(getterName) && ht.get(getterName) instanceof NativeJavaMethod njmGet ? extractGetMethod(njmGet.methods, isStatic) : null;
    }

    private static MemberBox extractGetMethod(MemberBox[] methods, boolean isStatic) {
        for (MemberBox method : methods) {
            if (method.argTypes.length == 0 && (!isStatic || method.isStatic())) {
                Class<?> type = method.getReturnType();
                if (type != void.class) {
                    return method;
                }
                break;
            }
        }
        return null;
    }

    private static MemberBox extractSetMethod(Class<?> type, MemberBox[] methods, boolean isStatic) {
        for (int pass = 1; pass <= 2; pass++) {
            for (MemberBox method : methods) {
                if (!isStatic || method.isStatic()) {
                    Class<?>[] params = method.argTypes;
                    if (params.length == 1) {
                        if (pass == 1) {
                            if (params[0] == type) {
                                return method;
                            }
                        } else {
                            if (pass != 2) {
                                Kit.codeBug();
                            }
                            if (params[0].isAssignableFrom(type)) {
                                return method;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static MemberBox extractSetMethod(MemberBox[] methods, boolean isStatic) {
        for (MemberBox method : methods) {
            if ((!isStatic || method.isStatic()) && method.getReturnType() == void.class && method.argTypes.length == 1) {
                return method;
            }
        }
        return null;
    }

    public static JavaMembers lookupClass(Context cx, Scriptable scope, Class<?> dynamicType, Class<?> staticType, boolean includeProtected) {
        Map<Class<?>, JavaMembers> ct = cx.getClassCacheMap();
        Class<?> cl = dynamicType;
        while (true) {
            JavaMembers members = (JavaMembers) ct.get(cl);
            if (members != null) {
                if (cl != dynamicType) {
                    ct.put(dynamicType, members);
                }
                return members;
            }
            try {
                members = new JavaMembers(cl, includeProtected, cx, scope);
            } catch (SecurityException var10) {
                if (staticType != null && staticType.isInterface()) {
                    cl = staticType;
                    staticType = null;
                    continue;
                }
                Class<?> parent = cl.getSuperclass();
                if (parent == null) {
                    if (!cl.isInterface()) {
                        throw var10;
                    }
                    parent = ScriptRuntime.ObjectClass;
                }
                cl = parent;
                continue;
            }
            ct.put(cl, members);
            if (cl != dynamicType) {
                ct.put(dynamicType, members);
            }
            return members;
        }
    }

    JavaMembers(Class<?> cl, boolean includeProtected, Context cx, Scriptable scope) {
        this.localContext = cx;
        ClassShutter shutter = cx.getClassShutter();
        if (shutter != null && !shutter.visibleToScripts(cl.getName(), 1)) {
            throw Context.reportRuntimeError1("msg.access.prohibited", cl.getName(), cx);
        } else {
            this.members = new HashMap();
            this.staticMembers = new HashMap();
            this.cl = cl;
            this.reflect(scope, includeProtected, cx);
        }
    }

    public boolean has(String name, boolean isStatic) {
        Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
        Object obj = ht.get(name);
        return obj != null ? true : this.findExplicitFunction(name, isStatic) != null;
    }

    public Object get(Scriptable scope, String name, Object javaObject, boolean isStatic, Context cx) {
        Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
        Object member = ht.get(name);
        if (!isStatic && member == null) {
            member = this.staticMembers.get(name);
        }
        if (member == null) {
            member = this.getExplicitFunction(scope, name, javaObject, isStatic, cx);
            if (member == null) {
                return Scriptable.NOT_FOUND;
            }
        }
        if (member instanceof Scriptable) {
            return member;
        } else {
            Object rval;
            Class<?> type;
            try {
                if (member instanceof BeanProperty bp) {
                    if (bp.getter == null) {
                        return Scriptable.NOT_FOUND;
                    }
                    rval = bp.getter.invoke(javaObject, ScriptRuntime.EMPTY_OBJECTS, cx, scope);
                    type = bp.getter.getReturnType();
                } else {
                    Field field = (Field) member;
                    rval = field.get(isStatic ? null : javaObject);
                    type = field.getType();
                }
            } catch (Exception var12) {
                throw Context.throwAsScriptRuntimeEx(var12, cx);
            }
            scope = ScriptableObject.getTopLevelScope(scope);
            return cx.getWrapFactory().wrap(cx, scope, rval, type);
        }
    }

    public void put(Scriptable scope, String name, Object javaObject, Object value, boolean isStatic, Context cx) {
        Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
        Object member = ht.get(name);
        if (!isStatic && member == null) {
            member = this.staticMembers.get(name);
        }
        if (member == null) {
            throw this.reportMemberNotFound(name, cx);
        } else {
            if (member instanceof FieldAndMethods) {
                FieldAndMethods fam = (FieldAndMethods) ht.get(name);
                member = fam.field;
            }
            if (member instanceof BeanProperty bp) {
                if (bp.setter == null) {
                    throw this.reportMemberNotFound(name, cx);
                }
                if (bp.setters != null && value != null) {
                    Object[] args = new Object[] { value };
                    cx.callSync(bp.setters, ScriptableObject.getTopLevelScope(scope), scope, args);
                } else {
                    Class<?> setType = bp.setter.argTypes[0];
                    Object[] args = new Object[] { Context.jsToJava(cx, value, setType) };
                    try {
                        bp.setter.invoke(javaObject, args, cx, scope);
                    } catch (Exception var16) {
                        throw Context.throwAsScriptRuntimeEx(var16, cx);
                    }
                }
            } else {
                if (!(member instanceof Field field)) {
                    String str = member == null ? "msg.java.internal.private" : "msg.java.method.assign";
                    throw Context.reportRuntimeError1(str, name, cx);
                }
                int fieldModifiers = field.getModifiers();
                if (Modifier.isFinal(fieldModifiers)) {
                    throw Context.throwAsScriptRuntimeEx(new IllegalAccessException("Can't modify final field " + field.getName()), cx);
                }
                Object javaValue = Context.jsToJava(cx, value, field.getType());
                try {
                    field.set(javaObject, javaValue);
                } catch (IllegalAccessException var14) {
                    throw Context.throwAsScriptRuntimeEx(var14, cx);
                } catch (IllegalArgumentException var15) {
                    throw Context.reportRuntimeError3("msg.java.internal.field.type", value.getClass().getName(), field, javaObject.getClass().getName(), cx);
                }
            }
        }
    }

    public Object[] getIds(boolean isStatic) {
        Map<String, Object> map = isStatic ? this.staticMembers : this.members;
        return map.keySet().toArray(ScriptRuntime.EMPTY_OBJECTS);
    }

    private MemberBox findExplicitFunction(String name, boolean isStatic) {
        int sigStart = name.indexOf(40);
        if (sigStart < 0) {
            return null;
        } else {
            Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
            MemberBox[] methodsOrCtors = null;
            boolean isCtor = isStatic && sigStart == 0;
            if (isCtor) {
                methodsOrCtors = this.ctors.methods;
            } else {
                String trueName = name.substring(0, sigStart);
                Object obj = ht.get(trueName);
                if (!isStatic && obj == null) {
                    obj = this.staticMembers.get(trueName);
                }
                if (obj instanceof NativeJavaMethod njm) {
                    methodsOrCtors = njm.methods;
                }
            }
            if (methodsOrCtors != null) {
                for (MemberBox methodsOrCtor : methodsOrCtors) {
                    Class<?>[] type = methodsOrCtor.argTypes;
                    String sig = liveConnectSignature(type);
                    if (sigStart + sig.length() == name.length() && name.regionMatches(sigStart, sig, 0, sig.length())) {
                        return methodsOrCtor;
                    }
                }
            }
            return null;
        }
    }

    private Object getExplicitFunction(Scriptable scope, String name, Object javaObject, boolean isStatic, Context cx) {
        Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
        Object member = null;
        MemberBox methodOrCtor = this.findExplicitFunction(name, isStatic);
        if (methodOrCtor != null) {
            Scriptable prototype = ScriptableObject.getFunctionPrototype(scope, cx);
            if (methodOrCtor.isCtor()) {
                NativeJavaConstructor fun = new NativeJavaConstructor(methodOrCtor);
                fun.setPrototype(prototype);
                member = fun;
                ht.put(name, fun);
            } else {
                String trueName = methodOrCtor.getName();
                member = ht.get(trueName);
                if (member instanceof NativeJavaMethod && ((NativeJavaMethod) member).methods.length > 1) {
                    NativeJavaMethod fun = new NativeJavaMethod(methodOrCtor, name);
                    fun.setPrototype(prototype);
                    ht.put(name, fun);
                    member = fun;
                }
            }
        }
        return member;
    }

    private void reflect(Scriptable scope, boolean includeProtected, Context cx) {
        if (this.cl.isAnnotationPresent(HideFromJS.class)) {
            this.ctors = new NativeJavaMethod(new MemberBox[0], this.cl.getSimpleName());
        } else {
            for (JavaMembers.MethodInfo methodInfo : this.getAccessibleMethods(cx, includeProtected)) {
                Method method = methodInfo.method;
                int mods = method.getModifiers();
                boolean isStatic = Modifier.isStatic(mods);
                Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
                String name = methodInfo.name;
                Object value = ht.get(name);
                if (value == null) {
                    ht.put(name, method);
                } else {
                    ObjArray overloadedMethods;
                    if (value instanceof ObjArray) {
                        overloadedMethods = (ObjArray) value;
                    } else {
                        if (!(value instanceof Method)) {
                            Kit.codeBug();
                        }
                        overloadedMethods = new ObjArray();
                        overloadedMethods.add(value);
                        ht.put(name, overloadedMethods);
                    }
                    overloadedMethods.add(method);
                }
            }
            for (int tableCursor = 0; tableCursor != 2; tableCursor++) {
                boolean isStatic = tableCursor == 0;
                Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
                for (Entry<String, Object> entry : ht.entrySet()) {
                    Object value = entry.getValue();
                    MemberBox[] methodBoxes;
                    if (value instanceof Method) {
                        methodBoxes = new MemberBox[1];
                        methodBoxes[0] = new MemberBox((Method) value);
                    } else {
                        ObjArray overloadedMethods = (ObjArray) value;
                        int N = overloadedMethods.size();
                        if (N < 2) {
                            Kit.codeBug();
                        }
                        methodBoxes = new MemberBox[N];
                        for (int i = 0; i != N; i++) {
                            Method method = (Method) overloadedMethods.get(i);
                            methodBoxes[i] = new MemberBox(method);
                        }
                    }
                    NativeJavaMethod fun = new NativeJavaMethod(methodBoxes);
                    if (scope != null) {
                        ScriptRuntime.setFunctionProtoAndParent(cx, scope, fun);
                    }
                    ht.put((String) entry.getKey(), fun);
                }
            }
            for (JavaMembers.FieldInfo fieldInfo : this.getAccessibleFields(cx, includeProtected)) {
                Field field = fieldInfo.field;
                String name = fieldInfo.name;
                int mods = field.getModifiers();
                try {
                    boolean isStatic = Modifier.isStatic(mods);
                    Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
                    Object member = ht.get(name);
                    if (member == null) {
                        ht.put(name, field);
                    } else if (member instanceof NativeJavaMethod method) {
                        FieldAndMethods fam = new FieldAndMethods(scope, method.methods, field, cx);
                        Map<String, FieldAndMethods> fmht = isStatic ? this.staticFieldAndMethods : this.fieldAndMethods;
                        if (fmht == null) {
                            fmht = new HashMap();
                            if (isStatic) {
                                this.staticFieldAndMethods = fmht;
                            } else {
                                this.fieldAndMethods = fmht;
                            }
                        }
                        fmht.put(name, fam);
                        ht.put(name, fam);
                    } else if (member instanceof Field oldField) {
                        if (oldField.getDeclaringClass().isAssignableFrom(field.getDeclaringClass())) {
                            ht.put(name, field);
                        }
                    } else {
                        Kit.codeBug();
                    }
                } catch (SecurityException var24) {
                    Context.reportWarning("Could not access field " + name + " of class " + this.cl.getName() + " due to lack of privileges.", cx);
                }
            }
            for (int tableCursor = 0; tableCursor != 2; tableCursor++) {
                boolean isStatic = tableCursor == 0;
                Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
                Map<String, BeanProperty> toAdd = new HashMap();
                for (String name : ht.keySet()) {
                    boolean memberIsGetMethod = name.startsWith("get");
                    boolean memberIsSetMethod = name.startsWith("set");
                    boolean memberIsIsMethod = name.startsWith("is");
                    if (memberIsGetMethod || memberIsIsMethod || memberIsSetMethod) {
                        String nameComponent = name.substring(memberIsIsMethod ? 2 : 3);
                        if (nameComponent.length() != 0) {
                            String beanPropertyName = nameComponent;
                            char ch0 = nameComponent.charAt(0);
                            if (Character.isUpperCase(ch0)) {
                                if (nameComponent.length() == 1) {
                                    beanPropertyName = nameComponent.toLowerCase();
                                } else {
                                    char ch1 = nameComponent.charAt(1);
                                    if (!Character.isUpperCase(ch1)) {
                                        beanPropertyName = Character.toLowerCase(ch0) + nameComponent.substring(1);
                                    }
                                }
                            }
                            if (!toAdd.containsKey(beanPropertyName)) {
                                Object v = ht.get(beanPropertyName);
                                if (v == null) {
                                    MemberBox getter = findGetter(isStatic, ht, "get", nameComponent);
                                    if (getter == null) {
                                        getter = findGetter(isStatic, ht, "is", nameComponent);
                                    }
                                    MemberBox setter = null;
                                    NativeJavaMethod setters = null;
                                    String setterName = "set".concat(nameComponent);
                                    if (ht.containsKey(setterName)) {
                                        Object member = ht.get(setterName);
                                        if (member instanceof NativeJavaMethod) {
                                            NativeJavaMethod njmSet = (NativeJavaMethod) member;
                                            if (getter != null) {
                                                Class<?> type = getter.getReturnType();
                                                setter = extractSetMethod(type, njmSet.methods, isStatic);
                                            } else {
                                                setter = extractSetMethod(njmSet.methods, isStatic);
                                            }
                                            if (njmSet.methods.length > 1) {
                                                setters = njmSet;
                                            }
                                        }
                                    }
                                    BeanProperty bp = new BeanProperty(getter, setter, setters);
                                    toAdd.put(beanPropertyName, bp);
                                }
                            }
                        }
                    }
                }
                ht.putAll(toAdd);
            }
            List<Constructor<?>> constructors = this.getAccessibleConstructors();
            MemberBox[] ctorMembers = new MemberBox[constructors.size()];
            for (int i = 0; i != constructors.size(); i++) {
                ctorMembers[i] = new MemberBox((Executable) constructors.get(i));
            }
            this.ctors = new NativeJavaMethod(ctorMembers, this.cl.getSimpleName());
        }
    }

    public List<Constructor<?>> getAccessibleConstructors() {
        List<Constructor<?>> constructorsList = new ArrayList();
        for (Constructor<?> c : this.cl.getConstructors()) {
            if (!c.isAnnotationPresent(HideFromJS.class) && Modifier.isPublic(c.getModifiers())) {
                constructorsList.add(c);
            }
        }
        return constructorsList;
    }

    public Collection<JavaMembers.FieldInfo> getAccessibleFields(Context cx, boolean includeProtected) {
        LinkedHashMap<String, JavaMembers.FieldInfo> fieldMap = new LinkedHashMap();
        try {
            for (Class<?> currentClass = this.cl; currentClass != null; currentClass = currentClass.getSuperclass()) {
                Set<String> remapPrefixes = new HashSet();
                for (RemapPrefixForJS r : (RemapPrefixForJS[]) currentClass.getAnnotationsByType(RemapPrefixForJS.class)) {
                    String s = r.value().trim();
                    if (!s.isEmpty()) {
                        remapPrefixes.add(s);
                    }
                }
                for (Field field : getDeclaredFieldsSafe(currentClass)) {
                    int mods = field.getModifiers();
                    if (!Modifier.isTransient(mods) && (Modifier.isPublic(mods) || includeProtected && Modifier.isProtected(mods)) && !field.isAnnotationPresent(HideFromJS.class)) {
                        try {
                            if (includeProtected && Modifier.isProtected(mods) && !field.isAccessible()) {
                                field.setAccessible(true);
                            }
                            JavaMembers.FieldInfo info = new JavaMembers.FieldInfo(field);
                            RemapForJS remap = (RemapForJS) field.getAnnotation(RemapForJS.class);
                            if (remap != null) {
                                info.name = remap.value().trim();
                            }
                            if (info.name.isEmpty()) {
                                for (String s : remapPrefixes) {
                                    if (field.getName().startsWith(s)) {
                                        info.name = field.getName().substring(s.length()).trim();
                                        break;
                                    }
                                }
                            }
                            if (info.name.isEmpty()) {
                                info.name = cx.getRemapper().getMappedField(currentClass, field);
                            }
                            if (info.name.isEmpty()) {
                                info.name = field.getName();
                            }
                            if (!fieldMap.containsKey(info.name)) {
                                fieldMap.put(info.name, info);
                            }
                        } catch (Exception var15) {
                        }
                    }
                }
            }
        } catch (SecurityException var16) {
        }
        return fieldMap.values();
    }

    public Collection<JavaMembers.MethodInfo> getAccessibleMethods(Context cx, boolean includeProtected) {
        LinkedHashMap<JavaMembers.MethodSignature, JavaMembers.MethodInfo> methodMap = new LinkedHashMap();
        ArrayDeque<Class<?>> stack = new ArrayDeque();
        stack.add(this.cl);
        while (!stack.isEmpty()) {
            Class<?> currentClass = (Class<?>) stack.pop();
            Set<String> remapPrefixes = new HashSet();
            for (RemapPrefixForJS r : (RemapPrefixForJS[]) currentClass.getAnnotationsByType(RemapPrefixForJS.class)) {
                String s = r.value().trim();
                if (!s.isEmpty()) {
                    remapPrefixes.add(s);
                }
            }
            for (Method method : getDeclaredMethodsSafe(currentClass)) {
                int mods = method.getModifiers();
                if (Modifier.isPublic(mods) || includeProtected && Modifier.isProtected(mods)) {
                    JavaMembers.MethodSignature signature = new JavaMembers.MethodSignature(method);
                    JavaMembers.MethodInfo info = (JavaMembers.MethodInfo) methodMap.get(signature);
                    boolean hidden = method.isAnnotationPresent(HideFromJS.class);
                    if (info == null) {
                        try {
                            if (!hidden && includeProtected && Modifier.isProtected(mods) && !method.isAccessible()) {
                                method.setAccessible(true);
                            }
                            info = new JavaMembers.MethodInfo(method);
                            methodMap.put(signature, info);
                        } catch (Exception var18) {
                        }
                    }
                    if (info != null) {
                        if (hidden) {
                            info.hidden = true;
                        } else {
                            RemapForJS remap = (RemapForJS) method.getAnnotation(RemapForJS.class);
                            if (remap != null) {
                                info.name = remap.value().trim();
                            }
                            if (info.name.isEmpty()) {
                                for (String s : remapPrefixes) {
                                    if (method.getName().startsWith(s)) {
                                        info.name = method.getName().substring(s.length()).trim();
                                        break;
                                    }
                                }
                            }
                            if (info.name.isEmpty()) {
                                info.name = cx.getRemapper().getMappedMethod(currentClass, method);
                            }
                        }
                    }
                }
            }
            stack.addAll(Arrays.asList(currentClass.getInterfaces()));
            Class<?> parent = currentClass.getSuperclass();
            if (parent != null) {
                stack.add(parent);
            }
        }
        ArrayList<JavaMembers.MethodInfo> list = new ArrayList(methodMap.size());
        for (JavaMembers.MethodInfo m : methodMap.values()) {
            if (!m.hidden) {
                if (m.name.isEmpty()) {
                    m.name = m.method.getName();
                }
                list.add(m);
            }
        }
        return list;
    }

    private static Method[] getDeclaredMethodsSafe(Class<?> cl) {
        try {
            return cl.getDeclaredMethods();
        } catch (Throwable var2) {
            System.err.println("[Rhino] Failed to get declared methods for " + cl.getName() + ": " + var2);
            return new Method[0];
        }
    }

    private static Field[] getDeclaredFieldsSafe(Class<?> cl) {
        try {
            return cl.getDeclaredFields();
        } catch (Throwable var2) {
            System.err.println("[Rhino] Failed to get declared fields for " + cl.getName() + ": " + var2);
            return new Field[0];
        }
    }

    public Map<String, FieldAndMethods> getFieldAndMethodsObjects(Scriptable scope, Object javaObject, boolean isStatic, Context cx) {
        Map<String, FieldAndMethods> ht = isStatic ? this.staticFieldAndMethods : this.fieldAndMethods;
        if (ht == null) {
            return null;
        } else {
            int len = ht.size();
            Map<String, FieldAndMethods> result = new HashMap(len);
            for (FieldAndMethods fam : ht.values()) {
                FieldAndMethods famNew = new FieldAndMethods(scope, fam.methods, fam.field, cx);
                famNew.javaObject = javaObject;
                result.put(fam.field.getName(), famNew);
            }
            return result;
        }
    }

    RuntimeException reportMemberNotFound(String memberName, Context cx) {
        return Context.reportRuntimeError2("msg.java.member.not.found", this.cl.getName(), memberName, cx);
    }

    public static class FieldInfo {

        public final Field field;

        public String name = "";

        public FieldInfo(Field f) {
            this.field = f;
        }
    }

    public static class MethodInfo {

        public Method method;

        public String name = "";

        public boolean hidden = false;

        public MethodInfo(Method m) {
            this.method = m;
        }
    }

    public static record MethodSignature(String name, Class<?>[] args) {

        private static final Class<?>[] NO_ARGS = new Class[0];

        public MethodSignature(Method method) {
            this(method.getName(), method.getParameterCount() == 0 ? NO_ARGS : method.getParameterTypes());
        }

        public boolean equals(Object o) {
            return !(o instanceof JavaMembers.MethodSignature ms) ? false : ms.name.equals(this.name) && Arrays.equals(this.args, ms.args);
        }

        public int hashCode() {
            return this.name.hashCode() ^ this.args.length;
        }
    }
}