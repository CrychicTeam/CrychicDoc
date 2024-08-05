package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.ast.AstRoot;
import dev.latvian.mods.rhino.ast.ScriptNode;
import dev.latvian.mods.rhino.classfile.ClassFileWriter;
import dev.latvian.mods.rhino.regexp.RegExp;
import dev.latvian.mods.rhino.util.CustomJavaToJsWrapper;
import dev.latvian.mods.rhino.util.CustomJavaToJsWrapperProvider;
import dev.latvian.mods.rhino.util.CustomJavaToJsWrapperProviderHolder;
import dev.latvian.mods.rhino.util.DefaultRemapper;
import dev.latvian.mods.rhino.util.Remapper;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;

public class Context {

    public final Object lock = new Object();

    public boolean generateObserverCount = false;

    private Scriptable topCallScope;

    boolean isContinuationsTopCall;

    NativeCall currentActivationCall;

    BaseFunction typeErrorThrower;

    ObjToIntMap iterating;

    RegExp regExp;

    Object lastInterpreterFrame;

    ObjArray previousInterpreterInvocations;

    int instructionCount;

    int instructionThreshold;

    long scratchUint32;

    private Scriptable scratchScriptable;

    boolean isTopLevelStrict;

    private int maximumInterpreterStackDepth;

    private Map<Object, Object> threadLocalMap;

    private ClassLoader applicationClassLoader;

    final List<CustomJavaToJsWrapperProviderHolder<?>> customScriptableWrappers = new ArrayList();

    final Map<Class<?>, CustomJavaToJsWrapperProvider> customScriptableWrapperCache = new HashMap();

    private final Map<String, Object> properties = new HashMap();

    TypeWrappers typeWrappers;

    Remapper remapper = DefaultRemapper.INSTANCE;

    private transient Map<Class<?>, JavaMembers> classTable;

    private transient Map<JavaAdapter.JavaAdapterSignature, Class<?>> classAdapterCache;

    private transient Map<Class<?>, Object> interfaceAdapterCache;

    private int generatedClassSerial;

    private ClassShutter classShutter;

    private WrapFactory wrapFactory;

    public static Context enter() {
        return new Context();
    }

    public static void reportWarning(Context cx, String message, String sourceName, int lineno, String lineSource, int lineOffset) {
        cx.getErrorReporter().warning(message, sourceName, lineno, lineSource, lineOffset);
    }

    public static void reportWarning(String message, Context cx) {
        int[] linep = new int[] { 0 };
        String filename = getSourcePositionFromStack(cx, linep);
        reportWarning(cx, message, filename, linep[0], null, 0);
    }

    public static void reportError(Context cx, String message, int lineno, String lineSource, int lineOffset, String sourceName) {
        if (cx != null) {
            cx.getErrorReporter().error(cx, message, sourceName, lineno, lineSource, lineOffset);
        } else {
            throw new EvaluatorException(cx, message, sourceName, lineno, lineSource, lineOffset);
        }
    }

    public static void reportError(Context cx, String message) {
        int[] linep = new int[] { 0 };
        String filename = getSourcePositionFromStack(cx, linep);
        reportError(cx, message, linep[0], null, 0, filename);
    }

    public static EvaluatorException reportRuntimeError(Context cx, String message, String sourceName, int lineno, String lineSource, int lineOffset) {
        if (cx != null) {
            return cx.getErrorReporter().runtimeError(cx, message, sourceName, lineno, lineSource, lineOffset);
        } else {
            throw new EvaluatorException(cx, message, sourceName, lineno, lineSource, lineOffset);
        }
    }

    public static EvaluatorException reportRuntimeError0(String messageId, Context cx) {
        String msg = ScriptRuntime.getMessage0(messageId);
        return reportRuntimeError(msg, cx);
    }

    public static EvaluatorException reportRuntimeError1(String messageId, Object arg1, Context cx) {
        String msg = ScriptRuntime.getMessage1(messageId, arg1);
        return reportRuntimeError(msg, cx);
    }

    public static EvaluatorException reportRuntimeError2(String messageId, Object arg1, Object arg2, Context cx) {
        String msg = ScriptRuntime.getMessage2(messageId, arg1, arg2);
        return reportRuntimeError(msg, cx);
    }

    public static EvaluatorException reportRuntimeError3(String messageId, Object arg1, Object arg2, Object arg3, Context cx) {
        String msg = ScriptRuntime.getMessage3(messageId, arg1, arg2, arg3);
        return reportRuntimeError(msg, cx);
    }

    public static EvaluatorException reportRuntimeError4(String messageId, Object arg1, Object arg2, Object arg3, Object arg4, Context cx) {
        String msg = ScriptRuntime.getMessage4(messageId, arg1, arg2, arg3, arg4);
        return reportRuntimeError(msg, cx);
    }

    public static EvaluatorException reportRuntimeError(String message, Context cx) {
        int[] linep = new int[] { 0 };
        String filename = getSourcePositionFromStack(cx, linep);
        return reportRuntimeError(cx, message, filename, linep[0], null, 0);
    }

    public static Object getUndefinedValue() {
        return Undefined.instance;
    }

    public static Object javaToJS(Context cx, Object value, Scriptable scope) {
        if (value instanceof String || value instanceof Number || value instanceof Boolean || value instanceof Scriptable) {
            return value;
        } else {
            return value instanceof Character ? String.valueOf((Character) value) : cx.getWrapFactory().wrap(cx, scope, value, null);
        }
    }

    public static Object jsToJava(Context cx, Object value, Class<?> desiredType) throws EvaluatorException {
        return desiredType == null ? value : NativeJavaObject.coerceTypeImpl(cx.hasTypeWrappers() ? cx.getTypeWrappers() : null, desiredType, value, cx);
    }

    public static RuntimeException throwAsScriptRuntimeEx(Throwable e, Context cx) {
        while (e instanceof InvocationTargetException) {
            e = ((InvocationTargetException) e).getTargetException();
        }
        if (e instanceof Error) {
            throw (Error) e;
        } else if (e instanceof RhinoException) {
            throw (RhinoException) e;
        } else {
            throw new WrappedException(cx, e);
        }
    }

    static Evaluator createInterpreter() {
        return new Interpreter();
    }

    public static String getSourcePositionFromStack(Context cx, int[] linep) {
        if (cx == null) {
            return null;
        } else {
            if (cx.lastInterpreterFrame != null) {
                Evaluator evaluator = createInterpreter();
                if (evaluator != null) {
                    return evaluator.getSourcePositionFromStack(cx, linep);
                }
            }
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            for (StackTraceElement st : stackTrace) {
                String file = st.getFileName();
                if (file != null && !file.endsWith(".java")) {
                    int line = st.getLineNumber();
                    if (line >= 0) {
                        linep[0] = line;
                        return file;
                    }
                }
            }
            return null;
        }
    }

    protected Context() {
        this.maximumInterpreterStackDepth = Integer.MAX_VALUE;
    }

    public final String getImplementationVersion() {
        return ImplementationVersion.get();
    }

    public final ErrorReporter getErrorReporter() {
        return DefaultErrorReporter.instance;
    }

    public final ScriptableObject initStandardObjects() {
        return this.initStandardObjects(null, false);
    }

    public final ScriptableObject initSafeStandardObjects() {
        return this.initSafeStandardObjects(null, false);
    }

    public final Scriptable initStandardObjects(ScriptableObject scope) {
        return this.initStandardObjects(scope, false);
    }

    public final Scriptable initSafeStandardObjects(ScriptableObject scope) {
        return this.initSafeStandardObjects(scope, false);
    }

    public ScriptableObject initStandardObjects(ScriptableObject scope, boolean sealed) {
        return ScriptRuntime.initStandardObjects(this, scope, sealed);
    }

    public ScriptableObject initSafeStandardObjects(ScriptableObject scope, boolean sealed) {
        return ScriptRuntime.initSafeStandardObjects(this, scope, sealed);
    }

    public final Object evaluateString(Scriptable scope, String source, String sourceName, int lineno, Object securityDomain) {
        Script script = this.compileString(source, sourceName, lineno, securityDomain);
        return script != null ? script.exec(this, scope) : null;
    }

    public final Object evaluateReader(Scriptable scope, Reader in, String sourceName, int lineno, Object securityDomain) throws IOException {
        Script script = this.compileReader(in, sourceName, lineno, securityDomain);
        return script != null ? script.exec(this, scope) : null;
    }

    public final Script compileReader(Reader in, String sourceName, int lineno, Object securityDomain) throws IOException {
        if (lineno < 0) {
            lineno = 0;
        }
        return (Script) this.compileImpl(null, Kit.readReader(in), sourceName, lineno, securityDomain, false, null, null);
    }

    public final Script compileString(String source, String sourceName, int lineno, Object securityDomain) {
        if (lineno < 0) {
            lineno = 0;
        }
        return this.compileString(source, null, null, sourceName, lineno, securityDomain);
    }

    final Script compileString(String source, Evaluator compiler, ErrorReporter compilationErrorReporter, String sourceName, int lineno, Object securityDomain) {
        try {
            return (Script) this.compileImpl(null, source, sourceName, lineno, securityDomain, false, compiler, compilationErrorReporter);
        } catch (IOException var8) {
            throw new RuntimeException(var8);
        }
    }

    final Function compileFunction(Scriptable scope, String source, Evaluator compiler, ErrorReporter compilationErrorReporter, String sourceName, int lineno, Object securityDomain) {
        try {
            return (Function) this.compileImpl(scope, source, sourceName, lineno, securityDomain, true, compiler, compilationErrorReporter);
        } catch (IOException var9) {
            throw new RuntimeException(var9);
        }
    }

    public Scriptable newObject(Scriptable scope) {
        NativeObject result = new NativeObject(this);
        ScriptRuntime.setBuiltinProtoAndParent(this, scope, result, TopLevel.Builtins.Object);
        return result;
    }

    public Scriptable newObject(Scriptable scope, String constructorName) {
        return this.newObject(scope, constructorName, ScriptRuntime.EMPTY_OBJECTS);
    }

    public Scriptable newObject(Scriptable scope, String constructorName, Object[] args) {
        return ScriptRuntime.newObject(this, scope, constructorName, args);
    }

    public Scriptable newArray(Scriptable scope, int length) {
        NativeArray result = new NativeArray(this, (long) length);
        ScriptRuntime.setBuiltinProtoAndParent(this, scope, result, TopLevel.Builtins.Array);
        return result;
    }

    public Scriptable newArray(Scriptable scope, Object[] elements) {
        if (elements.getClass().getComponentType() != ScriptRuntime.ObjectClass) {
            throw new IllegalArgumentException();
        } else {
            NativeArray result = new NativeArray(this, elements);
            ScriptRuntime.setBuiltinProtoAndParent(this, scope, result, TopLevel.Builtins.Array);
            return result;
        }
    }

    public final int getMaximumInterpreterStackDepth() {
        return this.maximumInterpreterStackDepth;
    }

    public final void setMaximumInterpreterStackDepth(int max) {
        if (max < 1) {
            throw new IllegalArgumentException("Cannot set maximumInterpreterStackDepth to less than 1");
        } else {
            this.maximumInterpreterStackDepth = max;
        }
    }

    public final Object getThreadLocal(Object key) {
        return this.threadLocalMap == null ? null : this.threadLocalMap.get(key);
    }

    public final synchronized void putThreadLocal(Object key, Object value) {
        if (this.threadLocalMap == null) {
            this.threadLocalMap = new HashMap();
        }
        this.threadLocalMap.put(key, value);
    }

    public final void removeThreadLocal(Object key) {
        if (this.threadLocalMap != null) {
            this.threadLocalMap.remove(key);
        }
    }

    public final int getInstructionObserverThreshold() {
        return this.instructionThreshold;
    }

    public final void setInstructionObserverThreshold(int threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException();
        } else {
            this.instructionThreshold = threshold;
            this.setGenerateObserverCount(threshold > 0);
        }
    }

    public void setGenerateObserverCount(boolean generateObserverCount) {
        this.generateObserverCount = generateObserverCount;
    }

    protected void observeInstructionCount(int instructionCount) {
    }

    public GeneratedClassLoader createClassLoader(ClassLoader parent) {
        return new DefiningClassLoader(parent);
    }

    public final ClassLoader getApplicationClassLoader() {
        if (this.applicationClassLoader == null) {
            ClassLoader threadLoader = Thread.currentThread().getContextClassLoader();
            if (threadLoader != null && Kit.testIfCanLoadRhinoClasses(threadLoader)) {
                return threadLoader;
            }
            this.applicationClassLoader = this.getClass().getClassLoader();
        }
        return this.applicationClassLoader;
    }

    public final void setApplicationClassLoader(ClassLoader loader) {
        if (loader == null) {
            this.applicationClassLoader = null;
        } else if (!Kit.testIfCanLoadRhinoClasses(loader)) {
            throw new IllegalArgumentException("Loader can not resolve Rhino classes");
        } else {
            this.applicationClassLoader = loader;
        }
    }

    private Object compileImpl(Scriptable scope, String sourceString, String sourceName, int lineno, Object securityDomain, boolean returnFunction, Evaluator compiler, ErrorReporter compilationErrorReporter) throws IOException {
        if (sourceName == null) {
            sourceName = "unnamed script";
        }
        if (securityDomain != null) {
            throw new IllegalArgumentException("securityDomain should be null if setSecurityController() was never called");
        } else {
            if (scope == null == returnFunction) {
                Kit.codeBug();
            }
            CompilerEnvirons compilerEnv = new CompilerEnvirons();
            compilerEnv.initFromContext(this);
            if (compilationErrorReporter == null) {
                compilationErrorReporter = compilerEnv.getErrorReporter();
            }
            ScriptNode tree = this.parse(sourceString, sourceName, lineno, compilerEnv, compilationErrorReporter, returnFunction);
            Object bytecode;
            try {
                if (compiler == null) {
                    compiler = this.createCompiler();
                }
                bytecode = compiler.compile(compilerEnv, tree, returnFunction, this);
            } catch (ClassFileWriter.ClassFileFormatException var13) {
                tree = this.parse(sourceString, sourceName, lineno, compilerEnv, compilationErrorReporter, returnFunction);
                compiler = createInterpreter();
                bytecode = compiler.compile(compilerEnv, tree, returnFunction, this);
            }
            Object result;
            if (returnFunction) {
                result = compiler.createFunctionObject(this, scope, bytecode, securityDomain);
            } else {
                result = compiler.createScriptObject(bytecode, securityDomain);
            }
            return result;
        }
    }

    private ScriptNode parse(String sourceString, String sourceName, int lineno, CompilerEnvirons compilerEnv, ErrorReporter compilationErrorReporter, boolean returnFunction) throws IOException {
        Parser p = new Parser(this, compilerEnv, compilationErrorReporter);
        if (returnFunction) {
            p.calledByCompileFunction = true;
        }
        if (this.isStrictMode()) {
            p.setDefaultUseStrictDirective(true);
        }
        AstRoot ast = p.parse(sourceString, sourceName, lineno);
        if (!returnFunction || ast.getFirstChild() != null && ast.getFirstChild().getType() == 110) {
            return new IRFactory(this, compilerEnv, compilationErrorReporter).transformTree(ast);
        } else {
            throw new IllegalArgumentException("compileFunction only accepts source with single JS function: " + sourceString);
        }
    }

    private Evaluator createCompiler() {
        return createInterpreter();
    }

    public RegExp getRegExp() {
        if (this.regExp == null) {
            this.regExp = new RegExp();
        }
        return this.regExp;
    }

    public final boolean isStrictMode() {
        return this.isTopLevelStrict || this.currentActivationCall != null && this.currentActivationCall.isStrict;
    }

    public void addToScope(Scriptable scope, String name, Object value) {
        if (value instanceof Class<?> c) {
            ScriptableObject.putProperty(scope, name, new NativeJavaClass(this, scope, c), this);
        } else {
            ScriptableObject.putProperty(scope, name, javaToJS(this, value, scope), this);
        }
    }

    Map<Class<?>, JavaMembers> getClassCacheMap() {
        if (this.classTable == null) {
            this.classTable = new ConcurrentHashMap(16, 0.75F, 1);
        }
        return this.classTable;
    }

    Map<JavaAdapter.JavaAdapterSignature, Class<?>> getInterfaceAdapterCacheMap() {
        if (this.classAdapterCache == null) {
            this.classAdapterCache = new ConcurrentHashMap(16, 0.75F, 1);
        }
        return this.classAdapterCache;
    }

    public final synchronized int newClassSerialNumber() {
        return ++this.generatedClassSerial;
    }

    Object getInterfaceAdapter(Class<?> cl) {
        return this.interfaceAdapterCache == null ? null : this.interfaceAdapterCache.get(cl);
    }

    synchronized void cacheInterfaceAdapter(Class<?> cl, Object iadapter) {
        if (this.interfaceAdapterCache == null) {
            this.interfaceAdapterCache = new ConcurrentHashMap(16, 0.75F, 1);
        }
        this.interfaceAdapterCache.put(cl, iadapter);
    }

    public TypeWrappers getTypeWrappers() {
        if (this.typeWrappers == null) {
            this.typeWrappers = new TypeWrappers();
        }
        return this.typeWrappers;
    }

    public boolean hasTypeWrappers() {
        return this.typeWrappers != null;
    }

    public Remapper getRemapper() {
        return this.remapper;
    }

    public void setRemapper(Remapper remapper) {
        this.remapper = remapper;
    }

    @Nullable
    public CustomJavaToJsWrapper wrapCustomJavaToJs(Object javaObject) {
        if (this.customScriptableWrappers.isEmpty()) {
            return null;
        } else {
            CustomJavaToJsWrapperProvider provider = (CustomJavaToJsWrapperProvider) this.customScriptableWrapperCache.get(javaObject.getClass());
            if (provider == null) {
                for (CustomJavaToJsWrapperProviderHolder wrapper : this.customScriptableWrappers) {
                    provider = wrapper.create(javaObject);
                    if (provider != null) {
                        break;
                    }
                }
                if (provider == null) {
                    provider = CustomJavaToJsWrapperProvider.NONE;
                }
                this.customScriptableWrapperCache.put(javaObject.getClass(), provider);
            }
            return provider.create(javaObject);
        }
    }

    public <T> void addCustomJavaToJsWrapper(Predicate<T> predicate, CustomJavaToJsWrapperProvider<T> provider) {
        this.customScriptableWrappers.add(new CustomJavaToJsWrapperProviderHolder<>(predicate, provider));
    }

    public <T> void addCustomJavaToJsWrapper(Class<T> type, CustomJavaToJsWrapperProvider<T> provider) {
        this.addCustomJavaToJsWrapper(new CustomJavaToJsWrapperProviderHolder.PredicateFromClass<>(type), provider);
    }

    public void setProperty(String key, @Nullable Object value) {
        if (value == null) {
            this.properties.remove(key);
        } else {
            this.properties.put(key, value);
        }
    }

    @Nullable
    public Object getProperty(String key) {
        return this.properties.get(key);
    }

    @Nullable
    public <T> T getProperty(String key, T def) {
        return (T) this.properties.getOrDefault(key, def);
    }

    @Nullable
    public final synchronized ClassShutter getClassShutter() {
        return this.classShutter;
    }

    public final synchronized void setClassShutter(ClassShutter shutter) {
        if (shutter == null) {
            throw new IllegalArgumentException();
        } else if (this.classShutter != null) {
            throw new SecurityException("Cannot overwrite existing ClassShutter object");
        } else {
            this.classShutter = shutter;
        }
    }

    public final WrapFactory getWrapFactory() {
        if (this.wrapFactory == null) {
            this.wrapFactory = new WrapFactory();
        }
        return this.wrapFactory;
    }

    public final void setWrapFactory(WrapFactory wrapFactory) {
        if (wrapFactory == null) {
            throw new IllegalArgumentException();
        } else {
            this.wrapFactory = wrapFactory;
        }
    }

    public boolean hasTopCallScope() {
        synchronized (this.lock) {
            return this.topCallScope != null;
        }
    }

    public Scriptable getTopCallScope() {
        synchronized (this.lock) {
            return this.topCallScope;
        }
    }

    public Scriptable getTopCallOrThrow() {
        synchronized (this.lock) {
            if (this.topCallScope == null) {
                throw new IllegalStateException();
            } else {
                return this.topCallScope;
            }
        }
    }

    public void setTopCall(Scriptable scope) {
        synchronized (this.lock) {
            this.topCallScope = scope;
        }
    }

    public void storeScriptable(Scriptable value) {
        synchronized (this.lock) {
            if (this.scratchScriptable != null) {
                throw new IllegalStateException();
            } else {
                this.scratchScriptable = value;
            }
        }
    }

    public Scriptable lastStoredScriptable() {
        synchronized (this.lock) {
            Scriptable result = this.scratchScriptable;
            this.scratchScriptable = null;
            return result;
        }
    }

    public Object callSync(Callable callable, Scriptable scope, Scriptable thisObj, Object[] args) {
        synchronized (this.lock) {
            return callable.call(this, scope, thisObj, args);
        }
    }

    public Object doTopCall(Scriptable scope, Callable callable, Scriptable thisObj, Object[] args, boolean isTopLevelStrict) {
        if (scope == null) {
            throw new IllegalArgumentException();
        } else if (this.hasTopCallScope()) {
            throw new IllegalStateException();
        } else {
            this.setTopCall(ScriptableObject.getTopLevelScope(scope));
            boolean previousTopLevelStrict = this.isTopLevelStrict;
            this.isTopLevelStrict = isTopLevelStrict;
            Object result;
            try {
                result = this.callSync(callable, scope, thisObj, args);
                if (result instanceof ConsString) {
                    result = result.toString();
                }
            } finally {
                this.setTopCall(null);
                this.isTopLevelStrict = previousTopLevelStrict;
                if (this.currentActivationCall != null) {
                    throw new IllegalStateException();
                }
            }
            return result;
        }
    }
}