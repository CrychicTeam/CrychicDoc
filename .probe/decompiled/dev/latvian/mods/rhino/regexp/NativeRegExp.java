package dev.latvian.mods.rhino.regexp;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Function;
import dev.latvian.mods.rhino.IdFunctionObject;
import dev.latvian.mods.rhino.IdScriptableObject;
import dev.latvian.mods.rhino.Kit;
import dev.latvian.mods.rhino.MemberType;
import dev.latvian.mods.rhino.ScriptRuntime;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.Symbol;
import dev.latvian.mods.rhino.SymbolKey;
import dev.latvian.mods.rhino.TopLevel;
import dev.latvian.mods.rhino.Undefined;

public class NativeRegExp extends IdScriptableObject implements Function {

    public static final int JSREG_GLOB = 1;

    public static final int JSREG_FOLD = 2;

    public static final int JSREG_MULTILINE = 4;

    public static final int TEST = 0;

    public static final int MATCH = 1;

    public static final int PREFIX = 2;

    private static final Object REGEXP_TAG = new Object();

    private static final boolean debug = false;

    private static final byte REOP_SIMPLE_START = 1;

    private static final byte REOP_EMPTY = 1;

    private static final byte REOP_BOL = 2;

    private static final byte REOP_EOL = 3;

    private static final byte REOP_WBDRY = 4;

    private static final byte REOP_WNONBDRY = 5;

    private static final byte REOP_DOT = 6;

    private static final byte REOP_DIGIT = 7;

    private static final byte REOP_NONDIGIT = 8;

    private static final byte REOP_ALNUM = 9;

    private static final byte REOP_NONALNUM = 10;

    private static final byte REOP_SPACE = 11;

    private static final byte REOP_NONSPACE = 12;

    private static final byte REOP_BACKREF = 13;

    private static final byte REOP_FLAT = 14;

    private static final byte REOP_FLAT1 = 15;

    private static final byte REOP_FLATi = 16;

    private static final byte REOP_FLAT1i = 17;

    private static final byte REOP_UCFLAT1 = 18;

    private static final byte REOP_UCFLAT1i = 19;

    private static final byte REOP_CLASS = 22;

    private static final byte REOP_NCLASS = 23;

    private static final byte REOP_SIMPLE_END = 23;

    private static final byte REOP_QUANT = 25;

    private static final byte REOP_STAR = 26;

    private static final byte REOP_PLUS = 27;

    private static final byte REOP_OPT = 28;

    private static final byte REOP_LPAREN = 29;

    private static final byte REOP_RPAREN = 30;

    private static final byte REOP_ALT = 31;

    private static final byte REOP_JUMP = 32;

    private static final byte REOP_ASSERT = 41;

    private static final byte REOP_ASSERT_NOT = 42;

    private static final byte REOP_ASSERTTEST = 43;

    private static final byte REOP_ASSERTNOTTEST = 44;

    private static final byte REOP_MINIMALSTAR = 45;

    private static final byte REOP_MINIMALPLUS = 46;

    private static final byte REOP_MINIMALOPT = 47;

    private static final byte REOP_MINIMALQUANT = 48;

    private static final byte REOP_ENDCHILD = 49;

    private static final byte REOP_REPEAT = 51;

    private static final byte REOP_MINIMALREPEAT = 52;

    private static final byte REOP_ALTPREREQ = 53;

    private static final byte REOP_ALTPREREQi = 54;

    private static final byte REOP_ALTPREREQ2 = 55;

    private static final byte REOP_END = 57;

    private static final int ANCHOR_BOL = -2;

    private static final int INDEX_LEN = 2;

    private static final int Id_lastIndex = 1;

    private static final int Id_source = 2;

    private static final int Id_global = 3;

    private static final int Id_ignoreCase = 4;

    private static final int Id_multiline = 5;

    private static final int MAX_INSTANCE_ID = 5;

    private static final int Id_compile = 1;

    private static final int Id_toString = 2;

    private static final int Id_toSource = 3;

    private static final int Id_exec = 4;

    private static final int Id_test = 5;

    private static final int Id_prefix = 6;

    private static final int SymbolId_match = 7;

    private static final int SymbolId_search = 8;

    private static final int MAX_PROTOTYPE_ID = 8;

    Object lastIndex = ScriptRuntime.zeroObj;

    private RECompiled re;

    private int lastIndexAttr = 6;

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        NativeRegExp proto = new NativeRegExp();
        proto.re = compileRE(cx, "", null, false);
        proto.activatePrototypeMap(8);
        proto.setParentScope(scope);
        proto.setPrototype(getObjectPrototype(scope, cx));
        NativeRegExpCtor ctor = new NativeRegExpCtor();
        proto.defineProperty(cx, "constructor", ctor, 2);
        ScriptRuntime.setFunctionProtoAndParent(cx, scope, ctor);
        ctor.setImmunePrototypeProperty(proto);
        if (sealed) {
            proto.sealObject(cx);
            ctor.sealObject(cx);
        }
        defineProperty(scope, "RegExp", ctor, 2, cx);
    }

    private static String escapeRegExp(Context cx, Object src) {
        String s = ScriptRuntime.toString(cx, src);
        StringBuilder sb = null;
        int start = 0;
        for (int slash = s.indexOf(47); slash > -1; slash = s.indexOf(47, slash + 1)) {
            if (slash == start || s.charAt(slash - 1) != '\\') {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(s, start, slash);
                sb.append("\\/");
                start = slash + 1;
            }
        }
        if (sb != null) {
            sb.append(s, start, s.length());
            s = sb.toString();
        }
        return s;
    }

    static RECompiled compileRE(Context cx, String str, String global, boolean flat) {
        RECompiled regexp = new RECompiled(str);
        int length = str.length();
        int flags = 0;
        if (global != null) {
            for (int i = 0; i < global.length(); i++) {
                char c = global.charAt(i);
                int f = 0;
                if (c == 'g') {
                    f = 1;
                } else if (c == 'i') {
                    f = 2;
                } else if (c == 'm') {
                    f = 4;
                } else {
                    reportError("msg.invalid.re.flag", String.valueOf(c), cx);
                }
                if ((flags & f) != 0) {
                    reportError("msg.invalid.re.flag", String.valueOf(c), cx);
                }
                flags |= f;
            }
        }
        regexp.flags = flags;
        CompilerState state = new CompilerState(cx, regexp.source, length, flags);
        if (flat && length > 0) {
            state.result = new RENode((byte) 14);
            state.result.chr = state.cpbegin[0];
            state.result.length = length;
            state.result.flatIndex = 0;
            state.progLength += 5;
        } else {
            if (!parseDisjunction(state, cx)) {
                return null;
            }
            if (state.maxBackReference > state.parenCount) {
                state = new CompilerState(cx, regexp.source, length, flags);
                state.backReferenceLimit = state.parenCount;
                if (!parseDisjunction(state, cx)) {
                    return null;
                }
            }
        }
        regexp.program = new byte[state.progLength + 1];
        if (state.classCount != 0) {
            regexp.classList = new RECharSet[state.classCount];
            regexp.classCount = state.classCount;
        }
        int endPC = emitREBytecode(state, regexp, 0, state.result, cx);
        regexp.program[endPC++] = 57;
        regexp.parenCount = state.parenCount;
        switch(regexp.program[0]) {
            case 2:
                regexp.anchorCh = -2;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            default:
                break;
            case 14:
            case 16:
                int k = getIndex(regexp.program, 1);
                regexp.anchorCh = regexp.source[k];
                break;
            case 15:
            case 17:
                regexp.anchorCh = (char) (regexp.program[1] & 255);
                break;
            case 18:
            case 19:
                regexp.anchorCh = (char) getIndex(regexp.program, 1);
                break;
            case 31:
                RENode n = state.result;
                if (n.kid.op == 2 && n.kid2.op == 2) {
                    regexp.anchorCh = -2;
                }
        }
        return regexp;
    }

    static boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    private static boolean isWord(char c) {
        return 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z' || isDigit(c) || c == '_';
    }

    private static boolean isControlLetter(char c) {
        return 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z';
    }

    private static boolean isLineTerm(char c) {
        return ScriptRuntime.isJSLineTerminator(c);
    }

    private static boolean isREWhiteSpace(int c) {
        return ScriptRuntime.isJSWhitespaceOrLineTerminator(c);
    }

    private static char upcase(char ch) {
        if (ch < 128) {
            return 'a' <= ch && ch <= 'z' ? (char) (ch + -32) : ch;
        } else {
            char cu = Character.toUpperCase(ch);
            return cu < 128 ? ch : cu;
        }
    }

    private static char downcase(char ch) {
        if (ch < 128) {
            return 'A' <= ch && ch <= 'Z' ? (char) (ch + ' ') : ch;
        } else {
            char cl = Character.toLowerCase(ch);
            return cl < 128 ? ch : cl;
        }
    }

    private static int toASCIIHexDigit(int c) {
        if (c < 48) {
            return -1;
        } else if (c <= 57) {
            return c - 48;
        } else {
            c |= 32;
            return 97 <= c && c <= 102 ? c - 97 + 10 : -1;
        }
    }

    private static boolean parseDisjunction(CompilerState state, Context cx) {
        if (!parseAlternative(state, cx)) {
            return false;
        } else {
            char[] source = state.cpbegin;
            int index = state.cp;
            if (index != source.length && source[index] == '|') {
                state.cp++;
                RENode result = new RENode((byte) 31);
                result.kid = state.result;
                if (!parseDisjunction(state, cx)) {
                    return false;
                }
                result.kid2 = state.result;
                state.result = result;
                if (result.kid.op == 14 && result.kid2.op == 14) {
                    result.op = (byte) ((state.flags & 2) == 0 ? 53 : 54);
                    result.chr = result.kid.chr;
                    result.index = result.kid2.chr;
                    state.progLength += 13;
                } else if (result.kid.op == 22 && result.kid.index < 256 && result.kid2.op == 14 && (state.flags & 2) == 0) {
                    result.op = 55;
                    result.chr = result.kid2.chr;
                    result.index = result.kid.index;
                    state.progLength += 13;
                } else if (result.kid.op == 14 && result.kid2.op == 22 && result.kid2.index < 256 && (state.flags & 2) == 0) {
                    result.op = 55;
                    result.chr = result.kid.chr;
                    result.index = result.kid2.index;
                    state.progLength += 13;
                } else {
                    state.progLength += 9;
                }
            }
            return true;
        }
    }

    private static boolean parseAlternative(CompilerState state, Context cx) {
        RENode headTerm = null;
        RENode tailTerm = null;
        char[] source = state.cpbegin;
        while (state.cp != state.cpend && source[state.cp] != '|' && (state.parenNesting == 0 || source[state.cp] != ')')) {
            if (!parseTerm(state, cx)) {
                return false;
            }
            if (headTerm == null) {
                headTerm = state.result;
                tailTerm = headTerm;
            } else {
                tailTerm.next = state.result;
            }
            while (tailTerm.next != null) {
                tailTerm = tailTerm.next;
            }
        }
        if (headTerm == null) {
            state.result = new RENode((byte) 1);
        } else {
            state.result = headTerm;
        }
        return true;
    }

    private static boolean calculateBitmapSize(CompilerState state, RENode target, char[] src, int index, int end, Context cx) {
        char rangeStart = 0;
        int max = 0;
        boolean inRange = false;
        target.bmsize = 0;
        target.sense = true;
        if (index == end) {
            return true;
        } else {
            if (src[index] == '^') {
                index++;
                target.sense = false;
            }
            while (index != end) {
                int localMax = 0;
                int nDigits = 2;
                if (src[index] == '\\') {
                    int var10001 = ++index;
                    index++;
                    char c = src[var10001];
                    switch(c) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                            int n = c - '0';
                            c = src[index];
                            if ('0' <= c && c <= '7') {
                                index++;
                                n = 8 * n + (c - '0');
                                c = src[index];
                                if ('0' <= c && c <= '7') {
                                    index++;
                                    int i = 8 * n + (c - '0');
                                    if (i <= 255) {
                                        n = i;
                                    } else {
                                        index--;
                                    }
                                }
                            }
                            localMax = n;
                            break;
                        case '8':
                        case '9':
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
                        case 'E':
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
                        case 'T':
                        case 'U':
                        case 'V':
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
                        case 'e':
                        case 'g':
                        case 'h':
                        case 'i':
                        case 'j':
                        case 'k':
                        case 'l':
                        case 'm':
                        case 'o':
                        case 'p':
                        case 'q':
                        default:
                            localMax = c;
                            break;
                        case 'D':
                        case 'S':
                        case 'W':
                        case 's':
                        case 'w':
                            target.bmsize = 65536;
                            return true;
                        case 'b':
                            localMax = 8;
                            break;
                        case 'c':
                            if (index < end && isControlLetter(src[index])) {
                                int var23 = (char) (src[index++] & 31);
                            } else {
                                index--;
                            }
                            localMax = 92;
                            break;
                        case 'd':
                            if (inRange) {
                                target.bmsize = 65536;
                                return true;
                            }
                            localMax = 57;
                            break;
                        case 'f':
                            localMax = 12;
                            break;
                        case 'n':
                            localMax = 10;
                            break;
                        case 'r':
                            localMax = 13;
                            break;
                        case 't':
                            localMax = 9;
                            break;
                        case 'u':
                            nDigits += 2;
                        case 'x':
                            int n = 0;
                            for (int i = 0; i < nDigits && index < end; i++) {
                                c = src[index++];
                                n = Kit.xDigitToInt(c, n);
                                if (n < 0) {
                                    index -= i + 1;
                                    n = 92;
                                    break;
                                }
                            }
                            localMax = n;
                            break;
                        case 'v':
                            localMax = 11;
                    }
                } else {
                    localMax = src[index++];
                }
                if (inRange) {
                    if (rangeStart > localMax) {
                        reportError("msg.bad.range", "", cx);
                        return false;
                    }
                    inRange = false;
                } else if (index < end - 1 && src[index] == '-') {
                    index++;
                    inRange = true;
                    rangeStart = (char) localMax;
                    continue;
                }
                if ((state.flags & 2) != 0) {
                    char cu = upcase((char) localMax);
                    char cd = downcase((char) localMax);
                    localMax = cu >= cd ? cu : cd;
                }
                if (localMax > max) {
                    max = localMax;
                }
            }
            target.bmsize = max + 1;
            return true;
        }
    }

    private static void doFlat(CompilerState state, char c) {
        state.result = new RENode((byte) 14);
        state.result.chr = c;
        state.result.length = 1;
        state.result.flatIndex = -1;
        state.progLength += 3;
    }

    private static int getDecimalValue(char c, CompilerState state, int maxValue, String overflowMessageId, Context cx) {
        boolean overflow = false;
        int start = state.cp;
        char[] src = state.cpbegin;
        int value;
        for (value = c - '0'; state.cp != state.cpend; state.cp++) {
            c = src[state.cp];
            if (!isDigit(c)) {
                break;
            }
            if (!overflow) {
                int v = value * 10 + (c - '0');
                if (v < maxValue) {
                    value = v;
                } else {
                    overflow = true;
                    value = maxValue;
                }
            }
        }
        if (overflow) {
            reportError(overflowMessageId, String.valueOf(src, start, state.cp - start), cx);
        }
        return value;
    }

    private static boolean parseTerm(CompilerState state, Context cx) {
        char[] src;
        int parenBaseCount;
        src = state.cpbegin;
        char c = src[state.cp++];
        int nDigits = 2;
        parenBaseCount = state.parenCount;
        label216: switch(c) {
            case '$':
                state.result = new RENode((byte) 3);
                state.progLength++;
                return true;
            case '(':
                RENode result = null;
                int termStart = state.cp;
                if (state.cp + 1 < state.cpend && src[state.cp] == '?' && ((c = src[state.cp + 1]) == '=' || c == '!' || c == ':')) {
                    state.cp += 2;
                    if (c == '=') {
                        result = new RENode((byte) 41);
                        state.progLength += 4;
                    } else if (c == '!') {
                        result = new RENode((byte) 42);
                        state.progLength += 4;
                    }
                } else {
                    result = new RENode((byte) 29);
                    state.progLength += 6;
                    result.parenIndex = state.parenCount++;
                }
                state.parenNesting++;
                if (!parseDisjunction(state, cx)) {
                    return false;
                }
                if (state.cp == state.cpend || src[state.cp] != ')') {
                    reportError("msg.unterm.paren", "", cx);
                    return false;
                }
                state.cp++;
                state.parenNesting--;
                if (result != null) {
                    result.kid = state.result;
                    state.result = result;
                }
                break;
            case ')':
                reportError("msg.re.unmatched.right.paren", "", cx);
                return false;
            case '*':
            case '+':
            case '?':
                reportError("msg.bad.quant", String.valueOf(src[state.cp - 1]), cx);
                return false;
            case '.':
                state.result = new RENode((byte) 6);
                state.progLength++;
                break;
            case '[':
                state.result = new RENode((byte) 22);
                int termStart = state.cp;
                for (state.result.startIndex = termStart; state.cp != state.cpend; state.cp++) {
                    if (src[state.cp] == '\\') {
                        state.cp++;
                    } else if (src[state.cp] == ']') {
                        state.result.kidlen = state.cp - termStart;
                        state.result.index = state.classCount++;
                        if (!calculateBitmapSize(state, state.result, src, termStart, state.cp++, cx)) {
                            return false;
                        }
                        state.progLength += 3;
                        break label216;
                    }
                }
                reportError("msg.unterm.class", "", cx);
                return false;
            case '\\':
                if (state.cp >= state.cpend) {
                    reportError("msg.trail.backslash", "", cx);
                    return false;
                }
                c = src[state.cp++];
                switch(c) {
                    case '0':
                        int numx = 0;
                        while (numx < 32 && state.cp < state.cpend) {
                            c = src[state.cp];
                            if (c < '0' || c > '7') {
                                break;
                            }
                            state.cp++;
                            numx = 8 * numx + (c - '0');
                        }
                        c = (char) numx;
                        doFlat(state, c);
                        break label216;
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        int termStart = state.cp - 1;
                        int num = getDecimalValue(c, state, 65535, "msg.overlarge.backref", cx);
                        if (num > state.backReferenceLimit) {
                            state.cp = termStart;
                            if (c >= '8') {
                                c = '\\';
                                doFlat(state, c);
                            } else {
                                state.cp++;
                                num = c - '0';
                                while (num < 32 && state.cp < state.cpend) {
                                    c = src[state.cp];
                                    if (c < '0' || c > '7') {
                                        break;
                                    }
                                    state.cp++;
                                    num = 8 * num + (c - '0');
                                }
                                c = (char) num;
                                doFlat(state, c);
                            }
                        } else {
                            state.result = new RENode((byte) 13);
                            state.result.parenIndex = num - 1;
                            state.progLength += 3;
                            if (state.maxBackReference < num) {
                                state.maxBackReference = num;
                            }
                        }
                        break label216;
                    case ':':
                    case ';':
                    case '<':
                    case '=':
                    case '>':
                    case '?':
                    case '@':
                    case 'A':
                    case 'C':
                    case 'E':
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
                    case 'T':
                    case 'U':
                    case 'V':
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
                    case 'e':
                    case 'g':
                    case 'h':
                    case 'i':
                    case 'j':
                    case 'k':
                    case 'l':
                    case 'm':
                    case 'o':
                    case 'p':
                    case 'q':
                    default:
                        state.result = new RENode((byte) 14);
                        state.result.chr = c;
                        state.result.length = 1;
                        state.result.flatIndex = state.cp - 1;
                        state.progLength += 3;
                        break label216;
                    case 'B':
                        state.result = new RENode((byte) 5);
                        state.progLength++;
                        return true;
                    case 'D':
                        state.result = new RENode((byte) 8);
                        state.progLength++;
                        break label216;
                    case 'S':
                        state.result = new RENode((byte) 12);
                        state.progLength++;
                        break label216;
                    case 'W':
                        state.result = new RENode((byte) 10);
                        state.progLength++;
                        break label216;
                    case 'b':
                        state.result = new RENode((byte) 4);
                        state.progLength++;
                        return true;
                    case 'c':
                        if (state.cp < state.cpend && isControlLetter(src[state.cp])) {
                            c = (char) (src[state.cp++] & 31);
                        } else {
                            state.cp--;
                            c = '\\';
                        }
                        doFlat(state, c);
                        break label216;
                    case 'd':
                        state.result = new RENode((byte) 7);
                        state.progLength++;
                        break label216;
                    case 'f':
                        c = '\f';
                        doFlat(state, c);
                        break label216;
                    case 'n':
                        c = '\n';
                        doFlat(state, c);
                        break label216;
                    case 'r':
                        c = '\r';
                        doFlat(state, c);
                        break label216;
                    case 's':
                        state.result = new RENode((byte) 11);
                        state.progLength++;
                        break label216;
                    case 't':
                        c = '\t';
                        doFlat(state, c);
                        break label216;
                    case 'u':
                        nDigits += 2;
                    case 'x':
                        int n = 0;
                        for (int i = 0; i < nDigits && state.cp < state.cpend; i++) {
                            c = src[state.cp++];
                            n = Kit.xDigitToInt(c, n);
                            if (n < 0) {
                                state.cp -= i + 2;
                                n = src[state.cp++];
                                break;
                            }
                        }
                        c = (char) n;
                        doFlat(state, c);
                        break label216;
                    case 'v':
                        c = '\u000b';
                        doFlat(state, c);
                        break label216;
                    case 'w':
                        state.result = new RENode((byte) 9);
                        state.progLength++;
                        break label216;
                }
            case '^':
                state.result = new RENode((byte) 2);
                state.progLength++;
                return true;
            default:
                state.result = new RENode((byte) 14);
                state.result.chr = c;
                state.result.length = 1;
                state.result.flatIndex = state.cp - 1;
                state.progLength += 3;
        }
        RENode term = state.result;
        if (state.cp == state.cpend) {
            return true;
        } else {
            boolean hasQ = false;
            switch(src[state.cp]) {
                case '*':
                    state.result = new RENode((byte) 25);
                    state.result.min = 0;
                    state.result.max = -1;
                    state.progLength += 8;
                    hasQ = true;
                    break;
                case '+':
                    state.result = new RENode((byte) 25);
                    state.result.min = 1;
                    state.result.max = -1;
                    state.progLength += 8;
                    hasQ = true;
                    break;
                case '?':
                    state.result = new RENode((byte) 25);
                    state.result.min = 0;
                    state.result.max = 1;
                    state.progLength += 8;
                    hasQ = true;
                    break;
                case '{':
                    int min = 0;
                    int max = -1;
                    int leftCurl = state.cp;
                    if (++state.cp < src.length && isDigit(c = src[state.cp])) {
                        state.cp++;
                        min = getDecimalValue(c, state, 65535, "msg.overlarge.min", cx);
                        if (state.cp < src.length) {
                            c = src[state.cp];
                            if (c == ',' && ++state.cp < src.length) {
                                c = src[state.cp];
                                if (isDigit(c) && ++state.cp < src.length) {
                                    max = getDecimalValue(c, state, 65535, "msg.overlarge.max", cx);
                                    c = src[state.cp];
                                    if (min > max) {
                                        reportError("msg.max.lt.min", String.valueOf(src[state.cp]), cx);
                                        return false;
                                    }
                                }
                            } else {
                                max = min;
                            }
                            if (c == '}') {
                                state.result = new RENode((byte) 25);
                                state.result.min = min;
                                state.result.max = max;
                                state.progLength += 12;
                                hasQ = true;
                            }
                        }
                    }
                    if (!hasQ) {
                        state.cp = leftCurl;
                    }
            }
            if (!hasQ) {
                return true;
            } else {
                state.cp++;
                state.result.kid = term;
                state.result.parenIndex = parenBaseCount;
                state.result.parenCount = state.parenCount - parenBaseCount;
                if (state.cp < state.cpend && src[state.cp] == '?') {
                    state.cp++;
                    state.result.greedy = false;
                } else {
                    state.result.greedy = true;
                }
                return true;
            }
        }
    }

    private static void resolveForwardJump(byte[] array, int from, int pc, Context cx) {
        if (from > pc) {
            throw Kit.codeBug();
        } else {
            addIndex(array, from, pc - from, cx);
        }
    }

    private static int getOffset(byte[] array, int pc) {
        return getIndex(array, pc);
    }

    private static int addIndex(byte[] array, int pc, int index, Context cx) {
        if (index < 0) {
            throw Kit.codeBug();
        } else if (index > 65535) {
            throw Context.reportRuntimeError("Too complex regexp", cx);
        } else {
            array[pc] = (byte) (index >> 8);
            array[pc + 1] = (byte) index;
            return pc + 2;
        }
    }

    private static int getIndex(byte[] array, int pc) {
        return (array[pc] & 0xFF) << 8 | array[pc + 1] & 0xFF;
    }

    private static int emitREBytecode(CompilerState state, RECompiled re, int pc, RENode t, Context cx) {
        byte[] program = re.program;
        while (t != null) {
            program[pc++] = t.op;
            switch(t.op) {
                case 1:
                    pc--;
                    break;
                case 13:
                    pc = addIndex(program, pc, t.parenIndex, cx);
                    break;
                case 14:
                    if (t.flatIndex != -1) {
                        while (t.next != null && t.next.op == 14 && t.flatIndex + t.length == t.next.flatIndex) {
                            t.length = t.length + t.next.length;
                            t.next = t.next.next;
                        }
                    }
                    if (t.flatIndex != -1 && t.length > 1) {
                        if ((state.flags & 2) != 0) {
                            program[pc - 1] = 16;
                        } else {
                            program[pc - 1] = 14;
                        }
                        pc = addIndex(program, pc, t.flatIndex, cx);
                        pc = addIndex(program, pc, t.length, cx);
                    } else if (t.chr < 256) {
                        if ((state.flags & 2) != 0) {
                            program[pc - 1] = 17;
                        } else {
                            program[pc - 1] = 15;
                        }
                        program[pc++] = (byte) t.chr;
                    } else {
                        if ((state.flags & 2) != 0) {
                            program[pc - 1] = 19;
                        } else {
                            program[pc - 1] = 18;
                        }
                        pc = addIndex(program, pc, t.chr, cx);
                    }
                    break;
                case 22:
                    if (!t.sense) {
                        program[pc - 1] = 23;
                    }
                    pc = addIndex(program, pc, t.index, cx);
                    re.classList[t.index] = new RECharSet(t.bmsize, t.startIndex, t.kidlen, t.sense);
                    break;
                case 25:
                    if (t.min == 0 && t.max == -1) {
                        program[pc - 1] = (byte) (t.greedy ? 26 : 45);
                    } else if (t.min == 0 && t.max == 1) {
                        program[pc - 1] = (byte) (t.greedy ? 28 : 47);
                    } else if (t.min == 1 && t.max == -1) {
                        program[pc - 1] = (byte) (t.greedy ? 27 : 46);
                    } else {
                        if (!t.greedy) {
                            program[pc - 1] = 48;
                        }
                        pc = addIndex(program, pc, t.min, cx);
                        pc = addIndex(program, pc, t.max + 1, cx);
                    }
                    int var25 = addIndex(program, pc, t.parenCount, cx);
                    int var26 = addIndex(program, var25, t.parenIndex, cx);
                    int var27 = var26 + 2;
                    pc = emitREBytecode(state, re, var27, t.kid, cx);
                    program[pc++] = 49;
                    resolveForwardJump(program, var26, pc, cx);
                    break;
                case 29:
                    pc = addIndex(program, pc, t.parenIndex, cx);
                    pc = emitREBytecode(state, re, pc, t.kid, cx);
                    program[pc++] = 30;
                    pc = addIndex(program, pc, t.parenIndex, cx);
                    break;
                case 41:
                    {
                        int nextTermFixup = pc;
                        int var19 = pc + 2;
                        pc = emitREBytecode(state, re, var19, t.kid, cx);
                        program[pc++] = 43;
                        resolveForwardJump(program, nextTermFixup, pc, cx);
                        break;
                    }
                case 42:
                    {
                        int nextTermFixup = pc;
                        int var17 = pc + 2;
                        pc = emitREBytecode(state, re, var17, t.kid, cx);
                        program[pc++] = 44;
                        resolveForwardJump(program, nextTermFixup, pc, cx);
                        break;
                    }
                case 53:
                case 54:
                case 55:
                    boolean ignoreCase = t.op == 54;
                    addIndex(program, pc, ignoreCase ? upcase(t.chr) : t.chr, cx);
                    pc += 2;
                    addIndex(program, pc, ignoreCase ? upcase((char) t.index) : t.index, cx);
                    pc += 2;
                case 31:
                    RENode nextAlt = t.kid2;
                    int var11 = pc + 2;
                    int var12 = emitREBytecode(state, re, var11, t.kid, cx);
                    program[var12++] = 32;
                    int var14 = var12 + 2;
                    resolveForwardJump(program, pc, var14, cx);
                    int var15 = emitREBytecode(state, re, var14, nextAlt, cx);
                    program[var15++] = 32;
                    pc = var15 + 2;
                    resolveForwardJump(program, var12, pc, cx);
                    resolveForwardJump(program, var15, pc, cx);
            }
            t = t.next;
        }
        return pc;
    }

    private static void pushProgState(REGlobalData gData, int min, int max, int cp, REBackTrackData backTrackLastToSave, int continuationOp, int continuationPc) {
        gData.stateStackTop = new REProgState(gData.stateStackTop, min, max, cp, backTrackLastToSave, continuationOp, continuationPc);
    }

    private static REProgState popProgState(REGlobalData gData) {
        REProgState state = gData.stateStackTop;
        gData.stateStackTop = state.previous;
        return state;
    }

    private static void pushBackTrackState(REGlobalData gData, byte op, int pc) {
        REProgState state = gData.stateStackTop;
        gData.backTrackStackTop = new REBackTrackData(gData, op, pc, gData.cp, state.continuationOp, state.continuationPc);
    }

    private static void pushBackTrackState(REGlobalData gData, byte op, int pc, int cp, int continuationOp, int continuationPc) {
        gData.backTrackStackTop = new REBackTrackData(gData, op, pc, cp, continuationOp, continuationPc);
    }

    private static boolean flatNMatcher(REGlobalData gData, int matchChars, int length, String input, int end) {
        if (gData.cp + length > end) {
            return false;
        } else {
            for (int i = 0; i < length; i++) {
                if (gData.regexp.source[matchChars + i] != input.charAt(gData.cp + i)) {
                    return false;
                }
            }
            gData.cp += length;
            return true;
        }
    }

    private static boolean flatNIMatcher(REGlobalData gData, int matchChars, int length, String input, int end) {
        if (gData.cp + length > end) {
            return false;
        } else {
            char[] source = gData.regexp.source;
            for (int i = 0; i < length; i++) {
                char c1 = source[matchChars + i];
                char c2 = input.charAt(gData.cp + i);
                if (c1 != c2 && upcase(c1) != upcase(c2)) {
                    return false;
                }
            }
            gData.cp += length;
            return true;
        }
    }

    private static boolean backrefMatcher(REGlobalData gData, int parenIndex, String input, int end) {
        if (gData.parens != null && parenIndex < gData.parens.length) {
            int parenContent = gData.parensIndex(parenIndex);
            if (parenContent == -1) {
                return true;
            } else {
                int len = gData.parensLength(parenIndex);
                if (gData.cp + len > end) {
                    return false;
                } else {
                    if ((gData.regexp.flags & 2) != 0) {
                        for (int i = 0; i < len; i++) {
                            char c1 = input.charAt(parenContent + i);
                            char c2 = input.charAt(gData.cp + i);
                            if (c1 != c2 && upcase(c1) != upcase(c2)) {
                                return false;
                            }
                        }
                    } else if (!input.regionMatches(parenContent, input, gData.cp, len)) {
                        return false;
                    }
                    gData.cp += len;
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    private static void addCharacterToCharSet(RECharSet cs, char c, Context cx) {
        int byteIndex = c / '\b';
        if (c >= cs.length) {
            throw ScriptRuntime.constructError(cx, "SyntaxError", "invalid range in character class");
        } else {
            cs.bits[byteIndex] = (byte) (cs.bits[byteIndex] | 1 << (c & 7));
        }
    }

    private static void addCharacterRangeToCharSet(RECharSet cs, char c1, char c2, Context cx) {
        int byteIndex1 = c1 / '\b';
        int byteIndex2 = c2 / '\b';
        if (c2 < cs.length && c1 <= c2) {
            c1 = (char) (c1 & 7);
            c2 = (char) (c2 & 7);
            if (byteIndex1 == byteIndex2) {
                cs.bits[byteIndex1] = (byte) (cs.bits[byteIndex1] | 255 >> 7 - (c2 - c1) << c1);
            } else {
                cs.bits[byteIndex1] = (byte) (cs.bits[byteIndex1] | 255 << c1);
                for (int i = byteIndex1 + 1; i < byteIndex2; i++) {
                    cs.bits[i] = -1;
                }
                cs.bits[byteIndex2] = (byte) (cs.bits[byteIndex2] | 255 >> 7 - c2);
            }
        } else {
            throw ScriptRuntime.constructError(cx, "SyntaxError", "invalid range in character class");
        }
    }

    private static void processCharSet(REGlobalData gData, RECharSet charSet, Context cx) {
        synchronized (charSet) {
            if (!charSet.converted) {
                processCharSetImpl(gData, charSet, cx);
                charSet.converted = true;
            }
        }
    }

    private static void processCharSetImpl(REGlobalData gData, RECharSet charSet, Context cx) {
        int src = charSet.startIndex;
        int end = src + charSet.strlength;
        char rangeStart = 0;
        boolean inRange = false;
        int byteLength = (charSet.length + 7) / 8;
        charSet.bits = new byte[byteLength];
        if (src != end) {
            if (gData.regexp.source[src] == '^') {
                assert !charSet.sense;
                src++;
            } else {
                assert charSet.sense;
            }
            label197: while (src != end) {
                int nDigits = 2;
                char thisCh;
                if (gData.regexp.source[src] == '\\') {
                    int var10001 = ++src;
                    src++;
                    char c = gData.regexp.source[var10001];
                    switch(c) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                            int n = c - '0';
                            c = gData.regexp.source[src];
                            if ('0' <= c && c <= '7') {
                                src++;
                                n = 8 * n + (c - '0');
                                c = gData.regexp.source[src];
                                if ('0' <= c && c <= '7') {
                                    src++;
                                    int i = 8 * n + (c - '0');
                                    if (i <= 255) {
                                        n = i;
                                    } else {
                                        src--;
                                    }
                                }
                            }
                            thisCh = (char) n;
                            break;
                        case '8':
                        case '9':
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
                        case 'E':
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
                        case 'T':
                        case 'U':
                        case 'V':
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
                        case 'e':
                        case 'g':
                        case 'h':
                        case 'i':
                        case 'j':
                        case 'k':
                        case 'l':
                        case 'm':
                        case 'o':
                        case 'p':
                        case 'q':
                        default:
                            thisCh = c;
                            break;
                        case 'D':
                            if (inRange) {
                                addCharacterToCharSet(charSet, '-', cx);
                                inRange = false;
                            }
                            addCharacterRangeToCharSet(charSet, '\u0000', '/', cx);
                            addCharacterRangeToCharSet(charSet, ':', (char) (charSet.length - 1), cx);
                            continue;
                        case 'S':
                            if (inRange) {
                                addCharacterToCharSet(charSet, '-', cx);
                                inRange = false;
                            }
                            int i = charSet.length - 1;
                            while (true) {
                                if (i < 0) {
                                    continue label197;
                                }
                                if (!isREWhiteSpace(i)) {
                                    addCharacterToCharSet(charSet, (char) i, cx);
                                }
                                i--;
                            }
                        case 'W':
                            if (inRange) {
                                addCharacterToCharSet(charSet, '-', cx);
                                inRange = false;
                            }
                            int i = charSet.length - 1;
                            while (true) {
                                if (i < 0) {
                                    continue label197;
                                }
                                if (!isWord((char) i)) {
                                    addCharacterToCharSet(charSet, (char) i, cx);
                                }
                                i--;
                            }
                        case 'b':
                            thisCh = '\b';
                            break;
                        case 'c':
                            if (src < end && isControlLetter(gData.regexp.source[src])) {
                                thisCh = (char) (gData.regexp.source[src++] & 31);
                            } else {
                                src--;
                                thisCh = '\\';
                            }
                            break;
                        case 'd':
                            if (inRange) {
                                addCharacterToCharSet(charSet, '-', cx);
                                inRange = false;
                            }
                            addCharacterRangeToCharSet(charSet, '0', '9', cx);
                            continue;
                        case 'f':
                            thisCh = '\f';
                            break;
                        case 'n':
                            thisCh = '\n';
                            break;
                        case 'r':
                            thisCh = '\r';
                            break;
                        case 's':
                            if (inRange) {
                                addCharacterToCharSet(charSet, '-', cx);
                                inRange = false;
                            }
                            int i = charSet.length - 1;
                            while (true) {
                                if (i < 0) {
                                    continue label197;
                                }
                                if (isREWhiteSpace(i)) {
                                    addCharacterToCharSet(charSet, (char) i, cx);
                                }
                                i--;
                            }
                        case 't':
                            thisCh = '\t';
                            break;
                        case 'u':
                            nDigits += 2;
                        case 'x':
                            int n = 0;
                            for (int i = 0; i < nDigits && src < end; i++) {
                                c = gData.regexp.source[src++];
                                int digit = toASCIIHexDigit(c);
                                if (digit < 0) {
                                    src -= i + 1;
                                    n = 92;
                                    break;
                                }
                                n = n << 4 | digit;
                            }
                            thisCh = (char) n;
                            break;
                        case 'v':
                            thisCh = 11;
                            break;
                        case 'w':
                            if (inRange) {
                                addCharacterToCharSet(charSet, '-', cx);
                                inRange = false;
                            }
                            for (int i = charSet.length - 1; i >= 0; i--) {
                                if (isWord((char) i)) {
                                    addCharacterToCharSet(charSet, (char) i, cx);
                                }
                            }
                            continue;
                    }
                } else {
                    thisCh = gData.regexp.source[src++];
                }
                if (!inRange) {
                    if ((gData.regexp.flags & 2) != 0) {
                        addCharacterToCharSet(charSet, upcase(thisCh), cx);
                        addCharacterToCharSet(charSet, downcase(thisCh), cx);
                    } else {
                        addCharacterToCharSet(charSet, thisCh, cx);
                    }
                    if (src < end - 1 && gData.regexp.source[src] == '-') {
                        src++;
                        inRange = true;
                        rangeStart = thisCh;
                    }
                } else {
                    if ((gData.regexp.flags & 2) != 0) {
                        assert rangeStart <= thisCh;
                        char c = rangeStart;
                        while (c <= thisCh) {
                            addCharacterToCharSet(charSet, c, cx);
                            char uch = upcase(c);
                            char dch = downcase(c);
                            if (c != uch) {
                                addCharacterToCharSet(charSet, uch, cx);
                            }
                            if (c != dch) {
                                addCharacterToCharSet(charSet, dch, cx);
                            }
                            if (++c == 0) {
                                break;
                            }
                        }
                    } else {
                        addCharacterRangeToCharSet(charSet, rangeStart, thisCh, cx);
                    }
                    inRange = false;
                }
            }
        }
    }

    private static boolean classMatcher(REGlobalData gData, RECharSet charSet, char ch, Context cx) {
        if (!charSet.converted) {
            processCharSet(gData, charSet, cx);
        }
        int byteIndex = ch >> 3;
        return (charSet.length == 0 || ch >= charSet.length || (charSet.bits[byteIndex] & 1 << (ch & 7)) == 0) ^ charSet.sense;
    }

    private static boolean reopIsSimple(int op) {
        return op >= 1 && op <= 23;
    }

    private static int simpleMatch(REGlobalData gData, String input, int op, byte[] program, int pc, int end, boolean updatecp, Context cx) {
        boolean result = false;
        int startcp = gData.cp;
        switch(op) {
            case 1:
                result = true;
                break;
            case 2:
                if (gData.cp == 0 || gData.multiline && isLineTerm(input.charAt(gData.cp - 1))) {
                    result = true;
                }
                break;
            case 3:
                if (gData.cp == end || gData.multiline && isLineTerm(input.charAt(gData.cp))) {
                    result = true;
                }
                break;
            case 4:
                result = (gData.cp == 0 || !isWord(input.charAt(gData.cp - 1))) ^ (gData.cp >= end || !isWord(input.charAt(gData.cp)));
                break;
            case 5:
                result = (gData.cp == 0 || !isWord(input.charAt(gData.cp - 1))) ^ (gData.cp < end && isWord(input.charAt(gData.cp)));
                break;
            case 6:
                if (gData.cp != end && !isLineTerm(input.charAt(gData.cp))) {
                    result = true;
                    gData.cp++;
                }
                break;
            case 7:
                if (gData.cp != end && isDigit(input.charAt(gData.cp))) {
                    result = true;
                    gData.cp++;
                }
                break;
            case 8:
                if (gData.cp != end && !isDigit(input.charAt(gData.cp))) {
                    result = true;
                    gData.cp++;
                }
                break;
            case 9:
                if (gData.cp != end && isWord(input.charAt(gData.cp))) {
                    result = true;
                    gData.cp++;
                }
                break;
            case 10:
                if (gData.cp != end && !isWord(input.charAt(gData.cp))) {
                    result = true;
                    gData.cp++;
                }
                break;
            case 11:
                if (gData.cp != end && isREWhiteSpace(input.charAt(gData.cp))) {
                    result = true;
                    gData.cp++;
                }
                break;
            case 12:
                if (gData.cp != end && !isREWhiteSpace(input.charAt(gData.cp))) {
                    result = true;
                    gData.cp++;
                }
                break;
            case 13:
                int parenIndex = getIndex(program, pc);
                pc += 2;
                result = backrefMatcher(gData, parenIndex, input, end);
                break;
            case 14:
                {
                    int offset = getIndex(program, pc);
                    pc += 2;
                    int length = getIndex(program, pc);
                    pc += 2;
                    result = flatNMatcher(gData, offset, length, input, end);
                    break;
                }
            case 15:
                char matchChx = (char) (program[pc++] & 255);
                if (gData.cp != end && input.charAt(gData.cp) == matchChx) {
                    result = true;
                    gData.cp++;
                }
                break;
            case 16:
                {
                    int offset = getIndex(program, pc);
                    pc += 2;
                    int length = getIndex(program, pc);
                    pc += 2;
                    result = flatNIMatcher(gData, offset, length, input, end);
                    break;
                }
            case 17:
                char matchChxxx = (char) (program[pc++] & 255);
                if (gData.cp != end) {
                    char c = input.charAt(gData.cp);
                    if (matchChxxx == c || upcase(matchChxxx) == upcase(c)) {
                        result = true;
                        gData.cp++;
                    }
                }
                break;
            case 18:
                char matchCh = (char) getIndex(program, pc);
                pc += 2;
                if (gData.cp != end && input.charAt(gData.cp) == matchCh) {
                    result = true;
                    gData.cp++;
                }
                break;
            case 19:
                char matchChxx = (char) getIndex(program, pc);
                pc += 2;
                if (gData.cp != end) {
                    char c = input.charAt(gData.cp);
                    if (matchChxx == c || upcase(matchChxx) == upcase(c)) {
                        result = true;
                        gData.cp++;
                    }
                }
                break;
            case 20:
            case 21:
            default:
                throw Kit.codeBug();
            case 22:
            case 23:
                int index = getIndex(program, pc);
                pc += 2;
                if (gData.cp != end && classMatcher(gData, gData.regexp.classList[index], input.charAt(gData.cp), cx)) {
                    gData.cp++;
                    result = true;
                }
        }
        if (result) {
            if (!updatecp) {
                gData.cp = startcp;
            }
            return pc;
        } else {
            gData.cp = startcp;
            return -1;
        }
    }

    private static boolean executeREBytecode(REGlobalData gData, String input, int end, Context cx) {
        int pc = 0;
        byte[] program = gData.regexp.program;
        int continuationOp = 57;
        int continuationPc = 0;
        boolean result = false;
        int op = program[pc++];
        if (gData.regexp.anchorCh < 0 && reopIsSimple(op)) {
            boolean anchor;
            for (anchor = false; gData.cp <= end; gData.cp++) {
                int match = simpleMatch(gData, input, op, program, pc, end, true, cx);
                if (match >= 0) {
                    anchor = true;
                    pc = match + 1;
                    op = program[match];
                    break;
                }
                gData.skipped++;
            }
            if (!anchor) {
                return false;
            }
        }
        while (true) {
            if (reopIsSimple(op)) {
                int match = simpleMatch(gData, input, op, program, pc, end, true, cx);
                result = match >= 0;
                if (result) {
                    pc = match;
                }
            } else {
                label212: switch(op) {
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                        boolean greedy = false;
                        int min;
                        int max;
                        switch(op) {
                            case 25:
                                greedy = true;
                            case 48:
                                min = getOffset(program, pc);
                                pc += 2;
                                max = getOffset(program, pc) - 1;
                                pc += 2;
                                break;
                            case 26:
                                greedy = true;
                            case 45:
                                min = 0;
                                max = -1;
                                break;
                            case 27:
                                greedy = true;
                            case 46:
                                min = 1;
                                max = -1;
                                break;
                            case 28:
                                greedy = true;
                            case 47:
                                min = 0;
                                max = 1;
                                break;
                            case 29:
                            case 30:
                            case 31:
                            case 32:
                            case 33:
                            case 34:
                            case 35:
                            case 36:
                            case 37:
                            case 38:
                            case 39:
                            case 40:
                            case 41:
                            case 42:
                            case 43:
                            case 44:
                            default:
                                throw Kit.codeBug();
                        }
                        pushProgState(gData, min, max, gData.cp, null, continuationOp, continuationPc);
                        if (greedy) {
                            pushBackTrackState(gData, (byte) 51, pc);
                            continuationOp = 51;
                            continuationPc = pc;
                            pc += 6;
                            op = program[pc++];
                        } else if (min != 0) {
                            continuationOp = 52;
                            continuationPc = pc;
                            pc += 6;
                            op = program[pc++];
                        } else {
                            pushBackTrackState(gData, (byte) 52, pc);
                            popProgState(gData);
                            int var40 = pc + 4;
                            pc = var40 + getOffset(program, var40);
                            op = program[pc++];
                        }
                        continue;
                    case 29:
                        {
                            int parenIndex = getIndex(program, pc);
                            pc += 2;
                            gData.setParens(parenIndex, gData.cp, 0);
                            op = program[pc++];
                            continue;
                        }
                    case 30:
                        {
                            int parenIndex = getIndex(program, pc);
                            pc += 2;
                            int cap_index = gData.parensIndex(parenIndex);
                            gData.setParens(parenIndex, cap_index, gData.cp - cap_index);
                            op = program[pc++];
                            continue;
                        }
                    case 32:
                        int offset = getOffset(program, pc);
                        pc += offset;
                        op = program[pc++];
                        continue;
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 50:
                    case 56:
                    default:
                        throw Kit.codeBug("invalid bytecode");
                    case 41:
                        int nextpc = pc + getIndex(program, pc);
                        pc += 2;
                        op = program[pc++];
                        if (!reopIsSimple(op) || simpleMatch(gData, input, op, program, pc, end, false, cx) >= 0) {
                            pushProgState(gData, 0, 0, gData.cp, gData.backTrackStackTop, continuationOp, continuationPc);
                            pushBackTrackState(gData, (byte) 43, nextpc);
                            continue;
                        }
                        result = false;
                        break;
                    case 42:
                        int nextpc = pc + getIndex(program, pc);
                        pc += 2;
                        op = program[pc++];
                        if (reopIsSimple(op)) {
                            int match = simpleMatch(gData, input, op, program, pc, end, false, cx);
                            if (match >= 0 && program[match] == 44) {
                                result = false;
                                break;
                            }
                        }
                        pushProgState(gData, 0, 0, gData.cp, gData.backTrackStackTop, continuationOp, continuationPc);
                        pushBackTrackState(gData, (byte) 44, nextpc);
                        continue;
                    case 43:
                    case 44:
                        REProgState state = popProgState(gData);
                        gData.cp = state.index;
                        gData.backTrackStackTop = state.backTrack;
                        continuationPc = state.continuationPc;
                        continuationOp = state.continuationOp;
                        if (op == 44) {
                            result = !result;
                        }
                        break;
                    case 49:
                        result = true;
                        pc = continuationPc;
                        op = continuationOp;
                        continue;
                    case 51:
                        int nextpc;
                        do {
                            REProgState statexx = popProgState(gData);
                            if (!result) {
                                if (statexx.min == 0) {
                                    result = true;
                                }
                                continuationPc = statexx.continuationPc;
                                continuationOp = statexx.continuationOp;
                                pc += 4;
                                pc += getOffset(program, pc);
                                break label212;
                            }
                            if (statexx.min == 0 && gData.cp == statexx.index) {
                                result = false;
                                continuationPc = statexx.continuationPc;
                                continuationOp = statexx.continuationOp;
                                pc += 4;
                                pc += getOffset(program, pc);
                                break label212;
                            }
                            int new_minx = statexx.min;
                            int new_maxx = statexx.max;
                            if (new_minx != 0) {
                                new_minx--;
                            }
                            if (new_maxx != -1) {
                                new_maxx--;
                            }
                            if (new_maxx == 0) {
                                result = true;
                                continuationPc = statexx.continuationPc;
                                continuationOp = statexx.continuationOp;
                                pc += 4;
                                pc += getOffset(program, pc);
                                break label212;
                            }
                            nextpc = pc + 6;
                            int nextop = program[nextpc];
                            int startcp = gData.cp;
                            if (reopIsSimple(nextop)) {
                                int match = simpleMatch(gData, input, nextop, program, ++nextpc, end, true, cx);
                                if (match < 0) {
                                    result = new_minx == 0;
                                    continuationPc = statexx.continuationPc;
                                    continuationOp = statexx.continuationOp;
                                    pc += 4;
                                    pc += getOffset(program, pc);
                                    break label212;
                                }
                                result = true;
                                nextpc = match;
                            }
                            continuationOp = 51;
                            continuationPc = pc;
                            pushProgState(gData, new_minx, new_maxx, startcp, null, statexx.continuationOp, statexx.continuationPc);
                            if (new_minx == 0) {
                                pushBackTrackState(gData, (byte) 51, pc, startcp, statexx.continuationOp, statexx.continuationPc);
                                int parenCount = getIndex(program, pc);
                                int parenIndexx = getIndex(program, pc + 2);
                                for (int k = 0; k < parenCount; k++) {
                                    gData.setParens(parenIndexx + k, -1, 0);
                                }
                            }
                        } while (program[nextpc] == 49);
                        pc = nextpc + 1;
                        op = program[nextpc];
                        continue;
                    case 52:
                        REProgState statex = popProgState(gData);
                        if (!result) {
                            if (statex.max != -1 && statex.max <= 0) {
                                continuationPc = statex.continuationPc;
                                continuationOp = statex.continuationOp;
                                break;
                            }
                            pushProgState(gData, statex.min, statex.max, gData.cp, null, statex.continuationOp, statex.continuationPc);
                            continuationOp = 52;
                            continuationPc = pc;
                            int parenCount = getIndex(program, pc);
                            pc += 2;
                            int parenIndex = getIndex(program, pc);
                            pc += 4;
                            for (int k = 0; k < parenCount; k++) {
                                gData.setParens(parenIndex + k, -1, 0);
                            }
                            op = program[pc++];
                        } else {
                            if (statex.min == 0 && gData.cp == statex.index) {
                                result = false;
                                continuationPc = statex.continuationPc;
                                continuationOp = statex.continuationOp;
                                break;
                            }
                            int new_min = statex.min;
                            int new_max = statex.max;
                            if (new_min != 0) {
                                new_min--;
                            }
                            if (new_max != -1) {
                                new_max--;
                            }
                            pushProgState(gData, new_min, new_max, gData.cp, null, statex.continuationOp, statex.continuationPc);
                            if (new_min == 0) {
                                continuationPc = statex.continuationPc;
                                continuationOp = statex.continuationOp;
                                pushBackTrackState(gData, (byte) 52, pc);
                                popProgState(gData);
                                int var24 = pc + 4;
                                pc = var24 + getOffset(program, var24);
                                op = program[pc++];
                                continue;
                            }
                            continuationOp = 52;
                            continuationPc = pc;
                            int parenCount = getIndex(program, pc);
                            pc += 2;
                            int parenIndex = getIndex(program, pc);
                            pc += 4;
                            for (int k = 0; k < parenCount; k++) {
                                gData.setParens(parenIndex + k, -1, 0);
                            }
                            op = program[pc++];
                        }
                        continue;
                    case 53:
                    case 54:
                    case 55:
                        char matchCh1 = (char) getIndex(program, pc);
                        pc += 2;
                        char matchCh2 = (char) getIndex(program, pc);
                        pc += 2;
                        if (gData.cp == end) {
                            result = false;
                            break;
                        } else {
                            char c = input.charAt(gData.cp);
                            if (op == 55) {
                                if (c != matchCh1 && !classMatcher(gData, gData.regexp.classList[matchCh2], c, cx)) {
                                    result = false;
                                    break;
                                }
                            } else {
                                if (op == 54) {
                                    c = upcase(c);
                                }
                                if (c != matchCh1 && c != matchCh2) {
                                    result = false;
                                    break;
                                }
                            }
                        }
                    case 31:
                        int nextpc = pc + getOffset(program, pc);
                        pc += 2;
                        op = program[pc++];
                        int startcp = gData.cp;
                        if (reopIsSimple(op)) {
                            int match = simpleMatch(gData, input, op, program, pc, end, true, cx);
                            if (match < 0) {
                                op = program[nextpc++];
                                pc = nextpc;
                                continue;
                            }
                            result = true;
                            pc = match + 1;
                            op = program[match];
                        }
                        byte nextop = program[nextpc++];
                        pushBackTrackState(gData, nextop, nextpc, startcp, continuationOp, continuationPc);
                        continue;
                    case 57:
                        return true;
                }
            }
            if (!result) {
                REBackTrackData backTrackData = gData.backTrackStackTop;
                if (backTrackData == null) {
                    return false;
                }
                gData.backTrackStackTop = backTrackData.previous;
                gData.parens = backTrackData.parens;
                gData.cp = backTrackData.cp;
                gData.stateStackTop = backTrackData.stateStackTop;
                continuationOp = backTrackData.continuationOp;
                continuationPc = backTrackData.continuationPc;
                pc = backTrackData.pc;
                op = backTrackData.op;
            } else {
                op = program[pc++];
            }
        }
    }

    private static boolean matchRegExp(REGlobalData gData, RECompiled re, String input, int start, int end, boolean multiline, Context cx) {
        if (re.parenCount != 0) {
            gData.parens = new long[re.parenCount];
        } else {
            gData.parens = null;
        }
        gData.backTrackStackTop = null;
        gData.stateStackTop = null;
        gData.multiline = multiline || (re.flags & 4) != 0;
        gData.regexp = re;
        int anchorCh = gData.regexp.anchorCh;
        for (int i = start; i <= end; i++) {
            if (anchorCh >= 0) {
                while (true) {
                    if (i == end) {
                        return false;
                    }
                    char matchCh = input.charAt(i);
                    if (matchCh == anchorCh || (gData.regexp.flags & 2) != 0 && upcase(matchCh) == upcase((char) anchorCh)) {
                        break;
                    }
                    i++;
                }
            }
            gData.cp = i;
            gData.skipped = i - start;
            for (int j = 0; j < re.parenCount; j++) {
                gData.parens[j] = -1L;
            }
            boolean result = executeREBytecode(gData, input, end, cx);
            gData.backTrackStackTop = null;
            gData.stateStackTop = null;
            if (result) {
                return true;
            }
            if (anchorCh == -2 && !gData.multiline) {
                gData.skipped = end;
                return false;
            }
            i = start + gData.skipped;
        }
        return false;
    }

    private static void reportError(String messageId, String arg, Context cx) {
        String msg = ScriptRuntime.getMessage1(messageId, arg);
        throw ScriptRuntime.constructError(cx, "SyntaxError", msg);
    }

    private static NativeRegExp realThis(Scriptable thisObj, IdFunctionObject f, Context cx) {
        if (!(thisObj instanceof NativeRegExp)) {
            throw incompatibleCallError(f, cx);
        } else {
            return (NativeRegExp) thisObj;
        }
    }

    NativeRegExp(Scriptable scope, RECompiled regexpCompiled, Context cx) {
        this.re = regexpCompiled;
        this.setLastIndex(ScriptRuntime.zeroObj, cx);
        ScriptRuntime.setBuiltinProtoAndParent(cx, scope, this, TopLevel.Builtins.RegExp);
    }

    NativeRegExp() {
    }

    @Override
    public String getClassName() {
        return "RegExp";
    }

    @Override
    public MemberType getTypeOf() {
        return MemberType.OBJECT;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        throw ScriptRuntime.notFunctionError(cx, thisObj);
    }

    @Override
    public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
        throw ScriptRuntime.notFunctionError(cx, this);
    }

    Scriptable compile(Context cx, Scriptable scope, Object[] args) {
        if (args.length <= 0 || !(args[0] instanceof NativeRegExp thatObj)) {
            String s = args.length != 0 && !(args[0] instanceof Undefined) ? escapeRegExp(cx, args[0]) : "";
            String global = args.length > 1 && args[1] != Undefined.instance ? ScriptRuntime.toString(cx, args[1]) : null;
            this.re = compileRE(cx, s, global, false);
            this.setLastIndex(ScriptRuntime.zeroObj, cx);
            return this;
        } else if (args.length > 1 && args[1] != Undefined.instance) {
            throw ScriptRuntime.typeError0(cx, "msg.bad.regexp.compile");
        } else {
            this.re = thatObj.re;
            this.setLastIndex(thatObj.lastIndex, cx);
            return this;
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('/');
        if (this.re.source.length != 0) {
            buf.append(this.re.source);
        } else {
            buf.append("(?:)");
        }
        buf.append('/');
        if ((this.re.flags & 1) != 0) {
            buf.append('g');
        }
        if ((this.re.flags & 2) != 0) {
            buf.append('i');
        }
        if ((this.re.flags & 4) != 0) {
            buf.append('m');
        }
        return buf.toString();
    }

    private Object execSub(Context cx, Scriptable scopeObj, Object[] args, int matchType) {
        RegExp reImpl = cx.getRegExp();
        String str;
        if (args.length == 0) {
            str = reImpl.input;
            if (str == null) {
                str = ScriptRuntime.toString(cx, Undefined.instance);
            }
        } else {
            str = ScriptRuntime.toString(cx, args[0]);
        }
        double d = 0.0;
        if ((this.re.flags & 1) != 0) {
            d = ScriptRuntime.toInteger(cx, this.lastIndex);
        }
        Object rval;
        if (!(d < 0.0) && !((double) str.length() < d)) {
            int[] indexp = new int[] { (int) d };
            rval = this.executeRegExp(cx, scopeObj, reImpl, str, indexp, matchType);
            if ((this.re.flags & 1) != 0) {
                if (rval != null && rval != Undefined.instance) {
                    this.setLastIndex((double) indexp[0], cx);
                } else {
                    this.setLastIndex(ScriptRuntime.zeroObj, cx);
                }
            }
        } else {
            this.setLastIndex(ScriptRuntime.zeroObj, cx);
            rval = null;
        }
        return rval;
    }

    Object executeRegExp(Context cx, Scriptable scope, RegExp res, String str, int[] indexp, int matchType) {
        REGlobalData gData = new REGlobalData();
        int start = indexp[0];
        int end = str.length();
        if (start > end) {
            start = end;
        }
        boolean matches = matchRegExp(gData, this.re, str, start, end, res.multiline, cx);
        if (!matches) {
            return matchType != 2 ? null : Undefined.instance;
        } else {
            int index = gData.cp;
            int ep = indexp[0] = index;
            int matchlen = ep - (start + gData.skipped);
            index -= matchlen;
            Object result;
            Scriptable obj;
            if (matchType == 0) {
                result = Boolean.TRUE;
                obj = null;
            } else {
                result = cx.newArray(scope, 0);
                obj = (Scriptable) result;
                String matchstr = str.substring(index, index + matchlen);
                obj.put(cx, 0, obj, matchstr);
            }
            if (this.re.parenCount == 0) {
                res.parens = null;
                res.lastParen = new SubString();
            } else {
                SubString parsub = null;
                res.parens = new SubString[this.re.parenCount];
                for (int num = 0; num < this.re.parenCount; num++) {
                    int cap_index = gData.parensIndex(num);
                    if (cap_index != -1) {
                        int cap_length = gData.parensLength(num);
                        parsub = new SubString(str, cap_index, cap_length);
                        res.parens[num] = parsub;
                        if (matchType != 0) {
                            obj.put(cx, num + 1, obj, parsub.toString());
                        }
                    } else if (matchType != 0) {
                        obj.put(cx, num + 1, obj, Undefined.instance);
                    }
                }
                res.lastParen = parsub;
            }
            if (matchType != 0) {
                obj.put(cx, "index", obj, start + gData.skipped);
                obj.put(cx, "input", obj, str);
            }
            if (res.lastMatch == null) {
                res.lastMatch = new SubString();
                res.leftContext = new SubString();
                res.rightContext = new SubString();
            }
            res.lastMatch.str = str;
            res.lastMatch.index = index;
            res.lastMatch.length = matchlen;
            res.leftContext.str = str;
            res.leftContext.index = 0;
            res.leftContext.length = start + gData.skipped;
            res.rightContext.str = str;
            res.rightContext.index = ep;
            res.rightContext.length = end - ep;
            return result;
        }
    }

    int getFlags() {
        return this.re.flags;
    }

    @Override
    protected int getMaxInstanceId() {
        return 5;
    }

    @Override
    protected int findInstanceIdInfo(String s, Context cx) {
        int id = switch(s) {
            case "lastIndex" ->
                1;
            case "source" ->
                2;
            case "global" ->
                3;
            case "ignoreCase" ->
                4;
            case "multiline" ->
                5;
            default ->
                0;
        };
        if (id == 0) {
            return super.findInstanceIdInfo(s, cx);
        } else {
            int attr = switch(id) {
                case 1 ->
                    this.lastIndexAttr;
                case 2, 3, 4, 5 ->
                    7;
                default ->
                    throw new IllegalStateException();
            };
            return instanceIdInfo(attr, id);
        }
    }

    @Override
    protected String getInstanceIdName(int id) {
        return switch(id) {
            case 1 ->
                "lastIndex";
            case 2 ->
                "source";
            case 3 ->
                "global";
            case 4 ->
                "ignoreCase";
            case 5 ->
                "multiline";
            default ->
                super.getInstanceIdName(id);
        };
    }

    @Override
    protected Object getInstanceIdValue(int id, Context cx) {
        return switch(id) {
            case 1 ->
                this.lastIndex;
            case 2 ->
                new String(this.re.source);
            case 3 ->
                (this.re.flags & 1) != 0;
            case 4 ->
                (this.re.flags & 2) != 0;
            case 5 ->
                (this.re.flags & 4) != 0;
            default ->
                super.getInstanceIdValue(id, cx);
        };
    }

    private void setLastIndex(Object value, Context cx) {
        if ((this.lastIndexAttr & 1) != 0) {
            throw ScriptRuntime.typeError1(cx, "msg.modify.readonly", "lastIndex");
        } else {
            this.lastIndex = value;
        }
    }

    @Override
    protected void setInstanceIdValue(int id, Object value, Context cx) {
        switch(id) {
            case 1:
                this.setLastIndex(value, cx);
                return;
            case 2:
            case 3:
            case 4:
            case 5:
                return;
            default:
                super.setInstanceIdValue(id, value, cx);
        }
    }

    @Override
    protected void setInstanceIdAttributes(int id, int attr, Context cx) {
        if (id == 1) {
            this.lastIndexAttr = attr;
        } else {
            super.setInstanceIdAttributes(id, attr, cx);
        }
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        if (id == 7) {
            this.initPrototypeMethod(REGEXP_TAG, id, SymbolKey.MATCH, "[Symbol.match]", 1, cx);
        } else if (id == 8) {
            this.initPrototypeMethod(REGEXP_TAG, id, SymbolKey.SEARCH, "[Symbol.search]", 1, cx);
        } else {
            String s;
            int arity;
            switch(id) {
                case 1:
                    arity = 2;
                    s = "compile";
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
                    arity = 1;
                    s = "exec";
                    break;
                case 5:
                    arity = 1;
                    s = "test";
                    break;
                case 6:
                    arity = 1;
                    s = "prefix";
                    break;
                default:
                    throw new IllegalArgumentException(String.valueOf(id));
            }
            this.initPrototypeMethod(REGEXP_TAG, id, s, arity, cx);
        }
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(REGEXP_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            switch(id) {
                case 1:
                    return realThis(thisObj, f, cx).compile(cx, scope, args);
                case 2:
                case 3:
                    return realThis(thisObj, f, cx).toString();
                case 4:
                    return realThis(thisObj, f, cx).execSub(cx, scope, args, 1);
                case 5:
                    Object x = realThis(thisObj, f, cx).execSub(cx, scope, args, 0);
                    return Boolean.TRUE.equals(x) ? Boolean.TRUE : Boolean.FALSE;
                case 6:
                    return realThis(thisObj, f, cx).execSub(cx, scope, args, 2);
                case 7:
                    return realThis(thisObj, f, cx).execSub(cx, scope, args, 1);
                case 8:
                    Scriptable scriptable = (Scriptable) realThis(thisObj, f, cx).execSub(cx, scope, args, 1);
                    return scriptable.get(cx, "index", scriptable);
                default:
                    throw new IllegalArgumentException(String.valueOf(id));
            }
        }
    }

    @Override
    protected int findPrototypeId(Symbol k) {
        if (SymbolKey.MATCH.equals(k)) {
            return 7;
        } else {
            return SymbolKey.SEARCH.equals(k) ? 8 : 0;
        }
    }

    @Override
    protected int findPrototypeId(String s) {
        return switch(s) {
            case "compile" ->
                1;
            case "toString" ->
                2;
            case "toSource" ->
                3;
            case "exec" ->
                4;
            case "test" ->
                5;
            case "prefix" ->
                6;
            default ->
                0;
        };
    }
}