package dev.latvian.mods.rhino;

public class NativeGlobal implements IdFunctionCall {

    static final long serialVersionUID = 6080442165748707530L;

    private static final String URI_DECODE_RESERVED = ";/?:@&=+$,#";

    private static final int INVALID_UTF8 = Integer.MAX_VALUE;

    private static final Object FTAG = "Global";

    private static final int Id_decodeURI = 1;

    private static final int Id_decodeURIComponent = 2;

    private static final int Id_encodeURI = 3;

    private static final int Id_encodeURIComponent = 4;

    private static final int Id_escape = 5;

    private static final int Id_eval = 6;

    private static final int Id_isFinite = 7;

    private static final int Id_isNaN = 8;

    private static final int Id_isXMLName = 9;

    private static final int Id_parseFloat = 10;

    private static final int Id_parseInt = 11;

    private static final int Id_unescape = 12;

    private static final int Id_uneval = 13;

    private static final int LAST_SCOPE_FUNCTION_ID = 13;

    private static final int Id_new_CommonError = 14;

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        NativeGlobal obj = new NativeGlobal();
        for (int id = 1; id <= 13; id++) {
            int arity = 1;
            String name;
            switch(id) {
                case 1:
                    name = "decodeURI";
                    break;
                case 2:
                    name = "decodeURIComponent";
                    break;
                case 3:
                    name = "encodeURI";
                    break;
                case 4:
                    name = "encodeURIComponent";
                    break;
                case 5:
                    name = "escape";
                    break;
                case 6:
                    name = "eval";
                    break;
                case 7:
                    name = "isFinite";
                    break;
                case 8:
                    name = "isNaN";
                    break;
                case 9:
                    name = "isXMLName";
                    break;
                case 10:
                    name = "parseFloat";
                    break;
                case 11:
                    name = "parseInt";
                    arity = 2;
                    break;
                case 12:
                    name = "unescape";
                    break;
                case 13:
                    name = "uneval";
                    break;
                default:
                    throw Kit.codeBug();
            }
            IdFunctionObject f = new IdFunctionObject(obj, FTAG, id, name, arity, scope);
            if (sealed) {
                f.sealObject(cx);
            }
            f.exportAsScopeProperty(cx);
        }
        ScriptableObject.defineProperty(scope, "NaN", ScriptRuntime.NaNobj, 7, cx);
        ScriptableObject.defineProperty(scope, "Infinity", ScriptRuntime.wrapNumber(Double.POSITIVE_INFINITY), 7, cx);
        ScriptableObject.defineProperty(scope, "undefined", Undefined.instance, 7, cx);
        for (TopLevel.NativeErrors error : TopLevel.NativeErrors.values()) {
            if (error != TopLevel.NativeErrors.Error) {
                String name = error.name();
                ScriptableObject errorProto = (ScriptableObject) ScriptRuntime.newBuiltinObject(cx, scope, TopLevel.Builtins.Error, ScriptRuntime.EMPTY_OBJECTS);
                errorProto.put(cx, "name", errorProto, name);
                errorProto.put(cx, "message", errorProto, "");
                IdFunctionObject ctor = new IdFunctionObject(obj, FTAG, 14, name, 1, scope);
                ctor.markAsConstructor(errorProto);
                errorProto.put(cx, "constructor", errorProto, ctor);
                errorProto.setAttributes(cx, "constructor", 2);
                if (sealed) {
                    errorProto.sealObject(cx);
                    ctor.sealObject(cx);
                }
                ctor.exportAsScopeProperty(cx);
            }
        }
    }

    static Object js_parseInt(Object[] args, Context cx) {
        String s = ScriptRuntime.toString(cx, args, 0);
        int radix = ScriptRuntime.toInt32(cx, args, 1);
        int len = s.length();
        if (len == 0) {
            return ScriptRuntime.NaNobj;
        } else {
            boolean negative = false;
            int start = 0;
            char c;
            do {
                c = s.charAt(start);
            } while (ScriptRuntime.isStrWhiteSpaceChar(c) && ++start < len);
            if (c == '+' || (negative = c == '-')) {
                start++;
            }
            int NO_RADIX = -1;
            if (radix == 0) {
                radix = -1;
            } else {
                if (radix < 2 || radix > 36) {
                    return ScriptRuntime.NaNobj;
                }
                if (radix == 16 && len - start > 1 && s.charAt(start) == '0') {
                    c = s.charAt(start + 1);
                    if (c == 'x' || c == 'X') {
                        start += 2;
                    }
                }
            }
            if (radix == -1) {
                radix = 10;
                if (len - start > 1 && s.charAt(start) == '0') {
                    c = s.charAt(start + 1);
                    if (c == 'x' || c == 'X') {
                        radix = 16;
                        start += 2;
                    } else if ('0' <= c && c <= '9' && cx == null) {
                        radix = 8;
                        start++;
                    }
                }
            }
            double d = ScriptRuntime.stringPrefixToNumber(s, start, radix);
            return ScriptRuntime.wrapNumber(negative ? -d : d);
        }
    }

    static Object js_parseFloat(Context cx, Object[] args) {
        if (args.length < 1) {
            return ScriptRuntime.NaNobj;
        } else {
            String s = ScriptRuntime.toString(cx, args[0]);
            int len = s.length();
            for (int start = 0; start != len; start++) {
                char c = s.charAt(start);
                if (!ScriptRuntime.isStrWhiteSpaceChar(c)) {
                    int i = start;
                    if (c == '+' || c == '-') {
                        i = start + 1;
                        if (i == len) {
                            return ScriptRuntime.NaNobj;
                        }
                        c = s.charAt(i);
                    }
                    if (c == 'I') {
                        if (i + 8 <= len && s.regionMatches(i, "Infinity", 0, 8)) {
                            double d;
                            if (s.charAt(start) == '-') {
                                d = Double.NEGATIVE_INFINITY;
                            } else {
                                d = Double.POSITIVE_INFINITY;
                            }
                            return ScriptRuntime.wrapNumber(d);
                        }
                        return ScriptRuntime.NaNobj;
                    }
                    int decimal = -1;
                    int exponent = -1;
                    boolean exponentValid;
                    label86: for (exponentValid = false; i < len; i++) {
                        switch(s.charAt(i)) {
                            case '+':
                            case '-':
                                if (exponent == i - 1) {
                                    if (i == len - 1) {
                                        i--;
                                        break label86;
                                    }
                                    break;
                                }
                            case ',':
                            case '/':
                            case ':':
                            case ';':
                            case '<':
                            case '=':
                            case '>':
                            case '?':
                            case '@':
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'F':
                            case 'G':
                            case 'H':
                            case 'I':
                            case 'J':
                            case 'K':
                            case 'L':
                            case 'M':
                            case 'N':
                            case 'O':
                            case 'P':
                            case 'Q':
                            case 'R':
                            case 'S':
                            case 'T':
                            case 'U':
                            case 'V':
                            case 'W':
                            case 'X':
                            case 'Y':
                            case 'Z':
                            case '[':
                            case '\\':
                            case ']':
                            case '^':
                            case '_':
                            case '`':
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            default:
                                break label86;
                            case '.':
                                if (decimal != -1) {
                                    break label86;
                                }
                                decimal = i;
                                break;
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                if (exponent != -1) {
                                    exponentValid = true;
                                }
                                break;
                            case 'E':
                            case 'e':
                                if (exponent != -1 || i == len - 1) {
                                    break label86;
                                }
                                exponent = i;
                        }
                    }
                    if (exponent != -1 && !exponentValid) {
                        i = exponent;
                    }
                    s = s.substring(start, i);
                    try {
                        return Double.valueOf(s);
                    } catch (NumberFormatException var11) {
                        return ScriptRuntime.NaNobj;
                    }
                }
            }
            return ScriptRuntime.NaNobj;
        }
    }

    private static Object js_escape(Object[] args, Context cx) {
        int URL_XALPHAS = 1;
        int URL_XPALPHAS = 2;
        int URL_PATH = 4;
        String s = ScriptRuntime.toString(cx, args, 0);
        int mask = 7;
        if (args.length > 1) {
            double d = ScriptRuntime.toNumber(cx, args[1]);
            if (Double.isNaN(d) || (double) (mask = (int) d) != d || 0 != (mask & -8)) {
                throw Context.reportRuntimeError0("msg.bad.esc.mask", cx);
            }
        }
        StringBuilder sb = null;
        int k = 0;
        for (int L = s.length(); k != L; k++) {
            int c = s.charAt(k);
            if (mask == 0 || (c < 48 || c > 57) && (c < 65 || c > 90) && (c < 97 || c > 122) && c != 64 && c != 42 && c != 95 && c != 45 && c != 46 && (0 == (mask & 4) || c != 47 && c != 43)) {
                if (sb == null) {
                    sb = new StringBuilder(L + 3);
                    sb.append(s);
                    sb.setLength(k);
                }
                int hexSize;
                if (c < 256) {
                    if (c == 32 && mask == 2) {
                        sb.append('+');
                        continue;
                    }
                    sb.append('%');
                    hexSize = 2;
                } else {
                    sb.append('%');
                    sb.append('u');
                    hexSize = 4;
                }
                for (int shift = (hexSize - 1) * 4; shift >= 0; shift -= 4) {
                    int digit = 15 & c >> shift;
                    int hc = digit < 10 ? 48 + digit : 55 + digit;
                    sb.append((char) hc);
                }
            } else if (sb != null) {
                sb.append((char) c);
            }
        }
        return sb == null ? s : sb.toString();
    }

    private static Object js_unescape(Object[] args, Context cx) {
        String s = ScriptRuntime.toString(cx, args, 0);
        int firstEscapePos = s.indexOf(37);
        if (firstEscapePos >= 0) {
            int L = s.length();
            char[] buf = s.toCharArray();
            int destination = firstEscapePos;
            for (int k = firstEscapePos; k != L; destination++) {
                char c = buf[k];
                if (c == '%' && ++k != L) {
                    int end;
                    int start;
                    if (buf[k] == 'u') {
                        start = k + 1;
                        end = k + 5;
                    } else {
                        start = k;
                        end = k + 2;
                    }
                    if (end <= L) {
                        int x = 0;
                        for (int i = start; i != end; i++) {
                            x = Kit.xDigitToInt(buf[i], x);
                        }
                        if (x >= 0) {
                            c = (char) x;
                            k = end;
                        }
                    }
                }
                buf[destination] = c;
            }
            s = new String(buf, 0, destination);
        }
        return s;
    }

    private static Object js_eval(Context cx, Scriptable scope, Object[] args) {
        throw new RuntimeException("eval is not allowed!");
    }

    static boolean isEvalFunction(Object functionObj) {
        return !(functionObj instanceof IdFunctionObject function) ? false : function.hasTag(FTAG) && function.methodId() == 6;
    }

    private static String encode(String str, boolean fullUri, Context cx) {
        byte[] utf8buf = null;
        StringBuilder sb = null;
        int k = 0;
        for (int length = str.length(); k != length; k++) {
            char C = str.charAt(k);
            if (encodeUnescaped(C, fullUri)) {
                if (sb != null) {
                    sb.append(C);
                }
            } else {
                if (sb == null) {
                    sb = new StringBuilder(length + 3);
                    sb.append(str);
                    sb.setLength(k);
                    utf8buf = new byte[6];
                }
                if ('\udc00' <= C && C <= '\udfff') {
                    throw uriError(cx);
                }
                int V;
                if (C >= '\ud800' && '\udbff' >= C) {
                    if (++k == length) {
                        throw uriError(cx);
                    }
                    char C2 = str.charAt(k);
                    if ('\udc00' > C2 || C2 > '\udfff') {
                        throw uriError(cx);
                    }
                    V = (C - '\ud800' << 10) + (C2 - '\udc00') + 65536;
                } else {
                    V = C;
                }
                int L = oneUcs4ToUtf8Char(utf8buf, V);
                for (int j = 0; j < L; j++) {
                    int d = 255 & utf8buf[j];
                    sb.append('%');
                    sb.append(toHexChar(d >>> 4));
                    sb.append(toHexChar(d & 15));
                }
            }
        }
        return sb == null ? str : sb.toString();
    }

    private static char toHexChar(int i) {
        if (i >> 4 != 0) {
            Kit.codeBug();
        }
        return (char) (i < 10 ? i + 48 : i - 10 + 65);
    }

    private static int unHex(char c) {
        if ('A' <= c && c <= 'F') {
            return c - 65 + 10;
        } else if ('a' <= c && c <= 'f') {
            return c - 97 + 10;
        } else {
            return 48 <= c && c <= 57 ? c - 48 : -1;
        }
    }

    private static int unHex(char c1, char c2) {
        int i1 = unHex(c1);
        int i2 = unHex(c2);
        return i1 >= 0 && i2 >= 0 ? i1 << 4 | i2 : -1;
    }

    private static String decode(String str, boolean fullUri, Context cx) {
        char[] buf = null;
        int bufTop = 0;
        int k = 0;
        int length = str.length();
        while (k != length) {
            char C = str.charAt(k);
            if (C == '%') {
                if (buf == null) {
                    buf = new char[length];
                    str.getChars(0, k, buf, 0);
                    bufTop = k;
                }
                int start = k;
                if (k + 3 > length) {
                    throw uriError(cx);
                }
                int B = unHex(str.charAt(k + 1), str.charAt(k + 2));
                if (B < 0) {
                    throw uriError(cx);
                }
                k += 3;
                if ((B & 128) == 0) {
                    C = (char) B;
                } else {
                    if ((B & 192) == 128) {
                        throw uriError(cx);
                    }
                    int utf8Tail;
                    int ucs4Char;
                    int minUcs4Char;
                    if ((B & 32) == 0) {
                        utf8Tail = 1;
                        ucs4Char = B & 31;
                        minUcs4Char = 128;
                    } else if ((B & 16) == 0) {
                        utf8Tail = 2;
                        ucs4Char = B & 15;
                        minUcs4Char = 2048;
                    } else if ((B & 8) == 0) {
                        utf8Tail = 3;
                        ucs4Char = B & 7;
                        minUcs4Char = 65536;
                    } else if ((B & 4) == 0) {
                        utf8Tail = 4;
                        ucs4Char = B & 3;
                        minUcs4Char = 2097152;
                    } else {
                        if ((B & 2) != 0) {
                            throw uriError(cx);
                        }
                        utf8Tail = 5;
                        ucs4Char = B & 1;
                        minUcs4Char = 67108864;
                    }
                    if (k + 3 * utf8Tail > length) {
                        throw uriError(cx);
                    }
                    for (int j = 0; j != utf8Tail; j++) {
                        if (str.charAt(k) != '%') {
                            throw uriError(cx);
                        }
                        B = unHex(str.charAt(k + 1), str.charAt(k + 2));
                        if (B < 0 || (B & 192) != 128) {
                            throw uriError(cx);
                        }
                        ucs4Char = ucs4Char << 6 | B & 63;
                        k += 3;
                    }
                    if (ucs4Char >= minUcs4Char && (ucs4Char < 55296 || ucs4Char > 57343)) {
                        if (ucs4Char == 65534 || ucs4Char == 65535) {
                            ucs4Char = 65533;
                        }
                    } else {
                        ucs4Char = Integer.MAX_VALUE;
                    }
                    if (ucs4Char >= 65536) {
                        ucs4Char -= 65536;
                        if (ucs4Char > 1048575) {
                            throw uriError(cx);
                        }
                        char H = (char) ((ucs4Char >>> 10) + 55296);
                        C = (char) ((ucs4Char & 1023) + 56320);
                        buf[bufTop++] = H;
                    } else {
                        C = (char) ucs4Char;
                    }
                }
                if (fullUri && ";/?:@&=+$,#".indexOf(C) >= 0) {
                    for (int x = start; x != k; x++) {
                        buf[bufTop++] = str.charAt(x);
                    }
                } else {
                    buf[bufTop++] = C;
                }
            } else {
                if (buf != null) {
                    buf[bufTop++] = C;
                }
                k++;
            }
        }
        return buf == null ? str : new String(buf, 0, bufTop);
    }

    private static boolean encodeUnescaped(char c, boolean fullUri) {
        if (('A' > c || c > 'Z') && ('a' > c || c > 'z') && ('0' > c || c > '9')) {
            if ("-_.!~*'()".indexOf(c) >= 0) {
                return true;
            } else {
                return fullUri ? ";/?:@&=+$,#".indexOf(c) >= 0 : false;
            }
        } else {
            return true;
        }
    }

    private static EcmaError uriError(Context cx) {
        return ScriptRuntime.constructError(cx, "URIError", ScriptRuntime.getMessage0("msg.bad.uri"));
    }

    private static int oneUcs4ToUtf8Char(byte[] utf8Buffer, int ucs4Char) {
        int utf8Length = 1;
        if ((ucs4Char & -128) == 0) {
            utf8Buffer[0] = (byte) ucs4Char;
        } else {
            int a = ucs4Char >>> 11;
            for (utf8Length = 2; a != 0; utf8Length++) {
                a >>>= 5;
            }
            for (int i = utf8Length; --i > 0; ucs4Char >>>= 6) {
                utf8Buffer[i] = (byte) (ucs4Char & 63 | 128);
            }
            utf8Buffer[0] = (byte) (256 - (1 << 8 - utf8Length) + ucs4Char);
        }
        return utf8Length;
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (f.hasTag(FTAG)) {
            int methodId = f.methodId();
            switch(methodId) {
                case 1:
                case 2:
                    {
                        String str = ScriptRuntime.toString(cx, args, 0);
                        return decode(str, methodId == 1, cx);
                    }
                case 3:
                case 4:
                    {
                        String str = ScriptRuntime.toString(cx, args, 0);
                        return encode(str, methodId == 3, cx);
                    }
                case 5:
                    return js_escape(args, cx);
                case 6:
                    return js_eval(cx, scope, args);
                case 7:
                    if (args.length < 1) {
                        return Boolean.FALSE;
                    }
                    return NativeNumber.isFinite(args[0], cx);
                case 8:
                    boolean result;
                    if (args.length < 1) {
                        result = true;
                    } else {
                        double d = ScriptRuntime.toNumber(cx, args[0]);
                        result = Double.isNaN(d);
                    }
                    return result;
                case 9:
                    return Boolean.FALSE;
                case 10:
                    return js_parseFloat(cx, args);
                case 11:
                    return js_parseInt(args, cx);
                case 12:
                    return js_unescape(args, cx);
                case 13:
                    Object value = args.length != 0 ? args[0] : Undefined.instance;
                    return ScriptRuntime.uneval(cx, scope, value);
                case 14:
                    return NativeError.make(cx, scope, f, args);
            }
        }
        throw f.unknown();
    }
}