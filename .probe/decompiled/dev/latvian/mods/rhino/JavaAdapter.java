package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.classfile.ClassFileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

public final class JavaAdapter implements IdFunctionCall {

    private static final Object FTAG = "JavaAdapter";

    private static final int Id_JavaAdapter = 1;

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        JavaAdapter obj = new JavaAdapter();
        IdFunctionObject ctor = new IdFunctionObject(obj, FTAG, 1, "JavaAdapter", 1, scope);
        ctor.markAsConstructor(null);
        if (sealed) {
            ctor.sealObject(cx);
        }
        ctor.exportAsScopeProperty(cx);
    }

    public static Object convertResult(Context cx, Object result, Class<?> c) {
        if (result == Undefined.instance && c != ScriptRuntime.ObjectClass && c != ScriptRuntime.StringClass) {
            return null;
        } else {
            return c == null ? result : Context.jsToJava(cx, result, c);
        }
    }

    public static Scriptable createAdapterWrapper(Scriptable obj, Object adapter, Context cx) {
        Scriptable scope = ScriptableObject.getTopLevelScope(obj);
        NativeJavaObject res = new NativeJavaObject(scope, adapter, null, true, cx);
        res.setPrototype(obj);
        return res;
    }

    public static Object getAdapterSelf(Class<?> adapterClass, Object adapter) throws NoSuchFieldException, IllegalAccessException {
        Field self = adapterClass.getDeclaredField("self");
        return self.get(adapter);
    }

    static Object js_createAdapter(Context cx, Scriptable scope, Object[] args) {
        int N = args.length;
        if (N == 0) {
            throw ScriptRuntime.typeError0(cx, "msg.adapter.zero.args");
        } else {
            int classCount;
            for (classCount = 0; classCount < N - 1; classCount++) {
                Object arg = args[classCount];
                if (arg instanceof NativeObject) {
                    break;
                }
                if (!(arg instanceof NativeJavaClass)) {
                    throw ScriptRuntime.typeError2(cx, "msg.not.java.class.arg", String.valueOf(classCount), ScriptRuntime.toString(cx, arg));
                }
            }
            Class<?> superClass = null;
            Class<?>[] intfs = new Class[classCount];
            int interfaceCount = 0;
            for (int i = 0; i < classCount; i++) {
                Class<?> c = ((NativeJavaClass) args[i]).getClassObject();
                if (!c.isInterface()) {
                    if (superClass != null) {
                        throw ScriptRuntime.typeError2(cx, "msg.only.one.super", superClass.getName(), c.getName());
                    }
                    superClass = c;
                } else {
                    intfs[interfaceCount++] = c;
                }
            }
            if (superClass == null) {
                superClass = ScriptRuntime.ObjectClass;
            }
            Class<?>[] interfaces = new Class[interfaceCount];
            System.arraycopy(intfs, 0, interfaces, 0, interfaceCount);
            Scriptable obj = ScriptableObject.ensureScriptable(args[classCount], cx);
            Class<?> adapterClass = getAdapterClass(cx, scope, superClass, interfaces, obj);
            int argsCount = N - classCount - 1;
            try {
                Object adapter;
                if (argsCount > 0) {
                    Object[] ctorArgs = new Object[argsCount + 2];
                    ctorArgs[0] = obj;
                    ctorArgs[1] = cx;
                    System.arraycopy(args, classCount + 1, ctorArgs, 2, argsCount);
                    NativeJavaClass classWrapper = new NativeJavaClass(cx, scope, adapterClass, true);
                    NativeJavaMethod ctors = classWrapper.members.ctors;
                    int index = ctors.findCachedFunction(cx, ctorArgs);
                    if (index < 0) {
                        String sig = NativeJavaMethod.scriptSignature(args);
                        throw Context.reportRuntimeError2("msg.no.java.ctor", adapterClass.getName(), sig, cx);
                    }
                    adapter = NativeJavaClass.constructInternal(cx, scope, ctorArgs, ctors.methods[index]);
                } else {
                    Class<?>[] ctorParms = new Class[] { ScriptRuntime.ScriptableClass, Context.class };
                    Object[] ctorArgs = new Object[] { obj, cx };
                    adapter = adapterClass.getConstructor(ctorParms).newInstance(ctorArgs);
                }
                Object self = getAdapterSelf(adapterClass, adapter);
                if (self instanceof Wrapper) {
                    Object unwrapped = ((Wrapper) self).unwrap();
                    if (unwrapped instanceof Scriptable) {
                        if (unwrapped instanceof ScriptableObject) {
                            ScriptRuntime.setObjectProtoAndParent(cx, scope, (ScriptableObject) unwrapped);
                        }
                        return unwrapped;
                    }
                }
                return self;
            } catch (Exception var18) {
                throw Context.throwAsScriptRuntimeEx(var18, cx);
            }
        }
    }

    private static ObjToIntMap getObjectFunctionNames(Context cx, Scriptable obj) {
        Object[] ids = ScriptableObject.getPropertyIds(cx, obj);
        ObjToIntMap map = new ObjToIntMap(ids.length);
        for (int i = 0; i != ids.length; i++) {
            Object value = ids[i];
            if (value instanceof String) {
                String id = (String) value;
                value = ScriptableObject.getProperty(obj, id, cx);
                if (value instanceof Function) {
                    Function f = (Function) value;
                    int length = ScriptRuntime.toInt32(cx, ScriptableObject.getProperty(f, "length", cx));
                    if (length < 0) {
                        length = 0;
                    }
                    map.put(id, length);
                }
            }
        }
        return map;
    }

    private static Class<?> getAdapterClass(Context cx, Scriptable scope, Class<?> superClass, Class<?>[] interfaces, Scriptable obj) {
        Map<JavaAdapter.JavaAdapterSignature, Class<?>> generated = cx.getInterfaceAdapterCacheMap();
        ObjToIntMap names = getObjectFunctionNames(cx, obj);
        JavaAdapter.JavaAdapterSignature sig = new JavaAdapter.JavaAdapterSignature(superClass, interfaces, names);
        Class<?> adapterClass = (Class<?>) generated.get(sig);
        if (adapterClass == null) {
            String adapterName = "adapter" + cx.newClassSerialNumber();
            byte[] code = createAdapterCode(names, adapterName, superClass, interfaces, null, cx);
            adapterClass = loadAdapterClass(cx, adapterName, code);
            generated.put(sig, adapterClass);
        }
        return adapterClass;
    }

    public static byte[] createAdapterCode(ObjToIntMap functionNames, String adapterName, Class<?> superClass, Class<?>[] interfaces, String scriptClassName, Context cx) {
        ClassFileWriter cfw = new ClassFileWriter(adapterName, superClass.getName(), "<adapter>");
        cfw.addField("context", "Ldev/latvian/mods/rhino/Context;", (short) 17);
        cfw.addField("delegee", "Ldev/latvian/mods/rhino/Scriptable;", (short) 17);
        cfw.addField("self", "Ldev/latvian/mods/rhino/Scriptable;", (short) 17);
        int interfacesCount = interfaces == null ? 0 : interfaces.length;
        for (int i = 0; i < interfacesCount; i++) {
            if (interfaces[i] != null) {
                cfw.addInterface(interfaces[i].getName());
            }
        }
        String superName = superClass.getName().replace('.', '/');
        Constructor<?>[] ctors = superClass.getDeclaredConstructors();
        for (Constructor<?> ctor : ctors) {
            int mod = ctor.getModifiers();
            if (Modifier.isPublic(mod) || Modifier.isProtected(mod)) {
                generateCtor(cfw, adapterName, superName, ctor);
            }
        }
        generateSerialCtor(cfw, adapterName, superName);
        if (scriptClassName != null) {
            generateEmptyCtor(cfw, adapterName, superName, scriptClassName);
        }
        ObjToIntMap generatedOverrides = new ObjToIntMap();
        ObjToIntMap generatedMethods = new ObjToIntMap();
        for (int ix = 0; ix < interfacesCount; ix++) {
            Method[] methods = interfaces[ix].getMethods();
            for (int j = 0; j < methods.length; j++) {
                Method method = methods[j];
                int mods = method.getModifiers();
                if (!Modifier.isStatic(mods) && !Modifier.isFinal(mods) && !method.isDefault()) {
                    String methodName = method.getName();
                    Class<?>[] argTypes = method.getParameterTypes();
                    if (!functionNames.has(methodName)) {
                        try {
                            superClass.getMethod(methodName, argTypes);
                            continue;
                        } catch (NoSuchMethodException var21) {
                        }
                    }
                    String methodSignature = getMethodSignature(method, argTypes);
                    String methodKey = methodName + methodSignature;
                    if (!generatedOverrides.has(methodKey)) {
                        generateMethod(cfw, adapterName, methodName, argTypes, method.getReturnType(), true, cx);
                        generatedOverrides.put(methodKey, 0);
                        generatedMethods.put(methodName, 0);
                    }
                }
            }
        }
        Method[] methods = getOverridableMethods(superClass);
        for (int jx = 0; jx < methods.length; jx++) {
            Method method = methods[jx];
            int mods = method.getModifiers();
            boolean isAbstractMethod = Modifier.isAbstract(mods);
            String methodNamex = method.getName();
            if (isAbstractMethod || functionNames.has(methodNamex)) {
                Class<?>[] argTypesx = method.getParameterTypes();
                String methodSignature = getMethodSignature(method, argTypesx);
                String methodKey = methodNamex + methodSignature;
                if (!generatedOverrides.has(methodKey)) {
                    generateMethod(cfw, adapterName, methodNamex, argTypesx, method.getReturnType(), true, cx);
                    generatedOverrides.put(methodKey, 0);
                    generatedMethods.put(methodNamex, 0);
                    if (!isAbstractMethod) {
                        generateSuper(cfw, adapterName, superName, methodNamex, methodSignature, argTypesx, method.getReturnType());
                    }
                }
            }
        }
        ObjToIntMap.Iterator iter = new ObjToIntMap.Iterator(functionNames);
        iter.start();
        for (; !iter.done(); iter.next()) {
            String functionName = (String) iter.getKey();
            if (!generatedMethods.has(functionName)) {
                int length = iter.getValue();
                Class<?>[] parms = new Class[length];
                for (int k = 0; k < length; k++) {
                    parms[k] = ScriptRuntime.ObjectClass;
                }
                generateMethod(cfw, adapterName, functionName, parms, ScriptRuntime.ObjectClass, false, cx);
            }
        }
        return cfw.toByteArray();
    }

    static Method[] getOverridableMethods(Class<?> clazz) {
        ArrayList<Method> list = new ArrayList();
        HashSet<String> skip = new HashSet();
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            appendOverridableMethods(c, list, skip);
        }
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            for (Class<?> intf : c.getInterfaces()) {
                appendOverridableMethods(intf, list, skip);
            }
        }
        return (Method[]) list.toArray(new Method[list.size()]);
    }

    private static void appendOverridableMethods(Class<?> c, ArrayList<Method> list, HashSet<String> skip) {
        Method[] methods = c.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            String methodKey = methods[i].getName() + getMethodSignature(methods[i], methods[i].getParameterTypes());
            if (!skip.contains(methodKey)) {
                int mods = methods[i].getModifiers();
                if (!Modifier.isStatic(mods)) {
                    if (Modifier.isFinal(mods)) {
                        skip.add(methodKey);
                    } else if (Modifier.isPublic(mods) || Modifier.isProtected(mods)) {
                        list.add(methods[i]);
                        skip.add(methodKey);
                    }
                }
            }
        }
    }

    static Class<?> loadAdapterClass(Context cx, String className, byte[] classBytes) {
        GeneratedClassLoader loader = cx.createClassLoader(cx.getApplicationClassLoader());
        Class<?> result = loader.defineClass(className, classBytes);
        loader.linkClass(result);
        return result;
    }

    private static void generateCtor(ClassFileWriter cfw, String adapterName, String superName, Constructor<?> superCtor) {
        short locals = 3;
        Class<?>[] parameters = superCtor.getParameterTypes();
        if (parameters.length == 0) {
            cfw.startMethod("<init>", "(Ldev/latvian/mods/rhino/Scriptable;Ldev/latvian/mods/rhino/Context;)V", (short) 1);
            cfw.add(42);
            cfw.addInvoke(183, superName, "<init>", "()V");
        } else {
            StringBuilder sig = new StringBuilder("(Ldev/latvian/mods/rhino/Scriptable;Ldev/latvian/mods/rhino/Context;");
            int marker = sig.length();
            for (Class<?> c : parameters) {
                appendTypeString(sig, c);
            }
            sig.append(")V");
            cfw.startMethod("<init>", sig.toString(), (short) 1);
            cfw.add(42);
            short paramOffset = 3;
            for (Class<?> parameter : parameters) {
                paramOffset = (short) (paramOffset + generatePushParam(cfw, paramOffset, parameter));
            }
            locals = paramOffset;
            sig.delete(1, marker);
            cfw.addInvoke(183, superName, "<init>", sig.toString());
        }
        cfw.add(42);
        cfw.add(43);
        cfw.add(181, adapterName, "delegee", "Ldev/latvian/mods/rhino/Scriptable;");
        cfw.add(42);
        cfw.add(44);
        cfw.add(181, adapterName, "context", "Ldev/latvian/mods/rhino/Context;");
        cfw.add(42);
        cfw.add(43);
        cfw.add(42);
        cfw.addInvoke(184, "dev/latvian/mods/rhino/JavaAdapter", "createAdapterWrapper", "(Ldev/latvian/mods/rhino/Scriptable;Ljava/lang/Object;)Ldev/latvian/mods/rhino/Scriptable;");
        cfw.add(181, adapterName, "self", "Ldev/latvian/mods/rhino/Scriptable;");
        cfw.add(177);
        cfw.stopMethod(locals);
    }

    private static void generateSerialCtor(ClassFileWriter cfw, String adapterName, String superName) {
        cfw.startMethod("<init>", "(Ldev/latvian/mods/rhino/Context;Ldev/latvian/mods/rhino/Scriptable;Ldev/latvian/mods/rhino/Scriptable;)V", (short) 1);
        cfw.add(42);
        cfw.addInvoke(183, superName, "<init>", "()V");
        cfw.add(42);
        cfw.add(43);
        cfw.add(181, adapterName, "context", "Ldev/latvian/mods/rhino/Context;");
        cfw.add(42);
        cfw.add(44);
        cfw.add(181, adapterName, "delegee", "Ldev/latvian/mods/rhino/Scriptable;");
        cfw.add(42);
        cfw.add(45);
        cfw.add(181, adapterName, "self", "Ldev/latvian/mods/rhino/Scriptable;");
        cfw.add(177);
        cfw.stopMethod((short) 4);
    }

    private static void generateEmptyCtor(ClassFileWriter cfw, String adapterName, String superName, String scriptClassName) {
        cfw.startMethod("<init>", "()V", (short) 1);
        cfw.add(42);
        cfw.addInvoke(183, superName, "<init>", "()V");
        cfw.add(42);
        cfw.add(1);
        cfw.add(181, adapterName, "context", "Ldev/latvian/mods/rhino/Context;");
        cfw.add(187, scriptClassName);
        cfw.add(89);
        cfw.addInvoke(183, scriptClassName, "<init>", "()V");
        cfw.addInvoke(184, "dev/latvian/mods/rhino/JavaAdapter", "runScript", "(Ldev/latvian/mods/rhino/Script;)Ldev/latvian/mods/rhino/Scriptable;");
        cfw.add(76);
        cfw.add(42);
        cfw.add(43);
        cfw.add(181, adapterName, "delegee", "Ldev/latvian/mods/rhino/Scriptable;");
        cfw.add(42);
        cfw.add(43);
        cfw.add(42);
        cfw.addInvoke(184, "dev/latvian/mods/rhino/JavaAdapter", "createAdapterWrapper", "(Ldev/latvian/mods/rhino/Scriptable;Ljava/lang/Object;)Ldev/latvian/mods/rhino/Scriptable;");
        cfw.add(181, adapterName, "self", "Ldev/latvian/mods/rhino/Scriptable;");
        cfw.add(177);
        cfw.stopMethod((short) 2);
    }

    static void generatePushWrappedArgs(ClassFileWriter cfw, Class<?>[] argTypes, int arrayLength) {
        cfw.addPush(arrayLength);
        cfw.add(189, "java/lang/Object");
        int paramOffset = 1;
        for (int i = 0; i != argTypes.length; i++) {
            cfw.add(89);
            cfw.addPush(i);
            paramOffset += generateWrapArg(cfw, paramOffset, argTypes[i]);
            cfw.add(83);
        }
    }

    private static int generateWrapArg(ClassFileWriter cfw, int paramOffset, Class<?> argType) {
        int size = 1;
        if (!argType.isPrimitive()) {
            cfw.add(25, paramOffset);
        } else if (argType == boolean.class) {
            cfw.add(187, "java/lang/Boolean");
            cfw.add(89);
            cfw.add(21, paramOffset);
            cfw.addInvoke(183, "java/lang/Boolean", "<init>", "(Z)V");
        } else if (argType == char.class) {
            cfw.add(21, paramOffset);
            cfw.addInvoke(184, "java/lang/String", "valueOf", "(C)Ljava/lang/String;");
        } else {
            cfw.add(187, "java/lang/Double");
            cfw.add(89);
            String typeName = argType.getName();
            switch(typeName.charAt(0)) {
                case 'b':
                case 'i':
                case 's':
                    cfw.add(21, paramOffset);
                    cfw.add(135);
                case 'c':
                case 'e':
                case 'g':
                case 'h':
                case 'j':
                case 'k':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                default:
                    break;
                case 'd':
                    cfw.add(24, paramOffset);
                    size = 2;
                    break;
                case 'f':
                    cfw.add(23, paramOffset);
                    cfw.add(141);
                    break;
                case 'l':
                    cfw.add(22, paramOffset);
                    cfw.add(138);
                    size = 2;
            }
            cfw.addInvoke(183, "java/lang/Double", "<init>", "(D)V");
        }
        return size;
    }

    static void generateReturnResult(ClassFileWriter cfw, Class<?> retType, boolean callConvertResult) {
        if (retType == void.class) {
            cfw.add(87);
            cfw.add(177);
        } else if (retType == boolean.class) {
            cfw.addInvoke(184, "dev/latvian/mods/rhino/Context", "toBoolean", "(Ljava/lang/Object;)Z");
            cfw.add(172);
        } else if (retType == char.class) {
            cfw.addInvoke(184, "dev/latvian/mods/rhino/Context", "toString", "(Ljava/lang/Object;)Ljava/lang/String;");
            cfw.add(3);
            cfw.addInvoke(182, "java/lang/String", "charAt", "(I)C");
            cfw.add(172);
        } else if (retType.isPrimitive()) {
            cfw.addInvoke(184, "dev/latvian/mods/rhino/Context", "toNumber", "(Ljava/lang/Object;)D");
            String typeName = retType.getName();
            switch(typeName.charAt(0)) {
                case 'b':
                case 'i':
                case 's':
                    cfw.add(142);
                    cfw.add(172);
                    break;
                case 'c':
                case 'e':
                case 'g':
                case 'h':
                case 'j':
                case 'k':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                default:
                    throw new RuntimeException("Unexpected return type " + retType);
                case 'd':
                    cfw.add(175);
                    break;
                case 'f':
                    cfw.add(144);
                    cfw.add(174);
                    break;
                case 'l':
                    cfw.add(143);
                    cfw.add(173);
            }
        } else {
            String retTypeStr = retType.getName();
            if (callConvertResult) {
                cfw.addLoadConstant(retTypeStr);
                cfw.addInvoke(184, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
                cfw.addInvoke(184, "dev/latvian/mods/rhino/JavaAdapter", "convertResult", "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;");
            }
            cfw.add(192, retTypeStr);
            cfw.add(176);
        }
    }

    private static void generateMethod(ClassFileWriter cfw, String genName, String methodName, Class<?>[] parms, Class<?> returnType, boolean convertResult, Context cx) {
        StringBuilder sb = new StringBuilder();
        int paramsEnd = appendMethodSignature(parms, returnType, sb);
        String methodSignature = sb.toString();
        cfw.startMethod(methodName, methodSignature, (short) 1);
        cfw.add(42);
        cfw.add(180, genName, "context", "Ldev/latvian/mods/rhino/Context;");
        cfw.add(42);
        cfw.add(180, genName, "self", "Ldev/latvian/mods/rhino/Scriptable;");
        cfw.add(42);
        cfw.add(180, genName, "delegee", "Ldev/latvian/mods/rhino/Scriptable;");
        cfw.addPush(methodName);
        cfw.addInvoke(184, "dev/latvian/mods/rhino/JavaAdapter", "getFunction", "(Ldev/latvian/mods/rhino/Scriptable;Ljava/lang/String;)Ldev/latvian/mods/rhino/Function;");
        generatePushWrappedArgs(cfw, parms, parms.length);
        if (parms.length > 64) {
            throw Context.reportRuntimeError0("JavaAdapter can not subclass methods with more then 64 arguments.", cx);
        } else {
            long convertionMask = 0L;
            for (int i = 0; i != parms.length; i++) {
                if (!parms[i].isPrimitive()) {
                    convertionMask |= 1L << i;
                }
            }
            cfw.addPush(convertionMask);
            cfw.addInvoke(184, "dev/latvian/mods/rhino/JavaAdapter", "callMethod", "(Ldev/latvian/mods/rhino/Context;Ldev/latvian/mods/rhino/Scriptable;Ldev/latvian/mods/rhino/Function;[Ljava/lang/Object;J)Ljava/lang/Object;");
            generateReturnResult(cfw, returnType, convertResult);
            cfw.stopMethod((short) paramsEnd);
        }
    }

    private static int generatePushParam(ClassFileWriter cfw, int paramOffset, Class<?> paramType) {
        if (!paramType.isPrimitive()) {
            cfw.addALoad(paramOffset);
            return 1;
        } else {
            String typeName = paramType.getName();
            switch(typeName.charAt(0)) {
                case 'b':
                case 'c':
                case 'i':
                case 's':
                case 'z':
                    cfw.addILoad(paramOffset);
                    return 1;
                case 'd':
                    cfw.addDLoad(paramOffset);
                    return 2;
                case 'e':
                case 'g':
                case 'h':
                case 'j':
                case 'k':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                default:
                    throw Kit.codeBug();
                case 'f':
                    cfw.addFLoad(paramOffset);
                    return 1;
                case 'l':
                    cfw.addLLoad(paramOffset);
                    return 2;
            }
        }
    }

    private static void generatePopResult(ClassFileWriter cfw, Class<?> retType) {
        if (retType.isPrimitive()) {
            String typeName = retType.getName();
            switch(typeName.charAt(0)) {
                case 'b':
                case 'c':
                case 'i':
                case 's':
                case 'z':
                    cfw.add(172);
                    break;
                case 'd':
                    cfw.add(175);
                case 'e':
                case 'g':
                case 'h':
                case 'j':
                case 'k':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                default:
                    break;
                case 'f':
                    cfw.add(174);
                    break;
                case 'l':
                    cfw.add(173);
            }
        } else {
            cfw.add(176);
        }
    }

    private static void generateSuper(ClassFileWriter cfw, String genName, String superName, String methodName, String methodSignature, Class<?>[] parms, Class<?> returnType) {
        cfw.startMethod("super$" + methodName, methodSignature, (short) 1);
        cfw.add(25, 0);
        int paramOffset = 1;
        for (Class<?> parm : parms) {
            paramOffset += generatePushParam(cfw, paramOffset, parm);
        }
        cfw.addInvoke(183, superName, methodName, methodSignature);
        if (!returnType.equals(void.class)) {
            generatePopResult(cfw, returnType);
        } else {
            cfw.add(177);
        }
        cfw.stopMethod((short) (paramOffset + 1));
    }

    private static String getMethodSignature(Method method, Class<?>[] argTypes) {
        StringBuilder sb = new StringBuilder();
        appendMethodSignature(argTypes, method.getReturnType(), sb);
        return sb.toString();
    }

    static int appendMethodSignature(Class<?>[] argTypes, Class<?> returnType, StringBuilder sb) {
        sb.append('(');
        int firstLocal = 1 + argTypes.length;
        for (Class<?> type : argTypes) {
            appendTypeString(sb, type);
            if (type == long.class || type == double.class) {
                firstLocal++;
            }
        }
        sb.append(')');
        appendTypeString(sb, returnType);
        return firstLocal;
    }

    private static StringBuilder appendTypeString(StringBuilder sb, Class<?> type) {
        while (type.isArray()) {
            sb.append('[');
            type = type.getComponentType();
        }
        if (type.isPrimitive()) {
            char typeLetter;
            if (type == boolean.class) {
                typeLetter = 'Z';
            } else if (type == long.class) {
                typeLetter = 'J';
            } else {
                String typeName = type.getName();
                typeLetter = Character.toUpperCase(typeName.charAt(0));
            }
            sb.append(typeLetter);
        } else {
            sb.append('L');
            sb.append(type.getName().replace('.', '/'));
            sb.append(';');
        }
        return sb;
    }

    static int[] getArgsToConvert(Class<?>[] argTypes) {
        int count = 0;
        for (int i = 0; i != argTypes.length; i++) {
            if (!argTypes[i].isPrimitive()) {
                count++;
            }
        }
        if (count == 0) {
            return null;
        } else {
            int[] array = new int[count];
            count = 0;
            for (int ix = 0; ix != argTypes.length; ix++) {
                if (!argTypes[ix].isPrimitive()) {
                    array[count++] = ix;
                }
            }
            return array;
        }
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (f.hasTag(FTAG) && f.methodId() == 1) {
            return js_createAdapter(cx, scope, args);
        } else {
            throw f.unknown();
        }
    }

    static class JavaAdapterSignature {

        Class<?> superClass;

        Class<?>[] interfaces;

        ObjToIntMap names;

        JavaAdapterSignature(Class<?> superClass, Class<?>[] interfaces, ObjToIntMap names) {
            this.superClass = superClass;
            this.interfaces = interfaces;
            this.names = names;
        }

        public boolean equals(Object obj) {
            if (obj instanceof JavaAdapter.JavaAdapterSignature sig) {
                if (this.superClass != sig.superClass) {
                    return false;
                } else {
                    if (this.interfaces != sig.interfaces) {
                        if (this.interfaces.length != sig.interfaces.length) {
                            return false;
                        }
                        for (int i = 0; i < this.interfaces.length; i++) {
                            if (this.interfaces[i] != sig.interfaces[i]) {
                                return false;
                            }
                        }
                    }
                    if (this.names.size() != sig.names.size()) {
                        return false;
                    } else {
                        ObjToIntMap.Iterator iter = new ObjToIntMap.Iterator(this.names);
                        iter.start();
                        while (!iter.done()) {
                            String name = (String) iter.getKey();
                            int arity = iter.getValue();
                            if (arity != sig.names.get(name, arity + 1)) {
                                return false;
                            }
                            iter.next();
                        }
                        return true;
                    }
                }
            } else {
                return false;
            }
        }

        public int hashCode() {
            return this.superClass.hashCode() + Arrays.hashCode(this.interfaces) ^ this.names.size();
        }
    }
}