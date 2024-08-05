package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.util.Deletable;
import dev.latvian.mods.rhino.util.JavaIteratorWrapper;
import dev.latvian.mods.rhino.util.wrap.TypeWrapperFactory;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public class NativeJavaObject implements Scriptable, SymbolScriptable, Wrapper {

    static final byte CONVERSION_TRIVIAL = 1;

    static final byte CONVERSION_NONTRIVIAL = 0;

    static final byte CONVERSION_NONE = 99;

    private static final Object COERCED_INTERFACE_KEY = "Coerced Interface";

    private static final int JSTYPE_UNDEFINED = 0;

    private static final int JSTYPE_NULL = 1;

    private static final int JSTYPE_BOOLEAN = 2;

    private static final int JSTYPE_NUMBER = 3;

    private static final int JSTYPE_STRING = 4;

    private static final int JSTYPE_JAVA_CLASS = 5;

    private static final int JSTYPE_JAVA_OBJECT = 6;

    private static final int JSTYPE_JAVA_ARRAY = 7;

    private static final int JSTYPE_OBJECT = 8;

    protected Scriptable prototype;

    protected Scriptable parent;

    protected transient Object javaObject;

    protected transient Class<?> staticType;

    protected transient JavaMembers members;

    protected transient Map<String, FieldAndMethods> fieldAndMethods;

    protected transient Map<String, Object> customMembers;

    protected transient boolean isAdapter;

    public static boolean canConvert(Context cx, Object fromObj, Class<?> to) {
        return getConversionWeight(cx, fromObj, to) < 99;
    }

    static int getConversionWeight(Context cx, Object fromObj, Class<?> to) {
        if (cx.hasTypeWrappers() && cx.getTypeWrappers().getWrapperFactory(to, fromObj) != null) {
            return 0;
        } else {
            int fromCode = getJSTypeCode(fromObj);
            switch(fromCode) {
                case 0:
                    if (to == ScriptRuntime.StringClass || to == ScriptRuntime.ObjectClass) {
                        return 1;
                    }
                    break;
                case 1:
                    if (!to.isPrimitive()) {
                        return 1;
                    }
                    break;
                case 2:
                    if (to == boolean.class) {
                        return 1;
                    }
                    if (to == ScriptRuntime.BooleanClass) {
                        return 2;
                    }
                    if (to == ScriptRuntime.ObjectClass) {
                        return 3;
                    }
                    if (to == ScriptRuntime.StringClass) {
                        return 4;
                    }
                    break;
                case 3:
                    if (to.isPrimitive()) {
                        if (to == double.class) {
                            return 1;
                        }
                        if (to != boolean.class) {
                            return 1 + getSizeRank(to);
                        }
                    } else {
                        if (to == ScriptRuntime.StringClass) {
                            return 9;
                        }
                        if (to == ScriptRuntime.ObjectClass) {
                            return 10;
                        }
                        if (ScriptRuntime.NumberClass.isAssignableFrom(to)) {
                            return 2;
                        }
                    }
                    break;
                case 4:
                    if (to == ScriptRuntime.StringClass) {
                        return 1;
                    }
                    if (to.isInstance(fromObj)) {
                        return 2;
                    }
                    if (to.isPrimitive()) {
                        if (to == char.class) {
                            return 3;
                        }
                        if (to != boolean.class) {
                            return 4;
                        }
                    }
                    break;
                case 5:
                    if (to == ScriptRuntime.ClassClass) {
                        return 1;
                    }
                    if (to == ScriptRuntime.ObjectClass) {
                        return 3;
                    }
                    if (to == ScriptRuntime.StringClass) {
                        return 4;
                    }
                    break;
                case 6:
                case 7:
                    Object javaObj = fromObj;
                    if (fromObj instanceof Wrapper) {
                        javaObj = ((Wrapper) fromObj).unwrap();
                    }
                    if (to.isInstance(javaObj)) {
                        return 0;
                    }
                    if (to == ScriptRuntime.StringClass) {
                        return 2;
                    }
                    if (to.isPrimitive() && to != boolean.class) {
                        return fromCode == 7 ? 99 : 2 + getSizeRank(to);
                    }
                    break;
                case 8:
                    if (to != ScriptRuntime.ObjectClass && to.isInstance(fromObj)) {
                        return 1;
                    }
                    if (to.isArray()) {
                        if (fromObj instanceof NativeArray) {
                            return 2;
                        }
                    } else {
                        if (to == ScriptRuntime.ObjectClass) {
                            return 3;
                        }
                        if (to == ScriptRuntime.StringClass) {
                            return 4;
                        }
                        if (to == ScriptRuntime.DateClass) {
                            if (fromObj instanceof NativeDate) {
                                return 1;
                            }
                        } else {
                            if (to.isInterface()) {
                                if (fromObj instanceof NativeFunction) {
                                    return 1;
                                }
                                if (fromObj instanceof NativeObject) {
                                    return 2;
                                }
                                return 12;
                            }
                            if (to.isPrimitive() && to != boolean.class) {
                                return 4 + getSizeRank(to);
                            }
                        }
                    }
            }
            return 99;
        }
    }

    static int getSizeRank(Class<?> aType) {
        if (aType == double.class) {
            return 1;
        } else if (aType == float.class) {
            return 2;
        } else if (aType == long.class) {
            return 3;
        } else if (aType == int.class) {
            return 4;
        } else if (aType == short.class) {
            return 5;
        } else if (aType == char.class) {
            return 6;
        } else if (aType == byte.class) {
            return 7;
        } else {
            return aType == boolean.class ? 99 : 8;
        }
    }

    private static int getJSTypeCode(Object value) {
        if (value == null) {
            return 1;
        } else if (value == Undefined.instance) {
            return 0;
        } else if (value instanceof CharSequence) {
            return 4;
        } else if (value instanceof Number) {
            return 3;
        } else if (value instanceof Boolean) {
            return 2;
        } else if (value instanceof Scriptable) {
            if (value instanceof NativeJavaClass) {
                return 5;
            } else if (value instanceof NativeJavaArray) {
                return 7;
            } else {
                return value instanceof Wrapper ? 6 : 8;
            }
        } else if (value instanceof Class) {
            return 5;
        } else {
            Class<?> valueClass = value.getClass();
            return valueClass.isArray() ? 7 : 6;
        }
    }

    static Object coerceTypeImpl(@Nullable TypeWrappers typeWrappers, Class<?> type, Object value, Context cx) {
        if (value != null && value.getClass() != type) {
            Object unwrappedValue = Wrapper.unwrapped(value);
            TypeWrapperFactory<?> typeWrapper = typeWrappers == null ? null : typeWrappers.getWrapperFactory(type, unwrappedValue);
            if (typeWrapper != null) {
                return typeWrapper.wrap(cx, unwrappedValue);
            } else {
                switch(getJSTypeCode(value)) {
                    case 0:
                        if (type != ScriptRuntime.StringClass && type != ScriptRuntime.ObjectClass) {
                            return reportConversionError("undefined", type, value, cx);
                        }
                        return "undefined";
                    case 1:
                        if (type.isPrimitive()) {
                            return reportConversionError(value, type, cx);
                        }
                        return null;
                    case 2:
                        if (type != boolean.class && type != ScriptRuntime.BooleanClass && type != ScriptRuntime.ObjectClass) {
                            if (type == ScriptRuntime.StringClass) {
                                return value.toString();
                            }
                            return reportConversionError(value, type, cx);
                        }
                        return value;
                    case 3:
                        if (type == ScriptRuntime.StringClass) {
                            return ScriptRuntime.toString(cx, value);
                        } else if (type == ScriptRuntime.ObjectClass) {
                            return coerceToNumber(double.class, value, cx);
                        } else {
                            return (!type.isPrimitive() || type == boolean.class) && !ScriptRuntime.NumberClass.isAssignableFrom(type) ? reportConversionError(value, type, cx) : coerceToNumber(type, value, cx);
                        }
                    case 4:
                        if (type != ScriptRuntime.StringClass && !type.isInstance(value)) {
                            if (type != char.class && type != ScriptRuntime.CharacterClass) {
                                if ((!type.isPrimitive() || type == boolean.class) && !ScriptRuntime.NumberClass.isAssignableFrom(type)) {
                                    return reportConversionError(value, type, cx);
                                }
                                return coerceToNumber(type, value, cx);
                            }
                            if (((CharSequence) value).length() == 1) {
                                return ((CharSequence) value).charAt(0);
                            }
                            return coerceToNumber(type, value, cx);
                        }
                        return value.toString();
                    case 5:
                        if (type != ScriptRuntime.ClassClass && type != ScriptRuntime.ObjectClass) {
                            if (type == ScriptRuntime.StringClass) {
                                return unwrappedValue.toString();
                            }
                            return reportConversionError(unwrappedValue, type, cx);
                        }
                        return unwrappedValue;
                    case 6:
                    case 7:
                        if (type.isPrimitive()) {
                            if (type == boolean.class) {
                                return reportConversionError(unwrappedValue, type, cx);
                            }
                            return coerceToNumber(type, unwrappedValue, cx);
                        } else if (type == ScriptRuntime.StringClass) {
                            return unwrappedValue.toString();
                        } else {
                            if (type.isInstance(unwrappedValue)) {
                                return unwrappedValue;
                            }
                            return reportConversionError(unwrappedValue, type, cx);
                        }
                    case 8:
                        if (type == ScriptRuntime.StringClass) {
                            return ScriptRuntime.toString(cx, value);
                        } else if (type.isPrimitive()) {
                            if (type == boolean.class) {
                                return reportConversionError(value, type, cx);
                            }
                            return coerceToNumber(type, value, cx);
                        } else if (type.isInstance(value)) {
                            return value;
                        } else if (type == ScriptRuntime.DateClass && value instanceof NativeDate) {
                            double time = ((NativeDate) value).getJSTimeValue();
                            return new Date((long) time);
                        } else {
                            if (type.isArray() && value instanceof NativeArray array) {
                                long length = array.getLength();
                                Class<?> arrayType = type.getComponentType();
                                Object Result = Array.newInstance(arrayType, (int) length);
                                for (int i = 0; (long) i < length; i++) {
                                    try {
                                        Array.set(Result, i, coerceTypeImpl(typeWrappers, arrayType, array.get(cx, i, array), cx));
                                    } catch (EvaluatorException var13) {
                                        return reportConversionError(value, type, cx);
                                    }
                                }
                                return Result;
                            }
                            if (value instanceof Wrapper) {
                                if (type.isInstance(unwrappedValue)) {
                                    return unwrappedValue;
                                }
                                return reportConversionError(unwrappedValue, type, cx);
                            } else {
                                if (!type.isInterface() || !(value instanceof NativeObject) && !(value instanceof NativeFunction) && !(value instanceof ArrowFunction)) {
                                    return reportConversionError(value, type, cx);
                                }
                                return createInterfaceAdapter(cx, type, (ScriptableObject) value);
                            }
                        }
                    default:
                        return value;
                }
            }
        } else {
            return value;
        }
    }

    public static Object createInterfaceAdapter(Context cx, Class<?> type, ScriptableObject so) {
        Object key = Kit.makeHashKeyFromPair(COERCED_INTERFACE_KEY, type);
        Object old = so.getAssociatedValue(key);
        if (old != null) {
            return old;
        } else {
            Object glue = InterfaceAdapter.create(cx, type, so);
            return so.associateValue(key, glue);
        }
    }

    private static Object coerceToNumber(Class<?> type, Object value, Context cx) {
        Class<?> valueClass = value.getClass();
        if (type != char.class && type != ScriptRuntime.CharacterClass) {
            if (type == ScriptRuntime.ObjectClass || type == ScriptRuntime.DoubleClass || type == double.class) {
                return valueClass == ScriptRuntime.DoubleClass ? value : toDouble(value, cx);
            } else if (type != ScriptRuntime.FloatClass && type != float.class) {
                if (type != ScriptRuntime.IntegerClass && type != int.class) {
                    if (type != ScriptRuntime.LongClass && type != long.class) {
                        if (type != ScriptRuntime.ShortClass && type != short.class) {
                            if (type != ScriptRuntime.ByteClass && type != byte.class) {
                                return toDouble(value, cx);
                            } else {
                                return valueClass == ScriptRuntime.ByteClass ? value : (byte) ((int) toInteger(value, ScriptRuntime.ByteClass, -128.0, 127.0, cx));
                            }
                        } else {
                            return valueClass == ScriptRuntime.ShortClass ? value : (short) ((int) toInteger(value, ScriptRuntime.ShortClass, -32768.0, 32767.0, cx));
                        }
                    } else if (valueClass == ScriptRuntime.LongClass) {
                        return value;
                    } else {
                        double max = Double.longBitsToDouble(4890909195324358655L);
                        double min = Double.longBitsToDouble(-4332462841530417152L);
                        return toInteger(value, ScriptRuntime.LongClass, min, max, cx);
                    }
                } else {
                    return valueClass == ScriptRuntime.IntegerClass ? value : (int) toInteger(value, ScriptRuntime.IntegerClass, -2.1474836E9F, 2.147483647E9, cx);
                }
            } else if (valueClass == ScriptRuntime.FloatClass) {
                return value;
            } else {
                double number = toDouble(value, cx);
                if (!Double.isInfinite(number) && !Double.isNaN(number) && number != 0.0) {
                    double absNumber = Math.abs(number);
                    if (absNumber < Float.MIN_VALUE) {
                        return number > 0.0 ? 0.0F : -0.0F;
                    } else {
                        return absNumber > Float.MAX_VALUE ? number > 0.0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY : (float) number;
                    }
                } else {
                    return (float) number;
                }
            }
        } else {
            return valueClass == ScriptRuntime.CharacterClass ? value : (char) ((int) toInteger(value, ScriptRuntime.CharacterClass, 0.0, 65535.0, cx));
        }
    }

    private static double toDouble(Object value, Context cx) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            return ScriptRuntime.toNumber(cx, (String) value);
        } else if (value instanceof Scriptable) {
            return value instanceof Wrapper ? toDouble(((Wrapper) value).unwrap(), cx) : ScriptRuntime.toNumber(cx, value);
        } else {
            Method meth;
            try {
                meth = value.getClass().getMethod("doubleValue", (Class[]) null);
            } catch (NoSuchMethodException var6) {
                meth = null;
            } catch (SecurityException var7) {
                meth = null;
            }
            if (meth != null) {
                try {
                    return ((Number) meth.invoke(value, (Object[]) null)).doubleValue();
                } catch (IllegalAccessException var4) {
                    reportConversionError(value, double.class, cx);
                } catch (InvocationTargetException var5) {
                    reportConversionError(value, double.class, cx);
                }
            }
            return ScriptRuntime.toNumber(cx, value.toString());
        }
    }

    private static long toInteger(Object value, Class<?> type, double min, double max, Context cx) {
        double d = toDouble(value, cx);
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            reportConversionError(ScriptRuntime.toString(cx, value), type, cx);
        }
        if (d > 0.0) {
            d = Math.floor(d);
        } else {
            d = Math.ceil(d);
        }
        if (d < min || d > max) {
            reportConversionError(ScriptRuntime.toString(cx, value), type, cx);
        }
        return (long) d;
    }

    static Object reportConversionError(Object value, Class<?> type, Context cx) {
        return reportConversionError(value, type, value, cx);
    }

    static Object reportConversionError(Object value, Class<?> type, Object stringValue, Context cx) {
        throw Context.reportRuntimeError2("msg.conversion.not.allowed", String.valueOf(stringValue), JavaMembers.javaSignature(type), cx);
    }

    public NativeJavaObject() {
    }

    public NativeJavaObject(Scriptable scope, Object javaObject, Class<?> staticType, Context cx) {
        this(scope, javaObject, staticType, false, cx);
    }

    public NativeJavaObject(Scriptable scope, Object javaObject, Class<?> staticType, boolean isAdapter, Context cx) {
        this.parent = scope;
        this.javaObject = javaObject;
        this.staticType = staticType;
        this.isAdapter = isAdapter;
        this.initMembers(cx, scope);
    }

    protected void initMembers(Context cx, Scriptable scope) {
        Class<?> dynamicType;
        if (this.javaObject != null) {
            dynamicType = this.javaObject.getClass();
        } else {
            dynamicType = this.staticType;
        }
        this.members = JavaMembers.lookupClass(cx, scope, dynamicType, this.staticType, this.isAdapter);
        this.fieldAndMethods = this.members.getFieldAndMethodsObjects(this, this.javaObject, false, cx);
        this.customMembers = null;
    }

    protected void addCustomMember(String name, Object fm) {
        if (this.customMembers == null) {
            this.customMembers = new HashMap();
        }
        this.customMembers.put(name, fm);
    }

    protected void addCustomFunction(String name, CustomFunction.Func func, Class<?>... argTypes) {
        this.addCustomMember(name, new CustomFunction(name, func, argTypes));
    }

    protected void addCustomFunction(String name, CustomFunction.NoArgFunc func) {
        this.addCustomFunction(name, func, CustomFunction.NO_ARGS);
    }

    public void addCustomProperty(String name, CustomProperty getter) {
        this.addCustomMember(name, getter);
    }

    @Override
    public boolean has(Context cx, String name, Scriptable start) {
        return this.members.has(name, false) || this.customMembers != null && this.customMembers.containsKey(name);
    }

    @Override
    public boolean has(Context cx, int index, Scriptable start) {
        return false;
    }

    @Override
    public boolean has(Context cx, Symbol key, Scriptable start) {
        return this.javaObject instanceof Iterable && SymbolKey.ITERATOR.equals(key);
    }

    @Override
    public Object get(Context cx, String name, Scriptable start) {
        if (this.fieldAndMethods != null) {
            Object result = this.fieldAndMethods.get(name);
            if (result != null) {
                return result;
            }
        }
        if (this.customMembers != null) {
            Object result = this.customMembers.get(name);
            if (result != null) {
                if (result instanceof CustomProperty) {
                    Object r = ((CustomProperty) result).get(cx);
                    if (r == null) {
                        return Undefined.instance;
                    }
                    Object r1 = cx.getWrapFactory().wrap(cx, this, r, r.getClass());
                    if (r1 instanceof Scriptable) {
                        return ((Scriptable) r1).getDefaultValue(cx, null);
                    }
                    return r1;
                }
                return result;
            }
        }
        return this.members.get(this, name, this.javaObject, false, cx);
    }

    @Override
    public Object get(Context cx, Symbol key, Scriptable start) {
        if (this.javaObject instanceof Iterable<?> itr && SymbolKey.ITERATOR.equals(key)) {
            return new JavaIteratorWrapper(itr.iterator());
        }
        return Scriptable.NOT_FOUND;
    }

    @Override
    public Object get(Context cx, int index, Scriptable start) {
        throw this.members.reportMemberNotFound(Integer.toString(index), cx);
    }

    @Override
    public void put(Context cx, String name, Scriptable start, Object value) {
        if (this.prototype != null && !this.members.has(name, false)) {
            this.prototype.put(cx, name, this.prototype, value);
        } else {
            this.members.put(this, name, this.javaObject, value, false, cx);
        }
    }

    @Override
    public void put(Context cx, Symbol symbol, Scriptable start, Object value) {
        String name = symbol.toString();
        if (this.prototype == null || this.members.has(name, false)) {
            this.members.put(this, name, this.javaObject, value, false, cx);
        } else if (this.prototype instanceof SymbolScriptable) {
            ((SymbolScriptable) this.prototype).put(cx, symbol, this.prototype, value);
        }
    }

    @Override
    public void put(Context cx, int index, Scriptable start, Object value) {
        throw this.members.reportMemberNotFound(Integer.toString(index), cx);
    }

    @Override
    public boolean hasInstance(Context cx, Scriptable value) {
        return false;
    }

    @Override
    public void delete(Context cx, String name) {
        if (this.fieldAndMethods != null) {
            Object result = this.fieldAndMethods.get(name);
            if (result != null) {
                Deletable.deleteObject(result);
                return;
            }
        }
        if (this.customMembers != null) {
            Object result = this.customMembers.get(name);
            if (result != null) {
                Deletable.deleteObject(result);
                return;
            }
        }
        Deletable.deleteObject(this.members.get(this, name, this.javaObject, false, cx));
    }

    @Override
    public void delete(Context cx, Symbol key) {
    }

    @Override
    public void delete(Context cx, int index) {
    }

    @Override
    public Scriptable getPrototype(Context cx) {
        return this.prototype == null && this.javaObject instanceof String ? TopLevel.getBuiltinPrototype(ScriptableObject.getTopLevelScope(this.parent), TopLevel.Builtins.String, cx) : this.prototype;
    }

    @Override
    public void setPrototype(Scriptable m) {
        this.prototype = m;
    }

    @Override
    public Scriptable getParentScope() {
        return this.parent;
    }

    @Override
    public void setParentScope(Scriptable m) {
        this.parent = m;
    }

    @Override
    public Object[] getIds(Context cx) {
        if (this.customMembers != null) {
            Object[] c = this.customMembers.keySet().toArray(ScriptRuntime.EMPTY_OBJECTS);
            Object[] m = this.members.getIds(false);
            Object[] result = new Object[c.length + m.length];
            System.arraycopy(c, 0, result, 0, c.length);
            System.arraycopy(m, 0, result, c.length, m.length);
            return result;
        } else {
            return this.members.getIds(false);
        }
    }

    @Override
    public Object unwrap() {
        return this.javaObject;
    }

    @Override
    public String getClassName() {
        return "JavaObject";
    }

    @Override
    public Object getDefaultValue(Context cx, Class<?> hint) {
        if (hint == null) {
            if (this.javaObject instanceof Boolean) {
                hint = ScriptRuntime.BooleanClass;
            }
            if (this.javaObject instanceof Number) {
                hint = ScriptRuntime.NumberClass;
            }
        }
        Object value;
        if (hint != null && hint != ScriptRuntime.StringClass) {
            String converterName;
            if (hint == ScriptRuntime.BooleanClass) {
                converterName = "booleanValue";
            } else {
                if (hint != ScriptRuntime.NumberClass) {
                    throw Context.reportRuntimeError0("msg.default.value", cx);
                }
                converterName = "doubleValue";
            }
            if (this.get(cx, converterName, this) instanceof Function f) {
                value = f.call(cx, f.getParentScope(), this, ScriptRuntime.EMPTY_OBJECTS);
            } else if (hint == ScriptRuntime.NumberClass && this.javaObject instanceof Boolean) {
                boolean b = (Boolean) this.javaObject;
                value = b ? ScriptRuntime.wrapNumber(1.0) : ScriptRuntime.zeroObj;
            } else {
                value = this.javaObject.toString();
            }
        } else {
            value = this.javaObject.toString();
        }
        return value;
    }
}