package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.regexp.NativeRegExp;
import java.text.Collator;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;

final class NativeString extends IdScriptableObject implements Wrapper {

    private static final Object STRING_TAG = "String";

    private static final int Id_length = 1;

    private static final int Id_namespace = 2;

    private static final int Id_path = 3;

    private static final int MAX_INSTANCE_ID = 3;

    private static final int ConstructorId_fromCharCode = -1;

    private static final int ConstructorId_fromCodePoint = -2;

    private static final int ConstructorId_raw = -3;

    private static final int Id_constructor = 1;

    private static final int Id_toString = 2;

    private static final int Id_toSource = 3;

    private static final int Id_valueOf = 4;

    private static final int Id_charAt = 5;

    private static final int Id_charCodeAt = 6;

    private static final int Id_indexOf = 7;

    private static final int Id_lastIndexOf = 8;

    private static final int Id_split = 9;

    private static final int Id_substring = 10;

    private static final int Id_toLowerCase = 11;

    private static final int Id_toUpperCase = 12;

    private static final int Id_substr = 13;

    private static final int Id_concat = 14;

    private static final int Id_slice = 15;

    private static final int Id_bold = 16;

    private static final int Id_italics = 17;

    private static final int Id_fixed = 18;

    private static final int Id_strike = 19;

    private static final int Id_small = 20;

    private static final int Id_big = 21;

    private static final int Id_blink = 22;

    private static final int Id_sup = 23;

    private static final int Id_sub = 24;

    private static final int Id_fontsize = 25;

    private static final int Id_fontcolor = 26;

    private static final int Id_link = 27;

    private static final int Id_anchor = 28;

    private static final int Id_equals = 29;

    private static final int Id_equalsIgnoreCase = 30;

    private static final int Id_match = 31;

    private static final int Id_search = 32;

    private static final int Id_replace = 33;

    private static final int Id_localeCompare = 34;

    private static final int Id_toLocaleLowerCase = 35;

    private static final int Id_toLocaleUpperCase = 36;

    private static final int Id_trim = 37;

    private static final int Id_trimLeft = 38;

    private static final int Id_trimRight = 39;

    private static final int Id_includes = 40;

    private static final int Id_startsWith = 41;

    private static final int Id_endsWith = 42;

    private static final int Id_normalize = 43;

    private static final int Id_repeat = 44;

    private static final int Id_codePointAt = 45;

    private static final int Id_padStart = 46;

    private static final int Id_padEnd = 47;

    private static final int Id_trimStart = 48;

    private static final int Id_trimEnd = 49;

    private static final int SymbolId_iterator = 50;

    private static final int MAX_PROTOTYPE_ID = 50;

    private static final int ConstructorId_charAt = -5;

    private static final int ConstructorId_charCodeAt = -6;

    private static final int ConstructorId_indexOf = -7;

    private static final int ConstructorId_lastIndexOf = -8;

    private static final int ConstructorId_split = -9;

    private static final int ConstructorId_substring = -10;

    private static final int ConstructorId_toLowerCase = -11;

    private static final int ConstructorId_toUpperCase = -12;

    private static final int ConstructorId_substr = -13;

    private static final int ConstructorId_concat = -14;

    private static final int ConstructorId_slice = -15;

    private static final int ConstructorId_equalsIgnoreCase = -30;

    private static final int ConstructorId_match = -31;

    private static final int ConstructorId_search = -32;

    private static final int ConstructorId_replace = -33;

    private static final int ConstructorId_localeCompare = -34;

    private static final int ConstructorId_toLocaleLowerCase = -35;

    private final CharSequence string;

    static void init(Scriptable scope, boolean sealed, Context cx) {
        NativeString obj = new NativeString("");
        obj.exportAsJSClass(50, scope, sealed, cx);
    }

    private static NativeString realThis(Scriptable thisObj, IdFunctionObject f, Context cx) {
        if (!(thisObj instanceof NativeString)) {
            throw incompatibleCallError(f, cx);
        } else {
            return (NativeString) thisObj;
        }
    }

    private static String tagify(Scriptable thisObj, String tag, String attribute, Object[] args, Context cx) {
        String str = ScriptRuntime.toString(cx, thisObj);
        StringBuilder result = new StringBuilder();
        result.append('<').append(tag);
        if (attribute != null) {
            result.append(' ').append(attribute).append("=\"").append(ScriptRuntime.toString(cx, args, 0)).append('"');
        }
        result.append('>').append(str).append("</").append(tag).append('>');
        return result.toString();
    }

    private static int js_indexOf(int methodId, String target, Object[] args, Context cx) {
        String searchStr = ScriptRuntime.toString(cx, args, 0);
        double position = ScriptRuntime.toInteger(cx, args, 1);
        if (methodId != 41 && methodId != 42 && searchStr.length() == 0) {
            return position > (double) target.length() ? target.length() : (int) position;
        } else if (methodId != 41 && methodId != 42 && position > (double) target.length()) {
            return -1;
        } else {
            if (position < 0.0) {
                position = 0.0;
            } else if (position > (double) target.length()) {
                position = (double) target.length();
            } else if (methodId == 42 && (Double.isNaN(position) || position > (double) target.length())) {
                position = (double) target.length();
            }
            if (42 != methodId) {
                return methodId == 41 ? (target.startsWith(searchStr, (int) position) ? 0 : -1) : target.indexOf(searchStr, (int) position);
            } else {
                if (args.length == 0 || args.length == 1 || args.length == 2 && args[1] == Undefined.instance) {
                    position = (double) target.length();
                }
                return target.substring(0, (int) position).endsWith(searchStr) ? 0 : -1;
            }
        }
    }

    private static int js_lastIndexOf(String target, Object[] args, Context cx) {
        String search = ScriptRuntime.toString(cx, args, 0);
        double end = ScriptRuntime.toNumber(cx, args, 1);
        if (Double.isNaN(end) || end > (double) target.length()) {
            end = (double) target.length();
        } else if (end < 0.0) {
            end = 0.0;
        }
        return target.lastIndexOf(search, (int) end);
    }

    private static CharSequence js_substring(Context cx, CharSequence target, Object[] args) {
        int length = target.length();
        double start = ScriptRuntime.toInteger(cx, args, 0);
        if (start < 0.0) {
            start = 0.0;
        } else if (start > (double) length) {
            start = (double) length;
        }
        double end;
        if (args.length > 1 && args[1] != Undefined.instance) {
            end = ScriptRuntime.toInteger(cx, args[1]);
            if (end < 0.0) {
                end = 0.0;
            } else if (end > (double) length) {
                end = (double) length;
            }
            if (end < start) {
                double temp = start;
                start = end;
                end = temp;
            }
        } else {
            end = (double) length;
        }
        return target.subSequence((int) start, (int) end);
    }

    private static CharSequence js_substr(Context cx, CharSequence target, Object[] args) {
        if (args.length < 1) {
            return target;
        } else {
            double begin = ScriptRuntime.toInteger(cx, args[0]);
            int length = target.length();
            if (begin < 0.0) {
                begin += (double) length;
                if (begin < 0.0) {
                    begin = 0.0;
                }
            } else if (begin > (double) length) {
                begin = (double) length;
            }
            double end = (double) length;
            if (args.length > 1) {
                Object lengthArg = args[1];
                if (!Undefined.isUndefined(lengthArg)) {
                    end = ScriptRuntime.toInteger(cx, lengthArg);
                    if (end < 0.0) {
                        end = 0.0;
                    }
                    end += begin;
                    if (end > (double) length) {
                        end = (double) length;
                    }
                }
            }
            return target.subSequence((int) begin, (int) end);
        }
    }

    private static String js_concat(Context cx, String target, Object[] args) {
        int N = args.length;
        if (N == 0) {
            return target;
        } else if (N == 1) {
            String arg = ScriptRuntime.toString(cx, args[0]);
            return target.concat(arg);
        } else {
            int size = target.length();
            String[] argsAsStrings = new String[N];
            for (int i = 0; i != N; i++) {
                String s = ScriptRuntime.toString(cx, args[i]);
                argsAsStrings[i] = s;
                size += s.length();
            }
            StringBuilder result = new StringBuilder(size);
            result.append(target);
            for (int i = 0; i != N; i++) {
                result.append(argsAsStrings[i]);
            }
            return result.toString();
        }
    }

    private static CharSequence js_slice(Context cx, CharSequence target, Object[] args) {
        double begin = args.length < 1 ? 0.0 : ScriptRuntime.toInteger(cx, args[0]);
        int length = target.length();
        if (begin < 0.0) {
            begin += (double) length;
            if (begin < 0.0) {
                begin = 0.0;
            }
        } else if (begin > (double) length) {
            begin = (double) length;
        }
        double end;
        if (args.length >= 2 && args[1] != Undefined.instance) {
            end = ScriptRuntime.toInteger(cx, args[1]);
            if (end < 0.0) {
                end += (double) length;
                if (end < 0.0) {
                    end = 0.0;
                }
            } else if (end > (double) length) {
                end = (double) length;
            }
            if (end < begin) {
                end = begin;
            }
        } else {
            end = (double) length;
        }
        return target.subSequence((int) begin, (int) end);
    }

    private static String js_repeat(Context cx, Scriptable thisObj, IdFunctionObject f, Object[] args) {
        String str = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
        double cnt = ScriptRuntime.toInteger(cx, args, 0);
        if (cnt < 0.0 || cnt == Double.POSITIVE_INFINITY) {
            throw ScriptRuntime.rangeError(cx, "Invalid count value");
        } else if (cnt != 0.0 && str.length() != 0) {
            long size = (long) str.length() * (long) cnt;
            if (!(cnt > 2.147483647E9) && size <= 2147483647L) {
                StringBuilder retval = new StringBuilder((int) size);
                retval.append(str);
                int i = 1;
                int icnt;
                for (icnt = (int) cnt; i <= icnt / 2; i *= 2) {
                    retval.append(retval);
                }
                if (i < icnt) {
                    retval.append(retval.substring(0, str.length() * (icnt - i)));
                }
                return retval.toString();
            } else {
                throw ScriptRuntime.rangeError(cx, "Invalid size or count value");
            }
        } else {
            return "";
        }
    }

    private static String js_pad(Context cx, Scriptable thisObj, IdFunctionObject f, Object[] args, boolean atStart) {
        String pad = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
        long intMaxLength = ScriptRuntime.toLength(cx, args, 0);
        if (intMaxLength <= (long) pad.length()) {
            return pad;
        } else {
            String filler = " ";
            if (args.length >= 2 && !Undefined.isUndefined(args[1])) {
                filler = ScriptRuntime.toString(cx, args[1]);
                if (filler.length() < 1) {
                    return pad;
                }
            }
            int fillLen = (int) (intMaxLength - (long) pad.length());
            StringBuilder concat = new StringBuilder();
            do {
                concat.append(filler);
            } while (concat.length() < fillLen);
            concat.setLength(fillLen);
            return atStart ? concat.append(pad).toString() : concat.insert(0, pad).toString();
        }
    }

    private static CharSequence js_raw(Context cx, Scriptable scope, Object[] args) {
        Object undefined = Undefined.instance;
        Object arg0 = args.length > 0 ? args[0] : undefined;
        Scriptable cooked = ScriptRuntime.toObject(cx, scope, arg0);
        Object rawValue = cooked.get(cx, "raw", cooked);
        if (rawValue == NOT_FOUND) {
            rawValue = undefined;
        }
        Scriptable raw = ScriptRuntime.toObject(cx, scope, rawValue);
        Object len = raw.get(cx, "length", raw);
        if (len == NOT_FOUND) {
            len = undefined;
        }
        long literalSegments = ScriptRuntime.toUint32(cx, len);
        if (literalSegments == 0L) {
            return "";
        } else {
            StringBuilder elements = new StringBuilder();
            long nextIndex = 0L;
            while (true) {
                Object next;
                if (nextIndex > 2147483647L) {
                    next = raw.get(cx, Long.toString(nextIndex), raw);
                } else {
                    next = raw.get(cx, (int) nextIndex, raw);
                }
                if (next == NOT_FOUND) {
                    next = undefined;
                }
                String nextSeg = ScriptRuntime.toString(cx, next);
                elements.append(nextSeg);
                if (++nextIndex == literalSegments) {
                    return elements.toString();
                }
                next = (long) args.length > nextIndex ? args[(int) nextIndex] : undefined;
                String nextSub = ScriptRuntime.toString(cx, next);
                elements.append(nextSub);
            }
        }
    }

    NativeString(CharSequence s) {
        this.string = s;
    }

    @Override
    public String getClassName() {
        return "String";
    }

    @Override
    public Object unwrap() {
        return this.string;
    }

    @Override
    public MemberType getTypeOf() {
        return MemberType.STRING;
    }

    @Override
    protected int getMaxInstanceId() {
        return 3;
    }

    @Override
    protected int findInstanceIdInfo(String s, Context cx) {
        return switch(s) {
            case "length" ->
                instanceIdInfo(7, 1);
            case "namespace" ->
                instanceIdInfo(7, 2);
            case "path" ->
                instanceIdInfo(7, 3);
            default ->
                super.findInstanceIdInfo(s, cx);
        };
    }

    @Override
    protected String getInstanceIdName(int id) {
        return switch(id) {
            case 1 ->
                "length";
            case 2 ->
                "namespace";
            case 3 ->
                "path";
            default ->
                super.getInstanceIdName(id);
        };
    }

    @Override
    protected Object getInstanceIdValue(int id, Context cx) {
        switch(id) {
            case 1:
                return this.string.length();
            case 2:
                {
                    String str = ScriptRuntime.toString(cx, this.string);
                    int colon = str.indexOf(58);
                    return colon == -1 ? "minecraft" : str.substring(0, colon);
                }
            case 3:
                {
                    String str = ScriptRuntime.toString(cx, this.string);
                    int colon = str.indexOf(58);
                    return colon == -1 ? str : str.substring(colon + 1);
                }
            default:
                return super.getInstanceIdValue(id, cx);
        }
    }

    @Override
    protected void fillConstructorProperties(IdFunctionObject ctor, Context cx) {
        this.addIdFunctionProperty(ctor, STRING_TAG, -1, "fromCharCode", 1, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -2, "fromCodePoint", 1, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -3, "raw", 1, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -5, "charAt", 2, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -6, "charCodeAt", 2, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -7, "indexOf", 2, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -8, "lastIndexOf", 2, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -9, "split", 3, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -10, "substring", 3, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -11, "toLowerCase", 1, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -12, "toUpperCase", 1, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -13, "substr", 3, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -14, "concat", 2, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -15, "slice", 3, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -30, "equalsIgnoreCase", 2, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -31, "match", 2, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -32, "search", 2, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -33, "replace", 2, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -34, "localeCompare", 2, cx);
        this.addIdFunctionProperty(ctor, STRING_TAG, -35, "toLocaleLowerCase", 1, cx);
        super.fillConstructorProperties(ctor, cx);
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        if (id == 50) {
            this.initPrototypeMethod(STRING_TAG, id, SymbolKey.ITERATOR, "[Symbol.iterator]", 0, cx);
        } else {
            String fnName = null;
            String s;
            int arity;
            switch(id) {
                case 1:
                    arity = 1;
                    s = "constructor";
                    break;
                case 2:
                    arity = 0;
                    s = "toString";
                    break;
                case 3:
                    arity = 0;
                    s = "toSource";
                    break;
                case 4:
                    arity = 0;
                    s = "valueOf";
                    break;
                case 5:
                    arity = 1;
                    s = "charAt";
                    break;
                case 6:
                    arity = 1;
                    s = "charCodeAt";
                    break;
                case 7:
                    arity = 1;
                    s = "indexOf";
                    break;
                case 8:
                    arity = 1;
                    s = "lastIndexOf";
                    break;
                case 9:
                    arity = 2;
                    s = "split";
                    break;
                case 10:
                    arity = 2;
                    s = "substring";
                    break;
                case 11:
                    arity = 0;
                    s = "toLowerCase";
                    break;
                case 12:
                    arity = 0;
                    s = "toUpperCase";
                    break;
                case 13:
                    arity = 2;
                    s = "substr";
                    break;
                case 14:
                    arity = 1;
                    s = "concat";
                    break;
                case 15:
                    arity = 2;
                    s = "slice";
                    break;
                case 16:
                    arity = 0;
                    s = "bold";
                    break;
                case 17:
                    arity = 0;
                    s = "italics";
                    break;
                case 18:
                    arity = 0;
                    s = "fixed";
                    break;
                case 19:
                    arity = 0;
                    s = "strike";
                    break;
                case 20:
                    arity = 0;
                    s = "small";
                    break;
                case 21:
                    arity = 0;
                    s = "big";
                    break;
                case 22:
                    arity = 0;
                    s = "blink";
                    break;
                case 23:
                    arity = 0;
                    s = "sup";
                    break;
                case 24:
                    arity = 0;
                    s = "sub";
                    break;
                case 25:
                    arity = 0;
                    s = "fontsize";
                    break;
                case 26:
                    arity = 0;
                    s = "fontcolor";
                    break;
                case 27:
                    arity = 0;
                    s = "link";
                    break;
                case 28:
                    arity = 0;
                    s = "anchor";
                    break;
                case 29:
                    arity = 1;
                    s = "equals";
                    break;
                case 30:
                    arity = 1;
                    s = "equalsIgnoreCase";
                    break;
                case 31:
                    arity = 1;
                    s = "match";
                    break;
                case 32:
                    arity = 1;
                    s = "search";
                    break;
                case 33:
                    arity = 2;
                    s = "replace";
                    break;
                case 34:
                    arity = 1;
                    s = "localeCompare";
                    break;
                case 35:
                    arity = 0;
                    s = "toLocaleLowerCase";
                    break;
                case 36:
                    arity = 0;
                    s = "toLocaleUpperCase";
                    break;
                case 37:
                    arity = 0;
                    s = "trim";
                    break;
                case 38:
                    arity = 0;
                    s = "trimLeft";
                    break;
                case 39:
                    arity = 0;
                    s = "trimRight";
                    break;
                case 40:
                    arity = 1;
                    s = "includes";
                    break;
                case 41:
                    arity = 1;
                    s = "startsWith";
                    break;
                case 42:
                    arity = 1;
                    s = "endsWith";
                    break;
                case 43:
                    arity = 0;
                    s = "normalize";
                    break;
                case 44:
                    arity = 1;
                    s = "repeat";
                    break;
                case 45:
                    arity = 1;
                    s = "codePointAt";
                    break;
                case 46:
                    arity = 1;
                    s = "padStart";
                    break;
                case 47:
                    arity = 1;
                    s = "padEnd";
                    break;
                case 48:
                    arity = 0;
                    s = "trimStart";
                    break;
                case 49:
                    arity = 0;
                    s = "trimEnd";
                    break;
                default:
                    throw new IllegalArgumentException(String.valueOf(id));
            }
            this.initPrototypeMethod(STRING_TAG, id, s, fnName, arity, cx);
        }
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(STRING_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            while (true) {
                switch(id) {
                    case -35:
                    case -34:
                    case -33:
                    case -32:
                    case -31:
                    case -30:
                    case -15:
                    case -14:
                    case -13:
                    case -12:
                    case -11:
                    case -10:
                    case -9:
                    case -8:
                    case -7:
                    case -6:
                    case -5:
                        if (args.length > 0) {
                            thisObj = ScriptRuntime.toObject(cx, scope, ScriptRuntime.toCharSequence(cx, args[0]));
                            Object[] newArgs = new Object[args.length - 1];
                            System.arraycopy(args, 1, newArgs, 0, newArgs.length);
                            args = newArgs;
                        } else {
                            thisObj = ScriptRuntime.toObject(cx, scope, ScriptRuntime.toCharSequence(cx, thisObj));
                        }
                        id = -id;
                        break;
                    case -29:
                    case -28:
                    case -27:
                    case -26:
                    case -25:
                    case -24:
                    case -23:
                    case -22:
                    case -21:
                    case -20:
                    case -19:
                    case -18:
                    case -17:
                    case -16:
                    case -4:
                    case 0:
                    default:
                        throw new IllegalArgumentException("String.prototype has no method: " + f.getFunctionName());
                    case -3:
                        return js_raw(cx, scope, args);
                    case -2:
                        int n = args.length;
                        if (n < 1) {
                            return "";
                        }
                        int[] codePoints = new int[n];
                        for (int i = 0; i != n; i++) {
                            Object arg = args[i];
                            int codePoint = ScriptRuntime.toInt32(cx, arg);
                            double num = ScriptRuntime.toNumber(cx, arg);
                            if (!ScriptRuntime.eqNumber(cx, num, codePoint) || !Character.isValidCodePoint(codePoint)) {
                                throw ScriptRuntime.rangeError(cx, "Invalid code point " + ScriptRuntime.toString(cx, arg));
                            }
                            codePoints[i] = codePoint;
                        }
                        return new String(codePoints, 0, n);
                    case -1:
                        int n = args.length;
                        if (n < 1) {
                            return "";
                        }
                        char[] chars = new char[n];
                        for (int i = 0; i != n; i++) {
                            chars[i] = ScriptRuntime.toUint16(cx, args[i]);
                        }
                        return new String(chars);
                    case 1:
                        CharSequence s;
                        if (args.length == 0) {
                            s = "";
                        } else if (ScriptRuntime.isSymbol(args[0]) && thisObj != null) {
                            s = args[0].toString();
                        } else {
                            s = ScriptRuntime.toCharSequence(cx, args[0]);
                        }
                        if (thisObj == null) {
                            return new NativeString(s);
                        }
                        return s instanceof String ? s : s.toString();
                    case 2:
                    case 4:
                        CharSequence cs = realThis(thisObj, f, cx).string;
                        return cs instanceof String ? cs : cs.toString();
                    case 3:
                        return "not_supported";
                    case 5:
                    case 6:
                        {
                            CharSequence target = ScriptRuntime.toCharSequence(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            double pos = ScriptRuntime.toInteger(cx, args, 0);
                            if (!(pos < 0.0) && !(pos >= (double) target.length())) {
                                char c = target.charAt((int) pos);
                                if (id == 5) {
                                    return String.valueOf(c);
                                }
                                return c;
                            }
                            if (id == 5) {
                                return "";
                            }
                            return ScriptRuntime.NaNobj;
                        }
                    case 7:
                        {
                            String thisString = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            return js_indexOf(7, thisString, args, cx);
                        }
                    case 8:
                        {
                            String thisStr = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            return js_lastIndexOf(thisStr, args, cx);
                        }
                    case 9:
                        {
                            String thisStr = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            return cx.getRegExp().js_split(cx, scope, thisStr, args);
                        }
                    case 10:
                        {
                            CharSequence target = ScriptRuntime.toCharSequence(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            return js_substring(cx, target, args);
                        }
                    case 11:
                        {
                            String thisStr = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            return thisStr.toLowerCase(Locale.ROOT);
                        }
                    case 12:
                        {
                            String thisStr = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            return thisStr.toUpperCase(Locale.ROOT);
                        }
                    case 13:
                        {
                            CharSequence target = ScriptRuntime.toCharSequence(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            return js_substr(cx, target, args);
                        }
                    case 14:
                        {
                            String thisStr = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            return js_concat(cx, thisStr, args);
                        }
                    case 15:
                        {
                            CharSequence target = ScriptRuntime.toCharSequence(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            return js_slice(cx, target, args);
                        }
                    case 16:
                        return tagify(thisObj, "b", null, null, cx);
                    case 17:
                        return tagify(thisObj, "i", null, null, cx);
                    case 18:
                        return tagify(thisObj, "tt", null, null, cx);
                    case 19:
                        return tagify(thisObj, "strike", null, null, cx);
                    case 20:
                        return tagify(thisObj, "small", null, null, cx);
                    case 21:
                        return tagify(thisObj, "big", null, null, cx);
                    case 22:
                        return tagify(thisObj, "blink", null, null, cx);
                    case 23:
                        return tagify(thisObj, "sup", null, null, cx);
                    case 24:
                        return tagify(thisObj, "sub", null, null, cx);
                    case 25:
                        return tagify(thisObj, "font", "size", args, cx);
                    case 26:
                        return tagify(thisObj, "font", "color", args, cx);
                    case 27:
                        return tagify(thisObj, "a", "href", args, cx);
                    case 28:
                        return tagify(thisObj, "a", "name", args, cx);
                    case 29:
                    case 30:
                        String s1 = ScriptRuntime.toString(cx, thisObj);
                        String s2 = ScriptRuntime.toString(cx, args, 0);
                        return id == 29 ? s1.equals(s2) : s1.equalsIgnoreCase(s2);
                    case 31:
                    case 32:
                    case 33:
                        int actionType;
                        if (id == 31) {
                            actionType = 1;
                        } else if (id == 32) {
                            actionType = 3;
                        } else {
                            actionType = 2;
                        }
                        ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f);
                        return cx.getRegExp().action(cx, scope, thisObj, args, actionType);
                    case 34:
                        {
                            String thisStr = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            Collator collator = Collator.getInstance();
                            collator.setStrength(3);
                            collator.setDecomposition(1);
                            return ScriptRuntime.wrapNumber((double) collator.compare(thisStr, ScriptRuntime.toString(cx, args, 0)));
                        }
                    case 35:
                        {
                            String thisStr = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            return thisStr.toLowerCase();
                        }
                    case 36:
                        {
                            String thisStr = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            return thisStr.toUpperCase();
                        }
                    case 37:
                        {
                            String str = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            char[] chars = str.toCharArray();
                            int start = 0;
                            while (start < chars.length && ScriptRuntime.isJSWhitespaceOrLineTerminator(chars[start])) {
                                start++;
                            }
                            int end = chars.length;
                            while (end > start && ScriptRuntime.isJSWhitespaceOrLineTerminator(chars[end - 1])) {
                                end--;
                            }
                            return str.substring(start, end);
                        }
                    case 38:
                    case 48:
                        {
                            String str = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            char[] chars = str.toCharArray();
                            int start = 0;
                            while (start < chars.length && ScriptRuntime.isJSWhitespaceOrLineTerminator(chars[start])) {
                                start++;
                            }
                            int end = chars.length;
                            return str.substring(start, end);
                        }
                    case 39:
                    case 49:
                        {
                            String str = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            char[] chars = str.toCharArray();
                            int start = 0;
                            int end = chars.length;
                            while (end > start && ScriptRuntime.isJSWhitespaceOrLineTerminator(chars[end - 1])) {
                                end--;
                            }
                            return str.substring(start, end);
                        }
                    case 40:
                    case 41:
                    case 42:
                        {
                            String thisString = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            if (args.length > 0 && args[0] instanceof NativeRegExp) {
                                throw ScriptRuntime.typeError2(cx, "msg.first.arg.not.regexp", String.class.getSimpleName(), f.getFunctionName());
                            }
                            int idx = js_indexOf(id, thisString, args, cx);
                            if (id == 40) {
                                return idx != -1;
                            }
                            if (id == 41) {
                                return idx == 0;
                            }
                            return idx != -1;
                        }
                    case 43:
                        if (args.length != 0 && !Undefined.isUndefined(args[0])) {
                            String formStr = ScriptRuntime.toString(cx, args, 0);
                            Form form;
                            if (Form.NFD.name().equals(formStr)) {
                                form = Form.NFD;
                            } else if (Form.NFKC.name().equals(formStr)) {
                                form = Form.NFKC;
                            } else if (Form.NFKD.name().equals(formStr)) {
                                form = Form.NFKD;
                            } else {
                                if (!Form.NFC.name().equals(formStr)) {
                                    throw ScriptRuntime.rangeError(cx, "The normalization form should be one of 'NFC', 'NFD', 'NFKC', 'NFKD'.");
                                }
                                form = Form.NFC;
                            }
                            return Normalizer.normalize(ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f)), form);
                        }
                        return Normalizer.normalize(ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f)), Form.NFC);
                    case 44:
                        return js_repeat(cx, thisObj, f, args);
                    case 45:
                        {
                            String str = ScriptRuntime.toString(cx, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                            double cnt = ScriptRuntime.toInteger(cx, args, 0);
                            return !(cnt < 0.0) && !(cnt >= (double) str.length()) ? str.codePointAt((int) cnt) : Undefined.instance;
                        }
                    case 46:
                    case 47:
                        return js_pad(cx, thisObj, f, args, id == 46);
                    case 50:
                        return new NativeStringIterator(cx, scope, ScriptRuntimeES6.requireObjectCoercible(cx, thisObj, f));
                }
            }
        }
    }

    public CharSequence toCharSequence() {
        return this.string;
    }

    public String toString() {
        return this.string.toString();
    }

    @Override
    public Object get(Context cx, int index, Scriptable start) {
        return 0 <= index && index < this.string.length() ? String.valueOf(this.string.charAt(index)) : super.get(cx, index, start);
    }

    @Override
    public void put(Context cx, int index, Scriptable start, Object value) {
        if (0 > index || index >= this.string.length()) {
            super.put(cx, index, start, value);
        }
    }

    @Override
    public boolean has(Context cx, int index, Scriptable start) {
        return 0 <= index && index < this.string.length() ? true : super.has(cx, index, start);
    }

    @Override
    public int getAttributes(Context cx, int index) {
        return 0 <= index && index < this.string.length() ? 5 : super.getAttributes(cx, index);
    }

    @Override
    protected Object[] getIds(Context cx, boolean nonEnumerable, boolean getSymbols) {
        if (cx == null) {
            return super.getIds(cx, nonEnumerable, getSymbols);
        } else {
            Object[] sids = super.getIds(cx, nonEnumerable, getSymbols);
            Object[] a = new Object[sids.length + this.string.length()];
            int i;
            for (i = 0; i < this.string.length(); i++) {
                a[i] = i;
            }
            System.arraycopy(sids, 0, a, i, sids.length);
            return a;
        }
    }

    @Override
    protected ScriptableObject getOwnPropertyDescriptor(Context cx, Object id) {
        if (!(id instanceof Symbol) && cx != null) {
            ScriptRuntime.StringIdOrIndex s = ScriptRuntime.toStringIdOrIndex(cx, id);
            if (s.stringId == null && 0 <= s.index && s.index < this.string.length()) {
                String value = String.valueOf(this.string.charAt(s.index));
                return this.defaultIndexPropertyDescriptor(value, cx);
            }
        }
        return super.getOwnPropertyDescriptor(cx, id);
    }

    private ScriptableObject defaultIndexPropertyDescriptor(Object value, Context cx) {
        Scriptable scope = this.getParentScope();
        if (scope == null) {
            scope = this;
        }
        ScriptableObject desc = new NativeObject(cx);
        ScriptRuntime.setBuiltinProtoAndParent(cx, scope, desc, TopLevel.Builtins.Object);
        desc.defineProperty(cx, "value", value, 0);
        desc.defineProperty(cx, "writable", Boolean.FALSE, 0);
        desc.defineProperty(cx, "enumerable", Boolean.TRUE, 0);
        desc.defineProperty(cx, "configurable", Boolean.FALSE, 0);
        return desc;
    }

    int getLength() {
        return this.string.length();
    }

    @Override
    protected int findPrototypeId(Symbol k) {
        return SymbolKey.ITERATOR.equals(k) ? 50 : 0;
    }

    @Override
    protected int findPrototypeId(String s) {
        return switch(s) {
            case "constructor" ->
                1;
            case "toString" ->
                2;
            case "toSource" ->
                3;
            case "valueOf" ->
                4;
            case "charAt" ->
                5;
            case "charCodeAt" ->
                6;
            case "indexOf" ->
                7;
            case "lastIndexOf" ->
                8;
            case "split" ->
                9;
            case "substring" ->
                10;
            case "toLowerCase" ->
                11;
            case "toUpperCase" ->
                12;
            case "substr" ->
                13;
            case "concat" ->
                14;
            case "slice" ->
                15;
            case "bold" ->
                16;
            case "italics" ->
                17;
            case "fixed" ->
                18;
            case "strike" ->
                19;
            case "small" ->
                20;
            case "big" ->
                21;
            case "blink" ->
                22;
            case "sup" ->
                23;
            case "sub" ->
                24;
            case "fontsize" ->
                25;
            case "fontcolor" ->
                26;
            case "link" ->
                27;
            case "anchor" ->
                28;
            case "equals" ->
                29;
            case "equalsIgnoreCase" ->
                30;
            case "match" ->
                31;
            case "search" ->
                32;
            case "replace" ->
                33;
            case "localeCompare" ->
                34;
            case "toLocaleLowerCase" ->
                35;
            case "toLocaleUpperCase" ->
                36;
            case "trim" ->
                37;
            case "trimLeft" ->
                38;
            case "trimRight" ->
                39;
            case "includes" ->
                40;
            case "startsWith" ->
                41;
            case "endsWith" ->
                42;
            case "normalize" ->
                43;
            case "repeat" ->
                44;
            case "codePointAt" ->
                45;
            case "padStart" ->
                46;
            case "padEnd" ->
                47;
            case "trimStart" ->
                48;
            case "trimEnd" ->
                49;
            default ->
                0;
        };
    }
}