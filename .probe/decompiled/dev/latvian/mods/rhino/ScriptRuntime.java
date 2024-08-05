package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.regexp.NativeRegExp;
import dev.latvian.mods.rhino.regexp.RegExp;
import dev.latvian.mods.rhino.util.SpecialEquality;
import dev.latvian.mods.rhino.v8dtoa.DoubleConversion;
import dev.latvian.mods.rhino.v8dtoa.FastDtoa;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ScriptRuntime {

    public static final Object[] EMPTY_OBJECTS = new Object[0];

    public static final String[] EMPTY_STRINGS = new String[0];

    public static final Class<Boolean> BooleanClass = Boolean.class;

    public static final Class<Byte> ByteClass = Byte.class;

    public static final Class<Character> CharacterClass = Character.class;

    public static final Class<Class> ClassClass = Class.class;

    public static final Class<Double> DoubleClass = Double.class;

    public static final Class<Float> FloatClass = Float.class;

    public static final Class<Integer> IntegerClass = Integer.class;

    public static final Class<Long> LongClass = Long.class;

    public static final Class<Number> NumberClass = Number.class;

    public static final Class<Object> ObjectClass = Object.class;

    public static final Class<Short> ShortClass = Short.class;

    public static final Class<String> StringClass = String.class;

    public static final Class<Date> DateClass = Date.class;

    public static final Class<?> ContextClass = Context.class;

    public static final Class<Function> FunctionClass = Function.class;

    public static final Class<ScriptableObject> ScriptableObjectClass = ScriptableObject.class;

    public static final Class<Scriptable> ScriptableClass = Scriptable.class;

    public static final double NaN = Double.NaN;

    public static final Double NaNobj = Double.NaN;

    public static final double negativeZero = Double.longBitsToDouble(Long.MIN_VALUE);

    public static final Double zeroObj = 0.0;

    public static final Double negativeZeroObj = -0.0;

    public static final int ENUMERATE_KEYS = 0;

    public static final int ENUMERATE_VALUES = 1;

    public static final int ENUMERATE_ARRAY = 2;

    public static final int ENUMERATE_KEYS_NO_ITERATOR = 3;

    public static final int ENUMERATE_VALUES_NO_ITERATOR = 4;

    public static final int ENUMERATE_ARRAY_NO_ITERATOR = 5;

    public static final int ENUMERATE_VALUES_IN_ORDER = 6;

    public static final ScriptRuntime.MessageProvider messageProvider = new ScriptRuntime.DefaultMessageProvider();

    private static final Object LIBRARY_SCOPE_KEY = "LIBRARY_SCOPE";

    public static BaseFunction typeErrorThrower(Context cx) {
        if (cx.typeErrorThrower == null) {
            BaseFunction thrower = new BaseFunction() {

                @Override
                public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    throw ScriptRuntime.typeError0(cx, "msg.op.not.allowed");
                }

                @Override
                public int getLength() {
                    return 0;
                }
            };
            setFunctionProtoAndParent(cx, cx.getTopCallScope(), thrower);
            thrower.preventExtensions();
            cx.typeErrorThrower = thrower;
        }
        return cx.typeErrorThrower;
    }

    public static boolean isRhinoRuntimeType(Class<?> cl) {
        return cl.isPrimitive() ? cl != char.class : cl == StringClass || cl == BooleanClass || NumberClass.isAssignableFrom(cl) || ScriptableClass.isAssignableFrom(cl);
    }

    public static ScriptableObject initSafeStandardObjects(Context cx, ScriptableObject scope, boolean sealed) {
        if (scope == null) {
            scope = new NativeObject(cx);
        }
        scope.associateValue(LIBRARY_SCOPE_KEY, scope);
        BaseFunction.init(scope, sealed, cx);
        NativeObject.init(cx, scope, sealed);
        Scriptable objectProto = ScriptableObject.getObjectPrototype(scope, cx);
        Scriptable functionProto = ScriptableObject.getClassPrototype(scope, "Function", cx);
        functionProto.setPrototype(objectProto);
        if (scope.getPrototype(cx) == null) {
            scope.setPrototype(objectProto);
        }
        NativeError.init(scope, sealed, cx);
        NativeGlobal.init(cx, scope, sealed);
        NativeArray.init(scope, sealed, cx);
        NativeString.init(scope, sealed, cx);
        NativeBoolean.init(scope, sealed, cx);
        NativeNumber.init(scope, sealed, cx);
        NativeDate.init(scope, sealed, cx);
        NativeMath.init(scope, sealed, cx);
        NativeJSON.init(scope, sealed, cx);
        NativeWith.init(scope, sealed, cx);
        NativeCall.init(scope, sealed, cx);
        NativeIterator.init(cx, scope, sealed);
        NativeArrayIterator.init(scope, sealed, cx);
        NativeStringIterator.init(scope, sealed, cx);
        NativeRegExp.init(cx, scope, sealed);
        NativeSymbol.init(cx, scope, sealed);
        NativeCollectionIterator.init(scope, "Set Iterator", sealed, cx);
        NativeCollectionIterator.init(scope, "Map Iterator", sealed, cx);
        NativeMap.init(cx, scope, sealed);
        NativeSet.init(cx, scope, sealed);
        NativeWeakMap.init(scope, sealed, cx);
        NativeWeakSet.init(scope, sealed, cx);
        if (scope instanceof TopLevel) {
            ((TopLevel) scope).cacheBuiltins(scope, sealed, cx);
        }
        return scope;
    }

    public static ScriptableObject initStandardObjects(Context cx, ScriptableObject scope, boolean sealed) {
        ScriptableObject s = initSafeStandardObjects(cx, scope, sealed);
        JavaAdapter.init(cx, s, sealed);
        return s;
    }

    public static ScriptableObject getLibraryScopeOrNull(Scriptable scope, Context cx) {
        return (ScriptableObject) ScriptableObject.getTopScopeValue(scope, LIBRARY_SCOPE_KEY, cx);
    }

    public static boolean isJSLineTerminator(int c) {
        return (c & 57296) != 0 ? false : c == 10 || c == 13 || c == 8232 || c == 8233;
    }

    public static boolean isJSWhitespaceOrLineTerminator(int c) {
        return isStrWhiteSpaceChar(c) || isJSLineTerminator(c);
    }

    static boolean isStrWhiteSpaceChar(int c) {
        return switch(c) {
            case 9, 10, 11, 12, 13, 32, 160, 8232, 8233, 65279 ->
                true;
            default ->
                Character.getType(c) == 12;
        };
    }

    public static Number wrapNumber(double x) {
        return Double.isNaN(x) ? NaNobj : x;
    }

    public static boolean toBoolean(Context cx, Object val) {
        if (val instanceof Boolean) {
            return (Boolean) val;
        } else if (val == null || val == Undefined.instance) {
            return false;
        } else if (val instanceof CharSequence) {
            return ((CharSequence) val).length() != 0;
        } else if (val instanceof Number) {
            double d = ((Number) val).doubleValue();
            return !Double.isNaN(d) && d != 0.0;
        } else if (!(val instanceof Scriptable)) {
            warnAboutNonJSObject(cx, val);
            return true;
        } else {
            return !(val instanceof ScriptableObject) || !((ScriptableObject) val).avoidObjectDetection();
        }
    }

    public static double toNumber(Context cx, Object val) {
        if (val instanceof Number) {
            return ((Number) val).doubleValue();
        } else if (val == null) {
            return 0.0;
        } else if (val == Undefined.instance) {
            return Double.NaN;
        } else if (val instanceof String) {
            return toNumber(cx, (String) val);
        } else if (val instanceof CharSequence) {
            return toNumber(cx, val.toString());
        } else if (val instanceof Boolean) {
            return (Boolean) val ? 1.0 : 0.0;
        } else if (val instanceof Symbol) {
            throw typeError0(cx, "msg.not.a.number");
        } else if (val instanceof Scriptable) {
            val = ((Scriptable) val).getDefaultValue(cx, NumberClass);
            if (val instanceof Scriptable && !isSymbol(val)) {
                throw errorWithClassName("msg.primitive.expected", val, cx);
            } else {
                return toNumber(cx, val);
            }
        } else {
            warnAboutNonJSObject(cx, val);
            return Double.NaN;
        }
    }

    public static double toNumber(Context cx, Object[] args, int index) {
        return index < args.length ? toNumber(cx, args[index]) : Double.NaN;
    }

    static double stringPrefixToNumber(String s, int start, int radix) {
        return stringToNumber(s, start, s.length() - 1, radix, true);
    }

    static double stringToNumber(String s, int start, int end, int radix) {
        return stringToNumber(s, start, end, radix, false);
    }

    private static double stringToNumber(String source, int sourceStart, int sourceEnd, int radix, boolean isPrefix) {
        char digitMax = '9';
        char lowerCaseBound = 'a';
        char upperCaseBound = 'A';
        if (radix < 10) {
            digitMax = (char) (48 + radix - 1);
        }
        if (radix > 10) {
            lowerCaseBound = (char) (97 + radix - 10);
            upperCaseBound = (char) (65 + radix - 10);
        }
        double sum = 0.0;
        int end;
        for (end = sourceStart; end <= sourceEnd; end++) {
            char c = source.charAt(end);
            int newDigit;
            if ('0' <= c && c <= digitMax) {
                newDigit = c - '0';
            } else if ('a' <= c && c < lowerCaseBound) {
                newDigit = c - 'a' + 10;
            } else {
                if ('A' > c || c >= upperCaseBound) {
                    if (!isPrefix) {
                        return Double.NaN;
                    }
                    break;
                }
                newDigit = c - 'A' + 10;
            }
            sum = sum * (double) radix + (double) newDigit;
        }
        if (sourceStart == end) {
            return Double.NaN;
        } else {
            if (sum > 9.007199254740991E15) {
                if (radix == 10) {
                    try {
                        return Double.parseDouble(source.substring(sourceStart, end));
                    } catch (NumberFormatException var26) {
                        return Double.NaN;
                    }
                }
                if (radix == 2 || radix == 4 || radix == 8 || radix == 16 || radix == 32) {
                    int bitShiftInChar = 1;
                    int digit = 0;
                    int SKIP_LEADING_ZEROS = 0;
                    int FIRST_EXACT_53_BITS = 1;
                    int AFTER_BIT_53 = 2;
                    int ZEROS_AFTER_54 = 3;
                    int MIXED_AFTER_54 = 4;
                    int state = 0;
                    int exactBitsLimit = 53;
                    double factor = 0.0;
                    boolean bit53 = false;
                    boolean bit54 = false;
                    int pos = sourceStart;
                    while (true) {
                        if (bitShiftInChar == 1) {
                            if (pos == end) {
                                switch(state) {
                                    case 0:
                                        sum = 0.0;
                                        return sum;
                                    case 1:
                                    case 2:
                                    default:
                                        return sum;
                                    case 3:
                                        if (bit54 & bit53) {
                                            sum++;
                                        }
                                        sum *= factor;
                                        return sum;
                                    case 4:
                                        if (bit54) {
                                            sum++;
                                        }
                                        sum *= factor;
                                        return sum;
                                }
                            }
                            int var29 = source.charAt(pos++);
                            if ('0' <= var29 && var29 <= '9') {
                                digit = var29 - '0';
                            } else if ('a' <= var29 && var29 <= 'z') {
                                digit = var29 - 'W';
                            } else {
                                digit = var29 - '7';
                            }
                            bitShiftInChar = radix;
                        }
                        bitShiftInChar >>= 1;
                        boolean bit = (digit & bitShiftInChar) != 0;
                        switch(state) {
                            case 0:
                                if (bit) {
                                    exactBitsLimit--;
                                    sum = 1.0;
                                    state = 1;
                                }
                                break;
                            case 1:
                                sum *= 2.0;
                                if (bit) {
                                    sum++;
                                }
                                if (--exactBitsLimit == 0) {
                                    bit53 = bit;
                                    state = 2;
                                }
                                break;
                            case 2:
                                bit54 = bit;
                                factor = 2.0;
                                state = 3;
                                break;
                            case 3:
                                if (bit) {
                                    state = 4;
                                }
                            case 4:
                                factor *= 2.0;
                        }
                    }
                }
            }
            return sum;
        }
    }

    public static double toNumber(Context cx, String s) {
        int len = s.length();
        for (int start = 0; start != len; start++) {
            char startChar = s.charAt(start);
            if (!isStrWhiteSpaceChar(startChar)) {
                int end = len - 1;
                char endChar;
                while (isStrWhiteSpaceChar(endChar = s.charAt(end))) {
                    end--;
                }
                boolean oldParsingMode = cx == null;
                if (startChar == '0') {
                    if (start + 2 <= end) {
                        char radixC = s.charAt(start + 1);
                        int radix = -1;
                        if (radixC != 'x' && radixC != 'X') {
                            if (oldParsingMode || radixC != 'o' && radixC != 'O') {
                                if (!oldParsingMode && (radixC == 'b' || radixC == 'B')) {
                                    radix = 2;
                                }
                            } else {
                                radix = 8;
                            }
                        } else {
                            radix = 16;
                        }
                        if (radix != -1) {
                            if (oldParsingMode) {
                                return stringPrefixToNumber(s, start + 2, radix);
                            }
                            return stringToNumber(s, start + 2, end, radix);
                        }
                    }
                } else if (oldParsingMode && (startChar == '+' || startChar == '-') && start + 3 <= end && s.charAt(start + 1) == '0') {
                    char radixCx = s.charAt(start + 2);
                    if (radixCx == 'x' || radixCx == 'X') {
                        double val = stringPrefixToNumber(s, start + 3, 16);
                        return startChar == '-' ? -val : val;
                    }
                }
                if (endChar == 'y') {
                    if (startChar == '+' || startChar == '-') {
                        start++;
                    }
                    if (start + 7 == end && s.regionMatches(start, "Infinity", 0, 8)) {
                        return startChar == '-' ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
                    }
                    return Double.NaN;
                }
                String sub = s.substring(start, end + 1);
                for (int i = sub.length() - 1; i >= 0; i--) {
                    char c = sub.charAt(i);
                    if (('0' > c || c > '9') && c != '.' && c != 'e' && c != 'E' && c != '+' && c != '-') {
                        return Double.NaN;
                    }
                }
                try {
                    return Double.parseDouble(sub);
                } catch (NumberFormatException var11) {
                    return Double.NaN;
                }
            }
        }
        return 0.0;
    }

    public static Object[] padArguments(Object[] args, int count) {
        if (count < args.length) {
            return args;
        } else {
            Object[] result = new Object[count];
            System.arraycopy(args, 0, result, 0, args.length);
            if (args.length < count) {
                Arrays.fill(result, args.length, count, Undefined.instance);
            }
            return result;
        }
    }

    public static String escapeString(String s) {
        return escapeString(s, '"');
    }

    public static String escapeString(String s, char escapeQuote) {
        if (escapeQuote != '"' && escapeQuote != '\'') {
            Kit.codeBug();
        }
        StringBuilder sb = null;
        int i = 0;
        for (int L = s.length(); i != L; i++) {
            int c = s.charAt(i);
            if (32 <= c && c <= 126 && c != escapeQuote && c != 92) {
                if (sb != null) {
                    sb.append((char) c);
                }
            } else {
                if (sb == null) {
                    sb = new StringBuilder(L + 3);
                    sb.append(s);
                    sb.setLength(i);
                }
                int escape = switch(c) {
                    case 8 ->
                        98;
                    case 9 ->
                        116;
                    case 10 ->
                        110;
                    case 11 ->
                        118;
                    case 12 ->
                        102;
                    case 13 ->
                        114;
                    case 32 ->
                        32;
                    case 92 ->
                        92;
                    default ->
                        -1;
                };
                if (escape >= 0) {
                    sb.append('\\');
                    sb.append((char) escape);
                } else if (c == escapeQuote) {
                    sb.append('\\');
                    sb.append(escapeQuote);
                } else {
                    int hexSize;
                    if (c < 256) {
                        sb.append("\\x");
                        hexSize = 2;
                    } else {
                        sb.append("\\u");
                        hexSize = 4;
                    }
                    for (int shift = (hexSize - 1) * 4; shift >= 0; shift -= 4) {
                        int digit = 15 & c >> shift;
                        int hc = digit < 10 ? 48 + digit : 87 + digit;
                        sb.append((char) hc);
                    }
                }
            }
        }
        return sb == null ? s : sb.toString();
    }

    static boolean isValidIdentifierName(Context cx, String s, boolean isStrict) {
        int L = s.length();
        if (L == 0) {
            return false;
        } else if (!Character.isJavaIdentifierStart(s.charAt(0))) {
            return false;
        } else {
            for (int i = 1; i != L; i++) {
                if (!Character.isJavaIdentifierPart(s.charAt(i))) {
                    return false;
                }
            }
            return !TokenStream.isKeyword(s, isStrict);
        }
    }

    public static CharSequence toCharSequence(Context cx, Object val) {
        if (val instanceof NativeString) {
            return ((NativeString) val).toCharSequence();
        } else {
            return (CharSequence) (val instanceof CharSequence ? (CharSequence) val : toString(cx, val));
        }
    }

    public static String toString(Context cx, Object val) {
        if (val == null) {
            return "null";
        } else if (val == Undefined.instance || val == Undefined.SCRIPTABLE_UNDEFINED) {
            return "undefined";
        } else if (val instanceof String) {
            return (String) val;
        } else if (val instanceof CharSequence) {
            return val.toString();
        } else if (val instanceof Number) {
            return numberToString(cx, ((Number) val).doubleValue(), 10);
        } else if (val instanceof Symbol) {
            throw typeError0(cx, "msg.not.a.string");
        } else if (val instanceof Scriptable) {
            val = ((Scriptable) val).getDefaultValue(cx, StringClass);
            if (val instanceof Scriptable && !isSymbol(val)) {
                throw errorWithClassName("msg.primitive.expected", val, cx);
            } else {
                return toString(cx, val);
            }
        } else {
            return val.toString();
        }
    }

    static String defaultObjectToString(Scriptable obj) {
        if (obj == null) {
            return "[object Null]";
        } else {
            return Undefined.isUndefined(obj) ? "[object Undefined]" : "[object " + obj.getClassName() + "]";
        }
    }

    public static String toString(Context cx, Object[] args, int index) {
        return index < args.length ? toString(cx, args[index]) : "undefined";
    }

    public static String toString(Context cx, double val) {
        return numberToString(cx, val, 10);
    }

    public static String numberToString(Context cx, double d, int base) {
        if (base < 2 || base > 36) {
            throw Context.reportRuntimeError1("msg.bad.radix", Integer.toString(base), cx);
        } else if (Double.isNaN(d)) {
            return "NaN";
        } else if (d == Double.POSITIVE_INFINITY) {
            return "Infinity";
        } else if (d == Double.NEGATIVE_INFINITY) {
            return "-Infinity";
        } else if (d == 0.0) {
            return "0";
        } else if (base != 10) {
            return DToA.JS_dtobasestr(base, d);
        } else {
            String result = FastDtoa.numberToString(d);
            if (result != null) {
                return result;
            } else {
                StringBuilder buffer = new StringBuilder();
                DToA.JS_dtostr(buffer, 0, 0, d);
                return buffer.toString();
            }
        }
    }

    static String uneval(Context cx, Scriptable scope, Object value) {
        if (value == null) {
            return "null";
        } else if (value == Undefined.instance) {
            return "undefined";
        } else if (value instanceof CharSequence) {
            String escaped = escapeString(value.toString());
            return "\"" + escaped + "\"";
        } else if (value instanceof Number) {
            double d = ((Number) value).doubleValue();
            return d == 0.0 && 1.0 / d < 0.0 ? "-0" : toString(cx, d);
        } else if (value instanceof Boolean) {
            return toString(cx, value);
        } else if (value instanceof Scriptable obj) {
            return ScriptableObject.hasProperty(obj, "toSource", cx) && ScriptableObject.getProperty(obj, "toSource", cx) instanceof Function f ? toString(cx, f.call(cx, scope, obj, EMPTY_OBJECTS)) : toString(cx, value);
        } else {
            warnAboutNonJSObject(cx, value);
            return value.toString();
        }
    }

    static String defaultObjectToSource(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return "not_supported";
    }

    @Deprecated
    public static Scriptable toObjectOrNull(Context cx, Object obj) {
        if (obj instanceof Scriptable) {
            return (Scriptable) obj;
        } else {
            return obj != null && obj != Undefined.instance ? toObject(cx, cx.getTopCallOrThrow(), obj) : null;
        }
    }

    public static Scriptable toObjectOrNull(Context cx, Object obj, Scriptable scope) {
        if (obj instanceof Scriptable) {
            return (Scriptable) obj;
        } else {
            return obj != null && obj != Undefined.instance ? toObject(cx, scope, obj) : null;
        }
    }

    public static Scriptable toObject(Context cx, Scriptable scope, Object val) {
        if (val == null) {
            throw typeError0(cx, "msg.null.to.object");
        } else if (Undefined.isUndefined(val)) {
            throw typeError0(cx, "msg.undef.to.object");
        } else if (isSymbol(val)) {
            NativeSymbol result = new NativeSymbol((NativeSymbol) val);
            setBuiltinProtoAndParent(cx, scope, result, TopLevel.Builtins.Symbol);
            return result;
        } else if (val instanceof Scriptable) {
            return (Scriptable) val;
        } else if (val instanceof CharSequence) {
            NativeString result = new NativeString((CharSequence) val);
            setBuiltinProtoAndParent(cx, scope, result, TopLevel.Builtins.String);
            return result;
        } else if (val instanceof Number) {
            NativeNumber result = new NativeNumber(cx, ((Number) val).doubleValue());
            setBuiltinProtoAndParent(cx, scope, result, TopLevel.Builtins.Number);
            return result;
        } else if (val instanceof Boolean) {
            NativeBoolean result = new NativeBoolean((Boolean) val);
            setBuiltinProtoAndParent(cx, scope, result, TopLevel.Builtins.Boolean);
            return result;
        } else {
            Object wrapped = cx.getWrapFactory().wrap(cx, scope, val, null);
            if (wrapped instanceof Scriptable) {
                return (Scriptable) wrapped;
            } else {
                throw errorWithClassName("msg.invalid.type", val, cx);
            }
        }
    }

    public static Scriptable newObject(Context cx, Scriptable scope, String constructorName, Object[] args) {
        scope = ScriptableObject.getTopLevelScope(scope);
        Function ctor = getExistingCtor(cx, scope, constructorName);
        if (args == null) {
            args = EMPTY_OBJECTS;
        }
        return ctor.construct(cx, scope, args);
    }

    public static Scriptable newBuiltinObject(Context cx, Scriptable scope, TopLevel.Builtins type, Object[] args) {
        scope = ScriptableObject.getTopLevelScope(scope);
        Function ctor = TopLevel.getBuiltinCtor(cx, scope, type);
        if (args == null) {
            args = EMPTY_OBJECTS;
        }
        return ctor.construct(cx, scope, args);
    }

    static Scriptable newNativeError(Context cx, Scriptable scope, TopLevel.NativeErrors type, Object[] args) {
        scope = ScriptableObject.getTopLevelScope(scope);
        Function ctor = TopLevel.getNativeErrorCtor(cx, scope, type);
        if (args == null) {
            args = EMPTY_OBJECTS;
        }
        return ctor.construct(cx, scope, args);
    }

    public static double toInteger(Context cx, Object val) {
        return toInteger(toNumber(cx, val));
    }

    public static double toInteger(double d) {
        if (Double.isNaN(d)) {
            return 0.0;
        } else if (d == 0.0 || Double.isInfinite(d)) {
            return d;
        } else {
            return d > 0.0 ? Math.floor(d) : Math.ceil(d);
        }
    }

    public static double toInteger(Context cx, Object[] args, int index) {
        return index < args.length ? toInteger(cx, args[index]) : 0.0;
    }

    public static long toLength(Context cx, Object[] args, int index) {
        double len = toInteger(cx, args, index);
        return len <= 0.0 ? 0L : (long) Math.min(len, 9.007199254740991E15);
    }

    public static int toInt32(Context cx, Object val) {
        return val instanceof Integer ? (Integer) val : toInt32(toNumber(cx, val));
    }

    public static int toInt32(Context cx, Object[] args, int index) {
        return index < args.length ? toInt32(cx, args[index]) : 0;
    }

    public static int toInt32(double d) {
        return DoubleConversion.doubleToInt32(d);
    }

    public static long toUint32(double d) {
        return (long) DoubleConversion.doubleToInt32(d) & 4294967295L;
    }

    public static long toUint32(Context cx, Object val) {
        return toUint32(toNumber(cx, val));
    }

    public static char toUint16(Context cx, Object val) {
        double d = toNumber(cx, val);
        return (char) DoubleConversion.doubleToInt32(d);
    }

    public static Object getTopLevelProp(Context cx, Scriptable scope, String id) {
        scope = ScriptableObject.getTopLevelScope(scope);
        return ScriptableObject.getProperty(scope, id, cx);
    }

    static Function getExistingCtor(Context cx, Scriptable scope, String constructorName) {
        Object ctorVal = ScriptableObject.getProperty(scope, constructorName, cx);
        if (ctorVal instanceof Function) {
            return (Function) ctorVal;
        } else if (ctorVal == Scriptable.NOT_FOUND) {
            throw Context.reportRuntimeError1("msg.ctor.not.found", constructorName, cx);
        } else {
            throw Context.reportRuntimeError1("msg.not.ctor", constructorName, cx);
        }
    }

    public static long indexFromString(String str) {
        int MAX_VALUE_LENGTH = 10;
        int len = str.length();
        if (len > 0) {
            int i = 0;
            boolean negate = false;
            int c = str.charAt(0);
            if (c == 45 && len > 1) {
                c = str.charAt(1);
                if (c == 48) {
                    return -1L;
                }
                i = 1;
                negate = true;
            }
            c -= 48;
            if (0 <= c && c <= 9 && len <= (negate ? 11 : 10)) {
                int index = -c;
                int oldIndex = 0;
                i++;
                if (index != 0) {
                    while (i != len && 0 <= (c = str.charAt(i) - '0') && c <= 9) {
                        oldIndex = index;
                        index = 10 * index - c;
                        i++;
                    }
                }
                if (i == len && (oldIndex > -214748364 || oldIndex == -214748364 && c <= (negate ? 8 : 7))) {
                    return 4294967295L & (long) (negate ? index : -index);
                }
            }
        }
        return -1L;
    }

    public static long testUint32String(String str) {
        int MAX_VALUE_LENGTH = 10;
        int len = str.length();
        if (1 <= len && len <= 10) {
            int c = str.charAt(0);
            c -= 48;
            if (c == 0) {
                return len == 1 ? 0L : -1L;
            }
            if (1 <= c && c <= 9) {
                long v = (long) c;
                for (int i = 1; i != len; i++) {
                    c = str.charAt(i) - '0';
                    if (0 > c || c > 9) {
                        return -1L;
                    }
                    v = 10L * v + (long) c;
                }
                if (v >>> 32 == 0L) {
                    return v;
                }
            }
        }
        return -1L;
    }

    static Object getIndexObject(String s) {
        long indexTest = indexFromString(s);
        return indexTest >= 0L ? (int) indexTest : s;
    }

    static Object getIndexObject(Context cx, double d) {
        int i = (int) d;
        return (double) i == d ? i : toString(cx, d);
    }

    static ScriptRuntime.StringIdOrIndex toStringIdOrIndex(Context cx, Object id) {
        if (id instanceof Number) {
            double d = ((Number) id).doubleValue();
            int index = (int) d;
            return (double) index == d ? new ScriptRuntime.StringIdOrIndex(index) : new ScriptRuntime.StringIdOrIndex(toString(cx, id));
        } else {
            if (!(id instanceof String s)) {
                s = toString(cx, id);
            }
            long indexTest = indexFromString(s);
            return indexTest >= 0L ? new ScriptRuntime.StringIdOrIndex((int) indexTest) : new ScriptRuntime.StringIdOrIndex(s);
        }
    }

    public static Object getObjectElem(Context cx, Scriptable scope, Object obj, Object elem) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefReadError(cx, obj, elem);
        } else {
            return getObjectElem(cx, sobj, elem);
        }
    }

    public static Object getObjectElem(Context cx, Scriptable obj, Object elem) {
        Object result;
        if (isSymbol(elem)) {
            result = ScriptableObject.getProperty(obj, (Symbol) elem, cx);
        } else {
            ScriptRuntime.StringIdOrIndex s = toStringIdOrIndex(cx, elem);
            if (s.stringId == null) {
                int index = s.index;
                result = ScriptableObject.getProperty(obj, index, cx);
            } else {
                result = ScriptableObject.getProperty(obj, s.stringId, cx);
            }
        }
        if (result == Scriptable.NOT_FOUND) {
            result = Undefined.instance;
        }
        return result;
    }

    public static Object getObjectProp(Context cx, Scriptable scope, Object obj, String property) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefReadError(cx, obj, property);
        } else {
            return getObjectProp(cx, sobj, property);
        }
    }

    public static Object getObjectProp(Context cx, Scriptable obj, String property) {
        Object result = ScriptableObject.getProperty(obj, property, cx);
        return result == Scriptable.NOT_FOUND ? Undefined.instance : result;
    }

    public static Object getObjectPropNoWarn(Context cx, Scriptable scope, Object obj, String property) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefReadError(cx, obj, property);
        } else {
            Object result = ScriptableObject.getProperty(sobj, property, cx);
            return result == Scriptable.NOT_FOUND ? Undefined.instance : result;
        }
    }

    public static Object getObjectPropOptional(Context cx, Scriptable scope, Object obj, String property) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            return Undefined.instance;
        } else {
            Object result = ScriptableObject.getProperty(sobj, property, cx);
            return result == Scriptable.NOT_FOUND ? Undefined.instance : result;
        }
    }

    public static Object getObjectIndex(Context cx, Scriptable scope, Object obj, double dblIndex) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefReadError(cx, obj, toString(cx, dblIndex));
        } else {
            int index = (int) dblIndex;
            if ((double) index == dblIndex) {
                return getObjectIndex(cx, sobj, index);
            } else {
                String s = toString(cx, dblIndex);
                return getObjectProp(cx, sobj, s);
            }
        }
    }

    public static Object getObjectIndex(Context cx, Scriptable obj, int index) {
        Object result = ScriptableObject.getProperty(obj, index, cx);
        if (result == Scriptable.NOT_FOUND) {
            result = Undefined.instance;
        }
        return result;
    }

    public static Object setObjectElem(Context cx, Scriptable scope, Object obj, Object elem, Object value) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefWriteError(cx, obj, elem, value);
        } else {
            return setObjectElem(cx, sobj, elem, value);
        }
    }

    public static Object setObjectElem(Context cx, Scriptable obj, Object elem, Object value) {
        if (isSymbol(elem)) {
            ScriptableObject.putProperty(obj, (Symbol) elem, value, cx);
        } else {
            ScriptRuntime.StringIdOrIndex s = toStringIdOrIndex(cx, elem);
            if (s.stringId == null) {
                ScriptableObject.putProperty(obj, s.index, value, cx);
            } else {
                ScriptableObject.putProperty(obj, s.stringId, value, cx);
            }
        }
        return value;
    }

    public static Object setObjectProp(Context cx, Scriptable scope, Object obj, String property, Object value) {
        if (!(obj instanceof Scriptable) && cx.isStrictMode()) {
            throw undefWriteError(cx, obj, property, value);
        } else {
            Scriptable sobj = toObjectOrNull(cx, obj, scope);
            if (sobj == null) {
                throw undefWriteError(cx, obj, property, value);
            } else {
                return setObjectProp(cx, sobj, property, value);
            }
        }
    }

    public static Object setObjectProp(Context cx, Scriptable obj, String property, Object value) {
        ScriptableObject.putProperty(obj, property, value, cx);
        return value;
    }

    public static Object setObjectIndex(Context cx, Scriptable scope, Object obj, double dblIndex, Object value) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefWriteError(cx, obj, String.valueOf(dblIndex), value);
        } else {
            int index = (int) dblIndex;
            if ((double) index == dblIndex) {
                return setObjectIndex(cx, sobj, index, value);
            } else {
                String s = toString(cx, dblIndex);
                return setObjectProp(cx, sobj, s, value);
            }
        }
    }

    public static Object setObjectIndex(Context cx, Scriptable obj, int index, Object value) {
        ScriptableObject.putProperty(obj, index, value, cx);
        return value;
    }

    public static boolean deleteObjectElem(Context cx, Scriptable target, Object elem) {
        if (isSymbol(elem)) {
            SymbolScriptable so = ScriptableObject.ensureSymbolScriptable(target, cx);
            Symbol s = (Symbol) elem;
            so.delete(cx, s);
            return !so.has(cx, s, target);
        } else {
            ScriptRuntime.StringIdOrIndex s = toStringIdOrIndex(cx, elem);
            if (s.stringId == null) {
                target.delete(cx, s.index);
                return !target.has(cx, s.index, target);
            } else {
                target.delete(cx, s.stringId);
                return !target.has(cx, s.stringId, target);
            }
        }
    }

    public static boolean hasObjectElem(Context cx, Scriptable target, Object elem) {
        boolean result;
        if (isSymbol(elem)) {
            result = ScriptableObject.hasProperty(target, (Symbol) elem, cx);
        } else {
            ScriptRuntime.StringIdOrIndex s = toStringIdOrIndex(cx, elem);
            if (s.stringId == null) {
                result = ScriptableObject.hasProperty(target, s.index, cx);
            } else {
                result = ScriptableObject.hasProperty(target, s.stringId, cx);
            }
        }
        return result;
    }

    public static Object refGet(Context cx, Ref ref) {
        return ref.get(cx);
    }

    public static Object refSet(Context cx, Scriptable scope, Ref ref, Object value) {
        return ref.set(cx, scope, value);
    }

    public static Object refDel(Context cx, Ref ref) {
        return ref.delete(cx);
    }

    static boolean isSpecialProperty(String s) {
        return s.equals("__proto__") || s.equals("__parent__");
    }

    public static Ref specialRef(Context cx, Scriptable scope, Object obj, String specialProperty) {
        return SpecialRef.createSpecial(cx, scope, obj, specialProperty);
    }

    public static Object delete(Context cx, Scriptable scope, Object obj, Object id, boolean isName) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            if (isName) {
                return Boolean.TRUE;
            } else {
                throw undefDeleteError(cx, obj, id);
            }
        } else {
            return deleteObjectElem(cx, sobj, id);
        }
    }

    public static Object name(Context cx, Scriptable scope, String name) {
        Scriptable parent = scope.getParentScope();
        if (parent == null) {
            Object result = topScopeName(cx, scope, name);
            if (result == Scriptable.NOT_FOUND) {
                throw notFoundError(cx, scope, name);
            } else {
                return result;
            }
        } else {
            return nameOrFunction(cx, scope, parent, name, false);
        }
    }

    private static Object nameOrFunction(Context cx, Scriptable scope, Scriptable parentScope, String name, boolean asFunctionCall) {
        Scriptable thisObj = scope;
        Object result;
        while (true) {
            if (scope instanceof NativeWith) {
                Scriptable withObj = scope.getPrototype(cx);
                result = ScriptableObject.getProperty(withObj, name, cx);
                if (result != Scriptable.NOT_FOUND) {
                    thisObj = withObj;
                    break;
                }
            } else if (scope instanceof NativeCall) {
                result = scope.get(cx, name, scope);
                if (result != Scriptable.NOT_FOUND) {
                    if (asFunctionCall) {
                        thisObj = ScriptableObject.getTopLevelScope(parentScope);
                    }
                    break;
                }
            } else {
                result = ScriptableObject.getProperty(scope, name, cx);
                if (result != Scriptable.NOT_FOUND) {
                    thisObj = scope;
                    break;
                }
            }
            scope = parentScope;
            parentScope = parentScope.getParentScope();
            if (parentScope == null) {
                result = topScopeName(cx, scope, name);
                if (result == Scriptable.NOT_FOUND) {
                    throw notFoundError(cx, scope, name);
                }
                thisObj = scope;
                break;
            }
        }
        if (asFunctionCall) {
            if (!(result instanceof Callable)) {
                throw notFunctionError(cx, result, name);
            }
            cx.storeScriptable(thisObj);
        }
        return result;
    }

    private static Object topScopeName(Context cx, Scriptable scope, String name) {
        return ScriptableObject.getProperty(scope, name, cx);
    }

    public static Scriptable bind(Context cx, Scriptable scope, String id) {
        Scriptable firstXMLObject = null;
        Scriptable parent = scope.getParentScope();
        if (parent != null) {
            do {
                if (!(scope instanceof NativeWith)) {
                    while (!ScriptableObject.hasProperty(scope, id, cx)) {
                        scope = parent;
                        parent = parent.getParentScope();
                        if (parent == null) {
                            return ScriptableObject.hasProperty(scope, id, cx) ? scope : firstXMLObject;
                        }
                    }
                    return scope;
                }
                Scriptable withObj = scope.getPrototype(cx);
                if (ScriptableObject.hasProperty(withObj, id, cx)) {
                    return withObj;
                }
                scope = parent;
                parent = parent.getParentScope();
            } while (parent != null);
        }
        return ScriptableObject.hasProperty(scope, id, cx) ? scope : firstXMLObject;
    }

    public static Object setName(Context cx, Scriptable scope, Scriptable bound, Object value, String id) {
        if (bound != null) {
            ScriptableObject.putProperty(bound, id, value, cx);
        } else {
            Context.reportError(cx, getMessage1("msg.assn.create.strict", id));
            bound = ScriptableObject.getTopLevelScope(scope);
            bound.put(cx, id, bound, value);
        }
        return value;
    }

    public static Object strictSetName(Context cx, Scriptable scope, Scriptable bound, Object value, String id) {
        if (bound != null) {
            ScriptableObject.putProperty(bound, id, value, cx);
            return value;
        } else {
            String msg = "Assignment to undefined \"" + id + "\" in strict mode";
            throw constructError(cx, "ReferenceError", msg);
        }
    }

    public static Object setConst(Context cx, Scriptable bound, Object value, String id) {
        ScriptableObject.putConstProperty(bound, id, value, cx);
        return value;
    }

    public static Scriptable toIterator(Context cx, Scriptable scope, Scriptable obj, boolean keyOnly) {
        if (ScriptableObject.hasProperty(obj, "__iterator__", cx)) {
            if (ScriptableObject.getProperty(obj, "__iterator__", cx) instanceof Callable f) {
                Object[] args = new Object[] { keyOnly ? Boolean.TRUE : Boolean.FALSE };
                Object var7 = f.call(cx, scope, obj, args);
                if (!(var7 instanceof Scriptable)) {
                    throw typeError0(cx, "msg.iterator.primitive");
                } else {
                    return (Scriptable) var7;
                }
            } else {
                throw typeError0(cx, "msg.invalid.iterator");
            }
        } else {
            return null;
        }
    }

    public static IdEnumeration enumInit(Context cx, Scriptable scope, Object value, int enumType) {
        IdEnumeration x = new IdEnumeration();
        x.obj = toObjectOrNull(cx, value, scope);
        if (enumType == 6) {
            x.enumType = enumType;
            x.iterator = null;
            return enumInitInOrder(cx, x);
        } else if (x.obj == null) {
            return x;
        } else {
            x.enumType = enumType;
            x.iterator = null;
            if (enumType != 3 && enumType != 4 && enumType != 5) {
                x.iterator = toIterator(cx, x.obj.getParentScope(), x.obj, enumType == 0);
            }
            if (x.iterator == null) {
                x.changeObject(cx);
            }
            return x;
        }
    }

    private static IdEnumeration enumInitInOrder(Context cx, IdEnumeration x) {
        Object iterator = x.obj instanceof SymbolScriptable ? ScriptableObject.getProperty(x.obj, SymbolKey.ITERATOR, cx) : null;
        if (iterator instanceof Callable f) {
            Scriptable scope = x.obj.getParentScope();
            Object v = f.call(cx, scope, x.obj, EMPTY_OBJECTS);
            if (!(v instanceof Scriptable)) {
                if (v instanceof IdEnumerationIterator) {
                    x.iterator = (IdEnumerationIterator) v;
                    return x;
                } else {
                    throw typeError1(cx, "msg.not.iterable", toString(cx, x.obj));
                }
            } else {
                x.iterator = (Scriptable) v;
                return x;
            }
        } else if (iterator instanceof IdEnumerationIterator) {
            x.iterator = (IdEnumerationIterator) iterator;
            return x;
        } else {
            throw typeError1(cx, "msg.not.iterable", toString(cx, x.obj));
        }
    }

    public static Callable getNameFunctionAndThis(Context cx, Scriptable scope, String name) {
        Scriptable parent = scope.getParentScope();
        if (parent == null) {
            Object result = topScopeName(cx, scope, name);
            if (!(result instanceof Callable)) {
                if (result == Scriptable.NOT_FOUND) {
                    throw notFoundError(cx, scope, name);
                } else {
                    throw notFunctionError(cx, result, name);
                }
            } else {
                cx.storeScriptable(scope);
                return (Callable) result;
            }
        } else {
            return (Callable) nameOrFunction(cx, scope, parent, name, true);
        }
    }

    public static Callable getElemFunctionAndThis(Context cx, Scriptable scope, Object obj, Object elem) {
        Scriptable thisObj;
        Object value;
        if (isSymbol(elem)) {
            thisObj = toObjectOrNull(cx, obj, scope);
            if (thisObj == null) {
                throw undefCallError(cx, obj, String.valueOf(elem));
            }
            value = ScriptableObject.getProperty(thisObj, (Symbol) elem, cx);
        } else {
            ScriptRuntime.StringIdOrIndex s = toStringIdOrIndex(cx, elem);
            if (s.stringId != null) {
                return getPropFunctionAndThis(cx, scope, obj, s.stringId);
            }
            thisObj = toObjectOrNull(cx, obj, scope);
            if (thisObj == null) {
                throw undefCallError(cx, obj, String.valueOf(elem));
            }
            value = ScriptableObject.getProperty(thisObj, s.index, cx);
        }
        if (!(value instanceof Callable)) {
            throw notFunctionError(cx, value, elem);
        } else {
            cx.storeScriptable(thisObj);
            return (Callable) value;
        }
    }

    public static Callable getPropFunctionAndThis(Context cx, Scriptable scope, Object obj, String property) {
        Scriptable thisObj = toObjectOrNull(cx, obj, scope);
        return getPropFunctionAndThisHelper(cx, thisObj, obj, property);
    }

    private static Callable getPropFunctionAndThisHelper(Context cx, Scriptable thisObj, Object obj, String property) {
        if (thisObj == null) {
            throw undefCallError(cx, obj, property);
        } else {
            Object value = ScriptableObject.getProperty(thisObj, property, cx);
            if (!(value instanceof Callable)) {
                Object noSuchMethod = ScriptableObject.getProperty(thisObj, "__noSuchMethod__", cx);
                if (noSuchMethod instanceof Callable) {
                    value = new ScriptRuntime.NoSuchMethodShim((Callable) noSuchMethod, property);
                }
            }
            if (!(value instanceof Callable)) {
                throw notFunctionError(cx, thisObj, value, property);
            } else {
                cx.storeScriptable(thisObj);
                return (Callable) value;
            }
        }
    }

    public static Callable getValueFunctionAndThis(Context cx, Object value) {
        if (value instanceof Callable f) {
            Scriptable thisObj = null;
            if (f instanceof Scriptable) {
                thisObj = ((Scriptable) f).getParentScope();
            }
            if (thisObj == null) {
                thisObj = cx.getTopCallOrThrow();
            }
            if (thisObj.getParentScope() != null && !(thisObj instanceof NativeWith) && thisObj instanceof NativeCall) {
                thisObj = ScriptableObject.getTopLevelScope(thisObj);
            }
            cx.storeScriptable(thisObj);
            return f;
        } else {
            throw notFunctionError(cx, value);
        }
    }

    public static Object callIterator(Context cx, Scriptable scope, Object obj) {
        Callable getIterator = getElemFunctionAndThis(cx, scope, obj, SymbolKey.ITERATOR);
        Scriptable iterable = cx.lastStoredScriptable();
        return getIterator.call(cx, scope, iterable, EMPTY_OBJECTS);
    }

    public static boolean isIteratorDone(Context cx, Object result) {
        if (!(result instanceof Scriptable)) {
            return false;
        } else {
            Object prop = getObjectProp(cx, (Scriptable) result, "done");
            return toBoolean(cx, prop);
        }
    }

    public static Ref callRef(Context cx, Scriptable thisObj, Callable function, Object[] args) {
        if (function instanceof RefCallable rfunction) {
            Ref ref = rfunction.refCall(cx, thisObj, args);
            if (ref == null) {
                throw new IllegalStateException(rfunction.getClass().getName() + ".refCall() returned null");
            } else {
                return ref;
            }
        } else {
            String msg = getMessage1("msg.no.ref.from.function", toString(cx, function));
            throw constructError(cx, "ReferenceError", msg);
        }
    }

    public static Scriptable newObject(Object fun, Context cx, Scriptable scope, Object[] args) {
        if (fun instanceof Function function) {
            return function.construct(cx, scope, args);
        } else {
            throw notFunctionError(cx, fun);
        }
    }

    public static Object callSpecial(Context cx, Scriptable scope, Callable fun, Scriptable thisObj, Object[] args, Scriptable callerThis, int callType, String filename, int lineNumber) {
        if (callType == 1) {
            if (thisObj.getParentScope() == null && NativeGlobal.isEvalFunction(fun)) {
                return evalSpecial(cx, scope, callerThis, args, filename, lineNumber);
            }
        } else {
            if (callType != 2) {
                throw Kit.codeBug();
            }
            if (NativeWith.isWithFunction(fun)) {
                throw Context.reportRuntimeError1("msg.only.from.new", "With", cx);
            }
        }
        return fun.call(cx, scope, thisObj, args);
    }

    public static Object newSpecial(Context cx, Scriptable scope, Object fun, Object[] args, int callType) {
        if (callType == 1) {
            if (NativeGlobal.isEvalFunction(fun)) {
                throw typeError1(cx, "msg.not.ctor", "eval");
            }
        } else {
            if (callType != 2) {
                throw Kit.codeBug();
            }
            if (NativeWith.isWithFunction(fun)) {
                return NativeWith.newWithSpecial(cx, scope, args);
            }
        }
        return newObject(fun, cx, scope, args);
    }

    public static Object applyOrCall(Context cx, Scriptable scope, boolean isApply, Scriptable thisObj, Object[] args) {
        int L = args.length;
        Callable function = getCallable(cx, thisObj);
        Scriptable callThis = null;
        if (L != 0) {
            callThis = args[0] == Undefined.instance ? Undefined.SCRIPTABLE_UNDEFINED : toObjectOrNull(cx, args[0], scope);
        }
        Object[] callArgs;
        if (isApply) {
            callArgs = L <= 1 ? EMPTY_OBJECTS : getApplyArguments(cx, args[1]);
        } else if (L <= 1) {
            callArgs = EMPTY_OBJECTS;
        } else {
            callArgs = new Object[L - 1];
            System.arraycopy(args, 1, callArgs, 0, L - 1);
        }
        return function.call(cx, scope, callThis, callArgs);
    }

    private static boolean isArrayLike(Context cx, Scriptable obj) {
        return obj != null && (obj instanceof NativeArray || obj instanceof Arguments || ScriptableObject.hasProperty(obj, "length", cx));
    }

    static Object[] getApplyArguments(Context cx, Object arg1) {
        if (arg1 == null || arg1 == Undefined.instance) {
            return EMPTY_OBJECTS;
        } else if (arg1 instanceof Scriptable && isArrayLike(cx, (Scriptable) arg1)) {
            return getArrayElements(cx, (Scriptable) arg1);
        } else if (arg1 instanceof ScriptableObject) {
            return EMPTY_OBJECTS;
        } else {
            throw typeError0(cx, "msg.arg.isnt.array");
        }
    }

    static Callable getCallable(Context cx, Scriptable thisObj) {
        if (!(thisObj instanceof Callable functionx)) {
            Object value = thisObj.getDefaultValue(cx, FunctionClass);
            if (!(value instanceof Callable functionx)) {
                throw notFunctionError(cx, value, thisObj);
            }
        }
        return functionx;
    }

    public static Object evalSpecial(Context cx, Scriptable scope, Object thisArg, Object[] args, String filename, int lineNumber) {
        if (args.length < 1) {
            return Undefined.instance;
        } else {
            Object x = args[0];
            if (!(x instanceof CharSequence)) {
                String message = getMessage0("msg.eval.nonstring");
                Context.reportWarning(message, cx);
                return x;
            } else {
                if (filename == null) {
                    int[] linep = new int[1];
                    filename = Context.getSourcePositionFromStack(cx, linep);
                    if (filename != null) {
                        lineNumber = linep[0];
                    } else {
                        filename = "";
                    }
                }
                String sourceName = makeUrlForGeneratedScript(true, filename, lineNumber);
                ErrorReporter reporter = DefaultErrorReporter.forEval(cx.getErrorReporter());
                Evaluator evaluator = Context.createInterpreter();
                if (evaluator == null) {
                    throw new JavaScriptException(cx, "Interpreter not present", filename, lineNumber);
                } else {
                    Script script = cx.compileString(x.toString(), evaluator, reporter, sourceName, 1, null);
                    evaluator.setEvalScriptFlag(script);
                    Callable c = (Callable) script;
                    return c.call(cx, scope, (Scriptable) thisArg, EMPTY_OBJECTS);
                }
            }
        }
    }

    public static MemberType typeof(Context cx, Object value) {
        return MemberType.get(value, cx);
    }

    public static MemberType typeofName(Context cx, Scriptable scope, String id) {
        Scriptable val = bind(cx, scope, id);
        return val == null ? MemberType.UNDEFINED : typeof(cx, getObjectProp(cx, val, id));
    }

    public static boolean isObject(Object value) {
        if (value == null) {
            return false;
        } else if (Undefined.instance.equals(value)) {
            return false;
        } else if (!(value instanceof ScriptableObject)) {
            return value instanceof Scriptable ? !(value instanceof Callable) : false;
        } else {
            MemberType type = ((ScriptableObject) value).getTypeOf();
            return type == MemberType.OBJECT || type == MemberType.FUNCTION;
        }
    }

    public static Object add(Context cx, Object val1, Object val2) {
        if (val1 instanceof Number && val2 instanceof Number) {
            return wrapNumber(((Number) val1).doubleValue() + ((Number) val2).doubleValue());
        } else if (!(val1 instanceof Symbol) && !(val2 instanceof Symbol)) {
            if (val1 instanceof Scriptable) {
                val1 = ((Scriptable) val1).getDefaultValue(cx, null);
            }
            if (val2 instanceof Scriptable) {
                val2 = ((Scriptable) val2).getDefaultValue(cx, null);
            }
            if (val1 instanceof CharSequence || val2 instanceof CharSequence) {
                return new ConsString(toCharSequence(cx, val1), toCharSequence(cx, val2));
            } else {
                return val1 instanceof Number && val2 instanceof Number ? wrapNumber(((Number) val1).doubleValue() + ((Number) val2).doubleValue()) : wrapNumber(toNumber(cx, val1) + toNumber(cx, val2));
            }
        } else {
            throw typeError0(cx, "msg.not.a.number");
        }
    }

    public static CharSequence add(Context cx, CharSequence val1, Object val2) {
        return new ConsString(val1, toCharSequence(cx, val2));
    }

    public static CharSequence add(Context cx, Object val1, CharSequence val2) {
        return new ConsString(toCharSequence(cx, val1), val2);
    }

    public static Object nameIncrDecr(Context cx, Scriptable scopeChain, String id, int incrDecrMask) {
        do {
            Scriptable target = scopeChain;
            do {
                Object value = target.get(cx, id, scopeChain);
                if (value != Scriptable.NOT_FOUND) {
                    return doScriptableIncrDecr(cx, target, id, scopeChain, value, incrDecrMask);
                }
                target = target.getPrototype(cx);
            } while (target != null);
            scopeChain = scopeChain.getParentScope();
        } while (scopeChain != null);
        throw notFoundError(cx, null, id);
    }

    public static Object propIncrDecr(Context cx, Scriptable scope, Object obj, String id, int incrDecrMask) {
        Scriptable start = toObjectOrNull(cx, obj, scope);
        if (start == null) {
            throw undefReadError(cx, obj, id);
        } else {
            Scriptable target = start;
            do {
                Object value = target.get(cx, id, start);
                if (value != Scriptable.NOT_FOUND) {
                    return doScriptableIncrDecr(cx, target, id, start, value, incrDecrMask);
                }
                target = target.getPrototype(cx);
            } while (target != null);
            start.put(cx, id, start, NaNobj);
            return NaNobj;
        }
    }

    private static Object doScriptableIncrDecr(Context cx, Scriptable target, String id, Scriptable protoChainStart, Object value, int incrDecrMask) {
        boolean post = (incrDecrMask & 2) != 0;
        double number;
        if (value instanceof Number) {
            number = ((Number) value).doubleValue();
        } else {
            number = toNumber(cx, value);
            if (post) {
                value = wrapNumber(number);
            }
        }
        if ((incrDecrMask & 1) == 0) {
            number++;
        } else {
            number--;
        }
        Number result = wrapNumber(number);
        target.put(cx, id, protoChainStart, result);
        return post ? value : result;
    }

    public static Object elemIncrDecr(Context cx, Object obj, Object index, Scriptable scope, int incrDecrMask) {
        Object value = getObjectElem(cx, scope, obj, index);
        boolean post = (incrDecrMask & 2) != 0;
        double number;
        if (value instanceof Number) {
            number = ((Number) value).doubleValue();
        } else {
            number = toNumber(cx, value);
            if (post) {
                value = wrapNumber(number);
            }
        }
        if ((incrDecrMask & 1) == 0) {
            number++;
        } else {
            number--;
        }
        Number result = wrapNumber(number);
        setObjectElem(cx, scope, obj, index, result);
        return post ? value : result;
    }

    public static Object refIncrDecr(Context cx, Scriptable scope, Ref ref, int incrDecrMask) {
        Object value = ref.get(cx);
        boolean post = (incrDecrMask & 2) != 0;
        double number;
        if (value instanceof Number) {
            number = ((Number) value).doubleValue();
        } else {
            number = toNumber(cx, value);
            if (post) {
                value = wrapNumber(number);
            }
        }
        if ((incrDecrMask & 1) == 0) {
            number++;
        } else {
            number--;
        }
        Number result = wrapNumber(number);
        ref.set(cx, scope, result);
        return post ? value : result;
    }

    public static Object toPrimitive(Context cx, Object val) {
        return toPrimitive(cx, val, null);
    }

    public static Object toPrimitive(Context cx, Object val, Class<?> typeHint) {
        if (val instanceof Scriptable s) {
            Object result = s.getDefaultValue(cx, typeHint);
            if (result instanceof Scriptable && !isSymbol(result)) {
                throw typeError0(cx, "msg.bad.default.value");
            } else {
                return result;
            }
        } else {
            return val;
        }
    }

    public static boolean eq(Context cx, Object x, Object y) {
        if (x != null && x != Undefined.instance) {
            if (x == y) {
                return true;
            } else {
                Object x1 = Wrapper.unwrapped(x);
                Object y1 = Wrapper.unwrapped(y);
                if (x1 == y1) {
                    return true;
                } else if (SpecialEquality.checkSpecialEquality(x1, y1, false)) {
                    return true;
                } else if (SpecialEquality.checkSpecialEquality(y1, x1, false)) {
                    return true;
                } else if (x instanceof Number) {
                    return eqNumber(cx, ((Number) x).doubleValue(), y);
                } else if (x instanceof CharSequence) {
                    return eqString(cx, (CharSequence) x, y);
                } else if (x instanceof Boolean) {
                    boolean b = (Boolean) x;
                    if (y instanceof Boolean) {
                        return b == (Boolean) y;
                    } else {
                        if (y instanceof ScriptableObject) {
                            Object test = ((ScriptableObject) y).equivalentValues(x);
                            if (test != Scriptable.NOT_FOUND) {
                                return (Boolean) test;
                            }
                        }
                        return eqNumber(cx, b ? 1.0 : 0.0, y);
                    }
                } else if (!(x instanceof Scriptable)) {
                    warnAboutNonJSObject(cx, x);
                    return x == y;
                } else if (!(y instanceof Scriptable)) {
                    if (y instanceof Boolean) {
                        if (x instanceof ScriptableObject) {
                            Object test = ((ScriptableObject) x).equivalentValues(y);
                            if (test != Scriptable.NOT_FOUND) {
                                return (Boolean) test;
                            }
                        }
                        double d = (Boolean) y ? 1.0 : 0.0;
                        return eqNumber(cx, d, x);
                    } else if (y instanceof Number) {
                        return eqNumber(cx, ((Number) y).doubleValue(), x);
                    } else {
                        return y instanceof CharSequence ? eqString(cx, (CharSequence) y, x) : false;
                    }
                } else {
                    if (x instanceof ScriptableObject) {
                        Object test = ((ScriptableObject) x).equivalentValues(y);
                        if (test != Scriptable.NOT_FOUND) {
                            return (Boolean) test;
                        }
                    }
                    if (y instanceof ScriptableObject) {
                        Object test = ((ScriptableObject) y).equivalentValues(x);
                        if (test != Scriptable.NOT_FOUND) {
                            return (Boolean) test;
                        }
                    }
                    return x instanceof Wrapper && y instanceof Wrapper ? x1 == y1 || isPrimitive(x1) && isPrimitive(y1) && eq(cx, x1, y1) : false;
                }
            }
        } else if (y != null && y != Undefined.instance) {
            if (y instanceof ScriptableObject) {
                Object test = ((ScriptableObject) y).equivalentValues(x);
                if (test != Scriptable.NOT_FOUND) {
                    return (Boolean) test;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public static boolean same(Context cx, Object x, Object y) {
        if (typeof(cx, x) != typeof(cx, y)) {
            return false;
        } else if (x instanceof Number) {
            return isNaN(x) && isNaN(y) ? true : x.equals(y);
        } else {
            return eq(cx, x, y);
        }
    }

    public static boolean sameZero(Context cx, Object x, Object y) {
        x = Wrapper.unwrapped(x);
        y = Wrapper.unwrapped(y);
        if (typeof(cx, x) != typeof(cx, y)) {
            return false;
        } else if (x instanceof Number) {
            if (isNaN(x) && isNaN(y)) {
                return true;
            } else {
                double dx = ((Number) x).doubleValue();
                if (y instanceof Number) {
                    double dy = ((Number) y).doubleValue();
                    if (dx == negativeZero && dy == 0.0 || dx == 0.0 && dy == negativeZero) {
                        return true;
                    }
                }
                return eqNumber(cx, dx, y);
            }
        } else {
            return eq(cx, x, y);
        }
    }

    public static boolean isNaN(Object n) {
        if (n instanceof Double) {
            return ((Double) n).isNaN();
        } else {
            return n instanceof Float ? ((Float) n).isNaN() : false;
        }
    }

    public static boolean isPrimitive(Object obj) {
        return obj == null || obj == Undefined.instance || obj instanceof Number || obj instanceof String || obj instanceof Boolean;
    }

    static boolean eqNumber(Context cx, double x, Object y) {
        if (y == null || y == Undefined.instance) {
            return false;
        } else if (y instanceof Number) {
            return x == ((Number) y).doubleValue();
        } else if (y instanceof CharSequence) {
            return x == toNumber(cx, y);
        } else if (y instanceof Boolean) {
            return x == ((Boolean) y ? 1.0 : 0.0);
        } else if (isSymbol(y)) {
            return false;
        } else if (y instanceof Scriptable) {
            if (y instanceof ScriptableObject) {
                Object xval = wrapNumber(x);
                Object test = ((ScriptableObject) y).equivalentValues(xval);
                if (test != Scriptable.NOT_FOUND) {
                    return (Boolean) test;
                }
            }
            return eqNumber(cx, x, toPrimitive(cx, y));
        } else {
            warnAboutNonJSObject(cx, y);
            return false;
        }
    }

    private static boolean eqString(Context cx, CharSequence x, Object y) {
        if (y == null || y == Undefined.instance) {
            return false;
        } else if (y instanceof CharSequence c) {
            return x.length() == c.length() && x.toString().equals(c.toString());
        } else if (y instanceof Number) {
            return toNumber(cx, x.toString()) == ((Number) y).doubleValue();
        } else if (y instanceof Boolean) {
            return toNumber(cx, x.toString()) == ((Boolean) y ? 1.0 : 0.0);
        } else if (isSymbol(y)) {
            return false;
        } else if (y instanceof Scriptable) {
            if (y instanceof ScriptableObject) {
                Object test = ((ScriptableObject) y).equivalentValues(x.toString());
                if (test != Scriptable.NOT_FOUND) {
                    return (Boolean) test;
                }
            }
            return eqString(cx, x, toPrimitive(cx, y));
        } else {
            warnAboutNonJSObject(cx, y);
            return false;
        }
    }

    public static boolean shallowEq(Context cx, Object x, Object y) {
        if (x == y) {
            if (!(x instanceof Number)) {
                return true;
            } else {
                double d = ((Number) x).doubleValue();
                return !Double.isNaN(d);
            }
        } else if (x != null && x != Undefined.instance && x != Undefined.SCRIPTABLE_UNDEFINED) {
            Object x1 = Wrapper.unwrapped(x);
            Object y1 = Wrapper.unwrapped(y);
            if (x1 == y1) {
                return true;
            } else if (SpecialEquality.checkSpecialEquality(x1, y1, true)) {
                return true;
            } else if (SpecialEquality.checkSpecialEquality(y1, x1, true)) {
                return true;
            } else {
                if (x1 instanceof Number) {
                    if (y1 instanceof Number) {
                        return ((Number) x1).doubleValue() == ((Number) y1).doubleValue();
                    }
                } else {
                    if (x1 instanceof CharSequence) {
                        return x1.toString().equals(String.valueOf(y1));
                    }
                    if (y1 instanceof CharSequence) {
                        return y1.toString().equals(String.valueOf(x1));
                    }
                    if (x1 instanceof Boolean) {
                        if (y1 instanceof Boolean) {
                            return x1.equals(y1);
                        }
                    } else if (!(x1 instanceof Scriptable)) {
                        warnAboutNonJSObject(cx, x1);
                    }
                }
                return false;
            }
        } else {
            return x == Undefined.instance && y == Undefined.SCRIPTABLE_UNDEFINED || x == Undefined.SCRIPTABLE_UNDEFINED && y == Undefined.instance;
        }
    }

    public static boolean instanceOf(Context cx, Object a, Object b) {
        if (!(b instanceof Scriptable)) {
            throw typeError0(cx, "msg.instanceof.not.object");
        } else {
            return !(a instanceof Scriptable) ? false : ((Scriptable) b).hasInstance(cx, (Scriptable) a);
        }
    }

    public static boolean jsDelegatesTo(Context cx, Scriptable lhs, Scriptable rhs) {
        for (Scriptable proto = lhs.getPrototype(cx); proto != null; proto = proto.getPrototype(cx)) {
            if (proto.equals(rhs)) {
                return true;
            }
        }
        return false;
    }

    public static boolean in(Context cx, Object a, Object b) {
        if (!(b instanceof Scriptable)) {
            throw typeError0(cx, "msg.in.not.object");
        } else {
            return hasObjectElem(cx, (Scriptable) b, a);
        }
    }

    public static boolean cmp_LT(Context cx, Object val1, Object val2) {
        double d1;
        double d2;
        if (val1 instanceof Number && val2 instanceof Number) {
            d1 = ((Number) val1).doubleValue();
            d2 = ((Number) val2).doubleValue();
        } else {
            if (val1 instanceof Symbol || val2 instanceof Symbol) {
                throw typeError0(cx, "msg.compare.symbol");
            }
            if (val1 instanceof Scriptable) {
                val1 = ((Scriptable) val1).getDefaultValue(cx, NumberClass);
            }
            if (val2 instanceof Scriptable) {
                val2 = ((Scriptable) val2).getDefaultValue(cx, NumberClass);
            }
            if (val1 instanceof CharSequence && val2 instanceof CharSequence) {
                return val1.toString().compareTo(val2.toString()) < 0;
            }
            d1 = toNumber(cx, val1);
            d2 = toNumber(cx, val2);
        }
        return d1 < d2;
    }

    public static boolean cmp_LE(Context cx, Object val1, Object val2) {
        double d1;
        double d2;
        if (val1 instanceof Number && val2 instanceof Number) {
            d1 = ((Number) val1).doubleValue();
            d2 = ((Number) val2).doubleValue();
        } else {
            if (val1 instanceof Symbol || val2 instanceof Symbol) {
                throw typeError0(cx, "msg.compare.symbol");
            }
            if (val1 instanceof Scriptable) {
                val1 = ((Scriptable) val1).getDefaultValue(cx, NumberClass);
            }
            if (val2 instanceof Scriptable) {
                val2 = ((Scriptable) val2).getDefaultValue(cx, NumberClass);
            }
            if (val1 instanceof CharSequence && val2 instanceof CharSequence) {
                return val1.toString().compareTo(val2.toString()) <= 0;
            }
            d1 = toNumber(cx, val1);
            d2 = toNumber(cx, val2);
        }
        return d1 <= d2;
    }

    public static void initScript(Context cx, Scriptable scope, NativeFunction funObj, Scriptable thisObj, boolean evalScript) {
        if (!cx.hasTopCallScope()) {
            throw new IllegalStateException();
        } else {
            int varCount = funObj.getParamAndVarCount();
            if (varCount != 0) {
                Scriptable varScope = scope;
                while (varScope instanceof NativeWith) {
                    varScope = varScope.getParentScope();
                }
                int i = varCount;
                while (i-- != 0) {
                    String name = funObj.getParamOrVarName(i);
                    boolean isConst = funObj.getParamOrVarConst(i);
                    if (!ScriptableObject.hasProperty(scope, name, cx)) {
                        if (isConst) {
                            ScriptableObject.defineConstProperty(varScope, name, cx);
                        } else if (!evalScript) {
                            if (!(funObj instanceof InterpretedFunction) || ((InterpretedFunction) funObj).hasFunctionNamed(name)) {
                                ScriptableObject.defineProperty(varScope, name, Undefined.instance, 4, cx);
                            }
                        } else {
                            varScope.put(cx, name, varScope, Undefined.instance);
                        }
                    } else {
                        ScriptableObject.redefineProperty(scope, name, isConst, cx);
                    }
                }
            }
        }
    }

    public static Scriptable createFunctionActivation(Context cx, Scriptable scope, NativeFunction funObj, Object[] args, boolean isStrict) {
        return new NativeCall(funObj, scope, args, false, isStrict, cx);
    }

    public static Scriptable createArrowFunctionActivation(Context cx, Scriptable scope, NativeFunction funObj, Object[] args, boolean isStrict) {
        return new NativeCall(funObj, scope, args, true, isStrict, cx);
    }

    public static void enterActivationFunction(Context cx, Scriptable scope) {
        if (!cx.hasTopCallScope()) {
            throw new IllegalStateException();
        } else {
            NativeCall call = (NativeCall) scope;
            call.parentActivationCall = cx.currentActivationCall;
            cx.currentActivationCall = call;
            call.defineAttributesForArguments(cx);
        }
    }

    public static void exitActivationFunction(Context cx) {
        NativeCall call = cx.currentActivationCall;
        cx.currentActivationCall = call.parentActivationCall;
        call.parentActivationCall = null;
    }

    static NativeCall findFunctionActivation(Context cx, Function f) {
        for (NativeCall call = cx.currentActivationCall; call != null; call = call.parentActivationCall) {
            if (call.function == f) {
                return call;
            }
        }
        return null;
    }

    public static Scriptable newCatchScope(Context cx, Scriptable scope, Throwable t, Scriptable lastCatchScope, String exceptionName) {
        Object obj;
        boolean cacheObj;
        if (t instanceof JavaScriptException) {
            cacheObj = false;
            obj = ((JavaScriptException) t).getValue();
        } else {
            cacheObj = true;
            if (lastCatchScope != null) {
                NativeObject last = (NativeObject) lastCatchScope;
                obj = last.getAssociatedValue(t);
                if (obj == null) {
                    Kit.codeBug();
                }
            } else {
                Throwable javaException = null;
                TopLevel.NativeErrors type;
                String errorMsg;
                RhinoException re;
                if (t instanceof EcmaError ee) {
                    re = ee;
                    type = TopLevel.NativeErrors.valueOf(ee.getName());
                    errorMsg = ee.getErrorMessage();
                } else if (t instanceof WrappedException we) {
                    re = we;
                    javaException = we.getWrappedException();
                    type = TopLevel.NativeErrors.JavaException;
                    errorMsg = javaException.getClass().getName() + ": " + javaException.getMessage();
                } else {
                    if (!(t instanceof EvaluatorException ee)) {
                        throw Kit.codeBug();
                    }
                    re = ee;
                    type = TopLevel.NativeErrors.InternalError;
                    errorMsg = ee.getMessage();
                }
                String sourceUri = re.sourceName();
                if (sourceUri == null) {
                    sourceUri = "";
                }
                int line = re.lineNumber();
                Object[] args;
                if (line > 0) {
                    args = new Object[] { errorMsg, sourceUri, line };
                } else {
                    args = new Object[] { errorMsg, sourceUri };
                }
                Scriptable errorObject = newNativeError(cx, scope, type, args);
                if (errorObject instanceof NativeError) {
                    ((NativeError) errorObject).setStackProvider(re, cx);
                }
                if (javaException != null && isVisible(cx, javaException, 3)) {
                    Object wrap = cx.getWrapFactory().wrap(cx, scope, javaException, null);
                    ScriptableObject.defineProperty(errorObject, "javaException", wrap, 7, cx);
                }
                if (isVisible(cx, re, 3)) {
                    Object wrap = cx.getWrapFactory().wrap(cx, scope, re, null);
                    ScriptableObject.defineProperty(errorObject, "rhinoException", wrap, 7, cx);
                }
                obj = errorObject;
            }
        }
        NativeObject catchScopeObject = new NativeObject(cx);
        catchScopeObject.defineProperty(cx, exceptionName, obj, 4);
        if (isVisible(cx, t, 3)) {
            catchScopeObject.defineProperty(cx, "__exception__", Context.javaToJS(cx, t, scope), 6);
        }
        if (cacheObj) {
            catchScopeObject.associateValue(t, obj);
        }
        return catchScopeObject;
    }

    public static Scriptable wrapException(Context cx, Scriptable scope, Throwable t) {
        Throwable javaException = null;
        RhinoException re;
        String errorName;
        String errorMsg;
        if (t instanceof EcmaError ee) {
            re = ee;
            errorName = ee.getName();
            errorMsg = ee.getErrorMessage();
        } else if (t instanceof WrappedException we) {
            re = we;
            javaException = we.getWrappedException();
            errorName = "JavaException";
            errorMsg = javaException.getClass().getName() + ": " + javaException.getMessage();
        } else {
            if (!(t instanceof EvaluatorException ee)) {
                throw Kit.codeBug();
            }
            re = ee;
            errorName = "InternalError";
            errorMsg = ee.getMessage();
        }
        String sourceUri = re.sourceName();
        if (sourceUri == null) {
            sourceUri = "";
        }
        int line = re.lineNumber();
        Object[] args;
        if (line > 0) {
            args = new Object[] { errorMsg, sourceUri, line };
        } else {
            args = new Object[] { errorMsg, sourceUri };
        }
        Scriptable errorObject = cx.newObject(scope, errorName, args);
        ScriptableObject.putProperty(errorObject, "name", errorName, cx);
        if (errorObject instanceof NativeError) {
            ((NativeError) errorObject).setStackProvider(re, cx);
        }
        if (javaException != null && isVisible(cx, javaException, 3)) {
            Object wrap = cx.getWrapFactory().wrap(cx, scope, javaException, null);
            ScriptableObject.defineProperty(errorObject, "javaException", wrap, 7, cx);
        }
        if (isVisible(cx, re, 3)) {
            Object wrap = cx.getWrapFactory().wrap(cx, scope, re, null);
            ScriptableObject.defineProperty(errorObject, "rhinoException", wrap, 7, cx);
        }
        return errorObject;
    }

    private static boolean isVisible(Context cx, Object obj, int type) {
        ClassShutter shutter = cx.getClassShutter();
        return shutter == null || shutter.visibleToScripts(obj.getClass().getName(), type);
    }

    public static Scriptable enterWith(Context cx, Scriptable scope, Object obj) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw typeError1(cx, "msg.undef.with", toString(cx, obj));
        } else {
            return new NativeWith(scope, sobj);
        }
    }

    public static Scriptable leaveWith(Scriptable scope) {
        NativeWith nw = (NativeWith) scope;
        return nw.getParentScope();
    }

    public static Scriptable enterDotQuery(Object value, Scriptable scope, Context cx) {
        throw notXmlError(cx, value);
    }

    public static Object updateDotQuery(boolean value, Scriptable scope) {
        NativeWith nw = (NativeWith) scope;
        return nw.updateDotQuery(value);
    }

    public static Scriptable leaveDotQuery(Scriptable scope) {
        NativeWith nw = (NativeWith) scope;
        return nw.getParentScope();
    }

    public static void setFunctionProtoAndParent(Context cx, Scriptable scope, BaseFunction fn) {
        setFunctionProtoAndParent(cx, scope, fn, false);
    }

    public static void setFunctionProtoAndParent(Context cx, Scriptable scope, BaseFunction fn, boolean es6GeneratorFunction) {
        fn.setParentScope(scope);
        if (es6GeneratorFunction) {
            fn.setPrototype(ScriptableObject.getGeneratorFunctionPrototype(scope, cx));
        } else {
            fn.setPrototype(ScriptableObject.getFunctionPrototype(scope, cx));
        }
    }

    public static void setObjectProtoAndParent(Context cx, Scriptable scope, ScriptableObject object) {
        scope = ScriptableObject.getTopLevelScope(scope);
        object.setParentScope(scope);
        Scriptable proto = ScriptableObject.getClassPrototype(scope, object.getClassName(), cx);
        object.setPrototype(proto);
    }

    public static void setBuiltinProtoAndParent(Context cx, Scriptable scope, ScriptableObject object, TopLevel.Builtins type) {
        scope = ScriptableObject.getTopLevelScope(scope);
        object.setParentScope(scope);
        object.setPrototype(TopLevel.getBuiltinPrototype(scope, type, cx));
    }

    public static void initFunction(Context cx, Scriptable scope, NativeFunction function, int type, boolean fromEvalCode) {
        if (type == 1) {
            String name = function.getFunctionName();
            if (name != null && name.length() != 0) {
                if (!fromEvalCode) {
                    ScriptableObject.defineProperty(scope, name, function, 4, cx);
                } else {
                    scope.put(cx, name, scope, function);
                }
            }
        } else {
            if (type != 3) {
                throw Kit.codeBug();
            }
            String name = function.getFunctionName();
            if (name != null && name.length() != 0) {
                while (scope instanceof NativeWith) {
                    scope = scope.getParentScope();
                }
                scope.put(cx, name, scope, function);
            }
        }
    }

    public static Scriptable newArrayLiteral(Context cx, Scriptable scope, Object[] objects, int[] skipIndices) {
        int SKIP_DENSITY = 2;
        int count = objects.length;
        int skipCount = 0;
        if (skipIndices != null) {
            skipCount = skipIndices.length;
        }
        int length = count + skipCount;
        if (length > 1 && skipCount * 2 < length) {
            Object[] sparse;
            if (skipCount == 0) {
                sparse = objects;
            } else {
                sparse = new Object[length];
                int skip = 0;
                int i = 0;
                for (int j = 0; i != length; i++) {
                    if (skip != skipCount && skipIndices[skip] == i) {
                        sparse[i] = Scriptable.NOT_FOUND;
                        skip++;
                    } else {
                        sparse[i] = objects[j];
                        j++;
                    }
                }
            }
            return cx.newArray(scope, sparse);
        } else {
            Scriptable array = cx.newArray(scope, length);
            int skip = 0;
            int i = 0;
            for (int jx = 0; i != length; i++) {
                if (skip != skipCount && skipIndices[skip] == i) {
                    skip++;
                } else {
                    array.put(cx, i, array, objects[jx]);
                    jx++;
                }
            }
            return array;
        }
    }

    public static Scriptable newObjectLiteral(Context cx, Scriptable scope, Object[] propertyIds, Object[] propertyValues, int[] getterSetters) {
        Scriptable object = cx.newObject(scope);
        int i = 0;
        for (int end = propertyIds.length; i != end; i++) {
            Object id = propertyIds[i];
            int getterSetter = getterSetters == null ? 0 : getterSetters[i];
            Object value = propertyValues[i];
            if (id instanceof String) {
                if (getterSetter == 0) {
                    if (isSpecialProperty((String) id)) {
                        Ref ref = specialRef(cx, scope, object, (String) id);
                        ref.set(cx, scope, value);
                    } else {
                        object.put(cx, (String) id, object, value);
                    }
                } else {
                    ScriptableObject so = (ScriptableObject) object;
                    Callable getterOrSetter = (Callable) value;
                    boolean isSetter = getterSetter == 1;
                    so.setGetterOrSetter(cx, (String) id, 0, getterOrSetter, isSetter);
                }
            } else {
                int index = (Integer) id;
                object.put(cx, index, object, value);
            }
        }
        return object;
    }

    public static boolean isArrayObject(Object obj) {
        return obj instanceof NativeArray || obj instanceof Arguments;
    }

    public static Object[] getArrayElements(Context cx, Scriptable object) {
        long longLen = NativeArray.getLengthProperty(cx, object, false);
        if (longLen > 2147483647L) {
            throw new IllegalArgumentException();
        } else {
            int len = (int) longLen;
            if (len == 0) {
                return EMPTY_OBJECTS;
            } else {
                Object[] result = new Object[len];
                for (int i = 0; i < len; i++) {
                    Object elem = ScriptableObject.getProperty(object, i, cx);
                    result[i] = elem == Scriptable.NOT_FOUND ? Undefined.instance : elem;
                }
                return result;
            }
        }
    }

    static void checkDeprecated(Context cx, String name) {
        throw Context.reportRuntimeError(getMessage1("msg.deprec.ctor", name), cx);
    }

    public static String getMessage0(String messageId) {
        return getMessage(messageId, null);
    }

    public static String getMessage1(String messageId, Object arg1) {
        Object[] arguments = new Object[] { arg1 };
        return getMessage(messageId, arguments);
    }

    public static String getMessage2(String messageId, Object arg1, Object arg2) {
        Object[] arguments = new Object[] { arg1, arg2 };
        return getMessage(messageId, arguments);
    }

    public static String getMessage3(String messageId, Object arg1, Object arg2, Object arg3) {
        Object[] arguments = new Object[] { arg1, arg2, arg3 };
        return getMessage(messageId, arguments);
    }

    public static String getMessage4(String messageId, Object arg1, Object arg2, Object arg3, Object arg4) {
        Object[] arguments = new Object[] { arg1, arg2, arg3, arg4 };
        return getMessage(messageId, arguments);
    }

    public static String getMessage(String messageId, Object[] arguments) {
        return messageProvider.getMessage(messageId, arguments);
    }

    public static EcmaError constructError(Context cx, String error, String message) {
        int[] linep = new int[1];
        String filename = Context.getSourcePositionFromStack(cx, linep);
        return constructError(cx, error, message, filename, linep[0], null, 0);
    }

    public static EcmaError constructError(Context cx, String error, String message, int lineNumberDelta) {
        int[] linep = new int[1];
        String filename = Context.getSourcePositionFromStack(cx, linep);
        if (linep[0] != 0) {
            linep[0] += lineNumberDelta;
        }
        return constructError(cx, error, message, filename, linep[0], null, 0);
    }

    public static EcmaError constructError(Context cx, String error, String message, String sourceName, int lineNumber, String lineSource, int columnNumber) {
        return new EcmaError(cx, error, message, sourceName, lineNumber, lineSource, columnNumber);
    }

    public static EcmaError rangeError(Context cx, String message) {
        return constructError(cx, "RangeError", message);
    }

    public static EcmaError typeError(Context cx, String message) {
        return constructError(cx, "TypeError", message);
    }

    public static EcmaError typeError0(Context cx, String messageId) {
        String msg = getMessage0(messageId);
        return typeError(cx, msg);
    }

    public static EcmaError typeError1(Context cx, String messageId, Object arg1) {
        String msg = getMessage1(messageId, arg1);
        return typeError(cx, msg);
    }

    public static EcmaError typeError2(Context cx, String messageId, Object arg1, Object arg2) {
        String msg = getMessage2(messageId, arg1, arg2);
        return typeError(cx, msg);
    }

    public static EcmaError typeError3(Context cx, String messageId, String arg1, String arg2, String arg3) {
        String msg = getMessage3(messageId, arg1, arg2, arg3);
        return typeError(cx, msg);
    }

    public static RuntimeException undefReadError(Context cx, Object object, Object id) {
        return typeError2(cx, "msg.undef.prop.read", toString(cx, object), toString(cx, id));
    }

    public static RuntimeException undefCallError(Context cx, Object object, Object id) {
        return typeError2(cx, "msg.undef.method.call", toString(cx, object), toString(cx, id));
    }

    public static RuntimeException undefWriteError(Context cx, Object object, Object id, Object value) {
        return typeError3(cx, "msg.undef.prop.write", toString(cx, object), toString(cx, id), toString(cx, value));
    }

    private static RuntimeException undefDeleteError(Context cx, Object object, Object id) {
        throw typeError2(cx, "msg.undef.prop.delete", toString(cx, object), toString(cx, id));
    }

    public static RuntimeException notFoundError(Context cx, Scriptable object, String property) {
        String msg = getMessage1("msg.is.not.defined", property);
        throw constructError(cx, "ReferenceError", msg);
    }

    public static RuntimeException notFunctionError(Context cx, Object value) {
        return notFunctionError(cx, value, value);
    }

    public static RuntimeException notFunctionError(Context cx, Object value, Object messageHelper) {
        String msg = messageHelper == null ? "null" : messageHelper.toString();
        return value == Scriptable.NOT_FOUND ? typeError1(cx, "msg.function.not.found", msg) : typeError2(cx, "msg.isnt.function", msg, typeof(cx, value));
    }

    public static RuntimeException notFunctionError(Context cx, Object obj, Object value, String propertyName) {
        String objString = toString(cx, obj);
        if (obj instanceof NativeFunction) {
            int paren = objString.indexOf(41);
            int curly = objString.indexOf(123, paren);
            if (curly > -1) {
                objString = objString.substring(0, curly + 1) + "...}";
            }
        }
        return value == Scriptable.NOT_FOUND ? typeError2(cx, "msg.function.not.found.in", propertyName, objString) : typeError3(cx, "msg.isnt.function.in", propertyName, objString, typeof(cx, value).toString());
    }

    private static RuntimeException notXmlError(Context cx, Object value) {
        throw typeError1(cx, "msg.isnt.xml.object", toString(cx, value));
    }

    private static void warnAboutNonJSObject(Context cx, Object nonJSObject) {
        String omitParam = getMessage0("params.omit.non.js.object.warning");
        if (!"true".equals(omitParam)) {
            String message = getMessage2("msg.non.js.object.warning", nonJSObject, nonJSObject.getClass().getName());
            Context.reportWarning(message, cx);
            System.err.println(message);
        }
    }

    public static void setRegExpProxy(Context cx, RegExp proxy) {
        if (proxy == null) {
            throw new IllegalArgumentException();
        } else {
            cx.regExp = proxy;
        }
    }

    public static Scriptable wrapRegExp(Context cx, Scriptable scope, Object compiled) {
        return cx.getRegExp().wrapRegExp(cx, scope, compiled);
    }

    public static Scriptable getTemplateLiteralCallSite(Context cx, Scriptable scope, Object[] strings, int index) {
        Object callsite = strings[index];
        if (callsite instanceof Scriptable) {
            return (Scriptable) callsite;
        } else {
            assert callsite instanceof String[];
            String[] vals = (String[]) callsite;
            assert (vals.length & 1) == 0;
            int FROZEN = 5;
            ScriptableObject siteObj = (ScriptableObject) cx.newArray(scope, vals.length >>> 1);
            ScriptableObject rawObj = (ScriptableObject) cx.newArray(scope, vals.length >>> 1);
            int i = 0;
            for (int n = vals.length; i < n; i += 2) {
                int idx = i >>> 1;
                siteObj.put(cx, idx, siteObj, vals[i]);
                siteObj.setAttributes(cx, idx, 5);
                rawObj.put(cx, idx, rawObj, vals[i + 1]);
                rawObj.setAttributes(cx, idx, 5);
            }
            rawObj.setAttributes(cx, "length", 5);
            rawObj.preventExtensions();
            siteObj.put(cx, "raw", siteObj, rawObj);
            siteObj.setAttributes(cx, "raw", 7);
            siteObj.setAttributes(cx, "length", 5);
            siteObj.preventExtensions();
            strings[index] = siteObj;
            return siteObj;
        }
    }

    public static void storeUint32Result(Context cx, long value) {
        if (value >>> 32 != 0L) {
            throw new IllegalArgumentException();
        } else {
            cx.scratchUint32 = value;
        }
    }

    public static long lastUint32Result(Context cx) {
        long value = cx.scratchUint32;
        if (value >>> 32 != 0L) {
            throw new IllegalStateException();
        } else {
            return value;
        }
    }

    static String makeUrlForGeneratedScript(boolean isEval, String masterScriptUrl, int masterScriptLine) {
        return isEval ? masterScriptUrl + "#" + masterScriptLine + "(eval)" : masterScriptUrl + "#" + masterScriptLine + "(Function)";
    }

    static boolean isSymbol(Object obj) {
        return obj instanceof NativeSymbol && ((NativeSymbol) obj).isSymbol() || obj instanceof SymbolKey;
    }

    public static RuntimeException errorWithClassName(String msg, Object val, Context cx) {
        return Context.reportRuntimeError1(msg, val.getClass().getName(), cx);
    }

    public static JavaScriptException throwError(Context cx, Scriptable scope, String message) {
        int[] linep = new int[] { 0 };
        String filename = Context.getSourcePositionFromStack(cx, linep);
        Scriptable error = newBuiltinObject(cx, scope, TopLevel.Builtins.Error, new Object[] { message, filename, linep[0] });
        return new JavaScriptException(cx, error, filename, linep[0]);
    }

    public static JavaScriptException throwCustomError(Context cx, Scriptable scope, String constructorName, String message) {
        int[] linep = new int[] { 0 };
        String filename = Context.getSourcePositionFromStack(cx, linep);
        Scriptable error = cx.newObject(scope, constructorName, new Object[] { message, filename, linep[0] });
        return new JavaScriptException(cx, error, filename, linep[0]);
    }

    protected ScriptRuntime() {
    }

    private static class DefaultMessageProvider implements ScriptRuntime.MessageProvider {

        @Override
        public String getMessage(String messageId, Object[] arguments) {
            String defaultResource = "dev.latvian.mods.rhino.resources.Messages";
            Locale locale = Locale.getDefault();
            ResourceBundle rb = ResourceBundle.getBundle("dev.latvian.mods.rhino.resources.Messages", locale);
            String formatString;
            try {
                formatString = rb.getString(messageId);
            } catch (MissingResourceException var8) {
                throw new RuntimeException("no message resource found for message property " + messageId);
            }
            MessageFormat formatter = new MessageFormat(formatString);
            return formatter.format(arguments);
        }
    }

    public interface MessageProvider {

        String getMessage(String var1, Object[] var2);
    }

    static class NoSuchMethodShim implements Callable {

        String methodName;

        Callable noSuchMethodMethod;

        NoSuchMethodShim(Callable noSuchMethodMethod, String methodName) {
            this.noSuchMethodMethod = noSuchMethodMethod;
            this.methodName = methodName;
        }

        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            Object[] nestedArgs = new Object[] { this.methodName, ScriptRuntime.newArrayLiteral(cx, scope, args, null) };
            return this.noSuchMethodMethod.call(cx, scope, thisObj, nestedArgs);
        }
    }

    static final class StringIdOrIndex {

        final String stringId;

        final int index;

        StringIdOrIndex(String stringId) {
            this.stringId = stringId;
            this.index = -1;
        }

        StringIdOrIndex(int index) {
            this.stringId = null;
            this.index = index;
        }
    }
}